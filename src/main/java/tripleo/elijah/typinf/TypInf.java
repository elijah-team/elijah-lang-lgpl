/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.typinf;

// Implementation of type inference.
//
// Eli Bendersky [http://eli.thegreenplace.net]
// This code is in the public domain.

import tripleo.elijah.util.Helpers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 9/3/21 12:40 AM
 */
public class TypInf {
	/**
	 * Subclasses of Type represent types for micro-ML.
	 */
	public abstract static class Type {
	}

	static class IntType extends Type {
		@Override
		public String toString() {
			return "Int";
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof IntType || super.equals(obj);
		}
	}

	static class BoolType extends Type {
		@Override
		public String toString() {
			return "Bool";
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof BoolType || super.equals(obj);
		}
	}

	/**
	 * Function (n-ary) type.
	 *
	 * <p>Encapsulates a sequence of argument types and a single return type.</p>
	 */
	static class FuncType extends Type {
		private final List<Type> argtypes;
		private final Type rettype;

		@Override
		public String toString() {
			if (argtypes.size() == 1) {
				return String.format("(%s -> %s)", argtypes.get(0), rettype);
			} else {
				return String.format("((%s) -> %s)",
						Helpers.String_join(", ",
								argtypes
									.stream()
									.map(t -> t.toString())
									.collect(Collectors.toList())
						), rettype);
			}
		}

		public FuncType(List<Type> aArgtypes, Type aRettype) {
			assert aArgtypes.size() > 0;
			this.argtypes = aArgtypes;
			this.rettype = aRettype;
		}

		@Override
		public boolean equals(Object aO) {
			/*
			    def __eq__(self, other):
        			return (type(self) == type(other) and
                		self.rettype == other.rettype and
               	 			all(self.argtypes[i] == other.argtypes[i]
                    			for i in range(len(self.argtypes))))

			*/
			if (this == aO) return true;
			if (aO == null || getClass() != aO.getClass()) return false;
			FuncType funcType = (FuncType) aO;
			return Objects.equals(argtypes, funcType.argtypes) &&
					Objects.equals(rettype, funcType.rettype);
		}

		@Override
		public int hashCode() {
			return Objects.hash(argtypes, rettype);
		}
	}

	static class TypeVar extends Type {
		private String name;

		public TypeVar(String aName) {
			name = aName;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public boolean equals(Object aO) {
			if (this == aO) return true;
			if (aO == null || getClass() != aO.getClass()) return false;

			TypeVar typeVar = (TypeVar) aO;

			return name != null ? name.equals(typeVar.name) : typeVar.name == null;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}
	}

	static class TypingError extends RuntimeException {
		public TypingError(String aS) {
			super(aS);
		}
	}

	// A symbol table is used to map symbols to types throughout the inference
	// process. Example:
	//
	//  > eight = 8
	//  > nine = 9
	//  > foo a = if a == 0 then eight else nine
	//
	// When inferring the type for 'foo', we already have 'eight' and 'nine' assigned
	// to IntType in the symbol table. Also, inside the definition of 'foo' we have
	// 'a' assigned a TypeVar type (since the type of 'a' is initially unknown).

	// Stages:
	//
	// 1. Visit the AST and assign types to all nodes: known types to constant nodes
	//    and fresh typevars to all other nodes. The types are placed in the _type
	//    attribute of each node.
	// 2. Visit the AST again, this time applying type inference rules to generate
	//    equations between the types. The result is a list of equations, all of
	//    which have to be satisfied.
	// 3. Find the most general unifier (solution) for these equations by using
	//    the classical unification algorithm.

	// Global counter to produce unique type names.
	static Counter _typecounter = new Counter();

	static class Counter {
		int i = 0;

		public int next() {
			return i++;
		}
	}

	/**
	 * Creates a fresh typename that will be unique throughout the program.
	 *
	 * @return
	 */
	static String _get_fresh_typename() {
		return String.format("t%d", _typecounter.next());
	}

	/**
	 * This function is useful for determinism in tests.
	 */
	public static void reset_type_counter() {
		_typecounter = new Counter();
	}

	public static void assign_typenames(ast.AstNode node) {
		assign_typenames(node, new HashMap<String, Type>());
	}

