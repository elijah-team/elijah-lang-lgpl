/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// TODO is ExpressionList an IExpression?
public class ProcedureCallExpression implements StatementItem, /*FunctionItem,*/ IExpression {
	
	private IExpression _left;
	private ExpressionList args=new ExpressionList();
	
	public ProcedureCallExpression(Token aToken, ExpressionList aExpressionList, Token aToken1) {
		throw new NotImplementedException();
		
	}
	
	public ProcedureCallExpression() {
//		NotImplementedException.raise();
	}
	
	/**
	 * Set  the left hand side of the procedure call expression, ie the method name
	 * 
	 * @param xyz a method name in Qualident form (might come as DotExpression in future)
	 */
	public void identifier(Qualident xyz) {
		setLeft(xyz);
	}
	
	/**
	 * Set  the left hand side of the procedure call expression, ie the method name
	 * 
	 * @param xyz a method name might come as DotExpression or IdentExpression
	 */
	public void identifier(IExpression xyz) {
		setLeft(xyz);
	}
	
	/**
	 * Get the argument list
	 * 
	 * @return the argument list
	 */
	public ExpressionList exprList() {
		return args;
	}
	
	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		// TODO Auto-generated method stub
		tos.incr_tabs();
		tos.put_string_ln("ProcedureCall {");
		tos.put_string("name = ");
//		target.print_osi(tos);
		getLeft().print_osi(tos);
//		tos.put_string(target.toString());
		tos.put_string("args = ");
		args.print_osi(tos);
		tos.dec_tabs();
		tos.put_string_ln("}");
	}

//	@Override
//	public void visitGen(ICodeGen visit) {
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
//	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.PROCEDURE_CALL;
	}

	@Override
	public void set(ExpressionKind aIncrement) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		return _left;
	}

	/**
	 * @see #identifier(Qualident)
	 */
	@Override
	public void setLeft(IExpression iexpression) {
		_left = iexpression;
	}

	@Override
	public String repr_() {
		// TODO garbage method
		return String.format("ProcedureCallExpression{%s %s}", getLeft(), args.toString()/*getRight()*/);
	}
	
	/**
	 * change then argument list all at once
	 *
	 * @param ael the new value
	 */
	public void setArgs(ExpressionList ael) {
		args = ael;
	}
	
	public String getReturnTypeString() {
		return "int"; // TODO hardcoded
	}
	
	public OS_Element getParent() {
		return null;
	}

	public boolean is_simple() {
		return false; // TODO is this correct?
	}
	
	public ExpressionList getArgs() {
		return args;
	}

}

//
//
//
