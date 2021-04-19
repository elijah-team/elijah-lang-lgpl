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
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.instructions.InstructionArgument;

import java.util.List;

/**
 * Created 9/12/20 10:07 PM
 */
public class ProcTableEntry implements TableEntryIV {
	public final int index;
	public final List<TypeTableEntry> args;
	/**
	 * Either a hint to the programmer-- The compiler should be able to work without this.
	 * <br/>
	 * Or for synthetic methods
	 */
	public final IExpression expression;
	public final InstructionArgument expression_num;
	public OS_Element resolved_element;
	private ClassInvocation classInvocation;
	private FunctionInvocation functionInvocation;

	public ProcTableEntry(final int index, final IExpression aExpression, final InstructionArgument expression_num, final List<TypeTableEntry> args) {
		this.index = index;
		this.expression = aExpression;
		this.expression_num = expression_num;
		this.args = args;
	}

	@Override @NotNull
	public String toString() {
		return "ProcTableEntry{" +
				"index=" + index +
				", expression=" + expression +
				", expression_num=" + expression_num +
				", args=" + args +
				'}';
	}

	public List<TypeTableEntry> getArgs() {
		return args;
	}

	public void setArgType(int aIndex, OS_Type aType) {
		args.get(aIndex).attached = aType;
	}

	public void setClassInvocation(ClassInvocation aClassInvocation) {
		classInvocation = aClassInvocation;
	}

	public ClassInvocation getClassInvocation() {
		return classInvocation;
	}

	public void setResolvedElement(OS_Element aResolvedElement) {
		resolved_element = aResolvedElement;
	}

	public OS_Element getResolvedElement() {
		return resolved_element;
	}

	public void setFunctionInvocation(FunctionInvocation aFunctionInvocation) {
		functionInvocation = aFunctionInvocation;
	}

	public FunctionInvocation getFunctionInvocation() {
		return functionInvocation;
	}
}

//
//
//
