/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.LogEvent;
import tripleo.elijah.util.NotImplementedException;

import java.util.*;
import java.util.function.Predicate;

/**
 * Make sure all (I)Expressions have valid and correct types.
 *
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 5:39:30 AM
 */
public class DeduceTypes {

	private int nextClassCode() { return module.parent.nextClassCode(); }
	private int nextFunctionCode() { return module.parent.nextFunctionCode(); }

	private final OS_Module module;

	public DeduceTypes(final OS_Module module) {
		if (module == null) throw new IllegalArgumentException("module must not be null");
		this.module = module;
	}

	public void addClass(final ClassStatement klass, final OS_Element parent) {
//		System.out.print("class " + klass.clsName + "{\n");
		klass._a.setCode(nextClassCode());
		//
		// SHOULDN'T BE MODIFYING NAMESPACE HERE. SETTING CODES IS QUESTIONABLE
		//
/*
		parent.getContext().nameTable().add(klass, klass.getName(), new OS_Type(klass)); // TODO cache or memoize, or singleton somehow
*/
		{
			for (final ClassItem element : klass.getItems())
				addClassItem(element, klass);
		}
//		System.out.print("}\n");
	}
	
	/**
	 * 
	 * @param element the element to add 
	 * @param parent could be class or namespace
	 */
	private void addClassItem(final ClassItem element, final OS_Element parent) {
		{
			if (element instanceof FunctionDef) {
				final FunctionDef fd = (FunctionDef) element;
//				System.out.print("void " + fd.funName + "(){\n");  // TODO: _returnType and mFal
				fd._a.setCode(nextFunctionCode());
				//
				// SHOULDN'T BE MODIFYING NAMESPACE HERE. SETTING CODES IS QUESTIONABLE
				//
/*
				parent.getContext().add(fd, fd.funName);
*/
				{
					for (final FunctionItem fi : fd.getItems())
						addFunctionItem(fi, fd);
				}
//				fd.visit(this);
//				System.out.print("\n}\n\n");
			} else if (element instanceof ClassStatement) {
//				((ClassStatement) element).visitGen(this);
				System.err.println("93 " + ((ClassStatement) element).getName()/*element.getClass().getName()*/);
				addClass((ClassStatement) element, parent);
			} else if (element instanceof VariableSequence) {
//				fd._a.setCode(nextFunctionCode());
//				parent._a.getContext().add(element, null);
				for (final VariableStatement ii : ((VariableSequence) element).items())
					if (parent instanceof ClassStatement)
						addClassItem_deduceVariableStatement((ClassStatement) parent, ii);
					else if (parent instanceof NamespaceStatement)
						addClassItem_deduceVariableStatement((NamespaceStatement) parent, ii);
					else
						throw new NotImplementedException();
			} else {
				System.err.println("92 "+element.getClass().getName());
			}
		}
	}
	
	public void addFunctionItem(final FunctionItem element, final FunctionDef parent) {
		addItem((OS_Element) element, parent);
	}

