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
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;

public class DeduceTypesTest2 {

	@Test
	public void testDeduceIdentExpression() {
		OS_Module mod = new OS_Module();
		mod.parent = new Compilation(new StdErrSink(), new IO());
		DeduceTypes d = new DeduceTypes(mod);
		final ModuleContext mctx = new ModuleContext(mod);
		mod.setContext(mctx);
		ClassStatement cs = new ClassStatement(mod);
		cs.setName(new IdentExpression(Helpers.makeToken("Test")));
		final ClassContext cctx = new ClassContext(mctx, cs);
		cs.setContext(cctx);
		final FunctionDef fd = cs.funcDef();
		fd.setName(new IdentExpression(Helpers.makeToken("test")));
		final FunctionContext fctx = new FunctionContext(cctx, fd);
		fd.setContext(fctx);
		VariableSequence vss = fd.scope().statementClosure().varSeq(fctx);
		final VariableStatement vs = vss.next();
		vs.setName(Helpers.makeToken("x"));
		final Qualident qu = new Qualident();
		qu.append(Helpers.makeToken("Integer"));
		vs.typeName().setName(qu);
		FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		OS_Type x = d.deduceExpression(new IdentExpression(Helpers.makeToken("x")), fc);
		System.out.println(x);
//		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
//		final RegularTypeName tn = new RegularTypeName();
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
		fd.postConstruct();
		cs.postConstruct();
		mod.postConstruct();
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

}
