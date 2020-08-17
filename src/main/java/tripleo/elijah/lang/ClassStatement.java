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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import tripleo.elijah.Documentable;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
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
			_packageName.addElement(this);
			module.add(this);
		} else if (aElement instanceof OS_Container) {
			((OS_Container) aElement).add(this);
		} else {
			throw new IllegalStateException(String.format("Cant add ClassStatement to %s", aElement));
		}
	}

//	/**
//	 * A simple add-to-list operation
//	 * @param aDef
//	 */
//	public void add(ClassItem aDef) {
//		items.add(aDef);
//	}

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
		if (clsName == null)
			return "";
		return clsName.getText();
	}
	
	public List<ClassItem> getItems() {
		return items;
	}
	
	public List<String> getDocstrings() {
		return mDocs;
	}
	
	@Override public OS_Element getParent() {
		return parent;
	}
	
	public void setName(IdentExpression aText) {
		clsName = aText;
	}

//	@Override
	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

	public IdentExpression clsName;

	private final List<ClassItem> items=new ArrayList<ClassItem>();
	private final List<String> mDocs = new ArrayList<String>();
//	private final List<IExpression> mExprs = new ArrayList<IExpression>();

	public /*final*/ OS_Element parent;

	public Attached _a = new Attached();

	@Override // OS_Container
	public List<OS_Element2> items() {
		Collection<ClassItem> c = Collections2.filter(getItems(), new Predicate<ClassItem>() {
			@Override
			public boolean apply(@Nullable ClassItem input) {
				final boolean b = input instanceof OS_Element2;
//				System.out.println(String.format("%s %b", input, b));
				return b;
			}
		});
		ArrayList<OS_Element2> a = new ArrayList<OS_Element2>();
		for (ClassItem functionItem : c) {
			a.add((OS_Element2) functionItem);
		}
		return a;
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

	@Override
	public String toString() {
		return String.format("<Class %d %s %s>", _a.getCode(), getPackageName()._name, getName());
	}

	public ConstructorDef addCtor(IdentExpression aConstructorName) {
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

	public void setContext(ClassContext ctx) {
		_a.setContext(ctx);
	}

	public void postConstruct() { // TODO
	}

	List<AnnotationClause> annotations = null;

	public void addAnnotation(AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}
}

//
//
//
