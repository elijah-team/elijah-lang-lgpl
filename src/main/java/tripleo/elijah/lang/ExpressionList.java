/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// Referenced classes of package pak2:
//			AbstractExpression

public class ExpressionList {

	public IExpression next(IExpression aExpr) {
		assert aExpr != null;
		exprs.add(aExpr);
		return aExpr;
	}

	private final List<IExpression> exprs = new ArrayList<IExpression>();

	public String toString() {
		return exprs.toString();
	}
	
	public void print_osi(TabbedOutputStream tos) throws IOException {
		for (IExpression expr : exprs) {
			tos.put_string_ln(expr.repr_());
		}
	}
}