	/**
	 * Assign typenames to the given AST subtree and all its children.
	 * <p>
	 * Symtab is the initial symbol table we can query for identifiers found
	 * throughout the subtree. All identifiers in the subtree must be bound either
	 * in symtab or in lambdas contained in the subtree.
	 * <p>
	 * This function doesn't return anything, but it updates the _type property
	 * on the AST nodes it visits.
	 */
	static void assign_typenames(ast.AstNode node, HashMap<String, Type> symtab) throws TypingError {
		if (node instanceof ast.Identifier) {
			ast.Identifier identifier = (ast.Identifier) node;

			// Identifier nodes are treated specially, as they have to refer to
			// previously defined identifiers in the symbol table.
			if (symtab.containsKey(identifier.name)) {
				node._type = symtab.get(identifier.name);
			} else {
				throw new TypingError(String.format("unbound name \"%s\"", identifier.name));
			}
		} else if (node instanceof ast.LambdaExpr) {
			ast.LambdaExpr lambdaExpr = (ast.LambdaExpr)node;

			lambdaExpr._type = new TypeVar(_get_fresh_typename());
			HashMap<String, Type> local_symtab = new HashMap<>();
			for (String argname: lambdaExpr.argnames) {
				String typename = _get_fresh_typename();
				local_symtab.put(argname, new TypeVar(typename));
			}
			lambdaExpr._arg_types = local_symtab;
			assign_typenames(lambdaExpr.expr, dict_combine(symtab, local_symtab));
		} else if ((node instanceof ast.OpExpr)) {
			node._type = new TypeVar(_get_fresh_typename());
			node.visit_children(c -> assign_typenames(c, symtab));
		} else if ((node instanceof ast.IfExpr)) {
			node._type = new TypeVar(_get_fresh_typename());
			node.visit_children(c -> assign_typenames(c, symtab));
		} else if ((node instanceof ast.AppExpr)) {
			node._type = new TypeVar(_get_fresh_typename());
			node.visit_children(c -> assign_typenames(c, symtab));
		} else if ((node instanceof ast.IntConstant)) {
			node._type = new IntType();
		} else if ((node instanceof ast.BoolConstant)) {
			node._type = new BoolType();
		} else {
			throw new TypingError(String.format("unknown node %s", node.getClass().getName()));
		}
	}

	/**
	 * Show a type assignment for the given subtree, as a table.
	 *
	 * @param node the given subtree
	 * @return Returns a string that shows the assigmnent.
	 */
	String show_type_assignment(ast.AstNode node) {
		List<String> lines = new LinkedList<>();

		show_rec(node, lines);
		return Helpers.String_join("\n", lines);
	}

	void show_rec(ast.AstNode node, List<String> lines) {
		lines.add(String.format("%60s %s", node, node._type));
		node.visit_children(node1 -> show_rec(node1, lines));
	}

	/**
	 * A type equation between two types: left and right.
	 *
	 *     orig_node is the original AST node from which this equation was derived, for
	 *     debugging.
	 */
	public static class TypeEquation {
		public final Type left;
		public final Type right;
		final ast.AstNode orig_node;

		public TypeEquation(Type aLeft, Type aRight, ast.AstNode aOrig_node) {
			left = aLeft;
			right = aRight;
			orig_node = aOrig_node;
		}

		@Override
		public String toString() {
			return String.format("%s :: %s [from %s]", left, right, orig_node);
		}
	}

