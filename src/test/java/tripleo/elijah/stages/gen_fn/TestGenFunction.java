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
import tripleo.elijah.stages.logging.ElLog;
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

//	@Test
	@SuppressWarnings("JUnit3StyleTestMethodInJUnit4Class")
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


		final ElLog.Verbosity verbosity1 = c.gitlabCIVerbosity();
		c.pipelineLogic = new PipelineLogic(verbosity1);
		final GeneratePhase generatePhase1 = c.pipelineLogic.generatePhase;//new GeneratePhase();
		final GenerateFunctions gfm = generatePhase1.getGenerateFunctions(m);
		DeducePhase dp = c.pipelineLogic.dp;//new DeducePhase(generatePhase1);
		gfm.generateFromEntryPoints(m.entryPoints, dp);

		final DeducePhase.@NotNull GeneratedClasses lgc = dp.generatedClasses; //new ArrayList<>();

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

		c.addFunctionMapHook(new FunctionMapHook(){
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

		c.addFunctionMapHook(new FunctionMapHook(){
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

		c.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("main") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

				System.out.println("main\n====");
				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8007 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.getAttached() != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.getAttached().getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.getAttached().getType());
					}
				}
				System.out.println();

				ran_hooks.add(this);
			}
		});

		c.addFunctionMapHook(new FunctionMapHook(){
			@Override
			public boolean matches(FunctionDef fd) {
				final boolean b = fd.name().equals("factorial") && fd.getParent() == main_class;
				return b;
			}

			@Override
			public void apply(Collection<GeneratedFunction> aGeneratedFunctions) {
				assert aGeneratedFunctions.size() == 1;

				GeneratedFunction gf = aGeneratedFunctions.iterator().next();

				System.out.println("factorial\n=========");
				for (int i = 0; i < gf.vte_list.size(); i++) {
					final VariableTableEntry vte = gf.getVarTableEntry(i);
					System.out.println(String.format("8008 %s %s %s", vte.getName(), vte.type, vte.potentialTypes()));
					if (vte.type.getAttached() != null) {
						Assert.assertNotEquals(OS_Type.Type.BUILT_IN, vte.type.getAttached().getType());
						Assert.assertNotEquals(OS_Type.Type.USER, vte.type.getAttached().getType());
					}
				}
				System.out.println();

				ran_hooks.add(this);
			}
		});

		dp.deduceModule(m, lgc, c.gitlabCIVerbosity());
		dp.finish();

		Assert.assertEquals("Not all hooks ran", 4, ran_hooks.size());
		Assert.assertEquals(16, c.errorCount());
	}

//	@Test
	@SuppressWarnings("JUnit3StyleTestMethodInJUnit4Class")
	public void testGenericA() throws Exception {
		final StdErrSink errSink = new StdErrSink();
		final Compilation c = new Compilation(errSink, new IO());

		final String f = "test/basic1/genericA/";

		c.feedCmdLine(List_of(f));
	}

//	@Test // ignore because of generateAllTopLevelClasses
	@SuppressWarnings("JUnit3StyleTestMethodInJUnit4Class")
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

		final ElLog.Verbosity verbosity1 = c.gitlabCIVerbosity();
		final PipelineLogic pl = new PipelineLogic(verbosity1);
		final GeneratePhase generatePhase = new GeneratePhase(verbosity1, pl);
		final GenerateFunctions gfm = generatePhase.getGenerateFunctions(m);
		final List<GeneratedNode> lgc = new ArrayList<>();
		gfm.generateAllTopLevelClasses(lgc);

		DeducePhase dp = new DeducePhase(generatePhase, pl, verbosity1);

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

		dp.deduceModule(m, lgc, c.gitlabCIVerbosity());
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

		PipelineLogic pipelineLogic = new PipelineLogic(Compilation.gitlabCIVerbosity());
		GenerateC ggc = new GenerateC(m, eee, c.gitlabCIVerbosity(), pipelineLogic);
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

		final String ff = "test/basic1/backlink3/";
		c.feedCmdLine(List_of(ff));
	}
}

//
//
//
