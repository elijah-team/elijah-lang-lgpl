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
import tripleo.elijah.contexts.WithContext;
import tripleo.elijah.gen.ICodeGen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 8/30/20 1:51 PM
 */
public class WithStatement implements OS_Element, OS_Container {
	private final OS_Element _parent;
	private WithContext ctx;
	private final List<FunctionItem> _items = new ArrayList<FunctionItem>();
	private final List<String> mDocs = new ArrayList<String>();
	private Scope _scope = new /*WithScope*/AbstractBlockScope(this) {
		@Override
		public Context getContext() {
			return WithStatement.this.getContext();
		}
	};

	@Override
	public void addDocString(Token aText) {
		mDocs.add(aText.getText());
	}

	public WithStatement(OS_Element aParent) {
		_parent = aParent;
	}

	@Override
	public void visitGen(ICodeGen visit) {

	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		return null;
	}

	public List<FunctionItem> getItems() {
		return _items;
	}

	public Collection<VariableStatement> getVarItems() {
		return hidden_seq.items();
	}

	public VariableStatement nextVarStmt() {
		return hidden_seq.next();
	}

	VariableSequence hidden_seq = new VariableSequence();

	public void setContext(WithContext ctx) {
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
	public void add(OS_Element anElement) {
		if (!(anElement instanceof FunctionItem))
			return;
		_items.add((FunctionItem) anElement);
	}

//	public final class WithScope implements Scope {
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
//		public TypeAliasExpression typeAlias() {
//			return null;
//		}
//
//		/* (non-Javadoc)
//		 * @see tripleo.elijah.lang.Scope#getParent()
//		 */
//		@Override
//		public OS_Element getParent() {
//			return WithStatement.this;
//		}
//
//		@Override
//		public OS_Element getElement() {
//			return WithStatement.this;
//		}
//
//
//	}
}

//
//
//
