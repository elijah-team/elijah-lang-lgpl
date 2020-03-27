/**
 * 
 */
package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo(sb)
 *
 * Created 	Dec 9, 2019 at 3:30:44 PM
 */
public class LookupResultList {

	private List<LookupResult> results = new ArrayList<LookupResult>();

	public void add(String name, int level, OS_Element element) {
		// TODO Auto-generated method stub
		results .add(new LookupResult(name, element, level));
	}

}
