/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {

	private final FunctionDef carrier;

	public FunctionContext(FunctionDef functionDef) {
		carrier = functionDef;
	}

	LookupResultList lookup(String name, int level) {
		final LookupResultList Result = new LookupResultList();
		for (FunctionItem item: carrier.getItems()) {
			if (item.name() != null) {
				if (item.name().equals(name)) {
					Result.add(name, level, item);
				}
			}
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1);
		return Result;
		
	}

}
