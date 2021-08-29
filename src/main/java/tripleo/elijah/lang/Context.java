/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.contexts.ModuleContext;

import java.util.ArrayList;
import java.util.List;

// TODO is this right, or should be interface??
public abstract class Context {

//	private OS_Container attached;

	public Context() {
	}
	
//	public Context(OS_Container attached) {
//		this.attached = attached;
//	}
	
	public LookupResultList lookup(@NotNull final String name) {
		final LookupResultList Result = new LookupResultList();
		return lookup(name, 0, Result, new ArrayList<Context>(), false);
	}
	
	public abstract LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one);

	public abstract Context getParent();

//	@Deprecated public void add(OS_Element element, String name) {
//		add(element, new IdentExpression(Helpers.makeToken(name)));
//	}
//
//	@Deprecated public void add(OS_Element element, String name, OS_Type dtype) {
//		add(element, new IdentExpression(Helpers.makeToken(name)), dtype);
//	}
//
//	public void add(OS_Element element, IExpression name) {
//		System.out.println(String.format("104 Context.add: %s %s %s", this, element, name));
//		members.put(name, element);
//	}
	
//
//	Map<IExpression, OS_Element> members = new HashMap<IExpression, OS_Element>();
//	private NameTable nameTable = new NameTable();
//
//	public void add(OS_Element element, IExpression name, OS_Type dtype) {
//		System.out.println(String.format("105 Context.add: %s %s %s %s", this, element, name, dtype));
////		element.setType(dtype);
//		members.put(name, element);
//	}
//
//	public NameTable nameTable() {
//		return this.nameTable ;
//	}

	public @NotNull OS_Module module() {
		Context ctx = this;//getParent();
		while (!(ctx instanceof ModuleContext))
			ctx = ctx.getParent();
		return ((ModuleContext) ctx).getCarrier();
	}

	public @NotNull Compilation compilation() {
		OS_Module module = module();
		return module.parent;
	}
}

//
//
//
