/**
 * 
 */
package tripleo.elijah.stages.deduce;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import antlr.Token;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 5:39:30 AM
 */
public class DeduceTypes {

	private static int _classCode = 101;
	private static int _functionCode = 1001;

	private OS_Module module;

	public DeduceTypes(OS_Module module) {
//		NotImplementedException.raise();
		this.module = module;
	}

	public void addClass(ClassStatement klass, OS_Module parent) {
//		System.out.print("class " + klass.clsName + "{\n");
		klass._a.setCode(nextClassCode());	
		parent.getContext().nameTable().add(klass, klass.getName(), new OS_Type(klass));
		
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
				fd._a.setCode(nextFunctionCode());
				parent.getContext().add(fd, fd.funName);
				{
					for (FunctionItem fi : fd.getItems())
						addFunctionItem(fi, fd);

				}
//				fd.visit(this);
//				System.out.print("\n}\n\n");
			} else if (element instanceof ClassStatement) {
//				((ClassStatement) element).visitGen(this);
				System.err.println("93 "+element.getClass().getName());
			} else {
				System.err.println("92 "+element.getClass().getName());
			}
		}
	}
	
	public void addFunctionItem(FunctionItem element, FunctionDef parent) {
		// TODO Auto-generated method stub
		if (element instanceof VariableSequence) {
//			fd._a.setCode(nextFunctionCode());
//			parent._a.getContext().add(element, null);
			for (VariableStatement ii : ((VariableSequence) element).items())
				addFunctionItem_deduceVariableStatement(parent, ii);
		}
		else if (element instanceof ProcedureCallExpression) {
			ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("%s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			addFunctionItem_Loop((Loop) element, parent);
		}  else if (element instanceof StatementWrapper) {
			IExpression expr = ((StatementWrapper) element).getExpr();
			if (expr.getKind() == ExpressionKind.ASSIGNMENT) {
//				NotImplementedException.raise();
				final OS_Type right_type = deduceExpression(((IBinaryExpression) expr).getRight(), parent.getContext());
				((IBinaryExpression)expr).getRight().setType(right_type);
				expr.getLeft().setType(right_type);
				expr.setType(expr.getLeft().getType());
			} else if (expr.getKind() == ExpressionKind.PROCEDURE_CALL) {
				deduceProcedureCall((ProcedureCallExpression) expr, parent.getContext());
			} else throw new NotImplementedException();
		} else if (element instanceof ClassStatement) {
			parent._a.getContext().nameTable().add((OS_Element) element, ((ClassStatement) element).getName(), new OS_Type((ClassStatement) element));
		} else {
			System.out.println("91 "+element);
			throw new NotImplementedException();
		}
	}

	private void addFunctionItem_Loop(Loop element, FunctionDef parent) {
		Loop loop = element;
		if (loop.getType() == Loop.FROM_TO_TYPE) {
			parent.getContext().add(new IdentExpression(Helpers.makeToken(loop.getIterName())), loop.getIterName());
			String varname="vt"+loop.getIterName();
			final NumericExpression fromPart = (NumericExpression)loop.getFromPart();
			if (loop.getToPart() instanceof NumericExpression) {
				final NumericExpression toPart = (NumericExpression)loop.getToPart();

				System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
						varname, fromPart.getValue(),
						varname, toPart.getValue(),  varname));
			} else if (loop.getToPart() instanceof IdentExpression) {
				final IdentExpression toPart = (IdentExpression)loop.getToPart();

				System.out.println(String.format("{for (int %s=%d;%s<=%s;%s++){\n\t",
						varname, fromPart.getValue(),
						varname, "vv"+toPart.getText(),  varname));

			}
			for (StatementItem item : loop.getItems()) {
				System.out.println("\t"+item);
			}
			System.out.println("}");
		} else if (loop.getType() == Loop.EXPR_TYPE) {
			if (loop.getToPart() instanceof NumericExpression) {
				String varname="vt0_TODO";
				final NumericExpression toPart = (NumericExpression)loop.getToPart();

				System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
						varname, 0,
						varname, toPart.getValue(),  varname));
				for (StatementItem item : loop.getItems()) {
					System.out.println("\t"+item);
				}
				System.out.println("}");
			} else if (loop.getToPart() instanceof DotExpression) {
				System.out.println("94 "+loop.getToPart().getClass().getName());
				NotImplementedException.raise();
			} else {
				System.out.println("95 "+loop.getToPart().getClass().getName());
				throw new NotImplementedException();
			}
		} else throw new NotImplementedException();
	}

	private void deduceProcedureCall(ProcedureCallExpression pce, Context ctx) {
		IExpression de = qualidentToDotExpression2(((Qualident) pce.getLeft()).parts());
		System.out.println("77 "+de);
		pce.setLeft(de);
		final String function_name = ((IdentExpression) pce.getLeft()).getText();
		LookupResultList lrl = ctx.lookup(function_name);
		if (lrl.results().size() == 0)
			module.parent.eee.reportError("function not found " + function_name);
		int y=2;
//		final OS_Type right_type = deduceExpression(((IBinaryExpression) expr).getRight(), parent.getContext());
//		((IBinaryExpression)expr).getRight().setType(right_type);
//		expr.getLeft().setType(right_type);
//		expr.setType(expr.getLeft().getType());
	}

