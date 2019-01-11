/*
 * Created on Sep 2, 2005 2:28:42 PM
 * 
 * $Id$
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tripleo.elijah.lang;

import tripleo.elijah.lang.*;

public class ExpressionBuilder {

	public static IBinaryExpression buildPartial(IExpression aE, ExpressionType aAssignment) {
		// TODO Auto-generated method stub
		return new AbstractExpression(aE, aAssignment, null);
	}

	public static IBinaryExpression build(IExpression aE, ExpressionType aIs_a, IExpression aExpression) {
		return new AbstractExpression(aE, aIs_a, aExpression);
	}

	public static IExpression build(IExpression aE, ExpressionType aPost_decrement) {
		// TODO Auto-generated method stub
		return null;
	}

}

