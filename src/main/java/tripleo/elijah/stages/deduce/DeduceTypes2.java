/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialFunctions;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.gen_fn.BaseTableEntry;
import tripleo.elijah.stages.gen_fn.ConstantTableEntry;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.ConstTableIA;
import tripleo.elijah.stages.instructions.FnCallArgs;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created 9/15/20 12:51 PM
 */
public class DeduceTypes2 {
	private final OS_Module module;
	private final DeducePhase phase;
	private final ErrSink errSink;

	public DeduceTypes2(OS_Module module, DeducePhase phase) {
		this.module = module;
		this.phase = phase;
		this.errSink = module.parent.eee;
	}

	public void deduceFunctions(final Iterable<GeneratedNode> lgf) {
		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				deduce_generated_function(generatedFunction);
				phase.addFunction(generatedFunction, generatedFunction.getFD());
			}
		}
	}

	public void deduce_generated_function(final GeneratedFunction generatedFunction) {
		final FunctionDef fd = generatedFunction.getFD();
		final Context fd_ctx = fd.getContext();
		//
		System.err.println("** deduce_generated_function "+ fd.name()+" "+fd);//+" "+((OS_Container)((FunctionDef)fd).getParent()).name());
		//
		for (final Instruction instruction : generatedFunction.instructions()) {
			final Context context = generatedFunction.getContextFromPC(instruction.getIndex());
//			System.out.println("8006 " + instruction);
			switch (instruction.getName()) {
			case E:
				{
					//
					// resolve all cte expressions
					//
					for (final ConstantTableEntry cte : generatedFunction.cte_list) {
						final IExpression iv = cte.initialValue;
						switch (iv.getKind()) {
						case NUMERIC:
							{
								final OS_Type a = cte.getTypeTableEntry().attached;
								if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
									try {
										cte.getTypeTableEntry().attached = resolve_type(new OS_Type(BuiltInTypes.SystemInteger), context);
									} catch (ResolveError resolveError) {
										System.out.println("71 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
									}
								}
								break;
							}
						case IDENT:
							{
								final OS_Type a = cte.getTypeTableEntry().attached;
								assert a != null;
								assert a.getType() != null;
								if (a.getType() == OS_Type.Type.BUILT_IN && a.getBType() == BuiltInTypes.Boolean) {
									assert BuiltInTypes.isBooleanText(cte.getName());
								} else
									throw new NotImplementedException();
								break;
							}
						default:
							{
								System.err.println("8192 "+iv.getKind());
								throw new NotImplementedException();
							}
						}
					}
					//
					// resolve ident table
					//
					for (IdentTableEntry ite : generatedFunction.idte_list) {
						if (ite.resolved_element != null)
							continue;
						final IdentIA identIA = new IdentIA(ite.getIndex(), generatedFunction);
						resolveIdentIA_(ite.getPC(), identIA, generatedFunction, new FoundElement(phase) {

							final String x = generatedFunction.getIdentIAPathNormal(identIA);

							@Override
							void foundElement(OS_Element e) {
								ite.setStatus(BaseTableEntry.Status.KNOWN, e);
								found_element_for_ite(generatedFunction, ite, e, context);
							}

							@Override
							void noFoundElement() {
								ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
								errSink.reportError("1004 Can't find element for "+ x);
							}
						});
					}
				}
				break;
			case X:
				{
					//
					// ATTACH A TYPE TO VTE'S
					// CONVERT USER TYPES TO USER_CLASS TYPES
					//
					for (final VariableTableEntry vte : generatedFunction.vte_list) {
//						System.out.println("704 "+vte.type.attached+" "+vte.potentialTypes());
						if (vte.type.attached != null && vte.type.attached.getType() == OS_Type.Type.USER) {
							final TypeName x = vte.type.attached.getTypeName();
							if (x instanceof NormalTypeName) {
								final String tn = ((NormalTypeName) x).getName();
								final LookupResultList lrl = x.getContext().lookup(tn);
								OS_Element best = lrl.chooseBest(null);
								if (best != null) {
									while (best instanceof AliasStatement) {
										best = _resolveAlias((AliasStatement) best);
									}
									if (!(OS_Type.isConcreteType(best))) {
										errSink.reportError(String.format("Not a concrete type %s for (%s)", best, tn));
									} else {
	//									System.out.println("705 " + best);
										vte.type.attached = new OS_Type((ClassStatement) best);
									}
								} else {
									errSink.reportDignostic(new ResolveError(x, lrl));
								}
							}
						}
					}
					//
					// ATTACH A TYPE TO IDTE'S
					//
					for (IdentTableEntry ite : generatedFunction.idte_list) {
						int y=2;
						if (!ite.hasResolvedElement()) {
							IdentIA ident_a = new IdentIA(ite.getIndex(), generatedFunction);
							resolveIdentIA_(context, ident_a, generatedFunction, new FoundElement(phase) {

								final String path = generatedFunction.getIdentIAPathNormal(ident_a);

								@Override
								void foundElement(OS_Element x) {
									ite.setStatus(BaseTableEntry.Status.KNOWN, x);
									if (ite.type != null && ite.type.attached != null) {
										if (ite.type.attached.getType() == OS_Type.Type.USER) {
											try {
												OS_Type xx = resolve_type(ite.type.attached, fd_ctx);
												ite.type.attached = xx;
											} catch (ResolveError resolveError) {
												System.out.println("192 Can't attach type to "+path);
//												resolveError.printStackTrace(); // TODO print diagnostic
//												continue;
											}
										}
									} else {
										int yy=2;
										if (!ite.hasResolvedElement()) {
											LookupResultList lrl = lookupExpression(ite.getIdent(), fd_ctx);
											OS_Element best = lrl.chooseBest(null);
											if (best != null) {
												ite.setStatus(BaseTableEntry.Status.KNOWN, x);
												if (ite.type != null && ite.type.attached != null) {
													if (ite.type.attached.getType() == OS_Type.Type.USER) {
														try {
															OS_Type xx = resolve_type(ite.type.attached, fd_ctx);
															ite.type.attached = xx;
														} catch (ResolveError resolveError) {
															System.out.println("210 Can't attach type to "+ite.getIdent());
//														resolveError.printStackTrace(); // TODO print diagnostic
//															continue;
														}
													}
												}
											} else {
												System.err.println("184 Couldn't resolve "+ite.getIdent());
											}
										}
									}
								}

								@Override
								void noFoundElement() {
									ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
									errSink.reportError("165 Can't resolve "+path);
								}
							});
						}
					}
					//
					// RESOLVE FUNCTION RETURN TYPES
					//
					resolve_function_return_type(generatedFunction);
				}
				break;
			case ES:
				break;
			case XS:
				break;
			case AGN:
				{ // TODO doesn't account for __assign__
					final InstructionArgument agn_lhs = instruction.getArg(0);
					if (agn_lhs instanceof IntegerIA) {
						final IntegerIA arg = (IntegerIA) agn_lhs;
						final VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
						final InstructionArgument i2 = instruction.getArg(1);
						if (i2 instanceof IntegerIA) {
							final VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(i2));
							vte.addPotentialType(instruction.getIndex(), vte2.type);
						} else if (i2 instanceof FnCallArgs) {
							final FnCallArgs fca = (FnCallArgs) i2;
							do_assign_call(generatedFunction, fd_ctx, vte, fca, instruction);
						} else if (i2 instanceof ConstTableIA) {
							do_assign_constant(generatedFunction, instruction, vte, (ConstTableIA) i2);
						} else if (i2 instanceof IdentIA) {
							IdentTableEntry idte = generatedFunction.getIdentTableEntry(to_int(i2));
							assert idte.type != null;
							assert idte.resolved_element != null;
							vte.addPotentialType(instruction.getIndex(), idte.type);
						} else if (i2 instanceof ProcIA) {
							throw new NotImplementedException();
						} else
							throw new NotImplementedException();
					} else if (agn_lhs instanceof IdentIA) {
						final IdentIA arg = (IdentIA) agn_lhs;
						final IdentTableEntry idte = generatedFunction.getIdentTableEntry(to_int(arg));
						final InstructionArgument i2 = instruction.getArg(1);
						if (i2 instanceof IntegerIA) {
							final VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(i2));
							idte.addPotentialType(instruction.getIndex(), vte2.type);
						} else if (i2 instanceof FnCallArgs) {
							final FnCallArgs fca = (FnCallArgs) i2;
							do_assign_call(generatedFunction, fd_ctx, idte, fca, instruction.getIndex());
						} else if (i2 instanceof IdentIA) {
							IdentTableEntry idte2 = generatedFunction.getIdentTableEntry(to_int(i2));
							if (idte2.type == null) {
								idte2.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, idte2.getIdent());
							}
							LookupResultList lrl1 = fd_ctx.lookup(idte2.getIdent().getText());
							OS_Element best1 = lrl1.chooseBest(null);
							if (best1 != null) {
								idte2.setStatus(BaseTableEntry.Status.KNOWN, best1);
								// TODO check for elements which may contain type information
							} else {
								idte2.setStatus(BaseTableEntry.Status.UNKNOWN, null);
								System.err.println("242 Bad lookup" + idte2.getIdent().getText());
							}
							idte.addPotentialType(instruction.getIndex(), idte2.type);
						} else if (i2 instanceof ConstTableIA) {
							do_assign_constant(generatedFunction, instruction, idte, (ConstTableIA) i2);
						} else if (i2 instanceof ProcIA) {
							throw new NotImplementedException();
						} else
							throw new NotImplementedException();
					}
				}
				break;
			case AGNK:
				{
					final IntegerIA arg = (IntegerIA)instruction.getArg(0);
					final VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
					final InstructionArgument i2 = instruction.getArg(1);
					final ConstTableIA ctia = (ConstTableIA) i2;
					do_assign_constant(generatedFunction, instruction, vte, ctia);
				}
				break;
			case AGNT:
				break;
			case AGNF:
				System.err.println("292 Encountered AGNF");
				break;
			case JE:
				{
					System.err.println("296 Encountered JE");
				}
				break;
			case JNE:
				break;
			case JL:
				break;
			case JMP:
				break;
			case CALL: {
				final int pte_num = ((ProcIA)instruction.getArg(0)).getIndex();
				final ProcTableEntry fn1 = generatedFunction.getProcTableEntry(pte_num);
//				final InstructionArgument i2 = (instruction.getArg(1));
				{
					String x = generatedFunction.getIdentIAPathNormal((IdentIA) fn1.expression_num);
					System.err.println("298 "+x);
					resolveIdentIA_(context, (IdentIA) fn1.expression_num, generatedFunction, new FoundElement(phase) {

						@Override
						void foundElement(OS_Element e) {
							fn1.resolved_element = e;
						}

						@Override
						void noFoundElement() {
							assert false;
						}
					});
				}
			}
			break;
			case CALLS: {
				final int i1 = to_int(instruction.getArg(0));
				final InstructionArgument i2 = (instruction.getArg(1));
				final ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
					implement_calls(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
/*
				if (i2 instanceof IntegerIA) {
					int i2i = to_int(i2);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(i2i);
					int y =2;
				} else
					throw new NotImplementedException();
*/
			}
			break;
			case RET:
				break;
			case YIELD:
				break;
			case TRY:
				break;
			case PC:
				break;
			case CAST_TO:
				// README potentialType info is already added by MatchConditional
				break;
			case DECL:
//				throw new NotImplementedException();
				break;
			case IS_A:
				break;
			case NOP:
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + instruction.getName());
			}
		}
		for (final VariableTableEntry vte : generatedFunction.vte_list) {
			if (vte.type.attached == null) {
				int potential_size = vte.potentialTypes().size();
				if (potential_size == 1)
					vte.type.attached = getPotentialTypesVte(vte).get(0).attached;
				else if (potential_size > 1) {
					// TODO Check type compatibility
					System.err.println("703 "+vte.getName()+" "+vte.potentialTypes());
				}
			}
		}
		{
			//
			// NOW CALCULATE DEFERRED CALLS
			//
			for (final Integer deferred_call : generatedFunction.deferred_calls) {
				final Instruction instruction = generatedFunction.getInstruction(deferred_call);

				final int i1 = to_int(instruction.getArg(0));
				final InstructionArgument i2 = (instruction.getArg(1));
				final ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
//					generatedFunction.deferred_calls.remove(deferred_call);
					implement_calls_(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
			}
		}
	}

	void resolve_function_return_type(GeneratedFunction generatedFunction) {
		@Nullable final InstructionArgument vte_index = generatedFunction.vte_lookup("Result");
		if (vte_index != null) {
			final VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(vte_index));

			if (vte.type.attached != null) {
				phase.typeDecided(generatedFunction, vte.type.attached);
			} else {
				@NotNull Collection<TypeTableEntry> pot1 = vte.potentialTypes();
				ArrayList<TypeTableEntry> pot = new ArrayList<>(pot1);
				if (pot.size() == 1) {
					phase.typeDecided(generatedFunction, pot.get(0).attached);
				} else if (pot.size() == 0) {
					phase.typeDecided(generatedFunction, new OS_Type(BuiltInTypes.Unit));
				} else {
					// TODO report some kind of error/diagnostic and/or let ForFunction know...
				}
			}
		} else {
			// if Result is not present, then make function return Unit
			// TODO May not be correct in all cases, such as when Value is present
			// but works for current code structure, where Result is a always present
			phase.typeDecided(generatedFunction, new OS_Type(BuiltInTypes.Unit));
		}
	}

	void found_element_for_ite(GeneratedFunction generatedFunction, IdentTableEntry ite, @Nullable OS_Element y, Context ctx) {
		assert y == ite.resolved_element;

		if (y instanceof VariableStatement) {
			final VariableStatement vs = (VariableStatement) y;
			TypeName typeName = vs.typeName();
			if (ite.type == null || ite.type.attached == null) {
				if (!(typeName.isNull())) {
					if (ite.type == null)
						ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
					ite.type.attached = new OS_Type(typeName);
				} else {
					System.err.println("394 typename is null "+ vs.getName());
				}
			}
		} else if (y instanceof ClassStatement) {
			ClassStatement classStatement = ((ClassStatement) y);
			OS_Type attached = new OS_Type(classStatement);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null);
			} else
				ite.type.attached = attached;
		} else if (y instanceof FunctionDef) {
			FunctionDef functionDef = ((FunctionDef) y);
			OS_Type attached = new OS_FuncType(functionDef);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null);
			} else
				ite.type.attached = attached;
		} else if (y instanceof PropertyStatement) {
			PropertyStatement ps = (PropertyStatement) y;
			OS_Type attached;
			switch (ps.getTypeName().kindOfType()) {
			case GENERIC:
				attached = new OS_Type(ps.getTypeName());
				break;
			case NORMAL:
				try {
					attached = new OS_Type(resolve_type(new OS_Type(ps.getTypeName()), ctx).getClassOf());
				} catch (ResolveError resolveError) {
					System.err.println("378 resolveError");
					resolveError.printStackTrace();
					return;
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + ps.getTypeName().kindOfType());
			}
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null);
			} else
				ite.type.attached = attached;
			int yy = 2;
		} else if (y instanceof AliasStatement) {
			System.err.println("396 AliasStatement");
			OS_Element x = _resolveAlias((AliasStatement) y);
			if (x == null) {
				ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
				errSink.reportError("399 resolveAlias returned null");
			} else {
				ite.setStatus(BaseTableEntry.Status.KNOWN, x);
				found_element_for_ite(generatedFunction, ite, x, ctx);
			}
		} else {
			//LookupResultList exp = lookupExpression();
			System.out.println("2009 "+y);
		}
	}

	@NotNull
	private OS_Type resolve_type(final OS_Type type, final Context ctx) throws ResolveError {
		switch (type.getType()) {

		case BUILT_IN:
			{
				switch (type.getBType()) {
				case SystemInteger:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("SystemInteger");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup(typeName);
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = _resolveAlias((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						return new OS_Type((ClassStatement) best);
					}
				case Boolean:
					{
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("Boolean");
						final OS_Element best = lrl.chooseBest(null);
						return new OS_Type((ClassStatement) best); // TODO might change to Type
					}
				default:
					throw new IllegalStateException("531 Unexpected value: " + type.getBType());
				}
			}
		case USER:
			{
				final TypeName tn1 = type.getTypeName();
				switch (tn1.kindOfType()) {
				case NORMAL:
					{
						final Qualident tn = ((NormalTypeName) tn1).getRealName();
						System.out.println("799 [resolving USER type named] " + tn);
						final LookupResultList lrl = lookupExpression(tn, tn1.getContext());
						OS_Element best = lrl.chooseBest(null);
						while (best instanceof AliasStatement) {
							best = _resolveAlias((AliasStatement) best);
						}
						if (best == null)
							throw new ResolveError(tn1, lrl);

						return new OS_Type((ClassStatement) best);
					}
				case FUNCTION:
				case GENERIC:
				case TYPE_OF:
					throw new NotImplementedException();
				default:
					throw new IllegalStateException("414 Unexpected value: " + tn1.kindOfType());
				}
			}
		case USER_CLASS:
			return type;
		case FUNCTION:
			return type;
		default:
			throw new IllegalStateException("565 Unexpected value: " + type.getType());
		}
//		throw new IllegalStateException("Cant be here.");
	}

	private void do_assign_constant(final GeneratedFunction generatedFunction, final Instruction instruction, final VariableTableEntry vte, final ConstTableIA i2) {
		if (vte.type.attached != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.attached == null) {
			System.out.println("Null type in CTE "+cte);
		}
//		vte.type = cte.type;
		vte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final GeneratedFunction generatedFunction,
								final Context ctx,
								final VariableTableEntry vte,
								final FnCallArgs fca,
								final Instruction instruction) {
		final int instructionIndex = instruction.getIndex();
		final ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		IdentIA identIA = (IdentIA) pte.expression_num;
		if (identIA != null){
			System.out.println("594 "+generatedFunction.getIdentTableEntry(to_int(pte.expression_num)).getStatus());

			resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {
				@Override
				void foundElement(OS_Element e) {
//					generatedFunction.getIdentTableEntry(identIA.getIndex()).setResolvedElement(y);
					System.out.println("601 "+generatedFunction.getIdentTableEntry(to_int(pte.expression_num)).getStatus());
				}

				@Override
				void noFoundElement() {
					System.out.println("1005 Can't find element for " + generatedFunction.getIdentIAPathNormal(identIA));
				}
			});
		}
		for (final TypeTableEntry tte : pte.getArgs()) { // TODO this looks wrong
//			System.out.println("770 "+tte);
			final IExpression e = tte.expression;
			if (e == null) continue;
			switch (e.getKind()) {
			case NUMERIC:
				{
					tte.attached = new OS_Type(BuiltInTypes.SystemInteger);
					//vte.type = tte;
				}
				break;
			case IDENT:
				{
/*
					LookupResultList lrl = ctx.lookup(((IdentExpression)e).getText());
					OS_Element best = lrl.chooseBest(null);
					int y=2;
*/
					final InstructionArgument vte_index = generatedFunction.vte_lookup(((IdentExpression) e).getText());
//					System.out.println("10000 "+vte_index);
					if (vte_index != null) {
						final List<TypeTableEntry> ll = getPotentialTypesVte(generatedFunction, vte_index);
						if (ll.size() == 1) {
							tte.attached = ll.get(0).attached;
							vte.addPotentialType(instructionIndex, ll.get(0));
						} else {
							LookupResultList lrl = ctx.lookup(((IdentExpression)e).getText());
							OS_Element best = lrl.chooseBest(null);
							if (best instanceof FormalArgListItem) {
								@NotNull final FormalArgListItem fali = (FormalArgListItem) best;
								@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(
										TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken());
								vte.type = tte1;
								tte.attached = tte1.attached;
							} else if (best instanceof VariableStatement) {
								final VariableStatement vs = (VariableStatement) best;
								int y=2;
								@Nullable InstructionArgument x = generatedFunction.vte_lookup(vs.getName());
								VariableTableEntry xx = generatedFunction.getVarTableEntry(to_int(x));
								vte.type = xx.type;
								tte.attached = vte.type.attached;
							} else {
								int y = 2;
								System.err.println("543 "+best.getClass().getName());
								throw new NotImplementedException();
							}
						}
					} else {
						int ia = generatedFunction.addIdentTableEntry((IdentExpression) e, ctx);
						IdentTableEntry idte = generatedFunction.getIdentTableEntry(ia);
						idte.addPotentialType(instructionIndex, tte);
					}
				}
				break;
			case PROCEDURE_CALL:
				{
					final ProcedureCallExpression pce = (ProcedureCallExpression) e;
					final LookupResultList lrl = lookupExpression(pce.getLeft(), ctx);
					final OS_Element best = lrl.chooseBest(null);
					if (best != null) {
						if (best instanceof FunctionDef) { // TODO what about alias?
							tte.attached = new OS_FuncType((FunctionDef) best);
							//vte.addPotentialType(instructionIndex, tte);
						} else {
							final int y=2;
							throw new NotImplementedException();
						}
					} else {
						final int y=2;
						throw new NotImplementedException();
					}
				}
				break;
			case GET_ITEM:
				{
					final GetItemExpression gie = (GetItemExpression) e;
					do_assign_call_GET_ITEM(gie, tte, generatedFunction, ctx);
					continue;
				}
//				break;

			default:
				throw new IllegalStateException("Unexpected value: " + e.getKind());
			}
		}
		{
			if (pte.expression_num == null) {
				if (fca.expression_to_call.getName() != InstructionName.CALLS) {
					final String text = ((IdentExpression) pte.expression).getText();
					final LookupResultList lrl = ctx.lookup(text);

					final OS_Element best = lrl.chooseBest(null);
					if (best != null)
						pte.resolved_element = best; // TODO do we need to add a dependency for class?
					else {
						errSink.reportError("Cant resolve "+text);
					}
				} else {
					implement_calls(generatedFunction, ctx.getParent(), instruction.getArg(1), pte, instructionIndex);
				}
			} else {
				final int y=2;
				resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {

					final String x = generatedFunction.getIdentIAPathNormal(identIA);

					@Override
					void foundElement(OS_Element el) {
						pte.resolved_element = el;
						if (el instanceof FunctionDef) {
							FunctionDef fd = (FunctionDef) el;
							forFunction(new FunctionInvocation(fd, pte), new ForFunction() {
								@Override
								public void typeDecided(OS_Type aType) {
									assert fd == generatedFunction.getFD();
									//
									@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aType, pte.expression);
									vte.addPotentialType(instructionIndex, tte);
								}
							});
						} else if (el instanceof ClassStatement) {
							ClassStatement kl = (ClassStatement) el;
							OS_Type type = new OS_Type(kl);
							@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, pte.expression);
							vte.addPotentialType(instructionIndex, tte);
						} else {
							System.err.println("7890 "+el.getClass().getName());
						}
					}

					@Override
					void noFoundElement() {
						System.err.println("IdentIA path cannot be resolved "+ x);
					}
				});
			}
		}
	}

	private void do_assign_call_GET_ITEM(GetItemExpression gie, TypeTableEntry tte, GeneratedFunction generatedFunction, Context ctx) {
		final LookupResultList lrl = lookupExpression(gie.getLeft(), ctx);
		final OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			if (best instanceof VariableStatement) { // TODO what about alias?
				VariableStatement vs = (VariableStatement) best;
				String s = vs.getName();
				@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(s);
				if (vte_ia != null) {
					VariableTableEntry vte1 = generatedFunction.getVarTableEntry(to_int(vte_ia));
					throw new NotImplementedException();
				} else {
					final IdentTableEntry idte = generatedFunction.getIdentTableEntryFor(vs.getNameToken());
					assert idte != null;
					@Nullable OS_Type ty = idte.type.attached;
					idte.onType(phase, new OnType() {
						@Override public void typeDeduced(final OS_Type ty) {
							assert ty != null;
							OS_Type rtype = null;
							try {
								rtype = resolve_type(ty, ctx);
							} catch (ResolveError resolveError) {
//								resolveError.printStackTrace();
								errSink.reportError("Cant resolve " + ty); // TODO print better diagnostic
								return;
							}
							LookupResultList lrl2 = rtype.getClassOf().getContext().lookup("__getitem__");
							OS_Element best2 = lrl2.chooseBest(null);
							if (best2 != null) {
								if (best2 instanceof FunctionDef) {
									FunctionDef fd = (FunctionDef) best2;
									ProcTableEntry pte = null;
									forFunction(new FunctionInvocation(fd, pte), new ForFunction() {
										@Override
										public void typeDecided(final OS_Type aType) {
											assert fd == generatedFunction.getFD();
											//
											@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aType); // TODO expression?
											idte.type = tte1;
										}
									});
								} else {
									throw new NotImplementedException();
								}
							} else {
								throw new NotImplementedException();
							}
						}

						@Override
						public void noTypeFound() {
							throw new NotImplementedException();
						}
					});
					if (ty == null) {
						@NotNull TypeTableEntry tte3 = generatedFunction.newTypeTableEntry(
								TypeTableEntry.Type.SPECIFIED, new OS_Type(vs.typeName()), vs.getNameToken());
						idte.type = tte3;
						ty = idte.type.attached;
					}
				}

//				tte.attached = new OS_FuncType((FunctionDef) best); // TODO: what is this??
				//vte.addPotentialType(instructionIndex, tte);
			} else {
				final int y=2;
				throw new NotImplementedException();
			}
		} else {
			final int y=2;
			throw new NotImplementedException();
		}
	}

