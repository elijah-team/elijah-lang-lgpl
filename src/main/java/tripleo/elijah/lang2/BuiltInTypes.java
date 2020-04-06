/**
 * 
 */
package tripleo.elijah.lang2;

/**
 * @author Tripleo
 *
 * Created 	Mar 27, 2020 at 2:08:59 AM
 */
public enum BuiltInTypes {
	SystemInteger(80);

	final int _code;
	
	BuiltInTypes(int aCode) {
		_code = aCode;
	}
	
	public int getCode() {
		return _code;
	}

}
