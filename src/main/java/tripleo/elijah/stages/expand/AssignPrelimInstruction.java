package tripleo.elijah.stages.expand;

public class AssignPrelimInstruction implements FunctionPrelimInstruction {
    private final FunctionPrelimInstruction var;
    private final FunctionPrelimInstruction expr;

    public AssignPrelimInstruction(FunctionPrelimInstruction fi, FunctionPrelimInstruction fi2) {
        this.var = fi;
        this.expr = fi2;
    }

    public void setInstructionNumber(int i) {_inst = i;}
    @Override
    public int instructionNumber() {
        return _inst;
    }
    private int _inst;
}
