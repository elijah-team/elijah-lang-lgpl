/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.ProcedureCallExpression;

/**
 * @author Tripleo(sb)
 *
 */
public class TmpSSACtxNode {
	
	public LocalAgnTmpNode _tmp = null;
	private IExpression __expr;
	private final CompilerContext _ctx;
	private IExpressionNode _node;
	
	public TmpSSACtxNode(CompilerContext cctx) {
		// TODO Auto-generated constructor stub
		this._ctx=cctx;
	}

	public String text() {
		return ExpressionNode.getStringPCE((ProcedureCallExpression) getExprType());
		//"--------------------"; // TODO hardcoded
	}
	
	public IExpressionNode getType() {
		if (_node != null)
			return _node;
		return new ExpressionNode(getExprType());
	}

	public CompilerContext getCtx() {
		return _ctx;
	}
	
	public IExpression getExprType() {
		return __expr;
	}
	
	public void setExprType(IExpression __expr) {
		this.__expr = __expr;
	}
	
	public void setExprType(IExpressionNode node) {
		this._node = node;
	}
}

//
//
//
