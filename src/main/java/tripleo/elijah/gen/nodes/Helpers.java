package tripleo.elijah.gen.nodes;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.ExpressionList;
import tripleo.elijah.lang.ExpressionType;
import tripleo.elijah.lang.VariableReference;
import tripleo.elijah.util.NotImplementedException;

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
}
