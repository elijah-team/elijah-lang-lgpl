/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.comp;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.comp.queries.QueryEzFileToModule;
import tripleo.elijah.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah.comp.queries.QuerySourceFileToModule;
import tripleo.elijah.comp.queries.QuerySourceFileToModuleParams;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.Helpers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static tripleo.elijah.nextgen.query.Mode.SUCCESS;

public class Compilation {

	private final int                               _compilationNumber;
	private       IO                                io;
	private final ErrSink                           eee;
	public final  List<OS_Module>                   modules      = new ArrayList<OS_Module>();
	private final Map<String, OS_Module>            fn2m         = new HashMap<String, OS_Module>();
	private final Map<String, CompilerInstructions> fn2ci        = new HashMap<String, CompilerInstructions>();
	private final Map<String, OS_Package>           _packages    = new HashMap<String, OS_Package>();
	private       int                               _packageCode = 1;
	public final  List<CompilerInstructions>        cis          = new ArrayList<CompilerInstructions>();

	CompilerInstructions rootCI;

	//
	//
	//
	public PipelineLogic pipelineLogic;
	final  Pipeline      pipelines = new Pipeline();
	//
	//
	//

	public Compilation(final ErrSink eee, final IO io) {
		this.eee = eee;
		this.io  = io;
		this._compilationNumber = new Random().nextInt(Integer.MAX_VALUE);
	}

	public void feedCmdLine(final List<String> args) {
		try {
			main(args, eee == null ? new StdErrSink() : eee);
		} catch (Exception aE) {
			throw new RuntimeException(aE);
		}
	}

	public IO getIO() {
		return io;
	}

	public void setIO(final IO io) {
		this.io = io;
	}

	//
	//
	//

	public String stage = "O"; // Output

	public void main(final List<String> args, final ErrSink errSink) throws Exception {
		boolean do_out = false, silent = false;
		try {
			if (args.size() > 0) {
				final Options options = new Options();
				options.addOption("s", true, "stage: E: parse; O: output");
				options.addOption("showtree", false, "show tree");
				options.addOption("out", false, "make debug files");
				options.addOption("silent", false, "suppress DeduceType output to console");
				final CommandLineParser clp = new DefaultParser();
				final CommandLine cmd = clp.parse(options, args.toArray(new String[args.size()]));

				if (cmd.hasOption("s")) {
					stage = cmd.getOptionValue('s');
				}
				if (cmd.hasOption("showtree")) {
					showTree = true;
				}
				if (cmd.hasOption("out")) {
					do_out = true;
				}
				if (isGitlab_ci() || cmd.hasOption("silent")) {
					silent = true;
				}

				CompilerInstructions ez_file = null;
				final String[] args2 = cmd.getArgs();

				for (int i = 0; i < args2.length; i++) {
					final String file_name = args2[i];
					final File f = new File(file_name);
					final boolean matches2 = Pattern.matches(".+\\.ez$", file_name);
					if (matches2)
						add_ci(parseEzFile(f, file_name, eee));
					else {
//						eee.reportError("9996 Not an .ez file "+file_name);
						if (f.isDirectory()) {
							final List<CompilerInstructions> ezs = searchEzFiles(f);
							if (ezs.size() > 1) {
//								eee.reportError("9998 Too many .ez files, using first.");
								eee.reportError("9997 Too many .ez files, be specific.");
//								add_ci(ezs.get(0));
							} else if (ezs.size() == 0) {
								eee.reportError("9999 No .ez files found.");
							} else {
								ez_file = ezs.get(0);
								add_ci(ez_file);
							}
						} else
							eee.reportError("9995 Not a directory "+f.getAbsolutePath());
					}
				}

				rootCI = cis.get(0);
				System.err.println("130 GEN_LANG: "+ rootCI.genLang());
				findStdLib("c"); // TODO find a better place for this

				for (final CompilerInstructions ci : cis) {
					use(ci, do_out);
				}

				//
				final ICompilationAccess ca = new DefaultCompilationAccess(this);
				final ProcessRecord pr = new ProcessRecord(ca);
				final RuntimeProcesses rt = StageToRuntime.get(stage, ca, pr);

				rt.run();
				rt.postProcess(pr, ca);

/*
				if (stage.equals("E")) {
					// do nothing. job over
				} else {
					rt.part1();
					rt.part2();

					pipelines.run();

					writeLogs(silent, pipelineLogic.elLogs);
				}
*/
			} else {
				System.err.println("Usage: eljc [--showtree] [-sE|O] <directory or .ez file names>");
			}
		} catch (final Exception e) {
			errSink.exception(e);
			throw e;
		}
	}

	static class StageToRuntime {
		public static @NotNull RuntimeProcesses get(final @NotNull String stage, final @NotNull ICompilationAccess aCa, final ProcessRecord aPr) {
			final RuntimeProcesses rtp = new RuntimeProcesses(aCa.getCompilation());

			if (stage.equals("E"))
				rtp.add(new EmptyProcess(aCa, aPr));
			if (stage.equals("O"))
				rtp.add(new OStageProcess(aCa, aPr));
			if (stage.equals("D"))
				rtp.add(new DStageProcess(aCa, aPr));

			return rtp;
		}
	}

