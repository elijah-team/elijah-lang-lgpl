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

	public Loop() {
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

	public void iterName(String s) {
//		assert type == ITER_TYPE;
		iterName=s;
	}

	String iterName;
	/**
	 * @category type
	 */
	int type;
IExpression topart,expr; 
IExpression frompart;

	/**
	 * @category type
	 */
	public final int FROM_TO_TYPE = 82;

	/**
	 * @category type
	 */
	public final int TO_TYPE = 81;
	/**
	 * @category type
	 */
	public final int ITER_TYPE = 86;

	/**
	 * @category type
	 */
	public final int EXPR_TYPE = 83;

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
		public void addDocString(String aS) {
			docstrings.add(aS);
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
}

//
//
//
