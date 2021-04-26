/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_fn.BaseTableEntry;
import tripleo.elijah.stages.gen_fn.GenerateFunctions;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 12/30/20 2:14 AM
 */
public class PipelineLogic {
	final DeducePhase dp = new DeducePhase();
	final List<OS_Module> mods = new ArrayList<OS_Module>();
	public GenerateC.GenerateResult gr = new GenerateC.GenerateResult();

	public void everythingBeforeGenerate(List<GeneratedNode> lgc) {
		for (OS_Module mod : mods) {
			run2(mod, lgc);
		}
		dp.finish();
	}

	public void generate(List<GeneratedNode> lgc) {
		GenerateC.GenerateResult ggr = null;
		for (OS_Module mod : mods) {
			try {
				ggr = run3(mod, lgc);
			} catch (IOException e) {
				mod.parent.eee.exception(e);
			}
		}
		gr = ggr;
	}

	public static void debug_buffers(GenerateC.GenerateResult gr, PrintStream stream) {
		for (GenerateC.AssociatedBuffer ab : gr.results()) {
			stream.println("---------------------------------------------------------------");
			stream.println(ab.counter);
			stream.println(ab.ty);
			stream.println(ab.output);
			stream.println(ab.node.identityString());
			stream.println(ab.buffer.getText());
			stream.println("---------------------------------------------------------------");
		}
	}

	protected void run2(OS_Module mod, List<GeneratedNode> lgc) {
		final GenerateFunctions gfm = new GenerateFunctions(mod);
//		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();
//		lgc = new ArrayList<GeneratedNode>();
		gfm.generateAllTopLevelClasses(lgc);

//		for (final GeneratedNode gn : lgc) {
//			if (gn instanceof GeneratedFunction) {
//				GeneratedFunction gf = (GeneratedFunction) gn;
//				for (final Instruction instruction : gf.instructions()) {
//					System.out.println("8100 " + instruction);
//				}
//			}
//		}

		List<GeneratedNode> resolved_nodes = new ArrayList<GeneratedNode>();

		for (final GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				if (generatedFunction.getCode() == 0)
					generatedFunction.setCode(mod.parent.nextFunctionCode());
			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				if (generatedClass.getCode() == 0)
					generatedClass.setCode(mod.parent.nextClassCode());
				for (GeneratedClass generatedClass2 : generatedClass.classMap.values()) {
					generatedClass2.setCode(mod.parent.nextClassCode());
				}
				for (GeneratedFunction generatedFunction : generatedClass.functionMap.values()) {
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode node = identTableEntry.resolved();
							resolved_nodes.add(node);
						}
					}
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				if (generatedNamespace.getCode() == 0)
					generatedNamespace.setCode(mod.parent.nextClassCode());
				for (GeneratedClass generatedClass : generatedNamespace.classMap.values()) {
					generatedClass.setCode(mod.parent.nextClassCode());
				}
				for (GeneratedFunction generatedFunction : generatedNamespace.functionMap.values()) {
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode node = identTableEntry.resolved();
							resolved_nodes.add(node);
						}
					}
				}
			}
		}

		for (final GeneratedNode generatedNode : resolved_nodes) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				if (generatedFunction.getCode() == 0)
					generatedFunction.setCode(mod.parent.nextFunctionCode());
			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				if (generatedClass.getCode() == 0)
					generatedClass.setCode(mod.parent.nextClassCode());
			} else if (generatedNode instanceof GeneratedNamespace) {
				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				if (generatedNamespace.getCode() == 0)
					generatedNamespace.setCode(mod.parent.nextClassCode());
			}
		}

		dp.deduceModule(mod, lgc, true);

		resolveCheck(lgc);

//		for (final GeneratedNode gn : lgf) {
//			if (gn instanceof GeneratedFunction) {
//				GeneratedFunction gf = (GeneratedFunction) gn;
//				System.out.println("----------------------------------------------------------");
//				System.out.println(gf.name());
//				System.out.println("----------------------------------------------------------");
//				GeneratedFunction.printTables(gf);
//				System.out.println("----------------------------------------------------------");
//			}
//		}

	}

	protected GenerateC.GenerateResult run3(OS_Module mod, List<GeneratedNode> lgc) throws IOException {
		GenerateC ggc = new GenerateC(mod);
//		ggc.generateCode(lgf);

		GenerateC.GenerateResult gr = new GenerateC.GenerateResult();

		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode.module() != mod) continue; // README curious
			//
			if (generatedNode instanceof GeneratedClass) {
				GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				ggc.generate_class(generatedClass, gr);
				final @NotNull Collection<GeneratedNode> gn1 = ggc.functions_to_list_of_generated_nodes(generatedClass.functionMap.values());
				GenerateC.GenerateResult gr2 = ggc.generateCode(gn1);
				gr.results().addAll(gr2.results());
				final @NotNull Collection<GeneratedNode> gn2 = ggc.classes_to_list_of_generated_nodes(generatedClass.classMap.values());
				GenerateC.GenerateResult gr3 = ggc.generateCode(gn2);
				gr.results().addAll(gr3.results());
			} else if (generatedNode instanceof GeneratedNamespace) {
				GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				ggc.generate_namespace(generatedNamespace, gr);
				final @NotNull Collection<GeneratedNode> gn3 = ggc.functions_to_list_of_generated_nodes(generatedNamespace.functionMap.values());
				GenerateC.GenerateResult gr3 = ggc.generateCode(gn3);
				gr.results().addAll(gr3.results());
				final @NotNull Collection<GeneratedNode> gn4 = ggc.classes_to_list_of_generated_nodes(generatedNamespace.classMap.values());
				GenerateC.GenerateResult gr4 = ggc.generateCode(gn4);
				gr.results().addAll(gr4.results());
			} else {
				System.out.println("2009 " + generatedNode.getClass().getName());
			}
		}

		return gr;
	}

	public void addModule(OS_Module m) {
		mods.add(m);
	}

	private void resolveCheck(List<GeneratedNode> lgc) {
		for (final GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedFunction) {

			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				for (GeneratedFunction generatedFunction : generatedClass.functionMap.values()) {
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						final IdentIA ia2 = new IdentIA(identTableEntry.getIndex(), generatedFunction);
						final String s = generatedFunction.getIdentIAPathNormal(ia2);
						if (identTableEntry/*.isResolved()*/.getStatus() == BaseTableEntry.Status.KNOWN) {
//							GeneratedNode node = identTableEntry.resolved();
//							resolved_nodes.add(node);
							System.out.println("91 Resolved IDENT "+ s);
						} else {
//							assert identTableEntry.getStatus() == BaseTableEntry.Status.UNKNOWN;
//							identTableEntry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
							System.out.println("92 Unresolved IDENT "+ s);
						}
					}
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
//				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
//				NamespaceStatement namespaceStatement = generatedNamespace.getNamespaceStatement();
//				for (GeneratedFunction generatedFunction : generatedNamespace.functionMap.values()) {
//					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
//						if (identTableEntry.isResolved()) {
//							GeneratedNode node = identTableEntry.resolved();
//							resolved_nodes.add(node);
//						}
//					}
//				}
			}
		}
	}

}

//
//
//
