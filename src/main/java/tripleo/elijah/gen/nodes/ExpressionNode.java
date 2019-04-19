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
import tripleo.elijah.lang.*;

/**
 * @author olu
 *
 */
public class ExpressionNode {

	public String genName;  // TODO since when does expression have a name?
	public String genText;
	public String genType;
    
	public boolean _is_const_expr;
	public OS_Element ref_;
	
	public IExpression getExpr() {
		return iex;
	}
	
	private IExpression iex;
	
	public ExpressionNode(OS_Integer expr1) {
		// TODO should  be interface
		genName=((Integer)expr1.getValue()).toString(); // TODO likely wrong
		genText=expr1.toString(); // TODO likely wrong
		_is_const_expr = true;
		iex = expr1;
	}

	public ExpressionNode(IExpression expr1) {
		// TODO Auto-generated constructor stub
		genName=expr1.toString(); // TODO likely wrong
		genText=expr1.toString(); // TODO likely wrong
		_is_const_expr = expr1.getLeft()  instanceof StringExpression
						|| expr1.getLeft()  instanceof NumericExpression; // TODO more
		iex = expr1;
	}

	public boolean is_const_expr() {
		return _is_const_expr;
	}

	public boolean is_underscore() {
		// TODO Auto-generated method stub
		if (iex !=null && iex instanceof VariableReference) {
			return ((VariableReference) iex).getName().equals("_");
		}
		return false;
	}

	public boolean is_var_ref() {
		return (iex !=null && iex instanceof VariableReference);
	}
	
	public boolean is_simple() {
		if (iex !=null && iex instanceof VariableReference) {
			return ((VariableReference) iex).is_simple();
		}
		return is_const_expr() || is_underscore();
	}
	
	public String genText(CompilerContext cctx) {
		if (iex instanceof OS_Integer) {
			final int value = ((OS_Integer) iex).getValue();
			return ((Integer) value).toString();
		}
		if (iex instanceof AbstractBinaryExpression) {
			if (iex.getLeft() instanceof VariableReference) {

				final AbstractBinaryExpression abstractBinaryExpression = (AbstractBinaryExpression) this.iex;
				if (abstractBinaryExpression.getRight() instanceof OS_Integer) {
					if (abstractBinaryExpression.type == ExpressionType.SUBTRACTION) {
						String s = String.format("%s - %d",
								((VariableReference) this.iex.getLeft()).getName(),
								((OS_Integer) abstractBinaryExpression.getRight()).getValue());
						return s;
					}
				}
				
				return "---------------2";

			}
		}
		if (iex instanceof OS_Integer) {
			final int value = ((OS_Integer) iex).getValue();
			return ((Integer) value).toString();
		}
		if (iex instanceof ProcedureCallExpression) {
			final StringBuilder sb=new StringBuilder();
			sb.append("z__");
			sb.append(iex.getLeft().toString());
			sb.append("(");
			for (IExpression e : ((ProcedureCallExpression) iex).exprList()) {
			
			}
			sb.append(")");
			return "-------------------3";
		}
		return "vai"; // TODO hardcoded
	}
	
	public String genType() {
		return "u64";  // TODO harcoded
	}
}

//
//
//
