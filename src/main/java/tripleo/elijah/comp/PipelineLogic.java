/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.NamespaceStatement;
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
import tripleo.util.buffer.Buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/30/20 2:14 AM
 */
public class PipelineLogic {
	final DeducePhase dp = new DeducePhase();
	final List<OS_Module> mods = new ArrayList<OS_Module>();
	GenerateC.GenerateResult gr;

	public void everythingBeforeGenerate(List<GeneratedNode> lgc) {
		for (OS_Module mod : mods) {
			run2(mod, lgc);
		}
		dp.finish();
	}

	public void generate(List<GeneratedNode> lgc) {
		for (OS_Module mod : mods) {
			try {
				gr = run3(mod, lgc);
			} catch (IOException e) {
				mod.parent.eee.exception(e);
			}
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
				final FunctionDef fd = ((GeneratedFunction) generatedNode).getFD();
				if (fd._a.getCode() == 0)
					fd._a.setCode(mod.parent.nextFunctionCode());
			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				ClassStatement classStatement = generatedClass.getKlass();
				if (classStatement._a.getCode() == 0)
					classStatement._a.setCode(mod.parent.nextClassCode());
				for (GeneratedFunction generatedFunction : generatedClass.functionMap.values()) {
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode node = identTableEntry.resolved();
							resolved_nodes.add(node);
						} else {
							final IdentIA ia2 = new IdentIA(identTableEntry.getIndex(), generatedFunction);
							final String s = generatedFunction.getIdentIAPathNormal(ia2);
//							assert identTableEntry.getStatus() == BaseTableEntry.Status.UNKNOWN;
							identTableEntry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
							System.out.println("92 Unresolved IDENT "+ s);
						}
					}
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				NamespaceStatement namespaceStatement = generatedNamespace.getNamespaceStatement();
				if (namespaceStatement._a.getCode() == 0)
					namespaceStatement._a.setCode(mod.parent.nextClassCode());
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
				final FunctionDef fd = ((GeneratedFunction) generatedNode).getFD();
				if (fd._a.getCode() == 0)
					fd._a.setCode(mod.parent.nextFunctionCode());
			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				ClassStatement classStatement = generatedClass.getKlass();
				if (classStatement._a.getCode() == 0)
					classStatement._a.setCode(mod.parent.nextClassCode());
			} else if (generatedNode instanceof GeneratedNamespace) {
				NamespaceStatement namespaceStatement = ((GeneratedNamespace) generatedNode).getNamespaceStatement();
				if (namespaceStatement._a.getCode() == 0)
					namespaceStatement._a.setCode(mod.parent.nextClassCode());
			}
		}

		dp.deduceModule(mod, lgc, true);

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
			Buffer b;
			if (generatedNode instanceof GeneratedClass) {
				GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				b = ggc.generate_class(generatedClass);
				gr.add(b, generatedClass, ggc.bufferCounter++);
				GenerateC.GenerateResult gr2 = ggc.generateCode2(generatedClass.functionMap.values());
				gr.results().addAll(gr2.results());
			} else if (generatedNode instanceof GeneratedNamespace) {
				GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				b = ggc.generate_namespace(generatedNamespace);
				gr.add(b, generatedNamespace, ggc.bufferCounter++);
				GenerateC.GenerateResult gr2 = ggc.generateCode2(generatedNamespace.functionMap.values());
				gr.results().addAll(gr2.results());
			} else {
				System.out.println("2009 " + generatedNode.getClass().getName());
			}
		}

//		System.out.println("167 "+gr);
		for (GenerateC.AssociatedBuffer ab : gr.results()) {
			System.out.println("---------------------------------------------------------------");
			System.out.println(ab.counter);
			if (ab.node instanceof GeneratedClass) {
				System.out.println(((GeneratedClass)ab.node).getKlass());
			} else if (ab.node instanceof GeneratedNamespace) {
				System.out.println(((GeneratedNamespace)ab.node).getNamespaceStatement());
			} else if (ab.node instanceof GeneratedFunction) {
				System.out.println(((GeneratedFunction)ab.node).getFD());
			}
			System.out.println(ab.buffer.getText());
			System.out.println("---------------------------------------------------------------");
		}

		return gr;
	}

	public void addModule(OS_Module m) {
		mods.add(m);
	}

}

//
//
//
