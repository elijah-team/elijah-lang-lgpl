/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			Scope

public class IfExpression implements StatementItem {

	public IfExpression(Scope aClosure) {
		this.parent = aClosure;
	}

	public IfExpression else_() {
		return null;
	}

	public IfExpression elseif() {
		return null;
	}

	public void expr(IExpression expr) {
		this.expr = expr;
	}

	public Scope scope() {
		return null;
	}

	private IExpression expr;
	private Scope parent;
}

//
//
//
