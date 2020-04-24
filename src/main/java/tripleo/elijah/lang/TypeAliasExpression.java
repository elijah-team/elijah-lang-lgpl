/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Apr 2, 2019 at 11:04:35 AM
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

/**
 * @author Tripleo(sb)
 *
 */
public class TypeAliasExpression {

	private final OS_Element parent;
	private IdentExpression x;
	private Qualident y;

	public TypeAliasExpression(OS_Element aParent) {
		this.parent = aParent;
	}

    public void make(IdentExpression x, Qualident y) {
		// TODO Auto-generated method stub
		this.x=x;
		this.y=y;
	}
	
	public void setIdent(Token aToken) {
		// TODO
		x = new IdentExpression(aToken);
	}
	
	public void setBecomes(Qualident qq) {
		y=qq;
	}
}

//
//
//
