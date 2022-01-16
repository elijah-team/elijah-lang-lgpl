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
import org.jdeferred2.FailCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 7/21/21 7:33 PM
 */
class Resolve_Ident_IA2 {
	private final DeduceTypes2 deduceTypes2;
	private final ErrSink errSink;
	private final DeducePhase phase;
	private final @NotNull BaseGeneratedFunction generatedFunction;
	private final @NotNull FoundElement foundElement;
	
	private final @NotNull ElLog LOG;
	
	public Resolve_Ident_IA2(final DeduceTypes2 aDeduceTypes2,
							 ErrSink aErrSink,
							 DeducePhase aPhase,
							 @NotNull BaseGeneratedFunction aGeneratedFunction,
							 @NotNull FoundElement aFoundElement) {
		deduceTypes2 = aDeduceTypes2;
		errSink = aErrSink;
		phase = aPhase;
		generatedFunction = aGeneratedFunction;
		foundElement = aFoundElement;
		//
		LOG = deduceTypes2.LOG;
	}

	@Nullable OS_Element el = null;
	Context ectx;

	public void resolveIdentIA2_(final @NotNull Context ctx,
								 final @Nullable IdentIA identIA,
								 @Nullable List<InstructionArgument> s) {
		el = null;
		ectx = ctx;

		if (identIA == null)
			assert s != null;

		if (s == null)
			s = generatedFunction._getIdentIAPathList(identIA);

		if (identIA != null) {
			DeducePath dp = identIA.getEntry().buildDeducePath(generatedFunction);
			int index = dp.size() - 1;
			InstructionArgument ia2 = dp.getIA(index);
			// ia2 is not == equals to identIA, but functionally equivalent
			if (ia2 instanceof IdentIA) {
				final @NotNull IdentTableEntry ite = ((IdentIA) ia2).getEntry();
				if (ite.getBacklink() != null) {
					InstructionArgument backlink = ite.getBacklink();
					if (backlink instanceof IntegerIA) {
						final @NotNull IntegerIA integerIA = (IntegerIA) backlink;
						@NotNull VariableTableEntry vte = integerIA.getEntry();
						final DeduceTypes2.PromiseExpectation<GenType> pe = deduceTypes2.promiseExpectation(vte, "TypePromise for vte "+vte);
						vte.typePromise().then(new DoneCallback<GenType>() {
							@Override
							public void onDone(@NotNull GenType result) {
								pe.satisfy(result);
								switch (result.resolved.getType()) {
								case FUNCTION:
									ectx = result.resolved.getElement().getContext();
									break;
								case USER_CLASS:
									ectx = result.resolved.getClassOf().getContext();
									break;
								default:
									throw new IllegalStateException("Unexpected value: " + result.resolved.getType());
								}
								ia2_IdentIA((IdentIA) ia2, ectx);
								foundElement.doFoundElement(el);
							}
						});
					} else if (backlink instanceof IdentIA) {
						dp.getElementPromise(index, new DoneCallback<OS_Element>() {
							@Override
							public void onDone(@NotNull OS_Element result) {
								el = result;
								ectx = result.getContext();
								ia2_IdentIA((IdentIA) ia2, ectx);
								foundElement.doFoundElement(el);
							}
						}, new FailCallback<Diagnostic>() {
							@Override
							public void onFail(Diagnostic result) {
								foundElement.doNoFoundElement();
							}
						});
						dp.getElementPromise(index - 1, new DoneCallback<OS_Element>() {
							@Override
							public void onDone(@NotNull OS_Element result) {
								ia2_IdentIA((IdentIA) dp.getIA(index-1), result.getContext()); // might fail
							}
						}, null);

					}
				} else {
					if (!ite.hasResolvedElement()) {
						ia2_IdentIA((IdentIA) ia2, ectx);
						foundElement.doFoundElement(el);
					}
				}
			}
//			el = dp.getElement(dp.size()-1);
		} else {
			for (InstructionArgument ia2 : s) {
				if (ia2 instanceof IntegerIA) {
					ia2_IntegerIA((IntegerIA) ia2, ectx);
				} else if (ia2 instanceof IdentIA) {
					@NotNull RIA_STATE st = ia2_IdentIA((IdentIA) ia2, ectx);

					switch (st) {
						case CONTINUE:
							continue;
						case NEXT:
							break;
						case RETURN:
							return;
					}
				} else if (ia2 instanceof ProcIA) {
					LOG.err("1373 ProcIA");
//						@NotNull ProcTableEntry pte = ((ProcIA) ia2).getEntry(); // README ectx seems to be set up already
					return;
				} else
					throw new NotImplementedException();
			}
			foundElement.doFoundElement(el);
		}
	}

