package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.DefFunctionDef;
import tripleo.elijah.lang.LookupResultList;

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

	/**
	 * By definition should have nothing to lookup
	 * 
	 * @param name
	 * @param level
	 * @return
	 */
	public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		return Result;
		
	}

}
