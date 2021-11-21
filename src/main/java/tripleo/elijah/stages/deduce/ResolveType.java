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
import tripleo.elijah.lang.AliasStatement;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NormalTypeName;
import tripleo.elijah.lang.OS_AnyType;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_GenericTypeNameType;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang.TypeOfTypeName;
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
		R.typeName = type;

		switch (type.getType()) {

		case BUILT_IN:
		{
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
				R.resolved = ((ClassStatement) best).getOS_Type();
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
				R.resolved = ((ClassStatement) best).getOS_Type();
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
				R.resolved = ((ClassStatement) best).getOS_Type();
				break;
			}
			case Boolean:
			{
				OS_Module prelude = module.prelude;
				if (prelude == null) // README Assume `module' IS prelude
					prelude = module;
				final LookupResultList lrl = prelude.getContext().lookup("Boolean");
				final @Nullable OS_Element best = lrl.chooseBest(null);
				R.resolved = ((ClassStatement) best).getOS_Type(); // TODO might change to Type
				break;
			}
			default:
				throw new IllegalStateException("531 Unexpected value: " + type.getBType());
			}
			break;
		}
		case USER:
		{
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
						/*return*/R.resolved = new OS_AnyType(); // TODO not a class
					throw new ResolveError(tn1, lrl);
				}

				if (best instanceof ClassContext.OS_TypeNameElement) {
					/*return*/R.resolved = new OS_GenericTypeNameType((ClassContext.OS_TypeNameElement) best); // TODO not a class
				} else
					R.resolved = ((ClassStatement) best).getOS_Type();
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
						R.resolved = (((ClassContext) type_of.getContext()).getCarrier()).getOS_Type();
					}
					int y=2;

				}
//				throw new NotImplementedException();
				break;
			default:
				throw new IllegalStateException("414 Unexpected value: " + tn1.kindOfType());
			}
		}
		case USER_CLASS:
			break;
		case FUNCTION:
			break;
		default:
			throw new IllegalStateException("565 Unexpected value: " + type.getType());
		}

		return R;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
