/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 8:43:27 PM
 * 
 * $Id$
 */
package tripleo.elijah.lang;

import antlr.Token;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO FunctionDef is not a Container is it?
public class FunctionDef implements Documentable, ClassItem, OS_Container, OS_Element2 {

	public Iterable<FormalArgListItem> getArgs() {
		return mFal.items();
	}

	public void setReturnType(final TypeName tn) {
		this._returnType = tn;
	}

	public final class FunctionDefScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this, getParent());

		@Override
		public void add(final StatementItem aItem) {
			if (!(aItem instanceof FunctionItem)) {
				System.err.println(String.format("Will not add false StatementItem, is not FunctionItem %s", aItem.getClass().getName()));
				return;
			}
			items.add((FunctionItem) aItem);
		}
		
		@Override
		public void addDocString(final Token aS) {
			mDocs.add(aS.getText());
		}
		
		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			throw new NotImplementedException();
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(final IExpression aExpr) {
			add(new StatementWrapper(aExpr, getContext(), getParent()));
		}

		@Override
		public TypeAliasStatement typeAlias() {
			throw new NotImplementedException();
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getParent()
		 */
		@Override
		public OS_Element getParent() {
			// TODO Auto-generated method stub
			return FunctionDef.this;
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getElement()
		 */
		@Override
		public OS_Element getElement() {
			return FunctionDef.this;
		}
	}

	public Attached _a = new Attached();
	private TypeName _returnType = null;
	private final FunctionDefScope mScope2 = new FunctionDefScope();

	// region constructor

	private final OS_Element parent;

	@Deprecated public FunctionDef(final OS_Element aElement) {
		assert aElement != null;
		parent = aElement;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else {
			throw new IllegalStateException("adding FunctionDef to "+aElement.getClass().getName());
		}
	}

	public FunctionDef(OS_Element element, Context context) {
		parent = element;
		if (element instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else {
			throw new IllegalStateException("adding FunctionDef to " + element.getClass().getName());
		}
		_a.setContext(new FunctionContext(context, this));
	}

	// endregion

	// region arglist

	public FormalArgList fal() {
		return mFal;
	}

	private final FormalArgList mFal = new FormalArgList();

	// endregion

	@Override // OS_Element
	public OS_Element getParent() {
		return parent;
	}

	// region items

	private final List<FunctionItem> items = new ArrayList<FunctionItem>();

	public List<FunctionItem> getItems() {
		return items;
	}

	@Override // OS_Container
	public List<OS_Element2> items() {
		final Collection<FunctionItem> c = Collections2.filter(getItems(), new Predicate<FunctionItem>() {
			@Override
			public boolean apply(@Nullable final FunctionItem input) {
				final boolean b = input instanceof OS_Element2;
//				System.out.println(String.format("%s %b", input, b));
				return b;
			}
		});
		final ArrayList<OS_Element2> a = new ArrayList<OS_Element2>();
		for (final FunctionItem functionItem : c) {
			a.add((OS_Element2) functionItem);
		}
		return a;
	}

	@Override // OS_Container
	public void add(final OS_Element anElement) {
		if (anElement instanceof FunctionItem)
			items.add((FunctionItem) anElement);
		else
			throw new IllegalStateException(String.format("Cant add %s to FunctionDef", anElement));
	}

	// endregion

	/**
	 * Can be {@code null} under the following circumstances:<br/><br/>
	 *
	 * 1. The compiler(parser) didn't get a chance to set it yet<br/>
	 * 2. The programmer did not specify a return value and the compiler must deduce it<br/>
	 * 3. The function is a void-type and specification isn't required <br/>
	 *
	 * @return the associated TypeName or NULL
	 */
	public TypeName returnType() {
//		if (_returnType.isNull()) System.err.println("101 NULL (Unresolved) returnType");
		return _returnType;
	}

	public Scope scope() {
		return mScope2;
	}

//	public void visit(JavaCodeGen gen) {
//		// TODO Auto-generated method stub
//		for (FunctionItem element : items)
//			gen.addFunctionItem(element);
//	}

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	// region name

	public IdentExpression funName;

	public void setName(final IdentExpression aText) {
		funName = aText;
	}

	@Override // OS_Element2
	public String name() {
		if (funName == null)
			return "";
		return funName.getText();
	}

	// endregion

	// region context

	@Override // OS_Element
	public Context getContext() {
		return _a._context;
	}

	public void setContext(final FunctionContext ctx) {
		_a.setContext(ctx);
	}

	// endregion

	public void postConstruct() { // TODO
	}

	// region annotations

	List<AnnotationClause> annotations = null;

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	// endregion

	// region Documentable

	private final List<String> mDocs = new ArrayList<String>();

	@Override  // Documentable
	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	// endregion

	// region abstract

	private boolean _isAbstract;

	public void setAbstract(final boolean b) {
		_isAbstract = b;
		if (b) {this.set(FunctionModifiers.ABSTRACT);}
	}

	// endregion

	// region modifiers

	private FunctionModifiers _mod;

	public void set(final FunctionModifiers mod) {
		_mod = mod;
	}

	// endregion
}

//
//
//
