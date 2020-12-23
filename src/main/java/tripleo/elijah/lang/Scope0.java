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
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Scope0 implements Scope {
	private final FuncExpr funcExpr;
	private final List<String> docstrings = new ArrayList<String>();
	private final List<FunctionItem> items = new ArrayList<FunctionItem>();

	private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

	public Scope0(final FuncExpr funcExpr) {
		this.funcExpr = funcExpr;
	}

	@Override
	public void add(final StatementItem aItem) {
		if (aItem instanceof FunctionItem)
			items.add((FunctionItem) aItem);
		else
			System.err.println(String.format("106 adding false FunctionItem %s",
				aItem.getClass().getName()));
	}

	@Override
	public void addDocString(final Token aS) {
		throw new NotImplementedException();
//			docstrings.add(aS.getText());
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
		add(new StatementWrapper(aExpr, getParent().getContext(), getParent()));
//			throw new NotImplementedException(); // TODO
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
		return funcExpr;
	}

	@Override
	public OS_Element getElement() {
		return funcExpr;
	}

	final FormalArgList formalArgList = new FormalArgList();

	public FormalArgList fal() {
		return formalArgList;
	}
		
		
}

//
//
//
