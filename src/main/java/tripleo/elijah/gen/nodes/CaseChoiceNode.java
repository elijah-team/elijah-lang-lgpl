package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.OS_Integer;

public class CaseChoiceNode {

	public ExpressionNode left;
	public ScopeNode right;

	public CaseChoiceNode(OS_Integer expr1) {
		// TODO Auto-generated constructor stub
		left=new ExpressionNode(expr1);
		right=null;
	}

	public CaseChoiceNode(ExpressionNode left, ScopeNode right) {
		super();
		this.right = right;
		this.left = left;
	}

}
