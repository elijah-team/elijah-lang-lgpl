package tripleo.elijah.stages.expand;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.OS_Module;

public class ExpandFunctionsTest {

    @Test
    public void expand() {
        Compilation c = new Compilation(new StdErrSink(), new IO());
        final String file_name = "test/basic/listfolders2.elijah";
        c.feedCmdLine(Helpers.List_of(file_name, "-sO"));
        OS_Module mod = c.moduleFor(file_name);
        Assert.assertTrue(mod != null);
    }
}