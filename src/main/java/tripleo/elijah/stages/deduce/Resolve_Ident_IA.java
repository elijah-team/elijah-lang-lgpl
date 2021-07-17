/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkList;

import java.util.Collection;
import java.util.List;

/**
 * Created 7/8/21 2:31 AM
 */
class Resolve_Ident_IA {
	private final Context context;
	private final IdentIA identIA;
	private final BaseGeneratedFunction generatedFunction;
	private final FoundElement foundElement;
	private final ErrSink errSink;

	private final DeduceTypes2 deduceTypes2;
	private final DeducePhase phase;

	@Contract(pure = true)
	public Resolve_Ident_IA(final @NotNull DeduceTypes2 aDeduceTypes2,
							final @NotNull DeducePhase aPhase,
							final @NotNull Context aContext,
							final @NotNull IdentIA aIdentIA,
							final BaseGeneratedFunction aGeneratedFunction,
							final @NotNull FoundElement aFoundElement,
							final @NotNull ErrSink aErrSink) {
		context = aContext;
		identIA = aIdentIA;
		generatedFunction = aGeneratedFunction;
		foundElement = aFoundElement;
		errSink = aErrSink;
		deduceTypes2 = aDeduceTypes2;
		phase = aPhase;
	}

	OS_Element el;
	Context ectx;

	public void action() {
		final List<InstructionArgument> s = generatedFunction._getIdentIAPathList(identIA);

		ectx = context;
		el = null;

		for (final InstructionArgument ia : s) {
			if (ia instanceof IntegerIA) {
				RIA_STATE state = action_IntegerIA(ia);
				switch (state) {
					case CONTINUE:
						continue;
					case RETURN:
						return;
					case NEXT:
						break;
					default:
						throw new IllegalStateException("Can't be here");
				}
			} else if (ia instanceof IdentIA) {
				RIA_STATE state = action_IdentIA(s, (IdentIA) ia);
				switch (state) {
					case CONTINUE:
						continue; // never happens here
					case RETURN:
						return;
					case NEXT:
						break;
					default:
						throw new IllegalStateException("Can't be here");
				}
			} else if (ia instanceof ProcIA) {
				action_ProcIA(ia);
			} else
				throw new IllegalStateException("Really cant be here");
		}
		preUpdateStatus(s);
		updateStatus(s);
	}

	private void preUpdateStatus(final @NotNull List<InstructionArgument> s) {
		final String normal_path = generatedFunction.getIdentIAPathNormal(identIA);
		if (s.size() > 1) {
			final OS_Element el2 = el;
			InstructionArgument x = s.get(s.size() - 1);
			if (x instanceof IntegerIA) {
				assert false;
				@NotNull VariableTableEntry y = generatedFunction.getVarTableEntry(DeduceTypes2.to_int(x));
				y.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
			} else if (x instanceof IdentIA) {
				@NotNull IdentTableEntry y = generatedFunction.getIdentTableEntry(DeduceTypes2.to_int(x));
				y.addStatusListener(new BaseTableEntry.StatusListener() {
					@Override
					public void onChange(IElementHolder eh, BaseTableEntry.Status newStatus) {
						if (newStatus == BaseTableEntry.Status.KNOWN) {
//								assert el2 != eh.getElement();
							System.out.println("1424 Found for " + normal_path);
							foundElement.doFoundElement(eh.getElement());
						}
					}
				});
			}
		} else {
//				System.out.println("1431 Found for " + normal_path);
			foundElement.doFoundElement(el);
		}
	}

	private void updateStatus(List<InstructionArgument> aS) {
		InstructionArgument x = aS.get(0);
		if (x instanceof IntegerIA) {
			@NotNull VariableTableEntry y = ((IntegerIA) x).getEntry();
			y.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
		} else if (x instanceof IdentIA) {
			@NotNull IdentTableEntry y = ((IdentIA) x).getEntry();
			assert y.getStatus() == BaseTableEntry.Status.KNOWN;
//				y.setStatus(BaseTableEntry.Status.KNOWN, el);
		} else if (x instanceof ProcIA) {
			@NotNull ProcTableEntry y = /*((ProcIA) x).getEntry()*/generatedFunction.getProcTableEntry(DeduceTypes2.to_int(x));
			assert y.getStatus() == BaseTableEntry.Status.KNOWN;
			//y.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
		} else
			throw new NotImplementedException();
	}

