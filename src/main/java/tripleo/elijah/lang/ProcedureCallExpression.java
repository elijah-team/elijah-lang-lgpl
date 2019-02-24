package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			ParserClosure, ExpressionList

public class ProcedureCallExpression implements StatementItem {

	public void identifier(String aIdent) {
		target=aIdent;
	}

	public ExpressionList exprList() {
		return args;
	}
	
	String target;
	ExpressionList args=new ExpressionList();
}
