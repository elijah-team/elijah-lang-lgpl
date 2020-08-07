/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import antlr.Token;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

	private OS_Module module;

	public DeduceTypes(OS_Module module) {
		if (module == null) throw new IllegalArgumentException("module must not be null");
		this.module = module;
	}

	public void addClass(ClassStatement klass, OS_Element parent) {
//		System.out.print("class " + klass.clsName + "{\n");
		klass._a.setCode(nextClassCode());
		//
		// SHOULDN'T BE MODIFYING NAMESPACE HERE. SETTING CODES IS QUESTIONABLE
		//
/*
		parent.getContext().nameTable().add(klass, klass.getName(), new OS_Type(klass)); // TODO cache or memoize, or singleton somehow
*/
		{
			for (ClassItem element : klass.getItems())
				addClassItem(element, klass);
		}
//		System.out.print("}\n");
	}
	
	/**
	 * 
	 * @param element the element to add 
	 * @param parent could be class or namespace
	 */
	private void addClassItem(ClassItem element, OS_Element parent) {
		{
			if (element instanceof FunctionDef) {
				FunctionDef fd = (FunctionDef) element;
//				System.out.print("void " + fd.funName + "(){\n");  // TODO: _returnType and mFal
				fd._a.setCode(nextFunctionCode());
				//
				// SHOULDN'T BE MODIFYING NAMESPACE HERE. SETTING CODES IS QUESTIONABLE
				//
/*
				parent.getContext().add(fd, fd.funName);
*/
				{
					for (FunctionItem fi : fd.getItems())
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
				for (VariableStatement ii : ((VariableSequence) element).items())
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
	
	public void addFunctionItem(FunctionItem element, FunctionDef parent) {
		if (element instanceof VariableSequence) {
//			fd._a.setCode(nextFunctionCode());
//			parent._a.getContext().add(element, null);
			for (VariableStatement ii : ((VariableSequence) element).items()) {
//				addFunctionItem_deduceVariableStatement(parent, ii);
				deduceVariableStatement(ii, parent);
//				ttt=deduceExpression(ii, parent.getContext());
			}
		}
		else if (element instanceof ProcedureCallExpression) {
			ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("91.5 %s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			addFunctionItem_Loop((Loop) element, parent);
		} else if (element instanceof IfConditional) {
			System.out.println("92 Fount if conditional "+((IfConditional) element).getExpr());
		}  else if (element instanceof StatementWrapper) {
			IExpression expr = ((StatementWrapper) element).getExpr();
			if (expr.getKind() == ExpressionKind.ASSIGNMENT) {
				NotImplementedException.raise();
				//
				// TODO doesn't take into account assignment operator
				//
				final OS_Type right_type = deduceExpression(((IBinaryExpression) expr).getRight(), parent.getContext());
				((IBinaryExpression)expr).getRight().setType(right_type);
				expr.getLeft().setType(right_type);
				expr.setType(expr.getLeft().getType());
			} else if (expr.getKind() == ExpressionKind.PROCEDURE_CALL) {
				deduceProcedureCall((ProcedureCallExpression) expr, parent.getContext());
			} else if (expr.getKind() == ExpressionKind.FUNC_EXPR) {
				OS_FuncExprType t = deduceFuncExpr((FuncExpr) expr, parent.getContext());
				expr.setType(t);
			}  else {
				System.out.println(String.format("93 %s %s", expr, expr.getKind()));
				throw new NotImplementedException();
			}
		} else if (element instanceof ClassStatement) {
			parent._a.getContext().nameTable().add((OS_Element) element, ((ClassStatement) element).getName(), new OS_Type((ClassStatement) element));
		} else if (element instanceof CaseConditional) {
			CaseConditional cc = (CaseConditional) element;
			int y=2;
		} else {
			System.out.println("91 "+element);
			throw new NotImplementedException();
		}
	}

	private OS_FuncExprType deduceFuncExpr(FuncExpr funcExpr, Context ctx) {
		int y=2;
		return new OS_FuncExprType(funcExpr);
	}

	private void addFunctionItem_Loop(Loop loop, FunctionDef parent) {

		if (loop.getType() == LoopTypes2.FROM_TO_TYPE) {
			parent.getContext().add(new IdentExpression(Helpers.makeToken(loop.getIterName())), loop.getIterName());
//			String varname="vt"+loop.getIterName();
			ToExpression toex = new ToExpression(loop.getFromPart(), loop.getToPart());
			deduceExpression_(toex.getLeft(), parent.getContext());
			deduceExpression_(toex.getRight(), parent.getContext());

			if (loop.getFromPart() instanceof IdentExpression)
				loop.getContext().add((OS_Element) toex.getLeft(), loop.getIterName(), toex.getLeft().getType());
			else
				throw new NotImplementedException();

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
//			for (StatementItem item : loop.getItems()) {
//				System.out.println("\t"+item);
//			}
//			System.out.println("}");
		} else if (loop.getType() == LoopTypes2.EXPR_TYPE) {
			addFunctionItem_Loop_EXPR_TYPE(loop, parent);
		} else throw new NotImplementedException();
	}

	private void addFunctionItem_Loop_EXPR_TYPE(Loop loop, FunctionDef parent) {
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
		ToExpression toex;
		if (loop.getFromPart() == null)
			toex = new ToExpression(new NumericExpression(0), loop.getToPart());
		else
			toex = new ToExpression(loop.getFromPart(), loop.getToPart());
		deduceExpression_(toex.getLeft(), parent.getContext());
		deduceExpression_(toex.getRight(), parent.getContext());

		if (loop.getFromPart() instanceof IdentExpression)
			loop.getContext().add((OS_Element) toex.getLeft(), loop.getIterName(), toex.getLeft().getType());
		else if (loop.getFromPart() == null) {
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
				for (StatementItem item : loop.getItems()) {
					System.out.println("\t"+item);
					if (item instanceof VariableSequence) {
//						fd._a.setCode(nextFunctionCode());
//						parent._a.getContext().add(element, null);
						for (VariableStatement ii : ((VariableSequence) item).items())
							deduceVariableStatement(ii, loop);
					} else if (item instanceof StatementWrapper) {
						IExpression e = ((StatementWrapper) item).getExpr();
						if (e instanceof BasicBinaryExpression) {
							deduceExpression_(e.getLeft(), loop.getContext());
							deduceExpression_(((BasicBinaryExpression) e).getRight(), loop.getContext());
							e.setType(e.getLeft().getType());
						}
					}

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

	private void deduceExpression_(IExpression expression, Context context) {
		OS_Type t = deduceExpression(expression, context);
		expression.setType(t);
	}

	static class ExpressionPair {

		private final IExpression left;
		private final IExpression right;

		public ExpressionPair(IExpression left, IExpression right) {
			this.left=left;
			this.right=right;
		}
	}

	private void deduceProcedureCall(ProcedureCallExpression pce, Context ctx) {
//		System.err.println(String.format("75 %s", pce.getType()));
		try {
			if (deduceProcedureCall_LEFT(pce, ctx))
				deduceProcedureCall_ARGS(pce, ctx);
		} finally {
//			System.err.println(String.format("76 %s", pce.getType()));
		}
	}

	private boolean deduceProcedureCall_LEFT(final ProcedureCallExpression pce, Context ctx) {
		IExpression de;
		if (pce.getLeft() instanceof Qualident) {
			de = qualidentToDotExpression2(((Qualident) pce.getLeft()).parts());
			System.out.println("77 "+de);
			pce.setLeft(de);
		} else {
			de = pce.getLeft();
		}

		OS_Type t = null;//deduceExpression(de, ctx);
//		int y=2;
		{
			LookupResultList lrl;
			if (de instanceof DotExpression)
				lrl = lookup_dot_expression(ctx, (DotExpression) de);
			else if (de instanceof IdentExpression)
				lrl = ctx.lookup(((IdentExpression) de).getText());
			else
				throw new NotImplementedException();
			//
			List<Predicate> pl = new ArrayList<Predicate>();
//			pl.add(new DeduceUtils.MatchConstructorArgs(pce));
			pl.add(new DeduceUtils.MatchFunctionArgs(pce));
			final OS_Element best = lrl.chooseBest(pl);
			if (best != null){
				final OS_Element element = best;
				if (element instanceof VariableStatement) {
					if (((VariableStatement) element).typeName() != null) {
						t = new OS_Type(((VariableStatement) element).typeName());
					} else
						t = deduceTypeName((VariableStatement) element, ctx);
				} else if (element instanceof FormalArgListItem) {
					final TypeName typeName = ((FormalArgListItem) element).tn;
					if (typeName != null) {
						t = new OS_Type(typeName);
					} else
						throw new NotImplementedException();
				} else if (element instanceof ClassStatement) {
					t = new OS_Type((ClassStatement) element);
				} else if (element instanceof FunctionDef) {
					t = new OS_FuncType((FunctionDef) element);
				}
				if (t == null) {
					System.err.println("89 "+element.getClass().getName());
					module.parent.eee.reportError("type not specified: "+ getElementName(element));
					NotImplementedException.raise();
					return false;
				}
				pce.setType(t);
			} else {
				if (!(de instanceof IdentExpression)) System.err.println("100 "+de.getClass().getName());
				module.parent.eee.reportError(String.format("1001 mIDENT not found: %s", de));
				NotImplementedException.raise();
				return false;
			}
		}
		return true;
	}

	private LookupResultList lookup_dot_expression(Context ctx, DotExpression de) {
		Stack<IExpression> s = dot_expression_to_stack(de);
		OS_Type t = null;
		IExpression ss = s.peek();
		while (!s.isEmpty()) {
			ss = s.peek();
			if (t != null && t.getType() == OS_Type.Type.USER_CLASS)
				ctx = t.getClassOf().getContext();
			t = deduceExpression(ss, ctx);
			ss.setType(t);  // TODO should this be here?
			s.pop();
		}
		if (t == null) {
			int y=2;
			return new LookupResultList();
		} else
			return t.getElement().getParent().getContext().lookup(((IdentExpression)ss).getText());
	}

	@NotNull
	private Stack<IExpression> dot_expression_to_stack(DotExpression de) {
		Stack<IExpression> s = new Stack<IExpression>();
		IExpression e = de;
		IExpression left = null;
		s.push(de.getRight());
		while (true) {
			left = e.getLeft();
			s.push(left);
			if (!(left instanceof DotExpression)) break;
		}
		return s;
	}

	private void lookup_and_set(Context ctx, IExpression exp, String function_name) {
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
				final TypeName typeName = ((VariableStatement) best).typeName();
				OS_Type t;
				if (typeName.isNull()) {
					//deduceProcedureCall((ProcedureCallExpression) ((VariableStatement) best).initialValue(), ctx);
					t = deduceExpression(((VariableStatement) best).initialValue(), ctx);
					int y=2;
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

	public static IExpression qualidentToDotExpression2(@NotNull List<Token> ts) {
		return qualidentToDotExpression2(ts, 1);
	}
	public static IExpression qualidentToDotExpression2(@NotNull List<Token> ts, int i) {
		if (ts.size() == 1) return new IdentExpression(ts.get(0));
		if (ts.size() == 0) return null;
		IExpression r = new IdentExpression(ts.get(0));
//		int i=1;
		while (ts.size() > i) {
			final IExpression dotExpression = qualidentToDotExpression2(ts.subList(i++, ts.size()), i+1);
			if (dotExpression == null) break;
//			r.setRight(dotExpression);
			r = new DotExpression(r, dotExpression);
		}
		return r;
	}

//	public void addFunctionItem_deduceVariableStatement(@NotNull FunctionDef parent, @NotNull VariableStatement vs) {
//		{
//			OS_Type dtype = null;
//			if (vs.typeName().isNull()) {
//				if (vs.initialValue() != null) {
//					IExpression iv = vs.initialValue();
//					if (iv instanceof NumericExpression) {
//						dtype = new OS_Type(BuiltInTypes.SystemInteger);
//					} else if (iv instanceof IdentExpression) {
//						LookupResultList lrl = parent.getContext().lookup(((IdentExpression) iv).getText());
//						for (LookupResult n: lrl.results()) {
//							System.out.println("99 "+n);
//						}
//					} else if (iv instanceof ProcedureCallExpression) {
//						final ProcedureCallExpression pce = (ProcedureCallExpression) iv;
//						final IExpression left = pce.getLeft();
//						if (left.getKind() == ExpressionKind.IDENT) {
//							addFunctionItem_deduceVariableStatement_procedureCallExpression(parent, iv, pce, (IdentExpression) left);
//						}
//					}
//					if (dtype != null) {
//						iv.setType(dtype);
//						// TODO plus should we be modifying vs.typeName anyway
////						vs.typeName().setName(new Qualident(dtype.getClassOf().getName()); // TODO no setTypeName
//					}
//				}
//			} else {
//				dtype = new OS_Type(vs.typeName());
//				NotImplementedException.raise();
//			}
////100			parent._a.getContext().add(vs, vs.getName(), dtype);
////				String theType;
////				if (ii.typeName().isNull()) {
//////					theType = "int"; // Z0*
////					theType = ii.initialValueType();
////				} else{
////					theType = ii.typeName().getName();
////				}
//			System.out.println(String.format("[#addFunctionItem_deduceVariableStatement] %s %s;", vs.getName(), dtype));
//
////			assert dtype should be String
//		}
//	}

	private void /*addFunctionItem_*/deduceVariableStatement_procedureCallExpression(
			@NotNull final /*FunctionDef*/OS_Element parent, IExpression iv,
			ProcedureCallExpression pce, @NotNull IdentExpression left) {
		final String text = left.getText();
		final LookupResultList lrl = parent.getContext().lookup(text);
		System.out.println("198 "+/*n*/iv);
		if (lrl.results().size() == 0 ) {
			System.err.println("196 no results for "+text);
			return;
		}
		for (LookupResult n: lrl.results()) {
			System.out.println("197 "+n);
//			return ((FunctionDef)n.getElement()).returnType().decoded(); // TODO loookup OS_Type from typename
//			Helpers.printXML(iv, new TabbedOutputStream(System.out));
//			Helpers.printXML(n, new TabbedOutputStream(System.out));
		}

		if (lrl.results().size() == 1) {
			LookupResult n = lrl.results().get(0);
			pce.getLeft().setType(new OS_FuncType((FunctionDef) n.getElement()));
			deduceProcedureCall_ARGS(pce, parent.getContext());
		} else {
			System.err.println("191 too many results");
		}
//		NotImplementedException.raise();
	}

	private void deduceProcedureCall_ARGS(ProcedureCallExpression pce, final Context ctx) {
		final Collection<IExpression> expressions = pce.getArgs().expressions();
/*
		List<OS_Type> q = expressions.stream()
				.map(n -> deduceExpression(n, ctx.getContext()))
				.collect(Collectors.toList());
*/
		Collection<OS_Type> qq = Collections2.transform(expressions, new Function<IExpression, OS_Type>() {
			@Override
			public @Nullable OS_Type apply(@Nullable IExpression input) {
				return deduceExpression(input, ctx);
			}
		});
//		List<OS_Type> q = Lists.newArrayList(qq);

		System.out.println("190 " + qq);

		int i = 0;
		for (OS_Type os_type : qq) {
			((ArrayList<IExpression>) expressions).get(i++).setType(os_type);
		}
	}

	public void addClassItem_deduceVariableStatement(ClassStatement parent, @NotNull VariableStatement vs) {
		deduceVariableStatement(vs, parent);
	}

	public void addClassItem_deduceVariableStatement(NamespaceStatement parent, @NotNull VariableStatement vs) {
		deduceVariableStatement(vs, parent);
	}

	public void deduceVariableStatement(@NotNull VariableStatement vs, OS_Element parent) {
		{
			OS_Type dtype = null;
			if (vs.typeName().isNull()) {
				if (vs.initialValue() != null) {
					IExpression iv = vs.initialValue();
					if (iv instanceof NumericExpression) {
						dtype = new OS_Type(BuiltInTypes.SystemInteger);
					} else if (iv instanceof IdentExpression) {
						LookupResultList lrl = parent.getContext().lookup(((IdentExpression) iv).getText());
						for (LookupResult n: lrl.results()) {
							System.out.println("99 "+n);
						}
					} else if (iv instanceof ProcedureCallExpression) {
						final ProcedureCallExpression pce = (ProcedureCallExpression) iv;
						final IExpression left = pce.getLeft();
						if (left.getKind() == ExpressionKind.IDENT) {
							deduceVariableStatement_procedureCallExpression(parent, iv, pce, (IdentExpression) left);
						}
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
//100			parent._a.getContext().add(vs, vs.getName(), dtype);
//				String theType;
//				if (ii.typeName().isNull()) {
////					theType = "int"; // Z0*
//					theType = ii.initialValueType();
//				} else{
//					theType = ii.typeName().getName();
//				}
			System.out.println(String.format("[#deduceVariableStatement] %s %s;", vs.getName(), dtype));

		}
	}

	private void addClassItem_deduceVariableStatement_procedureCallExpression(
			@NotNull ClassStatement parent, IExpression iv,
			ProcedureCallExpression pce, @NotNull IdentExpression left) {
		final String text = left.getText();
		final LookupResultList lrl = parent.getContext().lookup(text);
		System.out.println("98 "+/*n*/iv);
		if (lrl.results().size() == 0 )
			System.err.println("296 no results for "+text);
		for (LookupResult n: lrl.results()) {
			System.out.println("297 "+n);
//			Helpers.printXML(iv, new TabbedOutputStream(System.out));
		}
		final Collection<IExpression> expressions = pce.getArgs().expressions();
		List<OS_Type> q = expressions.stream()
				                  .map(n -> deduceExpression(n, parent.getContext()))
				                  .collect(Collectors.toList());
		System.out.println("90 "+q);
		NotImplementedException.raise();
	}

	private void addClassItem_deduceVariableStatement_procedureCallExpression(
			@NotNull NamespaceStatement parent, IExpression iv,
			ProcedureCallExpression pce, @NotNull IdentExpression left) {
		final String text = left.getText();
		final LookupResultList lrl = parent.getContext().lookup(text);
		System.out.println("98 "+/*n*/iv);
		if (lrl.results().size() == 0 )
			System.err.println("396 no results for "+text);
		for (LookupResult n: lrl.results()) {
			System.out.println("397 "+n);
//			Helpers.printXML(iv, new TabbedOutputStream(System.out));
		}
		final Collection<IExpression> expressions = pce.getArgs().expressions();
		List<OS_Type> q = expressions.stream()
				                  .map(n -> deduceExpression(n, parent.getContext()))
				                  .collect(Collectors.toList());
		System.out.println("90 "+q);
		NotImplementedException.raise();
	}

//	private void deduceVariableStatement_procedureCallExpression(
//			@NotNull OS_Element parent, IExpression iv,
//			ProcedureCallExpression pce, @NotNull IdentExpression left) {
//		final String text = left.getText();
//		final LookupResultList lrl = parent.getContext().lookup(text);
//		System.out.println("498 "+/*n*/iv);
//		if (lrl.results().size() == 0 )
//			System.err.println("496 no results for "+text);
//		for (LookupResult n: lrl.results()) {
//			System.out.println("497 "+n);
////			Helpers.printXML(iv, new TabbedOutputStream(System.out));
//		}
//		final Collection<IExpression> expressions = pce.getArgs().expressions();
//		List<OS_Type> q = expressions.stream()
//				.map(n -> deduceExpression(n, parent.getContext()))
//				.collect(Collectors.toList());
//		System.out.println("490 "+q);
//		NotImplementedException.raise();
//	}

	public OS_Type deduceIdentExpression(@NotNull IdentExpression n, Context context) {
		LookupResultList lrl = context.lookup(n.getText());
		if (lrl.results().size() == 1) { // TODO the reason were having problems here is constraints vs shadowing
			final OS_Element element = lrl.results().get(0).getElement();
			if (element instanceof VariableStatement) {
				final TypeName tn = ((VariableStatement) element).typeName();
				if (!tn.isNull())
					return new OS_Type(((VariableStatement) element).typeName());
				else
					return deduceTypeName((VariableStatement) element, context);
			} else if (element instanceof FormalArgListItem) {
				final TypeName typeName = ((FormalArgListItem) element).tn;
				if (typeName != null) {
					OS_Type t = deduceTypeName(typeName, context);
					return t;
				} else
					return null;
			} else if (element instanceof ClassStatement) {
				return new OS_Type((ClassStatement) element);
			} else if (element instanceof FunctionDef) {
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

	private OS_Type deduceTypeName(VariableStatement vs, Context ctx) {
		if (vs.typeName().isNull())
			if (vs.initialValue() instanceof NumericExpression)
				return new OS_Type(BuiltInTypes.SystemInteger);
		return null;
	}

	private OS_Type deduceTypeName(TypeName typeName, Context ctx) {
//		if (vs.typeName().isNull())
//			if (vs.initialValue() instanceof NumericExpression)
//				return new OS_Type(BuiltInTypes.SystemInteger);
		LookupResultList lrl = ctx.lookup(typeName.getName());
		OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			if (best instanceof ClassStatement)
				return new OS_Type((ClassStatement) best);
		}
		return null;
	}

	public OS_Type deduceExpression(@NotNull IExpression n, Context context) {
		if (n.getKind() == ExpressionKind.IDENT) {
			return deduceIdentExpression((IdentExpression)n, context);
		} else if (n.getKind() == ExpressionKind.NUMERIC) {
			return new OS_Type(BuiltInTypes.SystemInteger);
		} else if (n.getKind() == ExpressionKind.DOT_EXP) {
			DotExpression de = (DotExpression) n;
			OS_Type left_type = deduceExpression(de.getLeft(), context);
			OS_Type right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext());
			int y=2;
		} else if (n.getKind() == ExpressionKind.PROCEDURE_CALL) {
			deduceProcedureCall((ProcedureCallExpression) n, context);
			return n.getType();
		}
		
		return null;
	}

	private String getElementName(OS_Element element) {
		if (element instanceof VariableStatement) {
			return "<VariableStatement>";
		} else if (element instanceof FormalArgListItem) {
			return ((FormalArgListItem) element).name.getText();
		} else if (element instanceof OS_Element2) {
			return ((OS_Element2) element).name();
		}
		return "<"+element.getClass().getName()+">";
	}

	private void addImport(ImportStatement imp, OS_Module parent) {
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

	private void addModuleItem(ModuleItem element) {
		// TODO indexing, package, alias, pragma
		if (element instanceof ClassStatement) {
			ClassStatement cl = (ClassStatement) element;
			addClass(cl, module);
		} else if (element instanceof ImportStatement) {
			ImportStatement imp = (ImportStatement) element;
			addImport(imp, module);
		} else if (element instanceof NamespaceStatement) {
			NamespaceStatement ns = (NamespaceStatement) element;
			addNamespace(ns, module);
		}
	}
	private void addNamespace(NamespaceStatement ns, OS_Module parent) {
//		System.out.print("namespace " + klass.clsName + " {\n");
		ns._a.setCode(nextClassCode());	
//		parent.getContext().nameTable().add(ns, ns.getName(), new OS_Type(ns, OS_Type.Type.USER));
		
		{
			for (ClassItem element : ns.getItems())
				addClassItem(element, ns);
		}
//		System.out.print("}\n");
	}

	public void deduce() {
		System.out.println("-------------------------------------------");
		for (ModuleItem element : module.items) {
			addModuleItem(element);
		}
	}
}

//
//
//
