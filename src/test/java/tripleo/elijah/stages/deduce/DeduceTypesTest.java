/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.Helpers;

/**
 * Useless tests. We really want to know if a TypeName will resolve to the same types
 */
public class DeduceTypesTest {

	private OS_Type x;

	@Before
	public void setUp() throws ResolveError {
		final OS_Module mod = new OS_Module();
		mod.parent = new Compilation(new StdErrSink(), new IO());
		final ModuleContext mctx = new ModuleContext(mod);
		mod.setContext(mctx);
		final ClassStatement cs = new ClassStatement(mod, mctx);
		cs.setName(Helpers.string_to_ident("Test"));
		final FunctionDef fd = cs.funcDef();
		fd.setName(Helpers.string_to_ident("test"));
		Scope3 scope3 = new Scope3(fd);
		final VariableSequence vss = scope3.statementClosure().varSeq(fd.getContext());
		final VariableStatement vs = vss.next();
		final IdentExpression x = Helpers.string_to_ident("x");
		x.setContext(fd.getContext());
		vs.setName(x);
		final Qualident qu = new Qualident();
		qu.append(Helpers.string_to_ident("Integer"));
		((NormalTypeName)vs.typeName()).setName(qu);
		((NormalTypeName)vs.typeName()).setContext(fd.getContext());
		fd.scope(scope3);
		fd.postConstruct();
		cs.postConstruct();
		mod.postConstruct();
		final FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		final IdentExpression x1 = Helpers.string_to_ident("x");
		x1.setContext(fc);
		//
		//
		//
		DeducePhase dp = new DeducePhase();
		DeduceTypes2 d = dp.deduceModule(mod);
//		final DeduceTypes d = new DeduceTypes(mod);
		this.x = DeduceLookupUtils.deduceExpression(x1, fc);
		System.out.println(this.x);
	}
	/** TODO This test fails beacause we are comparing a BUILT_IN vs a USER OS_Type.
	 *   It fails because Integer is an interface and not a BUILT_IN
	 */
//	@Test
//	public void testDeduceIdentExpression1() {
//		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
//	}
	/**
	 * Now comparing {@link RegularTypeName} to {@link VariableTypeName} works
	 */
	@Test
	public void testDeduceIdentExpression2() {
		final RegularTypeName tn = new RegularTypeName();
		final Qualident tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn), x/*.getTypeName()*/);
	}
	@Test
	public void testDeduceIdentExpression3() {
		final VariableTypeName tn = new VariableTypeName();
		final Qualident tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
		Assert.assertEquals(new OS_Type(tn), x);
	}
	@Test
	public void testDeduceIdentExpression4() {
		final VariableTypeName tn = new VariableTypeName();
		final Qualident tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
		Assert.assertEquals(new OS_Type(tn), x);
		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

}

//
//
//