	/**
	 * Generate type equations from node and place them in type_equations.
	 *
	 *     Prior to calling this functions, node and its children already have to
	 *     be annotated with _type, by a prior call to assign_typenames.
	 *
	 * @param node
	 */
	public static void generate_equations(ast.AstNode node, List<TypeEquation> type_equations) {
		if ((node instanceof ast.IntConstant)) {
			type_equations.add(new TypeEquation(node._type, new IntType(), node));
		} else if ((node instanceof ast.BoolConstant)) {
			type_equations.add(new TypeEquation(node._type, new BoolType(), node));
		} else if ((node instanceof ast.Identifier)) {
			// Identifier references add no equations.
			//pass
		} else if ((node instanceof ast.OpExpr)) {
			ast.OpExpr opExpr = (ast.OpExpr) node;

			node.visit_children(c -> generate_equations(c, type_equations));
			// All op arguments are integers.
			type_equations.add(new TypeEquation(opExpr.left._type, new IntType(), node));
			type_equations.add(new TypeEquation(opExpr.right._type, new IntType(), node));
			// Some ops return boolean, and some return integer.
			if (List_of("!=", "==", ">=", "<=", ">", "<").contains(opExpr.op)) {
				type_equations.add(new TypeEquation(node._type, new BoolType(), node));
			} else {
				type_equations.add(new TypeEquation(node._type, new IntType(), node));
			}
		} else if (node instanceof ast.AppExpr) {
			ast.AppExpr appExpr = (ast.AppExpr) node;

			node.visit_children(c -> generate_equations(c, type_equations));
			List<Type> argtypes = appExpr.args
					.stream()
					.map(name -> appExpr._type)
					.collect(Collectors.toList());

			// An application forces its function's type.
			type_equations.add(new TypeEquation(appExpr.func._type,
					new FuncType(argtypes, node._type),
					node));
		} else if (node instanceof ast.IfExpr) {
			ast.IfExpr ifExpr = (ast.IfExpr) node;

			ifExpr.visit_children(c -> generate_equations(c, type_equations));
			type_equations.add(new TypeEquation(ifExpr.ifexpr._type, new BoolType(), node));
			type_equations.add(new TypeEquation(ifExpr._type, ifExpr.thenexpr._type, node));
			type_equations.add(new TypeEquation(ifExpr._type, ifExpr.elseexpr._type, node));
		} else if (node instanceof ast.LambdaExpr) {
			ast.LambdaExpr lambdaExpr = (ast.LambdaExpr) node;

			node.visit_children(c -> generate_equations(c, type_equations));
			List<Type> argtypes = lambdaExpr.argnames
					.stream()
					.map(name -> lambdaExpr._arg_types.get(name))
					.collect(Collectors.toList());
			type_equations.add(
					new TypeEquation(node._type,
							new FuncType(argtypes, lambdaExpr.expr._type), node));
		} else {
			throw new TypingError(String.format("unknown node %s", node.getClass().getName()));
		}

	}

	/**
	 * Unify two types typ_x and typ_y, with initial subst.
	 *
	 * 		Returns a subst (map of name->Type) that unifies typ_x and typ_y, or None if
	 * 		they can't be unified. Pass subst={} if no subst are initially
	 * 		known. Note that {} means valid (but empty) subst.
	 *
	 * @param typ_x
	 * @param typ_y
	 * @param subst
	 * @return
	 */
	static HashMap<String, Type> unify(Type typ_x, Type typ_y, HashMap<String, Type> subst) {
		if (subst == null) {
			return null;
		} else if  (typ_x.equals(typ_y)) {
			return subst;
		} else if  ((typ_x instanceof TypeVar)) {
			return unify_variable((TypeVar) typ_x, typ_y, subst);
		} else if  ((typ_y instanceof TypeVar)) {
			return unify_variable((TypeVar) typ_y, typ_x, subst);
		} else if  ((typ_x instanceof FuncType) && (typ_y instanceof FuncType)) {
			FuncType funcType_x = (FuncType) typ_x;
			FuncType funcType_y = (FuncType) typ_y;

			if (funcType_x.argtypes.size() != (funcType_y.argtypes.size())) {
				return null;
			} else {
				subst = unify(funcType_x.rettype, funcType_y.rettype, subst);
				for (int i = 0; i < funcType_x.argtypes.size(); i++) {
					subst = unify(funcType_x.argtypes.get(i), funcType_y.argtypes.get(i), subst);
				}
				return subst;
			}
		} else {
			return null;
		}
	}

	/**
	 * Does the variable v occur anywhere inside typ?
	 *
	 *     Variables in typ are looked up in subst and the check is applied
	 *     recursively.
	 *
	 * @param v
	 * @param subst
	 * @return
	 */
	static boolean occurs_check(TypeVar v, Object typ, HashMap<String, Type> subst) {
		if (v.equals(typ)) {
			return true;
		} else if (typ instanceof TypeVar && subst.containsKey(((TypeVar) typ).name)) {
			TypeVar typeVar = (TypeVar) typ;

			return occurs_check(v, subst.get(typeVar.name), subst);
		} else if (typ instanceof FuncType) {
			FuncType funcType = (FuncType)typ;

			if (occurs_check(v, funcType.rettype, subst)) return true;

			for (Type arg : funcType.argtypes) {
				if (occurs_check(v, arg, subst)) return true;
			}
		} else {
			return false;
		}
		return false;
	}


