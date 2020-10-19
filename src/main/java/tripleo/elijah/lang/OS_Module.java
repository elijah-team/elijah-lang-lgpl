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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class OS_Module implements OS_Element, OS_Container {

	private final Stack<Qualident> packageNames_q = new Stack<Qualident>();
	public List<ModuleItem> items = new ArrayList<ModuleItem>();
	public Attached _a = new Attached();
	public OS_Module prelude;

	public Compilation parent;
	private final List<IndexingItem> indexingItems = new ArrayList<IndexingItem>();
	private String _fileName;
	public List<ClassStatement> entryPoints = new ArrayList<ClassStatement>();

//	public void addIndexingItem(Token i1, IExpression c1) {
//		indexingItems.add(new IndexingItem(i1, c1));
//	}

	public OS_Element findClass(final String className) {
		for (final ModuleItem item : items) {
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

	public String getFileName() {
		return _fileName;
	}

	public void setFileName(final String fileName) {
		this._fileName = fileName;
	}

	public Collection<ModuleItem> getItems() {
		return items;
	}

	public boolean hasClass(final String className) {
		for (final ModuleItem item : items) {
			if (item instanceof ClassStatement) {
				if (((ClassStatement) item).getName().equals(className))
					return true;
			}
		}
		return false;
	}

	@Override // OS_Container
	public List<OS_Element2> items() {
		final Collection<ModuleItem> c = Collections2.filter(getItems(), new Predicate<ModuleItem>() {
			@Override
			public boolean apply(@org.checkerframework.checker.nullness.qual.Nullable final ModuleItem input) {
				final boolean b = input instanceof OS_Element2;
				return b;
			}
		});
		final ArrayList<OS_Element2> a = new ArrayList<OS_Element2>();
		for (final ModuleItem moduleItem : c) {
			a.add((OS_Element2) moduleItem);
		}
		return a;
	}

	@Override
	public void add(final OS_Element anElement) {
		if (!(anElement instanceof ModuleItem)) {
			parent.eee.info(String.format(
					"[Module#add] adding %s to OS_Module", anElement.getClass().getName()));
		}
		items.add((ModuleItem) anElement);
	}

//	public void modify_namespace(Qualident q, NamespaceModify aModification) { // TODO aModification is unused
////		NotImplementedException.raise();
//		System.err.println("[OS_Module#modify_namespace] " + q + " " + aModification);
//		//
//		// DON'T MODIFY  NAMETABLE
//		//
///*
//		getContext().add(null, q);
//*/
//	}
//
//	public void modify_namespace(ImportStatement imp, Qualident q, NamespaceModify aModification) { // TODO aModification is unused
////		NotImplementedException.raise();
//		System.err.println("[OS_Module#modify_namespace] " + imp + " " + q + " " + aModification);
///*
//		getContext().add(imp, q); // TODO prolly wrong; do a second pass later to add definition...?
//*/
//	}

	@Override
	public void visitGen(final ICodeGen visit) {
		visit.addModule(this);
	}

	/**
	 * A module has no parent which is an element (not even a package - this is not Java).<br>
	 * If you want the Compilation use the member {@link #parent}
	 *
	 * @return null
	 */
	/**
	 * @ ensures \result == null
	 */
	@Override
	public OS_Element getParent() {
		return null;
	}

	public void setParent(@NotNull final Compilation parent) {
		this.parent = parent;
	}

	@Override
	public Context getContext() {
		return _a._context;
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
		return parent.makePackage(packageNames_q.peek());//new OS_Package(packageNames_q.peek(), packageNames_q.size()); // TODO
	}

	public void pushPackageName(final Qualident xyz) {
		packageNames_q.push(xyz);
	}

	/**
	 * Get a class by name. Must not be qualified. Wont return a {@link NamespaceStatement}
	 * Same as {@link #findClass(String)}
	 *
	 * @param name the class we are looking for
	 * @return either the class or null
	 */
	public @Nullable ClassStatement getClassByName(final String name) {
		for (final ModuleItem item : items) {
			if (item instanceof ClassStatement)
				if (((ClassStatement) item).getName().equals(name))
					return (ClassStatement) item;
		}
		return null;
	}


	public void postConstruct() {
		for (final ModuleItem anElement : items) {
			if (anElement instanceof OS_Element2) {
				final String element_name = ((OS_Element2) anElement).name();
				// TODO make and check a nametable, will fail for imports
				if (element_name == null) {
//					throw new IllegalArgumentException("element2 with null name");
					System.err.println(String.format("*** OS_Element2 (%s) with null name", anElement));
				} else {
					for (final ModuleItem item : items) { // TODO Use Multimap
						if (item instanceof OS_Element2 && item != anElement)
							if (element_name.equals(((OS_Element2) item).name())) {
								parent.eee.reportWarning(String.format(
										"[Module#add] Already has a member by the name of %s",
										element_name));
								return;
							}
					}
				}
			}
			//
			// FIND ALL ENTRY POINTS (should only be one per module)
			//
			for (final ModuleItem item : items) {
				if (item instanceof ClassStatement) {
					if (((ClassStatement) item).getPackageName() == OS_Package.default_package) {
						entryPoints.add((ClassStatement) item);
//						break; // allow for "extend" class
					}
				}
			}
		}

	}

	public void setContext(final ModuleContext mctx) {
		_a.setContext(mctx);
	}

	@Override
	public void addDocString(final Token s1) {
		throw new NotImplementedException();
	}
}

//
//
//
