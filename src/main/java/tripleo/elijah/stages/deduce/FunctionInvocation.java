/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;

/**
 * Created 1/21/21 9:04 PM
 */
public class FunctionInvocation {
	private final FunctionDef fd;
	final ProcTableEntry pte;
	private ClassInvocation classInvocation;

	public FunctionInvocation(FunctionDef aFunctionDef, ProcTableEntry aProcTableEntry) {
		this.fd = aFunctionDef;
		this.pte = aProcTableEntry;
	}

	public GeneratedFunction getGenerated() {
		return null;
	}

	public FunctionDef getFunction() {
		return fd;
	}

	public void setClassInvocation(ClassInvocation aClassInvocation) {
		classInvocation = aClassInvocation;
	}

	public ClassInvocation getClassInvocation() {
		return classInvocation;
	}
}

//
//
//
