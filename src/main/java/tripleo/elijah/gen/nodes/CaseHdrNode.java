package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.VariableReference;

public class CaseHdrNode {

	private ExpressionNode expr;

	public ExpressionNode getExpr() {
		return expr;
	}

	public void setExpr(ExpressionNode expr) {
		this.expr = expr;
	}

	public CaseHdrNode(VariableReference varref) {
		// TODO Auto-generated constructor stub
		this.expr = new ExpressionNode(varref);
	}

}