/*
	private void forFunction(GeneratedFunction gf, ForFunction forFunction) {
		phase.forFunction(this, gf, forFunction);
	}
*/

	private void forFunction(FunctionInvocation gf, ForFunction forFunction) {
		phase.forFunction(this, gf, forFunction);
	}

	private LookupResultList lookupExpression(final IExpression left, final Context ctx) {
		switch (left.getKind()) {
		case QIDENT:
			final IExpression de = Helpers.qualidentToDotExpression2((Qualident) left);
			return lookupExpression(de, ctx)/*lookup_dot_expression(ctx, de)*/;
		case DOT_EXP:
			return lookup_dot_expression(ctx, (DotExpression) left);
		case IDENT:
			return ctx.lookup(((IdentExpression) left).getText());
		default:
			throw new IllegalArgumentException();
		}

	}

	private void do_assign_constant(final GeneratedFunction generatedFunction, final Instruction instruction, final IdentTableEntry idte, final ConstTableIA i2) {
		if (idte.type != null && idte.type.attached != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.attached == null) {
			System.out.println("*** ERROR: Null type in CTE "+cte);
		}
//		vte.type = cte.type;
		idte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final GeneratedFunction generatedFunction,
								final Context ctx,
								final IdentTableEntry idte,
								final FnCallArgs fca,
								final int instructionIndex) {
		final ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		for (final TypeTableEntry tte : pte.getArgs()) {
			System.out.println("771 "+tte);
			final IExpression e = tte.expression;
			if (e == null) continue;
			switch (e.getKind()) {
			case NUMERIC:
			{
				tte.attached = new OS_Type(BuiltInTypes.SystemInteger);
				idte.type = tte; // TODO why not addPotentialType ? see below for example
			}
			break;
			case IDENT:
			{
/*
					LookupResultList lrl = ctx.lookup(((IdentExpression)e).getText());
					OS_Element best = lrl.chooseBest(null);
					int y=2;
*/
				final InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) e).getText());
//					System.out.println("10000 "+vte_ia);
				final Collection<TypeTableEntry> c = generatedFunction.getVarTableEntry(to_int(vte_ia)).potentialTypes();
				final List<TypeTableEntry> ll = new ArrayList<TypeTableEntry>(c);
				if (ll.size() == 1) {
					tte.attached = ll.get(0).attached;
					idte.addPotentialType(instructionIndex, ll.get(0));
				} else
					throw new NotImplementedException();
			}
			break;
			default:
			{
				throw new NotImplementedException();
			}
			}
		}
		{
			final LookupResultList lrl = ctx.lookup(((IdentExpression)pte.expression).getText());
			final OS_Element best = lrl.chooseBest(null);
			if (best != null)
				pte.resolved_element = best; // TODO do we need to add a dependency for class?
			else
				throw new NotImplementedException();
		}
	}

	private void implement_calls(final GeneratedFunction gf, final Context context, final InstructionArgument i2, final ProcTableEntry fn1, final int pc) {
		if (gf.deferred_calls.contains(pc)) {
			System.err.println("Call is deferred "/*+gf.getInstruction(pc)*/+" "+fn1);
			return;
		}
		implement_calls_(gf, context, i2, fn1, pc);
	}

	private void implement_calls_(final GeneratedFunction gf, final Context context, final InstructionArgument i2, final ProcTableEntry fn1, final int pc) {
		final IExpression pn1 = fn1.expression;
		if (pn1 instanceof IdentExpression) {
			final String pn = ((IdentExpression) pn1).getText();
			boolean found = lookup_name_calls(context, pn, fn1);
			LookupResultList lrl;
			OS_Element best;
			if (found) return;

			final String pn2 = SpecialFunctions.reverse_name(pn);
			if (pn2 != null) {
//				System.out.println("7002 "+pn2);
				found = lookup_name_calls(context, pn2, fn1);
				if (found) return;
			}

			if (i2 instanceof IntegerIA) {
				final VariableTableEntry vte = gf.getVarTableEntry(to_int(i2));
				final Context ctx = gf.getContextFromPC(pc); // might be inside a loop or something
				final String vteName = vte.getName();
				if (vteName != null) {
					if (SpecialVariables.contains(vteName)) {
						System.err.println("Skipping special variable " + vteName + " " + pn);
					} else {
						final LookupResultList lrl2 = ctx.lookup(vteName);
//						System.out.println("7003 "+vte.getName()+" "+ctx);
						final OS_Element best2 = lrl2.chooseBest(null);
						if (best2 != null) {
							found = lookup_name_calls(best2.getContext(), pn, fn1);
							if (found) return;

							if (pn2 != null) {
								found = lookup_name_calls(best2.getContext(), pn2, fn1);
							}

							if (!found) {
								//throw new NotImplementedException(); // TODO
								errSink.reportError("Special Function not found " + pn);
							}
						} else {
							throw new NotImplementedException(); // Cant find vte, should never happen
						}
					}
				} else {
					final Collection<TypeTableEntry> t = vte.potentialTypes();
					final ArrayList<TypeTableEntry> tt = new ArrayList<TypeTableEntry>(t);
					if (tt.size() == 1) {
						final OS_Type x = tt.get(0).attached;
						assert x != null;
						assert x.getType() != null;
						if (x.getType() == OS_Type.Type.USER_CLASS) {
							final Context ctx1 = x.getClassOf().getContext();

							found = lookup_name_calls(ctx1, pn, fn1);
							if (found) return;

							if (pn2 != null) {
								found = lookup_name_calls(ctx1, pn2, fn1);
							}

							if (!found) {
								//throw new NotImplementedException(); // TODO
								errSink.reportError("Special Function not found " + pn);
							}
						} else
							assert false;
					} else
						assert false;
				}
			} else {
				final int y=2;
				System.err.println("i2 is not IntegerIA ("+i2.getClass().getName()+")");
			}
		} else
			throw new NotImplementedException(); // pn1 is not IdentExpression
	}

	private boolean lookup_name_calls(final Context ctx, final String pn, final ProcTableEntry fn1) {
		final LookupResultList lrl = ctx.lookup(pn);
		final OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			fn1.resolved_element = best; // TODO check arity and arg matching
			return true;
		}
		return false;
	}

	public static int to_int(@NotNull final InstructionArgument arg) {
		if (arg instanceof IntegerIA)
			return ((IntegerIA) arg).getIndex();
		if (arg instanceof ProcIA)
			return ((ProcIA) arg).getIndex();
		if (arg instanceof IdentIA)
			return ((IdentIA) arg).getIndex();
		throw new NotImplementedException();
	}

	@Nullable private OS_Element _resolveAlias(final AliasStatement aliasStatement) {
		final LookupResultList lrl2;
		if (aliasStatement.getExpression() instanceof Qualident) {
			final IExpression de = Helpers.qualidentToDotExpression2(((Qualident) aliasStatement.getExpression()));
			if (de instanceof DotExpression)
				lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			else
				lrl2 = aliasStatement.getContext().lookup(((IdentExpression) de).getText());
			return lrl2.chooseBest(null);
		}
		// TODO what about when DotExpression is not just simple x.y.z? then alias equivalent to val
		if (aliasStatement.getExpression() instanceof DotExpression) {
			final IExpression de = aliasStatement.getExpression();
			lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			return lrl2.chooseBest(null);
		}
		lrl2 = aliasStatement.getContext().lookup(((IdentExpression) aliasStatement.getExpression()).getText());
		return lrl2.chooseBest(null);
	}

	private LookupResultList lookup_dot_expression(Context ctx, final DotExpression de) {
		final Stack<IExpression> s = dot_expression_to_stack(de);
		OS_Type t = null;
		IExpression ss = s.peek();
		while (!s.isEmpty()) {
			ss = s.peek();
			if (t != null && (t.getType() == OS_Type.Type.USER_CLASS || t.getType() == OS_Type.Type.FUNCTION))
				ctx = t.getClassOf().getContext();
			t = deduceExpression(ss, ctx);
			if (t == null) break;
			s.pop();
		}
		if (t == null) {
			NotImplementedException.raise();
			return new LookupResultList(); // TODO throw ResolveError
		} else {
			final LookupResultList lrl = t.getElement().getParent().getContext().lookup(((IdentExpression) ss).getText());
			return lrl;
		}
	}

	/**
	 * @see {@link tripleo.elijah.stages.deduce.DotExpressionToStackTest}
	 * @param de The {@link DotExpression} to turn into a {@link Stack}
	 * @return a "flat" {@link Stack<IExpression>} of expressions
	 */
	@NotNull
	static Stack<IExpression> dot_expression_to_stack(final DotExpression de) {
		final Stack<IExpression> right_stack = new Stack<IExpression>();
		IExpression right = de.getRight();
		right_stack.push(de.getLeft());
		while (right instanceof DotExpression) {
			right_stack.push(right.getLeft());
			right = ((DotExpression) right).getRight();
		}
		right_stack.push(right);
		Collections.reverse(right_stack);
		return right_stack;
	}

	public OS_Type deduceExpression(@NotNull final IExpression n, final Context context) {
		switch (n.getKind()) {
		case IDENT:
			return deduceIdentExpression((IdentExpression) n, context);
		case NUMERIC:
			return new OS_Type(BuiltInTypes.SystemInteger);
		case DOT_EXP:
			final DotExpression de = (DotExpression) n;
			final LookupResultList lrl = lookup_dot_expression(context, de);
			final OS_Type left_type = deduceExpression(de.getLeft(), context);
			final OS_Type right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext());
			NotImplementedException.raise();
			break;
		case PROCEDURE_CALL:
			OS_Type ty = deduceProcedureCall((ProcedureCallExpression) n, context);
			return ty/*n.getType()*/;
		case QIDENT:
			final IExpression expression = Helpers.qualidentToDotExpression2(((Qualident) n));
			return deduceExpression(expression, context);
		}
		return null;
	}

	/**
	 * Try to find the type of a ProcedureCall. Will either be a constructor or function call, most likely
	 *
	 * @param pce the procedure call
	 * @param ctx the context to use for lookup
	 * @return the deduced type or {@code null}. Do not {@code pce.setType}
	 */
	@Nullable
	private OS_Type deduceProcedureCall(final ProcedureCallExpression pce, final Context ctx) {
		System.err.println("979 Skipping deduceProcedureCall "+pce);
		OS_Element best = lookup(pce.getLeft(), ctx);
		if (best == null) return null;
		int y=2;
		if (best instanceof ClassStatement) {
			return new OS_Type((ClassStatement) best);
		} else if (best instanceof FunctionDef) {
			final FunctionDef fd = (FunctionDef) best;
			if (fd.returnType() != null && !fd.returnType().isNull()) {
				return new OS_Type(fd.returnType());
			}
			return new OS_UnknownType(fd);			// TODO still must register somewhere
		} else if (best instanceof FuncExpr) {
			final FuncExpr funcExpr = (FuncExpr) best;
			if (funcExpr.returnType() != null && !funcExpr.returnType().isNull()) {
				return new OS_Type(funcExpr.returnType());
			}
			return new OS_UnknownType(funcExpr);	// TODO still must register somewhere
		} else {
			System.err.println("992 "+best.getClass().getName());
			throw new NotImplementedException();
		}
	}

	private OS_Type deduceIdentExpression(final IdentExpression ident, final Context ctx) {
		// is this right?
		LookupResultList lrl = ctx.lookup(ident.getText());
		OS_Element best = lrl.chooseBest(null);
		while (best instanceof AliasStatement) {
			best = _resolveAlias((AliasStatement) best);
		}
		if (best instanceof ClassStatement) {
			return new OS_Type((ClassStatement) best);
		} else if (best instanceof VariableStatement) {
			final VariableStatement vs = (VariableStatement) best;
			if (!vs.typeName().isNull())
				return new OS_Type(vs.typeName());
		} else if (best instanceof FunctionDef) {
			final FunctionDef functionDef = (FunctionDef) best;
			return new OS_FuncType(functionDef);
		}
		return null;
	}

	public void resolveIdentIA_(Context context, IdentIA identIA, GeneratedFunction generatedFunction, FoundElement foundElement) {
		final List<InstructionArgument> s = generatedFunction._getIdentIAPathList(identIA);

		OS_Element el = null;
		Context ectx = context;
		for (final InstructionArgument ia : s) {
			if (ia instanceof IntegerIA) {
				VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(ia));
				final String text = vte.getName();
				final LookupResultList lrl = ectx.lookup(text);
				el = lrl.chooseBest(null);
				if (el != null) {
					//
					// TYPE INFORMATION IS CONTAINED IN VARIABLE DECLARATION
					//
					if (el instanceof VariableStatement) {
						VariableStatement vs = (VariableStatement) el;
						if (!vs.typeName().isNull()) {
							ectx = vs.typeName().getContext();
							continue;
						}
					}
					//
					// OTHERWISE TYPE INFORMATION MAY BE IN POTENTIAL_TYPES
					//
					@NotNull List<TypeTableEntry> pot = getPotentialTypesVte(vte);
					if (pot.size() == 1) {
						OS_Type attached = pot.get(0).attached;
						if (attached != null) {
							switch (attached.getType()) {
							case USER_CLASS: {
								ClassStatement x = attached.getClassOf();
								ectx = x.getContext();
								break;
							}
							case FUNCTION: {
								int yy = 2;
								System.err.println("1005");
								FunctionDef x = (FunctionDef) attached.getElement();
								ectx = x.getContext();
								break;
							}
							case USER:
								if (el instanceof MatchConditional.MatchArm_TypeMatch) {
									// for example from match conditional
									final TypeName tn = ((MatchConditional.MatchArm_TypeMatch) el).getTypeName();
									try {
										@NotNull final OS_Type ty = resolve_type(new OS_Type(tn), tn.getContext());
										ectx = ty.getElement().getContext();
									} catch (ResolveError resolveError) {
										resolveError.printStackTrace();
										System.err.println("1182 Can't resolve " + tn);
										throw new IllegalStateException("ResolveError.");
									}
//									ectx = el.getContext();
								} else
									ectx = attached.getTypeName().getContext(); // TODO is this right?
								break;
							default:
								System.err.println("1010 " + attached.getType());
								throw new IllegalStateException("Dont know what youre doing here.");
							}
						} else {
							TypeTableEntry tte = pot.get(0);
							OS_Element el2 = lookup(tte.expression.getLeft(), ectx);
							if (el2 == null) {
								System.err.println("1062 "+tte.expression.getLeft());
								throw new IllegalStateException("foo bar");
							} else {
								ectx = el2.getContext();
								if (el2 instanceof ClassStatement) {
									tte.attached = new OS_Type((ClassStatement) el2);
								} else if (el2 instanceof FunctionDef) {
									assert tte.attached == null;
									switch (tte.expression.getKind()) {
									case PROCEDURE_CALL:
										final TypeName returnType = ((FunctionDef) el2).returnType();
										if (returnType != null && !returnType.isNull()) {
											tte.attached = new OS_Type(returnType);
										} else {
											// TODO we must find type at a later point
											System.err.println("1105 we must find type at a later point for "+tte.expression);
										}
										break;
									default:
										tte.attached = new OS_FuncType((FunctionDef) el2);
										break;
									}
								} else {
									System.err.println("1017 "+el2.getClass().getName());
									throw new NotImplementedException();
								}
							}
						}
					}
				} else {
					errSink.reportError("1001 Can't resolve "+text);
					//return null;
					foundElement.doNoFoundElement();
					return;
				}
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = generatedFunction.getIdentTableEntry(to_int(ia));
				if (idte.getStatus() == BaseTableEntry.Status.UNKNOWN) {
					System.out.println("1257 Not found for "+generatedFunction.getIdentIAPathNormal((IdentIA) ia));
					// No need checking more than once
					foundElement.doNoFoundElement();
					return;
				}
				//assert idte.backlink == null;

				if (idte.getStatus() == BaseTableEntry.Status.UNCHECKED) {
					if (idte.backlink == null) {
						final String text = idte.getIdent().getText();
						if (idte.resolved_element == null) {
							final LookupResultList lrl = ectx.lookup(text);
							el = lrl.chooseBest(null);
						} else {
							el = idte.resolved_element;
						}
						if (el != null) {
							idte.setStatus(BaseTableEntry.Status.KNOWN, el);
							if (el.getContext() != null)
								ectx = el.getContext();
							else {
								final int yy = 2;
								throw new NotImplementedException();
							}
						} else {
//							errSink.reportError("1179 Can't resolve " + text);
							idte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
							foundElement.doNoFoundElement();
							return;
						}
					} else {
						resolveIdentIA2_(ectx/*context*/, s, generatedFunction, new FoundElement(phase) {
							final String z = generatedFunction.getIdentIAPathNormal((IdentIA) ia);

							@Override
							void foundElement(OS_Element e) {
								foundElement.doFoundElement(e);
								idte.setStatus(BaseTableEntry.Status.KNOWN, e);
							}

							@Override
							void noFoundElement() {
								foundElement.noFoundElement();
								System.out.println("2002 Cant resolve " + z);
								idte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
							}
						});
					}
				} else if (idte.getStatus() == BaseTableEntry.Status.KNOWN) {
					el = idte.resolved_element;
					ectx = el.getContext();
				}
			} else if (ia instanceof ProcIA) {
				ProcTableEntry prte = generatedFunction.getProcTableEntry(to_int(ia));
				int y=2;
				IExpression exp = prte.expression;
				if (exp instanceof ProcedureCallExpression) {
					final ProcedureCallExpression pce = (ProcedureCallExpression) exp;
					exp = pce.getLeft(); // TODO might be another pce??!!
					if (exp instanceof ProcedureCallExpression)
						throw new IllegalArgumentException("double pce!");
				}
				LookupResultList lrl = lookupExpression(exp, ectx);
				el = lrl.chooseBest(null);
				prte.resolved_element = el;
			} else
				throw new IllegalStateException("Really cant be here");
		}
		System.out.println("1320 Found for "+generatedFunction.getIdentIAPathNormal(identIA));
		foundElement.doFoundElement(el);
	}

	static class Holder<T> {
		private T el;

		public void set(T el) {
			this.el = el;
		}

		public T get() {
			return el;
		}
	}

	public void resolveIdentIA2_(@NotNull final Context ctx,
								 @NotNull final List<InstructionArgument> s,
								 @NotNull final GeneratedFunction generatedFunction,
								 @NotNull final FoundElement foundElement) {
		OS_Element el = null;
		Context ectx = ctx;

		for (InstructionArgument ia2 : s) {
			if (ia2 instanceof IntegerIA) {
				VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(ia2));
				final String text = vte.getName();
/*
				final LookupResultList lrl = ectx.lookup(text);
				el = lrl.chooseBest(null);
				if (el == null) {
					errSink.reportError("1002 Can't resolve "+text);
					return null;
				} else {
					ectx = el.getContext();
				}
*/
				{
					List<TypeTableEntry> pot = getPotentialTypesVte(vte);
					if (pot.size() == 1) {
						final OS_Type attached = pot.get(0).attached;
						if (attached == null) {
							LookupResultList lrl = lookupExpression(pot.get(0).expression.getLeft(), ctx);
							OS_Element best = lrl.chooseBest(Helpers.List_of(
									new DeduceUtils.MatchFunctionArgs(
											(ProcedureCallExpression) pot.get(0).expression)));
							final FunctionDef fd;
							if (best instanceof FunctionDef) {
								fd = (FunctionDef) best;
							} else {
								fd = null;
								System.err.println("1195 Can't find match");
							}
							if (fd != null) {
								ProcTableEntry pte = null;
								forFunction(new FunctionInvocation(fd, pte), new ForFunction() {
									@Override
									public void typeDecided(OS_Type aType) {
										assert fd == generatedFunction.getFD();
										//
										pot.get(0).attached = aType;
									}
								});
							}
						} else {
							switch (attached.getType()) {
							case USER_CLASS:
								ectx = attached.getClassOf().getContext(); // TODO can combine later
								break;
							case FUNCTION:
								ectx = ((OS_FuncType) attached).getElement().getContext();
								break;
							case USER:
								ectx = attached.getTypeName().getContext();
								break;
							default:
								System.err.println("1098 " + attached.getType());
								throw new IllegalStateException("Can't be here.");
							}
						}
					}
				}

				OS_Type attached = vte.type.attached;
				if (attached != null) {
					switch (attached.getType()) {
					case USER_CLASS:
						ectx = attached.getClassOf().getContext();
						break;
					case FUNCTION:
						ectx = attached.getElement().getContext();
						break;
//					default:
//						throw new NotImplementedException();
					case USER:
						{
							try {
								@NotNull OS_Type ty = resolve_type(attached, ctx);
								ectx = ty.getClassOf().getContext();
							} catch (ResolveError resolveError) {
								System.err.println("1300 Can't resolve " + attached);
								resolveError.printStackTrace();
							}
						}
						break;
					default:
						throw new IllegalStateException("Unexpected value: " + attached.getType());
					}
				} else {
					if (vte.potentialTypes().size() == 1) {
						final ArrayList<TypeTableEntry> pot = getPotentialTypesVte(vte);
						final OS_Type attached1 = pot.get(0).attached;
						vte.type.attached = attached1;
						// TODO this will break
						final TypeName attached1TypeName = attached1.getTypeName();
						if (attached1TypeName instanceof RegularTypeName)
							ectx = lookupExpression(((RegularTypeName) attached1TypeName).getRealName(), ectx).results().get(0).getElement().getContext();
						else if (attached1.getType() == OS_Type.Type.USER_CLASS) {
							ectx = attached1.getClassOf().getContext();
						} else {
							System.out.println("1442 Don't know "+attached1TypeName.getClass().getName());
							throw new NotImplementedException();
						}
					} else
						System.out.println("1006 Can't find type of " + text);
				}
			} else if (ia2 instanceof IdentIA) {
				final IdentTableEntry idte2 = generatedFunction.getIdentTableEntry(to_int(ia2));
				final String text = idte2.getIdent().getText();

				final LookupResultList lrl = ectx.lookup(text);
				el = lrl.chooseBest(null);
				if (el == null) {
					errSink.reportError("1007 Can't resolve "+text);
					foundElement.doNoFoundElement();
					return;
				} else {
					if (idte2.type == null) {
						if (el instanceof VariableStatement) {
							VariableStatement vs = (VariableStatement) el;
							if (!vs.typeName().isNull()) {
								TypeTableEntry tte;
								OS_Type attached;
								if (/*vs.typeName() == null &&*/ vs.initialValue() != IExpression.UNASSIGNED) { // TODO was always false
									attached = deduceExpression(vs.initialValue(), ectx);
								} else { // if (vs.typeName() != null) {
									attached = new OS_Type(vs.typeName());
								}
//								else
//									attached = null;
								if (vs.initialValue() != IExpression.UNASSIGNED) {
									tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, vs.initialValue());
								} else {
									tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(vs.typeName())); // TODO where is expression? ie foo.x
								}
								idte2.type = tte;
							} else if (vs.initialValue() != IExpression.UNASSIGNED) {
								OS_Type attached = deduceExpression(vs.initialValue(), ectx);
								TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, vs.initialValue());
								idte2.type = tte;
							}
						} else if (el instanceof FunctionDef) {
							OS_Type attached = new OS_UnknownType(el);
							TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null);
							idte2.type = tte;
						}
					}
					if (idte2.type != null) {
						assert idte2.type.attached != null;
						try {
							if (!(idte2.type.attached instanceof OS_UnknownType)) { // TODO
								OS_Type rtype = resolve_type(idte2.type.attached, ectx);
								if (rtype.getType() == OS_Type.Type.USER_CLASS)
									ectx = rtype.getClassOf().getContext();
								else if (rtype.getType() == OS_Type.Type.FUNCTION)
									ectx = ((OS_FuncType) rtype).getElement().getContext();
								idte2.type.attached = rtype; // TODO may be losing alias information here
							}
						} catch (ResolveError resolveError) {
							if (resolveError.resultsList().size() > 1)
								errSink.reportDignostic(resolveError);
							else
								System.out.println("1089 Can't attach type to "+idte2.type.attached);
//							resolveError.printStackTrace(); // TODO print diagnostic
							continue;
						}
					} else {
//						throw new IllegalStateException("who knows");
						System.out.println("2010 idte2.type == null for "+ text);
					}
				}

				int yy=2;
			} else if (ia2 instanceof ProcIA) {
				System.err.println("1373 ProcIA");
//				throw new NotImplementedException();
			} else
				throw new NotImplementedException();
		}
		foundElement.doFoundElement(el);
	}

	@NotNull ArrayList<TypeTableEntry> getPotentialTypesVte(VariableTableEntry vte) {
		return new ArrayList<TypeTableEntry>(vte.potentialTypes());
	}

	@NotNull
	private ArrayList<TypeTableEntry> getPotentialTypesVte(GeneratedFunction generatedFunction, InstructionArgument vte_index) {
		return getPotentialTypesVte(generatedFunction.getVarTableEntry(to_int(vte_index)));
	}

	private OS_Element lookup(IExpression expression, Context ctx) {
		switch (expression.getKind()) {
		case IDENT:
			LookupResultList lrl = ctx.lookup(((IdentExpression)expression).getText());
			OS_Element best = lrl.chooseBest(null);
			return best;
		case PROCEDURE_CALL:
			LookupResultList lrl2 = lookupExpression(expression.getLeft(), ctx);
			OS_Element best2 = lrl2.chooseBest(null);
			return best2;
		case DOT_EXP:
			LookupResultList lrl3 = lookupExpression(expression, ctx);
			OS_Element best3 = lrl3.chooseBest(null);
			return best3;
		default:
			System.err.println("1242 "+expression);
			throw new NotImplementedException();
		}
	}


}

//
//
//
