/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

//
//
// TODO What the fuck is this?
//
//

package tripleo.elijah.lang;

import tripleo.elijah.util.TabbedOutputStream;
import java.io.IOException;

public abstract class AbstractExpression implements IExpression, ScopeElement {

	public AbstractExpression() {
		left  = null;
		_type  = null;
	}

	public AbstractExpression(IExpression aLeft, ExpressionType aType) {
		left = aLeft;
		_type = aType;
	}

	@Override
	public IExpression getLeft() {
		return left;
	}
	
	@Override
	public ExpressionType getType() {
		return _type;
	}

	@Override
	public void print_osi(TabbedOutputStream $1) throws IOException {
		throw new IllegalStateException("please implement this method");
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s>", left,_type);
	}

//	@Override
//	public void set(IBinaryExpression aEx) {
//		left=aEx.getLeft();
//		type=aEx.getType();
//	}

	@Override
	public void setLeft(IExpression aLeft) {
		left = aLeft;
	}

//	@Override
//	public void shift(ExpressionType aType) {
//		left=new AbstractExpression(left,type,right); //TODO
//		type=aType;
//		right=null;
//	}

	public IExpression left;
	public ExpressionType _type;

	@Override
	public void set(ExpressionType type1) {
		_type=type1;
	}
}

//
//
//
