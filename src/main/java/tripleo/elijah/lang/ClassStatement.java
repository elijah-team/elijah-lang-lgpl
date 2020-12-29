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
public class ClassStatement extends ProgramClosure implements ClassItem, ModuleItem, StatementItem, FunctionItem, OS_Element, OS_Element2, Documentable, OS_Container {
	
	private OS_Package _packageName;
	private ClassTypes _type;
	private List<AccessNotation> accesses = new ArrayList<AccessNotation>();

//	/**
//	 * For XMLBeans. Must use setParent.
//	 */
//	public ClassStatement() {
//	}

//	@Deprecated public ClassStatement(final OS_Element aElement) {
//		parent = aElement; // setParent
//		if (aElement instanceof  OS_Module) {
//			final OS_Module module = (OS_Module) aElement;
//			//
//			this.setPackageName(module.pullPackageName());
//			_packageName.addElement(this);
//			module.add(this);
//		} else if (aElement instanceof OS_Container) {
//			((OS_Container) aElement).add(this);
//		} else {
//			throw new IllegalStateException(String.format("Cant add ClassStatement to %s", aElement));
//		}
//	}

	public ClassStatement(final OS_Element aElement, final Context parentContext) {
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
		setContext(new ClassContext(parentContext, this));
	}

//	/**
//	 * A simple add-to-list operation
//	 * @param aDef
//	 */
//	public void add(ClassItem aDef) {
//		items.add(aDef);
//	}

	@Override
	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	ClassInheritance _inh = new ClassInheritance(this); // remove final for ClassBuilder

	public ClassInheritance classInheritance() {
		return _inh;
	}

	public FunctionDef funcDef() {
		return new FunctionDef(this, getContext());
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
	
	public void setName(final IdentExpression aText) {
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
		final Collection<ClassItem> c = Collections2.filter(getItems(), new Predicate<ClassItem>() {
			@Override
			public boolean apply(@Nullable final ClassItem input) {
				final boolean b = input instanceof OS_Element2;
//				System.out.println(String.format("%s %b", input, b));
				return b;
			}
		});
		final ArrayList<OS_Element2> a = new ArrayList<OS_Element2>();
		for (final ClassItem functionItem : c) {
			a.add((OS_Element2) functionItem);
		}
		return a;
	}

	@Override // OS_Container
	public void add(final OS_Element anElement) {
		if (!(anElement instanceof ClassItem))
			throw new IllegalStateException(String.format("Cant add %s to ClassStatement", anElement));
		items.add((ClassItem) anElement);
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		visit.addClass(this);
	}
	
	public void setPackageName(final OS_Package aPackageName) {
		_packageName = aPackageName;
	}
	
	public OS_Package getPackageName() {
		return _packageName;
	}

	@Override
	public String toString() {
		final String package_name;
		if (getPackageName() != null && getPackageName()._name != null) {
			final Qualident package_name_q = getPackageName()._name;
			package_name = package_name_q.toString();
		} else
			package_name = "`'";
		return String.format("<Class %d %s %s>", _a.getCode(), package_name, getName());
	}

	public ConstructorDef addCtor(final IdentExpression aConstructorName) {
		return new ConstructorDef(aConstructorName, this);
	}
	
	public DestructorDef addDtor() {
		return new DestructorDef(this);
	}
	
	public TypeAliasStatement typeAlias() {
		NotImplementedException.raise();
		return null;
	}
	
	public InvariantStatement invariantStatement() {
		NotImplementedException.raise();
		return null;
	}
	
	public ProgramClosure XXX() {
		return new ProgramClosure() {};
	}

	@Override // OS_Element
	public Context getContext() {
		return _a._context;
	}


	@Override // OS_Element2
	public String name() {
		return getName();
	}

	public Collection<ClassItem> findFunction(final String name) {
		return Collections2.filter(items, new Predicate<ClassItem>() {
			@Override
			public boolean apply(@Nullable final ClassItem item) {
				if (item instanceof FunctionDef)
					if (((FunctionDef) item).name().equals(name))
						return true;
				return false;
			}
		});
	}

	public void setType(final ClassTypes aType) {
		_type = aType;
	}

	public void setContext(final ClassContext ctx) {
		_a.setContext(ctx);
	}

	public void postConstruct() { // TODO
	}

	List<AnnotationClause> annotations = null;

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	public void addAccess(final AccessNotation acs) {
		accesses.add(acs);
	}


	public IdentExpression getNameNode() {
		return clsName;
	}

	public void setInheritance(ClassInheritance inh) {
		_inh = inh;
	}

	public void walkAnnotations(AnnotationWalker annotationWalker) {
		if (annotations == null) return;
		for (AnnotationClause annotationClause : annotations) {
			for (AnnotationPart annotationPart : annotationClause.aps) {
				annotationWalker.annotation(annotationPart);
			}
		}
	}

	public Iterable<AnnotationPart> annotationIterable() {
		List<AnnotationPart> aps = new ArrayList<AnnotationPart>();
		if (annotations == null) return aps;
		for (AnnotationClause annotationClause : annotations) {
			for (AnnotationPart annotationPart : annotationClause.aps) {
				aps.add(annotationPart);
			}
		}
		return aps;
	}

	public PropertyStatement prop() {
		return new PropertyStatement(this, getContext());
	}

	public boolean hasItem(OS_Element element) {
		return items.contains(element);
	}
}

//
//
//
