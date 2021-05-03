/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.jdeferred2.DoneCallback;
import org.jdeferred2.Promise;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialFunctions;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.ConstTableIA;
import tripleo.elijah.stages.instructions.FnCallArgs;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

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

	public void deduceFunctions(final @NotNull Iterable<GeneratedNode> lgf) {
		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				if (generatedFunction.deducedAlready) continue;
				deduce_generated_function(generatedFunction);
				generatedFunction.deducedAlready = true;
				for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
					if (identTableEntry.resolved_element instanceof  VariableStatement) {
						final VariableStatement vs = (VariableStatement) identTableEntry.resolved_element;
						OS_Element el = vs.getParent().getParent();
						OS_Element el2 = generatedFunction.fd.getParent();
						if (el != el2) {
							if (el instanceof ClassStatement || el instanceof NamespaceStatement)
								// NOTE there is no concept of gf here
								phase.registerResolvedVariable(identTableEntry, el, vs.getName());
						}
					}
				}
				phase.addFunction(generatedFunction, generatedFunction.getFD());
			}
		}
	}

	List<Runnable> onRunnables = new ArrayList<Runnable>();

	void onFinish(Runnable r) {
		onRunnables.add(r);
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
						case STRING_LITERAL:
							{
								final OS_Type a = cte.getTypeTableEntry().attached;
								if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
									try {
										cte.getTypeTableEntry().attached = resolve_type(new OS_Type(BuiltInTypes.String_), context);
									} catch (ResolveError resolveError) {
										System.out.println("117 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
									}
								}
								break;
							}
						case CHAR_LITERAL:
							{
								final OS_Type a = cte.getTypeTableEntry().attached;
								if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
									try {
										cte.getTypeTableEntry().attached = resolve_type(new OS_Type(BuiltInTypes.SystemCharacter), context);
									} catch (ResolveError resolveError) {
										System.out.println("117 Can't be here");
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
						resolve_ident_table_entry(ite, generatedFunction, context);
					}
				}
				break;
			case X:
				{
					//
					// resolve var table. moved from `E'
					//
					for (VariableTableEntry vte : generatedFunction.vte_list) {
						resolve_var_table_entry(vte, generatedFunction, context);
					}
					for (Runnable runnable : onRunnables) {
						runnable.run();
					}
//					System.out.println("167 "+generatedFunction);
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
										best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
									}
									if (!(OS_Type.isConcreteType(best))) {
										errSink.reportError(String.format("Not a concrete type %s for (%s)", best, tn));
									} else {
	//									System.out.println("705 " + best);
										// NOTE that when we set USER_CLASS from USER generic information is
										// still contained in constructable_pte
										vte.type.attached = new OS_Type((ClassStatement) best);
									}
									//vte.el = best;
									// NOTE we called resolve_var_table_entry above
									System.err.println("200 "+best);
									if (vte.el != null)
										assert vte.getStatus() == BaseTableEntry.Status.KNOWN;
//									vte.setStatus(BaseTableEntry.Status.KNOWN, best/*vte.el*/);
								} else {
									errSink.reportDiagnostic(new ResolveError(x, lrl));
								}
							}
						}
					}
					//
					// ATTACH A TYPE TO IDTE'S
					//
					for (IdentTableEntry ite : generatedFunction.idte_list) {
						int y=2;
						assign_type_to_idte(ite, generatedFunction, fd_ctx, context);
					}
					for (TypeTableEntry typeTableEntry : generatedFunction.tte_list) {
						@Nullable OS_Type attached = typeTableEntry.attached;
						if (attached == null) continue;
						if (attached.getType() == OS_Type.Type.USER) {
							TypeName tn = attached.getTypeName();
							switch (tn.kindOfType()) {
								case FUNCTION:
								case GENERIC:
								case TYPE_OF:
									continue; // TODO Skip these for now.
							}
							try {
								typeTableEntry.attached = resolve_type(attached, attached.getTypeName().getContext());
								if (typeTableEntry.attached.getType() == OS_Type.Type.USER_CLASS) {
									ClassStatement c = typeTableEntry.attached.getClassOf();
									phase.onClass(c, new OnClass() {
										// TODO what about ClassInvocation's?
										@Override
										public void classFound(GeneratedClass cc) {
											typeTableEntry.resolve(cc);
										}
									});
								} else {
									System.err.println("245 Can't resolve typeTableEntry "+typeTableEntry);
								}
							} catch (ResolveError aResolveError) {
								System.err.println("288 Failed to resolve type "+attached);
								errSink.reportDiagnostic(aResolveError);
							}
						} else if (attached.getType() == OS_Type.Type.USER_CLASS) {
							ClassStatement c = attached.getClassOf();
							phase.onClass(c, new OnClass() {
								// TODO what about ClassInvocation's?
								@Override
								public void classFound(GeneratedClass cc) {
									typeTableEntry.resolve(cc);
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
								idte2.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, idte2.getIdent(), idte2);
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
				final ProcTableEntry pte = generatedFunction.getProcTableEntry(pte_num);
//				final InstructionArgument i2 = (instruction.getArg(1));
				{
					final IdentIA expression = (IdentIA) pte.expression_num;
					final String x = generatedFunction.getIdentIAPathNormal(expression);
					System.err.println("298 "+x);
					resolveIdentIA_(context, expression, generatedFunction, new FoundElement(phase) {

						@Override
						public void foundElement(OS_Element e) {
//							pte.resolved_element = e;
							set_resolved_element_pte(expression, e, pte);
							if (fd instanceof DefFunctionDef) {
								forFunction(new FunctionInvocation((FunctionDef) e, pte), new ForFunction() {
									@Override
									public void typeDecided(OS_Type aType) {
										@Nullable InstructionArgument x = generatedFunction.vte_lookup("Result");
										assert x != null;
										((IntegerIA)x).getEntry().type.attached = aType;
									}
								});
							}
						}

						@Override
						public void noFoundElement() {
							errSink.reportError("370 Can't find callsite "+x);
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
			case CONSTRUCT:
				implement_construct(generatedFunction, instruction);
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
					errSink.reportDiagnostic(new CantDecideType(vte, vte.potentialTypes()));
				} else {
					// potential_size == 0
					// Result is handled by phase.typeDecideds, self is always valid
					if (vte.getName() != null && !(vte.getName().equals("Result") || vte.getName().equals("self")))
						errSink.reportDiagnostic(new CantDecideType(vte, vte.potentialTypes()));
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

	public void assign_type_to_idte(IdentTableEntry ite,
									GeneratedFunction generatedFunction,
									Context aFunctionContext,
									Context aContext) {
		if (!ite.hasResolvedElement()) {
			IdentIA ident_a = new IdentIA(ite.getIndex(), generatedFunction);
			resolveIdentIA_(aContext, ident_a, generatedFunction, new FoundElement(phase) {

				final String path = generatedFunction.getIdentIAPathNormal(ident_a);

				@Override
				public void foundElement(OS_Element x) {
					ite.setStatus(BaseTableEntry.Status.KNOWN, x);
					if (ite.type != null && ite.type.attached != null) {
						if (ite.type.attached.getType() == OS_Type.Type.USER) {
							try {
								OS_Type xx = resolve_type(ite.type.attached, aFunctionContext);
								ite.type.attached = xx;
							} catch (ResolveError resolveError) {
								System.out.println("192 Can't attach type to "+path);
//								resolveError.printStackTrace(); // TODO print diagnostic
//								continue;
							}
						}
					} else {
						int yy=2;
						if (!ite.hasResolvedElement()) {
							LookupResultList lrl = null;
							try {
								lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), aFunctionContext);
								OS_Element best = lrl.chooseBest(null);
								if (best != null) {
									ite.setStatus(BaseTableEntry.Status.KNOWN, x);
									if (ite.type != null && ite.type.attached != null) {
										if (ite.type.attached.getType() == OS_Type.Type.USER) {
											try {
												OS_Type xx = resolve_type(ite.type.attached, aFunctionContext);
												ite.type.attached = xx;
											} catch (ResolveError resolveError) { // TODO double catch
												System.out.println("210 Can't attach type to "+ite.getIdent());
//												resolveError.printStackTrace(); // TODO print diagnostic
//												continue;
											}
										}
									}
								} else {
									System.err.println("184 Couldn't resolve "+ite.getIdent());
								}
							} catch (ResolveError aResolveError) {
								System.err.println("184-506 Couldn't resolve "+ite.getIdent());
								aResolveError.printStackTrace();
							}
						}
					}
				}

				@Override
				public void noFoundElement() {
					ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					errSink.reportError("165 Can't resolve "+path);
				}
			});
		}
	}

	public void resolve_ident_table_entry(IdentTableEntry ite, GeneratedFunction generatedFunction, Context ctx) {
		InstructionArgument itex = new IdentIA(ite.getIndex(), generatedFunction);
		while (itex != null) {
			IdentTableEntry itee = generatedFunction.getIdentTableEntry(to_int(itex));
			while (itex != null) {
				BaseTableEntry x = null;
				if (itee.backlink instanceof IntegerIA) {
					x = generatedFunction.getVarTableEntry(to_int(itee.backlink));
					itex = null;
				} else if (itee.backlink instanceof IdentIA) {
					x = generatedFunction.getIdentTableEntry(to_int(itee.backlink));
					itex = ((IdentTableEntry) x).backlink;
				} else if (itee.backlink == null || itee.backlink instanceof ProcIA) {
					itex = null;
					x = null;
				}

				if (x != null) {
//					System.err.println("162 Adding FoundParent for "+itee);
					x.addStatusListener(new FoundParent(x, itee, itee.getIdent().getContext())); // TODO context??
				}
			}
		}
		if (ite.resolved_element != null)
			return;
		if (ite.backlink == null) {
			final IdentIA identIA = new IdentIA(ite.getIndex(), generatedFunction);
			resolveIdentIA_(ite.getPC(), identIA, generatedFunction, new FoundElement(phase) {

				final String x = generatedFunction.getIdentIAPathNormal(identIA);

				@Override
				public void foundElement(OS_Element e) {
					ite.setStatus(BaseTableEntry.Status.KNOWN, e);
					found_element_for_ite(generatedFunction, ite, e, ctx);
				}

				@Override
				public void noFoundElement() {
					ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					errSink.reportError("1004 Can't find element for "+ x);
				}
			});
		}
	}

	public void resolve_var_table_entry(VariableTableEntry vte, GeneratedFunction generatedFunction, Context ctx) {
		if (vte.el == null)
			return;
		{
			vte.setStatus(BaseTableEntry.Status.KNOWN, vte.el);
		}
	}

	class Implement_construct {

		private final GeneratedFunction generatedFunction;
		private final Instruction instruction;

		final ProcTableEntry pte;
		private final InstructionArgument expression;

		public Implement_construct(GeneratedFunction aGeneratedFunction, Instruction aInstruction) {
			// README all these asserts are redundant, I know
			generatedFunction = aGeneratedFunction;
			instruction = aInstruction;

			assert instruction.getName() == InstructionName.CONSTRUCT;
			assert instruction.getArg(0) instanceof ProcIA;

			final int pte_num = ((ProcIA) instruction.getArg(0)).getIndex();
			pte = generatedFunction.getProcTableEntry(pte_num);

			expression = pte.expression_num;

			assert expression instanceof IntegerIA || expression instanceof IdentIA;
		}

		public void action() {
			if (expression instanceof IntegerIA) {
				action_IntegerIA();
			} else if (expression instanceof IdentIA) {
				action_IdentIA();
			} else {
				throw new NotImplementedException();
			}
		}

		public void action_IdentIA() {
			IdentTableEntry idte = generatedFunction.getIdentTableEntry(to_int(expression));
			@NotNull List<InstructionArgument> x = generatedFunction._getIdentIAPathList(expression);
			{
				OS_Element el = null;
				Context ectx = generatedFunction.getFD().getContext();
				for (InstructionArgument ia2 : x) {
					if (ia2 instanceof IntegerIA) {
						@NotNull VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(ia2));
						// TODO will fail if we try to construct a tmp var, but we never try to do that
						assert vte.vtt != VariableTableType.TEMP;
						assert vte.el  != null;
						el    = vte.el;
						ectx  = el.getContext();
					} else if (ia2 instanceof IdentIA) {
						@NotNull IdentTableEntry idte2 = generatedFunction.getIdentTableEntry(to_int(ia2));
						final String s = idte2.getIdent().toString();
						LookupResultList lrl = ectx.lookup(s);
						OS_Element el2 = lrl.chooseBest(null);
						if (el2 == null) {
							int yy=2;
							assert el instanceof VariableStatement;
							VariableStatement vs = (VariableStatement) el;
							@NotNull TypeName tn = vs.typeName();
							OS_Type ty = new OS_Type(tn);
							// s is constructor name
							implement_construct_type(idte2, ty, s);
							return;
						} else {
							el = el2;
							ectx = el2.getContext();
						}
//						implement_construct_type(idte/*??*/, ty, null); // TODO how bout when there is no ctor name
					} else {
						throw new NotImplementedException();
					}
				}
			}
		}

		public void action_IntegerIA() {
			VariableTableEntry vte = generatedFunction.getVarTableEntry(((IntegerIA) expression).getIndex());
			assert vte.type.attached != null; // TODO will fail when empty variable expression
			@Nullable OS_Type ty = vte.type.attached;
			implement_construct_type(vte, ty, null);
		}

		private void implement_construct_type(Constructable co, @Nullable OS_Type aTy, String constructorName) {
			assert aTy != null;
			if (aTy.getType() == OS_Type.Type.USER) {
				TypeName tyn = aTy.getTypeName();
				if (tyn instanceof NormalTypeName) {
					final NormalTypeName tyn1 = (NormalTypeName) tyn;
					String s = tyn1.getName();
					LookupResultList lrl = tyn1.getContext().lookup(s);
					OS_Element best = lrl.chooseBest(null);
					assert best instanceof ClassStatement;
					List<TypeName> gp = ((ClassStatement) best).getGenericPart();
					ClassInvocation clsinv = new ClassInvocation((ClassStatement) best, constructorName);
					if (gp.size() == 0) {

					} else {
						TypeNameList gp2 = ((NormalTypeName) tyn).getGenericPart();
						for (int i = 0; i < gp.size(); i++) {
							final TypeName typeName = gp2.get(i);
							@NotNull OS_Type typeName2;
							try {
								typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
								clsinv.set(i, gp.get(i), typeName2);
							} catch (ResolveError aResolveError) {
								aResolveError.printStackTrace();
							}
						}
					}
					pte.setClassInvocation(clsinv);
					pte.setResolvedElement(best);
					// set FunctionInvocation with pte args
					{
						ConstructorDef cc = null;
						if (constructorName != null) {
							Collection<ConstructorDef> cs = ((ClassStatement) best).getConstructors();
							for (ConstructorDef c : cs) {
								if (c.name().equals(constructorName)) {
									cc = c;
									break;
								}
							}
						}
						// TODO also check arguments
						{
							FunctionInvocation fi = new FunctionInvocation(cc, pte);
							pte.setFunctionInvocation(fi);
						}
					}
				}
			}
			if (co != null) {
				co.setConstructable(pte);
			}
		}

	}

	void implement_construct(GeneratedFunction generatedFunction, Instruction instruction) {
		final Implement_construct ic = newImplement_construct(generatedFunction, instruction);
		ic.action();
	}

	@NotNull
	public DeduceTypes2.Implement_construct newImplement_construct(GeneratedFunction generatedFunction, Instruction instruction) {
		return new Implement_construct(generatedFunction, instruction);
	}

	void resolve_function_return_type(GeneratedFunction generatedFunction) {
		@Nullable final InstructionArgument vte_index = generatedFunction.vte_lookup("Result");
		if (vte_index != null) {
			final VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(vte_index));

			if (vte.type.attached != null) {
				phase.typeDecided(generatedFunction, vte.type.attached);
			} else {
				@NotNull Collection<TypeTableEntry> pot1 = vte.potentialTypes();
				ArrayList<TypeTableEntry> pot = new ArrayList<TypeTableEntry>(pot1);
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
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.attached = attached;
		} else if (y instanceof FunctionDef) {
			FunctionDef functionDef = ((FunctionDef) y);
			OS_Type attached = new OS_FuncType(functionDef);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
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
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.attached = attached;
			int yy = 2;
		} else if (y instanceof AliasStatement) {
			System.err.println("396 AliasStatement");
			OS_Element x = DeduceLookupUtils._resolveAlias((AliasStatement) y);
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
		return resolve_type(module, type, ctx);
	}

	@NotNull
	static OS_Type resolve_type(final OS_Module module, final OS_Type type, final Context ctx) throws ResolveError {
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
								best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						return new OS_Type((ClassStatement) best);
					}
				case String_:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("String_");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("ConstString"); // TODO not sure about String
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						return new OS_Type((ClassStatement) best);
					}
				case SystemCharacter:
					{
						String typeName = type.getBType().name();
						assert typeName.equals("SystemCharacter");
						OS_Module prelude = module.prelude;
						if (prelude == null) // README Assume `module' IS prelude
							prelude = module;
						final LookupResultList lrl = prelude.getContext().lookup("SystemCharacter");
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
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
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(tn, tn1.getContext());
						OS_Element best = lrl.chooseBest(null);
						while (best instanceof AliasStatement) {
							best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
						}
						if (best == null) {
							if (tn.asSimpleString().equals("Any"))
								return new OS_AnyType();
							throw new ResolveError(tn1, lrl);
						}

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
			System.out.println("594 "+identIA.getEntry().getStatus());

			resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {
				@Override
				public void foundElement(OS_Element e) {
					System.out.println("600 "+generatedFunction.getIdentIAPathNormal(identIA)+" "+e);
					System.out.println("601 "+identIA.getEntry().getStatus());
					assert e == identIA.getEntry().resolved_element;
					set_resolved_element_pte(identIA, e, pte);
				}

				@Override
				public void noFoundElement() {
					// TODO create Diagnostic and quit
					System.out.println("1005 Can't find element for " + generatedFunction.getIdentIAPathNormal(identIA));
				}
			});
		}
		List<TypeTableEntry> args = pte.getArgs();
		for (int i = 0; i < args.size(); i++) {
			final TypeTableEntry tte = args.get(i); // TODO this looks wrong
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
				do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, (IdentExpression) e);
				break;
			case PROCEDURE_CALL:
				{
					final ProcedureCallExpression pce = (ProcedureCallExpression) e;
					try {
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(pce.getLeft(), ctx);
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
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						int y=2;
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
					public void foundElement(OS_Element el) {
						if (pte.resolved_element == null)
							pte.resolved_element = el;
						if (el instanceof FunctionDef) {
							FunctionDef fd = (FunctionDef) el;
							forFunction(new FunctionInvocation(fd, pte), new ForFunction() {
								@Override
								public void typeDecided(OS_Type aType) {
									assert false;
									assert fd == generatedFunction.getFD();
									//
									@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aType, pte.expression, pte);
									vte.addPotentialType(instructionIndex, tte);
								}
							});
						} else if (el instanceof ClassStatement) {
							ClassStatement kl = (ClassStatement) el;
							OS_Type type = new OS_Type(kl);
							@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, pte.expression, pte);
							vte.addPotentialType(instructionIndex, tte);
							vte.setConstructable(pte);
						} else {
							System.err.println("7890 "+el.getClass().getName());
						}
					}

					@Override
					public void noFoundElement() {
						System.err.println("IdentIA path cannot be resolved "+ x);
					}
				});
			}
		}
	}

	public static void set_resolved_element_pte(Constructable co, OS_Element e, ProcTableEntry pte) {
		pte.setResolvedElement(e);
		if (e instanceof ClassStatement) {
			ClassInvocation ci = new ClassInvocation((ClassStatement) e, null);
			FunctionInvocation fi = new FunctionInvocation(null, pte);
//						fi.setClassInvocation(ci);
			pte.setClassInvocation(ci);
			pte.setFunctionInvocation(fi);

			if (co != null)
				co.setConstructable(pte);
		} else if (e instanceof FunctionDef) {
			FunctionInvocation fi = new FunctionInvocation((FunctionDef) e, pte);
			pte.setFunctionInvocation(fi);
		} else {
			System.err.println("845 Unknown element for ProcTableEntry "+e);
		}
	}

	private void do_assign_call_args_ident(GeneratedFunction generatedFunction,
										   Context ctx,
										   VariableTableEntry vte,
										   int aInstructionIndex,
										   ProcTableEntry aPte,
										   int aI,
										   TypeTableEntry aTte,
										   IdentExpression aE) {
		final String e_text = aE.getText();
		final InstructionArgument vte_ia = generatedFunction.vte_lookup(e_text);
//		System.out.println("10000 "+vte_ia);
		if (vte_ia != null) {
			final VariableTableEntry vte1 = generatedFunction.getVarTableEntry(to_int(vte_ia));
			final Promise<TypeTableEntry, Void, Void> p = vte1.promise();
			p.done(new DoneCallback<TypeTableEntry>() {
				@Override
				public void onDone(TypeTableEntry result) {
					assert vte != vte1;
					aTte.attached = result.attached;
					vte.addPotentialType(aInstructionIndex, result);
				}
			});
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					final List<TypeTableEntry> ll = getPotentialTypesVte(generatedFunction, vte_ia);
					doLogic(ll);
				}

				public void doLogic(List<TypeTableEntry> potentialTypes) {
					assert potentialTypes.size() >= 0;
					switch (potentialTypes.size()) {
						case 1:
//							tte.attached = ll.get(0).attached;
//							vte.addPotentialType(instructionIndex, ll.get(0));
							if (p.isResolved())
								System.out.printf("1047 (vte already resolved) vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, potentialTypes.get(0));
							else
								vte1.typeDeferred.resolve(potentialTypes.get(0));
							break;
						case 0:
							LookupResultList lrl = ctx.lookup(e_text);
							OS_Element best = lrl.chooseBest(null);
							if (best instanceof FormalArgListItem) {
								@NotNull final FormalArgListItem fali = (FormalArgListItem) best;
								@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(
										TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken(), vte1);
								if (p.isResolved())
									System.out.printf("890 Already resolved type: vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, tte1);
								else
									vte1.typeDeferred.resolve(tte1);
//								vte.type = tte1;
//								tte.attached = tte1.attached;
//								vte.setStatus(BaseTableEntry.Status.KNOWN, best);
							} else if (best instanceof VariableStatement) {
								final VariableStatement vs = (VariableStatement) best;
								//
								assert vs.getName().equals(e_text);
								//
								@Nullable InstructionArgument vte2_ia = generatedFunction.vte_lookup(vs.getName());
								VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(vte2_ia));
								if (p.isResolved())
									System.out.printf("915 Already resolved type: vte2.type = %s, gf = %s %n", vte1.type, generatedFunction);
								else
									vte1.typeDeferred.resolve(vte2.type);
//								vte.type = vte2.type;
//								tte.attached = vte.type.attached;
								vte.setStatus(BaseTableEntry.Status.KNOWN, best);
								vte2.setStatus(BaseTableEntry.Status.KNOWN, best); // TODO ??
							} else {
								int y = 2;
								System.err.println("543 " + best.getClass().getName());
								throw new NotImplementedException();
							}
							break;
						default:
							// TODO hopefully this works
							final ArrayList<TypeTableEntry> potentialTypes1 = new ArrayList<TypeTableEntry>(
									Collections2.filter(potentialTypes, new Predicate<TypeTableEntry>() {
										@Override
										public boolean apply(@org.checkerframework.checker.nullness.qual.Nullable TypeTableEntry input) {
											assert input != null;
											return input.attached != null;
										}
									}));
							// prevent infinite recursion
							if (potentialTypes1.size() < potentialTypes.size())
								doLogic(potentialTypes1);
							else
								System.out.println("913 Don't know");
							break;
					}
				}
			};
			onFinish(runnable);
		} else {
			int ia = generatedFunction.addIdentTableEntry(aE, ctx);
			IdentTableEntry idte = generatedFunction.getIdentTableEntry(ia);
			idte.addPotentialType(aInstructionIndex, aTte); // TODO DotExpression??
			final int ii = aI;
			idte.onType(phase, new OnType() {
				@Override
				public void typeDeduced(OS_Type aType) {
					aPte.setArgType(ii, aType); // TODO does this belong here or in FunctionInvocation?
					aTte.attached = aType; // since we know that tte.attached is always null here
				}

				@Override
				public void noTypeFound() {
					System.err.println("719 no type found "+generatedFunction.getIdentIAPathNormal(new IdentIA(ia, generatedFunction)));
				}
			});
		}
	}

	private void do_assign_call_GET_ITEM(GetItemExpression gie, TypeTableEntry tte, GeneratedFunction generatedFunction, Context ctx) {
		try {
			final LookupResultList lrl = DeduceLookupUtils.lookupExpression(gie.getLeft(), ctx);
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
												@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aType, idte); // TODO expression?
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
				} else if (best instanceof FormalArgListItem) {
					final FormalArgListItem fali = (FormalArgListItem) best;
					String s = fali.name();
					@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(s);
					if (vte_ia != null) {
						VariableTableEntry vte2 = generatedFunction.getVarTableEntry(to_int(vte_ia));

	//					final @Nullable OS_Type ty2 = vte2.type.attached;
						vte2.typeDeferred.promise().done(new DoneCallback<TypeTableEntry>() {
							@Override
							public void onDone(TypeTableEntry result) {
	//							assert false; // TODO this code is never reached
								final @Nullable OS_Type ty2 = result.attached;
								assert ty2 != null;
								OS_Type rtype = null;
								try {
									rtype = resolve_type(ty2, ctx);
								} catch (ResolveError resolveError) {
	//								resolveError.printStackTrace();
									errSink.reportError("Cant resolve " + ty2); // TODO print better diagnostic
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
												@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, aType, vte2); // TODO expression?
												vte2.type = tte1;
											}
										});
									} else {
										throw new NotImplementedException();
									}
								} else {
									throw new NotImplementedException();
								}
							}
						});
	//					vte2.onType(phase, new OnType() {
	//						@Override public void typeDeduced(final OS_Type ty2) {
	//						}
	//
	//						@Override
	//						public void noTypeFound() {
	//							throw new NotImplementedException();
	//						}
	//					});
	/*
						if (ty2 == null) {
							@NotNull TypeTableEntry tte3 = generatedFunction.newTypeTableEntry(
									TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken());
							vte2.type = tte3;
	//						ty2 = vte2.type.attached; // TODO this is final, but why assign anyway?
						}
	*/
					}
				} else {
					final int y=2;
					throw new NotImplementedException();
				}
			} else {
				final int y=2;
				throw new NotImplementedException();
			}
		} catch (ResolveError aResolveError) {
			aResolveError.printStackTrace();
			int y=2;
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

	private void do_assign_constant(final GeneratedFunction generatedFunction, final Instruction instruction, final IdentTableEntry idte, final ConstTableIA i2) {
		if (idte.type != null && idte.type.attached != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.attached == null) {
			System.out.println("*** ERROR: Null type in CTE "+cte);
		}
		// idte.type may be null, but we still addPotentialType here
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
				final InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) e).getText());
				final List<TypeTableEntry> ll = getPotentialTypesVte(generatedFunction, vte_ia);
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

	private void implement_calls_(final GeneratedFunction gf,
								  final Context context,
								  final InstructionArgument i2,
								  final ProcTableEntry pte,
								  final int pc) {
		final IExpression pn1 = pte.expression;
		if (!(pn1 instanceof IdentExpression)) {
			throw new IllegalStateException("pn1 is not IdentExpression");
		}

		final String pn = ((IdentExpression) pn1).getText();
		boolean found = lookup_name_calls(context, pn, pte);
		if (found) return;

		final String pn2 = SpecialFunctions.reverse_name(pn);
		if (pn2 != null) {
//			System.out.println("7002 "+pn2);
			found = lookup_name_calls(context, pn2, pte);
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
//					System.out.println("7003 "+vteName+" "+ctx);
					final OS_Element best2 = lrl2.chooseBest(null);
					if (best2 != null) {
						found = lookup_name_calls(best2.getContext(), pn, pte);
						if (found) return;

						if (pn2 != null) {
							found = lookup_name_calls(best2.getContext(), pn2, pte);
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
				final List<TypeTableEntry> tt = getPotentialTypesVte(vte);
				if (tt.size() == 1) {
					final OS_Type x = tt.get(0).attached;
					assert x != null;
					assert x.getType() != null;
					if (x.getType() == OS_Type.Type.USER_CLASS) {
						final Context ctx1 = x.getClassOf().getContext();

						found = lookup_name_calls(ctx1, pn, pte);
						if (found) return;

						if (pn2 != null) {
							found = lookup_name_calls(ctx1, pn2, pte);
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
			assert Pattern.matches("__.*__", ((IdentExpression) pn1).getText());
			System.err.println("i2 is not IntegerIA ("+i2.getClass().getName()+")");
		}

	}

	private boolean lookup_name_calls(final Context ctx, final String pn, final ProcTableEntry pte) {
		final LookupResultList lrl = ctx.lookup(pn);
		final OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			set_resolved_element_pte(null, best, pte); // TODO check arity and arg matching
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

	public void resolveIdentIA2_(Context context, IdentIA identIA, GeneratedFunction generatedFunction, FoundElement foundElement) {
		final List<InstructionArgument> s = generatedFunction._getIdentIAPathList(identIA);
		resolveIdentIA2_(context, s, generatedFunction, foundElement);
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
							case FUNC_EXPR: {
								FuncExpr x = (FuncExpr) attached.getElement();
								ectx = x.getContext();
								break;
							}
							default:
								System.err.println("1010 " + attached.getType());
								throw new IllegalStateException("Don't know what you're doing here.");
							}
						} else {
							TypeTableEntry tte = pot.get(0);
							if (tte.expression instanceof ProcedureCallExpression) {
								if (tte.tableEntry != null) {
									assert tte.tableEntry instanceof ProcTableEntry;
									ProcTableEntry pte = (ProcTableEntry) tte.tableEntry;
									IdentIA x = (IdentIA) pte.expression_num;
									@NotNull IdentTableEntry y = x.getEntry();
									if (y.resolved_element == null) {
										if (y.backlink instanceof ProcIA) {
											final ProcIA backlink_ = (ProcIA) y.backlink;
											@NotNull ProcTableEntry backlink = generatedFunction.getProcTableEntry(backlink_.getIndex());
											assert backlink.resolved_element != null;
											try {
												LookupResultList lrl2 = DeduceLookupUtils.lookupExpression(y.getIdent(), backlink.resolved_element.getContext());
												@Nullable OS_Element best = lrl2.chooseBest(null);
												assert best != null;
												y.setStatus(BaseTableEntry.Status.KNOWN, best);
											} catch (ResolveError aResolveError) {
												aResolveError.printStackTrace();
												assert false;
											}
										} else
											assert false;
									}
									FunctionInvocation fi = new FunctionInvocation((FunctionDef) y.resolved_element, pte);
									int yyy=2;
									if (pte.getFunctionInvocation() == null) {
										pte.setFunctionInvocation(fi);
									}
									el = y.resolved_element;
									ectx = el.getContext();
								}
							}
/*
							OS_Element el2 = DeduceLookupUtils.lookup(tte.expression.getLeft(), ectx);
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
*/
						}
					}
				} else {
					errSink.reportError("1001 Can't resolve "+text);
					//return null;
					foundElement.doNoFoundElement();
					return;
				}
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = ((IdentIA) ia).getEntry();
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
					} else if (false) {
						resolveIdentIA2_(ectx/*context*/, s, generatedFunction, new FoundElement(phase) {
							final String z = generatedFunction.getIdentIAPathNormal((IdentIA) ia);

							@Override
							public void foundElement(OS_Element e) {
								foundElement.doFoundElement(e);
								idte.setStatus(BaseTableEntry.Status.KNOWN, e);
							}

							@Override
							public void noFoundElement() {
								foundElement.noFoundElement();
								System.out.println("2002 Cant resolve " + z);
								idte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
							}
						});
					}
//					assert idte.getStatus() != BaseTableEntry.Status.UNCHECKED;
				} else if (idte.getStatus() == BaseTableEntry.Status.KNOWN) {
					el = idte.resolved_element;
					ectx = el.getContext();
				}
			} else if (ia instanceof ProcIA) {
				ProcTableEntry prte = generatedFunction.getProcTableEntry(to_int(ia));
				int y=2;
				if (prte.resolved_element == null) {
					IExpression exp = prte.expression;
					if (exp instanceof ProcedureCallExpression) {
						final ProcedureCallExpression pce = (ProcedureCallExpression) exp;
						exp = pce.getLeft(); // TODO might be another pce??!!
						if (exp instanceof ProcedureCallExpression)
							throw new IllegalArgumentException("double pce!");
					}
					try {
						LookupResultList lrl = DeduceLookupUtils.lookupExpression(exp, ectx);
						el = lrl.chooseBest(null);
						ectx = el.getContext();
						prte.resolved_element = el;
						// handle constructor calls
						if (el instanceof ClassStatement) {
							assert prte.getClassInvocation() == null;
							ClassInvocation ci = new ClassInvocation((ClassStatement) el, null);
	//						prte.setClassInvocation(ci);
							Collection<ConstructorDef> cs = (((ClassStatement) el).getConstructors());
							// TODO find a ctor that matches prte.getArgs()
							if (prte.getArgs().size() == 0 && cs.size() == 0) {
								// TODO use a virtual default ctor
							}
							FunctionDef selected_constructor = null;
							FunctionInvocation fi = new FunctionInvocation(selected_constructor, prte);
		//					fi.setClassInvocation(ci);
							prte.setFunctionInvocation(fi);
						}
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						int yyy=2;
						throw new NotImplementedException();
					}
				} else {
					el = prte.resolved_element;
					ectx = el.getContext();
				}
			} else
				throw new IllegalStateException("Really cant be here");
		}
		final String s1 = generatedFunction.getIdentIAPathNormal(identIA);
		if (s.size() > 1) {
			final OS_Element el2 = el;
			InstructionArgument x = s.get(s.size() - 1);
			if (x instanceof IntegerIA) {
				assert false;
				@NotNull VariableTableEntry y = generatedFunction.getVarTableEntry(to_int(x));
				y.setStatus(BaseTableEntry.Status.KNOWN, el);
			} else if (x instanceof IdentIA) {
				@NotNull IdentTableEntry y = generatedFunction.getIdentTableEntry(to_int(x));
				y.addStatusListener(new BaseTableEntry.StatusListener() {
					@Override
					public void onChange(OS_Element el3, BaseTableEntry.Status newStatus) {
						if (newStatus == BaseTableEntry.Status.KNOWN) {
//							assert el2 != el3;
							System.out.println("1424 Found for " + s1);
							foundElement.doFoundElement(el3);
						}
					}
				});
			}
		} else {
			System.out.println("1431 Found for " + s1);
			foundElement.doFoundElement(el);
		}
		{
			InstructionArgument x = s.get(0);
			if (x instanceof IntegerIA) {
				@NotNull VariableTableEntry y = generatedFunction.getVarTableEntry(to_int(x));
				y.setStatus(BaseTableEntry.Status.KNOWN, el);
			} else if (x instanceof IdentIA) {
				@NotNull IdentTableEntry y = generatedFunction.getIdentTableEntry(to_int(x));
				assert y.getStatus() == BaseTableEntry.Status.KNOWN;
//				y.setStatus(BaseTableEntry.Status.KNOWN, el);
			} else if (x instanceof ProcIA) {
				@NotNull ProcTableEntry y = generatedFunction.getProcTableEntry(to_int(x));
//				y.setStatus(BaseTableEntry.Status.KNOWN, el); // TODO
			} else
				throw new NotImplementedException();
		}
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
							try {
								LookupResultList lrl = DeduceLookupUtils.lookupExpression(pot.get(0).expression.getLeft(), ctx);
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
							} catch (ResolveError aResolveError) {
								aResolveError.printStackTrace();
								int y=2;
								throw new NotImplementedException();
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
						if (attached1TypeName instanceof RegularTypeName) {
							try {
								ectx = DeduceLookupUtils.lookupExpression(((RegularTypeName) attached1TypeName).getRealName(), ectx).results().get(0).getElement().getContext();
							} catch (ResolveError aResolveError) {
								aResolveError.printStackTrace();
								int y=2;
								throw new NotImplementedException();
							}
						} else if (attached1.getType() == OS_Type.Type.USER_CLASS) {
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
							try {
								if (!vs.typeName().isNull()) {
									TypeTableEntry tte;
									OS_Type attached;
									if (/*vs.typeName() == null &&*/ vs.initialValue() != IExpression.UNASSIGNED) { // TODO was always false
										attached = DeduceLookupUtils.deduceExpression(vs.initialValue(), ectx);
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
									OS_Type attached = DeduceLookupUtils.deduceExpression(vs.initialValue(), ectx);
									TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, vs.initialValue());
									idte2.type = tte;
								} else {
									System.err.println("Empty Variable Expression");
									throw new IllegalStateException("Empty Variable Expression");
	//								return; // TODO call noFoundElement, raise exception
								}
							} catch (ResolveError aResolveError) {
								System.err.println("1937 resolve error "+vs.getName());
								aResolveError.printStackTrace();
							}
						} else if (el instanceof FunctionDef) {
							OS_Type attached = new OS_UnknownType(el);
							TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, idte2);
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
								errSink.reportDiagnostic(resolveError);
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

	public class FoundParent implements BaseTableEntry.StatusListener {
		private IdentTableEntry ite;
		private BaseTableEntry bte;
		private Context ctx;

		public FoundParent(BaseTableEntry bte, IdentTableEntry ite, Context ctx) {
			this.ite = ite;
			this.bte = bte;
			this.ctx = ctx;
		}

		void found_element_for_ite2(GeneratedFunction generatedFunction, IdentTableEntry ite, Context ctx) {
			OS_Element y = ite.resolved_element;

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
					ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
				} else
					ite.type.attached = attached;
			} else if (y instanceof FunctionDef) {
				FunctionDef functionDef = ((FunctionDef) y);
				OS_Type attached = new OS_FuncType(functionDef);
				if (ite.type == null) {
					ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
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
					ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
				} else
					ite.type.attached = attached;
				int yy = 2;
			} else if (y instanceof AliasStatement) {
				System.err.println("396 AliasStatement");
				OS_Element x = DeduceLookupUtils._resolveAlias((AliasStatement) y);
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

		@Override
		public void onChange(OS_Element el, BaseTableEntry.Status newStatus) {
			if (newStatus == BaseTableEntry.Status.KNOWN) {
				if (bte instanceof VariableTableEntry) {
					final VariableTableEntry vte = (VariableTableEntry) bte;
					@NotNull ArrayList<TypeTableEntry> pot = getPotentialTypesVte(vte);
					if (vte.getStatus() == BaseTableEntry.Status.KNOWN && vte.type.attached != null && vte.el != null) {

						final OS_Type ty = vte.type.attached;

						OS_Element ele2 = null;

						if (ty.getType() == OS_Type.Type.USER) {
							@NotNull OS_Type ty2 = null;
							try {
								ty2 = resolve_type(ty, ty.getTypeName().getContext());
								OS_Element ele = ty2.getElement();
								LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
								ele2 = lrl.chooseBest(null);
							} catch (ResolveError aResolveError) {
								errSink.reportDiagnostic(aResolveError);
							}
						} else
							ele2 = ty.getClassOf(); // TODO might fail later (use getElement?)

						LookupResultList lrl = null;
						try {
							lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele2.getContext());
							OS_Element best = lrl.chooseBest(null);
//							ite.setStatus(BaseTableEntry.Status.KNOWN, best); // README infinite loop
//							tte = new tte
							ite.setResolvedElement(best);
						} catch (ResolveError aResolveError) {
							aResolveError.printStackTrace();
						}
					} else if (pot.size() == 1) {
						TypeTableEntry tte = pot.get(0);
						@Nullable OS_Type ty = tte.attached;
						if (ty != null) {
							if (ty.getType() == OS_Type.Type.USER) {
								try {
									@NotNull OS_Type ty2 = resolve_type(ty, ty.getTypeName().getContext());
									OS_Element ele = ty2.getElement();
									LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
									OS_Element best = lrl.chooseBest(null);
									ite.setStatus(BaseTableEntry.Status.KNOWN, best);
//									ite.setResolvedElement(best);
								} catch (ResolveError resolveError) {
									errSink.reportDiagnostic(resolveError);
								}
							} else if (ty.getType() == OS_Type.Type.USER_CLASS) {
								OS_Element ele = ty.getClassOf();
								LookupResultList lrl = null;
								try {
									lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
									OS_Element best = lrl.chooseBest(null);
//									ite.setStatus(BaseTableEntry.Status.KNOWN, best);
									assert best != null;
									ite.setResolvedElement(best);
								} catch (ResolveError aResolveError) {
									aResolveError.printStackTrace();
								}
							}
						} else {
							System.err.println("1696");
						}
					}
				} else {
					System.out.println("1621");
					LookupResultList lrl = null;
					try {
						lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ctx);
						OS_Element best = lrl.chooseBest(null);
						assert best != null;
						ite.setResolvedElement(best);
						found_element_for_ite(null, ite, best, ctx);
//						ite.setStatus(BaseTableEntry.Status.KNOWN, best);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
					}
				}
			}
		}
	}
}

//
//
//