	private void action_ProcIA(InstructionArgument ia) {
		ProcTableEntry prte = generatedFunction.getProcTableEntry(DeduceTypes2.to_int(ia));
		int y = 2;
		if (prte.getResolvedElement() == null) {
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
//					prte.setResolvedElement(el);
				prte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
				// handle constructor calls
				if (el instanceof ClassStatement) {
					assert prte.getClassInvocation() == null;
					if (prte.getFunctionInvocation() == null) {
						ClassInvocation ci = new ClassInvocation((ClassStatement) el, null);

						ci = phase.registerClassInvocation(ci);
//						prte.setClassInvocation(ci);
						Collection<ConstructorDef> cs = (((ClassStatement) el).getConstructors());
						ConstructorDef selected_constructor = null;
						if (prte.getArgs().size() == 0 && cs.size() == 0) {
							// TODO use a virtual default ctor
							System.out.println("2262 use a virtual default ctor for " + prte.expression);
							selected_constructor = ConstructorDef.defaultVirtualCtor;
						} else {
							// TODO find a ctor that matches prte.getArgs()
							final List<TypeTableEntry> x = prte.getArgs();
							int yy = 2;
						}
						assert ((ClassStatement) el).getGenericPart().size() == 0;
						FunctionInvocation fi = new FunctionInvocation(selected_constructor, prte, ci, phase.generatePhase);
//						fi.setClassInvocation(ci);
						prte.setFunctionInvocation(fi);
						if (fi.getFunction() instanceof ConstructorDef) {
							GenType genType = new GenType(ci.getKlass());
							genType.ci = ci;
							ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
								@Override
								public void onDone(GeneratedClass result) {
									genType.node = result;
								}
							});
							generatedFunction.addDependentType(genType);
							generatedFunction.addDependentFunction(fi);
						} else
							generatedFunction.addDependentFunction(fi);
					} else {
						// TODO does nothing
						FunctionInvocation fi = prte.getFunctionInvocation();
						ClassInvocation ci = fi.getClassInvocation();
						if (fi.getFunction() instanceof ConstructorDef) {
							GenType genType = new GenType(ci.getKlass());
							genType.ci = ci;
							ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
								@Override
								public void onDone(GeneratedClass result) {
									genType.node = result;
								}
							});
							final WorkList wl = new WorkList();
							final OS_Module module = fi.getClassInvocation().getKlass().getContext().module();
							final GenerateFunctions generateFunctions = deduceTypes2.getGenerateFunctions(module);
							if (prte.getFunctionInvocation().getFunction() == ConstructorDef.defaultVirtualCtor)
								wl.addJob(new WlGenerateDefaultCtor(generateFunctions, fi));
							else
								wl.addJob(new WlGenerateCtor(generateFunctions, fi, null));
							deduceTypes2.wm.addJobs(wl);
//							generatedFunction.addDependentType(genType);
//							generatedFunction.addDependentFunction(fi);
						}
					}
				}
			} catch (ResolveError aResolveError) {
				aResolveError.printStackTrace();
				int yyy = 2;
				throw new NotImplementedException();
			}
		} else {
			el = prte.getResolvedElement();
			ectx = el.getContext();
		}
	}

	private RIA_STATE action_IdentIA(List<InstructionArgument> aS, IdentIA ia) {
		final IdentTableEntry idte = ia.getEntry();
		if (idte.getStatus() == BaseTableEntry.Status.UNKNOWN) {
			System.out.println("1257 Not found for " + generatedFunction.getIdentIAPathNormal(ia));
			// No need checking more than once
			foundElement.doNoFoundElement();
			return RIA_STATE.RETURN;
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
				{
					if (el instanceof FunctionDef) {
						final FunctionDef functionDef = (FunctionDef) el;
						final OS_Element parent = functionDef.getParent();
						GenType genType = null;
						switch (DecideElObjectType.getElObjectType(parent)) {
							case UNKNOWN:
								break;
							case CLASS:
								genType = new GenType((ClassStatement) parent);
								break;
							case NAMESPACE:
								genType = new GenType((NamespaceStatement) parent);
								break;
							default:
								// do nothing
								break;
						}
						if (genType != null)
							generatedFunction.addDependentType(genType);
					} else if (el instanceof ClassStatement) {
						GenType genType = new GenType((ClassStatement) el);
						generatedFunction.addDependentType(genType);
					}
				}
				if (el != null) {
					idte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
					if (el.getContext() != null)
						ectx = el.getContext();
					else {
						throw new IllegalStateException("2468 null context");
					}
				} else {
					errSink.reportError("1179 Can't resolve " + text);
					idte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					foundElement.doNoFoundElement();
					return RIA_STATE.RETURN;
				}
			} else /*if (false)*/ {
				deduceTypes2.resolveIdentIA2_(ectx/*context*/, aS, generatedFunction, new FoundElement(phase) {
					final String z = generatedFunction.getIdentIAPathNormal(ia);

					@Override
					public void foundElement(OS_Element e) {
						foundElement.doFoundElement(e);
						idte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(e));
					}

					@Override
					public void noFoundElement() {
						foundElement.noFoundElement();
						System.out.println("2002 Cant resolve " + z);
						idte.setStatus(BaseTableEntry.Status.UNKNOWN, null);
					}
				});
			}
