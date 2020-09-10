package tripleo.elijah;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.util.Helpers;

public class QualidentToDotExpresstionTest {

    @Test
    public void qualidentToDotExpression2() {
        Qualident q = new Qualident();
        q.append(Helpers.makeToken("a"));
        q.append(tripleo.elijah.util.Helpers.makeToken("b"));
        q.append(tripleo.elijah.util.Helpers.makeToken("c"));
        IExpression e = DeduceTypes.qualidentToDotExpression2(q.parts());
        System.out.println(e);
        Assert.assertEquals("a.b.c", e.toString());
    }
}