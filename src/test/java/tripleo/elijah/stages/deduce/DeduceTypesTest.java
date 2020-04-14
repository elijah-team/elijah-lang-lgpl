package tripleo.elijah.stages.deduce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;

public class DeduceTypesTest {

	private OS_Type x;

	@Before
	public void setUp() {
		OS_Module mod = new OS_Module();
		DeduceTypes d = new DeduceTypes(mod);
		ClassStatement cs = new ClassStatement(mod);
		cs.setName(Helpers.makeToken("Test"));
		final FunctionDef fd = cs.funcDef();
		fd.setName(Helpers.makeToken("test"));
		VariableSequence vss = fd.scope().statementClosure().varSeq();
		final VariableStatement vs = vss.next();
		vs.setName(Helpers.makeToken("x"));
		final Qualident qu = new Qualident();
		qu.append(Helpers.makeToken("Integer"));
		vs.typeName().setName(qu);
		FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		x = d.deduceExpression(new IdentExpression(Helpers.makeToken("x")), fc);
		System.out.println(x);
	}
	@Test
	public void testDeduceIdentExpression1() {
		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
	}
	@Test
	public void testDeduceIdentExpression2() {
		final RegularTypeName tn = new RegularTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn), x.getTypeName());
	}
	@Test
	public void testDeduceIdentExpression3() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
	}
	@Test
	public void testDeduceIdentExpression3_5() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
	}
	@Test
	public void testDeduceIdentExpression4() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

}