//				assert idte.getStatus() != BaseTableEntry.Status.UNCHECKED;
		} else if (idte.getStatus() == BaseTableEntry.Status.KNOWN) {
			el = idte.resolved_element;
			ectx = el.getContext();
		}
		return RIA_STATE.NEXT;
	}

	private RIA_STATE action_IntegerIA(InstructionArgument ia) {
		VariableTableEntry vte = generatedFunction.getVarTableEntry(DeduceTypes2.to_int(ia));
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
					return RIA_STATE.CONTINUE;
				}
			}
			//
			// OTHERWISE TYPE INFORMATION MAY BE IN POTENTIAL_TYPES
			//
			@NotNull List<TypeTableEntry> pot = deduceTypes2.getPotentialTypesVte(vte);
			if (pot.size() == 1) {
				OS_Type attached = pot.get(0).getAttached();
				if (attached != null) {
					action_001(attached);
				} else {
					action_002(pot);
				}
			}
		} else {
			errSink.reportError("1001 Can't resolve " + text);
			foundElement.doNoFoundElement();
			return RIA_STATE.RETURN;
		}
		return RIA_STATE.NEXT;
	}

	private void action_002(@NotNull List<TypeTableEntry> aPot) {
		TypeTableEntry tte = aPot.get(0);
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
						final OS_Element resolvedElement = backlink.getResolvedElement();
						assert resolvedElement != null;
						try {
							LookupResultList lrl2 = DeduceLookupUtils.lookupExpression(y.getIdent(), resolvedElement.getContext());
							@Nullable OS_Element best = lrl2.chooseBest(null);
							assert best != null;
							y.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(best));
						} catch (ResolveError aResolveError) {
							aResolveError.printStackTrace();
							assert false;
						}
					} else
						assert false;
				}
				FunctionInvocation fi = null;
				if (y.resolved_element instanceof ClassStatement) {
					// assuming no constructor name or generic parameters based on function syntax
					ClassInvocation ci = new ClassInvocation((ClassStatement) y.resolved_element, null);
					ci = phase.registerClassInvocation(ci);
					fi = new FunctionInvocation(null, pte, ci, phase.generatePhase);
				} else if (y.resolved_element instanceof FunctionDef) {
					final IInvocation invocation = deduceTypes2.getInvocation((GeneratedFunction) generatedFunction);
					fi = new FunctionInvocation((FunctionDef) y.resolved_element, pte, invocation, phase.generatePhase);
				} else
					assert false;
				if (fi != null) {
					if (pte.getFunctionInvocation() == null) {
						pte.setFunctionInvocation(fi);
					}
				}
				el = y.resolved_element;
				ectx = el.getContext();
			}
		}
	}

	private void action_001(OS_Type aAttached) {
		switch (aAttached.getType()) {
			case USER_CLASS: {
				ClassStatement x = aAttached.getClassOf();
				ectx = x.getContext();
				break;
			}
			case FUNCTION: {
				int yy = 2;
				System.err.println("1005");
				FunctionDef x = (FunctionDef) aAttached.getElement();
				ectx = x.getContext();
				break;
			}
			case USER:
				if (el instanceof MatchConditional.MatchArm_TypeMatch) {
					// for example from match conditional
					final TypeName tn = ((MatchConditional.MatchArm_TypeMatch) el).getTypeName();
					try {
						@NotNull final OS_Type ty = deduceTypes2.resolve_type(new OS_Type(tn), tn.getContext());
						ectx = ty.getElement().getContext();
					} catch (ResolveError resolveError) {
						resolveError.printStackTrace();
						System.err.println("1182 Can't resolve " + tn);
						throw new IllegalStateException("ResolveError.");
					}
//						ectx = el.getContext();
				} else
					ectx = aAttached.getTypeName().getContext(); // TODO is this right?
				break;
			case FUNC_EXPR: {
				FuncExpr x = (FuncExpr) aAttached.getElement();
				ectx = x.getContext();
				break;
			}
			default:
				System.err.println("1010 " + aAttached.getType());
				throw new IllegalStateException("Don't know what you're doing here.");
		}
	}

	enum RIA_STATE {
		CONTINUE, RETURN, NEXT
	}
}

//
//
//
