package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.IExpression;

import java.util.List;

public class IntroducedFunction implements FunctionPrelimInstruction {
    private final IExpression base;
    private String funName;
    private List<IExpression> args;

    public IntroducedFunction(IExpression expression) {
        this.base = expression;
    }

    @Override
    public int instructionNumber() {
        return _inst;
    }
    private int _inst;
    public void setInstructionNumber(int i) {_inst = i;}

    public void setName(String s) {
        funName = s;
    }

    public void setArgs(List<IExpression> list) {
        this.args = list;
    }
}
