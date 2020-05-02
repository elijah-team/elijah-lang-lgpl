package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.ExpressionList;

public class FunctionCallPrelimInstruction implements FunctionPrelimInstruction {
    private final FunctionPrelimInstruction left_side;
    private final ExpressionList args;

    public FunctionCallPrelimInstruction(FunctionPrelimInstruction fi, ExpressionList args) {
        this.left_side = fi;
        this.args = args;
    }
    @Override
    public int instructionNumber() {
        return _inst;
    }
    private int _inst;
    public void setInstructionNumber(int i) {_inst = i;}
}
