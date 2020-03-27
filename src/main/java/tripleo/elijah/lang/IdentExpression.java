/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Apr 1, 2019 at 3:21:26 PM
 *
 */
package tripleo.elijah.lang;

import java.io.IOException;

import antlr.Token;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.util.TabbedOutputStream;

/**
 * @author Tripleo(sb)
 *
 */
public class IdentExpression implements IExpression {

	private Token text;
	public  Attached _a;

	public IdentExpression(Token r1) {
		this.text = r1;
		this._a = new Attached(); // remember to do something fancy with Context
	}

	@Override
	public void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException {
		Helpers.printXML(this, tabbedoutputstream);
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.IDENT;
	}

	/**
	 * same as getText()
	 */
	@Override
	public String toString() {
		return getText();
	}

	@Override
	public void set(ExpressionKind aIncrement) {
		// log and ignore
		System.err.println("Trying to set ExpressionType of IdentExpression to "+aIncrement.toString());
	}

	@Override
	public IExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		if (iexpression instanceof IdentExpression) {
			text = ((IdentExpression) iexpression).text;
		} else {
			// NOTE was System.err.println
			throw new IllegalArgumentException("Trying to set left-side of IdentExpression to " + iexpression.toString());
		}
	}

	@Override
	public String repr_() {
		return String.format("IdentExpression(%s %d)", text.getText(), _a.getCode());
	}

	public String getText() {
		return text.getText();
	}

	public boolean is_simple() {
		return true;
	}
}

//
//
//
