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
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.lang2.SpecialVariables;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.DefaultBuffer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

/**
 * Created 10/8/20 7:13 AM
 */
public class GenerateC {
	private final OS_Module module;

	public static class AssociatedBuffer {
		public final int counter;
		public final GenerateResult.TY ty;
		public final Buffer buffer;
		public final GeneratedNode node;
		public String output;

		public AssociatedBuffer(GenerateResult.TY aTy, Buffer aBuffer, GeneratedNode aNode, int aCounter) {
			ty = aTy;
			buffer = aBuffer;
			node = aNode;
			counter = aCounter;
		}
	}

	public static class GenerateResult {
		public int bufferCounter = 0;

		List<AssociatedBuffer> res = new ArrayList<AssociatedBuffer>();

		public void add(Buffer b, GeneratedNode n, int counter, TY ty) {
			res.add(new AssociatedBuffer(ty, b, n, counter));
		}

		public List<AssociatedBuffer> results() {
			return res;
		}

		public void addFunction(GeneratedFunction aGeneratedFunction, Buffer aBuffer) {
			add(aBuffer, aGeneratedFunction, ++bufferCounter, TY.IMPL);
		}

		public enum TY {
			HEADER, IMPL
		}

		public void addClass(TY ty, GeneratedClass aClass, Buffer aBuf) {
			add(aBuf, aClass, ++bufferCounter, ty);
		}

		public void addNamespace(TY ty, GeneratedNamespace aNamespace, Buffer aBuf) {
			add(aBuf, aNamespace, ++bufferCounter, ty);
		}
	}

	public GenerateC(final OS_Module m) {
		this.module = m;
	}

	public GenerateResult generateCode2(Collection<GeneratedFunction> generatedFunctions) {
		return generateCode(Collections2.transform(generatedFunctions, new Function<GeneratedFunction, GeneratedNode>() {
			@org.checkerframework.checker.nullness.qual.Nullable
			@Override
			public GeneratedNode apply(@org.checkerframework.checker.nullness.qual.Nullable GeneratedFunction input) {
				return input;
			}
		}));
	}

	public GenerateResult generateCode(final Collection<GeneratedNode> lgf) {
		GenerateResult gr = new GenerateResult();
		Buffer b;

		for (final GeneratedNode generatedNode : lgf) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				try {
					generateCodeForMethod(generatedFunction, gr);
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode x = identTableEntry.resolved();

							if (x instanceof GeneratedClass) {
								generate_class((GeneratedClass) x, gr);
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
					generate_class(generatedClass, gr);
				} catch (final IOException e) {
					module.parent.eee.exception(e);
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				try {
					GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
					generate_namespace(generatedNamespace, gr);
				} catch (final IOException e) {
					module.parent.eee.exception(e);
				}
			}
		}

		return gr;
	}

