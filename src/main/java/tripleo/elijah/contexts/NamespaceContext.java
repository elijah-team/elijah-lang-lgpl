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
 * Created 	Mar 29, 2020 at 8:59:42 PM
 */
public class NamespaceContext extends Context {

	public NamespaceStatement carrier;

	public NamespaceContext(NamespaceStatement namespaceStatement) {
		carrier = namespaceStatement;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
		for (ClassItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
					    !(item instanceof NamespaceStatement) &&
					    !(item instanceof VariableSequence)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item);
				}
			}
			if (item instanceof VariableSequence) {
//				System.out.println("[NamespaceContext#lookup] VariableSequence "+item);
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs);
				}
			}
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result);
		return Result;

	}

}
