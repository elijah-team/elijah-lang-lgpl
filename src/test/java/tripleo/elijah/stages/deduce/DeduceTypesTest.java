package tripleo.elijah.stages.deduce;

import org.junit.Assert;

import org.junit.Test;

import tripleo.elijah.lang.NumericExpression;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang2.BuiltInTypes;

public class DeduceTypesTest {

	@Test
	public void testDeduce() {
		DeduceTypes d = new DeduceTypes(null);
		OS_Type x = d.deduceExpression(new NumericExpression(3), null);
		System.out.println(x);
		Assert.assertEquals(x.getBType(), new OS_Type(BuiltInTypes.SystemInteger).getBType());
	}

}
