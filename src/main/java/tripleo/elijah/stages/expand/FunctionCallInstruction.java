package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.ExpressionList;

public class FunctionCallInstruction implements FunctionInstruction {
    private final FunctionInstruction left_side;
    private final ExpressionList args;

    public FunctionCallInstruction(FunctionInstruction fi, ExpressionList args) {
        this.left_side = fi;
        this.args = args;
    }
}
