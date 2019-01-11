/*
 * Created on Sep 2, 2005 2:08:03 PM
 * 
 * $Id$
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public interface IExpression {

	void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException;

	IExpression getLeft();

	void setLeft(IExpression iexpression);

	String repr_();

	IExpression UNASSIGNED = new AbstractExpression() {
	};

	ExpressionType getType();

	void set(ExpressionType aIncrement);

}