	public static ElLog.Verbosity gitlabCIVerbosity() {
		final boolean gitlab_ci = isGitlab_ci();
		return gitlab_ci ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	public static boolean isGitlab_ci() {
		return System.getenv("GITLAB_CI") != null;
	}

	private List<CompilerInstructions> searchEzFiles(final File directory) {
		final List<CompilerInstructions> R = new ArrayList<CompilerInstructions>();
		final FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(final File file, final String s) {
				final boolean matches2 = Pattern.matches(".+\\.ez$", s);
				return matches2;
			}
		};
		final String[] list = directory.list(filter);
		if (list != null) {
			for (final String file_name : list) {
				try {
					final File file = new File(directory, file_name);
					final CompilerInstructions ezFile = parseEzFile(file, file.toString(), eee);
					if (ezFile != null)
						R.add(ezFile);
					else
						eee.reportError("9995 ezFile is null "+file.toString());
				} catch (final Exception e) {
					eee.exception(e);
				}
			}
		}
		return R;
	}

	private void add_ci(final CompilerInstructions ci) {
		cis.add(ci);
	}

	public void use(final CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
		final File instruction_dir = new File(compilerInstructions.getFilename()).getParentFile();
		for (final LibraryStatementPart lsp : compilerInstructions.lsps) {
			final String dir_name = Helpers.remove_single_quotes_from_string(lsp.getDirName());
			File dir;// = new File(dir_name);
			if (dir_name.equals(".."))
				dir = instruction_dir/*.getAbsoluteFile()*/.getParentFile();
			else
				dir = new File(instruction_dir, dir_name);
			use_internal(dir, do_out, lsp);
		}
		final LibraryStatementPart lsp = new LibraryStatementPart();
		lsp.setName(Helpers.makeToken("default")); // TODO: make sure this doesn't conflict
		lsp.setDirName(Helpers.makeToken(String.format("\"%s\"", instruction_dir)));
		lsp.setInstructions(compilerInstructions);
		use_internal(instruction_dir, do_out, lsp);
	}

	private void use_internal(final File dir, final boolean do_out, LibraryStatementPart lsp) throws Exception {
		if (!dir.isDirectory()) {
			eee.reportError("9997 Not a directory " + dir.toString());
			return;
		}
		//
		final FilenameFilter accept_source_files = new FilenameFilter() {
			@Override
			public boolean accept(final File directory, final String file_name) {
				final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
						             || Pattern.matches(".+\\.elijjah$", file_name);
				return matches;
			}
		};
		final File[] files = dir.listFiles(accept_source_files);
		if (files != null) {
			for (final File file : files) {
				parseElijjahFile(file, file.toString(), eee, do_out, lsp);
			}
		}
	}

	private CompilerInstructions parseEzFile(final File f, final String file_name, final ErrSink errSink) throws Exception {
		System.out.println((String.format("   %s", f.getAbsolutePath())));
		if (!f.exists()) {
			errSink.reportError(
					"File doesn't exist " + f.getAbsolutePath());
			return null;
		}

		final CompilerInstructions m = realParseEzFile(file_name, io.readFile(f), f);
		{
			String prelude = m.genLang();
			System.err.println("230 " + prelude);
			if (prelude == null) prelude = "c"; // TODO should be java for eljc
		}
		return m;
	}

	private void parseElijjahFile(@NotNull final File f,
								  final String file_name,
								  final ErrSink errSink,
								  final boolean do_out,
								  LibraryStatementPart lsp) throws Exception {
		System.out.println((String.format("   %s", f.getAbsolutePath())));
		if (f.exists()) {
			final OS_Module m = realParseElijjahFile(file_name, f, do_out);
			m.setLsp(lsp);
			m.prelude = this.findPrelude("c"); // TODO we dont know which prelude to find yet
		} else {
			errSink.reportError(
					"File doesn't exist " + f.getAbsolutePath()); // TODO getPath?? and use a Diagnostic
		}
	}

