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
import tripleo.elijah.stages.deduce.declarations.DeferredMember;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.BaseTableEntry;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GenericElementHolder;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.logging.ElLog;

/**
 * Created 9/2/21 11:36 PM
 */
class Found_Element_For_ITE {

	private final BaseGeneratedFunction generatedFunction;
	private final Context ctx;
	private final ElLog LOG;
	private final ErrSink errSink;
	private final DeduceTypes2.DeduceClient1 dc;

	public Found_Element_For_ITE(BaseGeneratedFunction aGeneratedFunction, Context aCtx, ElLog aLOG, ErrSink aErrSink, DeduceTypes2.DeduceClient1 aDeduceClient1) {
		generatedFunction = aGeneratedFunction;
		ctx = aCtx;
		LOG = aLOG;
		errSink = aErrSink;
		dc = aDeduceClient1;
	}

	public void action(@NotNull IdentTableEntry ite) {
		final OS_Element y = ite.getResolvedElement();

		if (y instanceof VariableStatement) {
			action_VariableStatement(ite, (VariableStatement) y);
		} else if (y instanceof ClassStatement) {
			action_ClassStatement(ite, (ClassStatement) y);
		} else if (y instanceof FunctionDef) {
			action_FunctionDef(ite, (FunctionDef) y);
		} else if (y instanceof PropertyStatement) {
			action_PropertyStatement(ite, (PropertyStatement) y);
		} else if (y instanceof AliasStatement) {
			action_AliasStatement(ite, (AliasStatement) y);
		} else {
			//LookupResultList exp = lookupExpression();
			LOG.info("2009 " + y);
		}
	}

	public void action_AliasStatement(@NotNull IdentTableEntry ite, @NotNull AliasStatement y) {
		LOG.err("396 AliasStatement");
		@Nullable OS_Element x = dc._resolveAlias(y);
		if (x == null) {
			ite.setStatus(BaseTableEntry.Status.UNKNOWN, null);
			errSink.reportError("399 resolveAlias returned null");
		} else {
			ite.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(x));
			dc.found_element_for_ite(generatedFunction, ite, x, ctx);
		}
	}

	public void action_PropertyStatement(@NotNull IdentTableEntry ite, @NotNull PropertyStatement ps) {
		OS_Type attached;
		switch (ps.getTypeName().kindOfType()) {
			case GENERIC:
				attached = new OS_Type(ps.getTypeName());
				break;
			case NORMAL:
				try {
					attached = new OS_Type(dc.resolve_type(new OS_Type(ps.getTypeName()), ctx).resolved.getClassOf());
				} catch (ResolveError resolveError) {
					LOG.err("378 resolveError");
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
	}

	public void action_FunctionDef(@NotNull IdentTableEntry ite, FunctionDef functionDef) {
		@NotNull OS_Type attached = new OS_FuncType(functionDef);
		if (ite.type == null) {
			ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
		} else
			ite.type.setAttached(attached);
	}

	public void action_ClassStatement(@NotNull IdentTableEntry ite, ClassStatement classStatement) {
		@NotNull OS_Type attached = new OS_Type(classStatement);
		if (ite.type == null) {
			ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, attached, null, ite);
		} else
			ite.type.setAttached(attached);
	}

	public void action_VariableStatement(@NotNull IdentTableEntry ite, @NotNull VariableStatement vs) {
		@NotNull TypeName typeName = vs.typeName();
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
						final @NotNull GeneratedFunction generatedFunction1 = (GeneratedFunction) generatedFunction;
						state = (parent != generatedFunction1.getFD().getParent());
					} else {
						state = (parent != ((GeneratedConstructor) generatedFunction).getFD().getParent());
					}
					if (state) {
						@NotNull DeferredMember dm = dc.deferred_member(parent, dc.getInvocationFromBacklink(ite.backlink), vs, ite);
						dm.typePromise().
								done(new DoneCallback<GenType>() {
									@Override
									public void onDone(@NotNull GenType result) {
										if (ite.type == null)
											ite.type = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, vs.initialValue());
										assert result.resolved != null;
										ite.setGenType(result);
									}
								});
					}

					@Nullable GenType genType = null;
					if (parent instanceof NamespaceStatement)
						genType = new GenType((NamespaceStatement) parent);
					else if (parent instanceof ClassStatement)
						genType = new GenType((ClassStatement) parent);

					generatedFunction.addDependentType(genType);
				}
				LOG.err("394 typename is null " + vs.getName());
			}
		}
	}
}

//
//
//
