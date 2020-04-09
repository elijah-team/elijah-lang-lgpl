package tripleo.elijah.stages.deduce;

import org.junit.Assert;
import org.junit.Test;

import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.ClassStatement;
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
		OS_Type x = d.deduceExpression(new IdentExpression(Helpers.makeToken("x")), fc);
		System.out.println(x);
		Assert.assertEquals(x.getBType(), new OS_Type(BuiltInTypes.SystemInteger).getBType());
	}

}
