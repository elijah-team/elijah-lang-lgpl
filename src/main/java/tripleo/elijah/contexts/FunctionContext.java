/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.expand.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {

	private final FunctionDef carrier;
	public List<FunctionPrelimInstruction> functionPrelimInstructions = new ArrayList<FunctionPrelimInstruction>();
	private int functionPrelimInstructionsNumber = 1;

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
		addPrelimInstruction(introducedVariable);
		return introducedVariable;
	}

	public IntroducedVariable introduceVariable(VariableStatement variable) {
		System.out.println("[#introduceVariable] "+variable);
		final IntroducedVariable introducedVariable = new IntroducedVariable(variable);
		variableTable.add(introducedVariable);
		addPrelimInstruction(introducedVariable);
		return introducedVariable;
	}

	List<IntroducedVariable> variableTable = new ArrayList<IntroducedVariable>();

	public DotExpressionInstruction dotExpression(FunctionPrelimInstruction i, IExpression de) {
		DotExpressionInstruction dei = new DotExpressionInstruction(i, de);
		addPrelimInstruction(dei);
		return dei;
	}

	public FunctionCallPrelimInstruction makeProcCall(FunctionPrelimInstruction fi, ExpressionList args) {
		IntroducedExpressionList els = simplifyExpressionList(args);
		FunctionCallPrelimInstruction fci = new FunctionCallPrelimInstruction(fi, els);
		addPrelimInstruction(fci);
		return fci;
	}

	private IntroducedExpressionList simplifyExpressionList(ExpressionList args) {
		IntroducedExpressionList args1 = new IntroducedExpressionList();
		if (args == null) return args1;
		for (IExpression arg : args) {
			int y=2;
			if (arg.getKind() == ExpressionKind.PROCEDURE_CALL) {
				FunctionPrelimInstruction i = null; // TODO
				if (arg.getLeft().getKind() == ExpressionKind.IDENT) {
					i=introduceVariable(arg.getLeft());
					final ExpressionList args2 = ((ProcedureCallExpression) arg).getArgs();
					final IntroducedExpressionList expressionList = simplifyExpressionList(args2);
					((IntroducedVariable)i).makeIntoFunctionCall(expressionList);
					args1.add(i);
				}
				//makeProcCall(i, ((ProcedureCallExpression)arg).getArgs());
			} else if (arg.getKind() == ExpressionKind.FUNC_EXPR) {
				int yy=2;
				FunctionPrelimInstruction i = introduceVariable(arg);
				args1.add(i);
			}
		}
		return args1;
	}

//	public FunctionPrelimInstruction assign(FunctionPrelimInstruction fi, FunctionPrelimInstruction fi2) {
//		AssignPrelimInstruction api = new AssignPrelimInstruction(fi, fi2);
//		addPrelimInstruction(api);
//		return api;
//	}

	public FunctionPrelimInstruction assign(FunctionPrelimInstruction fi, IExpression fi2) {
		AssignPrelimInstruction api = new AssignPrelimInstruction(fi, fi2);
		addPrelimInstruction(api);
		return api;
	}

	private void addPrelimInstruction(final @NotNull FunctionPrelimInstruction fpi) {
		functionPrelimInstructions.add(fpi);
		fpi.setInstructionNumber(functionPrelimInstructionsNumber++);
	}

	public FunctionPrelimInstruction introduceFunction(IExpression expression) {
		System.out.println("[#introduceFunction] "+expression);
		final IntroducedFunction introducedFunction = new IntroducedFunction(expression);
//		variableTable.add(introducedFunction);
		addPrelimInstruction(introducedFunction);
		return introducedFunction;
	}
}

//
//
//
