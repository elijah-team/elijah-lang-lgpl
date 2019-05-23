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

import java.util.ArrayList;
import java.util.List;

public class IfExpression implements StatementItem {
	
	private final IfExpression sibling;
	private List<IfExpression> parts = new ArrayList<IfExpression>();
	
	public IfExpression(Scope aClosure) {
		this.parent = aClosure;
		this.sibling = null; // top
	}
	
	public IfExpression(IfExpression ifExpression) {
		this.sibling = ifExpression;
	}
	
	public IfExpression else_() {
		IfExpression elsepart = new IfExpression(this);
		parts.add(elsepart);
		return elsepart;
	}

	public IfExpression elseif() {
		IfExpression elseifpart = new IfExpression(this);
		parts.add(elseifpart);
		return elseifpart;
	}

	public void expr(IExpression expr) {
		this.expr = expr;
	}

	public Scope scope() {
		return new IfExpressionScope();
	}

	private IExpression expr;
	private Scope parent;
	
	private class IfExpressionScope implements Scope {
		@Override
		public void statementWrapper(IExpression aExpr) {
			parent.add(new FunctionDef.StatementWrapper(aExpr));
		}
		
		@Override
		public void addDocString(Token s) {
			parent.addDocString(s);
		}
		
		@Override
		public StatementClosure statementClosure() {
			return new AbstractStatementClosure(this); // TODO
		}
		
		@Override
		public BlockStatement blockStatement() {
			return parent.blockStatement(); // TODO
		}
		
		@Override
		public void add(StatementItem aItem) {
			parent.add(aItem);
		}
		
		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}
	}
}

//
//
//