	public void generate_class(GeneratedClass x, GenerateResult gr) throws IOException {
		int y=2;
		final CClassDecl decl = new CClassDecl(x);
		decl.evaluatePrimitive();
		final StringWriter stringWriterHdr = new StringWriter();
		final TabbedOutputStream tosHdr = new TabbedOutputStream(stringWriterHdr, false);
		final StringWriter stringWriter = new StringWriter();
		final TabbedOutputStream tos = new TabbedOutputStream(stringWriter, false);
		try {
			tosHdr.put_string_ln("typedef struct {");
			tosHdr.incr_tabs();
			tosHdr.put_string_ln("int _tag;");
			if (!decl.prim) {
				for (GeneratedClass.VarTableEntry o : x.varTable){
					String s;
					if (o.varType != null)
						s = getTypeName(o.varType);
					else
						s = "void*/*%null*/";
					tosHdr.put_string_ln(String.format("%s vm%s;", s, o.nameToken));
				}
			} else {
				tosHdr.put_string_ln(String.format("%s vsv;", decl.prim_decl));
			}

			String class_name = getTypeName(new OS_Type(x.getKlass()));
			int class_code = x.getKlass()._a.getCode();

			tosHdr.dec_tabs();
			tosHdr.put_string_ln("");
//			tosHdr.put_string_ln(String.format("} %s;", class_name));
			tosHdr.put_string_ln(String.format("} %s;  // class %s%s", class_name, decl.prim ? "box " : "", x.getName()));

			tosHdr.put_string_ln("");
			tosHdr.put_string_ln("");
			// TODO what about named constructors and ctor$0 and "the debug stack"
			tos.put_string_ln(String.format("%s* ZC%d() {", class_name, class_code));
			tos.incr_tabs();
			tos.put_string_ln(String.format("%s* R = GC_malloc(sizeof(%s));", class_name, class_name));
			tos.put_string_ln(String.format("R->_tag = %d;", class_code));
			if (decl.prim) {
				// TODO consider NULL, and floats and longs, etc
				if (!decl.prim_decl.equals("bool"))
					tos.put_string_ln("R->vsv = 0;");
				else if (decl.prim_decl.equals("bool"))
					tos.put_string_ln("R->vsv = false;");
			}
			tos.put_string_ln("return R;");
			tos.dec_tabs();
			tos.put_string_ln(String.format("} // class %s%s", decl.prim ? "box " : "", x.getName()));
			tos.put_string_ln("");
			tos.flush();
		} finally {
			tos.close();
			tosHdr.close();
			Buffer buf = new DefaultBuffer(stringWriter.toString());
//			System.out.println(buf.getText());
			gr.addClass(GenerateResult.TY.IMPL, x, buf);
			Buffer buf2 = new DefaultBuffer(stringWriterHdr.toString());
//			System.out.println(buf2.getText());
			gr.addClass(GenerateResult.TY.HEADER, x, buf2);
		}
	}

	public void generate_namespace(GeneratedNamespace x, GenerateResult gr) throws IOException {
		int y=2;
		final StringWriter stringWriterHdr = new StringWriter();
		final TabbedOutputStream tosHdr = new TabbedOutputStream(stringWriterHdr, false);
		final StringWriter stringWriter = new StringWriter();
		final TabbedOutputStream tos = new TabbedOutputStream(stringWriter, true);
		try {
			tosHdr.put_string_ln("typedef struct {");
			tosHdr.incr_tabs();
//			tosHdr.put_string_ln("int _tag;");
			for (GeneratedNamespace.VarTableEntry o : x.varTable){
				tosHdr.put_string_ln(String.format("%s* vm%s;", o.varType == null ? "void " : getTypeName(o.varType), o.nameToken));
			}

			String class_name = getTypeName(x.getNamespaceStatement());
			int class_code = x.getNamespaceStatement()._a.getCode();

			tosHdr.dec_tabs();
			tosHdr.put_string_ln("");
			tosHdr.put_string_ln(String.format("} %s; // namespace `%s'", class_name, x.getName()));

			tosHdr.put_string_ln("");
			tosHdr.put_string_ln("");
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
			tosHdr.close();
			Buffer buf = new DefaultBuffer(stringWriter.toString());
//			System.out.println(buf.getText());
			gr.addNamespace(GenerateResult.TY.IMPL, x, buf);
			Buffer buf2 = new DefaultBuffer(stringWriterHdr.toString());
//			System.out.println(buf2.getText());
			gr.addNamespace(GenerateResult.TY.HEADER, x, buf2);
		}
	}

