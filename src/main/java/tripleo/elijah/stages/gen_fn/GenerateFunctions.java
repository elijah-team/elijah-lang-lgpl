/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
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
import java.util.Collection;
import java.util.List;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;
import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 9/10/20 2:28 PM
 */
public class GenerateFunctions {
	private final OS_Module module;

	public GenerateFunctions(final OS_Module module_) {
		module = module_;
	}

	public List<GeneratedNode> generateAllTopLevelFunctions() {
		final List<GeneratedNode> R = new ArrayList<GeneratedNode>();

		for (final ModuleItem item : module.getItems()) {
			if (item instanceof NamespaceStatement) {
				final List<GeneratedNode> r;
				r = generateAllNamespaceFunctions(((NamespaceStatement) item));
				R.addAll(r);
			} else if (item instanceof ClassStatement) {
				final List<GeneratedNode> r;
				final ClassStatement classStatement = (ClassStatement) item;
				r = generateAllClassFunctions(classStatement);
				R.addAll(r);
			}
		}

		return R;
	}

	private List<GeneratedNode> generateAllClassFunctions(@NotNull final ClassStatement classStatement) {
		final List<GeneratedNode> R = new ArrayList<>();

		for (final ClassItem item : classStatement.getItems()) {
			if (item instanceof FunctionDef) {
				final FunctionDef function_def = (FunctionDef) item;
				GeneratedFunction generatedFunction = generateFunction(function_def, classStatement);
				function_def._a.setCode(nextFunctionCode());
				R.add(generatedFunction);
			} else if (item instanceof DefFunctionDef) {
				final DefFunctionDef defFunctionDef = (DefFunctionDef) item;
				R.add(generateDefFunction(defFunctionDef, classStatement));
				defFunctionDef._a.setCode(nextFunctionCode());
			}
		}

		return R;
	}

	private List<GeneratedNode> generateAllNamespaceFunctions(@NotNull final NamespaceStatement namespaceStatement) {
		final List<GeneratedNode> R = new ArrayList<GeneratedNode>();

		for (final ClassItem item : namespaceStatement.getItems()) {
			if (item instanceof FunctionDef) {
				final FunctionDef function_def = (FunctionDef) item;
				generateFunction(function_def, namespaceStatement);
				function_def._a.setCode(nextFunctionCode());
			} else if (item instanceof DefFunctionDef) {
				final DefFunctionDef defFunctionDef = (DefFunctionDef) item;
				generateDefFunction(defFunctionDef, namespaceStatement);
				defFunctionDef._a.setCode(nextFunctionCode());
			}
		}

		return R;
	}

	private @NotNull GeneratedFunction generateDefFunction(@NotNull final DefFunctionDef fd, final OS_Element parent) {
		System.err.println("601 fn "+fd.name());
		final GeneratedFunction gf = new GeneratedFunction(fd);
		final Context cctx = fd.getContext();
		final int e1 = add_i(gf, InstructionName.E, null, cctx);
		add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)), cctx);
		System.out.println(String.format("602 %s %s", fd.name(), gf.instructionsList));
		System.out.println(gf.vte_list);
		System.out.println(gf.cte_list);
		System.out.println(gf.prte_list);
		System.out.println(gf.tte_list);
//		System.out.println(gf.idte_list);
		return gf;
	}

	private @NotNull GeneratedFunction generateFunction(@NotNull final FunctionDef fd, final OS_Element parent) {
		System.err.println("601.1 fn "+fd.name());
		final GeneratedFunction gf = new GeneratedFunction(fd);
		if (parent instanceof ClassStatement)
			gf.addVariableTableEntry("self", VariableTableType.SELF, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type((ClassStatement) parent), IdentExpression.forString("self")));
		gf.addVariableTableEntry("Result", VariableTableType.RESULT, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(fd.returnType()), IdentExpression.forString("Result"))); // TODO what about Unit returns?
		for (final FormalArgListItem fali : fd.fal().falis) {
			final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(fali.typeName()), fali.getNameToken());
			gf.addVariableTableEntry(fali.name(), VariableTableType.ARG, tte);
		} // TODO Exception !!??
		//
		final Context cctx = fd.getContext();
		final int e1 = add_i(gf, InstructionName.E, null, cctx);
		for (final FunctionItem item : fd.getItems()) {
//			System.err.println("7001 fd.getItem = "+item);
			generate_item((OS_Element) item, gf, cctx);
		}
		final int x1 = add_i(gf, InstructionName.X, List_of(new IntegerIA(e1)), cctx);
		gf.addContext(fd.getContext(), new Range(e1, x1)); // TODO remove interior contexts
		System.out.println(String.format("602.1 %s", fd.name()));
