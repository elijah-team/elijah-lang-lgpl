/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import antlr.CommonToken;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijjah.ElijjahTokenTypes;
import tripleo.util.range.Range;

import java.util.ArrayList;
import java.util.List;

import static tripleo.elijah.lang.ExpressionKind.PROCEDURE_CALL;
import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;
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
		final Context cctx = fd.getContext();
		int e1 = add_i(gf, InstructionName.E, null, cctx);
		add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)), cctx);
		System.out.println(String.format("602 %s %s", fd.funName, gf.instructionsList));
		System.out.println(gf.vte_list);
		System.out.println(gf.cte_list);
		System.out.println(gf.prte_list);
		System.out.println(gf.tte_list);
//		System.out.println(gf.idte_list);
		return gf;
	}

	private GeneratedFunction generateFunction(FunctionDef fd, OS_Element parent) {
		System.err.println("601.1 fn "+fd.funName);
		GeneratedFunction gf = new GeneratedFunction(fd);
		if (parent instanceof ClassStatement)
			addVariableTableEntry("self", VariableTableType.SELF, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type((ClassStatement) parent)), gf);
		addVariableTableEntry("Result", VariableTableType.RESULT, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(fd.returnType())), gf); // TODO what about Unit returns?
		for (FormalArgListItem fali : fd.fal().falis) {
			final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken());
			addVariableTableEntry(fali.name.getText(), VariableTableType.ARG, tte, gf);
		} // TODO Exception !!??
		//
		final Context cctx = fd.getContext();
		int e1 = add_i(gf, InstructionName.E, null, cctx);
		for (FunctionItem item : fd.getItems()) {
//			System.err.println("7001 fd.getItem = "+item);
			generate_item((OS_Element) item, gf, cctx);
		}
		int x1 = add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)), cctx);
		gf.addContext(fd.getContext(), new Range(e1, x1)); // TODO remove interior contexts
		System.out.println(String.format("602.1 %s", fd.funName));
//		for (Instruction instruction : gf.instructionsList) {
//			System.out.println(instruction);
//		}
		System.out.println("VariableTable "+ gf.vte_list);
		System.out.println("ConstantTable "+ gf.cte_list);
		System.out.println("ProcTable     "+ gf.prte_list);
		System.out.println("TypeTable     "+ gf.tte_list);
