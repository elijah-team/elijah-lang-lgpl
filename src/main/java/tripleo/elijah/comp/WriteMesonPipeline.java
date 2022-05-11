/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.util.io.CharSink;
import tripleo.util.io.FileCharSink;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;
import static tripleo.elijah.util.Helpers.String_join;

/**
 * Created 9/13/21 11:58 PM
 */
public class WriteMesonPipeline implements PipelineMember {
//	private final File file_prefix;
//	private final GenerateResult gr;

	private final WritePipeline writePipeline;
	private final Compilation c;

	public WriteMesonPipeline(Compilation aCompilation, GenerateResult aGr, WritePipeline aWritePipeline) {
		c = aCompilation;
//		gr = aGr;
		writePipeline = aWritePipeline;

//		file_prefix = new File("COMP", c.getCompilationNumberString());
	}

	private void write_makefiles() throws IOException {
		Multimap<CompilerInstructions, String> lsp_outputs = writePipeline.getLspOutputs(); // TODO move this

		List<String> dep_dirs = new LinkedList<String>();

		write_root(lsp_outputs, dep_dirs);

		for (CompilerInstructions compilerInstructions : lsp_outputs.keySet()) {
			int y=2;
			final String sub_dir = compilerInstructions.getName();
			final Path dpath = getPath(sub_dir);
			if (dpath.toFile().exists()) {
				write_lsp(lsp_outputs, compilerInstructions, sub_dir);
			}
		}
		write_prelude();
	}

	private void write_root(Multimap<CompilerInstructions, String> lsp_outputs, List<String> aDep_dirs) throws IOException {
		CharSink root_file = c.getIO().openWrite(getPath("meson.build"));
		try {
			String project_name = c.rootCI.getName();
			String project_string = String.format("project('%s', 'c', version: '1.0.0', meson_version: '>= 0.48.0',)", project_name);
			root_file.accept(project_string);
			root_file.accept("\n");

			for (CompilerInstructions compilerInstructions : lsp_outputs.keySet()) {
				String name = compilerInstructions.getName();
				final Path dpath = getPath(name);
				if (dpath.toFile().exists()) {
					String name_subdir_string = String.format("subdir('%s')\n", name);
					root_file.accept(name_subdir_string);
					aDep_dirs.add(name);
				}
			}
			aDep_dirs.add("Prelude");
//			String prelude_string = String.format("subdir(\"Prelude_%s\")\n", /*c.defaultGenLang()*/"c");
			String prelude_string = "subdir('Prelude')\n";
			root_file.accept(prelude_string);

//			root_file.accept("\n");

			String deps_names = String_join(", ", aDep_dirs.stream()
					.map(x -> String.format("%s", x)) // TODO _lib ??
					.collect(Collectors.toList()));
			root_file.accept(String.format("%s_bin = executable('%s', link_with: [ %s ], install: true)", project_name, project_name, deps_names)); // dependencies, include_directories
		} finally {
			((FileCharSink) root_file).close();
		}
	}

	@NotNull
	private Path getPath(String aName) {
		return FileSystems.getDefault().getPath("COMP",
				c.getCompilationNumberString(),
				aName);
	}

	private void write_lsp(Multimap<CompilerInstructions, String> lsp_outputs, CompilerInstructions compilerInstructions, String aSub_dir) throws IOException {
		final Path path = FileSystems.getDefault().getPath("COMP",
				c.getCompilationNumberString(),
				aSub_dir,
				"meson.build");
		CharSink sub_file = c.getIO().openWrite(path);
		try {
			int yy = 2;
			Collection<String> files_ = lsp_outputs.get(compilerInstructions);
			Set<String> files = files_.stream()
					.filter(x -> x.endsWith(".c"))
					.map(x -> String.format("\t'%s',", pullFileName(x)))
					.collect(Collectors.toSet()); // TODO .toUnmodifiableSet -- language level 10
			sub_file.accept(String.format("%s_sources = files(\n%s\n)", aSub_dir, String_join("\n", files)));
			sub_file.accept("\n");
			sub_file.accept(String.format("%s = static_library('%s', %s_sources, install: false,)", aSub_dir, aSub_dir, aSub_dir)); // include_directories, dependencies: [],
			sub_file.accept("\n");
			sub_file.accept("\n");
			sub_file.accept(String.format("%s_dep = declare_dependency( link_with: %s )", aSub_dir, aSub_dir)); // include_directories
			sub_file.accept("\n");
		} finally {
			((FileCharSink) sub_file).close();
		}
	}

	private void write_prelude() throws IOException {
		final Path ppath1 = getPath("Prelude");
		final Path ppath = ppath1.resolve("meson.build"); // Java is wierd

		ppath.getParent().toFile().mkdirs(); // README just in case -- but should be unnecessary at this point

		CharSink prel_file = c.getIO().openWrite(ppath);
		try {
//			Collection<String> files_ = lsp_outputs.get(compilerInstructions);
			List<String> files = List_of("'Prelude.c'")/*files_.stream()
					.filter(x -> x.endsWith(".c"))
					.map(x -> String.format("\t'%s',", x))
					.collect(Collectors.toList())*/;
			prel_file.accept(String.format("Prelude_sources = files(\n%s\n)", String_join("\n", files)));
			prel_file.accept("\n");
			prel_file.accept("Prelude = static_library('Prelude', Prelude_sources, install: false,)"); // include_directories, dependencies: [],
			prel_file.accept("\n");
			prel_file.accept("\n");
			prel_file.accept(String.format("%s_dep = declare_dependency( link_with: %s )", "Prelude", "Prelude")); // include_directories
			prel_file.accept("\n");
		} finally {
			((FileCharSink) prel_file).close();
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

	@Override
	public void run() throws Exception {
		write_makefiles();
	}
}

//
//
//
