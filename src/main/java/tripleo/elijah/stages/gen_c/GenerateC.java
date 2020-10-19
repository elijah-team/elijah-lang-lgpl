/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 10/8/20 7:13 AM
 */
public class GenerateC {
	private final OS_Module module;

	public GenerateC(OS_Module m) {
		this.module = m;
	}

	public void generateCode(List<GeneratedFunction> lgf) {
		for (GeneratedFunction generatedFunction : lgf) {
			try {
				generateCodeForMethod(generatedFunction);
			} catch (IOException e) {
				module.parent.eee.exception(e);
			}
		}
	}

	private void generateCodeForMethod(GeneratedFunction gf) throws IOException {
		if (gf.fd == null) return;
		TabbedOutputStream tos = new TabbedOutputStream(System.out);
		String returnType;
		String name;
		//
		// FIND RETURN TYPE
		//
		OS_Type tte = gf.getTypeTableEntry(1).attached;
		if (tte != null) {
			returnType = tte.getClassOf().getName();
		} else {
			returnType = "void";
		}
		//
		name = gf.fd.name();
		String args = String.join(", ", Collections2.transform(gf.fd.fal().falis, new Function<FormalArgListItem, String>() {
			@Nullable
			@Override
			public String apply(@Nullable FormalArgListItem input) {
				return String.format("%s %s", getTypeName(input.tn), input.name);
			}
		}));
		tos.put_string_ln(String.format("%s %s(%s) {", returnType, name, args));
		tos.incr_tabs();
		//
		for (Instruction instruction : gf.instructions()) {
//			System.err.println("8999 "+instruction);
			Label label = gf.findLabel(instruction.getIndex());
			if (label != null) {
				tos.put_string_ln(label.getName()+":");
			}
			switch (instruction.getName()) {
			case E:
				{
					tos.put_string_ln("{");
					tos.incr_tabs();
				}
				break;
			case X:
				{
					tos.dec_tabs();
					tos.put_string_ln("}");
				}
				break;
			case ES:
				{
					tos.put_string_ln("{");
					tos.incr_tabs();
				}
				break;
			case XS:
				{
					tos.dec_tabs();
					tos.put_string_ln("}");
				}
				break;
			case AGN:
				{
					InstructionArgument target = instruction.getArg(0);
					InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					tos.put_string_ln(String.format("%s = %s;", realTarget, getAssignmentValue(value, gf)));
					int y=2;
				}
				break;
			case AGNK:
				{
					InstructionArgument target = instruction.getArg(0);
					InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					tos.put_string_ln(String.format("%s = %s;", realTarget, getAssignmentValue(value, gf)));
					int y=2;
				}
				break;
			case AGNT:
				break;
			case AGNF:
				break;
			case CMP:
				{
					InstructionArgument target = instruction.getArg(0);
					InstructionArgument value  = instruction.getArg(1);

					VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA)target).getIndex());
					assert value != null;

					if (value instanceof ConstTableIA) {
						ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) value).getIndex());
						final String realTargetName = getRealTargetName(gf, (IntegerIA) target);
						tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, getAssignmentValue(value, gf)));
					} else
						tos.put_string_ln(String.format("vsb = %s;", getRealTargetName(gf, (IntegerIA) target)));
					int y=2;
				}
				break;
			case JE:
				{
					InstructionArgument target = instruction.getArg(0);

					Label realTarget = (Label) target;

					tos.put_string_ln(String.format("if (vsb) goto %s;", realTarget.getName()));
					int y=2;
				}
				break;
			case JNE:
				{
					InstructionArgument target = instruction.getArg(0);

					Label realTarget = (Label) target;

					tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
					int y=2;
				}
				break;
			case JL:
				break;
			case JMP:
				{
					InstructionArgument target = instruction.getArg(0);
//					InstructionArgument value  = instruction.getArg(1);

					Label realTarget = (Label) target;

					tos.put_string_ln(String.format("goto %s;", realTarget.getName()));
					int y=2;
				}
				break;
			case CALL:
//				throw new NotImplementedException();
				{
					StringBuilder sb = new StringBuilder();
//					List<String> sl = new ArrayList<String>();
// 					System.err.println("9000 "+inst.getName());
					InstructionArgument x = instruction.getArg(0);
					assert x instanceof IntegerIA;
					ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA) x).getIndex());
					{
						if (pte.expression_num == null) {
							int y = 2;
							IdentExpression ptex = (IdentExpression) pte.expression;
							sb.append(ptex.getText());
						} else {
							OS_Element el = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) pte.expression_num, module);
							System.err.println("8777 " + el);
							IExpression ptex = pte.expression;
							if (ptex instanceof IdentExpression) {
								sb.append(((IdentExpression) ptex).getText());
								{
									sb.append('(');
									final List<String> sl3 = getArgumentStrings(gf, instruction);
									sb.append(String.join(", ", sl3));
									sb.append(");");
								}
							} else if (ptex instanceof ProcedureCallExpression) {
								sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
								{
									sb.append('(');
									final List<String> sl3 = getArgumentStrings(gf, instruction);
									sb.append(String.join(", ", sl3));
									sb.append(");");
								}
							}
						}
					}
				}
				break;
			case CALLS:
