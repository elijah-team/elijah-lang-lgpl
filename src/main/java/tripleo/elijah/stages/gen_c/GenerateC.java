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

	public GenerateC(final OS_Module m) {
		this.module = m;
	}

	public void generateCode(final List<GeneratedFunction> lgf) {
		for (final GeneratedFunction generatedFunction : lgf) {
			try {
				generateCodeForMethod(generatedFunction);
			} catch (final IOException e) {
				module.parent.eee.exception(e);
			}
		}
	}

	private void generateCodeForMethod(final GeneratedFunction gf) throws IOException {
		if (gf.fd == null) return;
		final TabbedOutputStream tos = new TabbedOutputStream(System.out);
		final String returnType;
		final String name;
		//
		// FIND RETURN TYPE
		//
		final OS_Type tte = gf.getTypeTableEntry(1).attached;
		if (tte != null) {
			returnType = tte.getClassOf().getName();
		} else {
			returnType = "void";
		}
		//
		name = gf.fd.name();
		final String args = String.join(", ", Collections2.transform(gf.fd.fal().falis, new Function<FormalArgListItem, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final FormalArgListItem input) {
				return String.format("%s %s", getTypeName(input.tn), input.name);
			}
		}));
		tos.put_string_ln(String.format("%s %s(%s) {", returnType, name, args));
		tos.incr_tabs();
		//
		for (final Instruction instruction : gf.instructions()) {
//			System.err.println("8999 "+instruction);
			final Label label = gf.findLabel(instruction.getIndex());
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
					final InstructionArgument target = instruction.getArg(0);
					final InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					tos.put_string_ln(String.format("%s = %s;", realTarget, getAssignmentValue(value, gf)));
					final int y=2;
				}
				break;
			case AGNK:
				{
					final InstructionArgument target = instruction.getArg(0);
					final InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					tos.put_string_ln(String.format("%s = %s;", realTarget, getAssignmentValue(value, gf)));
					final int y=2;
				}
				break;
			case AGNT:
				break;
			case AGNF:
				break;
			case CMP:
				{
					final InstructionArgument target = instruction.getArg(0);
					final InstructionArgument value  = instruction.getArg(1);

					final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA)target).getIndex());
					assert value != null;

					if (value instanceof ConstTableIA) {
						final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) value).getIndex());
						final String realTargetName = getRealTargetName(gf, (IntegerIA) target);
						tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, getAssignmentValue(value, gf)));
					} else
						tos.put_string_ln(String.format("vsb = %s;", getRealTargetName(gf, (IntegerIA) target)));
					final int y=2;
				}
				break;
			case JE:
				{
					final InstructionArgument target = instruction.getArg(0);

					final Label realTarget = (Label) target;

					tos.put_string_ln(String.format("if (vsb) goto %s;", realTarget.getName()));
					final int y=2;
				}
				break;
			case JNE:
				{
					final InstructionArgument target = instruction.getArg(0);

					final Label realTarget = (Label) target;

					tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
					final int y=2;
				}
				break;
			case JL:
				break;
			case JMP:
				{
					final InstructionArgument target = instruction.getArg(0);
//					InstructionArgument value  = instruction.getArg(1);

					final Label realTarget = (Label) target;

					tos.put_string_ln(String.format("goto %s;", realTarget.getName()));
					final int y=2;
				}
				break;
			case CALL:
//				throw new NotImplementedException();
				{
					final StringBuilder sb = new StringBuilder();
//					List<String> sl = new ArrayList<String>();
// 					System.err.println("9000 "+inst.getName());
					final InstructionArgument x = instruction.getArg(0);
					assert x instanceof IntegerIA;
					final ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA) x).getIndex());
					{
						if (pte.expression_num == null) {
							final IdentExpression ptex = (IdentExpression) pte.expression;
							String text = ptex.getText();
							@org.jetbrains.annotations.Nullable InstructionArgument xx = gf.vte_lookup(text);
							assert xx != null;
							String xxx = getRealTargetName(gf, (IntegerIA) xx);
							sb.append(xxx);
						} else {
							String path = gf.getIdentIAPath((IdentIA) pte.expression_num);
							sb.append(path);
						}
						{
							sb.append('(');
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							sb.append(String.join(", ", sl3));
							sb.append(");");
						}
					}
					tos.put_string_ln(sb.toString());
				}
				break;
			case CALLS:
