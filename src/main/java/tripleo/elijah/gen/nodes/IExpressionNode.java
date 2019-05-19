/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.ProcedureCallExpression;

public interface IExpressionNode {
//	String getStringPCE(ProcedureCallExpression expr);
	
	IExpression getExpr();
	
	boolean is_const_expr();
	
	boolean is_underscore();
	
	boolean is_var_ref();
	
	boolean is_simple();
	
	String genText(CompilerContext cctx);
	
	String genType();
	
	String genText();
	
	TypeRef getType();
}
