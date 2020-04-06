/**
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.lang.RegularTypeName;
import tripleo.elijah.lang.TypeModifiers;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang.TypeNameList;

/**
 * @author olu
 *
 * Created 	Mar 30, 2020 at 7:41:52 AM
 */
public class FuncExpr {

	private final TypeNameList argList = new TypeNameList();
	private final RegularTypeName typeName = new RegularTypeName();

	public void type(TypeModifiers function) {
		// TODO Auto-generated method stub
		
	}

	public TypeNameList argList() {
		return argList;
	}

	public TypeName returnValue() {
		// TODO Auto-generated method stub
		return typeName;
	}

}
