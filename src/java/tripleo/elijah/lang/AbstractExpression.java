package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			IExpression, ScopeElement

public class AbstractExpression implements IBinaryExpression, ScopeElement {

	public AbstractExpression() {
		left  = null;
		right = null;
		type  = null;
	}

	public AbstractExpression(IExpression aLeft, ExpressionType aType, IExpression aRight) {
		left = aLeft;
		type = aType;
		right = aRight;
	}

	public IExpression getLeft() {
		return left;
	}

	public IExpression getRight() {
		return right;
	}

	public ExpressionType getType() {
		return type;
	}

	public void print_osi(TabbedOutputStream $1) throws IOException {
		throw new IllegalStateException("please implement this method");
	}

	public String repr_() {
		return String.format("<Expression %s %s %s>", left,type,right);
	}

	public void set(IBinaryExpression aEx) {
		left=aEx.getLeft();
		type=aEx.getType();
		right=aEx.getRight();
	}

	public void setLeft(IExpression aLeft) {
		left = aLeft;
	}

	public void setRight(IExpression aRight) {
		right = aRight;
	}
	public void shift(ExpressionType aType) {
		left=new AbstractExpression(left,type,right); //TODO
		type=aType;
		right=null;
	}

	public IExpression left;
	public IExpression right;
	public ExpressionType type;

	public void set(ExpressionType aIncrement) {
		type=aIncrement;
	}
}
