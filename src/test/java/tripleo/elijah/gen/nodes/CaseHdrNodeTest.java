package tripleo.elijah.gen.nodes;

import org.junit.Assert;
import org.junit.Test;

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