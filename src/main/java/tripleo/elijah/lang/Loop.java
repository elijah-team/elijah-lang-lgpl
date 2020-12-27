/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.contexts.LoopContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Loop implements  StatementItem, FunctionItem, OS_Element {

	private final Scope _scope = new LoopScope(this);
	private final List<StatementItem> items = new ArrayList<StatementItem>();
	private final OS_Element parent;

	@Deprecated public Loop(final OS_Element aParent) {
		// document assumption
		if (!(aParent instanceof FunctionDef) && !(aParent instanceof Loop))
			System.out.println("parent is not FunctionDef or Loop");
		parent = aParent;
	}

	public Loop(final OS_Element aParent, final Context ctx) {
		// document assumption
		if (!(aParent instanceof FunctionDef) && !(aParent instanceof Loop))
			System.out.println("parent is not FunctionDef or Loop");
		parent = aParent;
		_a.setContext(new LoopContext(ctx, this));
	}

	public void type(final LoopTypes aType) {
		type = aType;
	}

	public Scope scope() {
		return _scope ;
	}

	public void expr(final IExpression aExpr) {
		expr=aExpr;
	}

	public void topart(final IExpression aExpr) {
		topart=aExpr;
	}

	public void frompart(final IExpression aExpr) {
		frompart=aExpr;
	}

	public void iterName(final IdentExpression s) {
//		assert type == ITER_TYPE;
		iterName=s;
	}

	IdentExpression iterName;
	/**
	 * @category type
	 */
	private LoopTypes type;

private IExpression topart,frompart;
private IExpression expr;
private final Attached _a = new Attached();

	public List<StatementItem> getItems() {
		return items;
	}

	@Override // OS_Element
	public void visitGen(final ICodeGen visit) {
		throw new NotImplementedException();
	}

	public String getIterName() {
		return iterName.getText();
	}
	
	public LoopTypes getType() {
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

	public void setContext(final LoopContext ctx) {
		_a.setContext(ctx);
	}

	public IdentExpression getIterNameToken() {
		return iterName;
	}

    private final class LoopScope extends AbstractScope2 {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		protected LoopScope(OS_Element aParent) {
			super(aParent);
		}

		@Override
		public void add(final StatementItem aItem) {
//			if (aItem instanceof FunctionItem)
//				items.add((FunctionItem) aItem);
//			else
//				System.err.println(String.format("adding false StatementItem %s",
//					aItem.getClass().getName()));
			items.add(aItem);
		}
		
		@Override
		public StatementClosure statementClosure() {
			return asc;
		}
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _a.getContext();
	}

}

//
//
//