//		for (Instruction instruction : gf.instructionsList) {
//			System.out.println(instruction);
//		}
//		GeneratedFunction.printTables(gf);
		return gf;
	}

	private void generate_item(final OS_Element item, @NotNull final GeneratedFunction gf, final Context cctx) {
		if (item instanceof AliasStatement) {
			throw new NotImplementedException();
		} else if (item instanceof CaseConditional) {
			throw new NotImplementedException();
		} else if (item instanceof ClassStatement) {
			GeneratedClass gc = generateClass((ClassStatement) item);
			int ite_index = gf.addIdentTableEntry(((ClassStatement) item).getNameNode());
			IdentTableEntry ite = gf.getIdentTableEntry(ite_index);
			ite.resolve(gc);
		} else if (item instanceof StatementWrapper) {
//				System.err.println("106");
			final IExpression x = ((StatementWrapper) item).getExpr();
			final ExpressionKind expressionKind = x.getKind();
//			System.err.println("106-1 "+x.getKind()+" "+x);
			if (x.is_simple()) {
//				int i = addTempTableEntry(x.getType(), gf);
				switch (expressionKind) {
				case ASSIGNMENT:
					System.err.println(String.format("703.2 %s %s", x.getLeft(), ((BasicBinaryExpression)x).getRight()));
					generate_item_assignment(x, gf, cctx);
					break;
				case AUG_MULT:
					{
						System.out.println(String.format("801.1 %s %s %s", expressionKind, x.getLeft(), ((BasicBinaryExpression) x).getRight()));
//						BasicBinaryExpression bbe = (BasicBinaryExpression) x;
//						final IExpression right1 = bbe.getRight();
						final InstructionArgument left = simplify_expression(x.getLeft(), gf, cctx);
						final InstructionArgument right = simplify_expression(((BasicBinaryExpression) x).getRight(), gf, cctx);
						final IdentExpression fn_aug_name = Helpers.string_to_ident(SpecialFunctions.of(expressionKind));
						final List<TypeTableEntry> argument_types = List_of(gf.getVarTableEntry(to_int(left)).type, gf.getVarTableEntry(to_int(right)).type);
//						System.out.println("801.2 "+argument_types);
						final int fn_aug = addProcTableEntry(fn_aug_name, null, argument_types, gf);
						final int i = add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(fn_aug), left, right), cctx);
						//
						// SEE IF CALL SHOULD BE DEFERRED
						//
						for (final TypeTableEntry argument_type : argument_types) {
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
				switch (expressionKind) {
				case ASSIGNMENT:
//					System.err.println(String.format("803.2 %s %s", x.getLeft(), ((BasicBinaryExpression)x).getRight()));
					generate_item_assignment(x, gf, cctx);
					break;
//				case IS_A:
//					break;
				case PROCEDURE_CALL:
					final ProcedureCallExpression pce = (ProcedureCallExpression) x;
					simplify_procedure_call(pce, gf, cctx);
					break;
				case DOT_EXP:
					{
						final DotExpression de = (DotExpression) x;
						generate_item_dot_expression(null, de.getLeft(), de.getRight(), gf, cctx);
					}
					break;
				default:
					break;
				}
			}
		} else if (item instanceof IfConditional) {
			final IfConditional ifc = (IfConditional) item;
			generate_if(ifc, gf);
//			throw new NotImplementedException();
		} else if (item instanceof Loop) {
			System.err.println("800");
			final Loop loop = (Loop) item;
			generate_loop(loop, gf);
		} else if (item instanceof MatchConditional) {
			final MatchConditional mc = (MatchConditional) item;
			generate_match_conditional(mc, gf);
//			throw new NotImplementedException();
		} else if (item instanceof NamespaceStatement) {
//			System.out.println("Skip namespace for now "+((NamespaceStatement) item).name());
			throw new NotImplementedException();
		} else if (item instanceof VariableSequence) {
			for (final VariableStatement vs : ((VariableSequence) item).items()) {
//					System.out.println("8004 " + vs);
				final String variable_name = vs.getName();
				if (vs.getTypeModifiers() == TypeModifiers.CONST) {
					if (vs.initialValue().is_simple()) {
						final int ci = addConstantTableEntry(variable_name, vs.initialValue(), vs.initialValue().getType(), gf);
						final int vte_num = addVariableTableEntry(variable_name, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, (vs.initialValue().getType()), vs.getNameToken()), gf);
						final IExpression iv = vs.initialValue();
						add_i(gf, InstructionName.DECL, List_of(new SymbolIA("const"), new IntegerIA(vte_num)), cctx);
						add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(vte_num), new ConstTableIA(ci, gf)), cctx);
					} else {
						final int vte_num = addVariableTableEntry(variable_name, gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, (vs.initialValue().getType()), vs.getNameToken()), gf);
						add_i(gf, InstructionName.DECL, List_of(new SymbolIA("val"), new IntegerIA(vte_num)), cctx);
						final IExpression iv = vs.initialValue();
						assign_variable(gf, vte_num, iv, cctx);
					}
				} else {
					final TypeTableEntry tte;
					if (vs.initialValue() == IExpression.UNASSIGNED && vs.typeName() != null) {
						tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(vs.typeName()), vs.getNameToken());
					} else {
						tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, vs.initialValue().getType(), vs.getNameToken());
					}
					final int vte_num = addVariableTableEntry(variable_name, tte, gf); // TODO why not vs.initialValue ??
					add_i(gf, InstructionName.DECL, List_of(new SymbolIA("var"), new IntegerIA(vte_num)), cctx);
					final IExpression iv = vs.initialValue();
					assign_variable(gf, vte_num, iv, cctx);
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

	public @NotNull GeneratedClass generateClass(@NotNull ClassStatement klass) {
		GeneratedClass gc = new GeneratedClass(klass, module);
		AccessNotation an = null;

		for (ClassItem item : klass.getItems()) {
			if (item instanceof AliasStatement) {
				System.out.println("Skip alias statement for now");
//				throw new NotImplementedException();
			} else if (item instanceof ClassStatement) {
				throw new NotImplementedException();
			} else if (item instanceof ConstructorDef) {
				throw new NotImplementedException();
			} else if (item instanceof DestructorDef) {
				throw new NotImplementedException();
			} else if (item instanceof FunctionDef) {
				//throw new NotImplementedException();
				@NotNull GeneratedFunction f = generateFunction((FunctionDef) item, klass);
				gc.addFunction((FunctionDef) item, f);
			} else if (item instanceof DefFunctionDef) {
				throw new NotImplementedException();
			} else if (item instanceof NamespaceStatement) {
				throw new NotImplementedException();
			} else if (item instanceof VariableSequence) {
				VariableSequence vsq = (VariableSequence) item;
				for (VariableStatement vs : vsq.items()) {
//					System.out.println("6999 "+vs);
					gc.addVarTableEntry(an, vs);
				}
			} else if (item instanceof AccessNotation) {
				//
				// TODO two AccessNotation's can be active at once, for example if the first
				//  one defined only classes and the second one defined only a category
				//
				an = (AccessNotation) item;
//				gc.addAccessNotation(an);
			} else
				throw new NotImplementedException();
		}

		gc.createCtor0();

		klass._a.setCode(nextClassCode());

		return gc;
	}

	public GeneratedNamespace generateNamespace(NamespaceStatement namespace1) {
		GeneratedNamespace gn = new GeneratedNamespace(namespace1, module);
		AccessNotation an = null;

		for (ClassItem item : namespace1.getItems()) {
			if (item instanceof AliasStatement) {
				throw new NotImplementedException();
			} else if (item instanceof ClassStatement) {
				throw new NotImplementedException();
			} else if (item instanceof ConstructorDef) {
				throw new NotImplementedException();
			} else if (item instanceof DestructorDef) {
				throw new NotImplementedException();
			} else if (item instanceof FunctionDef) {
				//throw new NotImplementedException();
				@NotNull GeneratedFunction f = generateFunction((FunctionDef) item, namespace1);
				gn.addFunction((FunctionDef) item, f);
			} else if (item instanceof DefFunctionDef) {
				throw new NotImplementedException();
			} else if (item instanceof NamespaceStatement) {
				throw new NotImplementedException();
			} else if (item instanceof VariableSequence) {
				VariableSequence vsq = (VariableSequence) item;
				for (VariableStatement vs : vsq.items()) {
//					System.out.println("6999 "+vs);
					gn.addVarTableEntry(an, vs);
				}
			} else if (item instanceof AccessNotation) {
				//
				// TODO two AccessNotation's can be active at once, for example if the first
				//  one defined only classes and the second one defined only a category
				//
				an = (AccessNotation) item;
//				gn.addAccessNotation(an);
			} else
				throw new NotImplementedException();
		}

		gn.createCtor0();

		namespace1._a.setCode(nextClassCode());

		return gn;
	}

	public List<GeneratedNode> generateAllTopLevelClasses() {
		List<GeneratedNode> R = new ArrayList<>();

		for (final ModuleItem item : module.getItems()) {
			if (item instanceof NamespaceStatement) {
				final NamespaceStatement namespaceStatement = (NamespaceStatement) item;
//				final List<GeneratedNode> r;
//				r = generateAllNamespaceFunctions(((NamespaceStatement) item));
//				R.addAll(r);
				GeneratedNamespace kl = generateNamespace(namespaceStatement);
				R.add(kl);
			} else if (item instanceof ClassStatement) {
//				final List<GeneratedNode> r;
				final ClassStatement classStatement = (ClassStatement) item;
//				r = generateAllClassFunctions(classStatement);
//				R.addAll(r);
				@NotNull GeneratedClass kl = generateClass(classStatement);
				R.add(kl);
			}
		}

		return R;
	}

	class Generate_item_assignment {

		public void procedure_call(@NotNull GeneratedFunction gf, BasicBinaryExpression bbe, ProcedureCallExpression pce, Context cctx) {
			final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, bbe.getType(), bbe.getLeft());
			final String text = ((IdentExpression) bbe.getLeft()).getText();
			final InstructionArgument lookup = gf.vte_lookup(text);
			if (lookup != null) {
				// TODO should be AGNC
				final int instruction_number = add_i(gf, InstructionName.AGN, List_of(lookup,
						new FnCallArgs(expression_to_call(pce, gf, cctx), gf)), cctx);
				final Instruction instruction = gf.getInstruction(instruction_number);
				final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA)lookup).getIndex());
				vte.addPotentialType(instruction.getIndex(), tte);
			} else {
				final int vte_num = addVariableTableEntry(text, tte, gf);
				add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(vte_num)), cctx);
				// TODO should be AGNC
				final int instruction_number = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(vte_num),
						new FnCallArgs(expression_to_call(pce, gf, cctx), gf)), cctx);
				final Instruction instruction = gf.getInstruction(instruction_number);
				final VariableTableEntry vte = gf.getVarTableEntry(vte_num);
				vte.addPotentialType(instruction.getIndex(), tte);
			}
		}

		public void ident(GeneratedFunction gf, IdentExpression left, IdentExpression right, Context cctx) {
			final InstructionArgument vte_left = gf.vte_lookup(left.getText());
			final int ident_left;
			int ident_right;
			InstructionArgument some_left;
			if (vte_left == null) {
				ident_left = gf.addIdentTableEntry(left);
				some_left = new IdentIA(ident_left, gf);
			} else
				some_left = vte_left;
			final InstructionArgument iiii = gf.vte_lookup(right.getText());
			final int inst;
			if (iiii == null) {
				ident_right = gf.addIdentTableEntry(right);
				inst = add_i(gf, InstructionName.AGN, List_of(some_left, new IdentIA(ident_right, gf)), cctx);
			} else {
				inst = add_i(gf, InstructionName.AGN, List_of(some_left, iiii), cctx);

				// TODO this will break one day
				assert vte_left != null;
				final VariableTableEntry vte = gf.getVarTableEntry(DeduceTypes2.to_int(vte_left));
				// ^^
				vte.addPotentialType(inst,
						gf.getVarTableEntry(DeduceTypes2.to_int(iiii/* != null ? iiii :
							gf.getVarTableEntry(iii5))*/)).type);
			}
		}

		public void numeric(@NotNull GeneratedFunction gf, IExpression left, NumericExpression ne, Context cctx) {
			@NotNull final InstructionArgument agn_path = gf.get_assignment_path(left, GenerateFunctions.this);
			final int cte = addConstantTableEntry("", ne, ne.getType(), gf);

			final int agn_inst = add_i(gf, InstructionName.AGN, List_of(agn_path, new ConstTableIA(cte, gf)), cctx);
			// TODO what now??
		}
	}

	private void generate_item_assignment(@NotNull final IExpression x, @NotNull final GeneratedFunction gf, final Context cctx) {
//		System.err.println(String.format("801 %s %s", x.getLeft(), ((BasicBinaryExpression) x).getRight()));
		final BasicBinaryExpression bbe = (BasicBinaryExpression) x;
		final IExpression right1 = bbe.getRight();
		final Generate_item_assignment gia = new Generate_item_assignment();
		switch (right1.getKind()) {
		case PROCEDURE_CALL:
			gia.procedure_call(gf, bbe, (ProcedureCallExpression) right1, cctx);
			break;
		case IDENT:
			gia.ident(gf, (IdentExpression) bbe.getLeft(), (IdentExpression) right1, cctx);
			break;
		case NUMERIC:
			gia.numeric(gf, bbe.getLeft(), (NumericExpression) right1, cctx);
			break;
		default:
			System.err.println("right1.getKind(): "+right1.getKind());
			throw new NotImplementedException();
		}
	}

	private void generate_item_dot_expression(@org.jetbrains.annotations.Nullable final InstructionArgument backlink, final IExpression left, @NotNull final IExpression right, @NotNull final GeneratedFunction gf, final Context cctx) {
		final int y=2;
		final int x = gf.addIdentTableEntry((IdentExpression) left);
		if (backlink != null) {
			gf.getIdentTableEntry(x).backlink = backlink;
		}
		if (right.getLeft() == right)
			return;
		//
		if (right instanceof IdentExpression)
			generate_item_dot_expression(new IdentIA(x, gf), right.getLeft(), ((IdentExpression)right), gf, cctx);
		else
			generate_item_dot_expression(new IdentIA(x, gf), right.getLeft(), ((BasicBinaryExpression)right).getRight(), gf, cctx);
	}

	private void generate_match_conditional(@NotNull final MatchConditional mc, @NotNull final GeneratedFunction gf) {
		final int y = 2;
		final Context cctx = mc.getContext();
		{
			final IExpression expr = mc.getExpr();
			final InstructionArgument i = simplify_expression(expr, gf, cctx);
			System.out.println("710 " + i);

			Label label_next = gf.addLabel();
			final Label label_end  = gf.addLabel();

			{
				for (final MatchConditional.MC1 part : mc.getParts()) {
					if (part instanceof MatchConditional.MatchConditionalPart1) {
						final MatchConditional.MatchConditionalPart1 mc1 = (MatchConditional.MatchConditionalPart1) part;
						final TypeName tn = mc1.getTypeName();
						final IdentExpression id = mc1.getIdent();

						final int begin0 = add_i(gf, InstructionName.ES, null, cctx);

						final int tmp = addTempTableEntry(new OS_Type(tn), id, gf); // TODO no context!
						VariableTableEntry vte_tmp = gf.getVarTableEntry(tmp);
						final TypeTableEntry t = vte_tmp.type;
						add_i(gf, InstructionName.IS_A, List_of(i, new IntegerIA(t.getIndex()), /*TODO not*/new LabelIA(label_next)), cctx);
						final Context context = mc1.getContext();

						add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(tmp)), context);
						final int cast_inst = add_i(gf, InstructionName.CAST_TO, List_of(new IntegerIA(tmp), new IntegerIA(t.getIndex()), (i)), context);
						vte_tmp.addPotentialType(cast_inst, t); // TODO in the future instructionIndex may be unsigned

						for (final FunctionItem item : mc1.getItems()) {
							generate_item((OS_Element) item, gf, context);
						}

						add_i(gf, InstructionName.JMP, List_of(label_end), context);
						add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin0)), cctx);
						gf.place(label_next);
						label_next = gf.addLabel();
					} else if (part instanceof MatchConditional.MatchConditionalPart2) {
						final MatchConditional.MatchConditionalPart2 mc2 = (MatchConditional.MatchConditionalPart2) part;
						final IExpression id = mc2.getMatchingExpression();

						final int begin0 = add_i(gf, InstructionName.ES, null, cctx);

						final InstructionArgument i2 = simplify_expression(id, gf, cctx);
						add_i(gf, InstructionName.JNE, List_of(i, i2, label_next), cctx);
						final Context context = mc2.getContext();

						for (final FunctionItem item : mc2.getItems()) {
							generate_item((OS_Element) item, gf, context);
						}

						add_i(gf, InstructionName.JMP, List_of(label_end), context);
						add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin0)), cctx);
						gf.place(label_next);
