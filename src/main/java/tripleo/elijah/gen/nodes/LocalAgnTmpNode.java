/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.IExpression;

/**
 * @author SBUSER
 *
 */
public class LocalAgnTmpNode {

//	private IExpression expr;
	private TmpSSACtxNode agnTo;
	private ExpressionNode agnWhat;
	private int n;
	private String declared = null;

	public LocalAgnTmpNode(TmpSSACtxNode tccssan, IExpression expression) {
		// TODO Auto-generated constructor stub
		this.agnTo = tccssan;
//		this.expr = expression;
		//
		setRight(new ExpressionNode(expression));
		//
		agnTo.__expr = expression;
		//
		n=tccssan.getCtx().nextTmp();
		this.declared ="vt"+n;
	}
	
	public ExpressionNode getRight() {
		return agnWhat;
	}
	
	public void setRight(ExpressionNode agnWhat) {
		this.agnWhat = agnWhat;
	}
	
	public ExpressionNode getLeft() {
		return new VariableReferenceNode2(declared, "Z0*", false); // TODO semi-hardcoded
	}
}

//
//
//
