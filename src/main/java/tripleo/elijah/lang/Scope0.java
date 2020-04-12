package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;

public class Scope0 implements Scope {
	private List<String> docstrings = new ArrayList<String>();
	private List<FunctionItem> items = new ArrayList<FunctionItem>();

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
//			docstrings.add(aS.getText());
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
			return null;
		}

		public FormalArgList fal() {
			// TODO Auto-generated method stub
			return new FormalArgList();
		}
		
		
	}