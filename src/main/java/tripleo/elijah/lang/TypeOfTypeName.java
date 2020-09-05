package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/16/20 7:42 AM
 */
public class TypeOfTypeName implements TypeName {
	private final Context _ctx;
	private Qualident _typeOf;
	private TypeModifiers modifiers;

	public TypeOfTypeName(Context cur) {
		_ctx=cur;
	}

	public void typeOf(Qualident xy) {
		_typeOf=xy;
	}

	public void set(TypeModifiers modifiers_) {
		modifiers = modifiers_;
	}

	@Override
	public Type kindOfType() {
		return Type.TYPE_OF;
	}

	@Override
	public void setContext(Context context) {
		throw new NotImplementedException();
	}

	@Override
	public Context getContext() {
		return _ctx;
	}
}
