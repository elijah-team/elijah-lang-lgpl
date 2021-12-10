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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 11/18/21 10:51 PM
 */
public class ResolveType {
	static @NotNull GenType resolve_type(final OS_Module module,
										 final @NotNull OS_Type type,
										 final Context ctx,
										 final ElLog LOG,
										 final DeduceTypes2 dt2) throws ResolveError {
		@NotNull GenType R = new GenType();
		if (type.getType() != OS_Type.Type.USER_CLASS)
			R.typeName = type;

		switch (type.getType()) {

		case BUILT_IN:
			resolve_built_in(module, type, dt2, R);
			break;
		case USER:
			resolve_user(type, LOG, dt2, R);
			break;
		case USER_CLASS:
			R.resolved = type;
			break;
		case FUNCTION:
			break;
		case FUNC_EXPR:
			R.resolved = type;//((OS_FuncExprType)type).getElement();
			break;
		default:
			throw new IllegalStateException("565 Unexpected value: " + type.getType());
		}

		return R;
	}

	private static void resolve_user(final @NotNull OS_Type type, final ElLog LOG, final DeduceTypes2 dt2, final @NotNull GenType aR) throws ResolveError {
		final TypeName tn1 = type.getTypeName();
		switch (tn1.kindOfType()) {
		case NORMAL:
		{
			final Qualident tn = ((NormalTypeName) tn1).getRealName();
			LOG.info("799 [resolving USER type named] " + tn);
			final LookupResultList lrl = DeduceLookupUtils.lookupExpression(tn, tn1.getContext(), dt2);
			@Nullable OS_Element best = lrl.chooseBest(null);
			while (best instanceof AliasStatement) {
				best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, dt2);
			}
			if (best == null) {
				if (tn.asSimpleString().equals("Any"))
					/*return*/ aR.resolved = new OS_AnyType(); // TODO not a class
				throw new ResolveError(tn1, lrl);
			}

			if (best instanceof ClassContext.OS_TypeNameElement) {
				/*return*/
				aR.resolved = new OS_GenericTypeNameType((ClassContext.OS_TypeNameElement) best); // TODO not a class
			} else
				aR.resolved = ((ClassStatement) best).getOS_Type();
			break;
		}
		case FUNCTION:
		case GENERIC:
			throw new NotImplementedException();
		case TYPE_OF:
			{
				final TypeOfTypeName type_of = (TypeOfTypeName) tn1;
				final Qualident q = type_of.typeOf();
				if (q.parts().size() == 1 && q.parts().get(0).equals("self")) {
					assert type_of.getContext() instanceof ClassContext;
					aR.resolved = ((ClassContext) type_of.getContext()).getCarrier().getOS_Type();
				}
				int y=2;

			}
//				throw new NotImplementedException();
			break;
		default:
			throw new IllegalStateException("414 Unexpected value: " + tn1.kindOfType());
		}
	}

	private static void resolve_built_in(final OS_Module module, final @NotNull OS_Type type, final DeduceTypes2 dt2, final @NotNull GenType aR) throws ResolveError {
		switch (type.getBType()) {
		case SystemInteger:
		{
			@NotNull String typeName = type.getBType().name();
			assert typeName.equals("SystemInteger");
			OS_Module prelude = module.prelude;
			if (prelude == null) // README Assume `module' IS prelude
				prelude = module;
			final LookupResultList lrl = prelude.getContext().lookup(typeName);
			@Nullable OS_Element best = lrl.chooseBest(null);
			while (!(best instanceof ClassStatement)) {
				if (best instanceof AliasStatement) {
					best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, dt2);
				} else if (OS_Type.isConcreteType(best)) {
					throw new NotImplementedException();
				} else
					throw new NotImplementedException();
			}
			if (best == null) {
				throw new ResolveError(IdentExpression.forString(typeName), lrl);
			}
			aR.resolved = ((ClassStatement) best).getOS_Type();
			break;
		}
		case String_:
		{
			@NotNull String typeName = type.getBType().name();
			assert typeName.equals("String_");
			OS_Module prelude = module.prelude;
			if (prelude == null) // README Assume `module' IS prelude
				prelude = module;
			final LookupResultList lrl = prelude.getContext().lookup("ConstString"); // TODO not sure about String
			@Nullable OS_Element best = lrl.chooseBest(null);
			while (!(best instanceof ClassStatement)) {
				if (best instanceof AliasStatement) {
					best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, dt2);
				} else if (OS_Type.isConcreteType(best)) {
					throw new NotImplementedException();
				} else
					throw new NotImplementedException();
			}
			if (best == null) {
				throw new ResolveError(IdentExpression.forString(typeName), lrl);
			}
			aR.resolved = (((ClassStatement) best).getOS_Type());
			break;
		}
		case SystemCharacter:
		{
			@NotNull String typeName = type.getBType().name();
			assert typeName.equals("SystemCharacter");
			OS_Module prelude = module.prelude;
			if (prelude == null) // README Assume `module' IS prelude
				prelude = module;
			final LookupResultList lrl = prelude.getContext().lookup("SystemCharacter");
			@Nullable OS_Element best = lrl.chooseBest(null);
			while (!(best instanceof ClassStatement)) {
				if (best instanceof AliasStatement) {
					best = DeduceLookupUtils._resolveAlias2((AliasStatement) best, dt2);
				} else if (OS_Type.isConcreteType(best)) {
					throw new NotImplementedException();
				} else
					throw new NotImplementedException();
			}
			if (best == null) {
				throw new ResolveError(IdentExpression.forString(typeName), lrl);
			}
			aR.resolved = ((ClassStatement) best).getOS_Type();
			break;
		}
		case Boolean:
		{
			OS_Module prelude = module.prelude;
			if (prelude == null) // README Assume `module' IS prelude
				prelude = module;
			final LookupResultList lrl = prelude.getContext().lookup("Boolean");
			final @Nullable OS_Element best = lrl.chooseBest(null);
			aR.resolved = ((ClassStatement) best).getOS_Type(); // TODO might change to Type
			break;
		}
		default:
			throw new IllegalStateException("531 Unexpected value: " + type.getBType());
		}
		return;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
