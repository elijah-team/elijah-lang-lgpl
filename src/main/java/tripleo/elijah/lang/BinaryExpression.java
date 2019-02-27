package tripleo.elijah.lang;

import tripleo.elijah.lang.*;

// Referenced classes of package pak2:
//			AbstractExpression, ExpressionType

class BinaryExpression extends AbstractExpression {

	@Deprecated
	public BinaryExpression(IExpression aLast_exp, ExpressionType aType,
			String aSide) {
		left = aLast_exp;
		type = aType;
		StringExpression se=new StringExpression(aSide);
		right = se;
	}

    public BinaryExpression(IExpression aleft,
                            ExpressionType atype,
                            IExpression aright) {
        left=aleft;
        type=atype;
        right=aright;
    }
}

