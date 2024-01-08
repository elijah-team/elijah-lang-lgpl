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
import tripleo.elijah.entrypoints.EntryPoint;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.work.WorkManager;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created 12/30/20 2:14 AM
 */
public class PipelineLogic {
	public final GeneratePhase generatePhase;
	public final DeducePhase dp;

	public GenerateResult gr = new GenerateResult();
	public List<ElLog> elLogs = new LinkedList<ElLog>();
	public boolean verbose = true;

	private final ElLog.Verbosity verbosity;

	final List<OS_Module> mods = new ArrayList<OS_Module>();

	public PipelineLogic(ElLog.Verbosity aVerbosity) {
		verbosity = aVerbosity;
		generatePhase = new GeneratePhase(aVerbosity, this);
		dp = new DeducePhase(generatePhase, this, verbosity);
	}

	public void everythingBeforeGenerate(List<EvaNode> lgc) {
		for (OS_Module mod : mods) {
			run2(mod, mod.entryPoints);
		}
//		List<List<EntryPoint>> entryPoints = mods.stream().map(mod -> mod.entryPoints).collect(Collectors.toList());
		dp.finish();

		dp.EvaClasses.addAll(lgc);

//		elLogs = dp.deduceLogs;
	}

	public void generate(List<EvaNode> lgc) {
		final WorkManager wm = new WorkManager();
		// README use any errSink, they should all be the same
		for (OS_Module mod : mods) {
			final GenerateC generateC = new GenerateC(mod, mod.parent.getErrSink(), verbosity, this);
			final GenerateResult ggr = run3(mod, lgc, wm, generateC);
			wm.drain();
			gr.results().addAll(ggr.results());
		}
	}

	public static void debug_buffers(GenerateResult gr, PrintStream stream) {
		for (GenerateResultItem ab : gr.results()) {
			stream.println("---------------------------------------------------------------");
			stream.println(ab.counter);
			stream.println(ab.ty);
			stream.println(ab.output);
			stream.println(ab.node.identityString());
			stream.println(ab.buffer.getText());
			stream.println("---------------------------------------------------------------");
		}
	}

	protected void run2(OS_Module mod, @NotNull List<EntryPoint> epl) {
		final GenerateFunctions gfm = getGenerateFunctions(mod);
		gfm.generateFromEntryPoints(epl, dp);

//		WorkManager wm = new WorkManager();
//		WorkList wl = new WorkList();

		DeducePhase.@NotNull EvaClasses lgc = dp.EvaClasses;
		List<EvaNode> resolved_nodes = new ArrayList<EvaNode>();

		for (final EvaNode EvaNode : lgc) {
			if (EvaNode instanceof GNCoded) {
				final GNCoded coded = (GNCoded) EvaNode;

				switch (coded.getRole()) {
				case FUNCTION: {
//					GeneratedFunction generatedFunction = (GeneratedFunction) EvaNode;
					if (coded.getCode() == 0)
						coded.setCode(mod.parent.nextFunctionCode());
					break;
				}
				case CLASS: {
					final EvaClass EvaClass = (EvaClass) EvaNode;
//					if (EvaClass.getCode() == 0)
//						EvaClass.setCode(mod.parent.nextClassCode());
					for (EvaClass EvaClass2 : EvaClass.classMap.values()) {
						if (EvaClass2.getCode() == 0)
							EvaClass2.setCode(mod.parent.nextClassCode());
					}
					for (GeneratedFunction generatedFunction : EvaClass.functionMap.values()) {
						for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
							if (identTableEntry.isResolved()) {
								EvaNode node = identTableEntry.resolvedType();
								resolved_nodes.add(node);
							}
						}
					}
					break;
				}
				case NAMESPACE:
				{
					final EvaNamespace EvaNamespace = (EvaNamespace) EvaNode;
					if (coded.getCode() == 0)
						coded.setCode(mod.parent.nextClassCode());
					for (EvaClass EvaClass : EvaNamespace.classMap.values()) {
						if (EvaClass.getCode() == 0)
							EvaClass.setCode(mod.parent.nextClassCode());
					}
					for (GeneratedFunction generatedFunction : EvaNamespace.functionMap.values()) {
						for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
							if (identTableEntry.isResolved()) {
								EvaNode node = identTableEntry.resolvedType();
								resolved_nodes.add(node);
							}
						}
					}
					break;
				}
				default:
					throw new IllegalStateException("Unexpected value: " + coded.getRole());
				}

			} else {
				throw new IllegalStateException("node must be coded");
			}
		}

		for (final EvaNode EvaNode : resolved_nodes) {
			if (EvaNode instanceof GNCoded) {
				final GNCoded coded = (GNCoded) EvaNode;
				final int code;
				if (coded.getCode() == 0) {
					switch (coded.getRole()) {
					case FUNCTION:
						code = (mod.parent.nextFunctionCode());
						break;
					case NAMESPACE:
					case CLASS:
						code = mod.parent.nextClassCode();
						break;
					default:
						throw new IllegalStateException("Invalid coded role");
					}
					coded.setCode(code);
				}
			} else
				throw new IllegalStateException("node is not coded");
		}

		dp.deduceModule(mod, lgc, getVerbosity());

		resolveCheck(lgc);