//		System.out.println("IdentTable    "+ gf.idte_list);
		return gf;
	}

	private void generate_item(OS_Element item, GeneratedFunction gf, Context cctx) {
		if (item instanceof AliasStatement) {
			throw new NotImplementedException();
		} else if (item instanceof CaseConditional) {
			throw new NotImplementedException();
		} else if (item instanceof ClassStatement) {
			throw new NotImplementedException();
		} else if (item instanceof StatementWrapper) {
//				System.err.println("106");
			IExpression x = ((StatementWrapper) item).getExpr();
			System.err.println("106-1 "+x.getKind()+" "+x);
			if (x.is_simple()) {
//					int i = addTempTableEntry(x.getType(), gf);
				switch (x.getKind()) {
				case ASSIGNMENT:
					{
						System.err.println(String.format("801 %s %s", x.getLeft(), ((BasicBinaryExpression) x).getRight()));
						BasicBinaryExpression bbe = (BasicBinaryExpression) x;
						final IExpression right1 = bbe.getRight();
						switch (right1.getKind()) {
						case PROCEDURE_CALL: {
							final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, bbe.getType(), bbe.getLeft());
							int ii = addVariableTableEntry(((IdentExpression)bbe.getLeft()).getText(), tte, gf);
							int instruction_number = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(ii), new FnCallArgs(expression_to_call(right1, gf), gf)), cctx);
							Instruction instruction = gf.getInstruction(instruction_number);
							VariableTableEntry vte = gf.getVarTableEntry(ii);
							vte.addPotentialType(instruction.getIndex(), tte);
						}
						break;
						case IDENT: {
							final IdentExpression left = (IdentExpression) bbe.getLeft();
							InstructionArgument iii = gf.vte_lookup(left.getText());
							int iii4, iii5=-1;
							if (iii == null) {
								iii4 = addIdentTableEntry(left, gf);
							}
							final IdentExpression right = (IdentExpression) right1;
							InstructionArgument iiii = gf.vte_lookup(right.getText());
							if (iiii == null) {
								iii5 = addIdentTableEntry(right, gf);
							}
							int ia1 = add_i(gf, InstructionName.AGN, List_of(iii, iiii), cctx);
							VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(iii));
							vte.addPotentialType(ia1,
									gf.getVarTableEntry(DeduceTypes2.to_int(iiii/* != null ? iiii :
											gf.getVarTableEntry(iii5))*/)).type);
						}

						break;
						default:
							throw new NotImplementedException();
						}
					}
					break;
				case AUG_MULT:
					{
						System.out.println(String.format("801.1 AUG_MULT %s %s", x.getLeft(), ((BasicBinaryExpression) x).getRight()));
//						BasicBinaryExpression bbe = (BasicBinaryExpression) x;
//						final IExpression right1 = bbe.getRight();
						InstructionArgument left = simplify_expression(x.getLeft(), gf);
						InstructionArgument right = simplify_expression(((BasicBinaryExpression) x).getRight(), gf);
						CommonToken t = new CommonToken(ElijjahTokenTypes.IDENT, "__aug_mult__");
						IdentExpression fn_aug_name = new IdentExpression(t);
						final List<TypeTableEntry> argument_types = List_of(gf.getVarTableEntry(to_int(left)).type, gf.getVarTableEntry(to_int(right)).type);
						System.out.println("801.2 "+argument_types); // TODO still dont know the argument types at this point, which creates a problem for resolving functions
						int fn_aug = addProcTableEntry(fn_aug_name, null, argument_types, gf);
						int i = add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(fn_aug), left, right), cctx);
						for (TypeTableEntry argument_type : argument_types) {
							if (argument_type.attached == null) {
								gf.deferred_calls.add(i);
								break;
							}
						}
					}
					break;
				default:
					throw new NotImplementedException();
				}
			} else {
				switch (x.getKind()) {
					case ASSIGNMENT:
						System.err.println(String.format("801.2 %s %s", x.getLeft(), ((BasicBinaryExpression)x).getRight()));
						break;
					case IS_A:
						break;
					case PROCEDURE_CALL:
						ProcedureCallExpression pce = (ProcedureCallExpression) x;
						simplify_procedure_call(pce, gf, cctx);
						break;
					default:
						break;
				}
			}
		} else if (item instanceof IfConditional) {
			throw new NotImplementedException();
		} else if (item instanceof Loop) {
			System.err.println("800");
			Loop loop = (Loop) item;
			generate_loop(loop, gf);
		} else if (item instanceof MatchConditional) {
			throw new NotImplementedException();
		} else if (item instanceof NamespaceStatement) {
			throw new NotImplementedException();
		} else if (item instanceof VariableSequence) {
			for (VariableStatement vs : ((VariableSequence) item).items()) {
//					System.out.println("8004 " + vs);
				if (vs.getTypeModifiers() == TypeModifiers.CONST) {
					if (vs.initialValue().is_simple()) {
						int ci = addConstantTableEntry(vs.getName(), vs.initialValue(), vs.initialValue().getType(), gf);
						int i = addVariableTableEntry(vs.getName(), gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, (vs.initialValue().getType()), vs.getNameToken()), gf);
						IExpression iv = vs.initialValue();
						add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(i), new ConstTableIA(ci, gf)), cctx);
					} else {
						int i = addVariableTableEntry(vs.getName(), gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, (vs.initialValue().getType()), vs.getNameToken()), gf);
						IExpression iv = vs.initialValue();
						assign_variable(gf, i, iv, cctx);
					}
				} else {
					int i = addVariableTableEntry(vs.getName(), gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, (vs.initialValue().getType()), vs.getNameToken()), gf);
					IExpression iv = vs.initialValue();
					assign_variable(gf, i, iv, cctx);
				}
