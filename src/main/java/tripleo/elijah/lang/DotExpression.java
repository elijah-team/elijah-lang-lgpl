/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo(envy)
 *
 * Created 	Mar 27, 2020 at 12:59:41 AM
 */
public class DotExpression extends BasicBinaryExpression {

	public DotExpression(IExpression ee, IdentExpression identExpression) {
		left = ee;
		right = identExpression;
		kind = ExpressionKind.DOT_EXP;
	}

	public DotExpression(IExpression ee, IExpression aExpression) {
		left = ee;
		right = aExpression;
		kind = ExpressionKind.DOT_EXP;
	}

	@Override
	public String toString() {
		return String.format("%s.%s", left, right);
	}

}

//
//
//
