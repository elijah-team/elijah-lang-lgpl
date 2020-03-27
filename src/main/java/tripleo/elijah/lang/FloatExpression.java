/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on May 19, 2019 23:47
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;

public class FloatExpression implements IExpression {

	float carrier;
	private Token n;

	public FloatExpression(Token n) {
		this.n = n;
		carrier = Float.parseFloat(n.getText());
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
		Helpers.printXML(this, aTabbedoutputstream);
	}

	@Override
	public String repr_() {
		return toString();
	}

	@Override
	public ExpressionType getType() {
		return ExpressionType.FLOAT; // TODO
	}

	@Override
	public void set(ExpressionType aType) {
		// log and ignore
		System.err.println("Trying to set ExpressionType of FloatExpression to "+aType.toString());
	}
	
	@Override
	public String toString() {
		return String.format("FloatExpression (%f)", carrier);
	}

	public boolean is_simple() {
		return true;
	}
}

//
//
//
