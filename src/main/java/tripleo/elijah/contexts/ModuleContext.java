/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.*;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:33:31 AM
 */
public class ModuleContext extends Context {

	private OS_Module carrier;

	public ModuleContext(OS_Module module) {
		this.carrier = module;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		for (ModuleItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) //&&
//				!(item instanceof VariableSequence)
			// TODO what about imports
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item);
				} else if (item instanceof ImportStatement) {
					System.err.println("ignoring "+item);
				}
			}
		}
		return carrier.prelude.getContext().lookup(name, level+1, Result);
	}

}
