/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.Collections;
import java.util.Stack;

/**
 * Created 3/7/21 1:13 AM
 */
public class DeduceLookupUtils {
	public static LookupResultList lookupExpression(final IExpression left, final Context ctx) throws ResolveError {
		switch (left.getKind()) {
		case QIDENT:
			final IExpression de = Helpers.qualidentToDotExpression2((Qualident) left);
			return lookupExpression(de, ctx)/*lookup_dot_expression(ctx, de)*/;
		case DOT_EXP:
			return lookup_dot_expression(ctx, (DotExpression) left);
		case IDENT:
			{
				final IdentExpression ident = (IdentExpression) left;
				final LookupResultList lrl = ctx.lookup(ident.getText());
				if (lrl.results().size() == 0) {
					throw new ResolveError(ident,  lrl);
				}
				return lrl;
			}
		default:
			throw new IllegalArgumentException();
		}

	}

	@Nullable
	public static OS_Element _resolveAlias(final AliasStatement aliasStatement) {
		LookupResultList lrl2;
		if (aliasStatement.getExpression() instanceof Qualident) {
			final IExpression de = Helpers.qualidentToDotExpression2(((Qualident) aliasStatement.getExpression()));
			if (de instanceof DotExpression) {
				try {
					lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
				} catch (ResolveError aResolveError) {
					aResolveError.printStackTrace();
					lrl2 = new LookupResultList();
				}
			} else
				lrl2 = aliasStatement.getContext().lookup(((IdentExpression) de).getText());
			return lrl2.chooseBest(null);
		}
		// TODO what about when DotExpression is not just simple x.y.z? then alias equivalent to val
		if (aliasStatement.getExpression() instanceof DotExpression) {
			final IExpression de = aliasStatement.getExpression();
			try {
				lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			} catch (ResolveError aResolveError) {
				aResolveError.printStackTrace();
				lrl2 = new LookupResultList();
			}
			return lrl2.chooseBest(null);
		}
		lrl2 = aliasStatement.getContext().lookup(((IdentExpression) aliasStatement.getExpression()).getText());
		return lrl2.chooseBest(null);
	}

	@Nullable
	public static OS_Element _resolveAlias2(final AliasStatement aliasStatement) throws ResolveError {
		LookupResultList lrl2;
		if (aliasStatement.getExpression() instanceof Qualident) {
			final IExpression de = Helpers.qualidentToDotExpression2(((Qualident) aliasStatement.getExpression()));
			if (de instanceof DotExpression) {
				lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			} else
				lrl2 = aliasStatement.getContext().lookup(((IdentExpression) de).getText());
			return lrl2.chooseBest(null);
		}
		// TODO what about when DotExpression is not just simple x.y.z? then alias equivalent to val
		if (aliasStatement.getExpression() instanceof DotExpression) {
			final IExpression de = aliasStatement.getExpression();
			lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			return lrl2.chooseBest(null);
		}
		lrl2 = aliasStatement.getContext().lookup(((IdentExpression) aliasStatement.getExpression()).getText());
		return lrl2.chooseBest(null);
	}

	private static LookupResultList lookup_dot_expression(Context ctx, final DotExpression de) throws ResolveError {
		final Stack<IExpression> s = dot_expression_to_stack(de);
		OS_Type t = null;
		IExpression ss = s.peek();
		while (/*!*/s.size() > 1/*isEmpty()*/) {
			ss = s.peek();
			if (t != null && (t.getType() == OS_Type.Type.USER_CLASS || t.getType() == OS_Type.Type.FUNCTION))
				ctx = t.getClassOf().getContext();
			t = deduceExpression(ss, ctx);
			if (t == null) break;
			s.pop();
		}
		{
//			s.pop();
			ss = s.peek();
		}
		if (t == null) {
			NotImplementedException.raise();
			return new LookupResultList(); // TODO throw ResolveError
		} else {
			if (t instanceof OS_UnknownType)
				return new LookupResultList(); // TODO is this right??
			final LookupResultList lrl = t.getElement()/*.getParent()*/.getContext().lookup(((IdentExpression) ss).getText());
			return lrl;
		}
	}

	/**
	 * @see {@link tripleo.elijah.stages.deduce.DotExpressionToStackTest}
	 * @param de The {@link DotExpression} to turn into a {@link Stack}
	 * @return a "flat" {@link Stack<IExpression>} of expressions
	 */
	@NotNull
	static Stack<IExpression> dot_expression_to_stack(final DotExpression de) {
		final Stack<IExpression> right_stack = new Stack<IExpression>();
		IExpression right = de.getRight();
		right_stack.push(de.getLeft());
		while (right instanceof DotExpression) {
			right_stack.push(right.getLeft());
			right = ((DotExpression) right).getRight();
		}
		right_stack.push(right);
		Collections.reverse(right_stack);
		return right_stack;
	}

