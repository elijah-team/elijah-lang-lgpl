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
import java.io.IOException;
import java.util.ArrayList;
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
		Assert.assertTrue("Method parsed correctly", m != null);
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();

		for (final GeneratedNode gn : lgf) {
			if (gn instanceof GeneratedFunction) {
				GeneratedFunction gf = (GeneratedFunction) gn;
//				System.err.println("7000 "+gf);

				if (gf.name().equals("main")) {
					int pc=0;
					Assert.assertEquals(InstructionName.E,    gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGNK, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.DECL,  gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.AGN,  gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.CALL, gf.getInstruction(pc++).getName());
					Assert.assertEquals(InstructionName.X,    gf.getInstruction(pc++).getName());
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

		new DeduceTypes2(m).deduceFunctions(lgf);

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

		final GenerateFunctions gfm = new GenerateFunctions(m);
		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();

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

		new DeduceTypes2(m).deduceFunctions(lgf);

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

		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedClass) {
				ggc.generate_class((GeneratedClass) generatedNode);
			} else {
				System.out.println(lgc.getClass().getName());
			}
		}
	}

	interface Runnable1 {

		void set(OS_Module m);

		void run();
	}

	@Test
	public void testBasic1Backlink3Elijah() throws Exception {
		final StdErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		final String f = "test/basic1/backlink3.elijah";
		final File file = new File(f);
		final OS_Module m = c.realParseElijjahFile(f, file, false);
		Assert.assertEquals("Method parsed correctly", 0, c.errorCount());
		m.prelude = c.findPrelude("c"); // TODO we dont know which prelude to find yet

		c.findStdLib("c");

		for (final CompilerInstructions ci : c.cis) {
			c.use(ci, false);
		}

		Runnable1 runnable = new Runnable1() {
			@Override
			public void run() {
				try {
					run2();
				} catch (IOException e) {
					mod.parent.eee.exception(e);
				}
			}

			private OS_Module mod;

			@Override
			public void set(OS_Module mm) {
				mod = mm;
			}

			public void run2() throws IOException {
				final GenerateFunctions gfm = new GenerateFunctions(mod);
//				final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();
				final List<GeneratedNode> lgc = gfm.generateAllTopLevelClasses();

				final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
				for (GeneratedNode lgci : lgc) {
					if (lgci instanceof GeneratedClass) {
						lgf.addAll(((GeneratedClass) lgci).functionMap.values());
					}
				}

//				for (final GeneratedNode gn : lgc) {
//					if (gn instanceof GeneratedFunction) {
//						GeneratedFunction gf = (GeneratedFunction) gn;
//						for (final Instruction instruction : gf.instructions()) {
//							System.out.println("8100 " + instruction);
//						}
//					}
//				}

				new DeduceTypes2(mod).deduceFunctions(lgf);
//				Collection<GeneratedNode> classes = Collections2.filter(lgc, new Predicate<GeneratedNode>() {
//					@Override
//					public boolean apply(@Nullable GeneratedNode input) {
//						return input instanceof GeneratedClass;
//					}
//				});
//				new DeduceTypes2(mod).deduceFunctions(new Iterable<GeneratedNode>() {
//					@NotNull
//					@Override
//					public Iterator<GeneratedNode> iterator() {
//						return new Iterator<GeneratedNode>() {
//
//							Iterator<GeneratedClass> ci = (Iterator<GeneratedClass>)classes.iterator();
//
//							@Override
//							public boolean hasNext() {
//								return ci.hasNext();
//							}
//
//							@Override
//							public GeneratedFunction next() {
//								return ci.next().;
//							}
//						};
//					}
//				}));

				for (final GeneratedNode gn : lgf) {
					if (gn instanceof GeneratedFunction) {
						GeneratedFunction gf = (GeneratedFunction) gn;
						System.out.println("----------------------------------------------------------");
						System.out.println(gf.name());
						System.out.println("----------------------------------------------------------");
//						GeneratedFunction.printTables(gf);
//						System.out.println("----------------------------------------------------------");
					}
				}

				GenerateC ggc = new GenerateC(mod);
//				ggc.generateCode(lgf);

				for (GeneratedNode generatedNode : lgc) {
					if (generatedNode instanceof GeneratedClass) {
						GeneratedClass generatedClass = (GeneratedClass) generatedNode;
						ggc.generate_class(generatedClass);
						ggc.generateCode2(generatedClass.functionMap.values());
					} else if (generatedNode instanceof GeneratedNamespace) {
						GeneratedNamespace generatedClass = (GeneratedNamespace) generatedNode;
						ggc.generate_namespace(generatedClass);
						ggc.generateCode2(generatedClass.functionMap.values());
					} else {
						System.out.println("2009 "+generatedNode.getClass().getName());
					}
				}
			}
		};

		runnable.set(m);
		runnable.run();
		runnable.set(m.prelude);
		runnable.run();
	}

}

//
//
//
