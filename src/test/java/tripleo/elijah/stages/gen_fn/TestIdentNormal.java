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
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.FoundElement;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.VariableTableType;

import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * Created 3/4/21 3:53 AM
 */
public class TestIdentNormal {

	@Test(expected = IllegalStateException.class)
	public void test() {
		Compilation comp = new Compilation(new StdErrSink(), new IO());
		OS_Module mod = new OS_Module();//mock(OS_Module.class);
		mod.setParent(comp);
		FunctionDef fd = mock(FunctionDef.class);
		Context ctx1 = mock(Context.class);
		Context ctx2 = mock(Context.class);

		GenerateFunctions generateFunctions = new GenerateFunctions(new GeneratePhase(), mod);
		GeneratedFunction generatedFunction = new GeneratedFunction(fd);
		VariableSequence seq = new VariableSequence(ctx1);
		VariableStatement vs = new VariableStatement(seq);
		final IdentExpression x = IdentExpression.forString("x");
		vs.setName(x);
		final IdentExpression foo = IdentExpression.forString("foo");
		ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
		@NotNull List<InstructionArgument> l = generatedFunction._getIdentIAPathList(s);
		System.out.println(l);
//      System.out.println(generatedFunction.getIdentIAPathNormal());

		//
		//
		//

		LookupResultList lrl = new LookupResultList();
		lrl.add("x", 1, vs, ctx2);
		expect(ctx2.lookup("x")).andReturn(lrl);

		replay(ctx2);

		//
		//
		//

		IdentIA identIA = new IdentIA(1, generatedFunction);

		final GeneratePhase generatePhase = new GeneratePhase();
		DeducePhase phase = new DeducePhase(generatePhase);
		DeduceTypes2 d2 = new DeduceTypes2(mod, phase);

		final List<InstructionArgument> ss = generatedFunction._getIdentIAPathList(identIA);
		d2.resolveIdentIA2_(ctx2, ss/*identIA*/, generatedFunction, new FoundElement(phase) {
			@Override
			public void foundElement(OS_Element e) {
				System.out.println(e);
			}

			@Override
			public void noFoundElement() {
				int y=2;
			}
		});
	}

	@Test
	public void test2() {
		Compilation comp = new Compilation(new StdErrSink(), new IO());
		OS_Module mod = new OS_Module();//mock(OS_Module.class);
		mod.setParent(comp);
		FunctionDef fd = mock(FunctionDef.class);
		Context ctx1 = mock(Context.class);
		Context ctx2 = mock(Context.class);

		GenerateFunctions generateFunctions = new GenerateFunctions(new GeneratePhase(), mod);
		GeneratedFunction generatedFunction = new GeneratedFunction(fd);

		//
		//
		//

		ClassStatement cs = new ClassStatement(mod, mod.getContext());
		FunctionDef fd2 = new FunctionDef(cs, cs.getContext());
		fd2.setName(IdentExpression.forString("foo"));

		TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(cs));
		generatedFunction.addVariableTableEntry("x", VariableTableType.VAR, tte, cs);

		//
		//
		//

		VariableSequence seq = new VariableSequence(ctx1);
		VariableStatement vs = seq.next();
		final IdentExpression x = IdentExpression.forString("x");
		vs.setName(x);
		final IdentExpression foo = IdentExpression.forString("foo");
		ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
/*
		@NotNull List<InstructionArgument> l = generatedFunction._getIdentIAPathList(s);
		System.out.println(l);
*/
//      System.out.println(generatedFunction.getIdentIAPathNormal());

		//
		//
		//

		vs.initial(pce);

		GeneratedFunction generatedFunction2 = new GeneratedFunction(fd2);
		generatedFunction2.addVariableTableEntry("self", VariableTableType.SELF, null, null);
		final TypeTableEntry type = null;
		int res = generatedFunction2.addVariableTableEntry("Result", VariableTableType.RESULT, type, null);

		//
		//
		//

		replay(fd, ctx1, ctx2);

		//
		//
		//

		IdentIA identIA = new IdentIA(0, generatedFunction);

		final GeneratePhase generatePhase = new GeneratePhase();
		DeducePhase phase = new DeducePhase(generatePhase);
		DeduceTypes2 d2 = new DeduceTypes2(mod, phase);

//		final List<InstructionArgument> ss = generatedFunction._getIdentIAPathList(identIA);
		d2.resolveIdentIA2_(ctx2, /*ss*/identIA, generatedFunction, new FoundElement(phase) {
			@Override
			public void foundElement(OS_Element e) {
				assert e == fd2;
			}

			@Override
			public void noFoundElement() {
				assert false;
			}
		});

		verify(fd, ctx1, ctx2);
	}

}

//
//
//
