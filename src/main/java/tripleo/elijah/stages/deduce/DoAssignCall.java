/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.deduce.declarations.DeferredMemberFunction;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.NotImplementedException;

import java.util.List;

/**
 * Created 12/12/21 12:30 AM
 */
public class DoAssignCall {
	private final ElLog LOG;
	private final DeduceTypes2.DeduceClient4 dc;
	private final OS_Module module;
	private final ErrSink errSink;

	public DoAssignCall(final DeduceTypes2.DeduceClient4 aDeduceClient4) {
		dc = aDeduceClient4;
		//
		module = dc.getModule();
		LOG = dc.getLOG();
		errSink = dc.getErrSink();
	}

	void do_assign_call(final @NotNull BaseGeneratedFunction generatedFunction,
						final @NotNull Instruction instruction,
						final @NotNull VariableTableEntry vte,
						final @NotNull FnCallArgs fca,
						final @NotNull Context ctx) {
		final int instructionIndex = instruction.getIndex();
		final @NotNull ProcTableEntry pte = generatedFunction.getProcTableEntry(DeduceTypes2.to_int(fca.getArg(0)));
		@NotNull IdentIA identIA = (IdentIA) pte.expression_num;

		if (vte.getStatus() == BaseTableEntry.Status.UNCHECKED) {
			pte.typePromise().then(new DoneCallback<GenType>() {
				@Override
				public void onDone(GenType result) {
					vte.resolveType(result);
				}
			});
			if (vte.getResolvedElement() != null) {
				try {
					OS_Element el;
					if (vte.getResolvedElement() instanceof IdentExpression)
						el = dc.lookup((IdentExpression) vte.getResolvedElement(), ctx);
					else
						el = dc.lookup(((VariableStatement) vte.getResolvedElement()).getNameToken(), ctx);
					vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
				} catch (ResolveError aResolveError) {
					dc.reportDiagnostic(aResolveError);
					return;
				}
			}
		}

		if (identIA != null) {
			//			LOG.info("594 "+identIA.getEntry().getStatus());

			{
				final @NotNull IdentTableEntry ite = identIA.getEntry();
				final OS_Element resolved_element = ite.getResolvedElement();

				if (resolved_element != null) {
					final @NotNull OS_Module mod1 = resolved_element.getContext().module();

					if (mod1 != module) {
						if (resolved_element instanceof FunctionDef) {
							final OS_Element parent = resolved_element.getParent();
							final @Nullable ClassInvocation invocation = dc.registerClassInvocation((ClassStatement) parent, null);
							final @NotNull FunctionInvocation fi = dc.newFunctionInvocation((FunctionDef) resolved_element, pte, invocation);
							final DeferredMemberFunction dmf = dc.deferred_member_function(parent, invocation, (FunctionDef) resolved_element, fi);

							dmf.typeResolved().then(new DoneCallback<GenType>() {
								@Override
								public void onDone(final GenType result) {
									LOG.info("2717 " + dmf.getFunctionDef() + " " + result);
									pte.typeDeferred().resolve(result);
								}
							});
						}
					}
				}
			}

			dc.resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(dc.getPhase()) {

				final String xx = generatedFunction.getIdentIAPathNormal(identIA);

				@Override
				public void foundElement(OS_Element e) {
					//					LOG.info(String.format("600 %s %s", xx ,e));
					//					LOG.info("601 "+identIA.getEntry().getStatus());
					dc.found_element_for_ite(generatedFunction, identIA.getEntry(), e, ctx);

					final OS_Element resolved_element = identIA.getEntry().getResolvedElement();

					if (e instanceof AliasStatement) {
						while (e instanceof AliasStatement)
							e = dc._resolveAlias((AliasStatement) e);
					}

					assert e == resolved_element || /*HACK*/ resolved_element instanceof AliasStatement || resolved_element == null;

					//					set_resolved_element_pte(identIA, e, pte);
					pte.setStatus(BaseTableEntry.Status.KNOWN, new ConstructableElementHolder(e, identIA));
					pte.onFunctionInvocation(new DoneCallback<FunctionInvocation>() {
						@Override
						public void onDone(@NotNull FunctionInvocation result) {
							result.generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
								@Override
								public void onDone(@NotNull BaseGeneratedFunction bgf) {
									@NotNull DeduceTypes2.PromiseExpectation<GenType> pe = dc.promiseExpectation(bgf, "Function Result type");
									bgf.typePromise().then(new DoneCallback<GenType>() {
										@Override
										public void onDone(@NotNull GenType result) {
											pe.satisfy(result);
											@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, result.resolved); // TODO there has to be a better way
											tte.genType.copy(result);
											vte.addPotentialType(instructionIndex, tte);
										}
									});
								}
							});
						}
					});
				}

