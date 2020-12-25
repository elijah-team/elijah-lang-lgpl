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
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 10/8/20 7:13 AM
 */
public class GenerateC {
	private final OS_Module module;

	public GenerateC(final OS_Module m) {
		this.module = m;
	}

	public void generateCode2(Collection<GeneratedFunction> generatedFunctions) {
		generateCode(Collections2.transform(generatedFunctions, new Function<GeneratedFunction, GeneratedNode>() {
			@Nullable
			@Override
			public GeneratedNode apply(@Nullable GeneratedFunction input) {
				return input;
			}
		}));
	}

	public void generateCode(final Collection<GeneratedNode> lgf) {
		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				try {
					generateCodeForMethod(generatedFunction);
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode x = identTableEntry.resolved();

							if (x instanceof GeneratedClass) {
								generate_class((GeneratedClass) x);
							} else
								throw new NotImplementedException();
						}
					}
				} catch (final IOException e) {
					module.parent.eee.exception(e);
				}
			} else if (generatedNode instanceof GeneratedClass) {
				try {
					GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					generate_class(generatedClass);
				} catch (final IOException e) {
					module.parent.eee.exception(e);
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				try {
					GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
					generate_namespace(generatedNamespace);
				} catch (final IOException e) {
					module.parent.eee.exception(e);
				}
			}
		}
	}

	public void generate_class(GeneratedClass x) throws IOException {
		int y=2;
		final ClassStatement xx = x.getKlass();
//		xx.annotationsOf();
		final CClassDecl decl = new CClassDecl(x);
		xx.walkAnnotations(new AnnotationWalker() {
			@Override
			public void annotation(AnnotationPart anno) {
				if (anno.annoClass().equals(Helpers.string_to_qualident("C.repr"))) {
					if (anno.getExprs() != null) {
						final ArrayList<IExpression> expressions = new ArrayList<IExpression>(anno.getExprs().expressions());
						final IExpression str0 = expressions.get(0);
						if (str0 instanceof StringExpression) {
							final String str = ((StringExpression) str0).getText();
							decl.setDecl(str);
						} else {
							System.out.println("Illegal C.decl");
						}
					}
				}
				if (anno.annoClass().equals(Helpers.string_to_qualident("Primitive")))
					decl.setPrimitive();
			}
		});
		final TabbedOutputStream tos = new TabbedOutputStream(System.out);
		try {
			tos.put_string_ln("typedef struct {");
			tos.incr_tabs();
			tos.put_string_ln("int _tag;");
			if (!decl.prim) {
				for (GeneratedClass.VarTableEntry o : x.varTable){
					tos.put_string_ln(String.format("void *vm%s;", o.nameToken));
				}
			} else {
				tos.put_string_ln(String.format("%s vsv;", decl.prim_decl));
			}

			String class_name = getTypeName(new OS_Type(x.getKlass()));//getName();
			int class_code = x.getKlass()._a.getCode();

			tos.dec_tabs();
			tos.put_string_ln("");
			tos.put_string_ln(String.format("} %s;", class_name));

			tos.put_string_ln("");
			tos.put_string_ln("");
			tos.put_string_ln(String.format("%s* ZC%d() {", class_name, class_code));
			tos.incr_tabs();
			tos.put_string_ln(String.format("%s* R = GC_malloc(sizeof(%s));", class_name, class_name));
			tos.put_string_ln(String.format("R->_tag = %d;", class_code));
			if (decl.prim) {
				if (!decl.prim_decl.equals("bool"))
					tos.put_string_ln("R->vsv = 0;");
				else if (decl.prim_decl.equals("bool"))
					tos.put_string_ln("R->vsv = false;");
			}
			tos.put_string_ln("return R;");
			tos.dec_tabs();
			tos.put_string_ln(String.format("} // class %s", x.getName()));
			tos.put_string_ln("");
			tos.flush();
		} finally {
			tos.close();
		}
	}

	public void generate_namespace(GeneratedNamespace x) throws IOException {
		int y=2;
		final TabbedOutputStream tos = new TabbedOutputStream(System.out);
		try {
			tos.put_string_ln("typedef struct {");
			tos.incr_tabs();
//			tos.put_string_ln("int _tag;");
			for (GeneratedNamespace.VarTableEntry o : x.varTable){
				tos.put_string_ln(String.format("%s* vm%s;", getTypeName(o.varType), o.nameToken));
			}

			String class_name = getTypeName(x.getNamespaceStatement());
			int class_code = x.getNamespaceStatement()._a.getCode();

			tos.dec_tabs();
			tos.put_string_ln("");
			tos.put_string_ln(String.format("} %s; // namespace `%s'", class_name, x.getName()));

			tos.put_string_ln("");
			tos.put_string_ln("");
			tos.put_string_ln(String.format("%s* ZC%d() {", class_name, class_code));
			tos.incr_tabs();
			tos.put_string_ln(String.format("%s* R = GC_malloc(sizeof(%s));", class_name, class_name));
//			tos.put_string_ln(String.format("R->_tag = %d;", class_code));
			tos.put_string_ln("return R;");
			tos.dec_tabs();
			tos.put_string_ln(String.format("} // namespace `%s'", x.getName()));
			tos.put_string_ln("");
			tos.flush();
		} finally {
			tos.close();
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
			returnType = String.format("%s*", getTypeName(tte));
		} else {
			returnType = "void";
		}
		//
		name = gf.fd.name();
		final String args = Helpers.String_join(", ", Collections2.transform(gf.fd.fal().falis, new Function<FormalArgListItem, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final FormalArgListItem input) {
				assert input != null;
				return String.format("%s va%s", getTypeName(input.typeName()), input.name());
			}
		}));
		if (gf.fd.getParent() instanceof ClassStatement) {
			ClassStatement st = (ClassStatement) gf.fd.getParent();
			final String class_name = getTypeName(new OS_Type(st));
			final String if_args = args.length() == 0 ? "" : ", ";
			tos.put_string_ln(String.format("%s %s%s(%s* vsc%s%s) {", returnType, class_name, name, class_name, if_args, args));
		} else {
			// TODO vsi for namespace instance??
			tos.put_string_ln(String.format("%s %s(%s) {", returnType, name, args));
		}
		tos.incr_tabs();
		//
		@NotNull List<Instruction> instructions = gf.instructions();
		for (int instruction_index = 0; instruction_index < instructions.size(); instruction_index++) {
			Instruction instruction = instructions.get(instruction_index);
//			System.err.println("8999 "+instruction);
			final Label label = gf.findLabel(instruction.getIndex());
			if (label != null) {
				tos.put_string_ln(label.getName() + ":");
			}
			switch (instruction.getName()) {
			case E:
				{
					tos.put_string_ln("bool vsb;");
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

					final String realTarget;
					if (target instanceof IntegerIA) {
						realTarget = getRealTargetName(gf, (IntegerIA) target);
					} else {
						realTarget = getRealTargetName(gf, (IdentIA) target);
					}
					String s = String.format("%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), value, gf));
					tos.put_string_ln(s);
					final int y = 2;
				}
				break;
			case AGNK:
				{
					final InstructionArgument target = instruction.getArg(0);
					final InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					String s = String.format("%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), value, gf));
					tos.put_string_ln(s);
					final int y = 2;
				}
				break;
			case AGNT:
				break;
			case AGNF:
				break;
			case JE:
				{
					final InstructionArgument lhs    = instruction.getArg(0);
					final InstructionArgument rhs    = instruction.getArg(1);
					final InstructionArgument target = instruction.getArg(2);

					final Label realTarget = (Label) target;

					final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA)lhs).getIndex());
					assert rhs != null;

					if (rhs instanceof ConstTableIA) {
						final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
						final String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
					} else {
						//
						// TODO need to lookup special __eq__ function
						//
						String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s == %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

						final int y = 2;
					}
				}
				break;
			case JNE:
				{
					final InstructionArgument lhs    = instruction.getArg(0);
					final InstructionArgument rhs    = instruction.getArg(1);
					final InstructionArgument target = instruction.getArg(2);

					final Label realTarget = (Label) target;

					final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) lhs).getIndex());
					assert rhs != null;

					if (rhs instanceof ConstTableIA) {
						final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
						final String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s != %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
					} else {
						//
						// TODO need to lookup special __ne__ function ??
						//
						String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s != %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

						final int y = 2;
					}
				}
				break;
			case JL:
				{
					final InstructionArgument lhs    = instruction.getArg(0);
					final InstructionArgument rhs    = instruction.getArg(1);
					final InstructionArgument target = instruction.getArg(2);

					final Label realTarget = (Label) target;

					final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) lhs).getIndex());
					assert rhs != null;

					if (rhs instanceof ConstTableIA) {
						final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) rhs).getIndex());
						final String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s < %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));
					} else {
						//
						// TODO need to lookup special __lt__ function
						//
						String realTargetName = getRealTargetName(gf, (IntegerIA) lhs);
						tos.put_string_ln(String.format("vsb = %s < %s;", realTargetName, getAssignmentValue(gf.getSelf(), rhs, gf)));
						tos.put_string_ln(String.format("if (!vsb) goto %s;", realTarget.getName()));

						final int y = 2;
					}
				}
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
				{
					final StringBuilder sb = new StringBuilder();
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
							sb.append(Helpers.String_join(", ", sl3));
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
								System.err.println("xxx is null " + text);
							}
							sb.append(xxx);
						} else {
							String path = gf.getIdentIAPath((IdentIA) pte.expression_num);
							sb.append(path);
						}
						{
							sb.append('(');
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							sb.append(Helpers.String_join(", ", sl3));
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
					final IntegerIA testing_var_  = (IntegerIA) instruction.getArg(0);
					final IntegerIA testing_type_ = (IntegerIA) instruction.getArg(1);
					final Label     target_label  = ((LabelIA) instruction.getArg(2)).label;

					final VariableTableEntry testing_var  = gf.getVarTableEntry(testing_var_.getIndex());
					final TypeTableEntry     testing_type = gf.getTypeTableEntry(testing_type_.getIndex());

					System.err.println("8887 " + testing_var);
					System.err.println("8888 " + testing_type);

					final OS_Type x = testing_type.attached;
					if (x != null) {
						if (x.getType() == OS_Type.Type.USER) {
							final TypeName y = x.getTypeName();
							if (y instanceof NormalTypeName) {
								final String z = ((NormalTypeName) y).getName();
								tos.put_string_ln(String.format("vsb = ZS<%s>_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
								tos.put_string_ln(String.format("if (!vsb) goto %s;", target_label.getName()));
							} else
								System.err.println("8886 " + y.getClass().getName());
						} else if (x.getType() == OS_Type.Type.USER_CLASS) {
							final String z = x.getClassOf().name();
							tos.put_string_ln(String.format("vsb = ZS<%s>_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
							tos.put_string_ln(String.format("if (!vsb) goto %s;", target_label.getName()));
						}
					} else {
						System.err.println("8885 testing_type.attached is null " + testing_type);
					}
					final int yyy = 2;
				}
				break;
			case DECL:
				{
					generate_method_decl(instruction, tos, gf);
				}
				break;
			case CAST:
				{
					generate_method_cast(instruction, tos, gf);
				}
				break;
			case NOP:
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

	private void generate_method_cast(Instruction instruction, TabbedOutputStream tos, GeneratedFunction gf) throws IOException {
		final IntegerIA  vte_num_ = (IntegerIA) instruction.getArg(0);
		final IntegerIA vte_type_ = (IntegerIA) instruction.getArg(1);
		final IntegerIA vte_targ_ = (IntegerIA) instruction.getArg(2);
		final String target_name = getRealTargetName(gf, vte_num_);
		final TypeTableEntry target_type_ = gf.getTypeTableEntry(vte_type_.getIndex());
		final String target_type = getTypeName(target_type_.attached);
		final String source_target = getRealTargetName(gf, vte_targ_);

		tos.put_string_ln(String.format("%s = (Z<%s>)%s;", target_name, target_type, source_target));
	}

	private void generate_method_decl(Instruction instruction, TabbedOutputStream tos, GeneratedFunction gf) throws IOException {
		final SymbolIA decl_type = (SymbolIA)  instruction.getArg(0);
		final IntegerIA  vte_num = (IntegerIA) instruction.getArg(1);
		final String target_name = getRealTargetName(gf, vte_num);
		final VariableTableEntry vte = gf.getVarTableEntry(vte_num.getIndex());

		final OS_Type x = vte.type.attached;
		if (x == null) {
			System.err.println("8885 x is null (No typename specified) for "+ vte.getName());
			return;
		}

		if (x.getType() == OS_Type.Type.USER_CLASS) {
			final String z = getTypeName(x);
			tos.put_string_ln(String.format("%s* %s;", z, target_name));
			return;
		} else if (x.getType() == OS_Type.Type.USER) {
			final TypeName y = x.getTypeName();
			if (y instanceof NormalTypeName) {
				final String z = getTypeName(y);
				tos.put_string_ln(String.format("%s %s;", z, target_name));
				return;
			}

			if (y != null) {
				//
				// VARIABLE WASN'T FULLY DEDUCED YET
				//
				System.err.println("8887 "+y.getClass().getName());
				return;
			}
		} else if(x.getType() == OS_Type.Type.BUILT_IN) {
			final Context context = gf.getFD().getContext();
			assert context != null;
			final OS_Type type = x.resolve(context);
			System.err.println("Bad potentialTypes size " + type);
			final String z = getTypeName(type);
			tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
		}

		//
		// VARIABLE WASN'T FULLY DEDUCED YET
		// MTL A TEMP VARIABLE
		//
		@NotNull final Collection<TypeTableEntry> pt_ = vte.potentialTypes();
		final List<TypeTableEntry> pt = new ArrayList<>(pt_);
		if (pt.size() == 1) {
			final TypeTableEntry ty = pt.get(0);
//			System.err.println("8885 " +ty.attached);
			final OS_Type attached = ty.attached;
			assert attached != null;
			final String z = getTypeName(attached);
			tos.put_string_ln(String.format("Z<%s> %s;", z, target_name));
		}
		System.err.println("8886 y is null (No typename specified)");
	}

	private String getTypeName(NamespaceStatement namespaceStatement) {
		String z;
		z = String.format("Z%d", namespaceStatement._a.getCode());
		return z;
	}

	private String getTypeName(final OS_Type ty) {
		if (ty == null) throw new IllegalArgumentException("ty is null");
		//
		String z;
		switch (ty.getType()) {
		case USER_CLASS:
			final ClassStatement el = ty.getClassOf();
			z = String.format("Z%d", el._a.getCode());//.getName();
			break;
		case FUNCTION:
			z = "<function>";
			break;
		case USER:
			System.err.println("Warning: USER TypeName in GenerateC");
			z = String.format("Z<%s>", ty.getTypeName().toString());
			break;
		case BUILT_IN:
			System.err.println("Warning: BUILT_IN TypeName in GenerateC");
			z = "Z"+ty.getBType().getCode();  // README should not even be here, but look at .name() for other code gen schemes
			break;
		default:
			throw new IllegalStateException("Unexpected value: " + ty.getType());
		}
		return z;
	}

	private String getTypeName(final TypeName typeName) {
		if (typeName instanceof RegularTypeName) {
			final String name = ((RegularTypeName) typeName).getName(); // TODO convert to Z-name

//			return String.format("Z<%s>/*kklkl*/", name);
			return getTypeName(new OS_Type(typeName));
		}
		module.parent.eee.reportError("Type is not fully deduced "+typeName);
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
				String text = gf.getIdentIAPath((IdentIA) ia);
				sl3.add(text);
			} else {
				System.err.println(ia.getClass().getName());
				throw new IllegalStateException("Invalid InstructionArgument");
			}
		}
		return sl3;
	}

	static class GetAssignmentValue {

		public String FnCallArgs(FnCallArgs fca, GeneratedFunction gf, OS_Module module) {
			final StringBuilder sb = new StringBuilder();
			final Instruction inst = fca.getExpression();
//			System.err.println("9000 "+inst.getName());
			final InstructionArgument x = inst.getArg(0);
			assert x instanceof IntegerIA;
			final ProcTableEntry pte = gf.getProcTableEntry(((IntegerIA)x).getIndex());
//			System.err.println("9000-2 "+pte);
			switch (inst.getName()) {
			case CALL:
			{
				if (pte.expression_num == null) {
//					assert false; // TODO synthetic methods
					final IdentExpression ptex = (IdentExpression) pte.expression;
					sb.append(ptex.getText());
				} else {
					String path = gf.getIdentIAPath((IdentIA) pte.expression_num);
					sb.append(path);
				}
				sb.append("(");
				{
					final List<String> sll = getAssignmentValueArgs(inst, gf, module);
					sb.append(Helpers.String_join(", ", sll));
				}
				sb.append(")");
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
					final List<String> sll = getAssignmentValueArgs(inst, gf, module);
					sb.append(Helpers.String_join(", ", sll));
				}
				sb.append(");");
				return sb.toString();
			}
			default:
				throw new IllegalStateException("Unexpected value: " + inst.getName());
			}
		}

		public String ConstTableIA(ConstTableIA constTableIA, GeneratedFunction gf) {
			final ConstantTableEntry cte = gf.getConstTableEntry(constTableIA.getIndex());
//			System.err.println(("9001-3 "+cte.initialValue));
			switch (cte.initialValue.getKind()) {
			case NUMERIC:
				return ("" + ((NumericExpression) cte.initialValue).getValue());
			case IDENT:
				final String text = ((IdentExpression) cte.initialValue).getText();
				if (text.equals("true") || text.equals("false"))
					return text;
				else if (text.equals("True") || text.equals("False"))
					return text;
				else
					throw new NotImplementedException();
			default:
				throw new NotImplementedException();
			}
		}

		@NotNull
		private List<String> getAssignmentValueArgs(final Instruction inst, final GeneratedFunction gf, OS_Module module) {
			final int args_size = inst.getArgsSize();
			final List<String> sll = new ArrayList<>();
			for (int i = 1; i < args_size; i++) {
				final InstructionArgument ia = inst.getArg(i);
				final int y=2;
//			System.err.println("7777 " +ia);
				if (ia instanceof ConstTableIA) {
					final ConstantTableEntry constTableEntry = gf.getConstTableEntry(((ConstTableIA) ia).getIndex());
					sll.add(""+ const_to_string(constTableEntry.initialValue));
				} else if (ia instanceof IntegerIA) {
					final VariableTableEntry variableTableEntry = gf.getVarTableEntry(((IntegerIA) ia).getIndex());
					sll.add(variableTableEntry.getName());
				} else if (ia instanceof IdentIA) {
//					@org.jetbrains.annotations.Nullable
//					final OS_Element ident = gf.resolveIdentIA(gf.getFD().getContext(), (IdentIA) ia, module);
					//String path = gf.getIAPath((IdentIA) ia));    // return x.y.z
//				String path2 = gf.getIdentIAPath((IdentIA) ia); // return ZP105get_z(vvx.vmy)
//				assert path.equals(path2); // should always fail
//					assert ident != null;
					throw new NotImplementedException();
				} else {
					throw new IllegalStateException("Cant be here: Invalid InstructionArgument");
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

	}

	@NotNull
	private String getAssignmentValue(VariableTableEntry value_of_this, final InstructionArgument value, final GeneratedFunction gf) {
		GetAssignmentValue gav = new GetAssignmentValue();
		if (value instanceof FnCallArgs) {
			final FnCallArgs fca = (FnCallArgs) value;
			return gav.FnCallArgs(fca, gf, module);
		}

		if (value instanceof ConstTableIA) {
			ConstTableIA constTableIA = (ConstTableIA) value;
			return gav.ConstTableIA(constTableIA, gf);
		}

		return ""+value;
	}

	private String getRealTargetName(final GeneratedFunction gf, final IntegerIA target) {
		final VariableTableEntry varTableEntry = gf.getVarTableEntry(target.getIndex());
		return getRealTargetName(gf, varTableEntry);
	}

	private String getRealTargetName(final GeneratedFunction gf, final VariableTableEntry varTableEntry) {
		final String vte_name = varTableEntry.getName();
		if (varTableEntry.vtt == VariableTableType.TEMP) {
			if (varTableEntry.getName() == null) {
				int tempNum = varTableEntry.tempNum;
				if (tempNum == -1) {
					varTableEntry.tempNum = gf.nextTemp();
					tempNum = varTableEntry.tempNum;
				}
				return "vt" + tempNum;
			} else {
				return "vt" + varTableEntry.getName();
			}
		} else if (varTableEntry.vtt == VariableTableType.ARG) {
			return "va" + vte_name;
		} else if (SpecialVariables.contains(vte_name)) {
			return SpecialVariables.get(vte_name);
		}
		return "vv" + vte_name;
	}

	private String getRealTargetName(final GeneratedFunction gf, final IdentIA target) {
		IdentTableEntry identTableEntry = gf.getIdentTableEntry(target.getIndex());
		List<String> ls = new LinkedList<String>();
		// TODO in Deduce set property lookupType to denote what type of lookup it is: MEMBER, LOCAL, or CLOSURE
		ls.add("vm"+identTableEntry.getIdent().getText()); // TODO blindly adding "vm" might not always work, also put in loop
		InstructionArgument backlink = identTableEntry.backlink;
		while (backlink != null) {
			if (backlink instanceof IntegerIA) {
				IntegerIA integerIA = (IntegerIA) backlink;
				String realTargetName = getRealTargetName(gf, integerIA);
				ls.add(0, realTargetName);
				backlink = null;
			} else if (backlink instanceof IdentIA) {
				IdentIA identIA = (IdentIA) backlink;
				int identIAIndex = identIA.getIndex();
				IdentTableEntry identTableEntry1 = gf.getIdentTableEntry(identIAIndex);
				String identTableEntryName = identTableEntry1.getIdent().getText();
				ls.add(0, "vm"+identTableEntryName); // TODO blindly adding "vm" might not always be right
				backlink = identTableEntry1.backlink;
			} else
				throw new IllegalStateException("Invalid InstructionArgument for backlink");
		}
		return Helpers.String_join("->", ls);
	}
}

//
//
//
