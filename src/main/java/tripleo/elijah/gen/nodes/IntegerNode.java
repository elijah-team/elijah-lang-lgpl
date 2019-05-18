package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Integer;

/*
 * Created on 5/13/2019 at 01:44
 *
 * $$Id$
 *
 */
public class IntegerNode implements IExpressionNode {
	private IExpression _expr;
	private TypeRef _type;
	
	public IntegerNode(OS_Integer aInteger, TypeRef aTypeRef) {
		_expr = aInteger;
		_type = aTypeRef;
	}
	
	@Override
	public IExpression getExpr() {
		return _expr;
	}
	
	@Override
	public boolean is_const_expr() {
		return true;
	}
	
	@Override
	public boolean is_underscore() {
		return false;
	}
	
	@Override
	public boolean is_var_ref() {
		return false;
	}
	
	@Override
	public boolean is_simple() {
		return true;
	}
	
	@Override
	public String genText(CompilerContext cctx) {
		return Integer.toString(((OS_Integer)_expr).getValue());
	}
	
	@Override
	public String genType() {
		return "u64"; // TODO hardcoded
	}
	
	@Override
	public String genText() {
		return Integer.toString(((OS_Integer)_expr).getValue());
	}
	
	@Override
	public TypeRef getType() {
		return _type;
	}
}
