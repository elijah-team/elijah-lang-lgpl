/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.ClassItem;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element2;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;


/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:04:02 AM
 */
public class ClassContext extends Context {

	private final ClassStatement carrier;

	public ClassContext(ClassStatement classStatement) {
		carrier = classStatement;
//		super(classStatement);
	}

	public void add(FunctionDef fd, String funName) {
		// TODO Auto-generated method stub
		
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		for (ClassItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof FunctionDef) &&
				!(item instanceof VariableSequence)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item);
				}
			}
			if (item instanceof VariableSequence) {
				System.out.println("102 "+item);
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs);
				}
			} /*else if (item instanceof FunctionDef) {
				if (((FunctionDef)item).funName.equals(name))
					Result.add(name, level, item);
				
			} else if (((OS_Element2)item).name() != null) {
				if (((OS_Element2)item).name().equals(name)) {
					Result.add(name, level, item);
				}
			}*/
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result);
		return Result;
		
	}
}
