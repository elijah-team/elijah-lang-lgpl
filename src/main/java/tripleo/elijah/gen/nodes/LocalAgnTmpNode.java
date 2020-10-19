/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo(sb)
 *
 */
public class LocalAgnTmpNode {

//	private IExpression expr;
	private TmpSSACtxNode agnTo;
	private IExpressionNode agnWhat;
	private int n;
	
	public String genName() {
		return declared;
	}
	
	private String declared = null;

//	public LocalAgnTmpNode(TmpSSACtxNode tccssan, IExpression expression) {
//		// TODO Auto-generated constructor stub
//		this.agnTo = tccssan;
////		this.expr = expression;
//		//
//		setRight(new ExpressionNode(expression));
//		//
//		agnTo.setExprType(expression);
//		//
//		n=tccssan.getCtx().nextTmp();
//		this.declared ="vt"+n;
//	}
	
	public LocalAgnTmpNode(final TmpSSACtxNode tmpSSACtxNode, final IExpressionNode node) {
		// TODO Auto-generated constructor stub
		this.agnTo = tmpSSACtxNode;
//		this.expr = expression;
		//
		setRight(node);
		//
		agnTo.setExprType(node);
		//
		// set ctx_node name (vt1, vt2, etc)
		//
		n=tmpSSACtxNode.getCtx().nextTmp();
		this.declared ="vt"+n;
		//
		tmpSSACtxNode._tmp = this;
		//
		// TODO extract type from getRight()
		//
	}
	
	public LocalAgnTmpNode(final String string) {
		// TODO Auto-generated constructor stub
		NotImplementedException.raise();
	}

	public IExpressionNode getRight() {
		return agnWhat;
	}
	
	public void setRight(final IExpressionNode agnWhat) {
		this.agnWhat = agnWhat;
	}
	
	public ExpressionNode getLeft() {
		return new VariableReferenceNode2(declared, "Z0*", false); // TODO semi-hardcoded
	}
}

//
//
//
