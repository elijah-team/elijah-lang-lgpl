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

	private boolean _isAbstract;
	private FunctionModifiers _mod;

	public Iterable<FormalArgListItem> getArgs() {
		return mFal.items();
	}
	private final List<String> mDocs = new ArrayList<String>();

	@Override
	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	public void setAbstract(final boolean b) {
		_isAbstract = b;
		if (b) {this.set(FunctionModifiers.ABSTRACT);}
	}

	public void set(final FunctionModifiers mod) {
		_mod = mod;
	}

	public void setReturnType(final TypeName tn) {
		this._returnType = tn;
	}

	public final class FunctionDefScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this, getParent());

		@Override
		public void add(final StatementItem aItem) {
			if (!(aItem instanceof FunctionItem)) {
				System.err.println(String.format("adding false FunctionItem %s", aItem.getClass().getName()));
				return;
			}
			items.add((FunctionItem) aItem);
		}
		
		@Override
		public void addDocString(final Token aS) {
			docstrings.add(aS.getText());
		}
		
		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(final IExpression aExpr) {
			add(new StatementWrapper(aExpr, getContext(), getParent()));
//			throw new NotImplementedException(); // TODO
		}

		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getParent()
		 */
		@Override
		public OS_Element getParent() {
			// TODO Auto-generated method stub
			return FunctionDef.this;
		}

		@Override
		public OS_Element getElement() {
			return FunctionDef.this;
		}


	}

	public Attached _a = new Attached();
	private TypeName _returnType = null/*new RegularTypeName()*/;
	private final List<String> docstrings = new ArrayList<String>();
	public IdentExpression funName;
	private final List<FunctionItem> items = new ArrayList<FunctionItem>();
	private final FormalArgList mFal = new FormalArgList();
	private final FunctionDefScope mScope2 = new FunctionDefScope();
	//	private FunctionDefScope mScope;
	private final OS_Element/*ClassStatement*/ parent;

	public FunctionDef(final OS_Element aElement) {
		parent = aElement;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else {
			throw new IllegalStateException("adding FunctionDef to "+aElement.getClass().getName());
		}
	}

	public FormalArgList fal() {
		return mFal;
	}

	@Override // OS_Element
	public Context getContext() {
		return _a._context;
	}

	public List<FunctionItem> getItems() {
		return items;
	}

	@Override // OS_Element
	public OS_Element getParent() {
		return parent;
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

	public TypeName returnType() {
		// TODO Auto-generated method stub
//		if (_returnType.isNull()) System.err.println("101 NULL (Unresolved) returnType");
		return _returnType;
	}

	public Scope scope() {
		//assert mScope == null;
		return mScope2;
	}

	public void setName(final IdentExpression aText) {
		funName = aText;
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

	@Override // OS_Element2
	public String name() {
		if (funName == null)
			return "";
		return funName.getText();
	}

	public void setContext(final FunctionContext ctx) {
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
}

//
//
//
