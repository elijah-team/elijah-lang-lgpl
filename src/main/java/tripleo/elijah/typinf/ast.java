/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.typinf;

import tripleo.elijah.util.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 9/3/21 2:12 AM
 */
public class ast {
	public abstract static class AstNode {
		/**
		 * Visit all children with a function that takes a child node.
		 */
		void visit_children(Consumer<AstNode> func) {
			for (AstNode child : _children) {
				func.accept(child);
			}
		}

    	// Used by the type inference algorithm.
		public TypInf.Type _type = null;

		// Used by passes that traverse the AST. Each concrete node class lists the
		// sub-nodes it has as children.
		List<AstNode> _children = new ArrayList<>();
	}

	public static class Identifier extends AstNode {
		final String name;

		public Identifier(String aName) {
			name = aName;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * lambda [args] -> expr
	 */
	public static class LambdaExpr extends AstNode {
		public final AstNode expr;
		final List<String> argnames;

		public LambdaExpr(List<String> argnames, AstNode expr) {
			this.argnames = argnames;
			this.expr = expr;
			this._children = List_of(expr);
		}

		@Override
		public String toString() {
			return String.format("Lambda([%s], %s)", Helpers.String_join(", ", argnames), expr);
		}

		/**
			Used by the type inference algorithm to map discovered types for the
			arguments of the lambda. Since we list arguments as names (strings) and
			not ASTNodes, we can't keep their _type on the node.
    	*/
		HashMap<String, TypInf.Type> _arg_types = null;
	}

	/**
	 * Binary operation between expressions.
	 */
	public static class OpExpr extends AstNode {
		final String op;
		public final AstNode left;
		public final AstNode right;

		public OpExpr(String op, AstNode left, AstNode right) {
			this.op = op;
			this.left = left;
			this.right = right;
			this._children = List_of(this.left, this.right);
		}

		@Override
		public String toString() {
			return String.format("(%s %s %s)", left, op, right);
		}
	}

	/**
	 * if ... then ... else ... expression.
	 */
	public static class IfExpr extends AstNode {
		final AstNode ifexpr;
		final AstNode thenexpr;
		final AstNode elseexpr;

		public IfExpr(AstNode ifexpr, AstNode thenexpr, AstNode elseexpr) {
			this.ifexpr = ifexpr;
			this.thenexpr = thenexpr;
			this.elseexpr = elseexpr;
			this._children = List_of(this.ifexpr, this.thenexpr, this.elseexpr);
		}

		@Override
		public String toString() {
			return String.format("If(%s, %s, %s)", this.ifexpr, this.thenexpr, this.elseexpr);
		}
	}

	/**
	 * Application of a function to a sequence of arguments.
	 *
	 *     func is a node, args is a sequence of nodes.
	 */
	public static class AppExpr extends AstNode {
		public final AstNode func;
		public final List<AstNode> args;

		public AppExpr(AstNode func, List<AstNode> args) {
			this.func = func;
			this.args = args;
			this._children = List_of(this.func);
			this._children.addAll(args);
		}

		@Override
		public String toString() {
			return String.format("App(%s, [%s])", func,
					Helpers.String_join(", ", args
							.stream()
							.map(a -> a.toString())
							.collect(Collectors.toList())));
		}
	}

	public static class IntConstant extends AstNode {
		private final int value;

		IntConstant(int aValue) {
			value = aValue;
		}

		public IntConstant(String aValue) {
			value = Integer.valueOf(aValue);
		}

		@Override
		public String toString() {
			return ""+value;
		}
	}

	public static class BoolConstant extends AstNode {
		private final boolean value;

		BoolConstant(boolean aValue) {
			value = aValue;
		}

		public BoolConstant(String aValue) {
			value = Boolean.valueOf(aValue);
		}

		@Override
		public String toString() {
			return ""+value;
		}
	}

	/**
	 * Declaration mapping name = expr.
	 *
	 *     For functions expr is a Lambda node.
	 */
	public static class Decl extends AstNode {
		String name;
		public AstNode expr;

		public Decl(String aName, AstNode aExpr) {
			name = aName;
			expr = aExpr;
			_children = List_of(expr);
		}

		@Override
		public String toString() {
			return String.format("Decl(%s, %s)", name, expr);
		}
	}
}

//
//
//
