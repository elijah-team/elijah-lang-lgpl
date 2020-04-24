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
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Apr 15, 2020 at 10:11:16 PM
 */
public class MatchConditional implements OS_Element {

	private IExpression expr;
	private OS_Element parent;

	/**
	 * @category OS_Element
	 * @param aTos
	 * @throws IOException
	 */
	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		throw new NotImplementedException();
	}

	/**
	 * @category OS_Element
	 */
	@Override
	public void visitGen(ICodeGen visit) {
		throw new NotImplementedException();
	}

	/**
	 * @category OS_Element
	 */
	@Override
	public OS_Element getParent() {
		return this.parent;
	}

	/**
	 * @category OS_Element
	 * @return
	 */
	@Override
	public Context getContext() {
		throw new NotImplementedException();
//		return null;
	}

	public void setParent(OS_Element aParent) {
		this.parent = aParent;
	}

	interface MC1 {
		void add(FunctionItem aItem);

		void addDocString(String text);
	}

	private final class MatchConditionalScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);
		private final MC1 element;

		public MatchConditionalScope(MC1 part1) {
			element = part1;
		}

		@Override
		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				element.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false StatementItem %s to MatchConditional",
						aItem.getClass().getName()));
		}

		@Override
		public void addDocString(Token aS) {
			element.addDocString(aS.getText());
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
			element.add(new StatementWrapper(aExpr));
		}

		@Override
		public TypeAliasExpression typeAlias() {
			throw new NotImplementedException();
//			return null;
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getParent()
		 */
		@Override
		public OS_Element getParent() {
			return MatchConditional.this;
		}
	}

	public class MatchConditionalPart2 implements MC1 {

		private List<String> docstrings = new ArrayList<String>();
		private List<FunctionItem> items = new ArrayList<FunctionItem>();

		private IExpression expr2;

		public void expr(IExpression expr) {
			this.expr2 = expr;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(String text) {
			docstrings.add(text);
		}
	}

	public class MatchConditionalPart1 implements MC1 {

		private List<String> docstrings = new ArrayList<String>();
		private List<FunctionItem> items = new ArrayList<FunctionItem>();

		RegularTypeName tn = new RegularTypeName();
		private Token ident;

		public void ident(Token i1) {
			this.ident = i1;
		}

		public TypeName typeName() {
			return tn;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(String text) {
			docstrings.add(text);
		}
	}

	public void expr(IExpression expr) {
		this.expr = expr;
	}

	public MatchConditionalPart1 typeMatch() {
		return new MatchConditionalPart1();
	}

	public MatchConditionalPart2 normal() {
		return new MatchConditionalPart2();
	}

}

//
//
//
