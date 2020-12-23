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
import tripleo.elijah.contexts.SyntacticBlockContext;
import tripleo.elijah.gen.ICodeGen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/30/20 1:49 PM
 */
public class SyntacticBlock implements OS_Element, OS_Container, FunctionItem, StatementItem {

	private final List<FunctionItem> _items = new ArrayList<FunctionItem>();
	private final OS_Element _parent;
	private SyntacticBlockContext ctx;
	private Scope _scope = new /*SyntacticBlockScope*/AbstractBlockScope(this) {
		@Override
		public Context getContext() {
			return SyntacticBlock.this.getContext();
		}
	};

	public SyntacticBlock(final OS_Element aParent) {
		_parent = aParent;
	}

	@Override
	public void visitGen(final ICodeGen visit) {

	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		return ctx;
	}

	public List<FunctionItem> getItems() {
		return _items;
	}

	public void setContext(final SyntacticBlockContext ctx) {
		this.ctx = ctx;
	}

	public Scope scope() {
		return _scope;
	}

	public void postConstruct() {
	}

	@Override
	public List<OS_Element2> items() {
		return null;
	}

	@Override
	public void add(final OS_Element anElement) {
		if (!(anElement instanceof FunctionItem))
			return;
		_items.add((FunctionItem) anElement);
	}

	@Override
	public void addDocString(final Token s1) {

	}

//	public final class SyntacticBlockScope implements Scope {
//
//		private final AbstractStatementClosure asc = new AbstractStatementClosure(this, getParent());
//
//		@Override
//		public void add(StatementItem aItem) {
//			if (aItem instanceof FunctionItem)
//				_items.add((FunctionItem) aItem);
//			else
//				System.err.println(String.format("adding false FunctionItem %s", aItem.getClass().getName()));
//		}
//
//		@Override
//		public void addDocString(Token aS) {
//			mDocs.add(aS.getText());
//		}
//
//		@Override
//		public BlockStatement blockStatement() {
//			return new BlockStatement(this);
//		}
//
//		@Override
//		public InvariantStatement invariantStatement() {
//			return null;
//		}
//
//		@Override
//		public StatementClosure statementClosure() {
//			return asc;
//		}
//
//		@Override
//		public void statementWrapper(IExpression aExpr) {
//			add(new StatementWrapper(aExpr, getContext(), getParent()));
//		}
//
//		@Override
//		public TypeAliasStatement typeAlias() {
//			return null;
//		}
//
//		/* (non-Javadoc)
//		 * @see tripleo.elijah.lang.Scope#getParent()
//		 */
//		@Override
//		public OS_Element getParent() {
//			return SyntacticBlock.this;
//		}
//
//		@Override
//		public OS_Element getElement() {
//			return SyntacticBlock.this;
//		}
//
//
//	}
}

//
//
//
