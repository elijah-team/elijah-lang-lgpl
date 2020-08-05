/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;

/**
 * @author Tripleo
 *
 * Created 	Apr 15, 2020 at 10:09:03 PM
 */
public class CaseConditional implements StatementItem, FunctionItem {

    private final OS_Element parent;
    private IExpression expr;

    public CaseConditional(OS_Element parent) {
        this.parent = parent;
    }

    public void expr(IExpression expr) {
		this.expr = expr;
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		throw new NotImplementedException();
	}
}

//
//
//
