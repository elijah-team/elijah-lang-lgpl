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
 * <p>
 * items -> ClassItems
 * docstrings
 * variables
 */
public class ClassStatement extends ProgramClosure implements ClassItem, ModuleItem, StatementItem, FunctionItem, OS_Element, OS_Element2, Documentable, OS_Container {

	private final List<ClassItem> items = new ArrayList<ClassItem>();
	private final List<String> mDocs = new ArrayList<String>();
	public IdentExpression clsName;
	public /*final*/ OS_Element parent;
	public Attached _a = new Attached();
	ClassInheritance _inh = new ClassInheritance(); // remove final for ClassBuilder
	List<AnnotationClause> annotations = null;
	private OS_Package _packageName;
	private ClassTypes _type;
	private List<AccessNotation> accesses = new ArrayList<AccessNotation>();
	private TypeNameList genericPart;

	public ClassStatement(final OS_Element parentElement, final Context parentContext) {
		parent = parentElement; // setParent
		if (parentElement instanceof OS_Module) {
			final OS_Module module = (OS_Module) parentElement;
			//
			this.setPackageName(module.pullPackageName());
			_packageName.addElement(this);
			module.add(this);
		} else if (parentElement instanceof FunctionDef) {
			// do nothing
		} else if (parentElement instanceof OS_Container) {
			((OS_Container) parentElement).add(this);
		} else {
			throw new IllegalStateException(String.format("Cant add ClassStatement to %s", parentElement));
		}
		setContext(new ClassContext(parentContext, this));
	}

	@Override
	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	public String getName() {
//		if (clsName == null)
//			return "";
		return clsName.getText();
	}

	public void setName(final IdentExpression aText) {
		clsName = aText;
	}

	public List<ClassItem> getItems() {
		return items;
	}

	public List<String> getDocstrings() {
		return mDocs;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

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
		visit.addClass(this); // TODO visitClass
	}

	public OS_Package getPackageName() {
		return _packageName;
	}

	public void setPackageName(final OS_Package aPackageName) {
		_packageName = aPackageName;
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
		return new ConstructorDef(aConstructorName, this, getContext());
	}

	public DestructorDef addDtor() {
		return new DestructorDef(this, getContext());
	}

	@Override // OS_Element
	public Context getContext() {
		return _a._context;
	}

	public void setContext(final ClassContext ctx) {
		_a.setContext(ctx);
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

	public void postConstruct() {
		assert clsName != null;
		int destructor_count = 0;
		for (ClassItem item : items) {
			if (item instanceof DestructorDef)
				destructor_count++;
		}
		assert destructor_count == 0 || destructor_count ==1;
	}

	// region inheritance

	public void addAccess(final AccessNotation acs) {
		accesses.add(acs);
	}

	public IdentExpression getNameNode() {
		return clsName;
	}

	public void setInheritance(ClassInheritance inh) {
		_inh = inh;
	}

	// endregion

	// region annotations

	public ClassInheritance classInheritance() {
		return _inh;
	}

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	public void addAnnotations(List<AnnotationClause> as) {
		if (as == null) return;
		for (AnnotationClause annotationClause : as) {
			addAnnotation(annotationClause);
		}
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
			aps.addAll(annotationClause.aps);
		}
		return aps;
	}

	// endregion

	// region called from parser

	public FunctionDef funcDef() {
		return new FunctionDef(this, getContext());
	}

	public PropertyStatement prop() {
		PropertyStatement propertyStatement = new PropertyStatement(this, getContext());
		add(propertyStatement);
		return propertyStatement;
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
		return new ProgramClosure() {
		};
	}

	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

	// endregion

	public boolean hasItem(OS_Element element) {
		if (!(element instanceof ClassItem)) return false;
		return items.contains((ClassItem) element);
	}

	public void setGenericPart(TypeNameList genericPart) {
		this.genericPart = genericPart;
	}

	public List<TypeName> getGenericPart() {
		if (genericPart == null)
			return new ArrayList<TypeName>();
		else
			return genericPart.p;
	}
}

//
//
//
