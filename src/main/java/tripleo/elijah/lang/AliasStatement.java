/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;

public class AliasStatement {
    private final OS_Element parent;
	private IExpression expr;
	private String name;

    public AliasStatement(OS_Element aParent) {
        this.parent = aParent;
    }

	public void setExpression(IExpression expr) {
		this.expr = expr;
	}

	public void setName(Token i1) {
		this.name = i1.getText();
	}
}

//
//
//
