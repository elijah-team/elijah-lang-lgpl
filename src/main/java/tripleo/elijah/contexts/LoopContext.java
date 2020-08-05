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

import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.Loop;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:40:43 PM
 */
public class LoopContext extends Context {

	private final Loop carrier;

	public LoopContext(Loop loop) {
		carrier = loop;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		// TODO implement me
//		throw new NotImplementedException();
//		final LookupResultList Result = new LookupResultList();

//		for (FunctionItem item: carrier.getItems()) {
//			if (!(item instanceof ClassStatement) &&
//				!(item instanceof NamespaceStatement) &&
//				!(item instanceof VariableSequence)
//			) continue;
//			if (item instanceof VariableSequence) {
//				System.out.println("101 "+item);
//				for (VariableStatement vs : ((VariableSequence) item).items()) {
//					if (vs.getName().equals(name))
//						Result.add(name, level, vs);
//				}
//			} else if (((OS_Element2)item).name() != null) {
//				if (((OS_Element2)item).name().equals(name)) {
//					Result.add(name, level, item);
//				}
//			}
//		}
		if (name.equals(carrier.getIterName())) { // reversed to prevent NPEs
			String iterName = carrier.getIterName();
			IdentExpression ie = new IdentExpression(Helpers.makeToken(iterName));
			Result.add(name, level, ie); // TODO just made ie
		}

		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result); // TODO test this
		return Result;
		
	}

}

//
//
//
