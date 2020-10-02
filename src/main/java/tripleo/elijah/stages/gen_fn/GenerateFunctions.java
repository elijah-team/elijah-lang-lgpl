/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialFunctions;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
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
			addVariableTableEntry("self", VariableTableType.SELF, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type((ClassStatement) parent), IdentExpression.forString("self")), gf);
		addVariableTableEntry("Result", VariableTableType.RESULT, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(fd.returnType()), IdentExpression.forString("Result")), gf); // TODO what about Unit returns?
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
		System.out.println("IdentTable    "+ gf.idte_list);
		return gf;
	}

	private void generate_item(OS_Element item, GeneratedFunction gf, Context cctx) {
		if (item instanceof AliasStatement) {
			throw new NotImplementedException();
		} else if (item instanceof CaseConditional) {
			throw new NotImplementedException();
		} else if (item instanceof ClassStatement) {
			//throw new NotImplementedException();
			System.out.println("Skip class for now "+((ClassStatement) item).name());
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
							int instruction_number = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(ii), new FnCallArgs(expression_to_call(right1, gf, cctx), gf)), cctx);
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
							assert iii != null;
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
						InstructionArgument left = simplify_expression(x.getLeft(), gf, cctx);
						InstructionArgument right = simplify_expression(((BasicBinaryExpression) x).getRight(), gf, cctx);
						IdentExpression fn_aug_name = Helpers.string_to_ident("__aug_mult__");
						final List<TypeTableEntry> argument_types = List_of(gf.getVarTableEntry(to_int(left)).type, gf.getVarTableEntry(to_int(right)).type);
