/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.IExpression;

/**
 * @author SBUSER
 *
 */
public class LocalAgnTmpNode {

	private IExpression expr;
	private TmpSSACtxNode agnTo;
	private ExpressionNode agnWhat;

	public LocalAgnTmpNode(TmpSSACtxNode tccssan, IExpression binex) {
		// TODO Auto-generated constructor stub
		this.agnTo = tccssan;
		this.expr = binex;
		//
		setRight(new ExpressionNode(binex));
	}
	
	public ExpressionNode getRight() {
		return agnWhat;
	}
	
	public void setRight(ExpressionNode agnWhat) {
		this.agnWhat = agnWhat;
	}
}
