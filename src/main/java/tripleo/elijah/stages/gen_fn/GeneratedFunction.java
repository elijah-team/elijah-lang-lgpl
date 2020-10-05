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
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.NotImplementedException;
import tripleo.util.range.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/10/20 2:57 PM
 */
public class GeneratedFunction {
	private final FunctionDef fd;
	private final DefFunctionDef dfd;
	private final List<Label> labelList = new ArrayList<Label>();
	public List<Instruction> instructionsList = new ArrayList<Instruction>();
	public List<Integer> deferred_calls = new ArrayList<Integer>();
	private int instruction_index = 0;
	public List<ConstantTableEntry> cte_list = new ArrayList<ConstantTableEntry>();
	public List<VariableTableEntry> vte_list = new ArrayList<VariableTableEntry>();
	public List<ProcTableEntry> prte_list = new ArrayList<ProcTableEntry>();
	List<TypeTableEntry> tte_list = new ArrayList<TypeTableEntry>();
	List<IdentTableEntry> idte_list = new ArrayList<IdentTableEntry>();
	private int label_count = 0;

	public GeneratedFunction(FunctionDef functionDef) {
		fd = functionDef;
		dfd = null;
	}

	public GeneratedFunction(DefFunctionDef dfd_) {
		dfd = dfd_;
		fd = null;
	}

	//
	// INSTRUCTIONS
	//

	public List<Instruction> instructions() {
		return instructionsList;
	}

	public Instruction getInstruction(int anIndex) {
		return instructionsList.get(anIndex);
	}

	public int add(InstructionName aName, List<InstructionArgument> args_, Context ctx) {
		Instruction i = new Instruction();
		i.setIndex(instruction_index++);
		i.setName(aName);
		i.setArgs(args_);
		i.setContext(ctx);
		instructionsList.add(i);
		return i.getIndex();
	}

	//
	// toString
	//

	@Override
	public String toString() {
		return String.format("<GeneratedFunction %s>", name());
	}

	public String name() {
		return fd != null ? fd.funName.getText() : dfd.funName;
	}

	//
	// LABELS
	//

	public Label addLabel(String base_name, boolean append_int) {
		Label label = new Label();
		String name;
		if (append_int) {
			name = String.format("%s%d", base_name, label_count++);
		} else
			name = base_name;
		label.setName(name);
		labelList.add(label);
		return label;
	}

	public void place(Label label) {
		label.setIndex(instruction_index);
	}

	public void setLabel(Label l) {
		l.setIndex(instruction_index);
	}

	public List<Label> labels() {
		return labelList;
	}

	//
	//
	//
	public VariableTableEntry getVarTableEntry(int index) {
		return vte_list.get(index);
	}

	public ConstantTableEntry getConstTableEntry(int index) {
		return cte_list.get(index);
	}

	public ProcTableEntry getProcTableEntry(int index) {
		return prte_list.get(index);
	}

	public TypeTableEntry newTypeTableEntry(TypeTableEntry.Type type1, OS_Type type) {
		return newTypeTableEntry(type1, type, null);
	}

	public TypeTableEntry newTypeTableEntry(TypeTableEntry.Type type1, OS_Type type, IExpression expression) {
		final TypeTableEntry typeTableEntry = new TypeTableEntry(tte_list.size(), type1, type, expression);
		tte_list.add(typeTableEntry);
		return typeTableEntry;
	}

	public OS_Element getFD() {
		return fd != null ? fd : dfd;
	}

	public void addContext(Context context, Range r) {
//		contextToRangeMap.put(r, context);
	}

	public Context getContextFromPC(int pc) {
//		for (Map.Entry<Range, Context> rangeContextEntry : contextToRangeMap.entrySet()) {
//			if (rangeContextEntry.getKey().has(pc))
//				return rangeContextEntry.getValue();
//		}
//		return null;
		return instructionsList.get(pc).getContext();
	}

//	Map<Range, Context> contextToRangeMap = new HashMap<Range, Context>();

	public InstructionArgument vte_lookup(String text) {
		int index = 0;
		for (VariableTableEntry variableTableEntry : vte_list) {
			final String variableTableEntryName = variableTableEntry.getName();
			if (variableTableEntryName != null) // TODO how can this be null?
				if (variableTableEntryName.equals(text))
					return new IntegerIA(index);
			index++;
		}
		index = 0;
		for (ConstantTableEntry constTableEntry : cte_list) {
			final String constTableEntryName = constTableEntry.getName();
			if (constTableEntryName != null) // TODO how can this be null?
				if (constTableEntryName.equals(text))
					return new ConstTableIA(index, this);
			index++;
		}
		return null;
	}

	public IdentTableEntry getIdentTableEntry(int i) {
		return idte_list.get(i);
	}

	public InstructionArgument get_assignment_path(IExpression expression, GenerateFunctions generateFunctions) {
		switch (expression.getKind()) {
		case DOT_EXP:
			{
				DotExpression de = (DotExpression) expression;
				InstructionArgument left_part = get_assignment_path(de.getLeft(), generateFunctions);
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
				IdentExpression ie = (IdentExpression) expression;
				String text = ie.getText();
				InstructionArgument lookup = vte_lookup(text); // IntegerIA(variable) or ConstTableIA or null
				if (lookup != null)
					return lookup;
				int ite = generateFunctions.addIdentTableEntry(ie, this);
				return new IdentIA(ite, this);
			}
		default:
			throw new IllegalStateException();
		}
	}

	private InstructionArgument get_assignment_path(InstructionArgument prev, IExpression expression, GenerateFunctions generateFunctions) {
		switch (expression.getKind()) {
		case DOT_EXP:
		{
			DotExpression de = (DotExpression) expression;
			InstructionArgument left_part = get_assignment_path(de.getLeft(), generateFunctions);
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
				IdentExpression ie = (IdentExpression) expression;
				int ite = generateFunctions.addIdentTableEntry(ie, this);
				final IdentIA identIA = new IdentIA(ite, this);
				identIA.setPrev(prev);
				return identIA;
			}
		default:
			throw new IllegalStateException();
		}
	}
}

//
//
//