//						System.out.println("801.2 "+argument_types);
						int fn_aug = addProcTableEntry(fn_aug_name, null, argument_types, gf);
						int i = add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(fn_aug), left, right), cctx);
						//
						// SEE IF CALL SHOULD BE DEFERRED
						//
						for (TypeTableEntry argument_type : argument_types) {
							if (argument_type.attached == null) {
								// still dont know the argument types at this point, which creates a problem
								// for resolving functions, so wait until later when more information is available
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
						System.err.println(String.format("803.2 %s %s", x.getLeft(), ((BasicBinaryExpression)x).getRight()));
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
			IfConditional ifc = (IfConditional) item;
			generate_if(ifc, gf);
//			throw new NotImplementedException();
		} else if (item instanceof Loop) {
			System.err.println("800");
			Loop loop = (Loop) item;
			generate_loop(loop, gf);
		} else if (item instanceof MatchConditional) {
			MatchConditional mc = (MatchConditional) item;
			generate_match_conditional(mc, gf);
//			throw new NotImplementedException();
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

	private void generate_match_conditional(MatchConditional mc, GeneratedFunction gf) {
		int y = 2;
		final Context cctx = mc.getContext();
		{
			IExpression expr = mc.getExpr();
			InstructionArgument i = simplify_expression(expr, gf, cctx);
			System.out.println("710 "+i);
		}
	}

	private void generate_if(IfConditional ifc, GeneratedFunction gf) {
		final Context cctx = ifc.getContext();
		{
			IExpression expr = ifc.getExpr();
			InstructionArgument i = simplify_expression(expr, gf, cctx);
			System.out.println("710 "+i);
		}
		List<IfConditional> parts = ifc.getParts();
		for (IfConditional part : parts) {
			for (OS_Element partItem : part.getItems()) {
				System.out.println("709 "+part+" "+partItem);
			}
		}
	}

	private void generate_loop(Loop loop, GeneratedFunction gf) {
		final Context cctx = loop.getContext();
		int e2 = add_i(gf, InstructionName.ES, null, cctx);
//		System.out.println("702 "+loop.getType());
		switch (loop.getType()) {
		case FROM_TO_TYPE:
			{
				IdentExpression iterNameToken = loop.getIterNameToken();
				String iterName = iterNameToken.getText();
				int i = addTempTableEntry(null, iterNameToken, gf); // TODO deduce later
				final InstructionArgument ia1 = simplify_expression(loop.getFromPart(), gf, cctx);
				if (ia1 instanceof ConstTableIA)
					add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(i), ia1), cctx);
				else
					add_i(gf, InstructionName.AGN, List_of(new IntegerIA(i), ia1), cctx);
				Label label_top = gf.addLabel("top", true);
				gf.place(label_top);
				Label label_bottom = gf.addLabel("bottom"+label_top, false);
				add_i(gf, InstructionName.CMP, List_of(new IntegerIA(i), simplify_expression(loop.getToPart(), gf, cctx)), cctx);
				add_i(gf, InstructionName.JE, List_of(label_bottom), cctx);
				for (StatementItem statementItem : loop.getItems()) {
					System.out.println("705 "+statementItem);
					generate_item((OS_Element)statementItem, gf, cctx);
				}
				IdentExpression pre_inc_name = Helpers.string_to_ident("__preinc__");
				TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, pre_inc_name);
				int pre_inc = addProcTableEntry(pre_inc_name, null, List_of(tte/*getType(left), getType(right)*/), gf);
				add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), new IntegerIA(i)), cctx);
				add_i(gf, InstructionName.JMP, List_of(label_top), cctx);
				gf.place(label_bottom);
			}
			break;
		case TO_TYPE:
			break;
		case EXPR_TYPE:
			{
				int i = addTempTableEntry(null, gf); // TODO deduce later
				int i2 = addConstantTableEntry("", new NumericExpression(0), new OS_Type(BuiltInTypes.SystemInteger), gf);
				final InstructionArgument ia1 = new ConstTableIA(i2, gf);
				if (ia1 instanceof ConstTableIA)
					add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(i), ia1), cctx);
				else
					add_i(gf, InstructionName.AGN, List_of(new IntegerIA(i), ia1), cctx);
				Label label_top = gf.addLabel("top", true);
				gf.place(label_top);
				Label label_bottom = gf.addLabel("bottom"+label_top, false);
				add_i(gf, InstructionName.CMP, List_of(new IntegerIA(i), simplify_expression(loop.getToPart(), gf, cctx)), cctx);
				add_i(gf, InstructionName.JE, List_of(label_bottom), cctx);
				for (StatementItem statementItem : loop.getItems()) {
					System.out.println("707 "+statementItem);
					generate_item((OS_Element)statementItem, gf, cctx);
				}
				final String txt = SpecialFunctions.of(ExpressionKind.INCREMENT);
				IdentExpression pre_inc_name = Helpers.string_to_ident(txt);
				TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, pre_inc_name);
				int pre_inc = addProcTableEntry(pre_inc_name, null, List_of(tte/*getType(left), getType(right)*/), gf);
				add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), new IntegerIA(i)), cctx);
				add_i(gf, InstructionName.JMP, List_of(label_top), cctx);
				gf.place(label_bottom);
			}
			break;
		case ITER_TYPE:
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
		if (value == IExpression.UNASSIGNED) return; // default_expression
		switch (value.getKind()) {
		case PROCEDURE_CALL:
			ProcedureCallExpression pce = (ProcedureCallExpression) value;
			final FnCallArgs fnCallArgs = new FnCallArgs(expression_to_call(value, gf, cctx), /*, simplify_args(pce.getArgs(), gf*/gf);
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
		int i = addProcTableEntry(left, simplify_expression(left, gf, cctx), get_args_types(args, gf), gf);
		final List<InstructionArgument> l = new ArrayList<InstructionArgument>();
		l.add(new IntegerIA(i));
		l.addAll(simplify_args(args, gf, cctx));
		add_i(gf, InstructionName.CALL, l, cctx);
	}

	private List<InstructionArgument> simplify_args(ExpressionList args, GeneratedFunction gf, Context cctx) {
		List<InstructionArgument> R = new ArrayList<InstructionArgument>();
		for (IExpression expression : args) {
			InstructionArgument ia = simplify_expression(expression, gf, cctx);
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

	private InstructionArgument simplify_expression(IExpression expression, GeneratedFunction gf, Context cctx) {
		final ExpressionKind expressionKind = expression.getKind();
		switch (expressionKind) {
		case PROCEDURE_CALL:
			{
				ProcedureCallExpression pce = (ProcedureCallExpression) expression;
				IExpression    left = pce.getLeft();
				ExpressionList args = pce.getArgs();
				InstructionArgument left_ia;
				List<InstructionArgument> right_ia = new ArrayList<InstructionArgument>(args.size());
				if (left.is_simple()) {
					if (left instanceof IdentExpression) {
						// for ident(xyz...)
						int y=2;
						int x = addIdentTableEntry((IdentExpression) left, gf);
						// TODO attach to var/const or lookup later in deduce
						left_ia = new IdentIA(x);
					} else if (left instanceof SubExpression) {
						// for (1).toString() etc
						SubExpression se = (SubExpression) left;
						InstructionArgument ia = simplify_expression(se.getExpression(), gf, cctx);
						//return ia;  // TODO is this correct?
						left_ia = ia;
					} else {
						// for "".strip() etc
						int x = addConstantTableEntry(null, left, left.getType(), gf);
						left_ia = new ConstTableIA(x, gf);
//						throw new IllegalStateException("Cant be here");
					}
				} else {
					InstructionArgument x = simplify_expression(left, gf, cctx);
					int y=2;
					left_ia = x;
				}
				List<TypeTableEntry> args1 = new ArrayList<>();
				for (IExpression arg : args) {
					InstructionArgument ia;
					TypeTableEntry iat;
					if (arg.is_simple()) {
						int y=2;
						if (arg instanceof IdentExpression) {
							int x = addIdentTableEntry((IdentExpression) arg, gf);
							// TODO attach to var/const or lookup later in deduce
							ia = new IdentIA(x);
							iat = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, arg);
						} else if (arg instanceof SubExpression) {
							SubExpression se = (SubExpression) arg;
							InstructionArgument ia2 = simplify_expression(se.getExpression(), gf, cctx);
							//return ia;  // TODO is this correct?
							ia = ia2;
							iat = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, arg);
						} else {
							int x = addConstantTableEntry(null, arg, arg.getType(), gf);
							ia = new ConstTableIA(x, gf);
							iat = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, arg.getType(), arg);
						}
					} else {
						InstructionArgument x = simplify_expression(left, gf, cctx);
						int y=2;
						ia = x;
						iat = null;
					}
					right_ia.add(ia);
					args1.add(iat);
				}
				int pte = addProcTableEntry(expression, null, args1, gf);
				right_ia.add(0, new IntegerIA(pte));
				int x = add_i(gf, InstructionName.CALL, right_ia, cctx);
				return new IntegerIA(x);
			}
//			throw new NotImplementedException();
		case DOT_EXP: {
			DotExpression de = (DotExpression) expression;
			IExpression expr = de.getLeft();
			do {
				InstructionArgument i = simplify_expression(expr, gf, cctx);
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
		case LT_: case GT: // TODO all BinaryExpressions go here
			{
				BasicBinaryExpression bbe = (BasicBinaryExpression) expression;
				IExpression left = bbe.getLeft();
				IExpression right = bbe.getRight();
				InstructionArgument left_instruction;
				InstructionArgument right_instruction;
				if (left.is_simple()) {
					if (left instanceof IdentExpression) {
						left_instruction = simplify_expression(left, gf, cctx);
					} else {
						// a constant
						int left_constant_num = addConstantTableEntry2(null, left, left.getType(), gf);
						left_instruction = new ConstTableIA(left_constant_num, gf);
					}
				} else {
					// create a tmp var
//					int left_temp = addTempTableEntry(null, gf);
//					left_instruction = new IntegerIA(left_temp);
					// TODO add_i ??
					left_instruction = simplify_expression(left, gf, cctx);
				}
				if (right.is_simple()) {
					if (right instanceof IdentExpression) {
						right_instruction = simplify_expression(right, gf, cctx);
					} else {
						// a constant
						int right_constant_num = addConstantTableEntry2(null, right, right.getType(), gf);
						right_instruction = new ConstTableIA(right_constant_num, gf);
					}
				} else {
					// create a tmp var
//					int right_temp = addTempTableEntry(null, gf);
//					right_instruction = new IntegerIA(right_temp);
					// TODO add_i ??
					right_instruction = simplify_expression(right, gf, cctx);
				}
				{
					// create a call
					IdentExpression expr_kind_name = Helpers.string_to_ident(SpecialFunctions.of(expressionKind));
//					TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, expr_kind_name);
					TypeTableEntry tte_left  = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, left);
					TypeTableEntry tte_right = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, right);
					int pre_inc = addProcTableEntry(expr_kind_name, null, List_of(tte_left, tte_right), gf);
					int x = add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), left_instruction, right_instruction), cctx);
					return new IntegerIA(x);
				}
