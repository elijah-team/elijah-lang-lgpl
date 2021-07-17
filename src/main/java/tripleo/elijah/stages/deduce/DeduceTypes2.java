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
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialFunctions;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.deduce.declarations.DeferredMember;
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
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created 9/15/20 12:51 PM
 */
public class DeduceTypes2 {
	private final OS_Module module;
	private final DeducePhase phase;
	private final ErrSink errSink;
	WorkManager wm = new WorkManager();

	public DeduceTypes2(OS_Module module, DeducePhase phase) {
		this.module = module;
		this.phase = phase;
		this.errSink = module.parent.getErrSink();
	}

	public void deduceFunctions(final @NotNull Iterable<GeneratedNode> lgf) {
		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				deduceOneFunction(generatedFunction, phase);
			}
		}
		List<GeneratedNode> generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		int size;
		do {
			size = 0;
			{
				for (GeneratedNode generatedNode : generatedClasses) {
					GeneratedContainerNC generatedContainerNC = (GeneratedContainerNC) generatedNode;
					Collection<GeneratedFunction> lgf2 = generatedContainerNC.functionMap.values();
					for (final GeneratedFunction generatedFunction : lgf2) {
						if (deduceOneFunction(generatedFunction, phase))
							size++;
					}
				}
			}
			generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		} while (size > 0);
		do {
			size = 0;
			{
				for (GeneratedNode generatedNode : generatedClasses) {
					if (!(generatedNode instanceof GeneratedClass)) continue;
//					GeneratedContainerNC generatedContainerNC = (GeneratedContainerNC) generatedNode;
					GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					Collection<GeneratedConstructor> lgf2 = generatedClass.constructors.values();
					for (final GeneratedConstructor generatedConstructor : lgf2) {
						if (deduceOneConstructor(generatedConstructor, phase))
							size++;
					}
				}
			}
			generatedClasses = new ArrayList<GeneratedNode>(phase.generatedClasses);
		} while (size > 0);
	}

	public boolean deduceOneFunction(GeneratedFunction aGeneratedFunction, DeducePhase aDeducePhase) {
		if (aGeneratedFunction.deducedAlready) return false;
		deduce_generated_function(aGeneratedFunction);
		aGeneratedFunction.deducedAlready = true;
		for (IdentTableEntry identTableEntry : aGeneratedFunction.idte_list) {
			if (identTableEntry.resolved_element instanceof VariableStatement) {
				final VariableStatement vs = (VariableStatement) identTableEntry.resolved_element;
				OS_Element el = vs.getParent().getParent();
				OS_Element el2 = aGeneratedFunction.getFD().getParent();
				if (el != el2) {
					if (el instanceof ClassStatement || el instanceof NamespaceStatement)
						// NOTE there is no concept of gf here
						aDeducePhase.registerResolvedVariable(identTableEntry, el, vs.getName());
				}
			}
		}
		{
			final GeneratedFunction gf = aGeneratedFunction;

			@Nullable InstructionArgument result_index = gf.vte_lookup("Result");
			if (result_index == null) {
				// if there is no Result, there should be Value
				result_index = gf.vte_lookup("Value");
				// but Value might be passed in. If it is, discard value
				if (result_index != null) {
					@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
					if (vte.vtt != VariableTableType.RESULT) {
						result_index = null;
					}
				}
			}
			if (result_index != null) {
				@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
				if (vte.resolvedType() == null) {
					OS_Type a = vte.type.getAttached();
					if (a != null) {
						// see resolve_function_return_type
						switch (a.getType()) {
							case USER_CLASS:
								dof_uc(vte, a);
								break;
							case USER:
								try {
									@NotNull OS_Type rt = resolve_type(a, a.getTypeName().getContext());
									dof_uc(vte, rt);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}
								break;
							default:
								// TODO do nothing for now
								int y3 = 2;
								break;
						}
					} /*else
							throw new NotImplementedException();*/
				}
			}
		}
		aDeducePhase.addFunction(aGeneratedFunction, (FunctionDef) aGeneratedFunction.getFD());
		return true;
	}

	public boolean deduceOneConstructor(GeneratedConstructor aGeneratedConstructor, DeducePhase aDeducePhase) {
		if (aGeneratedConstructor.deducedAlready) return false;
		deduce_generated_function_base(aGeneratedConstructor, aGeneratedConstructor.getFD());
		aGeneratedConstructor.deducedAlready = true;
		for (IdentTableEntry identTableEntry : aGeneratedConstructor.idte_list) {
			if (identTableEntry.resolved_element instanceof VariableStatement) {
				final VariableStatement vs = (VariableStatement) identTableEntry.resolved_element;
				OS_Element el = vs.getParent().getParent();
				OS_Element el2 = aGeneratedConstructor.getFD().getParent();
				if (el != el2) {
					if (el instanceof ClassStatement || el instanceof NamespaceStatement)
						// NOTE there is no concept of gf here
						aDeducePhase.registerResolvedVariable(identTableEntry, el, vs.getName());
				}
			}
		}
		{
			final GeneratedConstructor gf = aGeneratedConstructor;

			@Nullable InstructionArgument result_index = gf.vte_lookup("Result");
			if (result_index == null) {
				// if there is no Result, there should be Value
				result_index = gf.vte_lookup("Value");
				// but Value might be passed in. If it is, discard value
				if (result_index != null) {
					@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
					if (vte.vtt != VariableTableType.RESULT) {
						result_index = null;
					}
				}
			}
			if (result_index != null) {
				@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
				if (vte.resolvedType() == null) {
					OS_Type a = vte.type.getAttached();
					if (a != null) {
						// see resolve_function_return_type
						switch (a.getType()) {
							case USER_CLASS:
								dof_uc(vte, a);
								break;
							case USER:
								try {
									@NotNull OS_Type rt = resolve_type(a, a.getTypeName().getContext());
									dof_uc(vte, rt);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}
								break;
							default:
								// TODO do nothing for now
								int y3 = 2;
								break;
						}
					} /*else
							throw new NotImplementedException();*/
				}
			}
		}
//		aDeducePhase.addFunction(aGeneratedConstructor, (FunctionDef) aGeneratedConstructor.getFD()); // TODO do we need this?
		return true;
	}

	private void dof_uc(@NotNull VariableTableEntry aVte, OS_Type aA) {
		// we really want a ci from somewhere
		assert aA.getClassOf().getGenericPart().size() == 0;
		ClassInvocation ci = new ClassInvocation(aA.getClassOf(), null);
		ci = phase.registerClassInvocation(ci);
		ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(GeneratedClass result) {
				aVte.resolveType(result);
			}
		});
	}

	List<Runnable> onRunnables = new ArrayList<Runnable>();

	void onFinish(Runnable r) {
		onRunnables.add(r);
	}

	public void deduce_generated_constructor(final GeneratedFunction generatedFunction) {
		final ConstructorDef fd = (ConstructorDef) generatedFunction.getFD();
		deduce_generated_function_base(generatedFunction, fd);
	}

	public void deduce_generated_function(final GeneratedFunction generatedFunction) {
		final FunctionDef fd = (FunctionDef) generatedFunction.getFD();
		deduce_generated_function_base(generatedFunction, fd);
	}

	public void deduce_generated_function_base(final BaseGeneratedFunction generatedFunction, BaseFunctionDef fd) {
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
						resolve_cte_expression(cte, context);
					}
					//
					// add proc table listeners
					//
					add_proc_table_listeners(generatedFunction);
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
						if (vte.type.getAttached() != null && vte.type.getAttached().getType() == OS_Type.Type.USER) {
							final TypeName x = vte.type.getAttached().getTypeName();
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
										vte.type.setAttached(new OS_Type((ClassStatement) best));
										vte.type.genTypeCI(genCI(vte.type));
										((ClassInvocation) vte.type.genType.ci).resolvePromise().done(new DoneCallback<GeneratedClass>() {
											@Override
											public void onDone(GeneratedClass result) {
												vte.type.genType.node = result;
												vte.resolveType(result);
											}
										});
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
					{
						// TODO why are we doing this?
						Resolve_each_typename ret = new Resolve_each_typename(phase, this, errSink);
						for (TypeTableEntry typeTableEntry : generatedFunction.tte_list) {
							ret.action(typeTableEntry);
						}
					}
					{
						final WorkManager workManager = wm;//new WorkManager();
						Dependencies deps = new Dependencies(/*phase, this, errSink*/);
						for (GenType genType : generatedFunction.dependentTypes()) {
							deps.action_type(genType, workManager);
						}
						for (FunctionInvocation dependentFunction : generatedFunction.dependentFunctions()) {
							deps.action_function(dependentFunction, workManager);
						}
						workManager.drain();
					}
					//
					// RESOLVE FUNCTION RETURN TYPES
					//
					resolve_function_return_type(generatedFunction);
					//
					// LOOKUP FUNCTIONS
					//
					{
						for (ProcTableEntry pte : generatedFunction.prte_list) {
							lookup_function_on_exit(pte);
						}
						wm.drain();
					}
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
						final IdentTableEntry idte = arg.getEntry();
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
								idte2.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best1));
								// TODO check for elements which may contain type information
								if (best1 instanceof VariableStatement) {
									final VariableStatement vs = (VariableStatement) best1;
									deferred_member(vs.getParent().getParent(), null, vs)
										.typePromise()
										.done(new DoneCallback<GenType>() {
											@Override
											public void onDone(GenType result) {
												assert result.resolved != null;
												idte2.type.setAttached(result.resolved);
											}
										});
								}
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
					System.err.println("298 Calling "+x);
					resolveIdentIA_(context, expression, generatedFunction, new FoundElement(phase) {

						@SuppressWarnings("unused") final String xx = x;

						@Override
						public void foundElement(OS_Element e) {
							pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, expression));
							if (fd instanceof DefFunctionDef) {
								final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
								forFunction(newFunctionInvocation((FunctionDef) e, pte, invocation, phase), new ForFunction() {
									@Override
									public void typeDecided(OS_Type aType) {
										@Nullable InstructionArgument x = generatedFunction.vte_lookup("Result");
										assert x != null;
										((IntegerIA) x).getEntry().type.setAttached(aType);
									}
								});
							}
						}

						@Override
						public void noFoundElement() {
							errSink.reportError("370 Can't find callsite "+x);
							// TODO don't know if this is right
							@NotNull IdentTableEntry entry = expression.getEntry();
							if (entry.getStatus() != BaseTableEntry.Status.UNKNOWN)
								entry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
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
				// README for GenerateC, etc: marks the spot where a declaration should go. Wouldn't be necessary if we had proper Range's
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
			if (vte.type.getAttached() == null) {
				int potential_size = vte.potentialTypes().size();
				if (potential_size == 1)
					vte.type.setAttached(getPotentialTypesVte(vte).get(0).getAttached());
				else if (potential_size > 1) {
					// TODO Check type compatibility
					System.err.println("703 "+vte.getName()+" "+vte.potentialTypes());
					errSink.reportDiagnostic(new CantDecideType(vte, vte.potentialTypes()));
				} else {
					// potential_size == 0
					// Result is handled by phase.typeDecideds, self is always valid
					if (/*vte.getName() != null &&*/ !(vte.vtt == VariableTableType.RESULT || vte.vtt == VariableTableType.SELF))
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

	private void add_proc_table_listeners(BaseGeneratedFunction generatedFunction) {
		for (ProcTableEntry pte : generatedFunction.prte_list) {
			pte.addStatusListener(new BaseTableEntry.StatusListener() {
				@Override
				public void onChange(final IElementHolder eh, final BaseTableEntry.Status newStatus) {
					Constructable co = null;
					if (eh instanceof ConstructableElementHolder) {
						final ConstructableElementHolder constructableElementHolder = (ConstructableElementHolder) eh;
						co = constructableElementHolder.getConstructable();
					}
					set_resolved_element_pte(co, eh.getElement(), pte);
				}

				void set_resolved_element_pte(final Constructable co, final OS_Element e, final ProcTableEntry pte) {
					ClassInvocation ci;
					FunctionInvocation fi;
					GenType genType = null;

//								pte.setResolvedElement(e); // README already done
					if (e instanceof ClassStatement) {
						ci = new ClassInvocation((ClassStatement) e, null);
						ci = phase.registerClassInvocation(ci);
						fi = newFunctionInvocation(ConstructorDef.defaultVirtualCtor, pte, ci, phase);
						pte.setFunctionInvocation(fi);

						if (co != null) {
							co.setConstructable(pte);
							ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
								@Override
								public void onDone(GeneratedClass result) {
									co.resolveType(result);
								}
							});
						}
					} else if (e instanceof FunctionDef) {
						FunctionDef fd = (FunctionDef) e;
						final OS_Element parent = fd.getParent();
						if (parent instanceof NamespaceStatement) {
							final NamespaceStatement namespaceStatement = (NamespaceStatement) parent;
							genType = new GenType(namespaceStatement);
							final NamespaceInvocation nsi = phase.registerNamespaceInvocation(namespaceStatement);
//										pte.setNamespaceInvocation(nsi);
							genType.ci = nsi;
							fi = newFunctionInvocation(fd, pte, nsi, phase);
						} else if (parent instanceof ClassStatement) {
							final ClassStatement classStatement = (ClassStatement) parent;
							genType = new GenType(classStatement);
							ci = new ClassInvocation(classStatement, null);
							ci = phase.registerClassInvocation(ci);
							genType.ci = ci;
							pte.setClassInvocation(ci);
							fi = newFunctionInvocation(fd, pte, ci, phase);
						} else
							throw new IllegalStateException("Unknown parent");
						pte.setFunctionInvocation(fi);
					} else {
						System.err.println("845 Unknown element for ProcTableEntry "+e);
						return;
					}
					if (co != null) { // TODO really should pass in a param
						AbstractDependencyTracker depTracker = null;
						if (co instanceof IdentIA) {
							final IdentIA identIA = (IdentIA) co;
							depTracker = identIA.gf;
						} else if (co instanceof IntegerIA) {
							final IntegerIA integerIA = (IntegerIA) co;
							depTracker = integerIA.gf;
						}
						if (depTracker != null) {
							if (genType == null && fi.getFunction() == null) {
								// README Assume constructor
								final ClassStatement c = fi.getClassInvocation().getKlass();
								final GenType genType2 = new GenType(c);
								depTracker.addDependentType(genType2);
							} else {
								depTracker.addDependentFunction(fi);
								if (genType != null)
									depTracker.addDependentType(genType);
							}
						}
					}
				}
			});

			InstructionArgument en = pte.expression_num;
			if (en != null) {
				if (en instanceof IdentIA) {
					final IdentIA identIA = (IdentIA) en;
					@NotNull IdentTableEntry idte = identIA.getEntry();
					idte.addStatusListener(new BaseTableEntry.StatusListener() {
						@Override
						public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
							if (newStatus != BaseTableEntry.Status.KNOWN)
								return;

							final OS_Element el = eh.getElement();

							@NotNull ElObjectType type = DecideElObjectType.getElObjectType(el);

							switch (type) {
							case NAMESPACE:
								GenType genType = new GenType((NamespaceStatement) el);
								generatedFunction.addDependentType(genType);
								break;
							case CLASS:
								GenType genType2 = new GenType((ClassStatement) el);
								generatedFunction.addDependentType(genType2);
								break;
							case FUNCTION:
								IdentIA identIA2 = null;
								if (pte.expression_num instanceof IdentIA)
									identIA2 = (IdentIA) pte.expression_num;
								if (identIA2 != null) {
									@NotNull IdentTableEntry idte2 = identIA.getEntry();
									ProcTableEntry procTableEntry = idte2.getCallablePTE();
									if (procTableEntry != null) {
										// TODO doesn't seem like we need this
										procTableEntry.onFunctionInvocation(new DoneCallback<FunctionInvocation>() {
											@Override
											public void onDone(FunctionInvocation functionInvocation) {
												ClassInvocation ci = functionInvocation.getClassInvocation();
												NamespaceInvocation nsi = functionInvocation.getNamespaceInvocation();
												// do we register?? probably not
												if (ci == null && nsi == null)
													assert false;
												FunctionInvocation fi = newFunctionInvocation((FunctionDef) el, pte, ci != null ? ci : nsi, phase);
												generatedFunction.addDependentFunction(fi);
											}
										});
										// END
									}
								}
								break;
							default:
								System.err.println(String.format("228 Don't know what to do %s %s", type, el));
								break;
							}
						}
					});
				} else if (en instanceof IntegerIA) {
					final IntegerIA integerIA = (IntegerIA) en;
					@NotNull VariableTableEntry vte = integerIA.getEntry();
					vte.addStatusListener(new BaseTableEntry.StatusListener() {
						@Override
						public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
							if (newStatus != BaseTableEntry.Status.KNOWN)
								return;

							@NotNull VariableTableEntry vte2 = vte;

							final OS_Element el = eh.getElement();

							@NotNull ElObjectType type = DecideElObjectType.getElObjectType(el);

							switch (type) {
							case VAR:
								break;
							default:
								throw new NotImplementedException();
							}
						}
					});
				} else
					throw new NotImplementedException();
			}
		}
	}

	@Nullable
	private String getPTEString(ProcTableEntry pte) {
		String pte_string;
		if (pte == null)
			pte_string = "[]";
		else {
			for (TypeTableEntry typeTableEntry : pte.getArgs()) {
				if (typeTableEntry.getAttached() == null)
					System.err.println("267 attached == null");

				OS_Type attached = typeTableEntry.getAttached();
				int y=2;
			}
			pte_string = null;
		}
		return pte_string;
	}

	/**
	 * See {@link Implement_construct#_implement_construct_type}
	 */
	private ClassInvocation genCI(TypeTableEntry aType) {
		GenType genType = aType.genType;
		if (genType.nonGenericTypeName != null) {
			NormalTypeName aTyn1 = (NormalTypeName) genType.nonGenericTypeName;
			String constructorName = null; // TODO this comes from nowhere
			ClassStatement best = genType.resolved.getClassOf();
			//
			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv = new ClassInvocation(best, constructorName);
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						return null;
					}
				}
			}
			clsinv = phase.registerClassInvocation(clsinv);
			return clsinv;
		}
		if (genType.resolved != null) {
			ClassStatement best = genType.resolved.getClassOf();
			String constructorName = null; // TODO what to do about this, nothing I guess

			List<TypeName> gp = best.getGenericPart();
			ClassInvocation clsinv = new ClassInvocation(best, constructorName);
			assert best.getGenericPart().size() == 0;
/*
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
				for (int i = 0; i < gp.size(); i++) {
					final TypeName typeName = gp2.get(i);
					@NotNull OS_Type typeName2;
					try {
						typeName2 = resolve_type(new OS_Type(typeName), typeName.getContext());
						clsinv.set(i, gp.get(i), typeName2);
					} catch (ResolveError aResolveError) {
						aResolveError.printStackTrace();
						return null;
					}
				}
			}
*/
			clsinv = phase.registerClassInvocation(clsinv);
			return clsinv;
		}
		return null;
	}

	private FunctionInvocation newFunctionInvocation(BaseFunctionDef aFunctionDef, ProcTableEntry aPte, IInvocation aInvocation, DeducePhase aDeducePhase) {
		FunctionInvocation fi = new FunctionInvocation(aFunctionDef, aPte, aInvocation, aDeducePhase.generatePhase);
		// TODO register here
		return fi;
	}

	public String getFileName() {
		return module.getFileName();
	}

	public GenerateFunctions getGenerateFunctions(OS_Module aModule) {
		return phase.generatePhase.getGenerateFunctions(aModule);
	}

	static class Resolve_each_typename {

		private final DeducePhase phase;
		private final DeduceTypes2 dt2;
		private final ErrSink errSink;

		public Resolve_each_typename(DeducePhase aPhase, DeduceTypes2 aDeduceTypes2, ErrSink aErrSink) {
			phase = aPhase;
			dt2 = aDeduceTypes2;
			errSink = aErrSink;
		}

		public void action(TypeTableEntry typeTableEntry) {
			@Nullable OS_Type attached = typeTableEntry.getAttached();
			if (attached == null) return;
			if (attached.getType() == OS_Type.Type.USER) {
				action_USER(typeTableEntry, attached);
			} else if (attached.getType() == OS_Type.Type.USER_CLASS) {
				action_USER_CLASS(typeTableEntry, attached);
			}
		}

		public void action_USER_CLASS(TypeTableEntry typeTableEntry, @Nullable OS_Type aAttached) {
			ClassStatement c = aAttached.getClassOf();
			assert c != null;
			phase.onClass(c, new OnClass() {
				// TODO what about ClassInvocation's?
				@Override
				public void classFound(GeneratedClass cc) {
					typeTableEntry.resolve(cc);
				}
			});
		}

		public void action_USER(TypeTableEntry typeTableEntry, @Nullable OS_Type aAttached) {
			TypeName tn = aAttached.getTypeName();
			if (tn == null) return; // hack specifically for Result
			switch (tn.kindOfType()) {
				case FUNCTION:
				case GENERIC:
				case TYPE_OF:
					return;
			}
			try {
				typeTableEntry.setAttached(dt2.resolve_type(aAttached, aAttached.getTypeName().getContext()));
				if (typeTableEntry.getAttached().getType() == OS_Type.Type.USER_CLASS) {
					action_USER_CLASS(typeTableEntry, typeTableEntry.getAttached());
				} else if (typeTableEntry.getAttached().getType() == OS_Type.Type.GENERIC_TYPENAME) {
					System.err.println(String.format("801 Generic Typearg %s for %s", tn, "genericFunction.getFD().getParent()"));
				} else {
					// TODO print diagnostic because resolve_type failed
					System.err.println("245 Can't resolve typeTableEntry "+typeTableEntry);
				}
			} catch (ResolveError aResolveError) {
				System.err.println("288 Failed to resolve type "+ aAttached);
				errSink.reportDiagnostic(aResolveError);
			}
		}
	}

	class Dependencies {
		final WorkList wl = new WorkList();

		public void action_type(GenType genType, WorkManager aWorkManager) {
			// TODO work this out further
			if (genType.resolvedn != null) {
				OS_Module mod = genType.resolvedn.getContext().module();
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				NamespaceInvocation ni = phase.registerNamespaceInvocation(genType.resolvedn);
				WlGenerateNamespace gen = new WlGenerateNamespace(gf, ni, phase.generatedClasses);
				wl.addJob(gen);
			} else if (genType.resolved != null) {
				final ClassStatement c = genType.resolved.getClassOf();
				final OS_Module mod = c.getContext().module();
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				ClassInvocation ci;
				if (genType.ci == null) {
					ci = new ClassInvocation(c, null);
					ci = phase.registerClassInvocation(ci);
				} else {
					assert genType.ci instanceof ClassInvocation;
					ci = (ClassInvocation) genType.ci;
				}
				WlGenerateClass gen = new WlGenerateClass(gf, ci, phase.generatedClasses);
				wl.addJob(gen);
			}
			//
			aWorkManager.addJobs(wl);
		}

		public void action_function(FunctionInvocation aDependentFunction, WorkManager aWorkManager) {
			final BaseFunctionDef function = aDependentFunction.getFunction();
			WorkJob gen;
			final OS_Module mod;
			if (function == ConstructorDef.defaultVirtualCtor) {
				ClassInvocation ci = aDependentFunction.getClassInvocation();
				if (ci == null) {
					NamespaceInvocation ni = aDependentFunction.getNamespaceInvocation();
					if (ni == null)
						assert false;
					mod = ni.getNamespace().getContext().module();
				} else {
					mod = ci.getKlass().getContext().module();
				}
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				gen = new WlGenerateDefaultCtor(gf, aDependentFunction);
			} else {
				mod = function.getContext().module();
				final GenerateFunctions gf = phase.generatePhase.getGenerateFunctions(mod);
				gen = new WlGenerateFunction(gf, aDependentFunction);
			}
			wl.addJob(gen);
			aWorkManager.addJobs(wl);
		}
	}

	private void resolve_cte_expression(ConstantTableEntry cte, Context aContext) {
		final IExpression iv = cte.initialValue;
		switch (iv.getKind()) {
		case NUMERIC:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.SystemInteger), aContext));
					} catch (ResolveError resolveError) {
						System.out.println("71 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case STRING_LITERAL:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.String_), aContext));
					} catch (ResolveError resolveError) {
						System.out.println("117 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case CHAR_LITERAL:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
					try {
						cte.getTypeTableEntry().setAttached(resolve_type(new OS_Type(BuiltInTypes.SystemCharacter), aContext));
					} catch (ResolveError resolveError) {
						System.out.println("117 Can't be here");
//										resolveError.printStackTrace(); // TODO print diagnostic
					}
				}
				break;
			}
		case IDENT:
			{
				final OS_Type a = cte.getTypeTableEntry().getAttached();
				if (a != null) {
					assert a.getType() != null;
					if (a.getType() == OS_Type.Type.BUILT_IN && a.getBType() == BuiltInTypes.Boolean) {
						assert BuiltInTypes.isBooleanText(cte.getName());
					} else
						throw new NotImplementedException();
				} else {
					assert false;
				}
				break;
			}
		default:
			{
				System.err.println("8192 "+iv.getKind());
				throw new NotImplementedException();
			}
		}
	}

	class Lookup_function_on_exit {
		WorkList wl = new WorkList();

		public void action(ProcTableEntry pte) {
			FunctionInvocation fi = pte.getFunctionInvocation();
			if (fi == null) return;

			if (fi.getFunction() == null) {
				if (fi.pte == null) {
					return;
				} else {
//					System.err.println("592 " + fi.getClassInvocation());
					if (fi.pte.getClassInvocation() != null)
						fi.setClassInvocation(fi.pte.getClassInvocation());
//					else
//						fi.pte.setClassInvocation(fi.getClassInvocation());
				}
			}

			ClassInvocation ci = fi.getClassInvocation();
			BaseFunctionDef fd3 = fi.getFunction();
			if (ci == null) {
				ci = fi.pte.getClassInvocation();
			}
			if (fd3 == ConstructorDef.defaultVirtualCtor) {
				if (ci == null) {
					if (/*fi.getClassInvocation() == null &&*/ fi.getNamespaceInvocation() == null) {
						// Assume default constructor
						ci = new ClassInvocation((ClassStatement) pte.getResolvedElement(), null);
						ci = phase.registerClassInvocation(ci);
						fi.setClassInvocation(ci);
					} else
						throw new NotImplementedException();
				}
				final ClassStatement klass = ci.getKlass();

				Collection<ConstructorDef> cis = klass.getConstructors();
				for (ConstructorDef constructorDef : cis) {
					final Iterable<FormalArgListItem> constructorDefArgs = constructorDef.getArgs();

					if (!constructorDefArgs.iterator().hasNext()) { // zero-sized arg list
						fd3 = constructorDef;
						break;
					}
				}
			}

			final OS_Element parent;
			if (fd3 != null) {
				parent = fd3.getParent();
				if (parent instanceof ClassStatement) {
					ci = new ClassInvocation((ClassStatement) parent, null);
					{
						final ClassInvocation classInvocation = pte.getClassInvocation();
						if (classInvocation != null) {
							Map<TypeName, OS_Type> gp = classInvocation.genericPart;
							if (gp != null) {
								int i = 0;
								for (Map.Entry<TypeName, OS_Type> entry : gp.entrySet()) {
									ci.set(i, entry.getKey(), entry.getValue());
									i++;
								}
							}
						}
					}
					proceed(fi, ci, (ClassStatement) parent, wl);
				} else if (parent instanceof NamespaceStatement) {
					proceed(fi, (NamespaceStatement) parent, wl);
				}
			} else {
				parent = ci.getKlass();
				{
					final ClassInvocation classInvocation = pte.getClassInvocation();
					if (classInvocation != null && classInvocation.genericPart != null) {
						Map<TypeName, OS_Type> gp = classInvocation.genericPart;
						int i = 0;
						for (Map.Entry<TypeName, OS_Type> entry : gp.entrySet()) {
							ci.set(i, entry.getKey(), entry.getValue());
							i++;
						}
					}
				}
				proceed(fi, ci, (ClassStatement) parent, wl);
			}

//			proceed(fi, ci, parent);
		}

		void proceed(FunctionInvocation fi, ClassInvocation ci, ClassStatement aParent, WorkList wl) {
			ci = phase.registerClassInvocation(ci);

			ClassStatement kl = ci.getKlass();
			assert kl != null;

			final BaseFunctionDef fd2 = fi.getFunction();
			int state = 0;

			if (fd2 == ConstructorDef.defaultVirtualCtor) {
				state = 1;
			} else if (fd2 instanceof ConstructorDef) {
				if (fi.getClassInvocation().getConstructorName() != null)
					state = 3;
				else
					state = 2;
			} else {
				if (fi.getFunction() == null && fi.getClassInvocation() != null)
					state = 3;
				else
					state = 4;
			}

			switch (state) {
			case 1:
				assert fi.pte.getArgs().size() == 0;
				// default ctor
				wl.addJob(new WlGenerateDefaultCtor(phase.generatePhase.getGenerateFunctions(module), fi));
				break;
			case 2:
				wl.addJob(new WlGenerateCtor(phase.generatePhase.getGenerateFunctions(module), fi, null)); // TODO check this
				break;
			case 3:
				// README this is a special case to generate constructor
				// TODO should it be GenerateDefaultCtor? (check args size and ctor-name)
				final String constructorName = fi.getClassInvocation().getConstructorName();
				final IdentExpression constructorName1 = constructorName != null ? IdentExpression.forString(constructorName) : null;
				wl.addJob(new WlGenerateCtor(phase.generatePhase.getGenerateFunctions(module), fi, constructorName1));
				break;
			case 4:
				wl.addJob(new WlGenerateFunction(phase.generatePhase.getGenerateFunctions(module), fi));
				break;
			default:
				throw new NotImplementedException();
			}

			wm.addJobs(wl);
		}

		void proceed(FunctionInvocation fi, NamespaceStatement aParent, WorkList wl) {
//			ci = phase.registerClassInvocation(ci);

			final OS_Module module1 = aParent.getContext().module();

			final NamespaceInvocation nsi = phase.registerNamespaceInvocation(aParent);

			wl.addJob(new WlGenerateNamespace(phase.generatePhase.getGenerateFunctions(module1), nsi, phase.generatedClasses));
			wl.addJob(new WlGenerateFunction(phase.generatePhase.getGenerateFunctions(module1), fi));

			wm.addJobs(wl);
		}
	}

	public void lookup_function_on_exit(ProcTableEntry pte) {
		Lookup_function_on_exit lfoe = new Lookup_function_on_exit();
		lfoe.action(pte);
	}

	public void assign_type_to_idte(IdentTableEntry ite,
									BaseGeneratedFunction generatedFunction,
									Context aFunctionContext,
									Context aContext) {
		if (!ite.hasResolvedElement()) {
			IdentIA ident_a = new IdentIA(ite.getIndex(), generatedFunction);
			resolveIdentIA_(aContext, ident_a, generatedFunction, new FoundElement(phase) {

				final String path = generatedFunction.getIdentIAPathNormal(ident_a);

				@Override
				public void foundElement(OS_Element x) {
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
					if (ite.type != null && ite.type.getAttached() != null) {
						switch (ite.type.getAttached().getType()) {
						case USER:
							try {
								OS_Type xx = resolve_type(ite.type.getAttached(), aFunctionContext);
								ite.type.setAttached(xx);
							} catch (ResolveError resolveError) {
								System.out.println("192 Can't attach type to " + path);
								errSink.reportDiagnostic(resolveError);
							}
							if (ite.type.getAttached().getType() == OS_Type.Type.USER_CLASS) {
								use_user_class(ite.type.getAttached(), ite);
							}
							break;
						case USER_CLASS:
							use_user_class(ite.type.getAttached(), ite);
							break;
						default:
							throw new NotImplementedException();
						}
					} else {
						int yy=2;
						if (!ite.hasResolvedElement()) {
							LookupResultList lrl = null;
							try {
								lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), aFunctionContext);
								OS_Element best = lrl.chooseBest(null);
								if (best != null) {
									ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
									if (ite.type != null && ite.type.getAttached() != null) {
										if (ite.type.getAttached().getType() == OS_Type.Type.USER) {
											try {
												OS_Type xx = resolve_type(ite.type.getAttached(), aFunctionContext);
												ite.type.setAttached(xx);
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
							if (ite.type.getAttached().getType() == OS_Type.Type.USER_CLASS) {
								use_user_class(ite.type.getAttached(), ite);
							}
						}
					}
				}

				private void use_user_class(OS_Type aType, IdentTableEntry aEntry) {
					int yy=2;
					final ClassStatement cs = aType.getClassOf();
					if (aEntry.constructable_pte != null) {
						int yyy=3;
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

	public void resolve_ident_table_entry(IdentTableEntry ite, BaseGeneratedFunction generatedFunction, Context ctx) {
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
				} else if (itee.backlink instanceof ProcIA) {
					x = generatedFunction.getProcTableEntry(to_int(itee.backlink));
					itee.setCallablePTE((ProcTableEntry) x);
					itex = null; //((ProcTableEntry) x).backlink;
				} else if (itee.backlink == null) {
					itex = null;
					x = null;
				}

				if (x != null) {
//					System.err.println("162 Adding FoundParent for "+itee);
					x.addStatusListener(new FoundParent(x, itee, itee.getIdent().getContext(), generatedFunction)); // TODO context??
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
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
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

	public void resolve_var_table_entry(VariableTableEntry vte, BaseGeneratedFunction generatedFunction, Context ctx) {
		if (vte.el == null)
			return;
		{
			if (vte.type.getAttached() == null && vte.constructable_pte != null) {
				ClassStatement c = vte.constructable_pte.getFunctionInvocation().getClassInvocation().getKlass();
				final OS_Type attached = new OS_Type(c);
				// TODO this should have been set somewhere already
				//  typeName and nonGenericTypeName are not set
				//  but at this point probably wont be needed
				vte.type.genType.resolved = attached;
				vte.type.setAttached(attached);
			}
			vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(vte.el));
		}
	}

	class Implement_construct {

		private final BaseGeneratedFunction generatedFunction;
		private final Instruction instruction;
		private final InstructionArgument expression;

		private final ProcTableEntry pte;

		public Implement_construct(BaseGeneratedFunction aGeneratedFunction, Instruction aInstruction) {
			generatedFunction = aGeneratedFunction;
			instruction = aInstruction;

			// README all these asserts are redundant, I know
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
			IdentTableEntry idte = ((IdentIA)expression).getEntry();
			DeducePath deducePath = idte.buildDeducePath(generatedFunction);
			{
				OS_Element el3;
				Context ectx = generatedFunction.getFD().getContext();
				for (int i = 0; i < deducePath.size(); i++) {
					InstructionArgument ia2 = deducePath.getIA(i);

					el3 = deducePath.getElement(i);

					if (ia2 instanceof IntegerIA) {
						@NotNull VariableTableEntry vte = ((IntegerIA) ia2).getEntry();
						// TODO will fail if we try to construct a tmp var, but we never try to do that
						assert vte.vtt != VariableTableType.TEMP;
						assert el3     != null;
						assert i       == 0;
						ectx  = deducePath.getContext(i);
					} else if (ia2 instanceof IdentIA) {
						@NotNull IdentTableEntry idte2 = ((IdentIA) ia2).getEntry();
						final String s = idte2.getIdent().toString();
						LookupResultList lrl = ectx.lookup(s);
						OS_Element el2 = lrl.chooseBest(null);
						if (el2 == null) {
							assert el3 instanceof VariableStatement;
							VariableStatement vs = (VariableStatement) el3;
							@NotNull TypeName tn = vs.typeName();
							OS_Type ty = new OS_Type(tn);

							if (idte2.type == null) {
								// README Don't remember enough about the constructors to select a different one
								@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, ty);
								try {
									@NotNull OS_Type resolved = resolve_type(ty, tn.getContext());
									System.err.println("892 resolved: "+resolved);
									tte.setAttached(resolved);
								} catch (ResolveError aResolveError) {
									errSink.reportDiagnostic(aResolveError);
								}

								idte2.type = tte;
							}
							// s is constructor name
							implement_construct_type(idte2, ty, s);
							return;
						} else {
							if (i+1 == deducePath.size()) {
								assert el3 == el2;
								if (el2 instanceof ConstructorDef) {
									GenType type = deducePath.getType(i);
									if (type.nonGenericTypeName == null) {
										type.nonGenericTypeName = deducePath.getType(i-1).nonGenericTypeName; // HACK. not guararnteed to work!
									}
									OS_Type ty = new OS_Type(type.nonGenericTypeName);
									implement_construct_type(idte2, ty, s);
								}
							} else {
								ectx = deducePath.getContext(i);
							}
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
			assert vte.type.getAttached() != null; // TODO will fail when empty variable expression
			@Nullable OS_Type ty = vte.type.getAttached();
			implement_construct_type(vte, ty, null);
		}

		private void implement_construct_type(Constructable co, @Nullable OS_Type aTy, String constructorName) {
			assert aTy != null;
			if (aTy.getType() == OS_Type.Type.USER) {
				TypeName tyn = aTy.getTypeName();
				if (tyn instanceof NormalTypeName) {
					final NormalTypeName tyn1 = (NormalTypeName) tyn;
					_implement_construct_type(co, constructorName, (NormalTypeName) tyn);
				}
			} else
				throw new NotImplementedException();
			if (co != null) {
				co.setConstructable(pte);
				ClassInvocation best = pte.getClassInvocation();
				assert best != null;
				best.promise().done(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(GeneratedClass result) {
						co.resolveType(result);
					}
				});
			}
		}

		private void _implement_construct_type(Constructable co, String constructorName, NormalTypeName aTyn1) {
			String s = aTyn1.getName();
			LookupResultList lrl = aTyn1.getContext().lookup(s);
			OS_Element best = lrl.chooseBest(null);
			assert best instanceof ClassStatement;
			List<TypeName> gp = ((ClassStatement) best).getGenericPart();
			ClassInvocation clsinv = new ClassInvocation((ClassStatement) best, constructorName);
			if (gp.size() > 0) {
				TypeNameList gp2 = aTyn1.getGenericPart();
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
			clsinv = phase.registerClassInvocation(clsinv);
			if (co != null) {
				if (co instanceof IdentTableEntry) {
					final IdentTableEntry idte3 = (IdentTableEntry) co;
					idte3.type.genTypeCI(clsinv);
					clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(GeneratedClass result) {
							idte3.resolveType(result);
						}
					});
				} else if (co instanceof VariableTableEntry) {
					final VariableTableEntry vte = (VariableTableEntry) co;
					vte.type.genTypeCI(clsinv);
					clsinv.resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(GeneratedClass result) {
							vte.resolveType(result);
						}
					});
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
					if (cc == null) assert pte.getArgs().size() == 0;
					FunctionInvocation fi = newFunctionInvocation(cc, pte, clsinv, phase);
					pte.setFunctionInvocation(fi);
				}
			}
		}
	}

	void implement_construct(BaseGeneratedFunction generatedFunction, Instruction instruction) {
		final Implement_construct ic = newImplement_construct(generatedFunction, instruction);
		ic.action();
	}

	@NotNull
	public DeduceTypes2.Implement_construct newImplement_construct(BaseGeneratedFunction generatedFunction, Instruction instruction) {
		return new Implement_construct(generatedFunction, instruction);
	}

	void resolve_function_return_type(BaseGeneratedFunction generatedFunction) {
		// MODERNIZATION Does this have any affinity with DeferredMember?
		@Nullable final InstructionArgument vte_index = generatedFunction.vte_lookup("Result");
		if (vte_index != null) {
			final VariableTableEntry vte = generatedFunction.getVarTableEntry(to_int(vte_index));

			if (vte.type.getAttached() != null) {
				phase.typeDecided((GeneratedFunction) generatedFunction, vte.type.getAttached());
			} else {
				@NotNull Collection<TypeTableEntry> pot1 = vte.potentialTypes();
				ArrayList<TypeTableEntry> pot = new ArrayList<TypeTableEntry>(pot1);
				if (pot.size() == 1) {
					phase.typeDecided((GeneratedFunction) generatedFunction, pot.get(0).getAttached());
				} else if (pot.size() == 0) {
					phase.typeDecided((GeneratedFunction) generatedFunction, new OS_Type(BuiltInTypes.Unit));
				} else {
					// TODO report some kind of error/diagnostic and/or let ForFunction know...
					errSink.reportWarning("Can't resolve type of `Result'. potentialTypes > 1 for "+vte);
				}
			}
		} else {
			if (generatedFunction instanceof GeneratedConstructor) {
				// cant set return type of constructors
			} else {
				// if Result is not present, then make function return Unit
				// TODO May not be correct in all cases, such as when Value is present
				// but works for current code structure, where Result is a always present
				phase.typeDecided((GeneratedFunction) generatedFunction, new OS_Type(BuiltInTypes.Unit));
			}
		}
	}

	void found_element_for_ite(BaseGeneratedFunction generatedFunction, IdentTableEntry ite, @Nullable OS_Element y, Context ctx) {
		assert y == ite.resolved_element;

		if (y instanceof VariableStatement) {
			final VariableStatement vs = (VariableStatement) y;
			TypeName typeName = vs.typeName();
			if (ite.type == null || ite.type.getAttached() == null) {
				if (!(typeName.isNull())) {
					if (ite.type == null)
						ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
					ite.type.setAttached(new OS_Type(typeName));
				} else {
					final OS_Element parent = vs.getParent().getParent();
					if (parent instanceof NamespaceStatement || parent instanceof ClassStatement) {
						boolean state;
						if (generatedFunction instanceof GeneratedFunction) {
							final GeneratedFunction generatedFunction1 = (GeneratedFunction) generatedFunction;
							state = (parent != generatedFunction1.getFD().getParent());
						} else {
							state = (parent != ((GeneratedConstructor) generatedFunction).getFD().getParent());
						}
						if (state) {
							deferred_member(parent, getInvocationFromBacklink(ite.backlink), vs).typePromise().
									done(new DoneCallback<GenType>() {
										@Override
										public void onDone(GenType result) {
											if (ite.type == null)
												ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
											assert result.resolved != null;
											ite.type.setAttached(result.resolved);
										}
									});
						}

						GenType genType = null;
						if (parent instanceof NamespaceStatement)
							genType = new GenType((NamespaceStatement) parent);
						else if (parent instanceof ClassStatement)
							genType = new GenType((ClassStatement) parent);

						generatedFunction.addDependentType(genType);
					}
					System.err.println("394 typename is null "+ vs.getName());
				}
			}
		} else if (y instanceof ClassStatement) {
			ClassStatement classStatement = ((ClassStatement) y);
			OS_Type attached = new OS_Type(classStatement);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.setAttached(attached);
		} else if (y instanceof FunctionDef) {
			FunctionDef functionDef = ((FunctionDef) y);
			OS_Type attached = new OS_FuncType(functionDef);
			if (ite.type == null) {
				ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
			} else
				ite.type.setAttached(attached);
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
				ite.type.setAttached(attached);
			int yy = 2;
		} else if (y instanceof AliasStatement) {
			System.err.println("396 AliasStatement");
			OS_Element x = DeduceLookupUtils._resolveAlias((AliasStatement) y);
			if (x == null) {
				ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
				errSink.reportError("399 resolveAlias returned null");
			} else {
				ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
				found_element_for_ite(generatedFunction, ite, x, ctx);
			}
		} else {
			//LookupResultList exp = lookupExpression();
			System.out.println("2009 "+y);
		}
	}

	private IInvocation getInvocationFromBacklink(InstructionArgument aBacklink) {
		if (aBacklink == null) return null;
		// TODO implement me
		return null;
	}

	private DeferredMember deferred_member(OS_Element aParent, IInvocation aInvocation, VariableStatement aVariableStatement) {
		DeferredMember dm = new DeferredMember(aParent, aInvocation, aVariableStatement);
		phase.addDeferredMember(dm);
		return dm;
	}

	@NotNull OS_Type resolve_type(final OS_Type type, final Context ctx) throws ResolveError {
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
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
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
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
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
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
							} else if (OS_Type.isConcreteType(best)) {
								throw new NotImplementedException();
							} else
								throw new NotImplementedException();
						}
						if (best == null) {
							throw new ResolveError(IdentExpression.forString(typeName), lrl);
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
							best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
						}
						if (best == null) {
							if (tn.asSimpleString().equals("Any"))
								return new OS_AnyType();
							throw new ResolveError(tn1, lrl);
						}

						if (best instanceof ClassContext.OS_TypeNameElement) {
							return new OS_GenericTypeNameType((ClassContext.OS_TypeNameElement) best);
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

	private void do_assign_constant(final BaseGeneratedFunction generatedFunction, final Instruction instruction, final VariableTableEntry vte, final ConstTableIA i2) {
		if (vte.type.getAttached() != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.getAttached() == null) {
			System.out.println("Null type in CTE "+cte);
		}
//		vte.type = cte.type;
		vte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final BaseGeneratedFunction generatedFunction,
								final Context ctx,
								final VariableTableEntry vte,
								final FnCallArgs fca,
								final Instruction instruction) {
		final int instructionIndex = instruction.getIndex();
		final ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		IdentIA identIA = (IdentIA) pte.expression_num;
		if (identIA != null){
//			System.out.println("594 "+identIA.getEntry().getStatus());

			resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(phase) {

				final String xx = generatedFunction.getIdentIAPathNormal(identIA);

				@Override
				public void foundElement(OS_Element e) {
//					System.out.println(String.format("600 %s %s", xx ,e));
//					System.out.println("601 "+identIA.getEntry().getStatus());
					assert e == identIA.getEntry().resolved_element;
//					set_resolved_element_pte(identIA, e, pte);
					pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, identIA));
					pte.getFunctionInvocation().generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
						@Override
						public void onDone(BaseGeneratedFunction bgf) {
							bgf.typePromise().then(new DoneCallback<OS_Type>() {
								@Override
								public void onDone(OS_Type result) {
									vte.typeDeferred().resolve(result);
									if (vte.type.getAttached() == null)
										vte.type.setAttached(result);
								}
							});
						}
					});
				}

				@Override
				public void noFoundElement() {
					// TODO create Diagnostic and quit
					System.out.println("1005 Can't find element for " + xx);
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
				tte.setAttached(new OS_Type(BuiltInTypes.SystemInteger));
				//vte.type = tte;
				break;
			case CHAR_LITERAL:
				tte.setAttached(new OS_Type(BuiltInTypes.SystemCharacter));
				break;
			case IDENT:
				do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, (IdentExpression) e);
				break;
			case PROCEDURE_CALL:
				{
					final ProcedureCallExpression pce = (ProcedureCallExpression) e;
					try {
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(pce.getLeft(), ctx);
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							while (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
							}
							if (best instanceof FunctionDef) {
								final OS_Element parent = best.getParent();
								IInvocation invocation;
								if (parent instanceof NamespaceStatement) {
									invocation = phase.registerNamespaceInvocation((NamespaceStatement) parent);
								} else if (parent instanceof ClassStatement) {
									ClassInvocation ci = new ClassInvocation((ClassStatement) parent, null);
									invocation = phase.registerClassInvocation(ci);
								} else 
									throw new NotImplementedException(); // TODO implement me
								
								forFunction(newFunctionInvocation((FunctionDef) best, pte, invocation, phase), new ForFunction() {
									@Override
									public void typeDecided(OS_Type aType) {
										tte.setAttached(aType);
										//vte.addPotentialType(instructionIndex, tte);
									}
								});
//								tte.setAttached(new OS_FuncType((FunctionDef) best));
								
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
			case DOT_EXP:
				{
					final DotExpression de = (DotExpression) e;
					try {
						final LookupResultList lrl = DeduceLookupUtils.lookupExpression(de.getLeft(), ctx);
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							while (best instanceof AliasStatement) {
								best = DeduceLookupUtils._resolveAlias2((AliasStatement) best);
							}
							if (best instanceof FunctionDef) {
								tte.setAttached(new OS_FuncType((FunctionDef) best));
								//vte.addPotentialType(instructionIndex, tte);
							} else if (best instanceof ClassStatement) {
								tte.setAttached(new OS_Type((ClassStatement) best));
							} else if (best instanceof VariableStatement) {
								final VariableStatement vs = (VariableStatement) best;
								@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(vs.getName());
								TypeTableEntry tte1 = ((IntegerIA) vte_ia).getEntry().type;
								tte.setAttached(tte1.getAttached());
							} else {
								final int y=2;
								System.err.println(best.getClass().getName());
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
						pte.setResolvedElement(best); // TODO do we need to add a dependency for class?
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
						if (pte.getResolvedElement() == null)
							pte.setResolvedElement(el);
						if (el instanceof FunctionDef) {
							FunctionDef fd = (FunctionDef) el;
							final IInvocation invocation;
							if (fd.getParent() == generatedFunction.getFD().getParent()) {
								invocation = getInvocation((GeneratedFunction) generatedFunction);
							} else {
								if (fd.getParent() instanceof NamespaceStatement) {
									NamespaceInvocation ni = phase.registerNamespaceInvocation((NamespaceStatement) fd.getParent());
									invocation = ni;
								} else if (fd.getParent() instanceof ClassStatement) {
									final ClassStatement classStatement = (ClassStatement) fd.getParent();
									ClassInvocation ci = new ClassInvocation(classStatement, null);
									final List<TypeName> genericPart = classStatement.getGenericPart();
									if (genericPart.size() > 0) {
										// TODO handle generic parameters somehow (getInvocationFromBacklink?)

									}
									ci = phase.registerClassInvocation(ci);
									invocation = ci;
								} else
									throw new NotImplementedException();
							}
							forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
								@Override
								public void typeDecided(OS_Type aType) {
									if (!vte.typeDeferred().isPending()) {
										if (vte.resolvedType() == null) {
											final ClassInvocation ci = genCI(vte.type);
											vte.type.genTypeCI(ci);
											ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
												@Override
												public void onDone(GeneratedClass result) {
													vte.resolveType(result);
												}
											});
										}
										System.err.println("2041 type already found "+vte);
										return; // type already found
									}
									// I'm not sure if below is ever called
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

							register_and_resolve(vte, kl);
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

	public IInvocation getInvocation(@NotNull GeneratedFunction generatedFunction) {
		final ClassInvocation classInvocation = generatedFunction.fi.getClassInvocation();
		final NamespaceInvocation ni;
		if (classInvocation == null) {
			ni = generatedFunction.fi.getNamespaceInvocation();
			return ni;
		} else
			return classInvocation;
	}

	private void do_assign_call_args_ident(@NotNull BaseGeneratedFunction generatedFunction,
										   Context ctx,
										   VariableTableEntry vte,
										   int aInstructionIndex,
										   ProcTableEntry aPte,
										   int aI,
										   TypeTableEntry aTte,
										   @NotNull IdentExpression aExpression) {
		final String e_text = aExpression.getText();
		final InstructionArgument vte_ia = generatedFunction.vte_lookup(e_text);
//		System.out.println("10000 "+vte_ia);
		if (vte_ia != null) {
			final VariableTableEntry vte1 = generatedFunction.getVarTableEntry(to_int(vte_ia));
			final Promise<OS_Type, Void, Void> p = vte1.typePromise();
			p.done(new DoneCallback<OS_Type>() {
				@Override
				public void onDone(OS_Type result) {
					assert vte != vte1;
					aTte.setAttached(result/*.getAttached()*/);
//					vte.addPotentialType(aInstructionIndex, result); // TODO!!
				}
			});
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					final List<TypeTableEntry> ll = getPotentialTypesVte((GeneratedFunction) generatedFunction, vte_ia);
					doLogic(ll);
				}

				public void doLogic(@NotNull List<TypeTableEntry> potentialTypes) {
					assert potentialTypes.size() >= 0;
					switch (potentialTypes.size()) {
						case 1:
//							tte.attached = ll.get(0).attached;
//							vte.addPotentialType(instructionIndex, ll.get(0));
							if (p.isResolved())
								System.out.printf("1047 (vte already resolved) vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, potentialTypes.get(0));
							else
								vte1.typeDeferred().resolve(potentialTypes.get(0).getAttached());
							break;
						case 0:
							LookupResultList lrl = ctx.lookup(e_text);
							OS_Element best = lrl.chooseBest(null);
							if (best instanceof FormalArgListItem) {
								@NotNull final FormalArgListItem fali = (FormalArgListItem) best;
								final OS_Type osType = new OS_Type(fali.typeName());
								if (!osType.equals(vte.type.getAttached())) {
									@NotNull TypeTableEntry tte1 = generatedFunction.newTypeTableEntry(
											TypeTableEntry.Type.SPECIFIED, osType, fali.getNameToken(), vte1);
									if (p.isResolved())
										System.out.printf("890 Already resolved type: vte1.type = %s, gf = %s, tte1 = %s %n", vte1.type, generatedFunction, tte1);
									else
										vte1.typeDeferred().resolve(tte1.getAttached());
								}
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
									vte1.typeDeferred().resolve(vte2.type.getAttached());
//								vte.type = vte2.type;
//								tte.attached = vte.type.attached;
								vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
								vte2.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best)); // TODO ??
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
											return input.getAttached() != null;
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
			int ia = generatedFunction.addIdentTableEntry(aExpression, ctx);
			IdentTableEntry idte = generatedFunction.getIdentTableEntry(ia);
			idte.addPotentialType(aInstructionIndex, aTte); // TODO DotExpression??
			final int ii = aI;
			idte.onType(phase, new OnType() {
				@Override
				public void typeDeduced(OS_Type aType) {
					aPte.setArgType(ii, aType); // TODO does this belong here or in FunctionInvocation?
					aTte.setAttached(aType); // since we know that tte.attached is always null here
				}

				@Override
				public void noTypeFound() {
					System.err.println("719 no type found "+generatedFunction.getIdentIAPathNormal(new IdentIA(ia, generatedFunction)));
				}
			});
		}
	}

	private void do_assign_call_GET_ITEM(GetItemExpression gie, TypeTableEntry tte, BaseGeneratedFunction generatedFunction, Context ctx) {
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
						@Nullable OS_Type ty = idte.type.getAttached();
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
										final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
										forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
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
							ty = idte.type.getAttached();
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
						vte2.typePromise().done(new DoneCallback<OS_Type>() {
							@Override
							public void onDone(OS_Type result) {
	//							assert false; // TODO this code is never reached
								final @Nullable OS_Type ty2 = result/*.getAttached()*/;
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
										final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
										forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
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

	private void do_assign_constant(final BaseGeneratedFunction generatedFunction, final Instruction instruction, final IdentTableEntry idte, final ConstTableIA i2) {
		if (idte.type != null && idte.type.getAttached() != null) {
			// TODO check types
		}
		final ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.getAttached() == null) {
			System.out.println("*** ERROR: Null type in CTE "+cte);
		}
		// idte.type may be null, but we still addPotentialType here
		idte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(final BaseGeneratedFunction generatedFunction,
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
				tte.setAttached(new OS_Type(BuiltInTypes.SystemInteger));
				idte.type = tte; // TODO why not addPotentialType ? see below for example
			}
			break;
			case IDENT:
			{
				final InstructionArgument vte_ia = generatedFunction.vte_lookup(((IdentExpression) e).getText());
				final List<TypeTableEntry> ll = getPotentialTypesVte((GeneratedFunction) generatedFunction, vte_ia);
				if (ll.size() == 1) {
					tte.setAttached(ll.get(0).getAttached());
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
				pte.setResolvedElement(best); // TODO do we need to add a dependency for class?
			else
				throw new NotImplementedException();
		}
	}

	private void implement_calls(final BaseGeneratedFunction gf, final Context context, final InstructionArgument i2, final ProcTableEntry fn1, final int pc) {
		if (gf.deferred_calls.contains(pc)) {
			System.err.println("Call is deferred "/*+gf.getInstruction(pc)*/+" "+fn1);
			return;
		}
		implement_calls_(gf, context, i2, fn1, pc);
	}

	private void implement_calls_(final BaseGeneratedFunction gf,
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
					final OS_Type x = tt.get(0).getAttached();
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
			pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(best, null));
//			set_resolved_element_pte(null, best, pte); // TODO check arity and arg matching
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

	public void resolveIdentIA_(Context context, IdentIA identIA, BaseGeneratedFunction generatedFunction, FoundElement foundElement) {
		Resolve_Ident_IA ria = new Resolve_Ident_IA(this, phase, context, identIA, generatedFunction, foundElement, errSink);
		ria.action();
	}

	public static class Holder<T> {
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
								 @NotNull final BaseGeneratedFunction generatedFunction,
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
						final OS_Type attached = pot.get(0).getAttached();
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
									final IInvocation invocation = getInvocation((GeneratedFunction) generatedFunction);
									forFunction(newFunctionInvocation(fd, pte, invocation, phase), new ForFunction() {
										@Override
										public void typeDecided(OS_Type aType) {
											assert fd == generatedFunction.getFD();
											//
											pot.get(0).setAttached(aType);
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

				OS_Type attached = vte.type.getAttached();
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
						final OS_Type attached1 = pot.get(0).getAttached();
						vte.type.setAttached(attached1);
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
						assert idte2.type.getAttached() != null;
						try {
							if (!(idte2.type.getAttached() instanceof OS_UnknownType)) { // TODO
								OS_Type rtype = resolve_type(idte2.type.getAttached(), ectx);
								if (rtype.getType() == OS_Type.Type.USER_CLASS)
									ectx = rtype.getClassOf().getContext();
								else if (rtype.getType() == OS_Type.Type.FUNCTION)
									ectx = ((OS_FuncType) rtype).getElement().getContext();
								idte2.type.setAttached(rtype); // TODO may be losing alias information here
							}
						} catch (ResolveError resolveError) {
							if (resolveError.resultsList().size() > 1)
								errSink.reportDiagnostic(resolveError);
							else
								System.out.println("1089 Can't attach type to "+ idte2.type.getAttached());
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
		private BaseTableEntry bte;
		private IdentTableEntry ite;
		private Context ctx;
		private BaseGeneratedFunction generatedFunction;

		public FoundParent(BaseTableEntry aBte, IdentTableEntry aIte, Context aCtx, BaseGeneratedFunction aGeneratedFunction) {
			bte = aBte;
			ite = aIte;
			ctx = aCtx;
			generatedFunction = aGeneratedFunction;
		}

		void found_element_for_ite2(GeneratedFunction generatedFunction, IdentTableEntry ite, Context ctx) {
			OS_Element y = ite.resolved_element;

			if (y instanceof VariableStatement) {
				final VariableStatement vs = (VariableStatement) y;
				TypeName typeName = vs.typeName();
				if (ite.type == null || ite.type.getAttached() == null) {
					if (!(typeName.isNull())) {
						if (ite.type == null)
							ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
						ite.type.setAttached(new OS_Type(typeName));
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
					ite.type.setAttached(attached);
			} else if (y instanceof FunctionDef) {
				FunctionDef functionDef = ((FunctionDef) y);
				OS_Type attached = new OS_FuncType(functionDef);
				if (ite.type == null) {
					ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
				} else
					ite.type.setAttached(attached);
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
					ite.type.setAttached(attached);
				int yy = 2;
			} else if (y instanceof AliasStatement) {
				System.err.println("396 AliasStatement");
				OS_Element x = null;
				try {
					x = DeduceLookupUtils._resolveAlias2((AliasStatement) y);
					assert x != null;
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
					found_element_for_ite(generatedFunction, ite, x, ctx);
				} catch (ResolveError aResolveError) {
					ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					errSink.reportError("399 resolveAlias returned null");
				}
			} else {
				//LookupResultList exp = lookupExpression();
				System.out.println("2009 "+y);
			}
		}

		@Override
		public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
			if (newStatus == BaseTableEntry.Status.KNOWN) {
				if (bte instanceof VariableTableEntry) {
					final VariableTableEntry vte = (VariableTableEntry) bte;
					onChangeVTE(vte);
				} else if (bte instanceof ProcTableEntry) {
					final ProcTableEntry pte = (ProcTableEntry) bte;
					onChangePTE(pte);
				}
				postOnChange(eh);
			}
		}

		private void postOnChange(IElementHolder eh) {
			if (ite.type == null && eh.getElement() instanceof VariableStatement) {
				@NotNull TypeName typ = ((VariableStatement) eh.getElement()).typeName();
				OS_Type ty = new OS_Type(typ);

				try {
					@NotNull OS_Type ty2 = null;
					if (ty.getType() == OS_Type.Type.USER) {
						if (typ.isNull()) {
//									ty = ((VariableTableEntry) bte).type.getAttached();
							final OS_Type attached = ((VariableTableEntry) bte).type.getAttached();
							//assert attached != null;
							if (attached == null) {
								System.err.println("2842 attached == null for "+((VariableTableEntry) bte).type);
								((VariableTableEntry) bte).typePromise().done(new DoneCallback<OS_Type>() {
									@Override
									public void onDone(OS_Type result) {
										final OS_Type attached1 = result/*.getAttached()*/;
										assert attached1 != null;
										try {
											OS_Type ty3 = resolve_type(attached1, attached1.getTypeName().getContext());
											// no expression or TableEntryIV below
											ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, ty3); // or ty2?
										} catch (ResolveError aResolveError) {
											aResolveError.printStackTrace();
										}
									}
								});
							} else
								ty2 = attached;//resolve_type(ty, ty.getTypeName().getContext());
						} else {
							assert ty.getTypeName() != null;
							ty2 = resolve_type(ty, ty.getTypeName().getContext());
						}
//								OS_Element ele = ty2.getElement();
					} else
						ty2 = ty;

					// no expression or TableEntryIV below
					if (ty2 != null)
						ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, ty2); // or ty2?
				} catch (ResolveError aResolveError) {
					errSink.reportDiagnostic(aResolveError);
				}
			}
		}

		private void onChangePTE(ProcTableEntry aPte) {
			if (aPte.getStatus() == BaseTableEntry.Status.KNOWN) { // TODO might be obvious
				if (aPte.getFunctionInvocation() != null) {
					FunctionInvocation fi = aPte.getFunctionInvocation();
					BaseFunctionDef fd = fi.getFunction();
					if (fd instanceof ConstructorDef) {
						fi.generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
							@Override
							public void onDone(BaseGeneratedFunction result) {
								GeneratedConstructor constructorDef = (GeneratedConstructor) result;

								@NotNull BaseFunctionDef ele = constructorDef.getFD();

								try {
									LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
									OS_Element best = lrl.chooseBest(null);
									ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
								} catch (ResolveError aResolveError) {
									aResolveError.printStackTrace();
									errSink.reportDiagnostic(aResolveError);
								}
							}
						});
					}
				} else
					throw new NotImplementedException();
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

		private void onChangeVTE(VariableTableEntry vte) {
			@NotNull ArrayList<TypeTableEntry> pot = getPotentialTypesVte(vte);
			if (vte.getStatus() == BaseTableEntry.Status.KNOWN && vte.type.getAttached() != null && vte.el != null) {

				final OS_Type ty = vte.type.getAttached();

				OS_Element ele2 = null;

				try {
					if (ty.getType() == OS_Type.Type.USER) {
						@NotNull OS_Type ty2 = resolve_type(ty, ty.getTypeName().getContext());
						OS_Element ele;
						if (vte.type.genType.resolved == null) {
							if (ty2.getType() == OS_Type.Type.USER_CLASS) {
								vte.type.genType.resolved = ty2;
							}
						}
						ele = ty2.getElement();
						LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
						ele2 = lrl.chooseBest(null);
					} else
						ele2 = ty.getClassOf(); // TODO might fail later (use getElement?)

					LookupResultList lrl = null;

					lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele2.getContext());
					OS_Element best = lrl.chooseBest(null);
					if (best != ele2) System.err.println(String.format("2824 Divergent for %s, %s and %s", ite, best, ele2));;
					ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
				} catch (ResolveError aResolveError) {
					aResolveError.printStackTrace();
					errSink.reportDiagnostic(aResolveError);
				}
			} else if (pot.size() == 1) {
				TypeTableEntry tte = pot.get(0);
				@Nullable OS_Type ty = tte.getAttached();
				if (ty != null) {
					if (ty.getType() == OS_Type.Type.USER) {
						try {
							@NotNull OS_Type ty2 = resolve_type(ty, ty.getTypeName().getContext());
							OS_Element ele = ty2.getElement();
							LookupResultList lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), ele.getContext());
							OS_Element best = lrl.chooseBest(null);
							ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
//									ite.setResolvedElement(best);

							final ClassStatement klass = (ClassStatement) ele;

							resolve_vte_for_class(vte, klass);
						} catch (ResolveError resolveError) {
							errSink.reportDiagnostic(resolveError);
						}
					} else if (ty.getType() == OS_Type.Type.USER_CLASS) {
						ClassStatement klass = ty.getClassOf();
						LookupResultList lrl = null;
						try {
							lrl = DeduceLookupUtils.lookupExpression(ite.getIdent(), klass.getContext());
							OS_Element best = lrl.chooseBest(null);
//									ite.setStatus(BaseTableEntry.Status.KNOWN, best);
							assert best != null;
							ite.setResolvedElement(best);

							resolve_vte_for_class(vte, klass);
						} catch (ResolveError aResolveError) {
							aResolveError.printStackTrace();
						}
					}
				} else {
					System.err.println("1696");
				}
			}
		}

		public void resolve_vte_for_class(VariableTableEntry aVte, ClassStatement aKlass) {
			register_and_resolve(aVte, aKlass);
		}
	}

	public void register_and_resolve(VariableTableEntry aVte, ClassStatement aKlass) {
		ClassInvocation ci = new ClassInvocation(aKlass, null);
		ci = phase.registerClassInvocation(ci);
		ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(GeneratedClass result) {
				aVte.resolveType(result);
			}
		});
	}
}

//
//
//
