/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class OS_Module implements OS_Element, OS_Container {

	private List<IndexingItem> indexingItems=new ArrayList<IndexingItem>();
	public List<ModuleItem> items=new ArrayList<ModuleItem>();
	private final Stack<Qualident> packageNames_q = new Stack<Qualident>();
	public Attached _a = new Attached(new ModuleContext(this));

//	public String moduleName="default";

	public OS_Module prelude;
	private String _fileName;
	public Compilation parent;

	public void setParent(@NotNull Compilation parent) {
		this.parent = parent;
	}

//	public void add(ModuleItem aItem) {
////		if (aItem instanceof ClassStatement)
////			((ClassStatement)aItem).setPackageName(packageNames_q.peek());
//		items.add(aItem);
//	}
	
	@Override
	public void add(OS_Element anElement) {
		if (!(anElement instanceof ModuleItem)) {
			parent.eee.info(String.format(
					"[Module#add] adding %s to OS_Module", anElement.getClass().getName()));
		}
		if (anElement instanceof OS_Element2) {
			final String element_name = ((OS_Element2) anElement).name();
			// TODO make and check a nametable, will fail for imports
			for (ModuleItem item : items) {
				if (item instanceof OS_Element2)
					if (((OS_Element2) item).name().equals(element_name)) {
						parent.eee.reportError(String.format(
								"[Module#add] Already has a member by the name of ",
								element_name));
						return;
					}
			}
		}
		items.add((ModuleItem) anElement);
	}

	public void addIndexingItem(Token i1, IExpression c1) {
		indexingItems.add(new IndexingItem(i1, c1));
	}

	public OS_Element findClass(String className) {
		for (ModuleItem item : items) {
			if (item instanceof ClassStatement) {
				if (((ClassStatement) item).getName().equals(className))
					return item;
			}
		}
		return null;
	}

	public void finish() {
//		parent.put_module(_fileName, this);
	}

	@Override
	public Context getContext() {
		return _a._context;
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public Collection<ModuleItem> getItems() {
		return items;
	}

	/**
	 * A module has no parent which is an element (not even a package - this is not Java).<br>
	 * If you want the Compilation use the member {@link #parent}
	 *
	 * @return null
	 */
	/**@ ensures \result == null */
	@Override
	public OS_Element getParent() {
		return null;
	}

	public boolean hasClass(String className) {
		for (ModuleItem item : items) {
			if (item instanceof ClassStatement) {
				if (((ClassStatement) item).getName().equals(className))
					return true;
			}
		}
		return false;
	}

	@Override // OS_Container
	public List<OS_Element2> items() {
		throw new NotImplementedException();
//		return null;
	}

	public void modify_namespace(Qualident q, NamespaceModify import1) {
		NotImplementedException.raise();
//		getContext().add(null,  q.toString());
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Module print_osi");
//		if (packageName != null) {
//			tos.put_string("package ");
//			tos.put_string_ln(packageName);
//			tos.put_string_ln("");
//		}
		tos.put_string_ln("//");
		synchronized (items) {
			for (ModuleItem element : items)
				element.print_osi(tos);

		}
	}

	/**
	 * The last package name declared in the source file
	 *
	 * @return null or a OS_Package instance
	 */
	// TODO make class OS_Package
	public OS_Package pullPackageName() {
		if (packageNames_q.empty())
			return OS_Package.default_package;
		return new OS_Package(packageNames_q.peek(), packageNames_q.size()); // TODO
	}

	public void pushPackageName(Qualident xyz) {
		packageNames_q.push(xyz);
	}

	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		visit.addModule(this);
	}

	/**
	 * Get a class by name. Must not be qualified. Wont return a {@link NamespaceStatement}
	 * Same as {@link #findClass(String)}
	 *
	 * @param name the class we are looking for
	 * @return either the class or null
	 */
	public @Nullable ClassStatement getClassByName(String name) {
		for (ModuleItem item : items) {
			if (item instanceof ClassStatement)
				if (((ClassStatement) item).getName().equals(name))
					return (ClassStatement) item;
		}
		return null;
	}
}

//
//
//
