/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.stages.gen_c.CDependencyRef;
import tripleo.elijah.stages.gen_c.OutputFileC;
import tripleo.elijah.stages.gen_generic.Dependency;
import tripleo.elijah.stages.gen_generic.DependencyRef;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.util.Helpers;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.DefaultBuffer;
import tripleo.util.buffer.TextBuffer;
import tripleo.util.io.CharSink;
import tripleo.util.io.DisposableCharSink;
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
 * Created 8/21/21 10:19 PM
 */
public class WritePipeline implements PipelineMember {
	private final CompletedItemsHandler cih;

	private final WritePipelineSharedState st;

	public WritePipeline(Compilation aCompilation, GenerateResult aGr) {
		st = new WritePipelineSharedState();

		// given
		st.c  = aCompilation;
		st.gr = aGr;

		// computed
		st.file_prefix = new File("COMP", st.c.getCompilationNumberString());

		// created
		// TODO should we be doing this? see below comment
		st.os = new OutputStrategy();
		st.os.per(OutputStrategy.Per.PER_CLASS); // TODO this needs to be configured per lsp

		// state
		st.mmb         = ArrayListMultimap.create();
		st.lsp_outputs = ArrayListMultimap.create();

		// ??
		st.sys         = new ElSystem();
		st.sys.verbose = false; // TODO flag? ie CompilationOptions
		st.sys.setCompilation(st.c);
		st.sys.setOutputStrategy(st.os);
/*
		sys.generateOutputs(gr);
*/

		cih = new CompletedItemsHandler(st);

		st.gr.subscribeCompletedItems(cih.observer());
	}

	public Multimap<CompilerInstructions, String> getLspOutputs() {
		Preconditions.checkNotNull(st);
		Preconditions.checkNotNull(st.lsp_outputs);

		return st.lsp_outputs;
	}

	@Override
	public void run() throws Exception {
		st.sys.generateOutputs(st.gr);

		boolean made = st.file_prefix.mkdirs();

		// TODO flag?
		write_inputs();

//		write_files();

		// TODO flag?
		write_buffers();
	}

	public void write_files() throws IOException {
		Multimap<String, Buffer> mb = ArrayListMultimap.create();

		for (GenerateResultItem ab : st.gr.results()) {
			mb.put(((CDependencyRef) ab.getDependency().getRef()).getHeaderFile(), ab.buffer); // TODO see above
		}

		assert st.mmb.equals(mb);

		write_files_helper(mb);
	}

	private void write_files_helper(Multimap<String, Buffer> mb) throws IOException {
		String prefix = st.file_prefix.toString();

		for (Map.Entry<String, Collection<Buffer>> entry : mb.asMap().entrySet()) {
			final String key = entry.getKey();
			assert key != null;
			Path path = FileSystems.getDefault().getPath(prefix, key);
//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

			path.getParent().toFile().mkdirs();

			// TODO functionality
			System.out.println("201 Writing path: " + path);
			CharSink x = st.c.getIO().openWrite(path);
			for (Buffer buffer : entry.getValue()) {
				x.accept(buffer.getText());
			}
			((FileCharSink) x).close();
		}
	}

	private void write_inputs() throws IOException {
		final String fn1 = new File(st.file_prefix, "inputs.txt").toString();

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
		for (File file : st.c.getIO().recordedreads) {
			final String fn = file.toString();

			append_hash(buf, fn, st.c.getErrSink());
		}
		String s = buf.getText();
		Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn1, true)));
		w.write(s);
		w.close();
	}

	private void append_hash(TextBuffer aBuf, String aFilename, ErrSink errSink) throws IOException {
		@Nullable final String hh = Helpers.getHashForFilename(aFilename, errSink);
		if (hh != null) {
			aBuf.append(hh);
			aBuf.append(" ");
			aBuf.append_ln(aFilename);
		}
	}

	public void write_buffers() throws FileNotFoundException {
		st.file_prefix.mkdirs();

		PrintStream db_stream = new PrintStream(new File(st.file_prefix, "buffers.txt"));
		PipelineLogic.debug_buffers(st.gr, db_stream);
	}

	/**
	 * Really a record, but state is not all set at once
	 */
	private final static class WritePipelineSharedState {
		/*final*/ OutputStrategy os;
		/*final*/ ElSystem       sys;
		/*final*/ Multimap<CompilerInstructions, String> lsp_outputs;
		private /*final*/ Compilation    c;
		private /*final*/ GenerateResult gr;
		private /*final*/ File file_prefix;
		private /*final*/ Multimap<String, Buffer> mmb;
	}

	private static class CompletedItemsHandler {
		//private final Compilation c;
		//private final Multimap<String, Buffer> mmb;
		//private final Multimap<CompilerInstructions, String> lsp_outputs;
		//private final GenerateResult gr;
		//private final File file_prefix;

		// region state
		final Multimap<Dependency, GenerateResultItem> gris = ArrayListMultimap.create();
		// README debugging purposes
		final List<GenerateResultItem> abs = new ArrayList<>();
		private final WritePipelineSharedState     sharedState;
		private       Observer<GenerateResultItem> observer;

		public CompletedItemsHandler(final WritePipelineSharedState aSharedState) {
			sharedState = aSharedState;
		}

		// endregion state

		public void addItem(final @NotNull GenerateResultItem ab) {
			// README debugging purposes
			abs.add(ab);

			final Dependency dependency = ab.getDependency();

			// README debugging purposes
			final DependencyRef dependencyRef = dependency.getRef();

			if (dependencyRef == null) {
				gris.put(dependency, ab);
			} else {
				final String output = ((CDependencyRef) dependency.getRef()).getHeaderFile();
				sharedState.mmb.put(output, ab.buffer);
				sharedState.lsp_outputs.put(ab.lsp.getInstructions(), output);
				for (GenerateResultItem generateResultItem : gris.get(dependency)) {
					final String output1 = generateResultItem.output;
					sharedState.mmb.put(output1, generateResultItem.buffer);
					sharedState.lsp_outputs.put(generateResultItem.lsp.getInstructions(), output1);
				}
				gris.removeAll(dependency);
			}
		}

		public void completeSequence() {
			try {
//				write_files_helper(mmb);
				String prefix = sharedState.file_prefix.toString();

				for (Map.Entry<String, OutputFileC> entry : sharedState.gr.outputFiles.entrySet()) {
					final String key = entry.getKey();
					assert key != null;

					final Path path = FileSystems.getDefault().getPath(prefix, key);

					boolean made = path.getParent().toFile().mkdirs();

					// TODO functionality
					System.out.println("201a Writing path: " + path);
					try (DisposableCharSink x = sharedState.c.getIO().openWrite(path)) {
						x.accept(entry.getValue().getOutput());

						//((FileCharSink) x).close();
						//x.dispose(); // README close automatically because of try-with-resources
					}
				}
			} catch (Exception aE) {
				sharedState.c.getErrSink().exception(aE);
			}
		}

		public Observer<GenerateResultItem> observer() {
			if (observer == null) {
				observer = new Observer<GenerateResultItem>() {
					@Override
					public void onSubscribe(@NonNull Disposable d) {
					}

					@Override
					public void onNext(@NonNull GenerateResultItem ab) {
						addItem(ab);
					}

					@Override
					public void onError(@NonNull Throwable e) {
					}

					@Override
					public void onComplete() {
						completeSequence();
					}
				};
			}

			return observer;
		}
	}
}

//
//
//
