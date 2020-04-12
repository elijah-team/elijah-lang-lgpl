/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import antlr.Token;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;
import tripleo.elijah.Documentable;

/**
 * Represents a "class"
 * 
 * items -> ClassItems
 * docstrings 
 * variables
 * 
 */
public class ClassStatement extends ProgramClosure implements ClassItem, ModuleItem, FunctionItem, OS_Element, Documentable {
	
	private OS_Package _packageName;
	
//	/**
//	 * For XMLBeans. Must use setParent.
//	 */
//	public ClassStatement() {
//	}
	
	public ClassStatement(OS_Element aElement) {
		parent = aElement; // setParent
		if (aElement instanceof  OS_Module) {
			final OS_Module module = (OS_Module) aElement;
			//
			this.setPackageName(module.pullPackageName());
			module.add(this);
		} else if (aElement instanceof OS_Container) {
			((OS_Container) aElement).add(this);
		} else {
			throw new IllegalStateException(String.format("Cant add ClassStatement to %s", aElement));
		}
	}

	/**
	 * A simple add-to-list operation
	 * @param aDef
	 */
	public void add(ClassItem aDef) {
		items.add (aDef);
	}

	@Override
	public void addDocString(Token aText) {
		mDocs.add(aText.getText());
	}

	public ClassInheritance classInheritance() {
		// TODO once
		return new ClassInheritance(this);
	}

	public FunctionDef funcDef() {
		return new FunctionDef(this);
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		Helpers.printXML(this, tos);
		//
		System.out.println("Klass print_osi");
		tos.incr_tabs();
		tos.put_string_ln(String.format("Class (%s) {", clsName));
		tos.put_string_ln("//");
		for (ClassItem item : items) {
			item.print_osi(tos);
		}
		tos.dec_tabs();
		tos.put_string_ln(String.format("} // class %s ", clsName));
		tos.flush();
	}
	
	public String getName() {
		return clsName;
	}
	
	public List<ClassItem> getItems() {
		return items;
	}
	
	public ArrayList<String> getDocstrings() {
		return mDocs;
	}
	
	public OS_Element getParent() {
		return parent;
	}
	
	public void setName(@NotNull Token aText) {
		clsName=aText.getText();
	}

//	@Override
	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

	public String clsName;

	private final List<ClassItem> items=new ArrayList<ClassItem>();
	private final ArrayList<String> mDocs = new ArrayList<String>();
	private final ArrayList<IExpression> mExprs = new ArrayList<IExpression>();

	public /*final*/ OS_Element parent;

	public Attached _a = new Attached(new ClassContext(this));

	public synchronized Collection<ClassItem> items() {
		return items;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		visit.addClass(this);
	}
	
	public void setPackageName(OS_Package aPackageName) {
		_packageName = aPackageName;
	}
	
	public OS_Package getPackageName() {
		return _packageName;
	}
	
	public Scope addCtor(Token aConstructorKW) {
		NotImplementedException.raise();
		return null;
	}
	
	public Scope addDtor() {
		NotImplementedException.raise();
		return null;
	}
	
	public TypeAliasExpression typeAlias() {
		NotImplementedException.raise();
		return null;
	}
	
	public InvariantStatement invariantStatement() {
		NotImplementedException.raise();
		return null;
	}
	
	public ProgramClosure XXX() {
		return null;
	}

	@Override
	public Context getContext() {
		return _a._context;
	}

	
}

//
//
//