	public void addItem(final OS_Element element, final OS_Element parent) {
		if (element instanceof VariableSequence) {
//			fd._a.setCode(nextFunctionCode());
//			parent._a.getContext().add(element, null);
			for (final VariableStatement ii : ((VariableSequence) element).items()) {
//				addFunctionItem_deduceVariableStatement(parent, ii);
				deduceVariableStatement(ii, parent);
//				ttt=deduceExpression(ii, parent.getContext());
			}
		}
		else if (element instanceof ProcedureCallExpression) {
			final ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("91.5 %s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			addFunctionItem_Loop((Loop) element, (FunctionDef) parent);
		} else if (element instanceof IfConditional) {
			final IExpression expr = ((IfConditional) element).getExpr();
			System.out.println("92 Fount if conditional "+ expr); // TODO lookup expr, wrap with __bool__
			for (final OS_Element item : ((IfConditional) element).getItems()) {
				System.out.println("93 \t"+ item);
				deduceExpression_(expr, ((IfConditional) element).getContext());
/*
				if (item instanceof IExpression)
					deduceExpression_((IExpression)item, ((IfConditional) element).getContext());
				else if (item instanceof ProcedureCallExpression)
					deduceProcedureCall((ProcedureCallExpression) item, ((IfConditional) element).getContext());
*/
				addItem(item, parent);
			}
		}  else if (element instanceof StatementWrapper) {
			final IExpression expr = ((StatementWrapper) element).getExpr();
			switch (expr.getKind()) {
				case ASSIGNMENT:
					NotImplementedException.raise();
					//
					// TODO doesn't take into account assignment operator
					//
					final OS_Type right_type = deduceExpression(((IBinaryExpression) expr).getRight(), parent.getContext());
					((IBinaryExpression) expr).getRight().setType(right_type);
					expr.getLeft().setType(right_type);
					expr.setType(expr.getLeft().getType());
					break;
				case PROCEDURE_CALL:
					deduceProcedureCall((ProcedureCallExpression) expr, parent.getContext());
					break;
				case FUNC_EXPR:
					final OS_FuncExprType t = deduceFuncExpr((FuncExpr) expr, parent.getContext());
					expr.setType(t);
					break;
				default:
					System.out.println(String.format("93 %s %s", expr, expr.getKind()));
					throw new NotImplementedException();
			}
		} else if (element instanceof ClassStatement) {
			//
			// DON'T MODIFY  NAMETABLE
			//

//			parent._a.getContext().nameTable().add((OS_Element) element, ((ClassStatement) element).getName(), new OS_Type((ClassStatement) element));
		} else if (element instanceof CaseConditional) {
			NotImplementedException.raise();
			final CaseConditional cc = (CaseConditional) element;
			deduceExpression_(cc.getExpr(), cc.getContext());
			final HashMap<IExpression, CaseConditional.CaseScope> scopes = cc.getScopes();
			final Set<IExpression> ks = scopes.keySet();
			for (final IExpression k : ks) {
				if (k instanceof IdentExpression) {
					final IdentExpression ident_k = (IdentExpression) k;
					final String identKText = ident_k.getText();
					IExpression found_default = null;
					if (identKText.equals("_")) {
						if (found_default != null)
							module.parent.eee.reportError("Already found default "+ found_default);
						else
							found_default = k;
					} else {
						final LookupResultList lrl = ident_k.getContext().lookup(identKText);
						if (lrl.results().size() == 0) {
							k.setType(cc.getExpr().getType());
							if (found_default != null)
								module.parent.eee.reportError("Already found default "+ found_default);
							else
								found_default = k;
						}
					}
					if (k != null)
						scopes.get(k).setDefault();
				} else {
					final OS_Type t = deduceExpression(k, cc.getContext());
					if (t == null) {
						System.err.println("996 nil type for " + k);
					}
					k.setType(t);
				}
			}
		} else {
			System.out.println("91 "+element);
			throw new NotImplementedException();
		}
	}

	private OS_FuncExprType deduceFuncExpr(final FuncExpr funcExpr, final Context ctx) {
		NotImplementedException.raise();
		return new OS_FuncExprType(funcExpr);
	}

	private void addFunctionItem_Loop(final Loop loop, final FunctionDef parent) {

		if (loop.getType() == LoopTypes2.FROM_TO_TYPE) {
			//
			//   DON'T MODIFY NAMESPACE
			//
//			parent.getContext().add(new IdentExpression(Helpers.makeToken(loop.getIterName())), loop.getIterName());

//			String varname="vt"+loop.getIterName();
			final ToExpression toex = new ToExpression(loop.getFromPart(), loop.getToPart());
			deduceExpression_(toex.getLeft(), parent.getContext());
			deduceExpression_(toex.getRight(), parent.getContext());

			if (loop.getFromPart() instanceof IdentExpression) {
				//
				//   DON'T MODIFY NAMESPACE
				//
//				loop.getContext().add((OS_Element) toex.getLeft(), loop.getIterName(), toex.getLeft().getType());
			} else if (loop.getFromPart() instanceof NumericExpression) {
				System.err.println("2007 "+loop.getFromPart());
			} else {
				System.err.println("2006 "+loop.getFromPart().getClass().getName());
				throw new NotImplementedException();
			}

//			final NumericExpression fromPart = (NumericExpression)loop.getFromPart();
//			if (loop.getToPart() instanceof NumericExpression) {
//				final NumericExpression toPart = (NumericExpression)loop.getToPart();
//
//				System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
//						varname, fromPart.getValue(),
//						varname, toPart.getValue(),  varname));
//			} else if (loop.getToPart() instanceof IdentExpression) {
//				final IdentExpression toPart = (IdentExpression)loop.getToPart();
//
//				System.out.println(String.format("{for (int %s=%d;%s<=%s;%s++){\n\t",
//						varname, fromPart.getValue(),
//						varname, "vv"+toPart.getText(),  varname));
//
//			}
			for (final StatementItem item : loop.getItems()) {
				System.out.println("2007 \t"+item);
				if (item instanceof StatementWrapper) {
					final OS_Type t = deduceExpression(((StatementWrapper) item).getExpr(), parent.getContext());
					System.out.println("2008 \t"+t);
				}
			}
//			System.out.println("}");
		} else if (loop.getType() == LoopTypes2.EXPR_TYPE) {
			addFunctionItem_Loop_EXPR_TYPE(loop, parent);
		} else throw new NotImplementedException();
	}

	private void addFunctionItem_Loop_EXPR_TYPE(final Loop loop, final FunctionDef parent) {
		//
		//   DON'T MODIFY NAMESPACE
		//
/*
		if (loop.getIterName() != null) {
			parent.getContext().add(
					new IdentExpression(Helpers.makeToken(loop.getIterName())),
					loop.getIterName());
		} else {
			System.out.println("loop.getIterName() == null");
//				String varname="vt"+loop.getIterName();
		}
*/
		final ToExpression toex;
		if (loop.getFromPart() == null)
			toex = new ToExpression(new NumericExpression(0), loop.getToPart());
		else
			toex = new ToExpression(loop.getFromPart(), loop.getToPart());
		deduceExpression_(toex.getLeft(), parent.getContext());
		deduceExpression_(toex.getRight(), parent.getContext());

		if (loop.getFromPart() instanceof IdentExpression) {
			//
			//   DON'T MODIFY NAMESPACE
			//
//			loop.getContext().add((OS_Element) toex.getLeft(), loop.getIterName(), toex.getLeft().getType());
			NotImplementedException.raise();
		} else if (loop.getFromPart() == null) {
			System.out.println("88 loop.getFromPart() == null");
		} else
			throw new NotImplementedException();
//			if (loop.getToPart() instanceof NumericExpression) {
//				String varname="vt0_TODO";
//				final NumericExpression toPart = (NumericExpression)loop.getToPart();
//
//				System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
//						varname, 0,
//						varname, toPart.getValue(),  varname));
				for (final StatementItem item : loop.getItems()) {
					System.out.println("\t"+item);
					if (item instanceof VariableSequence) {
//						fd._a.setCode(nextFunctionCode());
//						parent._a.getContext().add(element, null);
						for (final VariableStatement ii : ((VariableSequence) item).items())
							deduceVariableStatement(ii, loop);
					} else if (item instanceof StatementWrapper) {
						final IExpression e = ((StatementWrapper) item).getExpr();
						if (e instanceof BasicBinaryExpression) {
							deduceExpression_(e.getLeft(), loop.getContext());
							deduceExpression_(((BasicBinaryExpression) e).getRight(), loop.getContext());
							e.setType(e.getLeft().getType());
						}
					} else
						throw new NotImplementedException();

				}
//				System.out.println("}");
//			} else if (loop.getToPart() instanceof DotExpression) {
//				System.out.println("94 "+loop.getToPart().getClass().getName());
//				NotImplementedException.raise();
//			} else {
//				System.out.println("95 "+loop.getToPart().getClass().getName());
//				throw new NotImplementedException();
//			}
	}

	private void deduceExpression_(final IExpression expression, final Context context) {
		final OS_Type t = deduceExpression(expression, context);
		expression.setType(t);
	}

	private void deduceProcedureCall(final ProcedureCallExpression pce, final Context ctx) {
//		System.err.println(String.format("75 %s", pce.getType()));
		try {
			if (deduceProcedureCall_LEFT(pce, ctx))
				deduceProcedureCall_ARGS(pce, ctx);
		} finally {
//			System.err.println(String.format("76 %s", pce.getType()));
		}
	}

	/**
	 *
	 * @param pce
	 * @param ctx
	 * @return true if we found a type
	 */
	private boolean deduceProcedureCall_LEFT(final ProcedureCallExpression pce, final Context ctx) {
		final IExpression de;
		if (pce.getLeft() instanceof Qualident) {
			de = Helpers.qualidentToDotExpression2(((Qualident) pce.getLeft()));
			System.out.println("77 "+de);
			pce.setLeft(de);
		} else {
			de = pce.getLeft();
		}

		OS_Type t = null;
		if (de.getType() != null) {
			t = de.getType(); // TODO what about pce.setType?
		} else {
			final LookupResultList lrl;
			if (de instanceof DotExpression)
				lrl = lookup_dot_expression(ctx, (DotExpression) de);
			else if (de instanceof IdentExpression)
				lrl = ctx.lookup(((IdentExpression) de).getText());
			else
				throw new NotImplementedException();
			//
			final List<Predicate> pl = new ArrayList<Predicate>();
//			pl.add(new DeduceUtils.MatchConstructorArgs(pce));
			pl.add(new DeduceUtils.MatchFunctionArgs(pce));
			final OS_Element best = lrl.chooseBest(pl);
			if (best != null){
				if (best instanceof VariableStatement) {
					final TypeName typeName = ((VariableStatement) best).typeName();
					if (typeName != null) {
						// TODO lookup typename.
						t = new OS_Type(typeName);
					} else
						t = deduceTypeName((VariableStatement) best, ctx);
				} else if (best instanceof FormalArgListItem) {
					final NormalTypeName typeName = (NormalTypeName) ((FormalArgListItem) best).tn;
					if (typeName != null) {
						t = new OS_Type(typeName);
					} else
						throw new NotImplementedException();
				} else if (best instanceof ClassStatement) {
					t = new OS_Type((ClassStatement) best);
				} else if (best instanceof FunctionDef) {
					t = deduceFunctionReturnType((FunctionDef) best, ctx);
				}
				if (t == null) {
					System.err.println("189 "+ best.getClass().getName());
					module.parent.eee.reportError("type not specified: "+ getElementName(best));
					NotImplementedException.raise();
					return false;
				}
				if (pce.getLeft() instanceof IdentExpression)
					((IdentExpression) pce.getLeft()).setResolvedElement(best);
				pce.setType(t);
			} else {
				if (!(de instanceof IdentExpression)) System.err.println("1002 "+de.getClass().getName()+" "+de);
				module.parent.eee.reportError(String.format("1001 IDENT not found: %s", de));
				NotImplementedException.raise();
				return false;
			}
		}
		return true;
	}

	@org.jetbrains.annotations.Nullable
	private OS_Type deduceFunctionReturnType(final FunctionDef element, final Context ctx) {
		OS_Type t = null;
		if (true) {
			final NormalTypeName typeName = (NormalTypeName) element.returnType();
			if (typeName != null && typeName.hasResolvedElement())
				return new OS_Type((ClassStatement) typeName.getResolvedElement());
			if (typeName != null && typeName.getName() != null) {
				final LookupResultList lrl3 = ctx.lookup(typeName.getName()); // TODO why not typeName.getContext().lookup(typeName.getName()) ??
				final ClassStatement klass = (ClassStatement) lrl3.chooseBest(null);
				if (klass != null) {
					t = new OS_Type(klass);
					typeName.setResolvedElement(klass);
				} else {
					System.out.println("8003 klass == null for " + typeName.getName());
					// TODO should produce module error here
					//  - module.parent.eee.reportError(...)
				}
			} else {
				t = null; // TODO build a control flow graph her and search for exit types
				for (final FunctionItem item : element.getItems()) {
					if (item instanceof StatementWrapper) {
						NotImplementedException.raise();
					}
				}
			}
		} else {
			t = new OS_FuncType(element);
		}
		return t;
	}

	private LookupResultList lookup_dot_expression(Context ctx, final DotExpression de) {
		final Stack<IExpression> s = dot_expression_to_stack(de);
		OS_Type t = null;
		IExpression ss = s.peek();
		while (!s.isEmpty()) {
			ss = s.peek();
			if (t != null && (t.getType() == OS_Type.Type.USER_CLASS || t.getType() == OS_Type.Type.FUNCTION))
				ctx = t.getClassOf().getContext();
			t = deduceExpression(ss, ctx);
			ss.setType(t);  // TODO should this be here?
			s.pop();
		}
		if (t == null) {
			NotImplementedException.raise();
			return new LookupResultList();
		} else
			return t.getElement().getParent().getContext().lookup(((IdentExpression)ss).getText());
	}

	@NotNull
	private Stack<IExpression> dot_expression_to_stack(final DotExpression de) {
		final Stack<IExpression> s = new Stack<IExpression>();
		final IExpression e = de;
		IExpression left = null;
		s.push(de.getRight());
		while (true) {
			left = e.getLeft();
			s.push(left);
			if (!(left instanceof DotExpression)) break;
		}
		return s;
	}

	private void lookup_and_set(final Context ctx, final IExpression exp, final String function_name) {
		final LookupResultList lrl = ctx.lookup(function_name);
		final OS_Element best = lrl.chooseBest(null);
		if (best == null) {
			module.parent.eee.reportError("function not found " + function_name);
			return;
		}
		{
			if (best instanceof ClassStatement) {
				final OS_Type t = new OS_Type((ClassStatement) best);
				exp.setType(t);
			} else if (best instanceof FunctionDef) {
				final OS_Type t = new OS_FuncType(((FunctionDef) best));
				exp.setType(t);
			} else if (best instanceof VariableStatement) {
				final NormalTypeName typeName = (NormalTypeName) ((VariableStatement) best).typeName();
				final OS_Type t;
				if (typeName.isNull()) {
					//deduceProcedureCall((ProcedureCallExpression) ((VariableStatement) best).initialValue(), ctx);
					t = deduceExpression(((VariableStatement) best).initialValue(), ctx);
					NotImplementedException.raise();
				} else
					t = new OS_Type(typeName);
				exp.setType(t);
			} else if (best instanceof FormalArgListItem) {
				final OS_Type t = new OS_Type(((FormalArgListItem) best).typeName());
				exp.setType(t);
			}
			else
				throw new NotImplementedException();
		}
	}

	private OS_Element lookup_ident_to_element(@NotNull final IdentExpression left) {
		final OS_Element best;
		final String text = left.getText();
		final LookupResultList lrl = left.getContext().lookup(text);
//		System.out.println("198 "+/*n*/iv);
		if (lrl.results().size() == 0) {
			System.err.println("196 no results for " + text);
			return null;
		}
//		for (LookupResult n: lrl.results()) {
//			System.out.println("197 "+n);
//		}

		best = lrl.chooseBest(null);
		if (best != null) {
			left.setResolvedElement(best);
		} else {
			System.err.println("191 too many results");
			return null;
		}
		return best;
	}

	private void deduceVariableStatement_procedureCallExpression(
			final ProcedureCallExpression pce, @NotNull final IdentExpression left, final Context context) {
		final OS_Element best;
		if (!(left.hasResolvedElement())) {
			best = lookup_ident_to_element(left);
		} else {
			best = left.getResolvedElement();
		}

		if (best instanceof FunctionDef) {
			final FunctionDef functionDef = (FunctionDef) best;
			deduceVariableStatement_procedureCallExpression_functionDef(pce, functionDef, context);
		} else if (best instanceof AliasStatement) {
			deduceVariableStatement_procedureCallExpression_aliasStatement((AliasStatement) best);
		} else {
			throw new NotImplementedException();
		}
	}

	private OS_Type deduceVariableStatement_procedureCallExpression_aliasStatement(final AliasStatement best) {
		LogEvent.logEvent(196,  ""+ best);
		final OS_Type t;
		final OS_Element element;
		//
		if (!(best.hasResolvedElement())) {
			element = resolveAlias(best);
			best.setResolvedElement(element);
		} else {
			element = best.getResolvedElement();
		}
		if (element instanceof FunctionDef) {
			t = findFunctionType((FunctionDef) element);
		} else {
			t = deduceExpression(best.getExpression(), best.getContext());
		}
		LogEvent.logEvent(199,  ""+ t);
		return t;
	}

	private void deduceVariableStatement_procedureCallExpression_functionDef(
			final ProcedureCallExpression pce, final FunctionDef functionDef, final Context context) {
		final OS_FuncType deducedExpression = new OS_FuncType(functionDef);
		//
		pce.getLeft().setType(deducedExpression); // TODO how do we know before looking at args?
		final OS_Element best2;
		final NormalTypeName typeName = (NormalTypeName) functionDef.returnType();
		if (typeName.hasResolvedElement()) {
			best2 = typeName.getResolvedElement();
		} else {
			final LookupResultList lrl2 = typeName.getContext().lookup(typeName.getName());
			//chooseBest(null); // TODO not using chooseBest here. see why
			best2 = lrl2.results().get(0).getElement();
			typeName.setResolvedElement(best2);
		}
		pce.setType(new OS_Type((ClassStatement) best2));
		deduceProcedureCall_ARGS(pce, context);
	}

	private OS_Type findFunctionType(final FunctionDef fd) {
		NotImplementedException.raise();
		final TypeName typeName1 = fd.returnType();
		if (typeName1 != null) {
			if (typeName1 instanceof NormalTypeName) {
				final NormalTypeName typeName = (NormalTypeName) typeName1;
				if (typeName.hasResolvedElement()) {
					if (typeName.getResolvedElement() instanceof ClassStatement) {
						return new OS_Type((ClassStatement) typeName.getResolvedElement());
					}
					if (typeName.getResolvedElement() instanceof FunctionDef) {
						return new OS_FuncType((FunctionDef) typeName.getResolvedElement());
					}
				} else {
					final String s = typeName.getName();
					final LookupResultList lrl = fd.getContext().lookup(s); // TODO why not typeName.getContext().lookup ??
					final OS_Element best = lrl.chooseBest(null);
					if (best != null) {
						NotImplementedException.raise();
//						System.err.println("5000 "+best);
						//return best; // TODO dont return here
						final OS_Element x;
						if (best instanceof AliasStatement) {
							x = resolveAlias((AliasStatement) best);
							((AliasStatement) best).setResolvedElement(x);
						} else
							x = best;

						if (x instanceof ClassStatement) {
							return new OS_Type((ClassStatement) x);
						} else
							throw new NotImplementedException();
					} else
						module.parent.eee.reportError("(5001) type not found " + s);
				}
			}
		}
		return null;
	}

	private OS_Element resolveAlias(final AliasStatement aliasStatement) {
		if (aliasStatement.hasResolvedElement())
			return aliasStatement.getResolvedElement();
		final OS_Element x = _resolveAlias(aliasStatement);
		aliasStatement.setResolvedElement(x);
		return x;
	}

	private OS_Element _resolveAlias(final AliasStatement aliasStatement) {
		final LookupResultList lrl2;
		IExpression expression = aliasStatement.getExpression();
		if (expression instanceof Qualident) {
			expression = Helpers.qualidentToDotExpression2(((Qualident) aliasStatement.getExpression()));
		}

		// TODO what about when DotExpression is not just simple x.y.z? then alias equivalent to val
		if (expression instanceof DotExpression) {
			lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) expression);
			return lrl2.chooseBest(null);
		}
		lrl2 = aliasStatement.getContext().lookup(((IdentExpression) expression).getText());
		return lrl2.chooseBest(null);
	}

	private void deduceProcedureCall_ARGS(final ProcedureCallExpression pce, final Context ctx) {
		if (pce.getArgs() == null) return;
		//
		final Collection<IExpression> expressions = pce.getArgs().expressions();
/*
		List<OS_Type> q = expressions.stream()
				.map(n -> deduceExpression(n, ctx))
				.collect(Collectors.toList());
*/
		final Collection<OS_Type> qq = Collections2.transform(expressions, new Function<IExpression, OS_Type>() {
			@Override
			public @Nullable OS_Type apply(@Nullable final IExpression input) {
				return deduceExpression(input, ctx);
			}
		});
//		List<OS_Type> q = Lists.newArrayList(qq);

//		System.out.println("190 " + pce.getArgs()+" "+qq);

		int i = 0;
		for (final OS_Type os_type : qq) {
			((ArrayList<IExpression>) expressions).get(i++).setType(os_type);
		}
	}

	public void addClassItem_deduceVariableStatement(final ClassStatement parent, @NotNull final VariableStatement vs) {
		deduceVariableStatement(vs, parent);
	}

	public void addClassItem_deduceVariableStatement(final NamespaceStatement parent, @NotNull final VariableStatement vs) {
		deduceVariableStatement(vs, parent);
	}

	public void deduceVariableStatement(@NotNull final VariableStatement vs, final OS_Element parent) {
		{
			OS_Type dtype = null;
			if (vs.typeName().isNull()) {
				if (vs.initialValue() != null) {
					final IExpression iv = vs.initialValue();
					if (iv instanceof NumericExpression) {
						dtype = new OS_Type(BuiltInTypes.SystemInteger);
					} else if (iv instanceof IdentExpression) {
						final IdentExpression identExpression = (IdentExpression) iv;
						final LookupResultList lrl = identExpression.getContext().lookup(identExpression.getText());
						for (final LookupResult n: lrl.results()) {
							System.out.println("99 "+n);
						}
					} else if (iv instanceof ProcedureCallExpression) {
						final ProcedureCallExpression pce = (ProcedureCallExpression) iv;
						final IExpression left = pce.getLeft();
						if (left.getKind() == ExpressionKind.IDENT) {
							deduceVariableStatement_procedureCallExpression(pce, (IdentExpression) left, parent.getContext());
							dtype = pce.getType();
						} else if (left.getKind() == ExpressionKind.DOT_EXP) {
							final LookupResultList lrl = lookup_dot_expression(parent.getContext(), (DotExpression) left);
							for (final LookupResult result : lrl.results()) {
								System.err.println("999 "+result);
							}
							final OS_Element best = lrl.chooseBest(null);
							if (best != null) {
								System.err.println("997 "+best);
							} else {
								System.err.println("998 no results for "+left);
							}
						} else
							throw new NotImplementedException();
					}
					if (dtype != null) {
						iv.setType(dtype);
						// TODO plus should we be modifying vs.typeName anyway
//						vs.typeName().setName(new Qualident(dtype.getClassOf().getName()); // TODO no setTypeName
					}
				}
			} else {
				dtype = new OS_Type(vs.typeName());
			}
			System.out.println(String.format("[#deduceVariableStatement] %s %s;", vs.getName(), dtype));
		}
	}

	public OS_Type deduceIdentExpression(@NotNull final IdentExpression n, final Context context) {
		/*if (n.hasResolvedElement()) {
			return (n.getResolvedElement()).getType();
		} else*/ {
			final LookupResultList lrl = n.getContext().lookup(n.getText());
			if (lrl.results().size() == 1) { // TODO the reason were having problems here is constraints vs shadowing
				final OS_Element element = lrl.results().get(0).getElement();
				if (element instanceof VariableStatement) {
					final NormalTypeName tn = (NormalTypeName) ((VariableStatement) element).typeName();
					if (!tn.isNull())
						return new OS_Type(tn);
					else
						return deduceTypeName((VariableStatement) element, context);
				} else if (element instanceof FormalArgListItem) {
					final NormalTypeName typeName = (NormalTypeName) ((FormalArgListItem) element).tn;
					if (typeName != null) {
						final OS_Type t = deduceTypeName(typeName, context);
						return t;
					} else
						return null;
				} else if (element instanceof ClassStatement) {
					n.setResolvedElement(element);
					return new OS_Type((ClassStatement) element);
				} else if (element instanceof FunctionDef) {
					n.setResolvedElement(element);
					return new OS_FuncType((FunctionDef) element);
				}
				System.err.println("89 " + element.getClass().getName());
				module.parent.eee.reportError("type not specified: " + getElementName(element));
				return null;
			} else {
				// TODO what to do here??
				module.parent.eee.reportError("1002 IDENT not found: " + n.getText());
				NotImplementedException.raise();
				return null;
			}
		}
	}

	private OS_Type deduceTypeName(final VariableStatement vs, final Context ctx) {
		if (vs.typeName().isNull())
			if (vs.initialValue() instanceof NumericExpression)
				return new OS_Type(BuiltInTypes.SystemInteger);
			else if (vs.initialValue() instanceof ProcedureCallExpression) {
				deduceProcedureCall((ProcedureCallExpression) vs.initialValue(), ctx);
				return vs.initialValue().getType();
			}
		return null;
	}

	private OS_Type deduceTypeName(final NormalTypeName typeName, final Context ctx) {
//		if (vs.typeName().isNull())
//			if (vs.initialValue() instanceof NumericExpression)
//				return new OS_Type(BuiltInTypes.SystemInteger);
		final LookupResultList lrl = ctx.lookup(typeName.getName());
		final OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			if (best instanceof ClassStatement)
				return new OS_Type((ClassStatement) best);
		}
		return null;
	}

	public OS_Type deduceExpression(@NotNull final IExpression n, final Context context) {
		if (n.getKind() == ExpressionKind.IDENT) {
			return deduceIdentExpression((IdentExpression)n, context);
		} else if (n.getKind() == ExpressionKind.NUMERIC) {
			return new OS_Type(BuiltInTypes.SystemInteger);
		} else if (n.getKind() == ExpressionKind.DOT_EXP) {
			final DotExpression de = (DotExpression) n;
			final LookupResultList lrl = lookup_dot_expression(context, de);
			final OS_Type left_type = deduceExpression(de.getLeft(), context);
			final OS_Type right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext());
			NotImplementedException.raise();
		} else if (n.getKind() == ExpressionKind.PROCEDURE_CALL) {
			deduceProcedureCall((ProcedureCallExpression) n, context);
			return n.getType();
		} else if (n.getKind() == ExpressionKind.QIDENT) {
			final IExpression expression = Helpers.qualidentToDotExpression2(((Qualident) n));
			return deduceExpression(expression, context);
		}
		
		return null;
	}

	private String getElementName(final OS_Element element) {
		if (element instanceof VariableStatement) {
			return "<VariableStatement>";
		} else if (element instanceof FormalArgListItem) {
			return ((FormalArgListItem) element).name.getText();
		} else if (element instanceof OS_Element2) {
			return ((OS_Element2) element).name();
		}
		return "<"+element.getClass().getName()+">";
	}

	private void addImport(final ImportStatement imp, final OS_Module parent) {
//		throw new NotImplementedException();
		//
		// SHOULDN'T BE MODIFYING NAMESPACE HERE
		//
/*
		if (imp.getRoot() == null) {
			for (Qualident q : imp.parts()) {
				module.modify_namespace(imp, q, NamespaceModify.IMPORT);
			}
		}
*/
//		module.
	}

	private void addModuleItem(final ModuleItem element) {
		// TODO indexing, package, alias, pragma
		if (element instanceof ClassStatement) {
			final ClassStatement cl = (ClassStatement) element;
			addClass(cl, module);
		} else if (element instanceof ImportStatement) {
			final ImportStatement imp = (ImportStatement) element;
			addImport(imp, module);
		} else if (element instanceof NamespaceStatement) {
			final NamespaceStatement ns = (NamespaceStatement) element;
			addNamespace(ns, module);
		}
	}
	private void addNamespace(final NamespaceStatement ns, final OS_Module parent) {
//		System.out.print("namespace " + klass.clsName + " {\n");
		ns._a.setCode(nextClassCode());	
//		parent.getContext().nameTable().add(ns, ns.getName(), new OS_Type(ns, OS_Type.Type.USER));
		
		{
			for (final ClassItem element : ns.getItems())
				addClassItem(element, ns);
		}
//		System.out.print("}\n");
	}

	public void deduce() {
		System.out.println("-------------------------------------------");
		if (false) {
			for (final ModuleItem element : module.items) {
				addModuleItem(element);
			}
		} else {
			for (final ClassStatement classStatement : module.entryPoints) {
				final Collection<ClassItem> fn_main = classStatement.findFunction("main");
				for (final ClassItem item : fn_main) {
					if (!(item instanceof FunctionDef)) {
						System.err.println("Not a function "+item);
					} else {
						final FunctionDef fd = (FunctionDef) item;
						if (fd.fal().falis.size() == 0 /*&& fd.returnType().isNull()*/) {
							// TODO we dont know for sure the return type
							// TODO Also check return type is Unit or NoneType
							addClassItem(fd, classStatement);
						}
					}
				}
			}
		}
	}

	public static final class LRUCache<K, V> extends LinkedHashMap<K, V> {
		private final int maxCacheSize;

		public LRUCache(final int initialCapacity, final int maxCacheSize) {
			super(initialCapacity, 0.75F, true);
			this.maxCacheSize = maxCacheSize;
		}

		@Override
		protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
			return this.size() > this.maxCacheSize;
		}

		@Override
		public V put(final K key, final V value) {
//			if (removeEldestEntry())
			return super.put(key, value);
		}
	}

}

//
//
//
