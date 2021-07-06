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
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.FormalArgListItem;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.NormalTypeName;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.gen_fn.ConstantTableEntry;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedContainerNC;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.BufferTabbedOutputStream;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkList;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

/**
 * Created 6/21/21 5:53 AM
 */
public class Generate_Code_For_Method {
	public Generate_Code_For_Method(@NotNull GenerateC aGenerateC) {
		gc = aGenerateC;
	}

	GenerateC gc;

	final BufferTabbedOutputStream tosHdr = new BufferTabbedOutputStream();
	final BufferTabbedOutputStream tos = new BufferTabbedOutputStream();

	void generateCodeForMethod(GeneratedFunction gf, GenerateResult gr, WorkList aWorkList) {
		action(gf);
		
		tos.flush();
		tos.close();
		tosHdr.flush();
		tosHdr.close();
		Buffer buf = tos.getBuffer();
//		System.out.println(buf.getText());
		gr.addFunction(gf, buf, GenerateResult.TY.IMPL);
		Buffer bufHdr = tosHdr.getBuffer();
//		System.out.println(bufHdr.getText());
		gr.addFunction(gf, bufHdr, GenerateResult.TY.HEADER);
	}

	boolean is_constructor = false, is_unit_type = false;

	private void action(GeneratedFunction gf) {
		Generate_Method_Header gmh = new Generate_Method_Header(gf, gc);

		tos.put_string_ln(String.format("%s {", gmh.header_string));
		tosHdr.put_string_ln(String.format("%s;", gmh.header_string));

		tos.incr_tabs();
		//
		@NotNull List<Instruction> instructions = gf.instructions();
		for (int instruction_index = 0; instruction_index < instructions.size(); instruction_index++) {
			Instruction instruction = instructions.get(instruction_index);
//			System.err.println("8999 "+instruction);
			final Label label = gf.findLabel(instruction.getIndex());
			if (label != null) {
				tos.put_string_ln_no_tabs(label.getName() + ":");
			}

			switch (instruction.getName()) {
				case E:
					action_E(gf, gmh);
					break;
				case X:
					action_X(gmh);
					break;
				case ES:
					action_ES();
					break;
				case XS:
					action_XS();
					break;
				case AGN:
					action_AGN(gf, instruction);
					break;
				case AGNK:
					action_AGNK(gf, instruction);
					break;
				case AGNT:
					break;
				case AGNF:
					break;
				case JE:
					action_JE(gf, instruction);
					break;
				case JNE:
					action_JNE(gf, instruction);
					break;
				case JL:
					action_JL(gf, instruction);
					break;
				case JMP:
					action_JMP(instruction);
					break;
				case CONSTRUCT:
					action_CONSTRUCT(gf, instruction);
					break;
				case CALL:
					action_CALL(gf, instruction);
					break;
				case CALLS:
					action_CALLS(gf, instruction);
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
					action_IS_A(instruction, tos, gf);
					break;
				case DECL:
					action_DECL(instruction, tos, gf);
					break;
				case CAST_TO:
					action_CAST(instruction, tos, gf);
					break;
				case NOP:
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + instruction.getName());
			}
		}
		tos.dec_tabs();
		tos.put_string_ln("}");
	}

	private void action_XS() {
		tos.dec_tabs();
		tos.put_string_ln("}");
	}

	private void action_ES() {
		tos.put_string_ln("{");
		tos.incr_tabs();
	}

	private void action_X(Generate_Method_Header aGmh) {
		tos.dec_tabs();
		tos.put_string_ln("}");
		if (!is_unit_type)
			if (is_constructor || aGmh.tte != null && aGmh.tte.isResolved()) {
				tos.put_string_ln("return vsr;");
			}
	}

	private void action_E(GeneratedFunction gf, Generate_Method_Header aGmh) {
		tos.put_string_ln("bool vsb;");
		int state = 0;

		if (gf.getFD() instanceof ConstructorDef)
			state = 2;
		else if (aGmh.tte == null)
			state = 3;
		else if (aGmh.tte.isResolved())
			state = 1;
		else if (aGmh.tte.getAttached() instanceof OS_Type.OS_UnitType)
			state = 4;

		switch (state) {
			case 0:
				tos.put_string_ln("Error_TTE_Not_Resolved "+ aGmh.tte);
				break;
			case 1:
				String ty = gc.getTypeName(aGmh.tte);
				tos.put_string_ln(String.format("%s vsr;", ty));
				break;
			case 2:
			case 3:
				// Assuming ctor
				is_constructor = true;
				final GeneratedNode genClass = gf.getGenClass();
				String ty2 = gc.getTypeNameForGenClass(genClass);
				tos.put_string_ln(String.format("%s vsr;", ty2));
				break;
			case 4:
				// don't print anything
				is_unit_type = true;
				break;
		}
		tos.put_string_ln("{");
		tos.incr_tabs();
	}

	private void action_AGN(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument target = aInstruction.getArg(0);
		final InstructionArgument value  = aInstruction.getArg(1);

		final String realTarget;
		if (target instanceof IntegerIA) {
			realTarget = gc.getRealTargetName(gf, (IntegerIA) target);
		} else {
			realTarget = gc.getRealTargetName(gf, (IdentIA) target);
		}
		final String assignmentValue = gc.getAssignmentValue(gf.getSelf(), value, gf);
		String s = String.format(Emit.emit("/*267*/")+"%s = %s;", realTarget, assignmentValue);
		tos.put_string_ln(s);
	}

	private void action_AGNK(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument target = aInstruction.getArg(0);
		final InstructionArgument value  = aInstruction.getArg(1);

		final String realTarget = gc.getRealTargetName(gf, (IntegerIA) target);
		final String assignmentValue = gc.getAssignmentValue(gf.getSelf(), value, gf);
		String s = String.format(Emit.emit("/*278*/")+"%s = %s;", realTarget, assignmentValue);
		tos.put_string_ln(s);
	}

	private void action_CALLS(GeneratedFunction gf, Instruction aInstruction) {
		final StringBuilder sb = new StringBuilder();
		final InstructionArgument x = aInstruction.getArg(0);
		assert x instanceof ProcIA;
		final ProcTableEntry pte = gf.getProcTableEntry(to_int(x));
		{
			CReference reference = null;
			if (pte.expression_num == null) {
				final int y = 2;
				final IdentExpression ptex = (IdentExpression) pte.expression;
				String text = ptex.getText();
				@Nullable InstructionArgument xx = gf.vte_lookup(text);
				String xxx;
				if (xx != null) {
					xxx = gc.getRealTargetName(gf, (IntegerIA) xx);
				} else {
					xxx = text;
					System.err.println("xxx is null " + text);
				}
				sb.append(Emit.emit("/*460*/")+xxx);
			} else {
				final IdentIA ia2 = (IdentIA) pte.expression_num;
				reference = new CReference();
				reference.getIdentIAPath(ia2, gf);
				final List<String> sl3 = gc.getArgumentStrings(gf, aInstruction);
				reference.args(sl3);
				String path = reference.build();
				sb.append(Emit.emit("/*463*/")+path);
			}
			if (reference == null){
				sb.append('(');
				final List<String> sl3 = gc.getArgumentStrings(gf, aInstruction);
				sb.append(Helpers.String_join(", ", sl3));
				sb.append(");");
			}
		}
		tos.put_string_ln(sb.toString());
	}

	private void action_CALL(GeneratedFunction gf, Instruction aInstruction) {
		final StringBuilder sb = new StringBuilder();
// 					System.err.println("9000 "+inst.getName());
		final InstructionArgument x = aInstruction.getArg(0);
		assert x instanceof ProcIA;
		final ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) x).getIndex());
		{
			if (pte.expression_num == null) {
				final IdentExpression ptex = (IdentExpression) pte.expression;
				String text = ptex.getText();
				@Nullable InstructionArgument xx = gf.vte_lookup(text);
				assert xx != null;
				String realTargetName = gc.getRealTargetName(gf, (IntegerIA) xx);
				sb.append(Emit.emit("/*424*/")+realTargetName);
				sb.append('(');
				final List<String> sl3 = gc.getArgumentStrings(gf, aInstruction);
				sb.append(Helpers.String_join(", ", sl3));
				sb.append(");");
			} else {
				final CReference reference = new CReference();
				final IdentIA ia2 = (IdentIA) pte.expression_num;
				reference.getIdentIAPath(ia2, gf);
				final List<String> sl3 = gc.getArgumentStrings(gf, aInstruction);
				reference.args(sl3);
				String path = reference.build();

				sb.append(Emit.emit("/*427*/")+path+";");
			}
		}
		tos.put_string_ln(sb.toString());
	}

	private void action_CONSTRUCT(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument _arg0 = aInstruction.getArg(0);
		assert _arg0 instanceof ProcIA;
		final ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) _arg0).getIndex());
		List<TypeTableEntry> x = pte.getArgs();
		int y = aInstruction.getArgsSize();
