package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/16/20 7:42 AM
 */
public class TypeOfTypeName implements TypeName {
	private final Context _ctx;
	private Qualident _typeOf;
	private TypeModifiers modifiers;

	public TypeOfTypeName(final Context cur) {
		_ctx=cur;
	}

	public void typeOf(final Qualident xy) {
		_typeOf=xy;
	}

	public void set(final TypeModifiers modifiers_) {
		modifiers = modifiers_;
	}

	@Override
	public Type kindOfType() {
		return Type.TYPE_OF;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public void setContext(final Context context) {
		throw new NotImplementedException();
	}

	@Override
	public Context getContext() {
		return _ctx;
	}

	// TODO doesn't work with dotted Qualidents
	public TypeName resolve(Context ctx) {
//		TypeName tn = null;
//		System.out.println(_typeOf.toString());
		LookupResultList lrl = ctx.lookup(_typeOf.toString()); // TODO what about multi-level qualidents
		OS_Element best = lrl.chooseBest(null);
		if (best instanceof VariableStatement)
			return ((VariableStatement) best).typeName();
		return null;
	}
}
