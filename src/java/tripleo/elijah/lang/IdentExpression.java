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
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

/**
 * @author SBUSER
 *
 */
public class IdentExpression implements IExpression {

	private Token text;

	public IdentExpression(Token r1) {
		this.text = r1;
	}

	@Override
	public void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExpressionType getType() {
		// TODO is this right?
		return ExpressionType.STRING_LITERAL;
	}

	@Override
	public void set(ExpressionType aIncrement) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String repr_() {
		// TODO Auto-generated method stub
		return null;
	}

}

//
//
//
