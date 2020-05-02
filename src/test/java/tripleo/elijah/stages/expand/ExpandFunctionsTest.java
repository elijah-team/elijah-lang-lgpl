package tripleo.elijah.stages.expand;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.OS_Module;

public class ExpandFunctionsTest {

    @Test
    public void expand() {
        Compilation c = new Compilation(new StdErrSink(), new IO());
        final String file_name = "test/basic/listfolders2.elijah";
        c.feedCmdLine(Helpers.List_of(file_name, "-sO"));
        OS_Module mod = c.moduleFor(file_name);
        Assert.assertTrue(mod != null);
        ClassStatement kl = mod.getClassByName("Main");
        FunctionDef fd = kl.findFunction("main");
        FunctionContext fc = (FunctionContext) fd.getContext();
        final FunctionPrelimInstruction fi1 = fc.functionPrelimInstructions.get(0);
        assert fi1 instanceof IntroducedVariable;
        {
            final IntroducedVariable introducedVariable = (IntroducedVariable) fi1;
            Assert.assertTrue(introducedVariable.kind == IntroducedVariable.Type.PROCEDURE_CALL);
            Assert.assertTrue(((IdentExpression) introducedVariable.variable).getText().equals("MainLogic"));
            Assert.assertTrue(introducedVariable.args.expressions().size() == 0);
        }
        {
            final FunctionPrelimInstruction fi2 = fc.functionPrelimInstructions.get(1);
            assert fi2 instanceof DotExpressionInstruction;
            Assert.assertTrue(((DotExpressionInstruction) fi2).expr != null);
        }
//        final FunctionInstruction fi1 = fc.functionInstructions.get(0);
//        assert fi1 instanceof IntroducedVariable;
//        Assert.assertTrue(((IntroducedVariable) fi1).kind == IntroducedVariable.Type.PROCEDURE_CALL);
//        final FunctionInstruction fi1 = fc.functionInstructions.get(0);
//        assert fi1 instanceof IntroducedVariable;
//        Assert.assertTrue(((IntroducedVariable) fi1).kind == IntroducedVariable.Type.PROCEDURE_CALL);
//        final FunctionInstruction fi1 = fc.functionInstructions.get(0);
//        assert fi1 instanceof IntroducedVariable;
//        Assert.assertTrue(((IntroducedVariable) fi1).kind == IntroducedVariable.Type.PROCEDURE_CALL);
        int y=2;
    }
}