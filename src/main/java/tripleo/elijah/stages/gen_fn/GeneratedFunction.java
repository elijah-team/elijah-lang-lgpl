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
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;
import tripleo.util.range.Range;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 9/10/20 2:57 PM
 */
public class GeneratedFunction implements GeneratedNode {
	public final @Nullable FunctionDef fd;
	private final @Nullable DefFunctionDef dfd;
	private final List<Label> labelList = new ArrayList<Label>();
	public @NotNull List<Instruction> instructionsList = new ArrayList<Instruction>();
	public @NotNull List<Integer> deferred_calls = new ArrayList<Integer>();
	private int instruction_index = 0;
	public @NotNull List<ConstantTableEntry> cte_list = new ArrayList<ConstantTableEntry>();
	public @NotNull List<VariableTableEntry> vte_list = new ArrayList<VariableTableEntry>();
	public @NotNull List<ProcTableEntry> prte_list = new ArrayList<ProcTableEntry>();
	@NotNull List<TypeTableEntry> tte_list = new ArrayList<TypeTableEntry>();
	@NotNull
	public List<IdentTableEntry> idte_list = new ArrayList<IdentTableEntry>();
	private int label_count = 0;
	private int _nextTemp = 0;

	public GeneratedFunction(final FunctionDef functionDef) {
		fd = functionDef;
		dfd = null;
	}

	public GeneratedFunction(final DefFunctionDef dfd_) {
		dfd = dfd_;
		fd = null;
	}

	public static void printTables(GeneratedFunction gf) {
		System.out.println("VariableTable ");
		for (VariableTableEntry variableTableEntry : gf.vte_list) {
			System.out.println("\t"+variableTableEntry);
		}
		System.out.println("ConstantTable ");
		for (ConstantTableEntry constantTableEntry : gf.cte_list) {
			System.out.println("\t"+constantTableEntry);
		}
		System.out.println("ProcTable     ");
		for (ProcTableEntry procTableEntry : gf.prte_list) {
			System.out.println("\t"+procTableEntry);
		}
		System.out.println("TypeTable     ");
		for (TypeTableEntry typeTableEntry : gf.tte_list) {
			System.out.println("\t"+typeTableEntry);
		}
		System.out.println("IdentTable    ");
		for (IdentTableEntry identTableEntry : gf.idte_list) {
			System.out.println("\t"+identTableEntry);
		}
	}

	//
	// region Ident-IA
	//

	public String getIdentIAPath(final IdentIA ia2) {
		assert ia2.gf == this;
		final List<InstructionArgument> s = _getIdentIAPathList(ia2);

		//
		// TODO NOT LOOKING UP THINGS, IE PROPERTIES, MEMBERS
		//
		List<String> sl = new ArrayList<String>();
		for (final InstructionArgument ia : s) {
			final String text;
			if (ia instanceof IntegerIA) { // should only be the first element if at all
				final VariableTableEntry vte = getVarTableEntry(DeduceTypes2.to_int(ia));
				text = "vv"+vte.getName();
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = getIdentTableEntry(((IdentIA) ia).getIndex());
				OS_Element resolved_element = idte.resolved_element;
				if (resolved_element != null) {
					if (resolved_element instanceof ClassStatement) {
						// Assuming constructor call
						// TODO what about named contrcuctors
						int code = ((ClassStatement) resolved_element)._a.getCode();
						text = String.format("ZC%d", code);
					} else if (resolved_element instanceof FunctionDef) {
						OS_Element parent = resolved_element.getParent();
						int code;
						if (parent instanceof ClassStatement)
							code = ((ClassStatement) parent)._a.getCode();
						else if (parent instanceof NamespaceStatement) {
							code = ((NamespaceStatement) parent)._a.getCode();
						} else // TODO what about FunctionDef, etc
							code = -1;
						// TODO what about overloaded functions
						text = String.format("Z%d%s", code, ((FunctionDef) resolved_element).name());
					} else if (resolved_element instanceof VariableStatement) {
						// first getParent is VariableSequence
						if (resolved_element.getParent().getParent() == getFD().getParent()) {
							// A direct member value. Doesn't handle when indirect
							text = "vsc->vm" + ((VariableStatement) resolved_element).getName();
						} else
							text = "vv"+((VariableStatement) resolved_element).getName();
					} else {
//						throw new NotImplementedException();
						text = idte.getIdent().getText();
						System.out.println("1008 "+resolved_element.getClass().getName());
					}
				} else
					text = idte.getIdent().getText();
			} else
				throw new NotImplementedException();
			sl.add(text);
		}
		return Helpers.String_join(".", sl);
	}