//						label_next = gf.addLabel();
					} else if (part instanceof MatchConditional.MatchConditionalPart3) {
						System.err.println("Don't know what this is");
					}
				}
				gf.place(label_next);
				add_i(gf, InstructionName.NOP, List_of(), cctx);
				gf.place(label_end);
			}
		}
	}

	private void generate_if(@NotNull final IfConditional ifc, @NotNull final GeneratedFunction gf) {
		final Context cctx = ifc.getContext();
		final IdentExpression Boolean_true = Helpers.string_to_ident("true");
		Label label_next = gf.addLabel();
		final Label label_end  = gf.addLabel();
		{
			final int begin0 = add_i(gf, InstructionName.ES, null, cctx);
			final IExpression expr = ifc.getExpr();
			final InstructionArgument i = simplify_expression(expr, gf, cctx);
//			System.out.println("711 " + i);
			final int const_true = addConstantTableEntry("true", Boolean_true, new OS_Type(BuiltInTypes.Boolean), gf);
			add_i(gf, InstructionName.JNE, List_of(i, new ConstTableIA(const_true, gf), label_next), cctx);
			final int begin_1st = add_i(gf, InstructionName.ES, null, cctx);
			final int begin_2nd = add_i(gf, InstructionName.ES, null, cctx);
			for (final OS_Element item : ifc.getItems()) {
				generate_item(item, gf, cctx);
			}
			add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin_2nd)), cctx);
			if (ifc.getParts().size() == 0) {
				gf.place(label_next);
				add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin_1st)), cctx);
