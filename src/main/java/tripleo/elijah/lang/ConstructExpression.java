/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 6:47:16 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;


public class ConstructExpression extends AbstractExpression implements StatementItem {
	private final IExpression _expr;
	private final FormalArgList _args;
	private OS_Type _type;

//	public ConstructExpression(Scope aScope) {
//	}

	public ConstructExpression(final IExpression aExpr, final FormalArgList aO) {
		_expr = aExpr;
		_args = aO;
	}

	@Override
	public boolean is_simple() {
		return false;
	}

	@Override
	public void setType(final OS_Type deducedExpression) {
		_type = deducedExpression;
	}

	@Override
	public OS_Type getType() {
		return _type;
	}
}
