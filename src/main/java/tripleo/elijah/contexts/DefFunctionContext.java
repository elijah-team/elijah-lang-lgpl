package tripleo.elijah.contexts;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.DefFunctionDef;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element2;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;

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
	public LookupResultList lookup(String name, int level) {
		final LookupResultList Result = new LookupResultList();
		return Result;
		
	}

}
