/**
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Tripleo(sb)
 *
 * Created 	Dec 9, 2019 at 3:30:44 PM
 */
public class LookupResultList {

	private List<LookupResult> results = new ArrayList<LookupResult>();

	public void add(String name, int level, OS_Element element) {
		// TODO Auto-generated method stub
		results.add(new LookupResult(name, element, level));
	}

	public List<LookupResult> results() {
		return results;
	}

	public OS_Element chooseBest(List<Predicate> l) {
		if (l != null) throw new NotImplementedException();
		//
		if (results().size() == 1)
			return results().get(0).getElement();
		else
			throw new NotImplementedException();
	}
}
