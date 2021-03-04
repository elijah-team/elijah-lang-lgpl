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
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

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
		Assert.assertTrue("Method parsed correctly", m != null);
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;
//				System.err.println("7000 "+gf);

				if (gf.name().equals("main")) {
					int pc = 0;
					Assert.assertEquals(InstructionName.E, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGN, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.CALL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.X, gf.getInstruction(pc++).getName());
				} else if (gf.name().equals("factorial")) {
					int pc = 0;
					Assert.assertEquals(InstructionName.E, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.ES, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.JE, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.CALLS, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.CALLS, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.JMP, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.XS, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGN, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.X, gf.getInstruction(pc++).getName());
				}
			}
		}

		DeducePhase dp = new DeducePhase();
		dp.deduceModule(m, lgf);
		dp.finish();
//		new DeduceTypes2(m).deduceFunctions(lgf);

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;

				if (gf.name().equals("main")) {
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
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet
		c.findStdLib("c");

		for (final CompilerInstructions ci : c.cis) {
			c.use(ci, false);
		}

		if (false) {
			final GenerateFunctions gfm = new GenerateFunctions(m);
			final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();

//			for (GeneratedFunction gf : lgf) {
//				for (Instruction instruction : gf.instructions()) {
//					System.out.println("8100 "+instruction);
//				}
//			}

			DeducePhase dp = new DeducePhase();
			dp.deduceModule(m, lgf);
			dp.finish();

			new GenerateC(m).generateCode(lgf);
		} else {
			PipelineLogic pipelineLogic = new PipelineLogic();
			ArrayList<GeneratedNode> lgc = new ArrayList<GeneratedNode>();
/*
			pipelineLogic.addModule(m.prelude);
			pipelineLogic.addModule(m);
*/
			for (OS_Module module : c.modules) {
				pipelineLogic.addModule(module);
			}
			pipelineLogic.everythingBeforeGenerate(lgc);
			pipelineLogic.generate(lgc);
		}
	}

	@Test
	public void testBasic1Backlink1Elijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/basic1/backlink1.elijah";
		final File file = new File(f);
		final OS_Module m = c.realParseElijjahFile(f, file, false);
		Assert.assertTrue("Method parsed correctly", m != null);
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet

		c.findStdLib("c");

		for (final CompilerInstructions ci : c.cis) {
			c.use(ci, false);
		}

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();
		final List<GeneratedNode> lgc = gfm.generateAllTopLevelClasses();

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;
				for (final Instruction instruction : gf.instructions()) {
					System.out.println("8100 " + instruction);
				}
			}
		}

		DeducePhase dp = new DeducePhase();
		dp.deduceModule(m, lgf);
		dp.finish();
//		new DeduceTypes2(m).deduceFunctions(lgf);

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;
				System.out.println("----------------------------------------------------------");
				System.out.println(gf.name());
				System.out.println("----------------------------------------------------------");
				GeneratedFunction.printTables(gf);
//				System.out.println("----------------------------------------------------------");
			}
		}

		GenerateC ggc = new GenerateC(m);
		ggc.generateCode(lgf);

		GenerateC.GenerateResult gr = new GenerateC.GenerateResult();

		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedClass) {
				ggc.generate_class((GeneratedClass) generatedNode, gr);
			} else {
				System.out.println(lgc.getClass().getName());
			}
		}
	}

	@Test
	public void testBasic1Backlink3Elijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/basic1/backlink3/backlink3.elijah";
		if (false) {
			final File file = new File(f);
			final OS_Module m = c.realParseElijjahFile(f, file, false);
			Assert.assertEquals("Method parsed correctly", 0, c.errorCount());
			m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet

			c.findStdLib("c");

			for (final CompilerInstructions ci : c.cis) {
				c.use(ci, false);
			}

			PipelineLogic pipelineLogic = new PipelineLogic();
			ArrayList<GeneratedNode> lgc = new ArrayList<GeneratedNode>();

			for (OS_Module module : c.modules) {
				pipelineLogic.addModule(module);
			}

			pipelineLogic.everythingBeforeGenerate(lgc);
			pipelineLogic.generate(lgc);
		} else {
			final String ff = "test/basic1/backlink3/";
			c.feedCmdLine(List_of(ff));
		}
	}
}

//
//
//
