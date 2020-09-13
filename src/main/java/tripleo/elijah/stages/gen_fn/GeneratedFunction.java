/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.DefFunctionDef;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.stages.instructions.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/10/20 2:57 PM
 */
public class GeneratedFunction {
	private final FunctionDef fd;
	private final DefFunctionDef dfd;
	private List<Label> labelList = new ArrayList<Label>();
	public List<Instruction> instructionsList = new ArrayList<>();
	private long instruction_index = 0;
	List<ConstantTableEntry> cte_list = new ArrayList<ConstantTableEntry>();
	List<VariableTableEntry> vte_list = new ArrayList<VariableTableEntry>();
	List<ProcTableEntry> prte_list = new ArrayList<ProcTableEntry>();
	List<TypeTableEntry> tte_list = new ArrayList<TypeTableEntry>();
	List<IdentTableEntry> idte_list = new ArrayList<IdentTableEntry>();
	List<FuncTableEntry> fte_list = new ArrayList<FuncTableEntry>();

	public GeneratedFunction(FunctionDef functionDef) {
		fd = functionDef;
		dfd = null;
	}

	public GeneratedFunction(DefFunctionDef dfd_) {
		dfd = dfd_;
		fd = null;
	}

	public List<Instruction> instructions() {
		return instructionsList;
	}

	public List<Label> labels() {
		return labelList;
	}

	public Instruction getInstruction(int anIndex) {
		return instructionsList.get(anIndex);
	}

	public void setLabel(Label l) {
		l.setIndex(instruction_index);
	}

	public long add(InstructionName aName, List<InstructionArgument> args_) {
		Instruction i = new Instruction();
		i.setIndex(instruction_index++);
		i.setName(aName);
		i.setArgs(args_);
		instructionsList.add(i);
		return i.getIndex();
	}

	@Override
	public String toString() {
		return String.format("<GeneratedFunction %s>", name());
	}

	public String name() {
		return fd != null ? fd.funName.getText() : dfd.funName;
	}
}

//
//
//
