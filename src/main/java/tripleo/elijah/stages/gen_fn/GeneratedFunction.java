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

	TypeTableEntry newTypeTableEntry(TypeTableEntry.Type type1, OS_Type type) {
		return newTypeTableEntry(type1, type, null);
	}

	TypeTableEntry newTypeTableEntry(TypeTableEntry.Type type1, OS_Type type, IExpression expression) {
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
			if (variableTableEntry.getName().equals(text))
				return new IntegerIA(index);
			index++;
		}
		index = 0;
		for (ConstantTableEntry constTableEntry : cte_list) {
			if (constTableEntry.getName().equals(text))
				return new ConstTableIA(index, this);
			index++;
		}
		return null;
	}
}

//
//
//
