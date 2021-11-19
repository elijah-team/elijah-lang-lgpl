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
import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;

/**
 * Created 11/18/21 12:02 PM
 */
public class DeduceTypeResolve {
	private final BaseTableEntry bte;
	private BaseTableEntry backlink;
	private DeferredObject<GenType, ResolveError, Void> typeResolution = new DeferredObject<GenType, ResolveError, Void>();

	public DeduceTypeResolve(BaseTableEntry aBte) {
		bte = aBte;
		if (bte instanceof IdentTableEntry) {
			((IdentTableEntry) bte).backlinkSet().then(new DoneCallback<InstructionArgument>() {
				@Override
				public void onDone(final InstructionArgument backlink0) {
					if (backlink0 instanceof IdentIA) {
						backlink = ((IdentIA)backlink0).getEntry();
						setBacklinkCallback();
					} else if (backlink0 instanceof IntegerIA) {
						backlink = ((IntegerIA)backlink0).getEntry();
						setBacklinkCallback();
					} else if (backlink0 instanceof ProcIA) {
						backlink = ((ProcIA)backlink0).getEntry();
						setBacklinkCallback();
					} else
						backlink = null;
				}
			});
		} else if (bte instanceof VariableTableEntry) {
			backlink = null;
		} else if (bte instanceof ProcTableEntry) {
			backlink = null;
		} else
			throw new IllegalStateException();

		if (backlink != null) {
		} else {
			bte.addStatusListener(new BaseTableEntry.StatusListener() {
				@Override
				public void onChange(final IElementHolder eh, final BaseTableEntry.Status newStatus) {
					if (newStatus != BaseTableEntry.Status.KNOWN) return;

					GenType genType = new GenType();
					eh.getElement().visitGen(new AbstractCodeGen() {
						@Override
						public void addClass(final ClassStatement klass) {
							genType.resolved = new OS_Type(klass);
						}

						@Override
						public void visitFunctionDef(final FunctionDef aFunctionDef) {
							genType.resolved = new OS_FuncType(aFunctionDef);
						}

						@Override
						public void visitVariableStatement(final VariableStatement variableStatement) {
//							final VariableStatement variableStatement = (VariableStatement) eh.getElement();
							if (variableStatement.typeName() instanceof NormalTypeName) {
								final NormalTypeName normalTypeName = (NormalTypeName) variableStatement.typeName();
								if (normalTypeName.getGenericPart() != null) {
									final TypeNameList genericPart = normalTypeName.getGenericPart();
									if (eh instanceof GenericElementHolderWithType) {
										final GenericElementHolderWithType eh1 = (GenericElementHolderWithType) eh;
										final DeduceTypes2 dt2 = eh1.getDeduceTypes2();
										final OS_Type type = eh1.getType();

										genType.nonGenericTypeName = normalTypeName;

										OS_Type typeName = new OS_Type(normalTypeName);
										try {
											final @NotNull GenType resolved = dt2.resolve_type(typeName, variableStatement.getContext());
											genType.resolved = resolved.resolved;
										} catch (ResolveError aResolveError) {
											aResolveError.printStackTrace();
											assert false;
										}
									} else
										genType.nonGenericTypeName = normalTypeName;
								} else {
									if (!normalTypeName.isNull()) {
										if (eh instanceof GenericElementHolderWithType) {
											final GenericElementHolderWithType eh1 = (GenericElementHolderWithType) eh;
											final DeduceTypes2 dt2 = eh1.getDeduceTypes2();
											final OS_Type type = eh1.getType();

											genType.typeName = new OS_Type(normalTypeName);
											try {
												final @NotNull GenType resolved = dt2.resolve_type(genType.typeName, variableStatement.getContext());
												if (resolved.resolved.getType() == OS_Type.Type.GENERIC_TYPENAME) {
													backlink.typeResolvePromise().then(new DoneCallback<GenType>() {
														@Override
														public void onDone(final GenType result) {
															int y=2;
														}
													});
												} else {
													genType.resolved = resolved.resolved;
												}
											} catch (ResolveError aResolveError) {
												aResolveError.printStackTrace();
												assert false;
											}
										} else
											genType.typeName = new OS_Type(normalTypeName);
									}
								}
							}
						}

						@Override
						public void visitFormalArgListItem(final FormalArgListItem aFormalArgListItem) {
							int y=2;
						}

						@Override
						public void defaultAction(final OS_Element anElement) {
							throw new IllegalStateException();
						}

					});

					if (!typeResolution.isPending()) {
						int y=2;
					} else {
						if (!genType.isNull())
							typeResolution.resolve(genType);
					}
				}
			});
		}


	}

	protected void setBacklinkCallback() {
		backlink.addStatusListener(new BaseTableEntry.StatusListener() {
			@Override
			public void onChange(final IElementHolder eh, final BaseTableEntry.Status newStatus) {
				if (newStatus != BaseTableEntry.Status.KNOWN) return;

				if (backlink instanceof IdentTableEntry){
					final IdentTableEntry identTableEntry = (IdentTableEntry) backlink;
					identTableEntry.typeResolvePromise().done(new DoneCallback<GenType>() {
						@Override
						public void onDone(final GenType result) {
							identTableEntry.type.setAttached(result);
						}
					});
				} else if (backlink instanceof VariableTableEntry) {
					final VariableTableEntry variableTableEntry = (VariableTableEntry) backlink;
					variableTableEntry.typeResolvePromise().done(new DoneCallback<GenType>() {
						@Override
						public void onDone(final GenType result) {
							variableTableEntry.type.setAttached(result);
						}
					});

				} else if (backlink instanceof ProcTableEntry) {
					final ProcTableEntry procTableEntry = (ProcTableEntry) backlink;
					procTableEntry.typeResolvePromise().done(new DoneCallback<GenType>() {
						@Override
						public void onDone(final GenType result) {
							//					procTableEntry.type.setAttached(result);
							int y=2;
						}
					});

				}
			}
		});

	}
	public Promise<GenType, ResolveError, Void> typeResolution() {
		return typeResolution.promise();
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
