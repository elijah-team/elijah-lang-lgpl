/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 8:43:27 PM
 * 
 * $Id$
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.contexts.DefFunctionContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class DefFunctionDef implements ClassItem {
	public static final int DEF_FUN = 1;
	public static final int REG_FUN = 0;
	
	private IExpression _expr;
	private int _type;
	private FormalArgList fal;
	
	public void setExpr(final IExpression aExpr) {
		_expr=aExpr;
	}
	
	public void setType(final int aType) {
		_type = aType;
	}
	
	public void setOpfal(final FormalArgList fal) {
		this.fal=fal;
	}
	
	static class StatementWrapper implements StatementItem, FunctionItem {

		private final IExpression expr;

		public StatementWrapper(final IExpression aexpr) {
			expr = aexpr;
		}

	}
	
	private final class DefFunctionDefScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		@Override
		public void add(final StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				/*getElement().*/items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("105 adding false StatementItem %s to DefFunctionDef",
					aItem.getClass().getName()));
		}
		
		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}

		@Override
		public OS_Element getParent() {
			return DefFunctionDef.this;
		}

		@Override
		public OS_Element getElement() {
			return DefFunctionDef.this;
		}

		@Override
		public void addDocString(final Token aS) {
			docstrings.add(aS.getText());
		}

		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(final IExpression aExpr) {
			add(new StatementWrapper(aExpr));
		}
	}

	private final List<String> docstrings = new ArrayList<String>(); // TODO do we allow this?
	private final List<FunctionItem> items = new ArrayList<FunctionItem>();

	private final FormalArgList mFal = new FormalArgList();
	private final DefFunctionDefScope mScope2 = new DefFunctionDefScope();
	public final Attached _a = new Attached(new DefFunctionContext(this));

	private final OS_Element parent;

	public String funName;
	private TypeName _returnType = null;

	public DefFunctionDef(final OS_Element aStatement) {
		assert aStatement != null;
		parent = aStatement;
		if (aStatement instanceof ClassStatement) {
			((ClassStatement)parent).add(this);
		} else {
			System.err.println("adding DefFunctionDef to "+aStatement.getClass().getName());
		}
	}

	public FormalArgList fal() {
		return mFal;
	}

	public Scope scope() {
		//assert mScope == null;
		return mScope2;
	}

	public void setName(final Token aText) {
		funName = aText.getText();
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO implement me
		throw new NotImplementedException();
	}

	public TypeName returnType() {
		return _returnType;
	}

	public void setReturnType(final TypeName returnType_) {
		_returnType = returnType_;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _a.getContext();
	}

}

//
//
//
