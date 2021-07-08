/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.entrypoints.EntryPoint;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.gen_c.GenerateC;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.DefaultBuffer;
import tripleo.util.buffer.TextBuffer;
import tripleo.util.io.CharSink;
import tripleo.util.io.FileCharSink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created 12/30/20 2:14 AM
 */
public class PipelineLogic {
	final GeneratePhase generatePhase = new GeneratePhase();
	final DeducePhase dp = new DeducePhase(generatePhase);

	final List<OS_Module> mods = new ArrayList<OS_Module>();
	public GenerateResult gr = new GenerateResult();

	public void everythingBeforeGenerate(List<GeneratedNode> lgc) {
		for (OS_Module mod : mods) {
			run2(mod, mod.entryPoints);
		}
//		List<List<EntryPoint>> entryPoints = mods.stream().map(mod -> mod.entryPoints).collect(Collectors.toList());
		dp.finish();
		lgc.addAll(dp.generatedClasses);
	}

	public void generate(List<GeneratedNode> lgc) {
		GenerateResult ggr = null;
		final WorkManager wm = new WorkManager();
		// README use any errSink, they should all be the same
		final GenerateC generateC = new GenerateC(mods.get(0).parent.getErrSink());
		for (OS_Module mod : mods) {
			ggr = run3(mod, lgc, wm, generateC);
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

		WorkManager wm = new WorkManager();
		WorkList wl = new WorkList();

//		for (final GeneratedNode gn : lgc) {
//			if (gn instanceof GeneratedFunction) {
//				GeneratedFunction gf = (GeneratedFunction) gn;
//				for (final Instruction instruction : gf.instructions()) {
//					System.out.println("8100 " + instruction);
//				}
//			}
//		}

/*
		for (EntryPoint entryPoint : epl) {
			for (GenType dependentType : entryPoint.dependentTypes()) {
//				dependentType.
				System.err.println(dependentType);
			}
			for (FunctionInvocation dependentFunction : entryPoint.dependentFunctions()) {
				System.err.println(dependentFunction);
			}
		}
*/

		List<GeneratedNode> lgc = dp.generatedClasses; //new ArrayList<GeneratedNode>();
		List<GeneratedNode> resolved_nodes = new ArrayList<GeneratedNode>();

		for (final GeneratedNode generatedNode : lgc) {
			if (generatedNode instanceof GeneratedFunction) {
				GeneratedFunction generatedFunction = (GeneratedFunction) generatedNode;
				if (generatedFunction.getCode() == 0)
					generatedFunction.setCode(mod.parent.nextFunctionCode());
			} else if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
//				if (generatedClass.getCode() == 0)
//					generatedClass.setCode(mod.parent.nextClassCode());
				for (GeneratedClass generatedClass2 : generatedClass.classMap.values()) {
					generatedClass2.setCode(mod.parent.nextClassCode());
				}
				for (GeneratedFunction generatedFunction : generatedClass.functionMap.values()) {
					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
						if (identTableEntry.isResolved()) {
							GeneratedNode node = identTableEntry.resolvedType();
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
							GeneratedNode node = identTableEntry.resolvedType();
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

	@NotNull
	private GenerateFunctions getGenerateFunctions(OS_Module mod) {
		return generatePhase.getGenerateFunctions(mod);
	}

	protected GenerateResult run3(OS_Module mod, List<GeneratedNode> lgc, WorkManager wm, GenerateC ggc) {
		GenerateResult gr = new GenerateResult();

		for (GeneratedNode generatedNode : lgc) {
			if (generatedNode.module() != mod) continue; // README curious

			if (generatedNode instanceof GeneratedContainerNC) {
				final GeneratedContainerNC nc = (GeneratedContainerNC) generatedNode;

				nc.generateCode(ggc, gr);
				final @NotNull Collection<GeneratedNode> gn1 = ggc.functions_to_list_of_generated_nodes(nc.functionMap.values());
				GenerateResult gr2 = ggc.generateCode(gn1, wm);
				gr.results().addAll(gr2.results());
				final @NotNull Collection<GeneratedNode> gn2 = ggc.classes_to_list_of_generated_nodes(nc.classMap.values());
				GenerateResult gr3 = ggc.generateCode(gn2, wm);
				gr.results().addAll(gr3.results());
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
//				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
//				for (GeneratedFunction generatedFunction : generatedClass.functionMap.values()) {
//					for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
//						final IdentIA ia2 = new IdentIA(identTableEntry.getIndex(), generatedFunction);
//						final String s = generatedFunction.getIdentIAPathNormal(ia2);
//						if (identTableEntry/*.isResolved()*/.getStatus() == BaseTableEntry.Status.KNOWN) {
////							GeneratedNode node = identTableEntry.resolved();
////							resolved_nodes.add(node);
//							System.out.println("91 Resolved IDENT "+ s);
//						} else {
////							assert identTableEntry.getStatus() == BaseTableEntry.Status.UNKNOWN;
////							identTableEntry.setStatus(BaseTableEntry.Status.UNKNOWN, null);
//							System.out.println("92 Unresolved IDENT "+ s);
//						}
//					}
//				}
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

	public void write_files(Compilation aCompilation) throws IOException {
		GenerateResult input = gr;

		//
		//
		//

		OutputStrategy os = new OutputStrategy();
		os.per(OutputStrategy.Per.PER_CLASS);

		ElSystem sys = new ElSystem();
		sys.verbose = false;
		sys.setCompilation(aCompilation);
		sys.setOutputStrategy(os);
		sys.generateOutputs(input);

		Multimap<String, Buffer> mb = ArrayListMultimap.create();

		for (GenerateResultItem ab : input.results()) {
			mb.put(ab.output, ab.buffer);
		}

		final File file_prefix = new File("COMP", aCompilation.getCompilationNumberString());
		file_prefix.mkdirs();
		String prefix = file_prefix.toString();

		{
			final String fn1 = new File(file_prefix, "inputs.txt").toString();

			DefaultBuffer buf = new DefaultBuffer("");
//			FileBackedBuffer buf = new FileBackedBuffer(fn1);
//			for (OS_Module module : modules) {
//				final String fn = module.getFileName();
//
//				append_hash(buf, fn);
//			}
//
//			for (CompilerInstructions ci : cis) {
//				final String fn = ci.getFilename();
//
//				append_hash(buf, fn);
//			}
			for (File file : aCompilation.getIO().recordedreads) {
				final String fn = file.toString();

				append_hash(buf, fn, aCompilation.getErrSink());
			}
			String s = buf.getText();
			Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn1, true)));
			w.write(s);
			w.close();
		}

		for (Map.Entry<String, Collection<Buffer>> entry : mb.asMap().entrySet()) {
			/*final*/ String key = entry.getKey();
//			assert key != null; // TODO remove me
			if (key == null) key = "random_000001";
			Path path = FileSystems.getDefault().getPath(prefix, key);
//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

			path.getParent().toFile().mkdirs();

			System.out.println("201 Writing path: "+path);
			CharSink x = aCompilation.getIO().openWrite(path);
			for (Buffer buffer : entry.getValue()) {
				x.accept(buffer.getText());
			}
			((FileCharSink)x).close();
		}
	}

	private void append_hash(TextBuffer aBuf, String aFilename, ErrSink errSink) throws IOException {
		@Nullable final String hh = Helpers.getHashForFilename(aFilename, errSink);
		if (hh != null) {
			aBuf.append(hh);
			aBuf.append(" ");
			aBuf.append_ln(aFilename);
		}
	}

	public void write_buffers(Compilation aCompilation) throws FileNotFoundException {
		final File file1 = new File("COMP", aCompilation.getCompilationNumberString());
		file1.mkdirs();

		PrintStream db_stream = new PrintStream(new File(file1, "buffers.txt"));
		debug_buffers(gr, db_stream);
	}
}

//
//
//