//					InstructionArgument z = instruction.getArg(1);
		ClassInvocation clsinv = pte.getClassInvocation();
		if (clsinv != null) {

			final InstructionArgument target = pte.expression_num;
//						final InstructionArgument value  = instruction;

			if (target instanceof IdentIA) {
				// how to tell between named ctors and just a path?
			}

//						final String realTarget;
//						if (target instanceof IntegerIA) {
//							realTarget = getRealTargetName(gf, (IntegerIA) target);
//						} else if (target instanceof IdentIA) {
//							realTarget = getRealTargetName(gf, (IdentIA) target);
//						} else {
//							throw new NotImplementedException();
//						}
//						String s = String.format(Emit.emit("/*500*/")+"%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), instruction, clsinv, gf));
			String s = String.format(Emit.emit("/*500*/")+"%s;", gc.getAssignmentValue(gf.getSelf(), aInstruction, clsinv, gf));
			tos.put_string_ln(s);
		}
	}

	private void action_JMP(Instruction aInstruction) {
		final InstructionArgument target = aInstruction.getArg(0);
//					InstructionArgument value  = instruction.getArg(1);

		final Label realTarget = (Label) target;

		tos.put_string_ln(String.format("goto %s;", realTarget.getName()));
		final int y=2;
	}

	private void action_JL(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument lhs    = aInstruction.getArg(0);
		final InstructionArgument rhs    = aInstruction.getArg(1);
		final InstructionArgument target = aInstruction.getArg(2);

		final Label realTarget = (Label) target;

		final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) lhs).getIndex());
		assert rhs != null;

		if (rhs instanceof ConstTableIA) {
			final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
			final String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s < %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
		} else {
			//
			// TODO need to lookup special __lt__ function
			//
			String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s < %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

			final int y = 2;
		}
	}

	private void action_JNE(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument lhs    = aInstruction.getArg(0);
		final InstructionArgument rhs    = aInstruction.getArg(1);
		final InstructionArgument target = aInstruction.getArg(2);

		final Label realTarget = (Label) target;

		final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) lhs).getIndex());
		assert rhs != null;

		if (rhs instanceof ConstTableIA) {
			final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
			final String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s != %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
		} else {
			//
			// TODO need to lookup special __ne__ function ??
			//
			String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s != %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

			final int y = 2;
		}
	}

	private void action_JE(GeneratedFunction gf, Instruction aInstruction) {
		final InstructionArgument lhs    = aInstruction.getArg(0);
		final InstructionArgument rhs    = aInstruction.getArg(1);
		final InstructionArgument target = aInstruction.getArg(2);

		final Label realTarget = (Label) target;

		final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA)lhs).getIndex());
		assert rhs != null;

		if (rhs instanceof ConstTableIA) {
			final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
			final String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
		} else {
			//
			// TODO need to lookup special __eq__ function
			//
			String realTargetName = gc.getRealTargetName(gf, (IntegerIA) lhs);
			tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, gc.getAssignmentValue(gf.getSelf(), rhs, gf)));
			tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

			final int y = 2;
		}
	}

	private void action_IS_A(Instruction instruction, BufferTabbedOutputStream tos, GeneratedFunction gf) {
		final IntegerIA testing_var_  = (IntegerIA) instruction.getArg(0);
		final IntegerIA testing_type_ = (IntegerIA) instruction.getArg(1);
		final Label target_label      = ((LabelIA) instruction.getArg(2)).label;

		final VariableTableEntry testing_var    = gf.getVarTableEntry(testing_var_.getIndex());
		final TypeTableEntry     testing_type__ = gf.getTypeTableEntry(testing_type_.getIndex());

		GeneratedNode testing_type = testing_type__.resolved();
		final int z = ((GeneratedContainerNC) testing_type).getCode();

		tos.put_string_ln(String.format("vsb = ZS%d_is_a(%s);", z, gc.getRealTargetName(gf, testing_var_)));
		tos.put_string_ln(String.format("if (!vsb) goto %s;", target_label.getName()));
	}

	private void action_CAST(Instruction instruction, BufferTabbedOutputStream tos, GeneratedFunction gf) {
		final IntegerIA  vte_num_ = (IntegerIA) instruction.getArg(0);
		final IntegerIA vte_type_ = (IntegerIA) instruction.getArg(1);
		final IntegerIA vte_targ_ = (IntegerIA) instruction.getArg(2);
		final String target_name = gc.getRealTargetName(gf, vte_num_);
		final TypeTableEntry target_type_ = gf.getTypeTableEntry(vte_type_.getIndex());
		final String target_type = gc.getTypeName(target_type_.getAttached());
		final String source_target = gc.getRealTargetName(gf, vte_targ_);

		tos.put_string_ln(String.format("%s = (%s)%s;", target_name, target_type, source_target));
	}

	private void action_DECL(Instruction instruction, BufferTabbedOutputStream tos, GeneratedFunction gf) {
		final SymbolIA decl_type = (SymbolIA)  instruction.getArg(0);
		final IntegerIA  vte_num = (IntegerIA) instruction.getArg(1);
		final String target_name = gc.getRealTargetName(gf, vte_num);
		final VariableTableEntry vte = gf.getVarTableEntry(vte_num.getIndex());

		final OS_Type x = vte.type.getAttached();
		if (x == null) {
			if (vte.vtt == VariableTableType.TEMP) {
				System.err.println("8884 temp variable has no type "+vte+" "+gf);
			} else {
				System.err.println("8885 x is null (No typename specified) for " + vte.getName());
			}
			return;
		}

		final GeneratedNode res = vte.resolvedType();
		if (res instanceof GeneratedClass) {
			final String z = gc.getTypeName((GeneratedClass) res);
			tos.put_string_ln(String.format("%s* %s;", z, target_name));
			return;
		}

		if (x.getType() == OS_Type.Type.USER_CLASS) {
			final String z = gc.getTypeName(x);
			tos.put_string_ln(String.format("%s* %s;", z, target_name));
			return;
		} else if (x.getType() == OS_Type.Type.USER) {
			final TypeName y = x.getTypeName();
			if (y instanceof NormalTypeName) {
				final String z;
				if (((NormalTypeName) y).getName().equals("Any"))
					z = "void *";  // TODO Technically this is wrong
				else
					z = gc.getTypeName(y);
				tos.put_string_ln(String.format("%s %s;", z, target_name));
				return;
			}

			if (y != null) {
				//
				// VARIABLE WASN'T FULLY DEDUCED YET
				//
				System.err.println("8885 "+y.getClass().getName());
				return;
			}
		} else if (x.getType() == OS_Type.Type.BUILT_IN) {
			final Context context = gf.getFD().getContext();
			assert context != null;
			final OS_Type type = x.resolve(context);
			if (type.isUnitType()) {
				// TODO still should not happen
				tos.put_string_ln(String.format("/*%s is declared as the Unit type*/", target_name));
			} else {
//				System.err.println("Bad potentialTypes size " + type);
				final String z = gc.getTypeName(type);
				tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
			}
		}

		//
		// VARIABLE WASN'T FULLY DEDUCED YET
		// MTL A TEMP VARIABLE
		//
		@NotNull final Collection<TypeTableEntry> pt_ = vte.potentialTypes();
		final List<TypeTableEntry> pt = new ArrayList<TypeTableEntry>(pt_);
		if (pt.size() == 1) {
			final TypeTableEntry ty = pt.get(0);
//			System.err.println("8885 " +ty.attached);
			final OS_Type attached = ty.getAttached();
			final String z;
			if (attached != null)
				z = gc.getTypeName(attached);
			else
				z = Emit.emit("/*763*/")+"Unknown";
			tos.put_string_ln(String.format("/*8890*/Z<%s> %s;", z, target_name));
		}
		System.err.println("8886 y is null (No typename specified)");
	}

	static class Generate_Method_Header {

		private final String return_type;
		private final String args_string;
		private final String header_string;
		private final String name;
		private final GenerateC gc;
		OS_Type type;
		TypeTableEntry tte;

		public Generate_Method_Header(@NotNull GeneratedFunction gf, @NotNull GenerateC aGenerateC) {
			gc            = aGenerateC;
			name          = gf.getFD().name();
			//
			return_type   = find_return_type(gf);
			args_string   = find_args_string(gf);
			header_string = find_header_string(gf);
		}

		String find_header_string(GeneratedFunction gf) {
			GeneratedContainerNC parent = gf.getParent();
			if (parent == null)
				parent = (GeneratedContainerNC) gf.getGenClass(); // TODO might not type check, but why not?

			if (parent instanceof GeneratedClass) {
				GeneratedClass st = (GeneratedClass) parent;
				final String class_name = gc.getTypeName(st);
//				System.out.println("234 class_name >> " + class_name);
				final String if_args = args_string.length() == 0 ? "" : ", ";
				return String.format("%s %s%s(%s* vsc%s%s)", return_type, class_name, name, class_name, if_args, args_string);
			} else if (parent instanceof GeneratedNamespace) {
				GeneratedNamespace st = (GeneratedNamespace) parent;
				final String class_name = gc.getTypeName(st);
				System.out.println(String.format("240 (namespace) %s -> %s", st.getName(), class_name));
				final String if_args = args_string.length() == 0 ? "" : ", ";
				// TODO vsi for namespace instance??
//				tos.put_string_ln(String.format("%s %s%s(%s* vsi%s%s) {", returnType, class_name, name, class_name, if_args, args));
				return String.format("%s %s%s(%s)", return_type, class_name, name, args_string);
			} else {
				return String.format("%s %s(%s)", return_type, name, args_string);
			}
		}

		String find_args_string(GeneratedFunction gf) {
			final String args;
			if (false) {
				args = Helpers.String_join(", ", Collections2.transform(gf.getFD().fal().falis, new Function<FormalArgListItem, String>() {
					@org.checkerframework.checker.nullness.qual.Nullable
					@Override
					public String apply(@org.checkerframework.checker.nullness.qual.Nullable final FormalArgListItem input) {
						assert input != null;
						return String.format("%s va%s", gc.getTypeName(input.typeName()), input.name());
					}
				}));
			} else {
				Collection<VariableTableEntry> x = Collections2.filter(gf.vte_list, new Predicate<VariableTableEntry>() {
					@Override
					public boolean apply(@org.checkerframework.checker.nullness.qual.Nullable VariableTableEntry input) {
						assert input != null;
						return input.vtt == VariableTableType.ARG;
					}
				});
				args = Helpers.String_join(", ", Collections2.transform(x, new Function<VariableTableEntry, String>() {
					@org.checkerframework.checker.nullness.qual.Nullable
					@Override
					public String apply(@org.checkerframework.checker.nullness.qual.Nullable VariableTableEntry input) {
						assert input != null;
						return String.format("%s va%s", gc.getTypeNameForVariableEntry(input), input.getName());
					}
				}));
			}
			return args;
		}

		String find_return_type(GeneratedFunction gf) {
			final String returnType;
			@Nullable InstructionArgument result_index = gf.vte_lookup("Result");
			if (result_index == null) {
				// if there is no Result, there should be Value
				result_index = gf.vte_lookup("Value");
				// but Value might be passed in. If it is, discard value
				@NotNull VariableTableEntry vte = ((IntegerIA) result_index).getEntry();
				if (vte.vtt != VariableTableType.RESULT)
					result_index = null;
				if (result_index == null)
					return "void"; // README Assuming Unit
			}

			// Get it from resolved
			tte = gf.getTypeTableEntry(((IntegerIA) result_index).getIndex());
			GeneratedNode res = tte.resolved();
			if (res instanceof GeneratedContainerNC) {
				final GeneratedContainerNC nc = (GeneratedContainerNC) res;
				int code = nc.getCode();
				return "Z"+code;
			}

			// Get it from type.attached
			type = tte.getAttached();

			System.out.println("228 "+ type);
			if (type != null && type.getType() == OS_Type.Type.BUILT_IN && type.getBType() == BuiltInTypes.Unit) {
				returnType = "void";
			} else if (type instanceof OS_Type.OS_UnitType) {
				returnType = "void/*Unit*/";
			} else if (type != null) {
				returnType = String.format("/*267*/%s*", gc.getTypeName(type));
			} else {
//			throw new IllegalStateException();
				returnType = "void/*2*/";
			}
			return returnType;
		}
	}
}

//
//
//
