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
	private TmpSSACtxNode tmp;

	public LocalAgnTmpNode(TmpSSACtxNode tccssan, IExpression binex) {
		// TODO Auto-generated constructor stub
		this.tmp = tccssan;
		this.expr = binex;
	}

}