				@Override
				public void noFoundElement() {
					// TODO create Diagnostic and quit
					LOG.info("1005 Can't find element for " + xx);
				}
			});
		}
		List<TypeTableEntry> args = pte.getArgs();
		for (int i = 0; i < args.size(); i++) {
			final TypeTableEntry tte = args.get(i); // TODO this looks wrong
			//			LOG.info("770 "+tte);
			IExpression e = tte.expression;
			if (e == null) continue;
			if (e instanceof SubExpression) e = ((SubExpression) e).getExpression();
			switch (e.getKind()) {
			case NUMERIC:
				tte.setAttached(new OS_Type(BuiltInTypes.SystemInteger));
				//vte.type = tte;
				break;
			case CHAR_LITERAL:
				tte.setAttached(new OS_Type(BuiltInTypes.SystemCharacter));
				break;
			case IDENT:
				dc.do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, (IdentExpression) e);
				break;
			case PROCEDURE_CALL: {
				final @NotNull ProcedureCallExpression pce = (ProcedureCallExpression) e;
				try {
					final LookupResultList lrl = dc.lookupExpression(pce.getLeft(), ctx);
					@Nullable OS_Element best = lrl.chooseBest(null);
					if (best != null) {
						while (best instanceof AliasStatement) {
							best = dc._resolveAlias2((AliasStatement) best);
						}
						if (best instanceof FunctionDef) {
							final OS_Element parent = best.getParent();
							@Nullable IInvocation invocation;
							if (parent instanceof NamespaceStatement) {
								invocation = dc.registerNamespaceInvocation((NamespaceStatement) parent);
							} else if (parent instanceof ClassStatement) {
								@NotNull ClassInvocation ci = new ClassInvocation((ClassStatement) parent, null);
								invocation = dc.registerClassInvocation(ci);
							} else
								throw new NotImplementedException(); // TODO implement me

							dc.forFunction(dc.newFunctionInvocation((FunctionDef) best, pte, invocation), new ForFunction() {
								@Override
								public void typeDecided(@NotNull GenType aType) {
									tte.setAttached(aType);
//									vte.addPotentialType(instructionIndex, tte);
								}
							});
//							tte.setAttached(new OS_FuncType((FunctionDef) best));

						} else {
							final int y = 2;
							throw new NotImplementedException();
						}
					} else {
						final int y = 2;
						throw new NotImplementedException();
					}
				} catch (ResolveError aResolveError) {
//					aResolveError.printStackTrace();
//					int y=2;
//					throw new NotImplementedException();
					dc.reportDiagnostic(aResolveError);
					tte.setAttached(new OS_UnknownType(new StatementWrapper(pce.getLeft(), null, null)));
				}
			}
			break;
			case DOT_EXP: {
				final @NotNull DotExpression de = (DotExpression) e;
				try {
					final LookupResultList lrl = dc.lookupExpression(de.getLeft(), ctx);
					@Nullable OS_Element best = lrl.chooseBest(null);
					if (best != null) {
						while (best instanceof AliasStatement) {
							best = dc._resolveAlias2((AliasStatement) best);
						}
						if (best instanceof FunctionDef) {
							tte.setAttached(((FunctionDef) best).getOS_Type());
							//vte.addPotentialType(instructionIndex, tte);
						} else if (best instanceof ClassStatement) {
							tte.setAttached(((ClassStatement) best).getOS_Type());
						} else if (best instanceof VariableStatement) {
							final @NotNull VariableStatement vs = (VariableStatement) best;
							@Nullable InstructionArgument vte_ia = generatedFunction.vte_lookup(vs.getName());
							TypeTableEntry tte1 = ((IntegerIA) vte_ia).getEntry().type;
							tte.setAttached(tte1.getAttached());
						} else {
							final int y = 2;
							LOG.err(best.getClass().getName());
							throw new NotImplementedException();
						}
					} else {
						final int y = 2;
						throw new NotImplementedException();
					}
				} catch (ResolveError aResolveError) {
					aResolveError.printStackTrace();
					int y = 2;
					throw new NotImplementedException();
				}
			}
			break;
			case ADDITION:
			case MODULO:
			case SUBTRACTION:
				int y = 2;
				System.err.println("2363");
				break;
			case GET_ITEM: {
				final @NotNull GetItemExpression gie = (GetItemExpression) e;
				dc.do_assign_call_GET_ITEM(gie, tte, generatedFunction, ctx);
				continue;
			}
//			break;
			default:
				throw new IllegalStateException("Unexpected value: " + e.getKind());
			}
		}
		{
			if (pte.expression_num == null) {
				if (fca.expression_to_call.getName() != InstructionName.CALLS) {
					final String text = ((IdentExpression) pte.expression).getText();
					final LookupResultList lrl = ctx.lookup(text);

					final @Nullable OS_Element best = lrl.chooseBest(null);
					if (best != null)
						pte.setResolvedElement(best); // TODO do we need to add a dependency for class?
					else {
						errSink.reportError("Cant resolve " + text);
					}
				} else {
					dc.implement_calls(generatedFunction, ctx.getParent(), instruction.getArg(1), pte, instructionIndex);
				}
			} else {
				final int y = 2;
				dc.resolveIdentIA_(ctx, identIA, generatedFunction, new FoundElement(dc.getPhase()) {

					final String x = generatedFunction.getIdentIAPathNormal(identIA);

					@Override
					public void foundElement(OS_Element el) {
						if (pte.getResolvedElement() == null)
							pte.setResolvedElement(el);
						if (el instanceof FunctionDef) {
							@NotNull FunctionDef fd = (FunctionDef) el;
							final @Nullable IInvocation invocation;
							if (fd.getParent() == generatedFunction.getFD().getParent()) {
								invocation = dc.getInvocation((GeneratedFunction) generatedFunction);
							} else {
								if (fd.getParent() instanceof NamespaceStatement) {
									NamespaceInvocation ni = dc.registerNamespaceInvocation((NamespaceStatement) fd.getParent());
									invocation = ni;
								} else if (fd.getParent() instanceof ClassStatement) {
									final @NotNull ClassStatement classStatement = (ClassStatement) fd.getParent();
									@Nullable ClassInvocation ci = new ClassInvocation(classStatement, null);
									final @NotNull List<TypeName> genericPart = classStatement.getGenericPart();
									if (genericPart.size() > 0) {
										// TODO handle generic parameters somehow (getInvocationFromBacklink?)

									}
									ci = dc.registerClassInvocation(ci);
									invocation = ci;
								} else
									throw new NotImplementedException();
							}
							dc.forFunction(dc.newFunctionInvocation(fd, pte, invocation), new ForFunction() {
								@Override
								public void typeDecided(@NotNull GenType aType) {
									if (!vte.typeDeferred_isPending()) {
										if (vte.resolvedType() == null) {
											final @Nullable ClassInvocation ci = dc.genCI(aType, null);
											vte.type.genTypeCI(ci);
											ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
												@Override
												public void onDone(GeneratedClass result) {
													vte.resolveTypeToClass(result);
												}
											});
										}
										LOG.err("2041 type already found " + vte);
										return; // type already found
									}
									// I'm not sure if below is ever called
									@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, dc.gt(aType), pte.expression, pte);
									vte.addPotentialType(instructionIndex, tte);
								}
							});
						} else if (el instanceof ClassStatement) {
							@NotNull ClassStatement kl = (ClassStatement) el;
							@NotNull OS_Type type = kl.getOS_Type();
							@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, pte.expression, pte);
							vte.addPotentialType(instructionIndex, tte);
							vte.setConstructable(pte);

							dc.register_and_resolve(vte, kl);
						} else {
							LOG.err("7890 " + el.getClass().getName());
						}
					}

					@Override
					public void noFoundElement() {
						LOG.err("IdentIA path cannot be resolved " + x);
					}
				});
			}
		}
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
