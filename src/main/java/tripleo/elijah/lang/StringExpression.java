/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import antlr.Token;
import tripleo.elijah.util.TabbedOutputStream;

public class StringExpression extends AbstractExpression {

public StringExpression(Token g) {
set(g.getText());
}

	public boolean is_simple() {
		return true;
	}

	/*@Override*/
	public  void print_osi(TabbedOutputStream tabbedoutputstream)
			throws IOException {
		assert false;
	}

	@Override
	public  IExpression getLeft() {
		assert false;
		return this;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		throw new IllegalArgumentException("Should use set()");
	}

	@Override
	public  String repr_() {return repr_;}

	public void set(String g) {repr_ = g;}
	String repr_;
}
