/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.List;


// Referenced classes of package pak2:
//			StatementClosure, VariableSequence, ProcedureCallExpression, Loop, 
//			YieldExpression, IfExpression

public final class AbstractStatementClosure implements StatementClosure, StatementItem {

	public AbstractStatementClosure(Scope aParent) {
		// TODO Auto-generated constructor stub
		parent = aParent;
	}

	public AbstractStatementClosure(ClassStatement classStatement) {
		// TODO check final member
//		parent = classStatement;
		parent = null;
	}

	@Override
	public BlockStatement blockClosure() {
		bs=new BlockStatement(this.parent);
		add(bs);
		return bs;
	}
	public IExpression constructExpression() {
		ctex=new ConstructExpression(this.parent); 
		add(ctex);
		return ctex;
	}
	@Override
	public void constructExpression(IExpression aExpr, FormalArgList aO) {
		add((StatementItem) aExpr);
	}
	@Override
	public IfExpression ifExpression() {
		ifex=new IfExpression(this.parent); 
		add(ifex);
		return ifex;
	}
	@Override
	public Loop loop() {
		loop=new Loop();
		add(loop);
		return loop;
	}
	@Override
	public StatementClosure procCallExpr() {
		pcex=new AbstractStatementClosure(parent); //TODO:
		add(pcex);
		return pcex;
	}

	@Override
	public ProcedureCallExpression procedureCallExpression() {
		pce=new ProcedureCallExpression();
		add(pce);
		return pce;
	}

	@Override
	public VariableSequence varSeq() {
		return (VariableSequence) add(vsq=new VariableSequence());
	}

	@Override
	public void yield(IExpression aExpr) {
		add(new YieldExpression(aExpr));
	}

	@org.jetbrains.annotations.Contract("_ -> param1")
	private StatementItem add(StatementItem aItem) {
		parent.add(aItem);
		return aItem;
	}
	
	private BlockStatement bs;
	private ConstructExpression ctex;
	private IfExpression ifex;
	
	private Loop loop;
	private ProcedureCallExpression pce;
	private AbstractStatementClosure pcex;
	private VariableSequence vsq;
	private YieldExpression yiex;

	final List<StatementItem> items =new ArrayList<StatementItem>();

	final Scope parent;
}