	public static OS_Type deduceExpression(@NotNull final IExpression n, final Context context) throws ResolveError {
		switch (n.getKind()) {
		case IDENT:
			return deduceIdentExpression((IdentExpression) n, context);
		case NUMERIC:
			return new OS_Type(BuiltInTypes.SystemInteger);
		case DOT_EXP:
			final DotExpression de = (DotExpression) n;
			final LookupResultList lrl = lookup_dot_expression(context, de);
			final OS_Type left_type = deduceExpression(de.getLeft(), context);
			final OS_Type right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext());
			NotImplementedException.raise();
			break;
		case PROCEDURE_CALL:
			OS_Type ty = deduceProcedureCall((ProcedureCallExpression) n, context);
			return ty/*n.getType()*/;
		case QIDENT:
			final IExpression expression = Helpers.qualidentToDotExpression2(((Qualident) n));
			return deduceExpression(expression, context);
		}
		return null;
	}

	/**
	 * Try to find the type of a ProcedureCall. Will either be a constructor or function call, most likely
	 *
	 * @param pce the procedure call
	 * @param ctx the context to use for lookup
	 * @return the deduced type or {@code null}. Do not {@code pce.setType}
	 */
	@Nullable
	private static OS_Type deduceProcedureCall(final ProcedureCallExpression pce, final Context ctx) {
		System.err.println("979 Skipping deduceProcedureCall "+pce);
		OS_Element best = null;
		try {
			best = lookup(pce.getLeft(), ctx);
		} catch (ResolveError aResolveError) {
			return null; // TODO should we log this?
		}
		if (best == null) return null;
		int y=2;
		if (best instanceof ClassStatement) {
			return new OS_Type((ClassStatement) best);
		} else if (best instanceof FunctionDef) {
			final FunctionDef fd = (FunctionDef) best;
			if (fd.returnType() != null && !fd.returnType().isNull()) {
				return new OS_Type(fd.returnType());
			}
			return new OS_UnknownType(fd);			// TODO still must register somewhere
		} else if (best instanceof FuncExpr) {
			final FuncExpr funcExpr = (FuncExpr) best;
			if (funcExpr.returnType() != null && !funcExpr.returnType().isNull()) {
				return new OS_Type(funcExpr.returnType());
			}
			return new OS_UnknownType(funcExpr);	// TODO still must register somewhere
		} else {
			System.err.println("992 "+best.getClass().getName());
			throw new NotImplementedException();
		}
	}

	private static OS_Type deduceIdentExpression(final IdentExpression ident, final Context ctx) throws ResolveError {
		// is this right?
		LookupResultList lrl = ctx.lookup(ident.getText());
		OS_Element best = lrl.chooseBest(null);
		while (best instanceof AliasStatement) {
			best = _resolveAlias((AliasStatement) best);
		}
		if (best instanceof ClassStatement) {
			return new OS_Type((ClassStatement) best);
		} else if (best instanceof VariableStatement) {
			final VariableStatement vs = (VariableStatement) best;
			if (!vs.typeName().isNull()) {
				try {
					OS_Module lets_hope_we_dont_need_this = null;
					@NotNull OS_Type ty = DeduceTypes2.resolve_type(lets_hope_we_dont_need_this, new OS_Type(vs.typeName()), ctx);
					return ty;
				} catch (ResolveError aResolveError) {
					// TODO This is the cheap way to do it
					//  Ideally, we would propagate this up the call chain all the way to lookupExpression
					aResolveError.printStackTrace();
				}
				return new OS_Type(vs.typeName());
			} else if (vs.initialValue() != IExpression.UNASSIGNED) {
				return new OS_UnknownType(vs);
//				return deduceExpression(vs.initialValue(), ctx); // infinite recursion
			}
		} else if (best instanceof FunctionDef) {
			final FunctionDef functionDef = (FunctionDef) best;
			return new OS_FuncType(functionDef);
		} else if (best instanceof FormalArgListItem) {
			final FormalArgListItem fali = (FormalArgListItem) best;
			if (!fali.typeName().isNull()) {
				try {
					OS_Module lets_hope_we_dont_need_this = null;
					@NotNull OS_Type ty = DeduceTypes2.resolve_type(lets_hope_we_dont_need_this, new OS_Type(fali.typeName()), ctx);
					return ty;
				} catch (ResolveError aResolveError) {
					// TODO This is the cheap way to do it
					//  Ideally, we would propagate this up the call chain all the way to lookupExpression
					aResolveError.printStackTrace();
				}
				return new OS_Type(fali.typeName());
			} else {
				return new OS_UnknownType(fali);
			}
		}
		throw new ResolveError(ident, lrl);
	}

	static OS_Element lookup(IExpression expression, Context ctx) throws ResolveError {
		switch (expression.getKind()) {
		case IDENT:
			LookupResultList lrl = ctx.lookup(((IdentExpression)expression).getText());
			OS_Element best = lrl.chooseBest(null);
			return best;
		case PROCEDURE_CALL:
			LookupResultList lrl2 = lookupExpression(expression.getLeft(), ctx);
			OS_Element best2 = lrl2.chooseBest(null);
			return best2;
		case DOT_EXP:
			LookupResultList lrl3 = lookupExpression(expression, ctx);
			OS_Element best3 = lrl3.chooseBest(null);
			return best3;
//		default:
//			System.err.println("1242 "+expression);
//			throw new NotImplementedException();
		default:
			throw new IllegalStateException("1242 Unexpected value: " + expression.getKind());
		}
	}
}

//
//
//
