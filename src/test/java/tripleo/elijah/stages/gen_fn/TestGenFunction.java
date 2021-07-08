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
import tripleo.elijah.entrypoints.MainClassEntryPoint;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.stages.instructions.InstructionName;
import tripleo.elijah.work.WorkManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

		//
		//
		//
		final ClassStatement main_class = (ClassStatement) m.findClass("Main");
		assert main_class != null;
		m.entryPoints = List_of(new MainClassEntryPoint(main_class));
		//
		//
		//

		List<FunctionMapHook> ran_hooks = new ArrayList<>();

		final GeneratePhase generatePhase1 = new GeneratePhase();
		final GenerateFunctions gfm = generatePhase1.getGenerateFunctions(m);
		DeducePhase dp = new DeducePhase(generatePhase1);
		gfm.generateFromEntryPoints(m.entryPoints, dp);

		final List<GeneratedNode> lgc = dp.generatedClasses; //new ArrayList<>();

/*
		List<GeneratedNode> lgf = new ArrayList<>();
		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedClass)
				lgf.addAll(((GeneratedClass) generatedNode).functionMap.values());
			if (generatedNode instanceof GeneratedNamespace)
				lgf.addAll(((GeneratedNamespace) generatedNode).functionMap.values());
			// TODO enum
		}
*/

//		Assert.assertEquals(2, lgf.size());

		WorkManager wm = new WorkManager();

		dp.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("main") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

				int pc = 0;
				Assert.assertEquals(InstructionName.E, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.AGN, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.CALL, gf.getInstruction(pc++).getName());
				Assert.assertEquals(InstructionName.X, gf.getInstruction(pc++).getName());

				ran_hooks.add(this);
			}
		});

		dp.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("factorial") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

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

				ran_hooks.add(this);
			}
		});

		dp.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("main") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8007 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.getAttached() != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.getAttached().getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.getAttached().getType());
					}
				}

				ran_hooks.add(this);
			}
		});

		dp.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("factorial") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8008 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.getAttached() != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.getAttached().getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.getAttached().getType());
					}
				}

				ran_hooks.add(this);
			}
		});

		dp.deduceModule(m, lgc, false);
		dp.finish();

		Assert.assertEquals("Not all hooks ran", 4, ran_hooks.size());
		Assert.assertEquals(8, c.errorCount());
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
			final GenerateFunctions gfm = new GenerateFunctions(new GeneratePhase(), m);
			final List<GeneratedNode> lgc = new ArrayList<>();
			gfm.generateAllTopLevelClasses(lgc);

			List<GeneratedNode> lgf = new ArrayList<>();
			for (GeneratedNode generatedNode : lgc) {
				if (generatedNode instanceof GeneratedClass)
					lgf.addAll(((GeneratedClass) generatedNode).functionMap.values());
				if (generatedNode instanceof GeneratedNamespace)
					lgf.addAll(((GeneratedNamespace) generatedNode).functionMap.values());
				// TODO enum
			}

//			for (GeneratedFunction gf : lgf) {
//				for (Instruction instruction : gf.instructions()) {
//					System.out.println("8100 "+instruction);
//				}
//			}

			final GeneratePhase generatePhase = new GeneratePhase();
			DeducePhase dp = new DeducePhase(generatePhase);
			dp.deduceModule(m, lgc, false);
			dp.finish();

			WorkManager wm = new WorkManager();
			new GenerateC(/*m*/eee).generateCode(lgf, wm);
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

//	@Test // ignore because of generateAllTopLevelClasses
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

		final GenerateFunctions gfm = new GenerateFunctions(new GeneratePhase(), m);
		final List<GeneratedNode> lgc = new ArrayList<>();
		gfm.generateAllTopLevelClasses(lgc);

		final GeneratePhase generatePhase = new GeneratePhase();
		DeducePhase dp = new DeducePhase(generatePhase);

		WorkManager wm = new WorkManager();

		List<GeneratedNode> lgf = new ArrayList<>();
		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedClass)
				lgf.addAll(((GeneratedClass) generatedNode).functionMap.values());
			if (generatedNode instanceof GeneratedNamespace)
				lgf.addAll(((GeneratedNamespace) generatedNode).functionMap.values());
			// TODO enum
		}

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;
				for (final Instruction instruction : gf.instructions()) {
					System.out.println("8100 " + instruction);
				}
			}
		}

		dp.deduceModule(m, lgc, false);
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

		GenerateC ggc = new GenerateC(/*m*/eee);
		ggc.generateCode(lgf, wm);

		GenerateResult gr = new GenerateResult();

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