	public @NotNull List<InstructionArgument> _getIdentIAPathList(@NotNull InstructionArgument oo) {
		List<InstructionArgument> s = new LinkedList<InstructionArgument>();
		while (oo != null) {
			if (oo instanceof IntegerIA) {
				s.add(0, oo);
				oo = null;
			} else if (oo instanceof IdentIA) {
				final IdentTableEntry ite1 = getIdentTableEntry(((IdentIA) oo).getIndex());
				s.add(0, oo);
				oo = ite1.backlink;
			} else
				throw new IllegalStateException("Invalid InstructionArgument");
		}
		return s;
	}

	/**
	 * Returns a string that represents the path encoded in ia2.
	 * Does not transform the string into target language (ie C).
	 * Called from {@link DeduceTypes2.do_assign_call}
	 *
	 * @param ia2 the path
	 * @return a string that represents the path encoded in ia2
	 */
	public String getIdentIAPathNormal(final IdentIA ia2) {
		final List<InstructionArgument> s = _getIdentIAPathList(ia2);

		//
		// TODO NOT LOOKING UP THINGS, IE PROPERTIES, MEMBERS
		//
		List<String> sl = new ArrayList<String>();
		for (final InstructionArgument ia : s) {
			final String text;
			if (ia instanceof IntegerIA) {
				final VariableTableEntry vte = getVarTableEntry(DeduceTypes2.to_int(ia));
				text = vte.getName();
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = getIdentTableEntry(((IdentIA) ia).getIndex());
				text = idte.getIdent().getText();
			} else
				throw new NotImplementedException();
			sl.add(text);
		}
		return Helpers.String_join(".", sl);
	}


	// endregion

	//
	// region INSTRUCTIONS
	//

	public @NotNull List<Instruction> instructions() {
		return instructionsList;
	}

	public Instruction getInstruction(final int anIndex) {
		return instructionsList.get(anIndex);
	}

	public int add(final InstructionName aName, final List<InstructionArgument> args_, final Context ctx) {
		final Instruction i = new Instruction();
		i.setIndex(instruction_index++);
		i.setName(aName);
		i.setArgs(args_);
		i.setContext(ctx);
		instructionsList.add(i);
		return i.getIndex();
	}

	// endregion

	//
	// region toString
	//

	@Override
	public String toString() {
		return String.format("<GeneratedFunction %s>", name());
	}

	public String name() {
		return fd != null ? fd.name() : dfd.name();
	}

	// endregion

	//
	// region LABELS
	//

	public @NotNull Label addLabel() {
		return addLabel("__label", true);
	}

	public @NotNull Label addLabel(final String base_name, final boolean append_int) {
		final Label label = new Label(this);
		final String name;
		if (append_int) {
			label.setNumber(label_count);
			name = String.format("%s%d", base_name, label_count++);
		} else {
			name = base_name;
			label.setNumber(label_count);
		}
		label.setName(name);
		labelList.add(label);
		return label;
	}

	public void place(@NotNull final Label label) {
		label.setIndex(instruction_index);
	}

	public @NotNull List<Label> labels() {
		return labelList;
	}

	// endregion

	//
	// region get-entries
	//

	public VariableTableEntry getVarTableEntry(final int index) {
		return vte_list.get(index);
	}

	public IdentTableEntry getIdentTableEntry(final int index) {
		return idte_list.get(index);
	}

	public ConstantTableEntry getConstTableEntry(final int index) {
		return cte_list.get(index);
	}

	public TypeTableEntry getTypeTableEntry(final int index) {
		return tte_list.get(index);
	}

	public ProcTableEntry getProcTableEntry(final int index) {
		return prte_list.get(index);
	}

	// endregion

	public @NotNull TypeTableEntry newTypeTableEntry(final TypeTableEntry.Type type1, final OS_Type type) {
		return newTypeTableEntry(type1, type, null);
	}

	public @NotNull TypeTableEntry newTypeTableEntry(final TypeTableEntry.Type type1, final OS_Type type, final IExpression expression) {
		final TypeTableEntry typeTableEntry = new TypeTableEntry(tte_list.size(), type1, type, expression);
		tte_list.add(typeTableEntry);
		return typeTableEntry;
	}

	public @NotNull OS_Element getFD() {
		if (fd != null) return fd;
		if (dfd != null) return dfd;
		throw new IllegalStateException("No function");
	}

	public void addContext(final Context context, final Range r) {
//		contextToRangeMap.put(r, context);
	}

