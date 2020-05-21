/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.expand;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tripleo
 *
 * Created 	Apr 24, 2020 at 21:50
 */
public class ExpandFunctions {

	private final OS_Module module;

	public ExpandFunctions(OS_Module module) {
		this.module = module;
	}

	public void addClass(ClassStatement klass, OS_Module parent) {
//		System.out.print("class " + klass.clsName + "{\n");
		{
			for (ClassItem element : klass.items())
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
				{
					for (FunctionItem fi : fd.getItems())
						addFunctionItem(fi, fd);
				}
//				System.out.print("\n}\n\n");
			} else if (element instanceof ClassStatement) {
//				((ClassStatement) element).visitGen(this);
				System.err.println("91 " + element.getClass().getName());
				addClassItem(element, parent);
			} else {
				System.err.println("90 "+element.getClass().getName());
			}
		}
	}
	
	public void addFunctionItem(FunctionItem element, FunctionDef parent) {
		final FunctionContext fc = (FunctionContext) parent.getContext();
		if (element instanceof VariableSequence) {
			for (VariableStatement ii : ((VariableSequence) element).items()) {
//				addFunctionItem_deduceVariableStatement(parent, ii);
				fc.introduceVariable(ii);
			}
		} else if (element instanceof ProcedureCallExpression) {
			ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("%s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			addFunctionItem_Loop((Loop) element, parent, fc);
		}  else if (element instanceof StatementWrapper) {
			IExpression expr = ((StatementWrapper) element).getExpr();
			if (expr.getKind() == ExpressionKind.ASSIGNMENT) {
				FunctionPrelimInstruction fi = fc.introduceVariable(expr.getLeft());
				if (((IBinaryExpression)expr).getRight().getKind() == ExpressionKind.IDENT) {
					FunctionPrelimInstruction fi2 = fc.introduceVariable(((IBinaryExpression) expr).getRight());
					FunctionPrelimInstruction fi3 = fc.assign(fi, fi2);
				} else
					throw new NotImplementedException();
			} else if (expr.getKind() == ExpressionKind.PROCEDURE_CALL) {
				final FunctionPrelimInstruction fi = expandProcedureCall((ProcedureCallExpression) expr, parent.getContext(), fc);
//				final ExpressionList args = ((ProcedureCallExpression) expr).getArgs();
//				fc.makeProcCall(fi, args);
				int y=2;
			} else throw new NotImplementedException();
		} else if (element instanceof IfConditional) {
		} else if (element instanceof ClassStatement) {
			parent._a.getContext().nameTable().add((OS_Element) element, ((ClassStatement) element).getName(), new OS_Type((ClassStatement) element));
		} else {
			System.out.println("91 "+element);
			throw new NotImplementedException();
		}
	}

	private void addFunctionItem_Loop(Loop loop, FunctionDef parent, FunctionContext fc) {

		if (loop.getType() == LoopTypes2.FROM_TO_TYPE) {
			parent.getContext().add(ie(loop.getIterName()), ie(loop.getIterName()));
//			String varname="vt"+loop.getIterName();
			ToExpression toex = new ToExpression(loop.getFromPart(), loop.getToPart());
			deduceExpression_(toex.getLeft(), parent.getContext(), fc);
			deduceExpression_(toex.getRight(), parent.getContext(), fc);

			if (loop.getFromPart() instanceof IdentExpression)
				loop.getContext().add((OS_Element) toex.getLeft(), ie(loop.getIterName()), toex.getLeft().getType());
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
			addFunctionItem_Loop_EXPR_TYPE(loop, parent, fc);
		} else throw new NotImplementedException();
	}

	@NotNull
	private IdentExpression ie(String s) {
		return new IdentExpression(Helpers.makeToken(s));
	}

	private void addFunctionItem_Loop_EXPR_TYPE(Loop loop, FunctionDef parent, FunctionContext fc) {
		if (loop.getIterName() != null) {
			parent.getContext().add(
					ie(loop.getIterName()),
					ie(loop.getIterName()));
		} else {
			System.out.println("loop.getIterName() == null");
//				String varname="vt"+loop.getIterName();
		}
		ToExpression toex;
		if (loop.getFromPart() == null)
			toex = new ToExpression(new NumericExpression(0), loop.getToPart());
		else
			toex = new ToExpression(loop.getFromPart(), loop.getToPart());
		deduceExpression_(toex.getLeft(), parent.getContext(), fc);
		deduceExpression_(toex.getRight(), parent.getContext(), fc);

		if (loop.getFromPart() instanceof IdentExpression)
			loop.getContext().add((OS_Element) toex.getLeft(), ie(loop.getIterName()), toex.getLeft().getType());
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
							deduceVariableStatement(loop, ii, fc);
					} else if (item instanceof StatementWrapper) {
						IExpression e = ((StatementWrapper) item).getExpr();
						if (e instanceof BasicBinaryExpression) {
							deduceExpression_(e.getLeft(), loop.getContext(), fc);
							deduceExpression_(((BasicBinaryExpression) e).getRight(), loop.getContext(), fc);
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

	private void deduceExpression_(IExpression expression, Context context, FunctionContext fc) {
		FunctionPrelimInstruction fi = deduceExpression(expression, context, fc);
		//expression.setType(t);
		int y=2;
	}
	private FunctionPrelimInstruction expandProcedureCall(ProcedureCallExpression pce, Context ctx, FunctionContext fc) {
		FunctionPrelimInstruction i;
		if (pce.getLeft().getKind() == ExpressionKind.PROCEDURE_CALL) {
			i =  expandProcedureCall((ProcedureCallExpression) pce.getLeft(), ctx, fc);
			return i;
		} else if (pce.getLeft().getKind() == ExpressionKind.DOT_EXP){
			i =  deduceExpression(pce.getLeft().getLeft(), ctx, fc);
			DotExpression de = (DotExpression) pce.getLeft();
			if (de.getRight().getKind() == ExpressionKind.IDENT) {
				i = fc.dotExpression(i, de.getRight());
				i = fc.makeProcCall(i, pce.getArgs()); // TODO look below
				return i;
			} else {
				throw new NotImplementedException();
			}
		} else if (pce.getLeft().getKind() == ExpressionKind.IDENT) {
			IntroducedVariable intro = fc.introduceVariable(pce.getLeft());
			intro.makeIntoFunctionCall(pce.getArgs()); // TODO look above
			return intro;
		} else {
			throw new NotImplementedException();
		}
		//return null;
	}

	public void deduceVariableStatement(OS_Element parent, @NotNull VariableStatement vs, FunctionContext fc) {
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
							deduceVariableStatement_procedureCallExpression(parent, iv, pce, (IdentExpression) left, fc);
						}
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
			System.out.println(String.format("[#addFunctionItem_deduceVariableStatement] %s %s;", vs.getName(), dtype));

		}
	}

	private void deduceVariableStatement_procedureCallExpression(
			@NotNull OS_Element parent, IExpression iv,
			ProcedureCallExpression pce, @NotNull IdentExpression left, FunctionContext fc) {
		final String text = left.getText();
		final LookupResultList lrl = parent.getContext().lookup(text);
		System.out.println("98 "+/*n*/iv);
		if (lrl.results().size() == 0 )
			System.err.println("96 no results for "+text);
		for (LookupResult n: lrl.results()) {
			System.out.println("97 "+n);
//			Helpers.printXML(iv, new TabbedOutputStream(System.out));
		}
		final Collection<IExpression> expressions = pce.getArgs().expressions();
		List<FunctionPrelimInstruction> q = expressions.stream()
				.map(n -> deduceExpression(n, parent.getContext(), fc))
				.collect(Collectors.toList());
		System.out.println("90 "+q);
		NotImplementedException.raise();
	}

	public FunctionPrelimInstruction deduceExpression(@NotNull IExpression n, Context context, FunctionContext fc) {
		if (n.getKind() == ExpressionKind.IDENT) {
			LookupResultList lrl = context.lookup(((IdentExpression)n).getText());
			if (lrl.results().size() == 1) { // TODO the reason were having problems here is constraints vs shadowing
//				return lrl.results().get(0).getElement();
				// TODO what to do here??
				final OS_Element element = lrl.results().get(0).getElement();
				if (element instanceof VariableStatement) {
					if (((VariableStatement) element).typeName() != null) {
						if (((VariableStatement) element).typeName().isNull()) {
							FunctionPrelimInstruction i = deduceExpression(((VariableStatement) element).initialValue(), context, fc);
							int y=2;
							return i;
						}
//						return new OS_Type(((VariableStatement) element).typeName());
					}
				} else if (element instanceof FormalArgListItem) {
					final TypeName typeName = ((FormalArgListItem) element).tn;
					if (typeName != null) {
//						return new OS_Type(typeName);
					}
				}
				System.err.println("89 "+n);//element.getClass().getName());
				module.parent.eee.reportError("type not specified: "+ getElementName(element));
				return null;
			}
			module.parent.eee.reportError("IDENT not found: "+((IdentExpression) n).getText());
			NotImplementedException.raise();
		} else if (n.getKind() == ExpressionKind.NUMERIC) {
//			return new OS_Type(BuiltInTypes.SystemInteger);
		} else if (n.getKind() == ExpressionKind.DOT_EXP) {
			DotExpression de = (DotExpression) n;
			var left_type = deduceExpression(de.getLeft(), context, fc);
//			var right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext(), fc);
			int y=2;
		} else if (n.getKind() == ExpressionKind.GET_ITEM) {
			int y=2;
			FunctionPrelimInstruction i = fc.introduceFunction(n.getLeft());
			final IntroducedFunction nn = (IntroducedFunction) i;
			nn.setName("__getitem__");
			nn.setArgs(Helpers.List_of(((GetItemExpression)n).index));
			return i;
		} else if (n.getKind() == ExpressionKind.PROCEDURE_CALL) {
			return expandProcedureCall((ProcedureCallExpression) n, context, fc);
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

	/** Only interested in classes and namespaces here, as that's where the functions are */
	private void addModuleItem(ModuleItem element) {
		if (element instanceof ClassStatement) {
			ClassStatement cl = (ClassStatement) element;
			addClass(cl, module);
		} else if (element instanceof NamespaceStatement) {
			NamespaceStatement ns = (NamespaceStatement) element;
			addNamespace(ns, module);
		}
	}
	private void addNamespace(NamespaceStatement ns, OS_Module parent) {
		System.out.print("namespace " + ns.getName() + "{\n");
		{
			for (ClassItem element : ns.getItems())
				addClassItem(element, ns);
		}
		System.out.print("}\n");
	}

	public void expand() {
		System.out.println("-- [ExpandFunctions#expand] ----------------------------");
		for (ModuleItem element : module.items) {
			addModuleItem(element);
		}
	}

}

//
//
//
