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
import tripleo.elijah.contexts.FuncExprContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 30, 2020 at 7:41:52 AM
 */
public class FuncExpr implements IExpression, OS_Element {

	private final List<String> docstrings = new ArrayList<String>();
	private final List<FunctionItem> items = new ArrayList<FunctionItem>();
	//	private final TypeNameList argList = new TypeNameList();
	private final FormalArgList argList = new FormalArgList();
	private final FuncExprScope funcExprScope = new FuncExprScope(this);
	private TypeName _returnType = null/*new RegularTypeName()*/;
	private OS_Type _type;
	private FuncExprContext _ctx;


	public void type(final TypeModifiers function) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	public FormalArgList argList() {
		return argList;
	}

	public TypeName returnType() {
		return _returnType;
	}

	public Scope scope() {
		return funcExprScope;
	}

	public void setReturnType(final TypeName tn) {
		_returnType = tn;
	}

	public List<FunctionItem> getItems() {
		return items;
	}

	public void setContext(final FuncExprContext ctx) {
		_ctx = ctx;
	}

	public void postConstruct() {
		// nop
	}

	public List<FormalArgListItem> getArgs() {
		return argList.falis;
	}

	final static class FuncExprScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);
		private final FuncExpr funcExpr;

		public FuncExprScope(final FuncExpr funcExpr) {
			this.funcExpr = funcExpr;
		}

		@Override
		public void add(final StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				funcExpr.items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false FunctionItem %s",
					aItem.getClass().getName()));
		}
		
		@Override
		public void addDocString(final Token aS) {
			funcExpr.docstrings.add(aS.getText());
		}
		
		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}

		@Override
		public OS_Element getElement() {
			return funcExpr;
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(final IExpression aExpr) {
			add(new StatementWrapper(aExpr, getParent().getContext(), getParent()));
//			throw new NotImplementedException(); // TODO
		}

		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getParent()
		 */
		@Override
		public OS_Element getParent() {
			// TODO Auto-generated method stub
			return funcExpr;
		}
		
		
	}

	/****** FOR IEXPRESSION ******/
	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.FUNC_EXPR;
	}

	@Override
	public void setKind(final ExpressionKind aKind) {
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		return null;
	}

	@Override
	public void setLeft(final IExpression iexpression) {
		throw new NotImplementedException();
	}

	@Override
	public String repr_() {
		return null;
	}

	@Override
	public boolean is_simple() {
		return false;
	}

	/************* FOR THE OTHER ONE ******************/
	@Override
	public void setType(final OS_Type deducedExpression) {
		_type = deducedExpression;
	}

	@Override
	public OS_Type getType() {
		return _type;
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		return null; // getContext().getParent().carrier() except if it is an Expression; but Expression is not an Element
	}

	@Override
	public Context getContext() {
		return _ctx;
	}
}

//
//
//
