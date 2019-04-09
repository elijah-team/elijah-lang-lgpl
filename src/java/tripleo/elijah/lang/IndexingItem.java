/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Mar 27, 2019 at 1:49:23 PM
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

/**
 * @author SBUSER
 *
 */
public class IndexingItem {

	private IExpression expr;
	private Token       token;

	public IndexingItem(Token i1, IExpression c1) {
		this.token = i1;
		this.expr  = c1;
	}

}
