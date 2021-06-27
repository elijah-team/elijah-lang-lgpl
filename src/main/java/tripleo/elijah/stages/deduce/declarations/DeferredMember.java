/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce.declarations;

import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.stages.deduce.IInvocation;
import tripleo.elijah.stages.gen_fn.GenType;

/**
 * Created 6/27/21 1:41 AM
 */
public class DeferredMember {
	private final OS_Element parent;
	private final IInvocation invocation;
	private final VariableStatement variableStatement;
	private final DeferredObject<GenType, Diagnostic, Void> typePromise = new DeferredObject<GenType, Diagnostic, Void>();

	public DeferredMember(OS_Element aParent, IInvocation aInvocation, VariableStatement aVariableStatement) {
		parent = aParent;
		invocation = aInvocation;
		variableStatement = aVariableStatement;
	}

	public Promise<GenType, Diagnostic, Void> typePromise() {
		return typePromise;
	}

	public OS_Element getParent() {
		return parent;
	}

	public IInvocation getInvocation() {
		return invocation;
	}

	public VariableStatement getVariableStatement() {
		return variableStatement;
	}

	// for DeducePhase
	public DeferredObject<GenType, Diagnostic, Void> typeResolved() {
		return typePromise;
	}
}

//
//
//