//	public DotExpression qualidentToDotExpression(DotExpression de, List<Token> ts) {
//		final DotExpression dotExpression = qualidentToDotExpression2(ts.subList(1, ts.size()));
//		if (dotExpression == null)
//			return de;
//		return new DotExpression(new IdentExpression(ts.get(0)),
//				dotExpression);
//	}
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

	public void addFunctionItem_deduceVariableStatement(@NotNull FunctionDef parent, @NotNull VariableStatement vs) {
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
							addFunctionItem_deduceVariableStatement_procedureCallExpression(parent, iv, pce, (IdentExpression) left);
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

	private void addFunctionItem_deduceVariableStatement_procedureCallExpression(
			@NotNull FunctionDef parent, IExpression iv,
			ProcedureCallExpression pce, @NotNull IdentExpression left) {
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
		List<OS_Type> q = expressions.stream()
				.map(n -> deduceExpression(n, parent.getContext()))
				.collect(Collectors.toList());
		System.out.println("90 "+q);
		NotImplementedException.raise();
	}

	public OS_Type deduceExpression(@NotNull IExpression n, Context context) {
		if (n.getKind() == ExpressionKind.IDENT) {
			LookupResultList lrl = context.lookup(((IdentExpression)n).getText());
			if (lrl.results().size() == 1) {
//				return lrl.results().get(0).getElement();
				// TODO what to do here??
				final OS_Element element = lrl.results().get(0).getElement();
				if (element instanceof VariableStatement) {
					if (((VariableStatement) element).typeName() != null)
						return new OS_Type(((VariableStatement) element).typeName());
				}

			}
			NotImplementedException.raise();
		} else if (n.getKind() == ExpressionKind.NUMERIC) {
			return new OS_Type(BuiltInTypes.SystemInteger);
		}
		
		return null;
	}

	private void addImport(ImportStatement imp, OS_Module parent) {
//		throw new NotImplementedException();
		if (imp.getRoot() == null) {
			for (Qualident q : imp.parts()) {
				module.modify_namespace(q, NamespaceModify.IMPORT);
			}
		}
//		module.
	}

	private void addModuleItem(ModuleItem element) {
		// TODO Auto-generated method stub
		if (element instanceof ClassStatement) {
			ClassStatement cl = (ClassStatement) element;
			addClass(cl, module);
		} else if (element instanceof ImportStatement) {
			ImportStatement imp = (ImportStatement) element;
			addImport(imp, module);
		} else if (element instanceof ImportStatement) {
			NamespaceStatement ns = (NamespaceStatement) element;
			addNamespace(ns, module);
		}
	}
	private void addNamespace(NamespaceStatement ns, OS_Module parent) {
//		System.out.print("class " + klass.clsName + "{\n");
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

	private int nextClassCode() {
		return ++_classCode;
	}
	
	private int nextFunctionCode() {
		return ++_functionCode;
	}
}
