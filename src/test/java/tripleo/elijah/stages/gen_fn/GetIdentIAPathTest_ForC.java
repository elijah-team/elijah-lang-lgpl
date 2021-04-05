/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_c.CReference;
import tripleo.elijah.stages.gen_c.Emit;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.util.Helpers;

import static org.easymock.EasyMock.*;

public class GetIdentIAPathTest_ForC {

	GeneratedFunction gf;
	OS_Module mod;

	@Before
	public void setUp() throws Exception {
		mod = mock(OS_Module.class);
		FunctionDef fd = mock(FunctionDef.class);
		gf = new GeneratedFunction(fd);

		Emit.emitting = false;
	}

	@Test
	public void testManualXDotFoo() {
		@NotNull IdentExpression x_ident = IdentExpression.forString("X");
		@NotNull IdentExpression foo_ident = IdentExpression.forString("foo");
		//
		OS_Type type = null;
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte, mock(VariableStatement.class));
		int ite_index = gf.addIdentTableEntry(foo_ident, null);
		IdentTableEntry ite = gf.getIdentTableEntry(ite_index);
		ite.backlink = new IntegerIA(int_index);
		IdentIA ident_ia = new IdentIA(ite_index, gf);
		String x = getIdentIAPath(ident_ia, gf);
		Assert.assertEquals("vvx->vmfoo", x);
	}

	@Test
	public void testManualXDotFoo2() {
		@NotNull IdentExpression x_ident = IdentExpression.forString("x");
		@NotNull IdentExpression foo_ident = IdentExpression.forString("foo");
		//
		GenerateFunctions gen = new GenerateFunctions(mod);
		Context ctx = mock(Context.class);
		//
		DotExpression expr = new DotExpression(x_ident, foo_ident);
		InstructionArgument xx = gen.simplify_expression(expr, gf, ctx);
		//
		IdentIA ident_ia = (IdentIA) xx;
		String x = getIdentIAPath(ident_ia, gf);
//		Assert.assertEquals("vvx->vmfoo", x);  // TODO real expectation, IOW output below is wrong
		// TODO actually compiler should comlain that it can't find x
		Assert.assertEquals("->vmx->vmfoo", x);
	}

	@Test
	public void testManualXDotFoo3() {
		IdentExpression x_ident = Helpers.string_to_ident("x");
		@NotNull IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		GenerateFunctions gen = new GenerateFunctions(mod);
		Context ctx = mock(Context.class);
		//
		OS_Type type = null;
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte, mock(VariableStatement.class));
		//
		DotExpression expr = new DotExpression(x_ident, foo_ident);
		InstructionArgument xx = gen.simplify_expression(expr, gf, ctx);
		//
/*
		int ite_index = gf.addIdentTableEntry(foo_ident);
		IdentTableEntry ite = gf.getIdentTableEntry(ite_index);
		ite.backlink = new IntegerIA(int_index);
*/
		IdentIA ident_ia = (IdentIA) xx;//new IdentIA(ite_index, gf);
		String x = getIdentIAPath(ident_ia, gf);
//		Assert.assertEquals("vvx->vmfoo", x); // TODO real expectation
		Assert.assertEquals("vvx->vmfoo", x);
	}

	@Test
	public void testManualXDotFooWithFooBeingFunction() {
		@NotNull IdentExpression x_ident = Helpers.string_to_ident("x");
		@NotNull IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		Context ctx = mock(Context.class);
		Context mockContext = mock(Context.class);

		LookupResultList lrl = new LookupResultList();
		LookupResultList lrl2 = new LookupResultList();

		expect(mod.pullPackageName()).andReturn(OS_Package.default_package);
//		expect(mod.add(classStatement)); // really want this but cant mock void functions
		mod.add(anyObject(ClassStatement.class));
		replay(mod);

		ClassStatement classStatement = new ClassStatement(mod, ctx);
		classStatement.setName(Helpers.string_to_ident("X")); // README not explicitly necessary

//		expect(mockContext.lookup(foo_ident.getText())).andReturn(lrl2);

//		expect(classStatement.getContext().lookup(foo_ident.getText())).andReturn(lrl2);

		lrl.add(x_ident.getText(), 1, classStatement, ctx);
		expect(ctx.lookup(x_ident.getText())).andReturn(lrl);

		FunctionDef functionDef = new FunctionDef(classStatement, classStatement.getContext());
		functionDef.setName(foo_ident);
		lrl2.add(foo_ident.getText(), 1, functionDef, mockContext);

		//
		// SET UP EXPECTATIONS
		//
		replay(ctx, mockContext);

		LookupResultList lrl_expected = ctx.lookup(x_ident.getText());

		//
		// VERIFY EXPECTATIONS
		//

		//
		final OS_Type type = new OS_Type(classStatement);
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte, mock(VariableStatement.class));
		//
		DotExpression expr = new DotExpression(x_ident, foo_ident);
		//
		GenerateFunctions gen = new GenerateFunctions(mod);
		InstructionArgument xx = gen.simplify_expression(expr, gf, ctx);

		//
		// This is the Deduce portion.
		// Not very extensive is it?
		//
		IdentIA ident_ia = (IdentIA) xx;
		IdentTableEntry ite = ident_ia.getEntry();
		ite.setStatus(BaseTableEntry.Status.KNOWN, functionDef);

		// This assumes we want a function call
		// but what if we want a function pointer or a curry or function reference?
		// IOW, a ProcedureCall is not specified
		String x = getIdentIAPath(ident_ia, gf);

		verify(mod, ctx, mockContext);

		Assert.assertEquals("Z0foo(vvx)", x);
	}

	String getIdentIAPath(final IdentIA ia2, GeneratedFunction generatedFunction) {
		final CReference reference = new CReference();
		reference.getIdentIAPath(ia2, generatedFunction);
		return reference.build();
	}


}

//
//
//
