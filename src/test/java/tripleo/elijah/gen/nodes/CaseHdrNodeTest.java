package tripleo.elijah.gen.nodes;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.lang.VariableReference;

import static org.junit.Assert.*;

public class CaseHdrNodeTest {
	
	@Test
	public void simpleGenText() {
		VariableReference vr = new VariableReference();
		vr.setMain("the");
		CaseHdrNode chn = new CaseHdrNode(vr);
		Assert.assertEquals("the", chn.simpleGenText());
	}
	@Test
	public void setExpr() {
	}
}