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
	private long index = 0;

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
		l.setIndex(index);
	}

	public void add(InstructionName aName, List<InstructionArgument> args_) {
		Instruction i = new Instruction();
		i.setIndex(index++);
		i.setName(aName);
		i.setArgs(args_);
		instructionsList.add(i);
	}
}

//
//
//
