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
import tripleo.elijah.contexts.IfConditionalContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class IfConditional implements StatementItem, FunctionItem, OS_Element {

//	private final Scope parent_scope;
	private final IfConditional sibling;
	private final List<IfConditional> parts = new ArrayList<IfConditional>();
//	private int order = 0;
	private IExpression expr;
	private final List<OS_Element> _items = new ArrayList<OS_Element>();
	final IfConditionalScope _scope = new IfConditionalScope();
	private final OS_Element _parent;
	private Context _ctx;
//	private final IfExpression if_parent;

	public IfConditional(final OS_Element _parent) {
		this._parent = _parent;
		this._ctx = null;
		this.sibling = null;
	}

	public IfConditional(final IfConditional ifExpression) {
		this.sibling = ifExpression;
//		this.order = ++sibling/*if_parent*/.order;
		//
		this._ctx = new IfConditionalContext(ifExpression._ctx, this, true);
		this._parent = ifExpression._parent;
//		this.parent_scope = this.sibling.parent_scope;
	}
	
//	public IfConditional(Scope aClosure) {
//		this.parent_scope = aClosure;
//		this.sibling = null; // top
//	}

	@Override
	public void visitGen(final ICodeGen visit) {
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		return _ctx;
	}

	public IExpression getExpr() {
		return expr;
    }

	public IfConditional else_() {
		final IfConditional elsepart = new IfConditional(this);
		parts.add(elsepart);
		return elsepart;
	}

	public IfConditional elseif() {
		final IfConditional elseifpart = new IfConditional(this);
		parts.add(elseifpart);
		return elseifpart;
	}

	/**
	 * will not be null during if or elseif
	 *
	 * @param expr
	 */
	public void expr(final IExpression expr) {
		this.expr = expr;
	}
	
	/**
	 * will always be nonnull
	 *
	 */
	public Scope scope() {
		return _scope;
	}
	
	public List<OS_Element> getItems() {
		return _items;
	}

	public List<IfConditional> getParts() {
		return parts;
	}

	public void setContext(final IfConditionalContext ifConditionalContext) {
		_ctx = ifConditionalContext;
	}

	private class IfConditionalScope implements Scope {
		private List<Token> mDocs;

		@Override
		public void addDocString(final Token s) {
			if (mDocs == null)
				mDocs = new ArrayList<Token>();
			mDocs.add(s);
		}

		/*@ requires parent != null; */
		@Override
		public void statementWrapper(final IExpression aExpr) {
			//if (parent_scope == null) throw new IllegalStateException("parent is null");
			add(new StatementWrapper(aExpr, getContext(), getParent()));
		}

	    @Override
		public StatementClosure statementClosure() {
			return new AbstractStatementClosure(this); // TODO
		}

		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this); // TODO
		}

		@Override
		public void add(final StatementItem aItem) {
			IfConditional.this.add(aItem);
		}

		@Override
		public TypeAliasExpression typeAlias() {
			throw new NotImplementedException();
//			return null;
		}

		@Override
		public InvariantStatement invariantStatement() {
			throw new NotImplementedException();
//			return null;
		}

	    @Override
	    public OS_Element getParent() {
		    return IfConditional.this;
	    }

	    @Override
	    public OS_Element getElement() {
		    return IfConditional.this;
	    }
	}

	private void add(final StatementItem aItem) {
		_items.add((OS_Element) aItem);
	}

}

//
//
//
