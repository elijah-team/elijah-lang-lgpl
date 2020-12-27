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
import tripleo.elijah.contexts.NamespaceContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Tripleo(sb)
 *
 * Created Apr 2, 2019 at 11:08:12 AM
 */
public class NamespaceStatement implements Documentable, ModuleItem, ClassItem, StatementItem, FunctionItem, OS_Container, OS_Element2 {

	private IdentExpression nsName;
	private final OS_Element parent;
	public Attached _a = new Attached();
	private final List<ClassItem> items = new ArrayList<ClassItem>();
	private NamespaceTypes _kind;
	private OS_Package _packageName;
	private List<AccessNotation> accesses = new ArrayList<AccessNotation>();

//	@Deprecated public NamespaceStatement(final OS_Element aElement) {
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
//			throw new IllegalStateException(String.format("Cant add NamespaceStatement to %s", aElement));
//		}
//	}

	public NamespaceStatement(final OS_Element aElement, final Context context) {
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
			throw new IllegalStateException(String.format("Cant add NamespaceStatement to %s", aElement));
		}
		setContext(new NamespaceContext(context, this));
	}

	public void setPackageName(final OS_Package aPackageName) {
		_packageName = aPackageName;
	}

	public OS_Package getPackageName() {
		return _packageName;
	}

	public void setName(final IdentExpression i1) {
		nsName = i1;
	}

	private final List<String> mDocs = new ArrayList<String>();

	@Override
	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(new AbstractScope2(this) {
			@Override
			public void statementWrapper(final IExpression aExpr) {
				throw new NotImplementedException();
			}

			@Override
			public StatementClosure statementClosure() {
				throw new NotImplementedException();
//				return null;
			}

			@Override
			public void add(final StatementItem aItem) {
				NamespaceStatement.this.add((OS_Element) aItem);
			}

		});
	}

	public TypeAliasStatement typeAlias() {
		throw new NotImplementedException();
	}

	public InvariantStatement invariantStatement() {
		throw new NotImplementedException();
	}
	
	public FunctionDef funcDef() {
		return new FunctionDef(this, getContext());
	}
	
	public ProgramClosure XXX() {
		return new ProgramClosure() {};
	}

	@Override // OS_Element
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override // OS_Element
	public OS_Element getParent() {
		return parent;
	}

	@Override // OS_Element
	public Context getContext() {
		return _a.getContext();
	}

	public List<ClassItem> getItems() {
		return items ;
	}
	
	public String getName() {
		if (nsName == null) return "";
		return nsName.getText();
	}

	public void setType(final NamespaceTypes aType) {
		_kind = aType;
	}

	public NamespaceTypes getKind() { return _kind; }

	@Override
	public String toString() {
		return String.format("<Namespace %d %s `%s'>", _a.getCode(), getPackageName()._name, getName());
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
		if (anElement instanceof ClassItem)
			items.add((ClassItem) anElement);
		else
			System.err.println(String.format("[NamespaceStatement#add] not a ClassItem: %s", anElement));
	}

	@Override // OS_Element2
	public String name() {
		return getName();
	}

	public void setContext(final NamespaceContext ctx) {
		_a.setContext(ctx);
	}

	List<AnnotationClause> annotations = null;

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	public void postConstruct() {
		if (nsName == null || nsName.getText().equals("")) {
			setType(NamespaceTypes.MODULE);
		} else if (nsName.getText().equals("_")) {
			setType(NamespaceTypes.PRIVATE);
		} else if (nsName.getText().equals("__package__")) {
			setType(NamespaceTypes.PACKAGE);
		} else {
			setType(NamespaceTypes.NAMED);
		}
	}

	public void addAccess(final AccessNotation acs) {
		accesses.add(acs);
	}

	public void walkAnnotations(AnnotationWalker annotationWalker) {
		if (annotations == null) return;
		for (AnnotationClause annotationClause : annotations) {
			for (AnnotationPart annotationPart : annotationClause.aps) {
				annotationWalker.annotation(annotationPart);
			}
		}
	}
}

//
//
//