//				throw new NotImplementedException();
				break;
			case RET:
				break;
			case YIELD:
				throw new NotImplementedException();
			case TRY:
				throw new NotImplementedException();
			case PC:
				break;
			case IS_A:
				{
					IntegerIA testing_var_   = (IntegerIA)instruction.getArg(0);
					IntegerIA testing_type_  = (IntegerIA)instruction.getArg(1);

					VariableTableEntry testing_var  = gf.getVarTableEntry(testing_var_.getIndex());
					TypeTableEntry     testing_type = gf.getTypeTableEntry(testing_type_.getIndex());

					System.err.println("8887 "+testing_var);
					System.err.println("8888 "+testing_type);

					OS_Type x = testing_type.attached;
					TypeName y = x.getTypeName();
					if (y instanceof NormalTypeName) {
						String z = ((NormalTypeName) y).getName();
						tos.put_string_ln(String.format("vsb = ZS<%s>_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
					} else System.err.println("8886 "+y.getClass().getName());
//					Label realTarget = (Label) target;
//
//					tos.put_string_ln(String.format("goto %s;", realTarget.getName()));
					int yyy=2;
				}
				break;
			case DECL:
				{
					InstructionArgument decl_type = instruction.getArg(0);
					IntegerIA vte_num = (IntegerIA) instruction.getArg(1);
					String target_name = getRealTargetName(gf, vte_num);
					VariableTableEntry vte = gf.getVarTableEntry(vte_num.getIndex());

					OS_Type x = vte.type.attached;
					if (x != null) {
						TypeName y = x.getTypeName();
						if (y instanceof NormalTypeName) {
							String z = ((NormalTypeName) y).getName();
							tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
						} else {
							if (y != null) {
								//
								// VARIABLE WASN'T FULLY DEDUCED YET
								//
								System.err.println("8887 "+y.getClass().getName());
							} else {
								//
								// VARIABLE WASN'T FULLY DEDUCED YET
								// MTL A TEMP VARIABLE
								//
								@NotNull Collection<TypeTableEntry> pt_ = vte.potentialTypes();
								List<TypeTableEntry> pt = new ArrayList<>(pt_);
								if (pt.size() == 1) {
									TypeTableEntry ty = pt.get(0);
//									System.err.println("8885 " +ty.attached);
									assert ty.attached.getType() == OS_Type.Type.USER_CLASS;
									ClassStatement el = ty.attached.getClassOf();
									String z = el.getName();
									tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
								} else {
									assert x.getType() == OS_Type.Type.BUILT_IN;
									OS_Type el = x.resolve(gf.getFD().getContext());
									System.err.println("Bad potentialTypes size "+el);
								}
//								System.err.println("8886 y is null (No typename specified)");
							}
						}
					} else {
						System.err.println("8885 x is null (No typename specified)");
					}
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + instruction.getName());
			}
		}
		tos.dec_tabs();
		tos.put_string_ln("}");
		tos.flush();
		tos.close();
	}

	private String getTypeName(TypeName typeName) {
		if (typeName instanceof RegularTypeName) {
			String name = ((RegularTypeName) typeName).getName(); // TODO convert to Z-name

			return name;
		}
		return ""+typeName; // TODO type is not fully deduced
	}

	@NotNull
	private List<String> getArgumentStrings(GeneratedFunction gf, Instruction instruction) {
		final List<String> sl3 = new ArrayList<String>();
		int args_size = instruction.getArgsSize();
		for (int i = 1; i < args_size; i++) {
			InstructionArgument ia = instruction.getArg(i);
			if (ia instanceof IntegerIA) {
//				VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(ia));
				String realTargetName = getRealTargetName(gf, (IntegerIA) ia);
				sl3.add(realTargetName);
			} else if (ia instanceof IdentIA) {
				int y=2;
				sl3.add("<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>");
			} else {
				System.err.println(ia.getClass().getName());
				throw new NotImplementedException();
			}
		}
		return sl3;
	}

	private String getAssignmentValue(InstructionArgument value, GeneratedFunction gf) {
		StringBuilder sb = new StringBuilder();
		if (value instanceof FnCallArgs) {
			FnCallArgs fca = (FnCallArgs) value;
			List<String> sl = new ArrayList<String>();
			Instruction inst = fca.getExpression();
//			System.err.println("9000 "+inst.getName());
			InstructionArgument x = inst.getArg(0);
			assert x instanceof IntegerIA;
			ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA)x).getIndex());
			switch (inst.getName()) {
			case CALL:
				{
					if (pte.expression_num == null) {
						int y=2;
						IdentExpression ptex = (IdentExpression) pte.expression;
						sb.append(ptex.getText());
					} else {
						OS_Element el = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) pte.expression_num, module);
						System.err.println("8777 " + el);
						IExpression ptex = pte.expression;
						if (ptex instanceof IdentExpression) {
							sb.append(((IdentExpression) ptex).getText());
						} else if (ptex instanceof ProcedureCallExpression) {
							sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
						}
					}
					sb.append("(");
					{
						int args_size = inst.getArgsSize();
						List<String> sll = new ArrayList<>();
						for (int i = 1; i < args_size; i++) {
							InstructionArgument ia = inst.getArg(i);
							int y=2;
							System.err.println("7777 " +ia);
							if (ia instanceof ConstTableIA) {
								ConstantTableEntry constTableEntry = gf.getConstTableEntry(((ConstTableIA) ia).getIndex());
								sll.add(""+ const_to_string(constTableEntry.initialValue));
							} else if (ia instanceof IntegerIA) {
								VariableTableEntry variableTableEntry = gf.getVarTableEntry(((IntegerIA) ia).getIndex());
								sll.add("" + variableTableEntry.getName());
							} else if (ia instanceof IdentIA) {
								@org.jetbrains.annotations.Nullable OS_Element ident = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) ia, module);
								//String path = gf.getIAPath((IdentIA) ia));
								assert ident != null;
								throw new NotImplementedException();
							} else {
								throw new IllegalStateException("Cant be here");
							}
						}
						sb.append(String.join(", ", sll));
					}
					sb.append(");");
					return sb.toString();
				}
			case CALLS:
				{
					if (pte.expression_num == null) {
						int y=2;
						IdentExpression ptex = (IdentExpression) pte.expression;
						sb.append(ptex.getText());
					} else {
						IExpression ptex = pte.expression;
						if (ptex instanceof IdentExpression) {
							sb.append(((IdentExpression) ptex).getText());
						} else if (ptex instanceof ProcedureCallExpression) {
							sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
						}
					}
					break;
				}
			default:
				throw new NotImplementedException();
			}
