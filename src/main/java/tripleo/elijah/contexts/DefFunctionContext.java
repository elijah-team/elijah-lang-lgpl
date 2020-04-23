/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
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
