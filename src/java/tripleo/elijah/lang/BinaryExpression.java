package tripleo.elijah.lang;

import tripleo.elijah.lang.*;

// Referenced classes of package pak2:
//			AbstractExpression, ExpressionType

class BinaryExpression extends AbstractExpression {

	public BinaryExpression(IExpression aLast_exp, ExpressionType aType,
			String aSide) {
		left = aLast_exp;
		type = aType;
		StringExpression se=new StringExpression(aSide);
		right = se;
	}

}

