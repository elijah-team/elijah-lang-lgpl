package tripleo.elijah.lang;

import java.util.*;


// Referenced classes of package pak2:
//			StatementClosure, VariableSequence, ProcedureCallExpression, Loop, 
//			YieldExpression, IfExpression

public final class AbstractStatementClosure implements StatementClosure, StatementItem {

	public AbstractStatementClosure(Scope aParent) {
		// TODO Auto-generated constructor stub
		parent = aParent;
	}

	private StatementItem add(StatementItem aItem) {
		parent.add(aItem);
		return aItem;
	}
	public BlockStatement blockClosure() {
		bs=new BlockStatement(this.parent);
		add(bs);
		return bs;
	}
	public IExpression constructExpression() {
		ctex=new ConstructExpression(this.parent); 
		add(ctex);
		return null;
	}
	public IfExpression ifExpression() {
		ifex=new IfExpression(this.parent); 
		add(ifex);
		return ifex;
	}
	public Loop loop() {
		loop=new Loop();
		add(loop);
		return loop;
	}
	public StatementClosure procCallExpr() {
		pcex=new AbstractStatementClosure(parent); //TODO:
		add(pcex);
		return pcex;
	}

	public ProcedureCallExpression procedureCallExpression() {
		pce=new ProcedureCallExpression();
		add(pce);
		return pce;
	}

	public VariableSequence varSeq() {
		return (VariableSequence) add(vsq=new VariableSequence());
	}

	private BlockStatement bs;

	private ConstructExpression ctex;
	private IfExpression ifex;
	List<StatementItem> items =new ArrayList<StatementItem>();
	private Loop loop;
	
	final Scope parent;

	private ProcedureCallExpression pce;

	private AbstractStatementClosure pcex;

	private VariableSequence vsq;

	private YieldExpression yiex;

	public void constructExpression(IExpression aExpr) {
		add((StatementItem) aExpr);
	}

	public void yield(IExpression aExpr) {
		add((StatementItem) aExpr);
	}
}
