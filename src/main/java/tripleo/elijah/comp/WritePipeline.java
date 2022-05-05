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
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.stages.gen_c.CDependencyRef;
import tripleo.elijah.stages.gen_c.OutputFileC;
import tripleo.elijah.stages.gen_generic.Dependency;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.util.Helpers;
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
 * Created 8/21/21 10:19 PM
 */
public class WritePipeline implements PipelineMember {
	private final Compilation    c;
	private final GenerateResult gr;

	final OutputStrategy os;
	final ElSystem sys;

	private final File file_prefix;

	private final Multimap<String, Buffer> mmb;
	final Multimap<CompilerInstructions, String> lsp_outputs;

	public WritePipeline(Compilation aCompilation, GenerateResult aGr) {
		c = aCompilation;
		gr = aGr;

		file_prefix = new File("COMP", c.getCompilationNumberString());

		os = new OutputStrategy();
		os.per(OutputStrategy.Per.PER_CLASS); // TODO this needs to be configured per lsp

		mmb = ArrayListMultimap.create();
		lsp_outputs = ArrayListMultimap.create();

		sys = new ElSystem();
		sys.verbose = false; // TODO flag? ie CompilationOptions
		sys.setCompilation(c);
		sys.setOutputStrategy(os);
/*
		sys.generateOutputs(gr);
*/
		gr.subscribeCompletedItems(new Observer<GenerateResultItem>() {
			Multimap<Dependency, GenerateResultItem> gris = ArrayListMultimap.create();

			@Override
			public void onSubscribe(@NonNull Disposable d) {

			}

			final List<GenerateResultItem> abs = new ArrayList<>();

			@Override
			public void onNext(@NonNull GenerateResultItem ab) {
				abs.add(ab);

				final Dependency dependency = ab.getDependency();
				if (dependency.getRef() == null) {
					gris.put(dependency, ab);
				} else {
					final String output = ((CDependencyRef) dependency.getRef()).getHeaderFile();
					mmb.put(output, ab.buffer);
					lsp_outputs.put(ab.lsp.getInstructions(), output);
					for (GenerateResultItem generateResultItem : gris.get(dependency)) {
						final String output1 = generateResultItem.output;
						mmb.put(output1, generateResultItem.buffer);
						lsp_outputs.put(generateResultItem.lsp.getInstructions(), output1);
					}
					gris.removeAll(dependency);
				}
			}

			@Override
			public void onError(@NonNull Throwable e) {

			}

			@Override
			public void onComplete() {
				try {
//					write_files_helper(mmb);
					write_files_helper2(abs);
				} catch (IOException aE) {
					c.getErrSink().exception(aE);
				}
			}
		});
	}

	@Override
	public void run() throws Exception {
		sys.generateOutputs(gr);

		file_prefix.mkdirs();
		// TODO flag?
		write_inputs();

//		write_files();

		// TODO flag?
		write_buffers();
	}

	public void write_files() throws IOException {
		Multimap<String, Buffer> mb = ArrayListMultimap.create();

		for (GenerateResultItem ab : gr.results()) {
			mb.put(((CDependencyRef)ab.getDependency().getRef()).getHeaderFile(), ab.buffer); // TODO see above
		}

		assert mmb.equals(mb);

		write_files_helper(mb);
	}

	private void write_files_helper(Multimap<String, Buffer> mb) throws IOException {
		String prefix = file_prefix.toString();

		for (Map.Entry<String, Collection<Buffer>> entry : mb.asMap().entrySet()) {
			final String key = entry.getKey();
			assert key != null;
			Path path = FileSystems.getDefault().getPath(prefix, key);
//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

			path.getParent().toFile().mkdirs();

			// TODO functionality
			System.out.println("201 Writing path: "+path);
			CharSink x = c.getIO().openWrite(path);
			for (Buffer buffer : entry.getValue()) {
				x.accept(buffer.getText());
			}
			((FileCharSink)x).close();
		}
	}

	private void write_files_helper2(List<GenerateResultItem> abs) throws IOException {
		String prefix = file_prefix.toString();

		for (Map.Entry<String, OutputFileC> entry : gr.outputFiles.entrySet()) {
			final String key = entry.getKey();
			assert key != null;
			Path path = FileSystems.getDefault().getPath(prefix, key);

			path.getParent().toFile().mkdirs();

			// TODO functionality
			System.out.println("201a Writing path: "+path);
			CharSink x = c.getIO().openWrite(path);
			x.accept(entry.getValue().getOutput());

			((FileCharSink)x).close();
		}
	}

	private void write_inputs() throws IOException {
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
		for (File file : c.getIO().recordedreads) {
			final String fn = file.toString();

			append_hash(buf, fn, c.getErrSink());
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
		file_prefix.mkdirs();

		PrintStream db_stream = new PrintStream(new File(file_prefix, "buffers.txt"));
		PipelineLogic.debug_buffers(gr, db_stream);
	}
}

//
//
//
