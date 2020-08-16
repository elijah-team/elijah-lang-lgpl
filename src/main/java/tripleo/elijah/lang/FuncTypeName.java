package tripleo.elijah.lang;

/**
 * Created 8/16/20 2:16 AM
 */
public class FuncTypeName implements TypeName {
	private TypeModifiers _modifiers;
	private TypeNameList _arglist = new TypeNameList();
	private NormalTypeName _returnValue = new RegularTypeName(); // TODO warning

	public TypeNameList argList() {
		return _arglist;
	}

	@Override
	public void type(TypeModifiers typeModifiers) {
		_modifiers = typeModifiers;
	}

	@Override
	public Type kindOfType() {
		return Type.FUNCTION;
	}

	public NormalTypeName returnValue() {
		return _returnValue;
	}
}