//				gf.place(label_end);
			} else {
				add_i(gf, InstructionName.JMP, List_of(label_end), cctx);
				final List<IfConditional> parts = ifc.getParts();
				for (final IfConditional part : parts) {
					gf.place(label_next);
//					label_next = gf.addLabel();
					if (part.getExpr() != null) {
						final InstructionArgument ii = simplify_expression(part.getExpr(), gf, cctx);
						System.out.println("712 " + ii);
						add_i(gf, InstructionName.JNE, List_of(ii, new ConstTableIA(const_true, gf), label_next), cctx);
					}
					final int begin_next = add_i(gf, InstructionName.ES, null, cctx);
					for (final OS_Element partItem : part.getItems()) {
						System.out.println("709 " + part + " " + partItem);
						generate_item(partItem, gf, cctx);
					}
					add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin_next)), cctx);
					gf.place(label_next);
				}
				gf.place(label_end);
			}
			add_i(gf, InstructionName.XS, List_of(new IntegerIA(begin0)), cctx);
		}
	}

	private void generate_loop(@NotNull final Loop loop, @NotNull final GeneratedFunction gf) {
		final Context cctx = loop.getContext();
		final int e2 = add_i(gf, InstructionName.ES, null, cctx);
//		System.out.println("702 "+loop.getType());
		switch (loop.getType()) {
		case FROM_TO_TYPE:
			{
				final IdentExpression iterNameToken = loop.getIterNameToken();
				final String iterName = iterNameToken.getText();
				final int iter_temp = addTempTableEntry(null, iterNameToken, gf); // TODO deduce later
				add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(iter_temp)), cctx);
				final InstructionArgument ia1 = simplify_expression(loop.getFromPart(), gf, cctx);
				if (ia1 instanceof ConstTableIA)
					add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(iter_temp), ia1), cctx);
				else
					add_i(gf, InstructionName.AGN, List_of(new IntegerIA(iter_temp), ia1), cctx);
				final Label label_top = gf.addLabel("top", true);
				gf.place(label_top);
				final Label label_bottom = gf.addLabel("bottom"+label_top.getIndex(), false);
				add_i(gf, InstructionName.JE, List_of(new IntegerIA(iter_temp), simplify_expression(loop.getToPart(), gf, cctx), label_bottom), cctx);
				for (final StatementItem statementItem : loop.getItems()) {
					System.out.println("705 "+statementItem);
					generate_item((OS_Element)statementItem, gf, cctx);
				}
				final IdentExpression pre_inc_name = Helpers.string_to_ident("__preinc__");
				final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, pre_inc_name);
				final int pre_inc = addProcTableEntry(pre_inc_name, null, List_of(tte/*getType(left), getType(right)*/), gf);
				add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), new IntegerIA(iter_temp)), cctx);
				add_i(gf, InstructionName.JMP, List_of(label_top), cctx);
				gf.place(label_bottom);
			}
			break;
		case TO_TYPE:
			break;
		case EXPR_TYPE:
			{
				final int loop_iterator = addTempTableEntry(null, gf); // TODO deduce later
				add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(loop_iterator)), cctx);
				final int i2 = addConstantTableEntry("", new NumericExpression(0), new OS_Type(BuiltInTypes.SystemInteger), gf);
				final InstructionArgument ia1 = new ConstTableIA(i2, gf);
