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
public class ProcedureCallExpression implements StatementItem, FunctionItem, IExpression {
	
	private IExpression _left;
	private ExpressionList args=new ExpressionList();
	
	public ProcedureCallExpression(Token aToken, ExpressionList aExpressionList, Token aToken1) {
		throw new NotImplementedException();
		
	}
	
	public ProcedureCallExpression() {
		NotImplementedException.raise();
		
	}
	
	public void identifier(Qualident xyz) {
		setLeft(xyz);
	}
	
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

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

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

	@Override
	public void setLeft(IExpression iexpression) {
		_left = iexpression;
	}

	@Override
	public String repr_() {
		// TODO garbage method
		return String.format("ProcedureCallExpression{%s %s}", getLeft(), args.toString()/*getRight()*/);
	}
	
//	/**
//	 * do not call
//	 *
//	 * @return NotImplementedException
//	 */
//	@Override
//	public IExpression getRight() {
//		// TODO fix this
//		throw new NotImplementedException();
////		return /* args */null;
//	}
//
//	/**
//	 * do not call
//	 *
//	 * @param iexpression
//	 * @throws NotImplementedException always
//	 */
//	@Override
//	public void setRight(IExpression iexpression) {
//		// TODO fix this
////		args = iexpression;
//		throw new NotImplementedException();
//	}
	
//	/**
//	 * same as setArgs(ExpressionList)
//	 *
//	 * @param ael the new value
//	 */
//	public void setRight(ExpressionList ael) {
//		args = ael;
//	}
	
	/**
	 * change then argument list all at once
	 *
	 * @param ael the new value
	 */
	public void setArgs(ExpressionList ael) {
		args = ael;
	}
	
//	@Override
//	public void shift(ExpressionType aType) {
//		// TODO Auto-generated method stub
//		throw new NotImplementedException();
//	}
//
//	@Override
//	public void set(IBinaryExpression aEx) {
//		// TODO Auto-generated method stub
//		throw new NotImplementedException();
//	}
	
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

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	
}

//
//
//
