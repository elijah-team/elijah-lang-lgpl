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
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.util.Helpers;

import static org.easymock.EasyMock.*;

public class GetIdentIAPathTest {

	GeneratedFunction gf;
	OS_Module mod;

	@Before
	public void setUp() throws Exception {
		mod = mock(OS_Module.class);
		FunctionDef fd = mock(FunctionDef.class);
		gf = new GeneratedFunction(fd);
	}

	@Test
	public void testManualXDotFoo() {
		IdentExpression x_ident = Helpers.string_to_ident("x");
		@NotNull IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		OS_Type type = null;
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte);
		int ite_index = gf.addIdentTableEntry(foo_ident);
		IdentTableEntry ite = gf.getIdentTableEntry(ite_index);
		ite.backlink = new IntegerIA(int_index);
		IdentIA ident_ia = new IdentIA(ite_index, gf);
		String x = gf.getIdentIAPath(ident_ia);
//		Assert.assertEquals("vvx->vmfoo", x);  // TODO real expectation
		Assert.assertEquals("vvx.vmfoo", x);
	}

	@Test
	public void testManualXDotFoo2() {
		IdentExpression x_ident = Helpers.string_to_ident("x");
		@NotNull IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		GenerateFunctions gen = new GenerateFunctions(mod);
		Context ctx = mock(Context.class);
		//
		DotExpression expr = new DotExpression(x_ident, foo_ident);
		InstructionArgument xx = gen.simplify_expression(expr, gf, ctx);
		//
/*
		OS_Type type = null;
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte);
		int ite_index = gf.addIdentTableEntry(foo_ident);
		IdentTableEntry ite = gf.getIdentTableEntry(ite_index);
		ite.backlink = new IntegerIA(int_index);
*/
		//
		IdentIA ident_ia = (IdentIA) xx;//new IdentIA(ite_index, gf);
		String x = gf.getIdentIAPath(ident_ia);
//		Assert.assertEquals("vvx->vmfoo", x);
		// TODO actually compiler should comlain that it can't find x
		Assert.assertEquals("x.vmfoo", x);
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
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte);
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
		String x = gf.getIdentIAPath(ident_ia);
//		Assert.assertEquals("vvx->vmfoo", x); // TODO real expectation
		Assert.assertEquals("vvx.vmfoo", x);
	}

	@Test
	public void testManualXDotFooWithFooBeingFunction() {
		IdentExpression x_ident = Helpers.string_to_ident("x");
		@NotNull IdentExpression foo_ident = Helpers.string_to_ident("foo");
		//
		GenerateFunctions gen = new GenerateFunctions(mod);
		Context ctx = mock(Context.class);
		Context mockContext = mock(Context.class);

		LookupResultList lrl = new LookupResultList();
		LookupResultList lrl2 = new LookupResultList();

		expect(mod.pullPackageName()).andReturn(OS_Package.default_package);
		mod.add(anyObject(ClassStatement.class));
		replay(mod);

//		expect(mockContext.lookup(foo_ident.getText())).andReturn(lrl2);

		ClassStatement classStatement = new ClassStatement(mod, ctx);

//		expect(classStatement.getContext().lookup(foo_ident.getText())).andReturn(lrl2);
//		expect(classStatement.getContext().lookup(foo_ident.getText(), anyInt(), anyObject(LookupResultList.class), anyObject(List.class), anyBoolean()));//.andReturn(lrl2);
//		expect(classStatement.getContext().getParent().lookup(eq(foo_ident.getText()), anyInt(), anyObject(LookupResultList.class), anyObject(List.class), anyBoolean())).andReturn(new LookupResultList());

		lrl.add(x_ident.getText(), 1, classStatement, ctx);
		expect(ctx.lookup(x_ident.getText())).andReturn(lrl);
		FunctionDef functionDef = new FunctionDef(classStatement, classStatement.getContext());
		functionDef.setName(foo_ident);
		lrl2.add(foo_ident.getText(), 1, functionDef, mockContext);

		//
		// SET UP EXPECTATIONS
		//
		replay(ctx);
		replay(mockContext);

		LookupResultList lrl_expected = ctx.lookup(x_ident.getText());

		//
		// VERIFY EXPECTATIONS
		//

		//
		OS_Type type = null;
		TypeTableEntry tte = gf.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_Type(classStatement), x_ident);
		int int_index = gf.addVariableTableEntry("x", VariableTableType.VAR, tte);
		//
		DotExpression expr = new DotExpression(x_ident, foo_ident);
		InstructionArgument xx = gen.simplify_expression(expr, gf, ctx);
		//
		IdentIA ident_ia = (IdentIA) xx;
		IdentTableEntry ite = gf.getIdentTableEntry(ident_ia.getIndex());
//		System.out.println(ite.backlink);
//		((IntegerIA)ite.backlink).setResolvedElement();
		ite.setResolvedElement(functionDef);
		String x = gf.getIdentIAPath(ident_ia);
//		Assert.assertEquals("vvx->vmfoo", x); // TODO real expectation
		verify(mod);
		verify(ctx);
		verify(mockContext);
		Assert.assertEquals("Z0foo(vvx)", x);
	}


}