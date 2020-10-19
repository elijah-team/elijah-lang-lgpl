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

/**
 * @author Tripleo
 *
 * Created 	Apr 19, 2020 at 00:32:00 AM
 */
public class InvariantStatementPart {
    private final Token name;
    private final InvariantStatement parent;
    private IExpression expr;

    public InvariantStatementPart(final InvariantStatement cr, final Token token) {
        this.parent = cr;
        this.name = token;
    }

    public void setExpr(final IExpression expr) {
        this.expr = expr;
    }
}

//
//
//
