package tripleo.elijah.lang;


// Referenced classes of package pak2:
//			Statement, LoopTypes, Scope

public class Loop implements Statement, LoopTypes, StatementItem {

	public Loop() {
	}

	public void type(int aType) {
		type = aType;
	}

	public Scope scope() {
		return null;
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
	int type;
IExpression topart,expr; 
IExpression frompart;

	public final int FROM_TO_TYPE = 82;

	public final int TO_TYPE = 81;
	public final int ITER_TYPE = 86;

	public final int EXPR_TYPE = 83;
}
