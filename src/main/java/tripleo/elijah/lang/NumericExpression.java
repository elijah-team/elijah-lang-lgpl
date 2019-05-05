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

import java.io.IOException;

import antlr.Token;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

public class NumericExpression implements IExpression {

	int carrier;
	private Token n;

//	public NumericExpression(int aCarrier) {
//		carrier = aCarrier;
//	}

	public NumericExpression(Token n) {
		this.n = n;
		carrier = Integer.parseInt(n.getText());
	}

	@Override
	public IExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(IExpression aLeft) {
		throw new NotImplementedException(); // TODO
	}

	@Override
	public void print_osi(TabbedOutputStream aTabbedoutputstream) throws IOException {
		throw new NotImplementedException(); // TODO
	}

	@Override
	public String repr_() {
		return toString();
	}

	@Override
	public ExpressionType getType() {
		return ExpressionType.SIMPLE; // TODO
	}

	@Override
	public void set(ExpressionType aIncrement) {
		throw new NotImplementedException(); // TODO
	}
	
	@Override
	public String toString() {
		return "NumericExpression ("+carrier+")";
	}

	public boolean is_simple() {
		return true;
	}
}
