/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.Documentable;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a "class"
 * 
 * items -> ClassItems
 * docstrings 
 * variables
 * 
 */
public class ClassStatement extends ProgramClosure implements ClassItem, ModuleItem, FunctionItem, OS_Element, OS_Element2, Documentable, OS_Container {
	
	private OS_Package _packageName;
	private ClassTypes _type;

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
		items.add(aDef);
	}

	@Override
	public void addDocString(Token aText) {
		mDocs.add(aText.getText());
	}

	final ClassInheritance _inh = new ClassInheritance(this);

	public ClassInheritance classInheritance() {
		return _inh;
	}

	public FunctionDef funcDef() {
		return new FunctionDef(this);
	}
	
	public String getName() {
		return clsName;
	}
	
	public List<ClassItem> getItems() {
		return items;
	}
	
	public List<String> getDocstrings() {
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
	private final List<String> mDocs = new ArrayList<String>();
	private final List<IExpression> mExprs = new ArrayList<IExpression>();

	public /*final*/ OS_Element parent;

	public Attached _a = new Attached(new ClassContext(this));

	@Override // OS_Container
	public List<OS_Element2> items() {
		return null;
	}

	@Override // OS_Container
	public void add(OS_Element anElement) {
		if (anElement instanceof ClassItem)
			items.add((ClassItem) anElement);
		else
			throw new IllegalStateException(String.format("Cant add %s to ClassStatement", anElement));
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
	
	public ConstructorDef addCtor(Token aConstructorName) {
		return new ConstructorDef(aConstructorName, this);
	}
	
	public DestructorDef addDtor() {
		return new DestructorDef(this);
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

	@Override // OS_Element
	public Context getContext() {
		return _a._context;
	}


	@Override // OS_Element2
	public String name() {
		return getName();
	}

	public FunctionDef findFunction(String name) {
		for (ClassItem item : items) {
			if (item instanceof FunctionDef)
				if (((FunctionDef) item).name().equals(name))
					return (FunctionDef) item;
		}
		return null;
	}

	public void setType(ClassTypes aType) {
		_type = aType;
	}

	@Override
	public void addImportStatement(ImportStatement imp) {
		add(imp);
	}
}

//
//
//