//			System.err.println("9000-2 "+pte);
			for (InstructionArgument arg : fca.getInstructionArguments()) {
//				System.err.println("9000-1 "+arg);
				if (arg instanceof IntegerIA) {
					VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) arg).getIndex());
					sl.add(getRealTargetName(gf, (IntegerIA) arg));
				} else if (arg instanceof ConstTableIA) {
					ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA)arg).getIndex());
					System.err.println(("9000-3 "+cte.initialValue));
					if (cte.initialValue instanceof NumericExpression) {
						sl.add(""+((NumericExpression) cte.initialValue).getValue());
					} else
						throw new NotImplementedException();
				} else
					throw new NotImplementedException();
			}

			sb.append('(');
			sb.append(String.join(", ", sl));
			sb.append(')');
			return sb.toString();
		}

		if (value instanceof ConstTableIA) {
			ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) value).getIndex());
			System.err.println(("9001-3 "+cte.initialValue));
			if (cte.initialValue instanceof NumericExpression) {
				return (""+((NumericExpression) cte.initialValue).getValue());
			} else if (cte.initialValue instanceof IdentExpression) {
				String text = ((IdentExpression) cte.initialValue).getText();
				if (text.equals("true") || text.equals("false"))
					return text;
				else
					throw new NotImplementedException();
			} else
				throw new NotImplementedException();
		}

		return ""+value;
	}

	private String const_to_string(IExpression expression) {
		if (expression instanceof NumericExpression) {
			return ""+((NumericExpression) expression).getValue();
		}
		// StringExpression
		// FloatLitExpression
		// CharListExpression
		// BooleanExpression
		throw new NotImplementedException();
	}

	private String getRealTargetName(GeneratedFunction gf, IntegerIA target) {
		final VariableTableEntry varTableEntry = gf.getVarTableEntry(target.getIndex());
		final String vte_name = varTableEntry.getName();
		if (varTableEntry.vtt == VariableTableType.TEMP) {
			int tempNum = varTableEntry.tempNum;
			if (tempNum == -1) {
				varTableEntry.tempNum = gf.nextTemp();
				tempNum = varTableEntry.tempNum;
			}
			return "vt" + tempNum;
		} else if (varTableEntry.vtt == VariableTableType.ARG) {
			return "va" + vte_name;
		} else if (SpecialVariables.contains(vte_name)) {
			return SpecialVariables.get(vte_name);
		}
		return "vv" + vte_name;
	}
}

//
//
//