//				throw new NotImplementedException();
				{
					final StringBuilder sb = new StringBuilder();
					final InstructionArgument x = instruction.getArg(0);
					assert x instanceof IntegerIA;
					final ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA) x).getIndex());
					{
						if (pte.expression_num == null) {
							final int y = 2;
							final IdentExpression ptex = (IdentExpression) pte.expression;
							String text = ptex.getText();
							@org.jetbrains.annotations.Nullable InstructionArgument xx = gf.vte_lookup(text);
							String xxx;
							if (xx != null) {
								xxx = getRealTargetName(gf, (IntegerIA) xx);
							} else {
								xxx = text;
								System.err.println("xxx is null "+text);
							}
							sb.append(xxx);
						} else {
							String path = gf.getIdentIAPath((IdentIA) pte.expression_num);
							sb.append(path);
						}
						{
							sb.append('(');
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							sb.append(String.join(", ", sl3));
							sb.append(");");
						}
					}
					tos.put_string_ln(sb.toString());
				}
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
					final IntegerIA testing_var_   = (IntegerIA)instruction.getArg(0);
					final IntegerIA testing_type_  = (IntegerIA)instruction.getArg(1);

					final VariableTableEntry testing_var  = gf.getVarTableEntry(testing_var_.getIndex());
					final TypeTableEntry     testing_type = gf.getTypeTableEntry(testing_type_.getIndex());

					System.err.println("8887 "+testing_var);
					System.err.println("8888 "+testing_type);

					final OS_Type x = testing_type.attached;
					final TypeName y = x.getTypeName();
					if (y instanceof NormalTypeName) {
						final String z = ((NormalTypeName) y).getName();
						tos.put_string_ln(String.format("vsb = ZS<%s>_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
					} else System.err.println("8886 "+y.getClass().getName());
//					Label realTarget = (Label) target;
//
//					tos.put_string_ln(String.format("goto %s;", realTarget.getName()));
					final int yyy=2;
				}
				break;
			case DECL:
				{
					final InstructionArgument decl_type = instruction.getArg(0);
					final IntegerIA vte_num = (IntegerIA) instruction.getArg(1);
					final String target_name = getRealTargetName(gf, vte_num);
					final VariableTableEntry vte = gf.getVarTableEntry(vte_num.getIndex());

					final OS_Type x = vte.type.attached;
					if (x != null) {
						final TypeName y = x.getTypeName();
						if (y instanceof NormalTypeName) {
							final String z = ((NormalTypeName) y).getName();
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
								@NotNull final Collection<TypeTableEntry> pt_ = vte.potentialTypes();
								final List<TypeTableEntry> pt = new ArrayList<>(pt_);
								if (pt.size() == 1) {
									final TypeTableEntry ty = pt.get(0);
//									System.err.println("8885 " +ty.attached);
									final OS_Type attached = ty.attached;
									assert attached != null;
									final String z = getTypeName(attached);
									tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
								} else {
									assert x.getType() == OS_Type.Type.BUILT_IN;
									final Context context = gf.getFD().getContext();
									assert context != null;
									final OS_Type type = x.resolve(context);
//									System.err.println("Bad potentialTypes size "+type);
									final String z = getTypeName(type);
									tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
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

	private String getTypeName(final OS_Type ty) {
		assert ty != null;
		assert ty.getType() == OS_Type.Type.USER_CLASS;
		final ClassStatement el = ty.getClassOf();
		final String z = el.getName();
		return z;
	}

	private String getTypeName(final TypeName typeName) {
		if (typeName instanceof RegularTypeName) {
			final String name = ((RegularTypeName) typeName).getName(); // TODO convert to Z-name

			return name;
		}
		return ""+typeName; // TODO type is not fully deduced
	}

	@NotNull
	private List<String> getArgumentStrings(final GeneratedFunction gf, final Instruction instruction) {
		final List<String> sl3 = new ArrayList<String>();
		final int args_size = instruction.getArgsSize();
		for (int i = 1; i < args_size; i++) {
			final InstructionArgument ia = instruction.getArg(i);
			if (ia instanceof IntegerIA) {
//				VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(ia));
				final String realTargetName = getRealTargetName(gf, (IntegerIA) ia);
				sl3.add(realTargetName);
			} else if (ia instanceof IdentIA) {
				final int y=2;
				sl3.add("<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>");
			} else {
				System.err.println(ia.getClass().getName());
				throw new NotImplementedException();
			}
		}
		return sl3;
	}

	private String getAssignmentValue(final InstructionArgument value, final GeneratedFunction gf) {
		final StringBuilder sb = new StringBuilder();
		if (value instanceof FnCallArgs) {
			final FnCallArgs fca = (FnCallArgs) value;
//			List<String> sl = new ArrayList<String>();
			final Instruction inst = fca.getExpression();
//			System.err.println("9000 "+inst.getName());
			final InstructionArgument x = inst.getArg(0);
			assert x instanceof IntegerIA;
			final ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA)x).getIndex());
			switch (inst.getName()) {
			case CALL:
				{
					if (pte.expression_num == null) {
//						assert false; // TODO synthetic methods
						final int y=2;
						final IdentExpression ptex = (IdentExpression) pte.expression;
						sb.append(ptex.getText());
					} else {
//						final OS_Element el = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) pte.expression_num, module);
						String path = gf.getIdentIAPath((IdentIA) pte.expression_num);
//						System.err.println("8677 " + path);
/*
						final IExpression ptex = pte.expression;
						if (ptex instanceof IdentExpression) {
							sb.append(((IdentExpression) ptex).getText());
						} else if (ptex instanceof ProcedureCallExpression) {
							sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
						} else
							throw new IllegalStateException("ptex is "+ptex.getClass().getName());
*/
						sb.append(path);
					}
					sb.append("(");
					{
						final List<String> sll = getAssignmentValueArgs(inst, gf);
						sb.append(String.join(", ", sll));
					}
					sb.append(");");
					return sb.toString();
				}
			case CALLS:
				{
					if (pte.expression_num == null) {
						final int y=2;
						final IdentExpression ptex = (IdentExpression) pte.expression;
						sb.append(ptex.getText());
					} else {
						final IExpression ptex = pte.expression;
						if (ptex instanceof IdentExpression) {
							sb.append(((IdentExpression) ptex).getText());
						} else if (ptex instanceof ProcedureCallExpression) {
							sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
						}
					}
					sb.append("(");
					{
						final List<String> sll = getAssignmentValueArgs(inst, gf);
						sb.append(String.join(", ", sll));
					}
					sb.append(");");
					return sb.toString();
				}
			default:
				throw new NotImplementedException();
			}
//			System.err.println("9000-2 "+pte);
/*
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
*/
		}

		if (value instanceof ConstTableIA) {
			final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) value).getIndex());
			System.err.println(("9001-3 "+cte.initialValue));
			if (cte.initialValue instanceof NumericExpression) {
				return (""+((NumericExpression) cte.initialValue).getValue());
			} else if (cte.initialValue instanceof IdentExpression) {
				final String text = ((IdentExpression) cte.initialValue).getText();
				if (text.equals("true") || text.equals("false"))
					return text;
				else
					throw new NotImplementedException();
			} else
				throw new NotImplementedException();
		}

		return ""+value;
	}

	@NotNull
	private List<String> getAssignmentValueArgs(final Instruction inst, final GeneratedFunction gf) {
		final int args_size = inst.getArgsSize();
		final List<String> sll = new ArrayList<>();
		for (int i = 1; i < args_size; i++) {
			final InstructionArgument ia = inst.getArg(i);
			final int y=2;
			System.err.println("7777 " +ia);
			if (ia instanceof ConstTableIA) {
				final ConstantTableEntry constTableEntry = gf.getConstTableEntry(((ConstTableIA) ia).getIndex());
				sll.add(""+ const_to_string(constTableEntry.initialValue));
			} else if (ia instanceof IntegerIA) {
				final VariableTableEntry variableTableEntry = gf.getVarTableEntry(((IntegerIA) ia).getIndex());
				sll.add("" + variableTableEntry.getName());
			} else if (ia instanceof IdentIA) {
				@org.jetbrains.annotations.Nullable final OS_Element ident = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) ia, module);
				//String path = gf.getIAPath((IdentIA) ia));
				assert ident != null;
				throw new NotImplementedException();
			} else {
				throw new IllegalStateException("Cant be here");
			}
		}
		return sll;
	}

	private String const_to_string(final IExpression expression) {
		if (expression instanceof NumericExpression) {
			return ""+((NumericExpression) expression).getValue();
		}
		// StringExpression
		// FloatLitExpression
		// CharListExpression
		// BooleanExpression
		throw new NotImplementedException();
	}

	private String getRealTargetName(final GeneratedFunction gf, final IntegerIA target) {
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
