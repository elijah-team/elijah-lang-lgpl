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
import tripleo.elijah.contexts.MatchConditionalContext;
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
	private final List<MC1> parts = new ArrayList<MC1>();
	private IExpression expr;
	private OS_Element parent;
	private MatchContext __ctx;

	public MatchConditional(final OS_Element parent, final Context parentContext) {
		this.parent = parent;
		this._ctx = new SingleIdentContext(parentContext, this);
	}

	public List<MC1> getParts() {
		return parts;
	}

	/**
	 * @category OS_Element
	 */
	@Override
	public void visitGen(final ICodeGen visit) {
		throw new NotImplementedException();
	}

	/**
	 * @category OS_Element
	 */
	@Override
	public OS_Element getParent() {
		return this.parent;
	}

	public void setParent(final OS_Element aParent) {
		this.parent = aParent;
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

	public void setContext(final MatchContext ctx) {
		__ctx = ctx;
	}

	public void postConstruct() {
	}

	//
	// EXPR
	//

	public IExpression getExpr() {
		return expr;
	}

	public void expr(final IExpression expr) {
		this.expr = expr;
	}

	//
	//
	//
	public MatchConditionalPart1 typeMatch() {
		final MatchConditionalPart1 p = new MatchConditionalPart1();
		parts.add(p);
		return p;
	}

	public MatchConditionalPart2 normal() {
		final MatchConditionalPart2 p = new MatchConditionalPart2();
		parts.add(p);
		return p;
	}

	public MatchConditionalPart3 valNormal() {
		final MatchConditionalPart3 p = new MatchConditionalPart3();
		parts.add(p);
		return p;
	}

	public interface MC1 extends Documentable {
		void add(FunctionItem aItem);

		Context getContext();

		Iterable<? extends FunctionItem> getItems();
	}

	private final class MatchConditionalScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);
		private final MC1 element;

		public MatchConditionalScope(final MC1 part1) {
			element = part1;
		}

		@Override
		public void add(final StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				element.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false StatementItem %s to MatchConditional",
						aItem.getClass().getName()));
		}

		@Override
		public void addDocString(final Token aS) {
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
		public void statementWrapper(final IExpression aExpr) {
			element.add(new StatementWrapper(aExpr, getContext(), /*MatchConditional.this.*/getParent()));
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

		private final Context ___ctx = new MatchConditionalContext(MatchConditional.this.getContext(), this);

		private final List<FunctionItem> items = new ArrayList<FunctionItem>();
		private List<Token> docstrings = null;
		private IdentExpression matching_expression;

		public void expr(final IdentExpression expr) {
			this.matching_expression = expr;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(final FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public Context getContext() {
			return ___ctx;
		}

		@Override
		public Iterable<? extends FunctionItem> getItems() {
			return items;
		}

		@Override
		public void addDocString(final Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}
	}

	public class MatchConditionalPart2 implements MC1 {

		private final Context ___ctx = new MatchConditionalContext(MatchConditional.this.getContext(), this);

		private final List<FunctionItem> items = new ArrayList<FunctionItem>();
		private List<Token> docstrings = new ArrayList<Token>();
		private IExpression matching_expression;

		@Override
		public List<FunctionItem> getItems() {
			return items;
		}

		public IExpression getMatchingExpression() {
			return matching_expression;
		}

		public void expr(final IExpression expr) {
			this.matching_expression = expr;
		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(final FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(final Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}

		@Override
		public Context getContext() {
			return ___ctx;
		}
	}

	public class MatchConditionalPart1 implements MC1 {

		private final List<FunctionItem> items = new ArrayList<FunctionItem>();
		private final Context ___ctx = new MatchConditionalContext(MatchConditional.this.getContext(), this);

		TypeName tn /*= new RegularTypeName()*/;
		private List<Token> docstrings = new ArrayList<Token>();
		private IdentExpression ident;

		public void ident(final IdentExpression i1) {
			this.ident = i1;
		}

//		public TypeName typeName() {
//			return tn;
//		}

		public Scope scope() {
			return new MatchConditionalScope(this);
		}

		@Override
		public void add(final FunctionItem aItem) {
			items.add(aItem);
		}

		@Override
		public void addDocString(final Token text) {
			if (docstrings == null)
				docstrings = new ArrayList<Token>();
			docstrings.add(text);
		}

		@Override
		public List<FunctionItem> getItems() {
			return items;
		}

		public TypeName getTypeName() {
			return tn;
		}

		public void setTypeName(final TypeName typeName) {
			tn = typeName;
		}

		public IdentExpression getIdent() {
			return ident;
		}

		@Override
		public Context getContext() {
			return ___ctx;
		}
	}

}

//
//
//
