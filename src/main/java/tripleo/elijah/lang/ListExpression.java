/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:28:55 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;


public class ListExpression extends BasicBinaryExpression {

	public ExpressionList contents() {
		return contents;
	}

	ExpressionList contents=new ExpressionList();
	
	public void setContents(final ExpressionList aList) {
		contents = aList;
	}
}
