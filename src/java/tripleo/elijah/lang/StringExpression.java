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

import tripleo.elijah.util.TabbedOutputStream;

public class StringExpression extends AbstractBinaryExpression {

public StringExpression(String g) {
set(g);
}

	@Override
	public  void print_osi(TabbedOutputStream tabbedoutputstream)
			throws IOException {
		assert false;
	}

	@Override
	public  IExpression getLeft() {
		assert false;
		return null;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		assert false;
	}

	@Override
	public  IExpression getRight() {
		assert false;
		return null;
	}

	@Override
	public  void setRight(IExpression iexpression){
		assert false;
	}

	@Override
	public  String repr_() {return repr_;}

	public void set(String g) {repr_ = g;}
	String repr_;
}
