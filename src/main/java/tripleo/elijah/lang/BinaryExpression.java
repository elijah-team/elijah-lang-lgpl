/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			AbstractExpression, ExpressionType

import antlr.Token;

class BinaryExpression extends AbstractBinaryExpression {

	public BinaryExpression(IExpression aLast_exp, ExpressionKind aType, Token aSide) {
		left = aLast_exp;
		kind = aType;
		StringExpression se=new StringExpression(aSide);
		right = se;
	}

}

