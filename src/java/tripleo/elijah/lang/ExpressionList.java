package tripleo.elijah.lang;

import java.util.*;


// Referenced classes of package pak2:
//			AbstractExpression

public class ExpressionList {

	public IExpression next(IExpression aExpr) {
		exprs.add(aExpr);
		return aExpr;
	}

	private final List<IExpression> exprs = new ArrayList<IExpression>();
}
