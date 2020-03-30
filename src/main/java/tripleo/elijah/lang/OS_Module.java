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
/* Created on Aug 30, 2005 8:21:52 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class OS_Module implements OS_Element {
	
	private String _fileName;
	
	public List<ModuleItem> items=new ArrayList<ModuleItem>();
	
	public List<IndexingItem> indexingItems=new ArrayList<IndexingItem>();

	//	public String packageName;
	public String moduleName="default";

	//	public void pushPackageName(String xy) {
//		// TODO Auto-generated method stub
//		packageNames.push(xy);
//	}
//	
//	Stack<String> packageNames = new Stack<String>();
	private Stack<Qualident> packageNames_q = new Stack<Qualident>();
	public Attached _a = new Attached(new ModuleContext(this));
	
	public void add(ModuleItem aItem) {
//		if (aItem instanceof ClassStatement)
//			((ClassStatement)aItem).setPackageName(packageNames_q.peek());
		items.add(aItem);
	}
	
	public void addIndexingItem(Token i1, IExpression c1) {
		indexingItems.add(new IndexingItem(i1, c1));
	}

	public void finish(TabbedOutputStream tos) throws IOException {
		tos.close();
	}

	public String getFileName() {
		return _fileName;
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

	@Override
	public OS_Element getParent() {
		// TODO return COMP??
		return null;
	}

	@Override
	public Context getContext() {
		return _a._context;
	}

	public void modify_namespace(Qualident q, NamespaceModify import1) {
//		NotImplementedException.raise();
		getContext().add(null,  q.toString());
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

	public boolean hasClass(String className) {
		for (ModuleItem item : items) {
			if (item instanceof ClassStatement) {
				if (((ClassStatement) item).getName().equals(className))
					return true;
			}
		}
		return false;
	}

	public Collection<ModuleItem> getItems() {
		return items;
	}
}

//
//
//
