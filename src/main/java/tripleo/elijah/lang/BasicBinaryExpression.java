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

public class BasicBinaryExpression implements IBinaryExpression, ScopeElement {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		BasicBinaryExpression abe = (BasicBinaryExpression)this;
		if (abe.getKind() == ExpressionKind.ASSIGNMENT) {
			sb.append(abe.getLeft().toString());
			sb.append("=");
			sb.append(abe.getRight().toString());
				
		} else if (abe.getKind() == ExpressionKind.AUG_MULT) {
			sb.append(abe.getLeft().toString());
			sb.append("*=");
			sb.append(abe.getRight().toString());
		}
		return sb.toString();
	}

	public BasicBinaryExpression() {
		left  = null;
		right = null;
		kind  = null;
	}

	public BasicBinaryExpression(IExpression aLeft, ExpressionKind aType, IExpression aRight) {
		left = aLeft;
		kind = aType;
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
	public ExpressionKind getKind() {
		return kind;
	}

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		BasicBinaryExpression abe = (BasicBinaryExpression)this;
		if (abe.getKind() == ExpressionKind.ASSIGNMENT) {
			aTos.put_string_ln("Assignment {");
			aTos.incr_tabs();
			aTos.put_string_ln(abe.getLeft().toString());
			aTos.put_string_ln(abe.getRight().toString());
			aTos.dec_tabs();
			aTos.put_string_ln("}");
		} else if (abe.getKind() == ExpressionKind.AUG_MULT) {
			aTos.put_string_ln("AssignmentMultiplication {");
			aTos.incr_tabs();
			aTos.put_string_ln(abe.getLeft().toString());
			aTos.put_string_ln(abe.getRight().toString());
			aTos.dec_tabs();
			aTos.put_string_ln("}");
		}
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s %s>", left,kind,right);
	}

	@Override
	public void set(IBinaryExpression aEx) {
		left=aEx.getLeft();
		kind=aEx.getKind();
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
	public void shift(ExpressionKind aType) {
		left=new BasicBinaryExpression(left,kind,right); //TODO
		kind=aType;
		right=null;
	}

	public IExpression left;
	public IExpression right;
	public ExpressionKind kind;

	@Override
	public void setKind(ExpressionKind aIncrement) {
		kind=aIncrement;
	}

	public boolean is_simple() {
		throw new IllegalStateException("Implement me");
	}

	OS_Type _type;

	public void setType(OS_Type deducedExpression) {
		_type = deducedExpression;
    }

	public OS_Type getType() {
    	return _type;
	}

}

//
//
//