	/**
	 * Unifies variable v with type typ, using subst.
	 *
	 *     Returns updated subst or None on failure.
	 * @param v
	 * @param typ
	 * @param subst
	 * @return
	 */
	static HashMap<String, Type> unify_variable(TypeVar v, Type typ, HashMap<String, Type> subst){
		if (subst.containsKey(v.name)) {
			return unify(subst.get(v.name), typ, subst);
		} else if  ((typ instanceof TypeVar) && subst.containsKey(((TypeVar)typ).name)) {
			return unify(v, subst.get(((TypeVar)typ).name), subst);
		} else if  (occurs_check(v, typ, subst)) {
			return null;
		} else {
			// v is not yet in subst and can't simplify x. Extend subst.
			return dict_combine(subst, mapping(v.name, typ));
        }
	}

	static <K, V> HashMap<K, V> mapping(K k, V v) {
		HashMap<K, V> r = new HashMap<K, V>();
		r.put(k, v);
		return r;
	}

	/**
	 * Unifies all type equations in the sequence eqs.
	 *
	 *     Returns a substitution (most general unifier).
	 *
	 * @param eqs
	 * @return
	 */
	public static HashMap<String, Type> unify_all_equations(List<TypeEquation> eqs){
		HashMap<String, Type> subst = new HashMap<>();
		for (TypeEquation eq : eqs){
			subst = unify(eq.left, eq.right, subst);
			if (subst == null) {
				break;
			}
		}
		return subst;
    }

	/**
	 *     Applies the unifier subst to typ.
	 *
	 *     Returns a type where all occurrences of variables bound in subst
	 *     were replaced (recursively); on failure returns None.
	 *
	 * @param typ
	 * @param subst
	 * @return
	 */
	static Type apply_unifier(Type typ, HashMap<String, Type> subst) {
		if (subst == null) {
			return null;
		} else if ((subst.size()) == 0) {
			return typ;
		} else if (typ instanceof BoolType || typ instanceof IntType) {
			return typ;
		} else if (typ instanceof TypeVar) {
			if (subst.containsKey(((TypeVar) typ).name))
				return apply_unifier(subst.get(((TypeVar) typ).name), subst);
			else
				return typ;
		} else if ((typ instanceof FuncType)) {
			List<Type> newargtypes = ((FuncType) typ).argtypes
					.stream()
					.map(arg -> apply_unifier(arg, subst))
					.collect(Collectors.toList());
			return new FuncType(newargtypes,
					apply_unifier(((FuncType) typ).rettype, subst));
		} else {
			return null;
		}
	}


	Type get_expression_type(ast.AstNode expr, HashMap<String, Type> subst) {
    	return get_expression_type(expr, subst, false);
	}

	/**
	 *     Finds the type of the expression given a substitution.
	 *
	 *     If rename_types is True, renames all the type vars to be sequential
	 *     characters starting from 'a', so that 't5 -> t3' will be renamed to
	 *     'a -> b'. These names are less cluttery and also facilitate testing.
	 *
	 *     Note: expr should already be annotated with assign_typenames.
	 *
	 * @param expr
	 * @param subst
	 * @param rename_types
	 * @return
	 */
	public static Type get_expression_type(ast.AstNode expr, HashMap<String, Type> subst, boolean rename_types) {
		Type typ = apply_unifier(expr._type, subst);
		if (rename_types) {
			Counter namecounter = new Counter();
			HashMap<String, String> namemap = new HashMap<String, String>();
			rename_type(typ, namemap, namecounter);
		}
		return typ;
	}

	static void rename_type(Type typ, HashMap<String, String> namemap, Counter namecounter) {
		if (typ instanceof TypeVar) {
			if (namemap.containsKey(((TypeVar) typ).name)) {
				((TypeVar) typ).name = namemap.get(((TypeVar) typ).name);
			} else {
				String name = String.format("a%d", namecounter.next());

				namemap.put(((TypeVar) typ).name, name);
				namemap.put(name, name);
				((TypeVar) typ).name = namemap.get(((TypeVar) typ).name);
			}
		} else if (typ instanceof FuncType) {
			rename_type(((FuncType)typ).rettype, namemap, namecounter);
			for (Type argtyp : ((FuncType)typ).argtypes) {
				rename_type(argtyp, namemap, namecounter);
			}
		}
	}

	private static HashMap<String, Type> dict_combine(HashMap<String, Type> a, HashMap<String, Type> b) {
		HashMap<String, Type> r = new HashMap<String, Type>();
		for (Map.Entry<String, Type> entry : a.entrySet()) {
			r.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Type> entry : b.entrySet()) {
			r.put(entry.getKey(), entry.getValue());
		}
		return r;
	}

}

//
//
//
