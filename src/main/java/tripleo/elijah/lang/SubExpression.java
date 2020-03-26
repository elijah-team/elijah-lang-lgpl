/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:28:33 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;


public class SubExpression extends AbstractBinaryExpression {

	private IExpression carrier;

	public SubExpression(IExpression ee) {
		carrier=ee;
	}

}
