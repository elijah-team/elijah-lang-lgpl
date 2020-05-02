package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.IExpression;

public class DotExpressionInstruction implements FunctionPrelimInstruction {
    private final FunctionPrelimInstruction variable;
    final IExpression expr;

    public DotExpressionInstruction(FunctionPrelimInstruction i, IExpression de) {
        this.variable = i;
        this.expr     = de;
    }

    @Override
    public String toString() {
        return "DotExpressionInstruction{" +
                "variable=" + variable +
                ", dot_exp=" + expr +
                '}';
    }
    @Override
    public int instructionNumber() {
        return _inst;
    }
    private int _inst;
    public void setInstructionNumber(int i) {_inst = i;}
}
