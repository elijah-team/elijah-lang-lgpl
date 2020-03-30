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

public abstract class Context {

	private OS_Container attached;

	public Context() {
	}
	
	public Context(OS_Container attached) {
		this.attached = attached;
	}
	
	public LookupResultList lookup(String name) {
		return lookup(name, 0);
	}
	
	public abstract LookupResultList lookup(String name, int level); // {
//		final LookupResultList Result = new LookupResultList();
//
//		/*
//		for (OS_Element2 i : attached.items()) {
////			if (i.name().equals(name)) {
////				Result.add (i, i.sig, level);
////			}
//		}
//		*/
//		if (members.containsKey(name))
//		{
//			OS_Element element = members.get(name);
//			Result.add(name, level, element);
//			element.getParent().getContext().lookup(name, level);
//		}
//		
////		lookup(name, attached, parent, Result ,level+1);
//		return Result;
//	}
	
	void lookup(String name, OS_Container attached1, OS_Element2 parent, LookupResultList Result, int level) {
		throw new NotImplementedException();
	}

	public void add(OS_Element element, String name) {
		members.put(name, element);
	}
	
//	class TypedElement {
//		OS_Element element;
//		OS_Type type;
//	}
	
	Map<String, OS_Element> members = new HashMap<String, OS_Element>();
	private NameTable nameTable = new NameTable();

	public void add(OS_Element element, String name, OS_Type dtype) {
//		element.setType(dtype);
		members.put(name, element);
	}

	public NameTable nameTable() {
		return this.nameTable ;
	}
}

//
//
//