	private @NotNull RIA_STATE ia2_IdentIA(@NotNull IdentIA ia2, @NotNull Context ectx) {
		final @NotNull IdentTableEntry idte2 = ia2.getEntry();
		final String text = idte2.getIdent().getText();

		if (idte2.getStatus() == BaseTableEntry.Status.KNOWN) {
			el = idte2.getResolvedElement();
			assert el != null;
		} else {
			final LookupResultList lrl = ectx.lookup(text);
			el = lrl.chooseBest(null);

			if (el == null) {
				errSink.reportDiagnostic(new ResolveError(idte2.getIdent(), lrl));
//				errSink.reportError("1007 Can't resolve " + text);
				foundElement.doNoFoundElement();
				return RIA_STATE.RETURN;
			}
		}

		if (!idte2.hasResolvedElement()) {
			idte2.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(el));
		}
		if (idte2.type == null) {
			if (el instanceof VariableStatement) {
				@NotNull VariableStatement vs = (VariableStatement) el;
				ia2_IdentIA_VariableStatement(idte2, vs, ectx);
			} else if (el instanceof FunctionDef) {
				ia2_IdentIA_FunctionDef(idte2);
			}
		}
		if (idte2.type != null) {
			assert idte2.type.getAttached() != null;
			if (findResolved(idte2, ectx)) return RIA_STATE.CONTINUE;
		} else {
//			throw new IllegalStateException("who knows");
			errSink.reportWarning("2010 idte2.type == null for " + text);
		}

