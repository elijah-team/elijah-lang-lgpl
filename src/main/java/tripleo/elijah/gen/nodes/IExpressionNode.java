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