//				final OS_Type type = vs.initialValue().getType();
//				final String stype = type == null ? "Unknown" : getTypeString(type);
//				System.out.println("8004-1 " + type);
//				System.out.println(String.format("8004-2 %s %s;", stype, vs.getName()));
			}
		} else if (item instanceof WithStatement) {
			throw new NotImplementedException();
		} else if (item instanceof SyntacticBlock) {
			throw new NotImplementedException();
		} else {
			throw new IllegalStateException("cant be here");
		}
	}

	private void generate_loop(Loop loop, GeneratedFunction gf) {
		final Context cctx = loop.getContext();
		int e2 = add_i(gf, InstructionName.ES, null, cctx);
		switch (loop.getType()) {
		case FROM_TO_TYPE:
			{
				IdentExpression iterNameToken = loop.getIterNameToken();
				String iterName = iterNameToken.getText();
				int i = addTempTableEntry(null, iterNameToken, gf); // TODO deduce later
				final InstructionArgument ia1 = simplify_expression(loop.getFromPart(), gf);
				if (ia1 instanceof ConstTableIA)
					add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(i), ia1), cctx);
				else
					add_i(gf, InstructionName.AGN, List_of(new IntegerIA(i), ia1), cctx);
				Label label_top = gf.addLabel("top", true);
				gf.place(label_top);
				Label label_bottom = gf.addLabel("bottom"+label_top, false);
				add_i(gf, InstructionName.CMP, List_of(new IntegerIA(i), simplify_expression(loop.getToPart(), gf)), cctx);
				add_i(gf, InstructionName.JE, List_of(label_bottom), cctx);
				for (StatementItem statementItem : loop.getItems()) {
					System.out.println("705 "+statementItem);
					generate_item((OS_Element)statementItem, gf, cctx);
				}
				CommonToken t = new CommonToken(ElijjahTokenTypes.IDENT, "__preinc__");
				IdentExpression pre_inc_name = new IdentExpression(t);
				TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, pre_inc_name);
				int pre_inc = addProcTableEntry(pre_inc_name, null, List_of(tte/*getType(left), getType(right)*/), gf);
				add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), new IntegerIA(i)), cctx);
				add_i(gf, InstructionName.JMP, List_of(label_top), cctx);
				gf.place(label_bottom);
			}
			break;
		case TO_TYPE:
			break;
		case ITER_TYPE:
			break;
		case EXPR_TYPE:
			break;
		case WHILE:
			break;
		case DO_WHILE:
			break;
		}
		int x2 = add_i(gf, InstructionName.XS, List_of(new IntegerIA(e2)), cctx);
		Range r = new Range(e2, x2);
		gf.addContext(loop.getContext(), r);
	}

	private void assign_variable(GeneratedFunction gf, int vte, IExpression value, Context cctx) {
		switch (value.getKind()) {
		case PROCEDURE_CALL:
			ProcedureCallExpression pce = (ProcedureCallExpression) value;
			final FnCallArgs fnCallArgs = new FnCallArgs(expression_to_call(value, gf), /*, simplify_args(pce.getArgs(), gf*/gf);
			add_i(gf, InstructionName.AGN, List_of(new IntegerIA(vte), fnCallArgs), cctx);
			break;
		case NUMERIC:
			int ci = addConstantTableEntry(null, value, value.getType(), gf);
			int ii = add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(vte), new ConstTableIA(ci, gf)), cctx);
			VariableTableEntry vte1 = gf.getVarTableEntry(vte);
			vte1.addPotentialType(ii, gf.getConstTableEntry(ci).type);
			break;
		default:
			throw new NotImplementedException();
		}
	}

	private TypeTableEntry getType(IExpression arg, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, arg.getType(), arg);
		return tte;
	}

	private void simplify_procedure_call(ProcedureCallExpression pce, GeneratedFunction gf, Context cctx) {
		IExpression left = pce.getLeft();
		ExpressionList args = pce.getArgs();
		//
		int i = addProcTableEntry(left, simplify_expression(left, gf), get_args_types(args, gf), gf);
		final List<InstructionArgument> l = new ArrayList<InstructionArgument>();
		l.add(new IntegerIA(i));
		l.addAll(simplify_args(args, gf));
		add_i(gf, InstructionName.CALL, l, cctx);
	}

	private List<InstructionArgument> simplify_args(ExpressionList args, GeneratedFunction gf) {
		List<InstructionArgument> R = new ArrayList<InstructionArgument>();
		for (IExpression expression : args) {
			InstructionArgument ia = simplify_expression(expression, gf);
			if (ia != null) {
				System.err.println("109 "+expression);
				R.add(ia);
			} else {
				System.err.println("109-0 error expr not found "+expression);
			}
		}
		return R;
	}

	private int addProcTableEntry(IExpression expression, InstructionArgument expression_num, List<TypeTableEntry> args, GeneratedFunction gf) {
		ProcTableEntry pte = new ProcTableEntry(gf.prte_list.size(), expression, expression_num, args);
		gf.prte_list.add(pte);
		return pte.index;
	}

	private InstructionArgument simplify_expression(IExpression expression, GeneratedFunction gf) {
		switch (expression.getKind()) {
		case PROCEDURE_CALL:
			throw new NotImplementedException();
		case DOT_EXP: {
			DotExpression de = (DotExpression) expression;
			IExpression expr = de.getLeft();
			do {
				InstructionArgument i = simplify_expression(expr, gf);
				VariableTableEntry x = gf.vte_list.get(to_int(i)/*((IntegerIA) i).getIndex()*/);
				System.err.println("901 "+x+" "+expr.getType());
				expr = de.getRight();
			} while (expr != null);
		}
		break;
		case QIDENT:
			throw new NotImplementedException();
		case IDENT:
			InstructionArgument i = gf.vte_lookup(((IdentExpression) expression).getText());
			return i;
		case NUMERIC:
			{
				NumericExpression ne = (NumericExpression) expression;
				int ii = addConstantTableEntry2(null, ne, ne.getType(), gf);
				return new ConstTableIA(ii, gf);
			}
		default:
			throw new NotImplementedException();
		}
		return null;
	}

	private List<TypeTableEntry> get_args_types(ExpressionList args, GeneratedFunction gf) {
		List<TypeTableEntry> R = new ArrayList<>();
		for (IExpression arg : args) {
			final OS_Type type = arg.getType();
			System.err.println(String.format("108 %s %s", arg, type));
			if (arg instanceof IdentExpression) {
				InstructionArgument x = gf.vte_lookup(((IdentExpression) arg).getText());
				TypeTableEntry tte;
				if (x instanceof ConstTableIA) {
					ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) x).getIndex());
					tte = cte.getTypeTableEntry();
				} else if (x instanceof IntegerIA) {
					VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) x).getIndex());
					tte = vte.type;
				} else
					continue; // TODO
				R.add(tte);
			} else
				R.add(getType(arg, gf));
		}
		assert R.size() == args.size();
		return R;
	}

	private Instruction expression_to_call(IExpression expression, GeneratedFunction gf) {
		if (expression.getKind() != PROCEDURE_CALL)
			throw new NotImplementedException();

		switch (expression.getLeft().getKind()){
		case IDENT:
			ProcedureCallExpression pce = (ProcedureCallExpression) expression;
			return expression_to_call_add_entry(gf, pce, (IdentExpression) expression.getLeft());
		case QIDENT:
			simplify_qident((Qualident) expression.getLeft(), gf);
			break;
		case DOT_EXP: {
			simplify_dot_expression((DotExpression) expression.getLeft(), gf);
//			return expression_to_call_add_entry(gf, pce, i);
			}
			break;
		default:
			throw new NotImplementedException();
		}
//		int i = simplify_expression(expression, gf);
		return null;
	}

	@NotNull
	private Instruction expression_to_call_add_entry(GeneratedFunction gf, ProcedureCallExpression pce, IdentExpression left) {
		Instruction i = new Instruction();
		i.setName(InstructionName.CALL);
		List<InstructionArgument> li = new ArrayList<>();
//			int ii = addIdentTableEntry((IdentExpression) expression.getLeft(), gf);
		int ii = addProcTableEntry(left, null, get_args_types(pce.getArgs(), gf), gf);
		li.add(new IntegerIA(ii));
		final List<InstructionArgument> args_ = simplify_args(pce.getArgs(), gf);
		li.addAll(args_);
		i.setArgs(li);
		return i;
	}

	private int addIdentTableEntry(IdentExpression ident, GeneratedFunction gf) {
		throw new NotImplementedException();
//		IdentTableEntry idte = new IdentTableEntry(gf.idte_list.size(), ident);
//		gf.idte_list.add(idte);
//		return idte.index;
	}

	private void simplify_qident(Qualident left, GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	private void simplify_dot_expression(DotExpression left, GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	private int addVariableTableEntry(String name, TypeTableEntry type, GeneratedFunction gf) {
		return addVariableTableEntry(name, VariableTableType.VAR, type, gf);
	}

	private int addVariableTableEntry(String name, VariableTableType vtt, TypeTableEntry type, GeneratedFunction gf) {
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), vtt, name, type);
		gf.vte_list.add(vte);
		return vte.index;
	}

	private int addTempTableEntry(OS_Type type, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type);
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, null, tte);
		gf.vte_list.add(vte);
		return vte.index;
	}

	private int addTempTableEntry(OS_Type type, String name, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type);
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, name, tte);
		gf.vte_list.add(vte);
		return vte.index;
	}

	private int addTempTableEntry(OS_Type type, IdentExpression name, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, name);
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, name.getText(), tte);
		gf.vte_list.add(vte);
		return vte.index;
	}

	private int addConstantTableEntry(String name, IExpression initialValue, OS_Type type, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, initialValue);
		ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, tte);
		gf.cte_list.add(cte);
		return cte.index;
	}

	private int addConstantTableEntry2(String name, IExpression initialValue, OS_Type type, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, initialValue);
		ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, tte);
		gf.cte_list.add(cte);
		return cte.index;
	}

	private int add_i(GeneratedFunction gf, InstructionName x, List<InstructionArgument> list_of, Context ctx) {
		int i = gf.add(x, list_of, ctx);
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
