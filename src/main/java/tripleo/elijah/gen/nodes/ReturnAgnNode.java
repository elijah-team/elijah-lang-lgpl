/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/** 
 * Created Mar 13, 2019 at 11:23:02 AM
 *
 */

package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.IExpression;

/**
 * @author Tripleo(sb)
 *
 */
public class ReturnAgnNode {
	
	private LocalAgnTmpNode _latn;
	public ExpressionNode expr;

	public ReturnAgnNode(IExpression expression) {
		// TODO Auto-generated constructor stub
		expr = new ExpressionNode(expression);
	}

	public ReturnAgnNode(LocalAgnTmpNode latn3) {
		// TODO might be wrong
//		throw new NotImplementedException();
		this._latn = latn3;
	}
	
	public IExpressionNode getExpr() {
		if (expr != null)
			return expr;
		return _latn.getRight();//.getExpr();
	}
}

//
//
//
