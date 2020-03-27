package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.DefFunctionDef;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:24:44 PM
 */
public class DefFunctionContext extends Context {

	private final DefFunctionDef carrier;

	public DefFunctionContext(DefFunctionDef functionDef) {
		carrier = functionDef;
	}

}
