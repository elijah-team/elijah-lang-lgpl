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
        assert fc.functionInstructions.get(0) instanceof IntroducedVariable;
        int y=2;
    }
}