	public Context getContextFromPC(final int pc) {
//		for (Map.Entry<Range, Context> rangeContextEntry : contextToRangeMap.entrySet()) {
//			if (rangeContextEntry.getKey().has(pc))
//				return rangeContextEntry.getValue();
//		}
//		return null;
		return instructionsList.get(pc).getContext();
	}

//	Map<Range, Context> contextToRangeMap = new HashMap<Range, Context>();

	/**
	 *
	 * @param text variable name from the source file
	 * @return {@link IntegerIA} or {@link ConstTableIA} or null if not found, meaning not a local variable
	 */
	public @Nullable InstructionArgument vte_lookup(final String text) {
		int index = 0;
		for (final VariableTableEntry variableTableEntry : vte_list) {
			final String variableTableEntryName = variableTableEntry.getName();
			if (variableTableEntryName != null) // TODO how can this be null?
				if (variableTableEntryName.equals(text))
					return new IntegerIA(index);
			index++;
		}
		index = 0;
		for (final ConstantTableEntry constTableEntry : cte_list) {
			final String constTableEntryName = constTableEntry.getName();
			if (constTableEntryName != null) // TODO how can this be null?
				if (constTableEntryName.equals(text))
					return new ConstTableIA(index, this);
			index++;
		}
		return null;
	}

	public @NotNull InstructionArgument get_assignment_path(@NotNull final IExpression expression, @NotNull final GenerateFunctions generateFunctions) {
		switch (expression.getKind()) {
		case DOT_EXP:
			{
				final DotExpression de = (DotExpression) expression;
				final InstructionArgument left_part = get_assignment_path(de.getLeft(), generateFunctions);
				return get_assignment_path(left_part, de.getRight(), generateFunctions);
			}
		case QIDENT:
			throw new NotImplementedException();
		case PROCEDURE_CALL:
			throw new NotImplementedException();
		case GET_ITEM:
			throw new NotImplementedException();
		case IDENT:
			{
				final IdentExpression ie = (IdentExpression) expression;
				final String text = ie.getText();
				final InstructionArgument lookup = vte_lookup(text); // IntegerIA(variable) or ConstTableIA or null
				if (lookup != null)
					return lookup;
				final int ite = addIdentTableEntry(ie);
				return new IdentIA(ite, this);
			}
		default:
			throw new IllegalStateException("Unexpected value: " + expression.getKind());
		}
	}

	private @NotNull InstructionArgument get_assignment_path(final InstructionArgument prev, @NotNull final IExpression expression, @NotNull final GenerateFunctions generateFunctions) {
		switch (expression.getKind()) {
		case DOT_EXP:
		{
			final DotExpression de = (DotExpression) expression;
			final InstructionArgument left_part = get_assignment_path(de.getLeft(), generateFunctions);
			if (left_part instanceof IdentIA) {
				((IdentIA)left_part).setPrev(prev);
			} else
				throw new NotImplementedException();
			return get_assignment_path(left_part, de.getRight(), generateFunctions);
		}
		case QIDENT:
			throw new NotImplementedException();
		case PROCEDURE_CALL:
			throw new NotImplementedException();
		case GET_ITEM:
			throw new NotImplementedException();
		case IDENT:
			{
				final IdentExpression ie = (IdentExpression) expression;
				final int ite = addIdentTableEntry(ie);
				final IdentIA identIA = new IdentIA(ite, this);
				identIA.setPrev(prev);
				return identIA;
			}
		default:
			throw new IllegalStateException("Unexpected value: " + expression.getKind());
		}
	}

	public int nextTemp() {
		return ++_nextTemp;
	}

	public @Nullable Label findLabel(final int index) {
		for (final Label label : labelList) {
			if (label.getIndex() == index)
				return label;
		}
		return null;
	}

	public VariableTableEntry getSelf() {
		if (getFD().getParent() instanceof ClassStatement)
			return getVarTableEntry(0);
		else
			return null;
	}

	/**
	 * Returns first {@link IdentTableEntry} that matches expression
	 * Only works for IdentExpressions
	 *
	 * @param expression {@link IdentExpression} to test for
	 * @return IdentTableEntry or null
	 */
	public IdentTableEntry getIdentTableEntryFor(IExpression expression) {
		for (IdentTableEntry identTableEntry : idte_list) {
			if (identTableEntry.getIdent().equals(expression)) {
				return identTableEntry;
			}
		}
		return null;
	}

	public int addIdentTableEntry(final IdentExpression ident) {
		for (int i = 0; i < idte_list.size(); i++) {
			if (idte_list.get(i).getIdent() == ident)
				return i;
		}
		final IdentTableEntry idte = new IdentTableEntry(idte_list.size(), ident);
		idte_list.add(idte);
		return idte.getIndex();
	}
}

//
//
//
