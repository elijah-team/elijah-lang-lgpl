/**
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
		if (iex instanceof OS_Integer){
			final int value = ((OS_Integer) iex).getValue();
			return ((Integer)value).toString();
		}
		return "vai"; // TODO hardcoded
	}
}
