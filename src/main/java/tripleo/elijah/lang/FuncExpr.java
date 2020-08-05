/**
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
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

	public Scope scope() {
		return new FuncExprScope();
	}

	private List<String> docstrings = new ArrayList<String>();
	private List<FunctionItem> items = new ArrayList<FunctionItem>();

	final class FuncExprScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		@Override
		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false StatementItem %s",
					aItem.getClass().getName()));
		}
		
		@Override
		public void addDocString(Token aS) {
			docstrings.add(aS.getText());
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
			return FuncExpr.this;
		}
		
		
	}

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/****** FOR IEXPRESSION ******/
	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.FUNC_EXPR;
	}

	@Override
	public void setKind(ExpressionKind aKind) {

	}

	@Override
	public IExpression getLeft() {
		return null;
	}

	@Override
	public void setLeft(IExpression iexpression) {

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

	}

	@Override
	public OS_Type getType() {
		return null;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OS_Element getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}

//
//
//
