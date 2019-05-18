/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.gen.nodes;

import org.eclipse.jdt.annotation.NonNull;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.*;

/**
 * @author olu
 *
 */
public class ExpressionNode implements IExpressionNode {

	public String genName;  // TODO since when does expression have a name?
	public String genText;
	public String genType;
    
	public boolean _is_const_expr;
	public OS_Element ref_;
	
	private IExpression iex;
	
	/**
	 * For {@link VariableReferenceNode2}
	 */
	public ExpressionNode() {
		genName = null;
		genText = null;
		genType = null;
		_is_const_expr = false;
		ref_ = null;
		iex = null;
	}
	
	@Override
	public IExpression getExpr() {
		return iex;
	}
	
	
	public ExpressionNode(@NonNull  OS_Integer expr1) {
		// TODO should  be interface
		genName=((Integer)expr1.getValue()).toString(); // TODO likely wrong
		genText=expr1.toString(); // TODO likely wrong
		_is_const_expr = true;
		iex = expr1;
	}

	public ExpressionNode(@NonNull IExpression expr1) {
		// TODO Auto-generated constructor stub
		if (expr1 != null) {
			genName=expr1.toString(); // TODO likely wrong
			genText=expr1.toString(); // TODO likely wrong
			_is_const_expr = expr1.getLeft()  instanceof StringExpression
							|| expr1.getLeft()  instanceof NumericExpression; // TODO more
			iex = expr1;
		}
	}

	@Override
	public boolean is_const_expr() {
		return _is_const_expr;
	}

	@Override
	public boolean is_underscore() {
		// TODO Auto-generated method stub
		if (iex !=null && iex instanceof VariableReference) {
			return ((VariableReference) iex).getName().equals("_");
		}
		return false;
	}

	@Override
	public boolean is_var_ref() {
		return (iex !=null && iex instanceof VariableReference);
	}
	
	@Override
	public boolean is_simple() {
		if (iex !=null && iex instanceof VariableReference) {
			return ((VariableReference) iex).is_simple();
		}
		return is_const_expr() || is_underscore();
	}
	
	@Override
	public String genText(CompilerContext cctx) {
		if (iex instanceof OS_Integer) {
			final int value = ((OS_Integer) iex).getValue();
			return ((Integer) value).toString();
		}
		if (iex instanceof AbstractBinaryExpression) {
			if (iex.getLeft() instanceof VariableReference) {

				final String left_side = ((VariableReference) this.iex.getLeft()).getName();
				String right_side = null;
				
				final AbstractBinaryExpression abe = (AbstractBinaryExpression) this.iex;
				if (abe.getRight() instanceof OS_Integer) {
					right_side = ""+((OS_Integer) abe.getRight()).getValue();
				}
				if (abe.type == ExpressionType.SUBTRACTION) {
					String s = String.format("%s - %s", left_side,	right_side);
					return s;
				}
				if (abe.type == ExpressionType.MULTIPLY) {
					String s = String.format("%s * %s", left_side, right_side);
					//((OS_Integer) abe.getRight()).getValue()*;
					return s;
				}
				
				return "---------------2";
			}
		}
		if (iex instanceof OS_Integer) {
			final int value = ((OS_Integer) iex).getValue();
			return ((Integer) value).toString();
		}
		if (iex instanceof ProcedureCallExpression) {
			return getStringPCE((ProcedureCallExpression) iex);
		}
//		if (iex instanceof VariableReference) {
		return "vai"; // TODO hardcoded
	}
	
	static String getStringPCE(ProcedureCallExpression expr) {
		final StringBuilder sb=new StringBuilder();
		sb.append("z__");
		sb.append(expr.getLeft().toString());
		sb.append("(");
		boolean x=false;
		for (IExpression e : expr.exprList()) {
			sb.append(e.toString());
			sb.append(", ");
			x=true;
		}
		if (x==true) {
			sb.deleteCharAt(sb.length());
			sb.deleteCharAt(sb.length());
		}
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public String genType() {
		return "u64";  // TODO harcoded
	}
	
	@Override
	public String genText() {
		return null;
	}
	
	@Override
	public TypeRef getType() {
		return null;
	}
}

//
//
//
