package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.*;



/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */

public class NumericExpression implements IExpression {

	int carrier;

	public NumericExpression(int aCarrier) {
		carrier = aCarrier;
	}

	public IExpression getLeft() {
		return this;
	}

	public void setLeft(IExpression aLeft) {
		throw new NotImplementedException(); // TODO
	}

	public void print_osi(TabbedOutputStream aTabbedoutputstream) throws IOException {
		throw new NotImplementedException(); // TODO
	}

	public String repr_() {
		throw new NotImplementedException(); // TODO
//		return null;
	}

	public ExpressionType getType() {
		return ExpressionType.SIMPLE; // TODO
	}

	public void set(ExpressionType aIncrement) {
		throw new NotImplementedException(); // TODO
	}

}
