package tripleo.elijah.stages.deduce;

import org.junit.Assert;
import org.junit.Test;

import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionContext;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.NumericExpression;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.lang2.BuiltInTypes;

public class DeduceTypesTest {

	@Test
	public void testDeduceNumericExpression() {
		DeduceTypes d = new DeduceTypes(null);
		OS_Type x = d.deduceExpression(new NumericExpression(3), null);
		System.out.println(x);
		Assert.assertEquals(x.getBType(), new OS_Type(BuiltInTypes.SystemInteger).getBType());
	}

	@Test
	public void testDeduceIdentExpression() {
		DeduceTypes d = new DeduceTypes(null);
		OS_Module mod = new OS_Module();
		ClassStatement cs = new ClassStatement(mod);
		cs.setName(Helpers.makeToken("Test"));
		final FunctionDef fd = cs.funcDef();
		fd.setName(Helpers.makeToken("test"));
		FunctionContext fc = (FunctionContext) fd.getContext(); // new FunctionContext(fd); // TODO needs to be mocked
		VariableSequence vss = new VariableSequence();
		final VariableStatement vs = new VariableStatement(vss);
		vs.setName(Helpers.makeToken("x"));
		fd.add(vss)
		final Qualident qu = new Qualident();
		qu.append(Helpers.makeToken("Integer"));
		vs.typeName().setName(qu);
//		fc.add(vs, "x");
		OS_Type x = d.deduceExpression(new IdentExpression(Helpers.makeToken("x")), fc);
		System.out.println(x);
		Assert.assertEquals(x.getBType(), new OS_Type(BuiltInTypes.SystemInteger).getBType());
	}

}
