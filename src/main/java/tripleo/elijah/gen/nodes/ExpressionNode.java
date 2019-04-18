/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Integer;
import tripleo.elijah.lang.StringExpression;
import tripleo.elijah.lang.VariableReference;

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
		_is_const_expr = expr1.getLeft()  instanceof StringExpression; // TODO more
		iex = expr1;
	}

	public boolean is_const_expr() {
		return _is_const_expr;
	}

	public boolean is_underscore() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean is_var_ref() {
		return (iex !=null && iex instanceof VariableReference);
	}

}
