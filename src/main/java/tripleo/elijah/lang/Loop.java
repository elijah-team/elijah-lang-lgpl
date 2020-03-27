/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import tripleo.elijah.contexts.LoopContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.lang.FunctionDef.StatementWrapper;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak2:
//			Statement, LoopTypes, Scope

public class Loop implements Statement, LoopTypes, StatementItem, FunctionItem {

	private Scope _scope = new LoopScope();
	private List<String> docstrings = new ArrayList<String>();
	private List<StatementItem> items = new ArrayList<StatementItem>();
	private OS_Element parent;

	@Deprecated public Loop() {
	}

	public Loop(OS_Element aParent) {
		parent = aParent;
	}

	public void type(int aType) {
		type = aType;
	}

	public Scope scope() {
		return _scope ;
	}

	public void expr(IExpression aExpr) {
		expr=aExpr;
	}

	public void topart(IExpression aExpr) {
		topart=aExpr;
	}

	public void frompart(IExpression aExpr) {
		frompart=aExpr;
	}

	public void iterName(Token s) {
//		assert type == ITER_TYPE;
		iterName=s.getText();
	}

	String iterName;
	/**
	 * @category type
	 */
	private int type;
private IExpression topart,frompart;
private IExpression expr;
private Attached _a = new Attached(new LoopContext(this));

	/**
	 * @category type
	 */
	public static final int FROM_TO_TYPE = 82;

	/**
	 * @category type
	 */
	public static final int TO_TYPE = 81;
	
	public List<StatementItem> getItems() {
		return items;
	}
	
	/**
	 * @category type
	 */
	public static final int ITER_TYPE = 86;

	/**
	 * @category type
	 */
	public static final int EXPR_TYPE = 83;

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO this is not implementeed
		NotImplementedException.raise();
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}
	
	public String getIterName() {
		return iterName;
	}
	
	public int getType() {
		return type;
	}
	
	public IExpression getToPart() {
		return topart;
	}
	
	public IExpression getExpr() {
		return expr;
	}
	
	public IExpression getFromPart() {
		return frompart;
	}
	
	private final class LoopScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		@Override
		public void add(StatementItem aItem) {
//			if (aItem instanceof FunctionItem)
//				items.add((FunctionItem) aItem);
//			else
//				System.err.println(String.format("adding false StatementItem %s",
//					aItem.getClass().getName()));
			items.add(aItem);
		}
		
		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}
		
		@Override
		public void addDocString(Token aS) {
			docstrings.add(aS.getText());
		}

		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(IExpression aExpr) {
			add(new StatementWrapper(aExpr));
//			throw new NotImplementedException(); // TODO
		}
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _a ._context;
	}

	
}

//
//
//
