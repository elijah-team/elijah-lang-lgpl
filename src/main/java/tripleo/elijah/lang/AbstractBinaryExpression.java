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

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			IExpression, ScopeElement

public class AbstractBinaryExpression implements IBinaryExpression, ScopeElement {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		AbstractBinaryExpression abe = (AbstractBinaryExpression)this;
		if (abe.getType() == ExpressionType.ASSIGNMENT) {
			sb.append(abe.getLeft().toString());
			sb.append("=");
			sb.append(abe.getRight().toString());
				
		} else if (abe.getType() == ExpressionType.AUG_MULT) {
			sb.append(abe.getLeft().toString());
			sb.append("*=");
			sb.append(abe.getRight().toString());
		}
		return sb.toString();
	}

	public AbstractBinaryExpression() {
		left  = null;
		right = null;
		type  = null;
	}

	public AbstractBinaryExpression(IExpression aLeft, ExpressionType aType, IExpression aRight) {
		left = aLeft;
		type = aType;
		right = aRight;
	}

	@Override
	public IExpression getLeft() {
		return left;
	}

	@Override
	public IExpression getRight() {
		return right;
	}

	@Override
	public ExpressionType getType() {
		return type;
	}

	@Override
	public void print_osi(TabbedOutputStream $1) throws IOException {
		throw new IllegalStateException("please implement this method");
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s %s>", left,type,right);
	}

	@Override
	public void set(IBinaryExpression aEx) {
		left=aEx.getLeft();
		type=aEx.getType();
		right=aEx.getRight();
	}

	@Override
	public void setLeft(IExpression aLeft) {
		left = aLeft;
	}

	@Override
	public void setRight(IExpression aRight) {
		right = aRight;
	}
	@Override
	public void shift(ExpressionType aType) {
		left=new AbstractBinaryExpression(left,type,right); //TODO
		type=aType;
		right=null;
	}

	public IExpression left;
	public IExpression right;
	public ExpressionType type;

	@Override
	public void set(ExpressionType aIncrement) {
		type=aIncrement;
	}

	public boolean is_simple() {
		// TODO Auto-generated method stub
		return false;
	}
}
