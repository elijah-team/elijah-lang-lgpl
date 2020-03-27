/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.util.HashMap;
import java.util.Map;

import tripleo.elijah.util.NotImplementedException;

public class Context {

	private OS_Container attached;

	public Context() {
	}
	
	LookupResultList lookup(String name) {
		return lookup(name, 0);
	}
	
	LookupResultList lookup(String name, int level) {
		final LookupResultList Result = new LookupResultList();

		/*
		for (OS_Element2 i : attached.items()) {
//			if (i.name().equals(name)) {
//				Result.add (i, i.sig, level);
//			}
		}
		*/
		OS_Element element = members.get(name);
		Result.add(name, level, element);
		element.getParent().getContext().lookup(name, level);
		
//		lookup(name, attached, parent, Result ,level+1);
		return Result;
	}
	
	void lookup(String name, OS_Container attached, OS_Element2 parent, LookupResultList Result, int level) {
		throw new NotImplementedException();
	}

	public void add(OS_Element element, String name) {
//		NotImplementedException.raise();
		members.put(name, element);
	}
	
	Map<String, OS_Element> members = new HashMap<String, OS_Element>();
}

//
//
//
