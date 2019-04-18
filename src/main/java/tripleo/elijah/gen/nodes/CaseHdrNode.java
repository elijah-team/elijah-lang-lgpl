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

//
//
//
