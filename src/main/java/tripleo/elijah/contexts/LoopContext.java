/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.Loop;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element2;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;

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
		// TODO implement me
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
//		if (carrier.getParent() != null)
//			carrier.getParent().getContext().lookup(name, level+1);
		return Result;
		
	}

}
