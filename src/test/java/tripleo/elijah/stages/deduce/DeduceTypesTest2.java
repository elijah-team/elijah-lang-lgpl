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
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.Helpers;

public class DeduceTypesTest2 {

	@Test
	public void testDeduceIdentExpression() {
		final OS_Module mod = new OS_Module();
		mod.parent = new Compilation(new StdErrSink(), new IO());
		final DeduceTypes d = new DeduceTypes(mod);
		final ModuleContext mctx = new ModuleContext(mod);
		mod.setContext(mctx);
		final ClassStatement cs = new ClassStatement(mod);
		cs.setName(new IdentExpression(tripleo.elijah.util.Helpers.makeToken("Test")));
		final ClassContext cctx = new ClassContext(mctx, cs);
		cs.setContext(cctx);
		final FunctionDef fd = cs.funcDef();
		fd.setName(new IdentExpression(Helpers.makeToken("test")));
		final FunctionContext fctx = new FunctionContext(cctx, fd);
		fd.setContext(fctx);
		final VariableSequence vss = fd.scope().statementClosure().varSeq(fctx);
		final VariableStatement vs = vss.next();
		vs.setName(new IdentExpression(tripleo.elijah.util.Helpers.makeToken("x")));
		final Qualident qu = new Qualident();
		qu.append(tripleo.elijah.util.Helpers.makeToken("Integer"));
		((NormalTypeName)vs.typeName()).setName(qu);
		final FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		final IdentExpression x1 = new IdentExpression(tripleo.elijah.util.Helpers.makeToken("x"));
		x1.setContext(fc);
		final OS_Type x = d.deduceExpression(x1, fc);
		System.out.println(x);
//		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
//		final RegularTypeName tn = new RegularTypeName();
		final VariableTypeName tn = new VariableTypeName();
		final Qualident tnq = new Qualident();
		tnq.append(tripleo.elijah.util.Helpers.makeToken("Integer"));
		tn.setName(tnq);
		fd.postConstruct();
		cs.postConstruct();
		mod.postConstruct();
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

}
