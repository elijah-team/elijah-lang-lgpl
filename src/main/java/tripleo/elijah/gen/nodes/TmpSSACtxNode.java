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
 * @author SBUSER
 *
 */
public class TmpSSACtxNode {
	
	public IExpression __expr;
	private final CompilerContext _ctx;
	
	public TmpSSACtxNode(CompilerContext cctx) {
		// TODO Auto-generated constructor stub
		this._ctx=cctx;
	}

	public String text() {
		return ExpressionNode.getStringPCE((ProcedureCallExpression) __expr);
		//"--------------------"; // TODO hardcoded
	}
	
	public ExpressionNode getType() {
		return new ExpressionNode(__expr);
	}

	public CompilerContext getCtx() {
		return _ctx;
	}
}

//
//
//
