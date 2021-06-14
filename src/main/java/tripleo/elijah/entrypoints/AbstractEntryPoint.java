/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.entrypoints;

import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.gen_fn.GenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 6/14/21 7:23 AM
 */
public class AbstractEntryPoint implements EntryPoint {
	private List<FunctionInvocation> dependentFunctions = new ArrayList<FunctionInvocation>();
	private List<GenType> dependentTypes = new ArrayList<GenType>();

	@Override
	public List<GenType> dependentTypes() {
		return dependentTypes;
	}

	@Override
	public List<FunctionInvocation> dependentFunctions() {
		return dependentFunctions;
	}

	public void addDependentType(GenType aType) {
		dependentTypes.add(aType);
	}

	public void addDependentFunction(FunctionInvocation aFunction) {
		dependentFunctions.add(aFunction);
	}
}

//
//
//
