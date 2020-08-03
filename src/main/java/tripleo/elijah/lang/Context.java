/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

// TODO is this right, or should be interface??
public abstract class Context {

	private OS_Container attached;

	public Context() {
	}
	
	public Context(OS_Container attached) {
		this.attached = attached;
	}
	
	public LookupResultList lookup(String name) {
		final LookupResultList Result = new LookupResultList();
		return lookup(name, 0, Result);
	}
	
	public abstract LookupResultList lookup(String name, int level, LookupResultList Result); // {
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

	@Deprecated public void add(OS_Element element, String name) {
		add(element, new IdentExpression(Helpers.makeToken(name)));
	}

	@Deprecated public void add(OS_Element element, String name, OS_Type dtype) {
		add(element, new IdentExpression(Helpers.makeToken(name)), dtype);
	}

	public void add(OS_Element element, IExpression name) {
		System.out.println(String.format("104 Context.add: %s %s %s", this, element, name));
		members.put(name, element);
	}
	
//	class TypedElement {
//		OS_Element element;
//		OS_Type type;
//	}
	
	Map<IExpression, OS_Element> members = new HashMap<IExpression, OS_Element>();
	private NameTable nameTable = new NameTable();

	public void add(OS_Element element, IExpression name, OS_Type dtype) {
		System.out.println(String.format("105 Context.add: %s %s %s %s", this, element, name, dtype));
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
