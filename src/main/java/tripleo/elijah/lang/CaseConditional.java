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

import antlr.Token;
import tripleo.elijah.contexts.CaseContext;
import tripleo.elijah.contexts.SingleIdentContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Apr 15, 2020 at 10:09:03 PM
 */
public class CaseConditional implements OS_Element, StatementItem, FunctionItem {

    private final OS_Element parent;
    private IExpression expr;
	private SingleIdentContext _ctx = null;
	private HashMap<IExpression, CaseScope> scopes = new LinkedHashMap<IExpression, CaseScope>();
	private CaseScope default_case_scope = null;
	private CaseContext __ctx = null; // TODO look into removing this

	public CaseConditional(OS_Element parent, Context parentContext) {
        this.parent = parent;
        this._ctx = new SingleIdentContext(parentContext, this);
    }

    public void expr(IExpression expr) {
		this.expr = expr;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		throw new NotImplementedException();
	}

	public HashMap<IExpression, CaseScope> getScopes() {
		return scopes;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _ctx;
	}

	private void addScopeFor(IExpression expression, CaseScope caseScope) {
		if (scopes.containsKey(expression))
			System.err.println("already has an expression" + expression); // TODO put in some verify step
		scopes.put(expression, caseScope);
	}

	public void postConstruct() {
		// nop
	}

	public IExpression getExpr() {
		return expr;
	}

	public void setContext(CaseContext ctx) {
		__ctx = ctx;
	}

	public class CaseScope implements OS_Container, OS_Element {

		private final IExpression expr;
		private List<OS_Element> _items = new ArrayList<OS_Element>();
		private ArrayList<Token> mDocs = null;
		private boolean _isDefault = false;

		public CaseScope(IExpression expression) {
			this.expr = expression;
			addScopeFor(expr, this);
		}

		@Override
		public List<OS_Element2> items() {
			return null;
		}

		@Override
		public void add(OS_Element anElement) {
			_items.add(anElement);
		}

		public List<OS_Element> getItems() {
			return _items;
		}

		@Override
		public void addDocString(Token s1) {
			if (mDocs == null)
				mDocs = new ArrayList<Token>();
			mDocs.add(s1);
		}

		@Override
		public void visitGen(ICodeGen visit) {
			throw new NotImplementedException();
		}

		@Override
		public OS_Element getParent() {
			return CaseConditional.this;
		}

		@Override
		public Context getContext() {
			return getParent().getContext();
		}

		public void setDefault() {
			_isDefault = true;
			default_case_scope = this;
			_ctx.carrier = (IdentExpression) expr;
		}
	}

	public Scope scope(IExpression expression) {
		return new AbstractBlockScope(new CaseScope(expression)) {
			@Override
			public Context getContext() {
				return CaseConditional.this.getContext();
			}
		};
	}
}

//
//
//
