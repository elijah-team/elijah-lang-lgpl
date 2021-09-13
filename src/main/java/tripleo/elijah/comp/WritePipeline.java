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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;
import static tripleo.elijah.util.Helpers.String_join;

/**
 * Created 8/21/21 10:19 PM
 */
public class WritePipeline implements PipelineMember {
	private final Compilation c;
	private final GenerateResult gr;

	final OutputStrategy os;
	final ElSystem sys;

	private final File file_prefix;

	private final Multimap<String, Buffer> mmb;
	private final Multimap<CompilerInstructions, String> lsp_outputs;

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
		gr.subscribeCompletedItems(new Observer<GenerateResultItem>() {
			@Override
			public void onSubscribe(@NonNull Disposable d) {

			}

			@Override
			public void onNext(@NonNull GenerateResultItem ab) {
				// OutputFileCommand (somewhere...)
				mmb.put(ab.output, ab.buffer);
				lsp_outputs.put(ab.lsp.getInstructions(), ab.output);
			}

			@Override
			public void onError(@NonNull Throwable e) {

			}

			@Override
			public void onComplete() {
				try {
					write_files_helper(mmb);
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

		write_makefiles();
	}

	public void write_files() throws IOException {
		Multimap<String, Buffer> mb = ArrayListMultimap.create();

		for (GenerateResultItem ab : gr.results()) {
			mb.put(ab.output, ab.buffer);
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

	public void write_makefiles() throws IOException {
		List<String> dep_dirs = new LinkedList<String>();

		CharSink root_file = c.getIO().openWrite(FileSystems.getDefault().getPath("COMP", c.getCompilationNumberString(), "meson.build"));
		try {
			String project_name = c.rootCI.getName();
			String project_string = String.format("project('%s', 'c', version: '1.0.0', meson_version: '>= 0.48.0',)", project_name);
			root_file.accept(project_string);
			root_file.accept("\n");

			for (CompilerInstructions compilerInstructions : lsp_outputs.keySet()) {
				String name = compilerInstructions.getName();
				final Path dpath = FileSystems.getDefault().getPath("COMP",
						c.getCompilationNumberString(),
						name);
				if (dpath.toFile().exists()) {
					String name_subdir_string = String.format("subdir('%s')\n", name);
					root_file.accept(name_subdir_string);
					dep_dirs.add(name);
				}
			}
			dep_dirs.add("Prelude");
//			String prelude_string = String.format("subdir(\"Prelude_%s\")\n", /*c.defaultGenLang()*/"c");
			String prelude_string = "subdir('Prelude')\n";
			root_file.accept(prelude_string);

//			root_file.accept("\n");

			String deps_names = String_join(", ", dep_dirs.stream()
					.map(x -> String.format("%s", x)) // TODO _lib ??
					.collect(Collectors.toList()));
			root_file.accept(String.format("%s_bin = executable('%s', link_with: [ %s ], install: true)", project_name, project_name, deps_names)); // dependencies, include_directories
		} finally {
			((FileCharSink) root_file).close();
		}

		for (CompilerInstructions compilerInstructions : lsp_outputs.keySet()) {
			int y=2;
			final String sub_dir = compilerInstructions.getName();
			final Path dpath = FileSystems.getDefault().getPath("COMP",
					c.getCompilationNumberString(),
					sub_dir);
			if (dpath.toFile().exists()) {
				final Path path = FileSystems.getDefault().getPath("COMP",
						c.getCompilationNumberString(),
						sub_dir,
						"meson.build");
				CharSink sub_file = c.getIO().openWrite(path);
				try {
					int yy = 2;
					Collection<String> files_ = lsp_outputs.get(compilerInstructions);
					Set<String> files = files_.stream()
							.filter(x -> x.endsWith(".c"))
							.map(x -> String.format("\t'%s',", pullFileName(x)))
							.collect(Collectors.toSet()); // TODO .toUnmodifiableSet -- language level 10
					sub_file.accept(String.format("%s_sources = files(\n%s\n)", sub_dir, String_join("\n", files)));
					sub_file.accept("\n");
					sub_file.accept(String.format("%s = static_library('%s', %s_sources, install: true,)", sub_dir, sub_dir, sub_dir)); // include_directories, dependencies: [],
					sub_file.accept("\n");
					sub_file.accept("\n");
					sub_file.accept(String.format("%s_dep = declare_dependency( link_with: %s )", sub_dir, sub_dir)); // include_directories
					sub_file.accept("\n");
				} finally {
					((FileCharSink) sub_file).close();
				}
				{
					final Path ppath = FileSystems.getDefault().getPath("COMP",
							c.getCompilationNumberString(),
							"Prelude",
							"meson.build");
					CharSink prel_file = c.getIO().openWrite(ppath);
					try {
						int yy = 2;
						Collection<String> files_ = lsp_outputs.get(compilerInstructions);
						List<String> files = List_of("'Prelude.c'")/*files_.stream()
								.filter(x -> x.endsWith(".c"))
								.map(x -> String.format("\t'%s',", x))
								.collect(Collectors.toList())*/;
						prel_file.accept(String.format("Prelude_sources = files(\n%s\n)", String_join("\n", files)));
						prel_file.accept("\n");
						prel_file.accept("Prelude = static_library('Prelude', Prelude_sources, install: true,)"); // include_directories, dependencies: [],
						prel_file.accept("\n");
					} finally {
						((FileCharSink) prel_file).close();
					}
				}
			}
		}
	}

	final Pattern pullPat = Pattern.compile("/[^/]+/(.+)");
	private String pullFileName(String aFilename) {
		//return aFilename.substring(aFilename.lastIndexOf('/')+1);
		Matcher x = pullPat.matcher(aFilename);
		try {
			if (x.matches())
				return x.group(1);
		} catch (IllegalStateException aE) {
		}
		return null;
	}
}

//
//
//
