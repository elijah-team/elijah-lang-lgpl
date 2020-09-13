/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 9/10/20 2:28 PM
 */
public class GenerateFunctions {
	private final OS_Module module;

	public GenerateFunctions(OS_Module module_) {
		module = module_;
	}

	public List<GeneratedFunction> generateAllTopLevelFunctions() {
		List<GeneratedFunction> R = new ArrayList<>();

		for (ModuleItem item : module.getItems()) {
			if (item instanceof NamespaceStatement) {
				List<GeneratedFunction> r;
				r = generateAllNamespaceFunctions(((NamespaceStatement) item));
				R.addAll(r);
			} else if (item instanceof ClassStatement) {
				List<GeneratedFunction> r;
				ClassStatement classStatement = (ClassStatement) item;
				r = generateAllClassFunctions(classStatement);
				R.addAll(r);
			}
		}

		return R;
	}

	private List<GeneratedFunction> generateAllClassFunctions(ClassStatement classStatement) {
		List<GeneratedFunction> R = new ArrayList<>();

		for (ClassItem item : classStatement.getItems()) {
			if (item instanceof FunctionDef) {
				FunctionDef function_def = (FunctionDef) item;
				R.add(generateFunction(function_def, classStatement));
			} else if (item instanceof DefFunctionDef) {
				DefFunctionDef defFunctionDef = (DefFunctionDef) item;
				R.add(generateDefFunction(defFunctionDef, classStatement));
			}
		}

		return R;
	}

