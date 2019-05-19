/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 2, 2005 2:28:42 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

public class ExpressionBuilder {

	public static IBinaryExpression buildPartial(IExpression aE, ExpressionType aType) {
		// TODO Auto-generated method stub
		return new AbstractBinaryExpression(aE, aType, null);
	}

	public static IBinaryExpression build(IExpression aE, ExpressionType aType, IExpression aExpression) {
		return new AbstractBinaryExpression(aE, aType, aExpression);
	}

	public static IExpression build(IExpression aE, ExpressionType aType) {
		// TODO Auto-generated method stub
		return new AbstractExpression(aE, aType) {
			@Override
			public boolean is_simple() {
				return false; // TODO whoa
			}
		};
	}
	
}

//
//
//