	public OS_Module realParseElijjahFile(final String f, final File file, final boolean do_out) throws Exception {
		final String absolutePath = file.getCanonicalFile().toString();
		if (fn2m.containsKey(absolutePath)) { // don't parse twice
			return fn2m.get(absolutePath);
		}
		final InputStream s = io.readFile(file);
		try {
			final Operation<OS_Module> om = parseFile_(f, s, do_out);
			if (om.mode() != SUCCESS) {
				final Exception e = om.failure();
				assert e != null;

				System.err.println(("parser exception: " + e));
				e.printStackTrace(System.err);
				s.close();
				return null;
			}
			final OS_Module R = (OS_Module) om.success();
			fn2m.put(absolutePath, R);
			s.close();
			return R;
		} catch (final ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	public CompilerInstructions realParseEzFile(final String f, final InputStream s, final File file) throws Exception {
		final String absolutePath = file.getCanonicalFile().toString();
		if (fn2ci.containsKey(absolutePath)) { // don't parse twice
			return fn2ci.get(absolutePath);
		}
		try {
			final Operation<CompilerInstructions> cio = parseEzFile_(f, s);
			if (cio.mode() != SUCCESS) {
				final Exception e = cio.failure();
				assert e != null;

				System.err.println(("parser exception: " + e));
				e.printStackTrace(System.err);
				s.close();
				return null;
			}
			final CompilerInstructions R = cio.success();
			R.setFilename(file.toString());
			fn2ci.put(absolutePath, R);
			s.close();
			return R;
		} catch (final ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	public static class ModuleBuilder {
//		private final Compilation compilation;
		private final OS_Module mod;
		private boolean _addToCompilation = false;
		private String _fn = null;

		public ModuleBuilder(Compilation aCompilation) {
//			compilation = aCompilation;
			mod = new OS_Module();
			mod.setParent(aCompilation);
		}

		public ModuleBuilder setContext() {
			final ModuleContext mctx = new ModuleContext(mod);
			mod.setContext(mctx);
			return this;
		}

		public OS_Module build() {
			if (_addToCompilation) {
				if (_fn == null) throw new IllegalStateException("Filename not set in ModuleBuilder");
				mod.getCompilation().addModule(mod, _fn);
			}
			return mod;
		}

		public ModuleBuilder withPrelude(String aPrelude) {
			mod.prelude = mod.getCompilation().findPrelude("c");
			return this;
		}

		public ModuleBuilder withFileName(String aFn) {
			_fn = aFn;
			mod.setFileName(aFn);
			return this;
		}

		public ModuleBuilder addToCompilation() {
			_addToCompilation = true;
			return this;
		}
	}

	public ModuleBuilder moduleBuilder() {
		return new ModuleBuilder(this);
	}

	private Operation<OS_Module> parseFile_(final String f, final InputStream s, final boolean do_out) throws RecognitionException, TokenStreamException {
		final QuerySourceFileToModuleParams qp = new QuerySourceFileToModuleParams(s, f, do_out);
		return new QuerySourceFileToModule(qp, this).calculate();
	}

	private Operation<CompilerInstructions> parseEzFile_(final String f, final InputStream s) throws RecognitionException, TokenStreamException {
		final QueryEzFileToModuleParams qp = new QueryEzFileToModuleParams(f, s);
		return new QueryEzFileToModule(qp).calculate();
	}

	boolean showTree = false;

	public List<ClassStatement> findClass(final String aClassName) {
		final List<ClassStatement> l = new ArrayList<ClassStatement>();
		for (final OS_Module module : modules) {
			if (module.hasClass(aClassName)) {
				l.add((ClassStatement) module.findClass(aClassName));
			}
		}
		return l;
	}

	public int errorCount() {
		return eee.errorCount();
	}

	public OS_Module findPrelude(final String prelude_name) {
		final File local_prelude = new File("lib_elijjah/lib-"+prelude_name+"/Prelude.elijjah");
		if (local_prelude.exists()) {
			try {
				return realParseElijjahFile(local_prelude.getName(), local_prelude, false);
			} catch (final Exception e) {
				eee.exception(e);
				return null;
			}
		}
		return null;
	}

	public boolean findStdLib(final String prelude_name) {
		final File local_stdlib = new File("lib_elijjah/lib-"+prelude_name+"/stdlib.ez");
		if (local_stdlib.exists()) {
			try {
				final CompilerInstructions ci = realParseEzFile(local_stdlib.getName(), io.readFile(local_stdlib), local_stdlib);
				add_ci(ci);
				return true;
			} catch (final Exception e) {
				eee.exception(e);
			}
		}
		return false;
	}

	//
	// region MODULE STUFF
	//

	public void addModule(final OS_Module module, final String fn) {
		modules.add(module);
		fn2m.put(fn, module);
	}

	// endregion

    //
	// region CLASS AND FUNCTION CODES
	//

	private int _classCode = 101;
	private int _functionCode = 1001;

	public int nextClassCode() {
		return _classCode++;
	}

	public int nextFunctionCode() {
		return _functionCode++;
	}

	// endregion

	//
	// region PACKAGES
	//

	public boolean isPackage(final String pkg) {
		return _packages.containsKey(pkg);
	}

	public OS_Package getPackage(final Qualident pkg_name) {
		return _packages.get(pkg_name.toString());
	}

	public OS_Package makePackage(final Qualident pkg_name) {
		if (!isPackage(pkg_name.toString())) {
			final OS_Package newPackage = new OS_Package(pkg_name, nextPackageCode());
			_packages.put(pkg_name.toString(), newPackage);
			return newPackage;
		} else
			return _packages.get(pkg_name.toString());
	}

	private int nextPackageCode() {
		return _packageCode++;
	}

	// endregion

	public int compilationNumber() {
		return _compilationNumber;
	}

	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

	public ErrSink getErrSink() {
		return eee;
	}

	public void addFunctionMapHook(FunctionMapHook aFunctionMapHook) {
		pipelineLogic.dp.addFunctionMapHook(aFunctionMapHook);
	}

}

//
//
//
