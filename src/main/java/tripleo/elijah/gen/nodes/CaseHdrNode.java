/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/*
 * Created on Sep 2, 2005 2:28:42 PM
 *
 * $Id$
 *
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.Node;
import tripleo.elijah.lang.VariableReference;

public class CaseHdrNode implements Node {

	private IExpressionNode expr;
	
//	public CaseHdrNode(VariableReferenceNode2 varref) {
//		this.expr = varref;
//	}
//
//	public CaseHdrNode(VariableReference vr) {
////		NotImplementedException.raise();
//		this.expr = new VariableReferenceNode2(vr.getName(),  null, false);
//	}
	
	public CaseHdrNode(final VariableReferenceNode3 varref) {
		// TODO Auto-generated constructor stub
		this.expr = /*new ExpressionNode*/(varref);
	}

	public IExpressionNode getExpr() {
		return expr;
	}

	public void setExpr(final ExpressionNode expr) {
		this.expr = expr;
	}
	
	public String simpleGenText() {
		if (expr instanceof VariableReferenceNode3) {
			return expr.genText();
		}
		if (expr.getExpr() instanceof VariableReference) {
			return ((VariableReference) expr.getExpr()).getName();
		}
		if (expr/*.getExpr()*/ instanceof VariableReferenceNode2) {
			return expr.genText();
		}
		throw new IllegalStateException("no implementation");
//		NotImplementedException.raise();
//		return null;
	}
	
	/**
	 * do not call
	 *
	 * @return
	 */
	@Override
	public int getCode() {
		return -1;
	}
}

//
//
//
