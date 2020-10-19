/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.util.NotImplementedException;

public class NumericExpression implements IExpression {

	int carrier;
	private Token n;

	public NumericExpression(final int aCarrier) {
		carrier = aCarrier;
	}

	public NumericExpression(final Token n) {
		this.n = n;
		carrier = Integer.parseInt(n.getText());
	}

	@Override
	public IExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(final IExpression aLeft) {
		throw new NotImplementedException(); // TODO
	}

	@Override
	public String repr_() {
		return toString();
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.NUMERIC; // TODO
	}

	@Override
	public void setKind(final ExpressionKind aType) {
		// log and ignore
		System.err.println("Trying to set ExpressionType of NumericExpression to "+aType.toString());
	}
	
	@Override
	public String toString() {
		return String.format("NumericExpression (%d)", carrier);
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	OS_Type _type;

	@Override
	public void setType(final OS_Type deducedExpression) {
		_type = deducedExpression;
    }

	@Override
	public OS_Type getType() {
    	return _type;
	}

	public int getValue() {
		return carrier;
	}
}

//
//
//