//				throw new NotImplementedException();
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

	private Instruction expression_to_call(IExpression expression, GeneratedFunction gf, Context cctx) {
		if (expression.getKind() != PROCEDURE_CALL)
			throw new NotImplementedException();

		switch (expression.getLeft().getKind()){
		case IDENT:
			ProcedureCallExpression pce = (ProcedureCallExpression) expression;
			return expression_to_call_add_entry(gf, pce, (IdentExpression) expression.getLeft(), cctx);
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
	private Instruction expression_to_call_add_entry(GeneratedFunction gf, ProcedureCallExpression pce, IdentExpression left, Context cctx) {
		Instruction i = new Instruction();
		i.setName(InstructionName.CALL);
		List<InstructionArgument> li = new ArrayList<>();
//			int ii = addIdentTableEntry((IdentExpression) expression.getLeft(), gf);
		int ii = addProcTableEntry(left, null, get_args_types(pce.getArgs(), gf), gf);
		li.add(new IntegerIA(ii));
		final List<InstructionArgument> args_ = simplify_args(pce.getArgs(), gf, cctx);
		li.addAll(args_);
		i.setArgs(li);
		return i;
	}

	private int addIdentTableEntry(IdentExpression ident, GeneratedFunction gf) {
//		throw new NotImplementedException();
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

/*
	private int addTempTableEntry(OS_Type type, String name, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type);
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, name, tte);
		gf.vte_list.add(vte);
		return vte.index;
	}
*/

	private int addTempTableEntry(OS_Type type, IdentExpression name, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, name);
		VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, name.getText(), tte);
		gf.vte_list.add(vte);
		return vte.index;
	}

	/**
	 * Add a Constant Table Entry of type with Type Table Entry type Specified
	 * @param name
	 * @param initialValue
	 * @param type
	 * @param gf
	 * @return the cte table index
	 */
	private int addConstantTableEntry(String name, IExpression initialValue, OS_Type type, GeneratedFunction gf) {
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, initialValue);
		ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, tte);
		gf.cte_list.add(cte);
		return cte.index;
	}

	/**
	 * Add a Constant Table Entry of type with Type Table Entry type Transient
	 * @param name
	 * @param initialValue
	 * @param type
	 * @param gf
	 * @return the cte table index
	 */
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
