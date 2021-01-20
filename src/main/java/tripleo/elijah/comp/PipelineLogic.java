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
import tripleo.elijah.stages.gen_fn.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/30/20 2:14 AM
 */
public class PipelineLogic {
	final DeducePhase dp = new DeducePhase();
	final List<OS_Module> mods = new ArrayList<OS_Module>();

	List<GeneratedNode> lgc = null;

	public List<GeneratedNode> __nodes() {
		return lgc;
	}

	public void everythingBeforeGenerate() {
		for (OS_Module mod : mods) {
			run2(mod);
			dp.finish();
		}
	}

	public void generate() {
		for (OS_Module mod : mods) {
			try {
				run3(mod);
			} catch (IOException e) {
				mod.parent.eee.exception(e);
			}
		}
	}

	protected void run2(OS_Module mod) {
		final GenerateFunctions gfm = new GenerateFunctions(mod);
//		final List<GeneratedNode> lgf = gfm.generateAllTopLevelFunctions();
		lgc = gfm.generateAllTopLevelClasses();

		final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
		for (GeneratedNode lgci : lgc) {
			if (lgci instanceof GeneratedClass) {
				lgf.addAll(((GeneratedClass) lgci).functionMap.values());
			}
		}

//		for (final GeneratedNode gn : lgc) {
//			if (gn instanceof GeneratedFunction) {
//				GeneratedFunction gf = (GeneratedFunction) gn;
//				for (final Instruction instruction : gf.instructions()) {
//					System.out.println("8100 " + instruction);
//				}
//			}
//		}

		List<GeneratedNode> resolved_nodes = new ArrayList<>();

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

		dp.deduceModule(mod, lgf);
//		new DeduceTypes2(mod).deduceFunctions(lgf);
//
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

	protected void run3(OS_Module mod) throws IOException {
		GenerateC ggc = new GenerateC(mod);
//		ggc.generateCode(lgf);

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
				System.out.println("2009 " + generatedNode.getClass().getName());
			}
		}
	}

	public void addModule(OS_Module m) {
		mods.add(m);
	}

}

//
//
//
