/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {

	private final FunctionDef carrier;

	public FunctionContext(FunctionDef functionDef) {
		carrier = functionDef;
	}

}
