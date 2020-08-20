package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.ExpressionList;

public class FunctionCallPrelimInstruction implements FunctionPrelimInstruction {
    private final FunctionPrelimInstruction left_side;
    private ExpressionList args;
	private IntroducedExpressionList args2;

	public FunctionCallPrelimInstruction(FunctionPrelimInstruction fi, ExpressionList args) {
        this.left_side = fi;
        this.args = args;
    }

	public FunctionCallPrelimInstruction(FunctionPrelimInstruction fi, IntroducedExpressionList els) {
		this.left_side = fi;
		this.args2 = els;
	}

	@Override
    public int instructionNumber() {
        return _inst;
    }
    private int _inst;
    @Override
    public void setInstructionNumber(int i) {_inst = i;}
}
