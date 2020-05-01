/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.*;
import tripleo.elijah.stages.expand.DotExpressionInstruction;
import tripleo.elijah.stages.expand.FunctionCallInstruction;
import tripleo.elijah.stages.expand.FunctionInstruction;
import tripleo.elijah.stages.expand.IntroducedVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {

	private final FunctionDef carrier;
	private List<FunctionInstruction> functionInstructions = new ArrayList<FunctionInstruction>();

	public FunctionContext(FunctionDef functionDef) {
		carrier = functionDef;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		for (FunctionItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof VariableSequence)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, (OS_Element) item);
				}
			} else if (item instanceof VariableSequence) {
				System.out.println("[FunctionContext#lookup] VariableSequence "+item);
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs);
				}
			}
		}
		for (FormalArgListItem arg : carrier.getArgs()) {
			if (arg.name.getText().equals(name)) {
				Result.add(name, level, arg);
			}
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result);
		return Result;
		
	}

    public IntroducedVariable introduceVariable(IExpression variable) {
		System.out.println("[#introduceVariable] "+variable);
		final IntroducedVariable introducedVariable = new IntroducedVariable(variable);
		variableTable.add(introducedVariable);
		functionInstructions.add(introducedVariable);
		return introducedVariable;
	}

	public IntroducedVariable introduceVariable(VariableStatement variable) {
		System.out.println("[#introduceVariable] "+variable);
		final IntroducedVariable introducedVariable = new IntroducedVariable(variable);
		variableTable.add(introducedVariable);
		functionInstructions.add(introducedVariable);
		return introducedVariable;
	}

	List<IntroducedVariable> variableTable = new ArrayList<IntroducedVariable>();

	public DotExpressionInstruction dotExpression(FunctionInstruction i, IExpression de) {
		DotExpressionInstruction dei = new DotExpressionInstruction(i, de);
		functionInstructions.add(dei);
		return dei;
	}

	public FunctionCallInstruction makeProcCall(FunctionInstruction fi, ExpressionList args) {
		int y=2;
//		((IntroducedVariable)fi).makeIntoFunctionCall(args);
		FunctionCallInstruction fci = new FunctionCallInstruction(fi, args);
		functionInstructions.add(fci);
		return fci;
	}
}

//
//
//
