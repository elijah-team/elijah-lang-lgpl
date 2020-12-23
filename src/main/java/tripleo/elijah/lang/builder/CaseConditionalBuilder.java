/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang.builder;

import tripleo.elijah.lang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/23/20 5:50 AM
 */
public class CaseConditionalBuilder extends ElBuilder {
	private Context _context;
	private IExpression expr;
	private BaseScope baseScope;
	private List<Part> parts = new ArrayList<Part>();

	class Part {
		IExpression expr;
		BaseScope scope;

		public Part(IExpression expr, BaseScope baseScope) {
			this.expr = expr;
			this.scope = baseScope;
		}
	}

	@Override
	protected CaseConditional build() {
		CaseConditional caseConditional = new CaseConditional(_parent, _context);
		caseConditional.expr(expr);
		for (Part part : parts) {
			Scope sc = caseConditional.scope(part.expr);
			OS_Element built;
			for (ElBuilder item : part.scope.items()) {
				item.setParent(caseConditional);
				item.setContext(caseConditional.getContext());
				built = item.build();
				sc.add((StatementItem) built);
			}
		}
		caseConditional.postConstruct();
		return caseConditional;
	}

	@Override
	protected void setContext(Context context) {
		_context = context;
	}

	public void expr(IExpression expr) {
		this.expr = expr;
	}


	public BaseScope scope(IExpression expr) {
		final BaseScope baseScope = new BaseScope() {
		};
		final Part p = new Part(expr, baseScope);
		parts.add(p);
		return baseScope;
	}
}

//
//
//