	private List<GeneratedFunction> generateAllNamespaceFunctions(NamespaceStatement namespaceStatement) {
		List<GeneratedFunction> R = new ArrayList<>();

		for (ClassItem item : namespaceStatement.getItems()) {
			if (item instanceof FunctionDef) {
				FunctionDef function_def = (FunctionDef) item;
				generateFunction(function_def, namespaceStatement);
			} else if (item instanceof DefFunctionDef) {
				DefFunctionDef defFunctionDef = (DefFunctionDef) item;
				generateDefFunction(defFunctionDef, namespaceStatement);
			}
		}

		return R;
	}
	private GeneratedFunction generateDefFunction(DefFunctionDef fd, OS_Element parent) {
		System.err.println("601 fn "+fd.funName);
		GeneratedFunction gf = new GeneratedFunction(fd);
		long e1 = add_i(gf, InstructionName.E, null);
		add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)));
		System.out.println(String.format("602 %s %s", fd.funName, gf.instructionsList));
		System.out.println(gf.vte_list);
		System.out.println(gf.cte_list);
		System.out.println(gf.prte_list);
		System.out.println(gf.tte_list);
		System.out.println(gf.idte_list);
		return gf;
	}

	private GeneratedFunction generateFunction(FunctionDef fd, OS_Element parent) {
		System.err.println("601.1 fn "+fd.funName);
		GeneratedFunction gf = new GeneratedFunction(fd);
		if (parent instanceof ClassStatement)
			addVariableTableEntry("self", VariableTableType.SELF, InstructionType.known(new OS_Type((ClassStatement) parent)), gf);
		addVariableTableEntry("Result", VariableTableType.RESULT, null, gf); // TODO what about Unit returns?
		for (FormalArgListItem fali : fd.fal().falis) {
			addVariableTableEntry(fali.name.getText(), VariableTableType.ARG, InstructionType.known(new OS_Type(fali.typeName())), gf);
		} // TODO Exception !!??
		//
		long e1 = add_i(gf, InstructionName.E, null);
		for (FunctionItem item : fd.getItems()) {
			System.err.println("7001 fd.getItem = "+item);
			if (item instanceof AliasStatement) {

			} else if (item instanceof CaseConditional) {

			} else if (item instanceof ClassStatement) {

			} else if (item instanceof StatementWrapper) {
//				System.err.println("106");
				IExpression x = ((StatementWrapper) item).getExpr();
				System.err.println("106-1 "+x.getKind()+" "+x);
				if (x.is_simple()) {
//					int i = addTempTableEntry(x.getType(), gf);
					switch (x.getKind()) {
					case ASSIGNMENT:
						System.err.println(String.format("801 %s %s", x.getLeft(), ((BasicBinaryExpression) x).getRight()));
						BasicBinaryExpression bbe = (BasicBinaryExpression) x;
						final IExpression right1 = bbe.getRight();
						switch (right1.getKind()) {
						case PROCEDURE_CALL:
							int ii = addVariableTableEntry(((IdentExpression)bbe.getLeft()).getText(), getType(bbe), gf);
							add_i(gf, InstructionName.AGN, List_of(new IntegerIA(ii), new FnCallIA(expression_to_call(right1, gf))));
							break;
						case IDENT:
							final IdentExpression left = (IdentExpression) bbe.getLeft();
							int iii = vte_lookup(left.getText(), gf);
							if (iii == -1) {
								iii = addIdentTableEntry(left, gf);
							}
							final IdentExpression right = (IdentExpression) right1;
							int iiii = vte_lookup(right.getText(), gf);
							if (iiii == -1) {
								iiii = addIdentTableEntry(right, gf);
							}
							add_i(gf, InstructionName.AGN, List_of(new IntegerIA(iii), new IntegerIA(iiii)));
							break;
						default:
							throw new NotImplementedException();
						}
						break;
					}
				} else {
					switch (x.getKind()) {
						case ASSIGNMENT:
							System.err.println(String.format("801.1 %s %s", x.getLeft(), ((BasicBinaryExpression)x).getRight()));
							break;
						case IS_A:
							break;
						case PROCEDURE_CALL:
							ProcedureCallExpression pce = (ProcedureCallExpression) x;
							simplify_procedure_call(pce, gf);
							break;
						default:
							break;
					}
				}
			} else if (item instanceof IfConditional) {

			} else if (item instanceof Loop) {
				System.err.println("800");

			} else if (item instanceof MatchConditional) {

			} else if (item instanceof NamespaceStatement) {

			} else if (item instanceof VariableSequence) {
				for (VariableStatement vs : ((VariableSequence) item).items()) {
//					System.out.println("8004 " + vs);
					if (vs.getTypeModifiers() == TypeModifiers.CONST) {
						if (vs.initialValue().is_simple()) {
							addConstantTableEntry(vs.getName(), vs.initialValue(), vs.initialValue().getType(), gf);
						} else {
							int i = addVariableTableEntry(vs.getName(), getType(vs.initialValue()), gf);
							IExpression iv = vs.initialValue();
							add_i(gf, InstructionName.AGN, List_of(new IntegerIA(i), new FnCallIA(expression_to_call(iv, gf))));
						}
					}
					final OS_Type type = vs.initialValue().getType();
					final String stype = type == null ? "Unknown" : getTypeString(type);
					System.out.println("8004-1 " + type);
					System.out.println(String.format("8004-2 %s %s;", stype, vs.getName()));
				}
			} else if (item instanceof WithStatement) {

			} else if (item instanceof SyntacticBlock) {

			} else {
				throw new IllegalStateException("cant be here");
			}
		}
		add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)));
		System.out.println(String.format("602.1 %s %s", fd.funName, gf.instructionsList));
		System.out.println("VariableTable "+ gf.vte_list);
		System.out.println("ConstantTable "+ gf.cte_list);
		System.out.println("ProcTable     "+ gf.prte_list);
		System.out.println("TypeTable     "+ gf.tte_list);
		System.out.println("IdentTable    "+ gf.idte_list);
		System.out.println("FuncTable     "+ gf.fte_list);
		return gf;
	}

	private InstructionType getType(IExpression arg) {
		OS_Type type = arg.getType();
		if (type == null)
			return (InstructionType.unknown(arg));
		else
			return (InstructionType.known(type));
	}

	private void simplify_procedure_call(ProcedureCallExpression pce, GeneratedFunction gf) {
		IExpression left = pce.getLeft();
		ExpressionList args = pce.getArgs();
		//
		int i = addProcTableEntry(left, simplify_expression(left, gf), get_args_types(args, gf), gf);
		final List<InstructionArgument> l = new ArrayList<InstructionArgument>();
		l.add(new IntegerIA(i));
		l.addAll(simplify_args(args, gf));
		add_i(gf, InstructionName.CALL, l);
	}

	private List<InstructionArgument> simplify_args(ExpressionList args, GeneratedFunction gf) {
		List<InstructionArgument> R = new ArrayList<InstructionArgument>();
		for (IExpression expression : args) {
			int ia = simplify_expression(expression, gf);
			if (ia > 0) {
				System.err.println("109 "+expression);
			} else {
				System.err.println("109-0 error expr not found "+expression);
			}
		}
		return R;
	}

	private int addProcTableEntry(IExpression expression, int expression_num, List<InstructionType> args, GeneratedFunction gf) {
		ProcTableEntry pte = new ProcTableEntry(gf.prte_list.size(), expression, expression_num, args);
		gf.prte_list.add(pte);
		return pte.index;
	}

	private int simplify_expression(IExpression expression, GeneratedFunction gf) {
		switch (expression.getKind()) {
		case PROCEDURE_CALL:
			break;
		case DOT_EXP:
			DotExpression de = (DotExpression) expression;
			IExpression expr = de.getLeft();
			do {
				int i = simplify_expression(expr, gf);
				VariableTableEntry x = gf.vte_list.get(i);
				System.err.println("901 "+expr.getType());
//				expr =
			} while (expr != null);
			break;
		case QIDENT:
			break;
		case IDENT:
			return vte_lookup(((IdentExpression)expression).getText(), gf);
		default:
			return -1;
		}
		return 0;
	}

	private int vte_lookup(String text, GeneratedFunction gf) {
		int index = 0;
		for (VariableTableEntry variableTableEntry : gf.vte_list) {
			if (variableTableEntry.getName().equals(text))
				return index;
			index++;
		}
		index = 0;
		for (ConstantTableEntry constTableEntry : gf.cte_list) {
			if (constTableEntry.getName().equals(text))
				return index;
			index++;
		}
		return -1;
	}

	private List<InstructionType> get_args_types(ExpressionList args, GeneratedFunction gf) {
		List<InstructionType> R = new ArrayList<InstructionType>();
		for (IExpression arg : args) {
			final OS_Type type = arg.getType();
			System.err.println(String.format("108 %s %s", arg, type));
			R.add(getType(arg));
		}
		return R;
	}

	private Instruction expression_to_call(IExpression expression, GeneratedFunction gf) {
		if (expression.getKind() != ExpressionKind.PROCEDURE_CALL)
			throw new NotImplementedException();

		switch (expression.getLeft().getKind()){
		case IDENT:
			ProcedureCallExpression pce = (ProcedureCallExpression) expression;
			Instruction i = new Instruction();
			i.setName(InstructionName.CALL);
			List<InstructionArgument> li = new ArrayList<>();
//			int ii = addIdentTableEntry((IdentExpression) expression.getLeft(), gf);
			int ii = addFuncTableEntry((IdentExpression) expression.getLeft(), get_args_types(pce.getArgs(), gf), gf);
			li.add(new IntegerIA(ii));
			final List<InstructionArgument> args_ = simplify_args(pce.getArgs(), gf);
			li.addAll(args_);
			i.setArgs(li);
			return i;
		case QIDENT:
			simplify_qident((Qualident) expression.getLeft(), gf);
			break;
		case DOT_EXP:
			simplify_dot_expression((DotExpression) expression.getLeft(), gf);
			break;
		default:
			throw new NotImplementedException();
		}
//		int i = simplify_expression(expression, gf);
		return null;
	}

	private int addFuncTableEntry(IdentExpression name, List<InstructionType> args_types, GeneratedFunction gf) {
		FuncTableEntry fte = new FuncTableEntry(gf.fte_list.size(), name, args_types);
		gf.fte_list.add(fte);
		return fte.index;
	}

	private int addIdentTableEntry(IdentExpression ident, GeneratedFunction gf) {
		IdentTableEntry idte = new IdentTableEntry(gf.idte_list.size(), ident);
		gf.idte_list.add(idte);
		return idte.index;
	}

	private void simplify_qident(Qualident left, GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	private void simplify_dot_expression(DotExpression left, GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	private int addVariableTableEntry(String name, InstructionType type, GeneratedFunction gf) {
		return addVariableTableEntry(name, VariableTableType.VAR, type, gf);
	}

	private int addVariableTableEntry(String name, VariableTableType vtt, InstructionType type, GeneratedFunction gf) {
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), vtt, name, type);
		gf.vte_list.add(vte);
		return vte.index;
	}

	private int addTempTableEntry(OS_Type type, GeneratedFunction gf) {
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, null, InstructionType.known(type));
		gf.vte_list.add(vte);
		return vte.index;
	}

	private void addConstantTableEntry(String name, IExpression initialValue, OS_Type type, GeneratedFunction gf) {
		ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, type);
		gf.cte_list.add(cte);
	}

	private long add_i(GeneratedFunction gf, InstructionName x, List<InstructionArgument> list_of) {
		long i = gf.add(x, list_of);
		return i;
	}

	private String getTypeString(OS_Type type) {
		switch (type.getType()) {
			case BUILT_IN:
				BuiltInTypes bt = type.getBType();
				return bt.name();
//				if (type.resolve())
//					return type.getClassOf().getName();
//				else
//					return "Unknown";
			case USER:
				return type.getTypeName().toString();
			case USER_CLASS:
				return type.getClassOf().getName();
			case FUNCTION:
				return "Function<>";
			default:
				throw new IllegalStateException("cant be here");
		}
//		return type.toString();
	}

}

//
//
//
