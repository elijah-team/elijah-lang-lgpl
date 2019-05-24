package tripleo.elijah.gen.nodes;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/*
 * Created on 5/18/2019 at 16:36
 *
 * $$Id$
 *
 */
public class Helpers {
	@NotNull
	static ExpressionList LocalAgnTmpNodeToListVarRef(List<LocalAgnTmpNode> of) {
		ExpressionList expl = new ExpressionList();
		for (LocalAgnTmpNode node : of) {
			VariableReference vr = new VariableReference();
			vr.setMain(node.genName());
			expl.add(vr);
//			NotImplementedException.raise();
		}
		return expl;
	}
	
	@NotNull
	static ExpressionType ExpressionOperatorToExpressionType(ExpressionOperators middle) {
		ExpressionType middle1;
		switch (middle) {
			case OP_MINUS:
				middle1 = ExpressionType.SUBTRACTION;
				break;
			case OP_MULT:
				middle1 = ExpressionType.MULTIPLY;
				break;
			default:
				throw new NotImplementedException();
		}
		return middle1;
	}
	
	@NotNull
	public static String getFunctionName(int code, String aStr, ExpressionList expressionList) {
		final StringBuilder sb=new StringBuilder();
		sb.append("z");
		sb.append(code);
		sb.append(aStr);
		sb.append("(");
/*
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
*/
		List<String> ls = new ArrayList<String>();
		for (IExpression e : expressionList) {
			ls.add(e.toString());
		}
		sb.append(String.join(", ", ls));
		sb.append(")");
		return sb.toString();
	}
}
