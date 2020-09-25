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
import tripleo.elijah.contexts.MatchContext;
import tripleo.elijah.contexts.SingleIdentContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Apr 15, 2020 at 10:11:16 PM
 */
public class MatchConditional implements OS_Element, StatementItem, FunctionItem {

	private final SingleIdentContext _ctx;
	private IExpression expr;
	private OS_Element parent;
	private MatchContext __ctx;

	public MatchConditional(OS_Element parent, Context parentContext) {
		this.parent = parent;
		this._ctx = new SingleIdentContext(parentContext, this);
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
//		throw new NotImplementedException();
		return __ctx;
	}

	public void setParent(OS_Element aParent) {
		this.parent = aParent;
	}

	public void postConstruct() {
	}

	public void setContext(MatchContext ctx) {
		__ctx=ctx;
	}

	interface MC1 extends Documentable {
		void add(FunctionItem aItem);

//		void addDocString(String text);
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
			element.addDocString(aS);
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
			return MatchConditional.this;
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(IExpression aExpr) {
			element.add(new StatementWrapper(aExpr, getContext(), MatchConditional.this.getParent()));
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

	public class MatchConditionalPart3 implements MC1 {

		private List<Token> docstrings = null;
		private final List<FunctionItem> items = new ArrayList<FunctionItem>();

		private IdentExpression matching_expression;

		public void expr(IdentExpression expr) {
			this.matching_expression = expr;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}
	}

	public class MatchConditionalPart2 implements MC1 {

		private List<Token> docstrings = new ArrayList<Token>();
		private final List<FunctionItem> items = new ArrayList<FunctionItem>();

		private IExpression matching_expression;

		public void expr(IExpression expr) {
			this.matching_expression = expr;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}
	}

	public class MatchConditionalPart1 implements MC1 {

		private List<Token> docstrings = new ArrayList<Token>();
		private final List<FunctionItem> items = new ArrayList<FunctionItem>();

		TypeName tn /*= new RegularTypeName()*/;
		private IdentExpression ident;

		public void ident(IdentExpression i1) {
			this.ident = i1;
		}

//		public TypeName typeName() {
//			return tn;
//		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}

		public void setTypeName(TypeName typeName) {
			tn = typeName;
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

	public MatchConditionalPart3 valNormal() {
		return new MatchConditionalPart3();
	}

}

//
//
//
