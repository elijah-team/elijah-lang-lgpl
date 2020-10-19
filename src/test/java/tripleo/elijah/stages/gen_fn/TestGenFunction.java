/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionName;

import java.io.File;
import java.util.List;

/**
 * Created 9/10/20 2:20 PM
 */
public class TestGenFunction {

	@Test
	public void testDemoElNormalFact1Elijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/demo-el-normal/fact1.elijah";
		final File file = new File(f);
		final OS_Module m = c.realParseElijjahFile(f, file, false);
//		OS_Module m = c.parseElijjahFile(file, f, eee, false);
			m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet
			Assert.assertTrue("Method parsed correctly", m != null);

			final GenerateFunctions gfm = new GenerateFunctions(m);
			final List<GeneratedFunction> lgf = gfm.generateAllTopLevelFunctions();

			for (final GeneratedFunction gf : lgf) {
//			System.err.println("7000 "+gf);

			if (gf.name().equals("main")) {
				Assert.assertEquals(InstructionName.E,    gf.getInstruction(0).getName());
				Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(1).getName());
				Assert.assertEquals(InstructionName.AGN,  gf.getInstruction(2).getName());
				Assert.assertEquals(InstructionName.CALL, gf.getInstruction(3).getName());
				Assert.assertEquals(InstructionName.X,    gf.getInstruction(4).getName());
			} else if (gf.name().equals("factorial")) {
				Assert.assertEquals(InstructionName.E,    gf.getInstruction(0).getName());
				Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(1).getName());
				Assert.assertEquals(InstructionName.ES,   gf.getInstruction(2).getName());
				Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(3).getName());
				Assert.assertEquals(InstructionName.CMP,  gf.getInstruction(4).getName());
				Assert.assertEquals(InstructionName.JE,   gf.getInstruction(5).getName());
				Assert.assertEquals(InstructionName.CALLS, gf.getInstruction(6).getName());
				Assert.assertEquals(InstructionName.CALLS, gf.getInstruction(7).getName());
				Assert.assertEquals(InstructionName.JMP,  gf.getInstruction(8).getName());
				Assert.assertEquals(InstructionName.XS,   gf.getInstruction(9).getName());
				Assert.assertEquals(InstructionName.AGN,  gf.getInstruction(10).getName());
				Assert.assertEquals(InstructionName.CALLS,  gf.getInstruction(11).getName());
				Assert.assertEquals(InstructionName.X,    gf.getInstruction(12).getName());
			}
		}

		new DeduceTypes2(m).deduceFunctions(lgf);
		for (final GeneratedFunction gf : lgf) {
			if (gf.name().equals("main")) {
/*
				Assert.assertEquals(InstructionName.E,    gf.getInstruction(0).getName());
				Assert.assertEquals(InstructionName.AGN,  gf.getInstruction(1).getName());
				Assert.assertEquals(InstructionName.CALL, gf.getInstruction(2).getName());
				Assert.assertEquals(InstructionName.X,    gf.getInstruction(3).getName());
*/
				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8007 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.attached != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.attached.getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.attached.getType());
					}
				}
			} else if (gf.name().equals("factorial")) {
				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8008 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.attached != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.attached.getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.attached.getType());
					}
				}
			}
		}

		Assert.assertEquals(2, c.errorCount());
	}

	@Test
	public void testBasic1GenericElijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/basic1/genericA.elijah";
		final File file = new File(f);
		final OS_Module m = c.realParseElijjahFile(f, file, false);
		Assert.assertTrue("Method parsed correctly", m != null);
//		OS_Module m = c.parseElijjahFile(file, f, eee, false);
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet
		c.findStdLib("c");

		for (final CompilerInstructions ci : c.cis) {
			c.use(ci, false);
		}

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedFunction> lgf = gfm.generateAllTopLevelFunctions();

//		for (GeneratedFunction gf : lgf) {
//			for (Instruction instruction : gf.instructions()) {
//				System.out.println("8100 "+instruction);
//			}
//		}

		new DeduceTypes2(m).deduceFunctions(lgf);

		new GenerateC(m).generateCode(lgf);
	}

	@Test
	public void testBasic1Backlink1Elijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/basic1/backlink1.elijah";
		final File file = new File(f);
		final OS_Module m = c.realParseElijjahFile(f, file, false);
//		OS_Module m = c.parseElijjahFile(file, f, eee, false);
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet
		Assert.assertTrue("Method parsed correctly", m != null);

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedFunction> lgf = gfm.generateAllTopLevelFunctions();

		for (final GeneratedFunction gf : lgf) {
			for (final Instruction instruction : gf.instructions()) {
				System.out.println("8100 "+instruction);
			}
		}

		new DeduceTypes2(m).deduceFunctions(lgf);

		for (final GeneratedFunction gf : lgf) {
			System.out.println("----------------------------------------------------------");
			System.out.println(gf.name());
			System.out.println("----------------------------------------------------------");
			System.out.println("VariableTable " + gf.vte_list);
			System.out.println("ConstantTable " + gf.cte_list);
			System.out.println("ProcTable     " + gf.prte_list);
			System.out.println("TypeTable     " + gf.tte_list);
			System.out.println("IdentTable    " + gf.idte_list);
			System.out.println("----------------------------------------------------------");
		}
	}

}

//
//
//