	private void generateCodeForMethod(GeneratedFunction gf, GenerateResult gr) throws IOException {
		if (gf.fd == null) return;
		final StringWriter stringWriterHdr = new StringWriter();
		final TabbedOutputStream tosHdr = new TabbedOutputStream(stringWriterHdr, true);
		final StringWriter stringWriter = new StringWriter();
		final TabbedOutputStream tos = new TabbedOutputStream(stringWriter, true);
		final String returnType;
		final String name;
		//
		// FIND RETURN TYPE
		//
		final OS_Type tte = gf.getTypeTableEntry(1).attached;
		System.out.println("228 "+tte);
		if (tte != null && tte.getType() == OS_Type.Type.BUILT_IN && tte.getBType() == BuiltInTypes.Unit) {
			returnType = "void";
		} else if (tte != null) {
			returnType = String.format("/*267*/%s*", getTypeName(tte));
		} else {
//			throw new IllegalStateException();
			returnType = "void/*2*/";
		}

		//
		name = gf.fd.name();
		final String args;
		if (false) {
			args = Helpers.String_join(", ", Collections2.transform(gf.fd.fal().falis, new Function<FormalArgListItem, String>() {
				@org.checkerframework.checker.nullness.qual.Nullable
				@Override
				public String apply(@org.checkerframework.checker.nullness.qual.Nullable final FormalArgListItem input) {
					assert input != null;
					return String.format("%s va%s", getTypeName(input.typeName()), input.name());
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
					return String.format("%s va%s", getTypeNameForVariableEntry(input), input.getName());
				}
			}));
		}
		if (gf.fd.getParent() instanceof ClassStatement) {
			ClassStatement st = (ClassStatement) gf.fd.getParent();
			final String class_name = getTypeName(new OS_Type(st));
//			System.out.println("234 class_name >> " + class_name);
			final String if_args = args.length() == 0 ? "" : ", ";
			tos.put_string_ln(String.format("%s %s%s(%s* vsc%s%s) {", returnType, class_name, name, class_name, if_args, args));
		} else if (gf.fd.getParent() instanceof NamespaceStatement) {
			NamespaceStatement st = (NamespaceStatement) gf.fd.getParent();
			final String class_name = getTypeName(st);
			System.out.println(String.format("240 (namespace) %s -> %s", st.getName(), class_name));
			final String if_args = args.length() == 0 ? "" : ", ";
			// TODO vsi for namespace instance??
//			tos.put_string_ln(String.format("%s %s%s(%s* vsi%s%s) {", returnType, class_name, name, class_name, if_args, args));
			tos.put_string_ln(String.format("%s %s%s(%s) {", returnType, class_name, name, args));
		} else {
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
				tos.put_string_ln_no_tabs(label.getName() + ":");
			}
			switch (instruction.getName()) {
			case E:
				{
					tos.put_string_ln("bool vsb;");
					if (tte != null) {
						String ty = getTypeName(tte);
						tos.put_string_ln(String.format("%s vsr;", ty));
					}
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
					String s = String.format(Emit.emit("/*267*/")+"%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), value, gf));
					tos.put_string_ln(s);
					final int y = 2;
				}
				break;
			case AGNK:
				{
					final InstructionArgument target = instruction.getArg(0);
					final InstructionArgument value  = instruction.getArg(1);

					final String realTarget = getRealTargetName(gf, (IntegerIA) target);
					String s = String.format(Emit.emit("/*278*/")+"%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), value, gf));
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
			case CONSTRUCT:
				{
					final InstructionArgument _arg0 = instruction.getArg(0);
					assert _arg0 instanceof ProcIA;
					final ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) _arg0).getIndex());
					List<TypeTableEntry> x = pte.getArgs();
					int y = instruction.getArgsSize();
//					InstructionArgument z = instruction.getArg(1);
					ClassInvocation clsinv = pte.getClassInvocation();
					if (clsinv != null) {

						final InstructionArgument target = pte.expression_num;
//						final InstructionArgument value  = instruction;

						if (target instanceof IdentIA) {
							// how to tell between named ctors and just a path?
						}

						final String realTarget;
						if (target instanceof IntegerIA) {
							realTarget = getRealTargetName(gf, (IntegerIA) target);
						} else if (target instanceof IdentIA) {
							realTarget = getRealTargetName(gf, (IdentIA) target);
						} else {
							throw new NotImplementedException();
						}
//						String s = String.format(Emit.emit("/*500*/")+"%s = %s;", realTarget, getAssignmentValue(gf.getSelf(), instruction, clsinv, gf));
						String s = String.format(Emit.emit("/*500*/")+"/*%s = */%s;", realTarget, getAssignmentValue(gf.getSelf(), instruction, clsinv, gf));
						tos.put_string_ln(s);


					}
				}
				break;
			case CALL:
				{
					final StringBuilder sb = new StringBuilder();
// 					System.err.println("9000 "+inst.getName());
					final InstructionArgument x = instruction.getArg(0);
					assert x instanceof ProcIA;
					final ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) x).getIndex());
					{
						if (pte.expression_num == null) {
							final IdentExpression ptex = (IdentExpression) pte.expression;
							String text = ptex.getText();
							@org.jetbrains.annotations.Nullable InstructionArgument xx = gf.vte_lookup(text);
							assert xx != null;
							String realTargetName = getRealTargetName(gf, (IntegerIA) xx);
							sb.append(Emit.emit("/*424*/")+realTargetName);
							sb.append('(');
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							sb.append(Helpers.String_join(", ", sl3));
							sb.append(");");
						} else {
							final CReference reference = new CReference();
							final IdentIA ia2 = (IdentIA) pte.expression_num;
							reference.getIdentIAPath(ia2, gf);
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							reference.args(sl3);
							String path = reference.build();

							sb.append(Emit.emit("/*427*/")+path+";");
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
					assert x instanceof ProcIA;
					final ProcTableEntry pte = gf.getProcTableEntry(to_int(x));
					{
						CReference reference = null;
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
							sb.append(Emit.emit("/*460*/")+xxx);
						} else {
							final IdentIA ia2 = (IdentIA) pte.expression_num;
							reference = new CReference();
							reference.getIdentIAPath(ia2, gf);
							final List<String> sl3 = getArgumentStrings(gf, instruction);
							reference.args(sl3);
							String path = reference.build();
							sb.append(Emit.emit("/*463*/")+path);
						}
						if (reference == null){
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
				generate_method_is_a(instruction, tos, gf);
				break;
			case DECL:
				generate_method_decl(instruction, tos, gf);
				break;
			case CAST_TO:
				generate_method_cast(instruction, tos, gf);
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
		Buffer buf = new DefaultBuffer(stringWriter.toString());
//		System.out.println(buf.getText());
		gr.addFunction(gf, buf);
	}

	private void generate_method_is_a(Instruction instruction, TabbedOutputStream tos, GeneratedFunction gf) throws IOException {
		final IntegerIA testing_var_  = (IntegerIA) instruction.getArg(0);
		final IntegerIA testing_type_ = (IntegerIA) instruction.getArg(1);
		final Label target_label  = ((LabelIA) instruction.getArg(2)).label;

		final VariableTableEntry testing_var  = gf.getVarTableEntry(testing_var_.getIndex());
		final TypeTableEntry testing_type = gf.getTypeTableEntry(testing_type_.getIndex());

//		System.err.println("8887 " + testing_var);
//		System.err.println("8888 " + testing_type);

		final OS_Type x = testing_type.attached;
		if (x != null) {
			if (x.getType() == OS_Type.Type.USER) {
				final TypeName y = x.getTypeName();
				if (y instanceof NormalTypeName) {
					module.parent.eee.reportError("500 USER TypeName in GenerateC: "+y);
					return;
//					final int z = getTypeNumber((NormalTypeName) y);
//					tos.put_string_ln(String.format("vsb = ZS<%d>_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
//					tos.put_string_ln(String.format("if (!vsb) goto %s;", target_label.getName()));
				} else
					System.err.println("8883 " + y.getClass().getName());
			} else if (x.getType() == OS_Type.Type.USER_CLASS) {
				final int z = getTypeNumber(new OS_Type(x.getClassOf()));
				if (z == 0 || z == -1) {
					System.err.println("510 TypeName not assigned a code: "+x.getClassOf());
				}
				tos.put_string_ln(String.format("vsb = ZS%d_is_a(%s);", z, getRealTargetName(gf, testing_var_)));
				tos.put_string_ln(String.format("if (!vsb) goto %s;", target_label.getName()));
			} else {
				module.parent.eee.reportError("512 Bad TypeName in GenerateC: "+x);
				return;
			}
		} else {
			System.err.println("8885 testing_type.attached is null " + testing_type);
		}
		final int yyy = 2;
	}

	private String getTypeNameForVariableEntry(VariableTableEntry input) {
		OS_Type attached = input.type.attached;
		if (attached.getType() == OS_Type.Type.USER_CLASS) {
			return attached.getClassOf().name();
		} else if (attached.getType() == OS_Type.Type.USER) {
			TypeName typeName = attached.getTypeName();
			String name;
			if (typeName instanceof NormalTypeName)
				name = ((NormalTypeName) typeName).getName();
			else
				name = typeName.toString();
			return String.format(Emit.emit("/*543*/")+"Z<%s>*", name);
		} else
			throw new NotImplementedException();
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
			if (vte.vtt == VariableTableType.TEMP) {
				System.err.println("8884 temp variable has no type "+vte+" "+gf);
			} else {
				System.err.println("8885 x is null (No typename specified) for " + vte.getName());
			}
			return;
		}

		if (x.getType() == OS_Type.Type.USER_CLASS) {
			final String z = getTypeName(x);
			tos.put_string_ln(String.format("%s* %s;", z, target_name));
			return;
		} else if (x.getType() == OS_Type.Type.USER) {
			final TypeName y = x.getTypeName();
			if (y instanceof NormalTypeName) {
				final String z;
				if (((NormalTypeName) y).getName().equals("Any"))
					z = "void *";  // TODO Technically this is wrong
				else
					z = getTypeName(y);
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
				System.err.println("Bad potentialTypes size " + type);
				final String z = getTypeName(type);
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
			final OS_Type attached = ty.attached;
			assert attached != null;
			final String z = getTypeName(attached);
			tos.put_string_ln(String.format("/*8890*/Z<%s> %s;", z, target_name));
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
			System.err.println("Warning: USER TypeName in GenerateC "+ty.getTypeName());
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

	private int getTypeNumber(final OS_Type ty) {
		if (ty == null) throw new IllegalArgumentException("ty is null");
		//
		int z;
		switch (ty.getType()) {
		case USER_CLASS:
			final ClassStatement el = ty.getClassOf();
			z = el._a.getCode();
			break;
//		case FUNCTION:
//			z = "<function>";
//			break;
//		case USER:
//			System.err.println("Warning: USER TypeName in GenerateC "+ty.getTypeName());
//			z = String.format("Z<%s>", ty.getTypeName().toString());
//			break;
		case BUILT_IN:
			System.err.println("Warning: BUILT_IN TypeName in GenerateC");
			z = ty.getBType().getCode();  // README should not even be here, but look at .name() for other code gen schemes
			break;
		default:
			z = -1; // bad input
			//throw new IllegalStateException("Unexpected value: " + ty.getType());
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
				sl3.add(Emit.emit("/*669*/")+""+realTargetName);
			} else if (ia instanceof IdentIA) {
				final int y = 2;
				final CReference reference = new CReference();
				reference.getIdentIAPath((IdentIA) ia, gf);
				String text = reference.build();
				sl3.add(Emit.emit("/*673*/")+""+text);
			} else if (ia instanceof ConstTableIA) {
				ConstTableIA c = (ConstTableIA) ia;
				ConstantTableEntry cte = gf.getConstTableEntry(c.getIndex());
				if (cte.initialValue instanceof CharLitExpression) {
					sl3.add(String.format("'%s'", cte.initialValue.toString()));
				}
				int y = 2;
			} else if (ia instanceof ProcIA) {
				System.err.println("740 ProcIA");
				throw new NotImplementedException();
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
			assert x instanceof ProcIA;
			final ProcTableEntry pte = gf.getProcTableEntry(to_int(x));
//			System.err.println("9000-2 "+pte);
			switch (inst.getName()) {
			case CALL:
			{
				if (pte.expression_num == null) {
//					assert false; // TODO synthetic methods
					final IdentExpression ptex = (IdentExpression) pte.expression;
					sb.append(ptex.getText());
					sb.append(Emit.emit("/*671*/")+"(");

					final List<String> sll = getAssignmentValueArgs(inst, gf, module);
					sb.append(Helpers.String_join(", ", sll));

					sb.append(")");
				} else {
					final IdentIA ia2 = (IdentIA) pte.expression_num;
					if (gf.getIdentTableEntry(to_int(ia2)).getStatus() != BaseTableEntry.Status.UNKNOWN) {
						final CReference reference = new CReference();
						reference.getIdentIAPath(ia2, gf);
						final List<String> sll = getAssignmentValueArgs(inst, gf, module);
						reference.args(sll);
						String path = reference.build();
						sb.append(Emit.emit("/*827*/")+path);
					} else {
						final String path = gf.getIdentIAPathNormal(ia2);
						sb.append(Emit.emit("/*828*/")+String.format("%s is UNKNOWN", path));
					}
				}
				return sb.toString();
			}
			case CALLS:
			{
				CReference reference = null;
				if (pte.expression_num == null) {
					final int y=2;
					final IdentExpression ptex = (IdentExpression) pte.expression;
					sb.append(Emit.emit("/*684*/"));
					sb.append(ptex.getText());
				} else {
					// TODO Why not expression_num?
					reference = new CReference();
					final IdentIA ia2 = (IdentIA) pte.expression_num;
					reference.getIdentIAPath(ia2, gf);
					final List<String> sll = getAssignmentValueArgs(inst, gf, module);
					reference.args(sll);
					String path = reference.build();
					sb.append(Emit.emit("/*807*/")+path);

					final IExpression ptex = pte.expression;
					if (ptex instanceof IdentExpression) {
						sb.append(Emit.emit("/*803*/"));
						sb.append(((IdentExpression) ptex).getText());
					} else if (ptex instanceof ProcedureCallExpression) {
						sb.append(Emit.emit("/*806*/"));
						sb.append(ptex.getLeft()); // TODO Qualident, IdentExpression, DotExpression
					}
				}
				if (true /*reference == null*/) {
					sb.append(Emit.emit("/*810*/") + "(");
					{
						final List<String> sll = getAssignmentValueArgs(inst, gf, module);
						sb.append(Helpers.String_join(", ", sll));
					}
					sb.append(");");
				}
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
			case STRING_LITERAL:
				return (String.format("\"%s\"", ((StringExpression) cte.initialValue).getText()));
			case IDENT:
				final String text = ((IdentExpression) cte.initialValue).getText();
				if (BuiltInTypes.isBooleanText(text))
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
			final List<String> sll = new ArrayList<String>();
			for (int i = 1; i < args_size; i++) {
				final InstructionArgument ia = inst.getArg(i);
				final int y=2;
//			System.err.println("7777 " +ia);
				if (ia instanceof ConstTableIA) {
					final ConstantTableEntry constTableEntry = gf.getConstTableEntry(((ConstTableIA) ia).getIndex());
					sll.add(""+ const_to_string(constTableEntry.initialValue));
				} else if (ia instanceof IntegerIA) {
					final VariableTableEntry variableTableEntry = gf.getVarTableEntry(((IntegerIA) ia).getIndex());
					sll.add(Emit.emit("/*853*/")+""+getRealTargetName(gf, variableTableEntry));
				} else if (ia instanceof IdentIA) {
					String path = gf.getIdentIAPathNormal((IdentIA) ia);    // return x.y.z
					IdentTableEntry ite = gf.getIdentTableEntry(to_int(ia));
					if (ite.getStatus() == BaseTableEntry.Status.UNKNOWN) {
						sll.add(String.format("%s is UNKNOWN", path));
					} else {
						final CReference reference = new CReference();
						reference.getIdentIAPath((IdentIA) ia, gf);
						String path2 = reference.build();                        // return ZP105get_z(vvx.vmy)
						if (path.equals(path2)) {
							// should always fail
							//throw new AssertionError();
							System.err.println(String.format("864 should always fail but didn't %s %s", path, path2));
						}
//					assert ident != null;
//					IdentTableEntry ite = gf.getIdentTableEntry(((IdentIA) ia).getIndex());
//					sll.add(Emit.emit("/*748*/")+""+ite.getIdent().getText());
						sll.add(Emit.emit("/*748*/") + "" + path2);
						System.out.println("743 " + path2 + " " + path);
					}
				} else if (ia instanceof ProcIA) {
					System.err.println("863 ProcIA");
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

		public String IntegerIA(IntegerIA integerIA, GeneratedFunction gf) {
			VariableTableEntry vte = gf.getVarTableEntry(integerIA.getIndex());
			String x = getRealTargetName(gf, vte);
			return x;
		}

		public String IdentIA(IdentIA identIA, GeneratedFunction gf) {
			final CReference reference = new CReference();
			reference.getIdentIAPath(identIA, gf);
			return reference.build();
		}

		public String forClassInvocation(Instruction aInstruction, ClassInvocation aClsinv, GeneratedFunction gf, OS_Module module) {
			int y=2;
			InstructionArgument _arg0 = aInstruction.getArg(0);
			@NotNull ProcTableEntry pte = gf.getProcTableEntry(((ProcIA) _arg0).getIndex());
			final CtorReference reference = new CtorReference();
			reference.getConstructorPath(pte.expression_num, gf);
			@NotNull List<String> x = getAssignmentValueArgs(aInstruction, gf, module);
			reference.args(x);
			return reference.build(aClsinv);
		}
	}

	private String getAssignmentValue(VariableTableEntry aSelf, Instruction aInstruction, ClassInvocation aClsinv, GeneratedFunction gf) {
		GetAssignmentValue gav = new GetAssignmentValue();
		return gav.forClassInvocation(aInstruction, aClsinv, gf, module);
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

		if (value instanceof IntegerIA) {
			IntegerIA integerIA = (IntegerIA) value;
			return gav.IntegerIA(integerIA, gf);
		}

		if (value instanceof IdentIA) {
			IdentIA identIA = (IdentIA) value;
			return gav.IdentIA(identIA, gf);
		}

		System.err.println(String.format("783 %s %s", value.getClass().getName(), value));
		return ""+value;
	}

	private String getRealTargetName(final GeneratedFunction gf, final IntegerIA target) {
		final VariableTableEntry varTableEntry = gf.getVarTableEntry(target.getIndex());
		return getRealTargetName(gf, varTableEntry);
	}

	static String getRealTargetName(final GeneratedFunction gf, final VariableTableEntry varTableEntry) {
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
		} else if (isValue(gf, vte_name)) {
			return "vsc->vsv";
		}
		return Emit.emit("/*879*/")+"vv" + vte_name;
	}

	private static boolean isValue(GeneratedFunction gf, String name) {
		if (!name.equals("Value")) return false;
		//
		FunctionDef fd = (FunctionDef) gf.getFD();
		switch (fd.getType()) {
		case REG_FUN:
		case DEF_FUN:
			if (!(fd.getParent() instanceof ClassStatement)) return false;
			for (AnnotationPart anno : ((ClassStatement) fd.getParent()).annotationIterable()) {
				if (anno.annoClass().equals(Helpers.string_to_qualident("Primitive"))) {
					return true;
				}
			}
			return false;
		case PROP_GET:
		case PROP_SET:
			return true;
		default:
			throw new IllegalStateException("Unexpected value: " + fd.getType());
		}
	}

	String getRealTargetName(final GeneratedFunction gf, final IdentIA target) {
		IdentTableEntry identTableEntry = gf.getIdentTableEntry(target.getIndex());
		LinkedList<String> ls = new LinkedList<String>();
		// TODO in Deduce set property lookupType to denote what type of lookup it is: MEMBER, LOCAL, or CLOSURE
		InstructionArgument backlink = identTableEntry.backlink;
		if (backlink == null)
			ls.add(Emit.emit("/*912*/")+"vsc->vm"+identTableEntry.getIdent().getText()); // TODO blindly adding "vm" might not always work, also put in loop
		else
			ls.add(Emit.emit("/*872*/")+"vm"+identTableEntry.getIdent().getText()); // TODO blindly adding "vm" might not always work, also put in loop
		while (backlink != null) {
			if (backlink instanceof IntegerIA) {
				IntegerIA integerIA = (IntegerIA) backlink;
				String realTargetName = getRealTargetName(gf, integerIA);
				ls.addFirst(Emit.emit("/*892*/")+realTargetName);
				backlink = null;
			} else if (backlink instanceof IdentIA) {
				IdentIA identIA = (IdentIA) backlink;
				int identIAIndex = identIA.getIndex();
				IdentTableEntry identTableEntry1 = gf.getIdentTableEntry(identIAIndex);
				String identTableEntryName = identTableEntry1.getIdent().getText();
				ls.addFirst(Emit.emit("/*885*/")+"vm"+identTableEntryName); // TODO blindly adding "vm" might not always be right
				backlink = identTableEntry1.backlink;
			} else
				throw new IllegalStateException("Invalid InstructionArgument for backlink");
		}
		final CReference reference = new CReference();
		reference.getIdentIAPath(target, gf);
		String path = reference.build();
		System.out.println("932 "+path);
		String s = Helpers.String_join("->", ls);
		System.out.println("933 "+s);
		if (identTableEntry.resolved_element instanceof ConstructorDef)
			return path;
		else
			return s;
	}
}

//
//
//
