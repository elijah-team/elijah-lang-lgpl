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
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class IfConditional implements StatementItem, FunctionItem, OS_Element {

	private final Scope parent_scope;
	private final IfConditional sibling;
	private final List<IfConditional> parts = new ArrayList<IfConditional>();
	private int order = 0;
	private IExpression expr;
	private List<OS_Element> _items = new ArrayList<OS_Element>();
//	private final IfExpression if_parent;

	public IfConditional(IfConditional ifExpression) {
		this.sibling = ifExpression;
		this.order = ++sibling/*if_parent*/.order;
		this.parent_scope = this.sibling.parent_scope;
	}
	
	public IfConditional(Scope aClosure) {
		this.parent_scope = aClosure;
		this.sibling = null; // top
	}

	@Override
	public void visitGen(ICodeGen visit) {
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		throw new NotImplementedException();
//		return null;
	}

	@Override
	public Context getContext() {
		throw new NotImplementedException();
//		return null;
	}

	public IExpression getExpr() {
		return expr;
    }

	public IfConditional else_() {
		IfConditional elsepart = new IfConditional(this);
		parts.add(elsepart);
		return elsepart;
	}

	public IfConditional elseif() {
		IfConditional elseifpart = new IfConditional(this);
		parts.add(elseifpart);
		return elseifpart;
	}

	/**
	 * will not be null during if or elseif
	 *
	 * @param expr
	 */
	public void expr(IExpression expr) {
		this.expr = expr;
	}
	
	/**
	 * will always be nonnull
	 *
	 */
	public Scope scope() {
		return new IfConditionalScope();
	}
	
	public List<OS_Element> getItems() {
		return _items;
	}

    private class IfConditionalScope implements Scope {
		@Override
		public void addDocString(Token s) {
			addDocString(s);
		}

		/*@ requires parent != null; */
		@Override
		public void statementWrapper(IExpression aExpr) {
			//if (parent_scope == null) throw new IllegalStateException("parent is null");
			add(new StatementWrapper(aExpr, ctx, parent));
		}

	    @Override
		public StatementClosure statementClosure() {
			return new AbstractStatementClosure(this); // TODO
		}

		@Override
		public BlockStatement blockStatement() {
			return blockStatement(); // TODO
		}

		@Override
		public void add(StatementItem aItem) {
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

	private void add(StatementItem aItem) {
		_items.add((OS_Element) aItem);
	}

}

//
//
//
