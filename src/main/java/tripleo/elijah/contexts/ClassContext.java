/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.*;

import java.util.List;


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

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one) {
		alreadySearched.add(carrier.getContext());
		for (ClassItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof FunctionDef) &&
				!(item instanceof VariableSequence) &&
				!(item instanceof AliasStatement)
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
			}
		}
		for (TypeName tn : carrier.classInheritance().tns) {
//			System.out.println("1001 "+tn);
			LookupResultList tnl = carrier.getParent().getContext().lookup(tn.getName()); // TODO why getParent here?
//			System.out.println("1002 "+tnl.results());
			OS_Element best = tnl.chooseBest(null);
			if (best != null) {
				LookupResultList lrl2 = best.getContext().lookup(name);
				OS_Element best2 = lrl2.chooseBest(null);
				if (best2 != null)
					Result.add(name, level, best2);
			}
//			System.out.println("1003 "+name+" "+Result.results());
		}
		// search inherited classes, tho this might be being done above
//		for (Context context : carrier.classInheritance().) {
//
//		}
		if (carrier.getParent() != null) {
			final Context context = carrier.getParent().getContext();
			if (!alreadySearched.contains(context) || !one)
				return context.lookup(name, level + 1, Result, alreadySearched, false);
		}
		return Result;
		
	}
}