//				if (ia1 instanceof ConstTableIA)
					add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(loop_iterator), ia1), cctx);
//				else
//					add_i(gf, InstructionName.AGN, List_of(new IntegerIA(loop_iterator), ia1), cctx);
				final Label label_top = gf.addLabel("top", true);
				gf.place(label_top);
				final Label label_bottom = gf.addLabel("bottom"+label_top.getIndex(), false);
				add_i(gf, InstructionName.JE, List_of(new IntegerIA(loop_iterator), simplify_expression(loop.getToPart(), gf, cctx), label_bottom), cctx);
				for (final StatementItem statementItem : loop.getItems()) {
					System.out.println("707 "+statementItem);
					generate_item((OS_Element)statementItem, gf, cctx);
				}
				final String txt = SpecialFunctions.of(ExpressionKind.INCREMENT);
				final IdentExpression pre_inc_name = Helpers.string_to_ident(txt);
				final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, pre_inc_name);
				final int pre_inc = addProcTableEntry(pre_inc_name, null, List_of(tte), gf);
				add_i(gf, InstructionName.CALLS, List_of(new IntegerIA(pre_inc), new IntegerIA(loop_iterator)), cctx);
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
		final int x2 = add_i(gf, InstructionName.XS, List_of(new IntegerIA(e2)), cctx);
		final Range r = new Range(e2, x2);
		gf.addContext(loop.getContext(), r);
	}

	private void assign_variable(@NotNull final GeneratedFunction gf, final int vte, @NotNull final IExpression value, final Context cctx) {
		if (value == IExpression.UNASSIGNED) return; // default_expression
		switch (value.getKind()) {
		case PROCEDURE_CALL:
			final ProcedureCallExpression pce = (ProcedureCallExpression) value;
			final FnCallArgs fnCallArgs = new FnCallArgs(expression_to_call(pce, gf, cctx), gf);
			final int ii2 = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(vte), fnCallArgs), cctx);
			final VariableTableEntry vte2 = gf.getVarTableEntry(vte);
			final TypeTableEntry tte2 = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, value);
			fnCallArgs.setType(tte2);
			vte2.addPotentialType(ii2, tte2);
			break;
		case NUMERIC:
			final int ci = addConstantTableEntry(null, value, value.getType(), gf);
			final int ii = add_i(gf, InstructionName.AGNK, List_of(new IntegerIA(vte), new ConstTableIA(ci, gf)), cctx);
			final VariableTableEntry vte1 = gf.getVarTableEntry(vte);
			vte1.addPotentialType(ii, gf.getConstTableEntry(ci).type);
			break;
		default:
			throw new NotImplementedException();
		}
	}

	private TypeTableEntry getType(@NotNull final IExpression arg, @NotNull final GeneratedFunction gf) {
		final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, arg.getType(), arg);
		return tte;
	}

	private void simplify_procedure_call(@NotNull final ProcedureCallExpression pce, @NotNull final GeneratedFunction gf, final Context cctx) {
		final IExpression left = pce.getLeft();
		final ExpressionList args = pce.getArgs();
		//
		InstructionArgument expression_num = simplify_expression(left, gf, cctx);
		if (expression_num == null) {
			expression_num = gf.get_assignment_path(left, this);
		}
		final int i = addProcTableEntry(left, expression_num, get_args_types(args, gf), gf);
		final List<InstructionArgument> l = new ArrayList<InstructionArgument>();
		l.add(new IntegerIA(i));
		l.addAll(simplify_args(args, gf, cctx));
		add_i(gf, InstructionName.CALL, l, cctx);
	}

	private @NotNull List<InstructionArgument> simplify_args(@org.jetbrains.annotations.Nullable final ExpressionList args, @NotNull final GeneratedFunction gf, final Context cctx) {
		final List<InstructionArgument> R = new ArrayList<InstructionArgument>();
		if (args == null) return R;
		//
		for (final IExpression expression : args) {
			final InstructionArgument ia = simplify_expression(expression, gf, cctx);
			if (ia != null) {
//				System.err.println("109 "+expression);
				R.add(ia);
			} else {
				System.err.println("109-0 error expr not found "+expression);
			}
		}
		return R;
	}

	private @NotNull Collection<InstructionArgument> simplify_args2(@org.jetbrains.annotations.Nullable final ExpressionList args, @NotNull final GeneratedFunction gf, final Context cctx) {
		Collection<InstructionArgument> R = new ArrayList<InstructionArgument>();
		if (args == null) return R;
		//
		R = Collections2.transform(args.expressions(), new Function<IExpression, InstructionArgument>() {
			@Override
			public @Nullable InstructionArgument apply(@Nullable final IExpression input) {
				assert input != null;
				@NotNull final IExpression expression = input;
				final InstructionArgument ia = simplify_expression(expression, gf, cctx);
				if (ia != null) {
					System.err.println("109-1 "+expression);
				} else {
					System.err.println("109-01 error expr not found "+expression);
				}
				return ia;
			}
		});
		return R;
	}

	private int addProcTableEntry(final IExpression expression, final InstructionArgument expression_num, final List<TypeTableEntry> args, @NotNull final GeneratedFunction gf) {
		final ProcTableEntry pte = new ProcTableEntry(gf.prte_list.size(), expression, expression_num, args);
		gf.prte_list.add(pte);
		return pte.index;
	}

	InstructionArgument simplify_expression(@NotNull final IExpression expression, @NotNull final GeneratedFunction gf, final Context cctx) {
		final ExpressionKind expressionKind = expression.getKind();
		switch (expressionKind) {
		case PROCEDURE_CALL:
			{
				final ProcedureCallExpression pce = (ProcedureCallExpression) expression;
				final IExpression    left = pce.getLeft();
				final ExpressionList args = pce.getArgs();
				final InstructionArgument left_ia;
				final List<InstructionArgument> right_ia = new ArrayList<InstructionArgument>(args.size());
				if (left.is_simple()) {
					if (left instanceof IdentExpression) {
						// for ident(xyz...)
						final int x = gf.addIdentTableEntry((IdentExpression) left);
						// TODO attach to var/const or lookup later in deduce
						left_ia = new IdentIA(x, gf);
					} else if (left instanceof SubExpression) {
						// for (1).toString() etc
						final SubExpression se = (SubExpression) left;
						final InstructionArgument ia = simplify_expression(se.getExpression(), gf, cctx);
						//return ia;  // TODO is this correct?
						left_ia = ia;
					} else {
						// for "".strip() etc
						assert IExpression.isConstant(left);
						final int x = addConstantTableEntry(null, left, left.getType(), gf);
						left_ia = new ConstTableIA(x, gf);
//						throw new IllegalStateException("Cant be here");
					}
				} else {
					final InstructionArgument x = simplify_expression(left, gf, cctx);
					final int y=2;
					left_ia = x;
				}
				final List<TypeTableEntry> args1 = new ArrayList<>();
				for (final IExpression arg : args) {
					final InstructionArgument ia;
					final TypeTableEntry iat;
					if (arg.is_simple()) {
						final int y=2;
						if (arg instanceof IdentExpression) {
							final int x = gf.addIdentTableEntry((IdentExpression) arg);
							// TODO attach to var/const or lookup later in deduce
							ia = new IdentIA(x, gf);
							iat = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, arg);
						} else if (arg instanceof SubExpression) {
							final SubExpression se = (SubExpression) arg;
							final InstructionArgument ia2 = simplify_expression(se.getExpression(), gf, cctx);
							//return ia;  // TODO is this correct?
							ia = ia2;
							iat = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, arg);
						} else {
							assert IExpression.isConstant(arg);
							final int x = addConstantTableEntry(null, arg, arg.getType(), gf);
							ia = new ConstTableIA(x, gf);
							iat = gf.getConstTableEntry(x).type;
						}
					} else {
						final InstructionArgument x = simplify_expression(left, gf, cctx);
						final int y=2;
						ia = x;
						iat = null;
					}
					right_ia.add(ia);
					args1.add(iat);
				}
				final int pte = addProcTableEntry(expression, left_ia, args1, gf);
				right_ia.add(0, new IntegerIA(pte));
				{
					final int tmp_var = addTempTableEntry(null, gf);
					add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(tmp_var)), cctx);
					final Instruction i = new Instruction();
					i.setName(InstructionName.CALL);
					i.setArgs(right_ia);
					// TODO should be AGNC
					final int x = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(tmp_var), new FnCallArgs(i, gf)), cctx);
					return new IntegerIA(tmp_var); // return tmp_var instead of expression assigning it
				}
			}
		case CAST_TO:
			{
				TypeCastExpression tce = (TypeCastExpression) expression;
				InstructionArgument simp = simplify_expression(tce.getLeft(), gf, cctx);
				@NotNull TypeTableEntry tte_index = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(tce.getTypeName()));
				final int x = add_i(gf, InstructionName.CAST_TO, List_of(simp, new IntegerIA(tte_index.getIndex())), cctx);
			}
		case AS_CAST:
			{
				TypeCastExpression tce = (TypeCastExpression) expression;
				InstructionArgument simp = simplify_expression(tce.getLeft(), gf, cctx);
				@NotNull TypeTableEntry tte_index = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(tce.getTypeName()));
				final int x = add_i(gf, InstructionName.AS_CAST, List_of(simp, new IntegerIA(tte_index.getIndex())), cctx);
			}
		case DOT_EXP:
			{
				final DotExpression de = (DotExpression) expression;
				return gf.get_assignment_path(de, this);
			}
		case QIDENT:
			throw new NotImplementedException();
		case IDENT:
			String text = ((IdentExpression) expression).getText();
			InstructionArgument i = gf.vte_lookup(text);
			if (i == null) {
				IdentTableEntry x = gf.getIdentTableEntryFor(expression);
				if (x == null) {
					int ii = gf.addIdentTableEntry((IdentExpression) expression);
					i = new IdentIA(ii, gf);
				} else {
					i = new IdentIA(x.getIndex(), gf);
				}
			}
			return i;
		case NUMERIC:
		{
			final NumericExpression ne = (NumericExpression) expression;
			final int ii = addConstantTableEntry2(null, ne, ne.getType(), gf);
			return new ConstTableIA(ii, gf);
		}
		case CHAR_LITERAL:
		{
			final CharLitExpression cle = (CharLitExpression) expression;
			final int ii = addConstantTableEntry2(null, cle, cle.getType(), gf);
			return new ConstTableIA(ii, gf);
		}
		case GET_ITEM:
			{
				final GetItemExpression gie = (GetItemExpression) expression;
				final IExpression left = gie.getLeft();
				final IExpression right = gie.index;
				final InstructionArgument left_instruction;
				final InstructionArgument right_instruction;
				if (left.is_simple()) {
					if (left instanceof IdentExpression) {
						left_instruction = simplify_expression(left, gf, cctx);
					} else {
						// a constant
						assert IExpression.isConstant(right);
						final int left_constant_num = addConstantTableEntry2(null, left, left.getType(), gf);
						left_instruction = new ConstTableIA(left_constant_num, gf);
					}
				} else {
					// create a tmp var
					left_instruction = simplify_expression(left, gf, cctx);
				}
				if (right.is_simple()) {
					if (right instanceof IdentExpression) {
						right_instruction = simplify_expression(right, gf, cctx);
					} else {
						// a constant
						assert IExpression.isConstant(right);
						final int right_constant_num = addConstantTableEntry2(null, right, right.getType(), gf);
						right_instruction = new ConstTableIA(right_constant_num, gf);
					}
				} else {
					// create a tmp var
					right_instruction = simplify_expression(right, gf, cctx);
				}
				{
					// create a call
					final IdentExpression expr_kind_name = Helpers.string_to_ident(SpecialFunctions.of(expressionKind));
	//					TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, expr_kind_name);
					final TypeTableEntry tte_left = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, left);
					final TypeTableEntry tte_right = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, right);
					final int pte = addProcTableEntry(expr_kind_name, null, List_of(tte_left, tte_right), gf);
					final int tmp = addTempTableEntry(expression.getType(), // README should be Boolean
							gf);
					add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(tmp)), cctx);
					final Instruction inst = new Instruction();
					inst.setName(InstructionName.CALLS);
					inst.setArgs(List_of(new IntegerIA(pte), left_instruction, right_instruction));
					final FnCallArgs fca = new FnCallArgs(inst, gf);
					final int x = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(tmp), fca), cctx);
					return new IntegerIA(tmp);
				}
			}
		case LT_: case GT: // TODO all BinaryExpressions go here
			{
				final BasicBinaryExpression bbe = (BasicBinaryExpression) expression;
				final IExpression left = bbe.getLeft();
				final IExpression right = bbe.getRight();
				final InstructionArgument left_instruction;
				final InstructionArgument right_instruction;
				if (left.is_simple()) {
					if (left instanceof IdentExpression) {
						left_instruction = simplify_expression(left, gf, cctx);
					} else {
						// a constant
						assert IExpression.isConstant(right);
						final int left_constant_num = addConstantTableEntry2(null, left, left.getType(), gf);
						left_instruction = new ConstTableIA(left_constant_num, gf);
					}
				} else {
					// create a tmp var
					left_instruction = simplify_expression(left, gf, cctx);
				}
				if (right.is_simple()) {
					if (right instanceof IdentExpression) {
						right_instruction = simplify_expression(right, gf, cctx);
					} else {
						// a constant
						assert IExpression.isConstant(right);
						final int right_constant_num = addConstantTableEntry2(null, right, right.getType(), gf);
						right_instruction = new ConstTableIA(right_constant_num, gf);
					}
				} else {
					// create a tmp var
					right_instruction = simplify_expression(right, gf, cctx);
				}
				{
					// create a call
					final IdentExpression expr_kind_name = Helpers.string_to_ident(SpecialFunctions.of(expressionKind));
//					TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, expr_kind_name);
					final TypeTableEntry tte_left  = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, left);
					final TypeTableEntry tte_right = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, null, right);
					final int pte = addProcTableEntry(expr_kind_name, null, List_of(tte_left, tte_right), gf);
					final int tmp = addTempTableEntry(expression.getType(), // README should be Boolean
							gf);
					add_i(gf, InstructionName.DECL, List_of(new SymbolIA("tmp"), new IntegerIA(tmp)), cctx);
					final Instruction inst = new Instruction();
					inst.setName(InstructionName.CALLS);
					inst.setArgs(List_of(new IntegerIA(pte), left_instruction, right_instruction));
					final FnCallArgs fca = new FnCallArgs(inst, gf);
					// TODO should be AGNC
					final int x = add_i(gf, InstructionName.AGN, List_of(new IntegerIA(tmp), fca), cctx);
					return new IntegerIA(tmp); // TODO  is this right?? we want to return the variable, not proc calls, right?
				}