//		for (final EvaNode gn : lgf) {
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

	@NotNull
	private GenerateFunctions getGenerateFunctions(OS_Module mod) {
		return generatePhase.getGenerateFunctions(mod);
	}

	protected GenerateResult run3(OS_Module mod, List<EvaNode> lgc, WorkManager wm, GenerateC ggc) {
		GenerateResult gr = new GenerateResult();

		for (EvaNode EvaNode : lgc) {
			if (EvaNode.module() != mod) continue; // README curious

			if (EvaNode instanceof GeneratedContainerNC) {
				final GeneratedContainerNC nc = (GeneratedContainerNC) EvaNode;

				nc.generateCode(ggc, gr);
				if (nc instanceof EvaClass) {
					final EvaClass EvaClass = (EvaClass) nc;

					final @NotNull Collection<EvaNode> gn2 = ggc.constructors_to_list_of_generated_nodes(EvaClass.constructors.values());
					GenerateResult gr3 = ggc.generateCode(gn2, wm);
					gr.results().addAll(gr3.results());
				}
				final @NotNull Collection<EvaNode> gn1 = ggc.functions_to_list_of_generated_nodes(nc.functionMap.values());
				GenerateResult gr2 = ggc.generateCode(gn1, wm);
				gr.results().addAll(gr2.results());
				final @NotNull Collection<EvaNode> gn2 = ggc.classes_to_list_of_generated_nodes(nc.classMap.values());
				GenerateResult gr3 = ggc.generateCode(gn2, wm);
				gr.results().addAll(gr3.results());
			} else {
				System.out.println("2009 " + EvaNode.getClass().getName());
			}
		}

		return gr;
	}

	public void addModule(OS_Module m) {
		mods.add(m);
	}

	private void resolveCheck(DeducePhase.EvaClasses lgc) {
		for (final EvaNode EvaNode : lgc) {
			if (EvaNode instanceof GeneratedFunction) {

			} else if (EvaNode instanceof EvaClass) {
//				final EvaClass EvaClass = (EvaClass) EvaNode;
//				for (GeneratedFunction generatedFunction : EvaClass.functionMap.values()) {
//					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
//						final IdentIA ia2 = new IdentIA(identTableEntry.getIndex(), generatedFunction);
//						final String s = generatedFunction.getIdentIAPathNormal(ia2);
//						if (identTableEntry/*.isResolved()*/.getStatus() == BaseTableEntry.Status.KNOWN) {
////							EvaNode node = identTableEntry.resolved();
////							resolved_nodes.add(node);
//							System.out.println("91 Resolved IDENT "+ s);
//						} else {
////							assert identTableEntry.getStatus() == BaseTableEntry.Status.UNKNOWN;
////							identTableEntry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
//							System.out.println("92 Unresolved IDENT "+ s);
//						}
//					}
//				}
			} else if (EvaNode instanceof EvaNamespace) {
//				final EvaNamespace EvaNamespace = (EvaNamespace) EvaNode;
//				NamespaceStatement namespaceStatement = EvaNamespace.getNamespaceStatement();
//				for (GeneratedFunction generatedFunction : EvaNamespace.functionMap.values()) {
//					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
//						if (identTableEntry.isResolved()) {
//							EvaNode node = identTableEntry.resolved();
//							resolved_nodes.add(node);
//						}
//					}
//				}
			}
		}
	}

	public ElLog.Verbosity getVerbosity() {
		return verbose ? ElLog.Verbosity.VERBOSE : ElLog.Verbosity.SILENT;
	}

	public void addLog(ElLog aLog) {
		elLogs.add(aLog);
	}

}

//
//
//