		return RIA_STATE.NEXT;
	}

	private void ia2_IdentIA_FunctionDef(@NotNull IdentTableEntry idte2) {
		@Nullable OS_Type attached = new OS_UnknownType(el);
		@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, idte2);
		idte2.type = tte;

		// Set type to something other than Unknown when found
		@Nullable ProcTableEntry pte = idte2.getCallablePTE();
		if (pte == null) {
			assert false;
		} else {
//			assert pte != null;
			@Nullable FunctionInvocation fi = pte.getFunctionInvocation();
			if (fi == null) {
				InstructionArgument bl = idte2.getBacklink();
				@Nullable IInvocation invocation = null;
				if (bl instanceof IntegerIA) {
					final @NotNull IntegerIA integerIA = (IntegerIA) bl;
					@NotNull VariableTableEntry vte = integerIA.getEntry();
					if (vte.constructable_pte != null) {
						ProcTableEntry cpte = vte.constructable_pte;
						invocation = cpte.getFunctionInvocation().getClassInvocation();
					} else if (vte.typeDeferred_isResolved()) {
						final IInvocation[] ainvocation = new IInvocation[1];
						vte.typePromise().then(new DoneCallback<GenType>() {
							@Override
							public void onDone(final GenType result) {
								if (result.ci == null) {
									deduceTypes2.genCIForGenType2(result);
								}
								ainvocation[0] = result.ci;
							}
						});
						invocation = ainvocation[0];
					}
				} else if (bl instanceof IdentIA) {
					final @NotNull IdentIA identIA = (IdentIA) bl;
					@NotNull IdentTableEntry ite = identIA.getEntry();
					if (ite.type.genType.ci != null)
						invocation = ite.type.genType.ci;
					else if (ite.type.getAttached() != null) {
						@NotNull OS_Type attached1 = ite.type.getAttached();
						assert attached1.getType() == OS_Type.Type.USER_CLASS;
						invocation = phase.registerClassInvocation(attached1.getClassOf(), null); // TODO will fail one day
						// TODO dont know if next line is right
						final ClassInvocation invocation2 = DeduceTypes2.ClassInvocationMake.withGenericPart(attached1.getClassOf(), null, (NormalTypeName) tte.genType.nonGenericTypeName, deduceTypes2, errSink);
						int y=2;
					}
				} else if (bl instanceof ProcIA) {
					final @NotNull ProcIA procIA = (ProcIA) bl;
					FunctionInvocation bl_fi = procIA.getEntry().getFunctionInvocation();
					if (bl_fi.getClassInvocation() != null) {
						invocation = bl_fi.getClassInvocation();
					} else if (bl_fi.getNamespaceInvocation() != null) {
						invocation = bl_fi.getNamespaceInvocation();
					}
				}

				if (invocation == null) {
					int y=2;
					// assert false;
				}
				fi = new FunctionInvocation((BaseFunctionDef) el, pte, invocation, phase.generatePhase);
				pte.setFunctionInvocation(fi);
			}

			pte.getFunctionInvocation().generatePromise().then(new DoneCallback<BaseGeneratedFunction>() {
				@Override
				public void onDone(@NotNull BaseGeneratedFunction result) {
					result.typePromise().then(new DoneCallback<GenType>() {
						@Override
						public void onDone(GenType result) {
							// NOTE there is no Promise-type notification for when type changes
							idte2.type.setAttached(result);
						}
					});
				}
			});
		}
	}

	/* @requires idte2.type.getAttached() != null; */
	private boolean findResolved(@NotNull IdentTableEntry idte2, Context ctx) {
		try {
			if (idte2.type.getAttached() instanceof OS_UnknownType) // TODO ??
				return false;

			final OS_Type attached = idte2.type.getAttached();
			if (attached.getType() == OS_Type.Type.USER_CLASS) {
				if (idte2.type.genType.resolved == null) {
					@NotNull GenType rtype = deduceTypes2.resolve_type(attached, ctx);
					if (rtype.resolved != null) {
						switch (rtype.resolved.getType()) {
						case USER_CLASS:
							ctx = rtype.resolved.getClassOf().getContext();
							break;
						case FUNCTION:
							ctx = ((OS_FuncType) rtype.resolved).getElement().getContext();
							break;
						}
						idte2.type.setAttached(rtype); // TODO may be losing alias information here
					}
				}
			}
		} catch (ResolveError resolveError) {
			if (resolveError.resultsList().size() > 1)
				errSink.reportDiagnostic(resolveError);
			else
				LOG.info("1089 Can't attach type to " + idte2.type.getAttached());
//				resolveError.printStackTrace(); // TODO print diagnostic
			return true;
		}
		return false;
	}

	private void ia2_IdentIA_VariableStatement(@NotNull IdentTableEntry idte, @NotNull VariableStatement vs, @NotNull Context ctx) {
		try {
			final boolean has_initial_value = vs.initialValue() != IExpression.UNASSIGNED;
			if (!vs.typeName().isNull()) {
				TypeTableEntry tte;
				@Nullable GenType attached;
				if (has_initial_value) {
					attached = DeduceLookupUtils.deduceExpression(deduceTypes2, vs.initialValue(), ctx);
				} else { // if (vs.typeName() != null) {
					attached = new GenType();
					attached.set(new OS_Type(vs.typeName()));
				}

				@Nullable IExpression initialValue;

				if (has_initial_value) {
					initialValue = vs.initialValue();
				} else {
//					attached = new OS_Type(vs.typeName());
					initialValue = null; // README presumably there is none, ie when generated
				}

				final OS_Type resolvedOrTypename = attached.resolved != null ? attached.resolved : attached.typeName;
				tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, resolvedOrTypename, initialValue);
				idte.type = tte;
			} else if (has_initial_value) {
				@Nullable GenType attached = DeduceLookupUtils.deduceExpression(deduceTypes2, vs.initialValue(), ctx);
				final OS_Type resolvedOrTypename = attached.resolved != null ? attached.resolved : attached.typeName;
				@NotNull TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, resolvedOrTypename, vs.initialValue());
				idte.type = tte;
			} else {
				LOG.err("Empty Variable Expression");
				throw new IllegalStateException("Empty Variable Expression");
//				return; // TODO call noFoundElement, raise exception
			}
		} catch (ResolveError aResolveError) {
			LOG.err("1937 resolve error " + vs.getName());
//			aResolveError.printStackTrace();
			errSink.reportDiagnostic(aResolveError);
		}
	}

	private void ia2_IntegerIA(@NotNull IntegerIA ia2, @NotNull Context ctx) {
		@NotNull VariableTableEntry vte = generatedFunction.getVarTableEntry(DeduceTypes2.to_int(ia2));
		final String text = vte.getName();

		{
			@NotNull List<TypeTableEntry> pot = deduceTypes2.getPotentialTypesVte(vte);
			if (pot.size() == 1) {
				final OS_Type attached = pot.get(0).getAttached();
				if (attached == null) {
					ia2_IntegerIA_null_attached(ctx, pot);
				} else {
					// TODO what is the state of vte.genType here?
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
							LOG.err("1098 " + attached.getType());
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
				case USER:
					try {
						@NotNull GenType ty = deduceTypes2.resolve_type(attached, ctx);
						ectx = ty.resolved.getClassOf().getContext();
					} catch (ResolveError resolveError) {
						LOG.err("1300 Can't resolve " + attached);
						resolveError.printStackTrace();
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + attached.getType());
			}
		} else {
			if (vte.potentialTypes().size() == 1) {
				ia2_IntegerIA_potentialTypes_equals_1(vte, text);
			}
		}
	}

	private void ia2_IntegerIA_potentialTypes_equals_1(@NotNull VariableTableEntry aVte, String aText) {
		int state = 0;
		final @NotNull ArrayList<TypeTableEntry> pot = deduceTypes2.getPotentialTypesVte(aVte);
		final OS_Type attached1 = pot.get(0).getAttached();
		TableEntryIV te = pot.get(0).tableEntry;
		if (te instanceof ProcTableEntry) {
			final @NotNull ProcTableEntry procTableEntry = (ProcTableEntry) te;
			// This is how it should be done, with an Incremental
			procTableEntry.getFunctionInvocation().generateDeferred().done(new DoneCallback<BaseGeneratedFunction>() {
				@Override
				public void onDone(@NotNull BaseGeneratedFunction result) {
					result.typePromise().then(new DoneCallback<GenType>() {
						@Override
						public void onDone(GenType result) {
							int y = 2;
							aVte.resolveType(result); // save for later
						}
					});
				}
			});
			// but for now, just set ectx
			InstructionArgument en = procTableEntry.expression_num;
			if (en instanceof IdentIA) {
				final @NotNull IdentIA identIA2 = (IdentIA) en;
				DeducePath ded = identIA2.getEntry().buildDeducePath(generatedFunction);
				@Nullable OS_Element el2 = ded.getElement(ded.size() - 1);
				if (el2 != null) {
					state = 1;
					ectx = el2.getContext();
					aVte.type.setAttached(attached1);
				}
			}
		}
		switch (state) {
			case 0:
				assert attached1 != null;
				aVte.type.setAttached(attached1);
				// TODO this will break
				switch (attached1.getType()) {
					case USER:
						final TypeName attached1TypeName = attached1.getTypeName();
						assert attached1TypeName instanceof RegularTypeName;
						final Qualident realName = ((RegularTypeName) attached1TypeName).getRealName();
						try {
							final List<LookupResult> lrl = DeduceLookupUtils.lookupExpression(realName, ectx, deduceTypes2).results();
							ectx = lrl.get(0).getElement().getContext();
						} catch (ResolveError aResolveError) {
							aResolveError.printStackTrace();
							int y = 2;
							throw new NotImplementedException();
						}
						break;
					case USER_CLASS:
						ectx = attached1.getClassOf().getContext();
						break;
					default:
						final TypeName typeName = attached1.getTypeName();
						errSink.reportError("1442 Don't know " + typeName.getClass().getName());
						throw new NotImplementedException();
				}
				break;
			case 1:
				break;
			default:
				LOG.info("1006 Can't find type of " + aText);
				break;
		}
	}

	/* @requires pot.get(0).getAttached() == null; */
	private void ia2_IntegerIA_null_attached(@NotNull Context ctx, @NotNull List<TypeTableEntry> pot) {
		try {
			@Nullable FunctionDef fd = null;
			@Nullable ProcTableEntry pte = null;
			TableEntryIV xx = pot.get(0).tableEntry;
			if (xx != null) {
				if (xx instanceof ProcTableEntry) {
					final @NotNull ProcTableEntry procTableEntry = (ProcTableEntry) xx;
					pte = procTableEntry;
					InstructionArgument xxx = procTableEntry.expression_num;
					if (xxx instanceof IdentIA) {
						final @NotNull IdentIA identIA2 = (IdentIA) xxx;
						@NotNull IdentTableEntry ite = identIA2.getEntry();
						DeducePath deducePath = ite.buildDeducePath(generatedFunction);
						@Nullable OS_Element el5 = deducePath.getElement(deducePath.size() - 1);
						int y = 2;
						fd = (FunctionDef) el5;
					}
				}
			} else {
				LookupResultList lrl = DeduceLookupUtils.lookupExpression(pot.get(0).expression.getLeft(), ctx, deduceTypes2);
				@Nullable OS_Element best = lrl.chooseBest(Helpers.List_of(
						new DeduceUtils.MatchFunctionArgs(
								(ProcedureCallExpression) pot.get(0).expression)));
				if (best instanceof FunctionDef) {
					fd = (FunctionDef) best;
				} else {
					fd = null;
					LOG.err("1195 Can't find match");
				}
			}
			if (fd != null) {
				final IInvocation invocation = deduceTypes2.getInvocation((GeneratedFunction) generatedFunction);
				final @Nullable FunctionDef fd2 = fd;
				deduceTypes2.forFunction(deduceTypes2.newFunctionInvocation(fd2, pte, invocation, phase), new ForFunction() {
					@Override
					public void typeDecided(@NotNull GenType aType) {
						assert fd2 == generatedFunction.getFD();
						//
						pot.get(0).setAttached(deduceTypes2.gt(aType));
					}
				});
			} else {
				errSink.reportError("1196 Can't find function");
			}
		} catch (ResolveError aResolveError) {
			aResolveError.printStackTrace();
			int y = 2;
			throw new NotImplementedException();
		}
	}

	enum RIA_STATE {
		CONTINUE, RETURN, NEXT
	}
}

//
//
//
