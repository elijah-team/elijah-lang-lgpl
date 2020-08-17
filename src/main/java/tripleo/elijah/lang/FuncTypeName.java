package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/16/20 2:16 AM
 */
public class FuncTypeName implements TypeName {
	private final Context _ctx;
	private TypeModifiers _modifiers;
	private TypeNameList _arglist = null/*new TypeNameList()*/;
	private TypeName _returnValue = null /*new RegularTypeName()*/; // TODO warning

	public FuncTypeName(Context cur) {
		_ctx = cur;
	}

	public void argList(TypeNameList tnl) {
		_arglist = tnl;
	}

	@Override
	public void type(TypeModifiers typeModifiers) {
		_modifiers = typeModifiers;
	}

	@Override
	public Type kindOfType() {
		return Type.FUNCTION;
	}

	public void returnValue(TypeName rtn) {
		_returnValue = rtn;
	}

	@Override
	public boolean isNull() {
		return _arglist == null && _returnValue == null;
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
