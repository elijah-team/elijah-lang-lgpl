/**
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 30, 2020 at 7:41:52 AM
 */
public class FuncExpr implements IExpression, OS_Element {

//	private final TypeNameList argList = new TypeNameList();
	private final FormalArgList argList = new FormalArgList();
	private final RegularTypeName typeName = new RegularTypeName();

	public void type(TypeModifiers function) {
		// TODO Auto-generated method stub
		
	}

	public FormalArgList argList() {
		return argList;
	}

	public TypeName returnValue() {
		return typeName;
	}

	final FuncExprScope funcExprScope = new FuncExprScope(this);

	public Scope scope() {
		return funcExprScope;
	}

	private List<String> docstrings = new ArrayList<String>();
	private List<FunctionItem> items = new ArrayList<FunctionItem>();

	final static class FuncExprScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);
		private final FuncExpr funcExpr;

		public FuncExprScope(FuncExpr funcExpr) {
			this.funcExpr = funcExpr;
		}

		@Override
		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				funcExpr.items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false FunctionItem %s",
					aItem.getClass().getName()));
		}
		
		@Override
		public void addDocString(Token aS) {
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
		public void statementWrapper(IExpression aExpr) {
			add(new StatementWrapper(aExpr));
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

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	/****** FOR IEXPRESSION ******/
	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.FUNC_EXPR;
	}

	@Override
	public void setKind(ExpressionKind aKind) {
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		return null;
	}

	@Override
	public void setLeft(IExpression iexpression) {
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
	public void setType(OS_Type deducedExpression) {
		throw new NotImplementedException();
	}

	@Override
	public OS_Type getType() {
		throw new NotImplementedException();
//		return null;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
//		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
//		return null;
	}
}

//
//
//
