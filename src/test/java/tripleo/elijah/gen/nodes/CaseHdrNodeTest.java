package tripleo.elijah.gen.nodes;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.lang.VariableReference;

import static org.junit.Assert.*;

public class CaseHdrNodeTest {
	
	@Test
	public void simpleGenText() {
		VariableReferenceNode3 vr = new VariableReferenceNode3("the", new ScopeNode(), null);
		CaseHdrNode chn = new CaseHdrNode(vr);
		String actual = chn.simpleGenText();
		Assert.assertEquals("vvthe", actual);
	}
	@Test
	public void setExpr() {
	}
}