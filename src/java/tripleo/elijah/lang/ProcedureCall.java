/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.collections.impl.LList;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			AbstractExpression, ExprListListener, ScopeElement, VariableReference, 
//			IExpression

public class ProcedureCall extends AbstractBinaryExpression implements
		ExprListListener, ScopeElement {

	public ProcedureCall(VariableReference ref) {
		name = ref;
	}

	public void setArgs(LList s) {
		args = s;
	}

	@Override
	public boolean isEmpty() {
		return _args == null;
	}

	@Override
	public void change(IExpression e) {
		if (!isEmpty()) {
			throw new IllegalStateException("_args!=null");
		} else {
			_args = e;
			return;
		}
	}

	@Override
	public void print_osi(TabbedOutputStream tos) {
		try {
			tos.incr_tabs();
			tos.put_string_ln("ProcedureCall {");
			tos.put_string("name = ");
			name.print_osi(tos);
			tos.put_string("name = ");
			tos.dec_tabs();
			tos.put_string_ln("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VariableReference name;

	private LList args;

	private IExpression _args;
}
