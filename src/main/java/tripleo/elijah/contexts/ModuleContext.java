/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.ModuleItem;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Module;

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
			) continue;
//			if (item instanceof VariableSequence) {
//				System.out.println("101 "+item);
//				for (VariableStatement vs : ((VariableSequence) item).items()) {
//					if (vs.getName().equals(name))
//						Result.add(name, level, vs);
//				}
//			} else 
//			if (item.getName() != null) {
//				if (item.getName().equals(name)) {
//					Result.add(name, level, item);
//				}
//			}
			if (item instanceof ClassStatement) {
				if (((ClassStatement) item).getName().equals(name)) {
					Result.add(name, level, item);
				}
			} else if (item instanceof NamespaceStatement) {
				if (((NamespaceStatement) item).getName().equals(name)) {
					Result.add(name, level, item);
				}
			}
			
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result);
		return Result;
		
	}

}