//				throw new NotImplementedException();
			}
		default:
			throw new IllegalStateException("Unexpected value: " + expressionKind);
		}
//		return null;
	}

	private @NotNull List<TypeTableEntry> get_args_types(@org.jetbrains.annotations.Nullable final ExpressionList args, @NotNull final GeneratedFunction gf) {
		final List<TypeTableEntry> R = new ArrayList<>();
		if (args == null) return R;
		//
		for (final IExpression arg : args) {
			final OS_Type type = arg.getType();
//			System.err.println(String.format("108 %s %s", arg, type));
			if (arg instanceof IdentExpression) {
				final InstructionArgument x = gf.vte_lookup(((IdentExpression) arg).getText());
				final TypeTableEntry tte;
				if (x instanceof ConstTableIA) {
					final ConstantTableEntry cte = gf.getConstTableEntry(((ConstTableIA) x).getIndex());
					tte = cte.getTypeTableEntry();
				} else if (x instanceof IntegerIA) {
					final VariableTableEntry vte = gf.getVarTableEntry(((IntegerIA) x).getIndex());
					tte = vte.type;
				} else {
					//
					// WHEN VTE_LOOKUP FAILS, IE WHEN A MEMBER VARIABLE
					//
					int y=2;
					int idte_index = gf.addIdentTableEntry((IdentExpression) arg);
					tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, arg);
					gf.getIdentTableEntry(idte_index).type = tte;
				}
				R.add(tte);
			} else
				R.add(getType(arg, gf));
		}
		assert R.size() == args.size();
		return R;
	}

	private @NotNull Instruction expression_to_call(@NotNull final ProcedureCallExpression pce, @NotNull final GeneratedFunction gf, final Context cctx) {
		switch (pce.getLeft().getKind()) {
		case IDENT: {
			return expression_to_call_add_entry(gf, pce, pce.getLeft(), cctx);
		}
		case QIDENT: {
			simplify_qident((Qualident) pce.getLeft(), gf); // TODO ??
			return expression_to_call_add_entry(gf, pce, pce.getLeft(), cctx);
		}
		case DOT_EXP: {
			simplify_dot_expression((DotExpression) pce.getLeft(), gf); // TODO ??
			return expression_to_call_add_entry(gf, pce, pce.getLeft(), cctx);
		}
		default:
			throw new NotImplementedException();
		}
	}

	@NotNull
	private Instruction expression_to_call_add_entry(@NotNull final GeneratedFunction gf, @NotNull final ProcedureCallExpression pce, final IExpression left, final Context cctx) {
		final Instruction i = new Instruction();
		i.setName(InstructionName.CALL); // TODO see line 686
		final List<InstructionArgument> li = new ArrayList<>();
//			int ii = addIdentTableEntry((IdentExpression) expression.getLeft(), gf);
		final int pte_num = addProcTableEntry(left, gf.get_assignment_path(left, this), get_args_types(pce.getArgs(), gf), gf);
		li.add(new IntegerIA(pte_num));
		final List<InstructionArgument> args_ = simplify_args(pce.getArgs(), gf, cctx);
		li.addAll(args_);
		i.setArgs(li);
		return i;
	}

	private void simplify_qident(final Qualident left, final GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	private void simplify_dot_expression(final DotExpression left, final GeneratedFunction gf) {
		throw new NotImplementedException();
	}

	//
	// region add-table-entries
	//

	private int addVariableTableEntry(final String name, final TypeTableEntry type, @NotNull final GeneratedFunction gf) {
		return gf.addVariableTableEntry(name, VariableTableType.VAR, type);
	}

	private int addTempTableEntry(final OS_Type type, @NotNull final GeneratedFunction gf) {
		final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type);
		final VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, null, tte);
		gf.vte_list.add(vte);
		return vte.getIndex();
	}

	private int addTempTableEntry(final OS_Type type, @NotNull final IdentExpression name, @NotNull final GeneratedFunction gf) {
		final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, name);
		final VariableTableEntry vte = new VariableTableEntry(gf.vte_list.size(), VariableTableType.TEMP, name.getText(), tte);
		gf.vte_list.add(vte);
		return vte.getIndex();
	}

	/**
	 * Add a Constant Table Entry of type with Type Table Entry type {@link TypeTableEntry.Type.SPECIFIED}
	 * @param name
	 * @param initialValue
	 * @param type
	 * @param gf
	 * @return the cte table index
	 */
	private int addConstantTableEntry(final String name, final IExpression initialValue, final OS_Type type, @NotNull final GeneratedFunction gf) {
		final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, initialValue);
		final ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, tte);
		gf.cte_list.add(cte);
		return cte.index;
	}

	/**
	 * Add a Constant Table Entry of type with Type Table Entry type {@link TypeTableEntry.Type.TRANSIENT}
	 * @param name
	 * @param initialValue
	 * @param type
	 * @param gf
	 * @return the cte table index
	 */
	private int addConstantTableEntry2(final String name, final IExpression initialValue, final OS_Type type, @NotNull final GeneratedFunction gf) {
		final TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, type, initialValue);
		final ConstantTableEntry cte = new ConstantTableEntry(gf.cte_list.size(), name, initialValue, tte);
		gf.cte_list.add(cte);
		return cte.index;
	}

	// endregion

	private int add_i(@NotNull final GeneratedFunction gf, final InstructionName x, final List<InstructionArgument> list_of, final Context ctx) {
		final int i = gf.add(x, list_of, ctx);
		return i;
	}

	private String getTypeString(@NotNull final OS_Type type) {
		switch (type.getType()) {
			case BUILT_IN:
				final BuiltInTypes bt = type.getBType();
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

	private int nextClassCode() { return module.parent.nextClassCode(); }
	private int nextFunctionCode() { return module.parent.nextFunctionCode(); }

}

//
//
//
