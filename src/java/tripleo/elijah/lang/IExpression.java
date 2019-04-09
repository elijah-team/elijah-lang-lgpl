/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

/*
 * Created on Sep 2, 2005 2:08:03 PM
 * 
 * $Id$
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public interface IExpression {

	void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException;

	ExpressionType getType();

	void set(ExpressionType aIncrement);

	IExpression getLeft();
	void setLeft(IExpression iexpression);

	String repr_();

	IExpression UNASSIGNED = new AbstractBinaryExpression() {
	};

}
