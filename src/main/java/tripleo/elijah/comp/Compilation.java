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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.Out;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.stages.translate.TranslateModule;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;
import tripleo.elijjah.EzLexer;
import tripleo.elijjah.EzParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Compilation {

	private IO io;
	public ErrSink eee;
	private final List<OS_Module> modules = new ArrayList<OS_Module>();
	private final Map<String, OS_Module> fn2m = new HashMap<String, OS_Module>();
	private final Map<String, CompilerInstructions> fn2ci = new HashMap<String, CompilerInstructions>();
	private final Map<String, OS_Package> _packages = new HashMap<String, OS_Package>();
	private int _packageCode = 1;
	final private List<CompilerInstructions> cis = new ArrayList<CompilerInstructions>();

	public Compilation(ErrSink eee, IO io) {
		this.eee = eee;
		this.io  = io;
	}

	public void feedCmdLine(List<String> args) {
		main(args, new StdErrSink());
	}

	public IO getIO() {
		return io;
	}

	public void setIO(IO io) {
		this.io = io;
	}

	//
	//
	//

	public String stage = "O"; // Output

	public void main(List<String> args, ErrSink errSink) {
		boolean do_out = false;
		try {
			if (args.size() > 0) {
				Options options = new Options();
				options.addOption("s", true, "stage: E: parse; O: output");
				options.addOption("showtree", false, "show tree");
				options.addOption("out", false, "make debug files");
				CommandLineParser clp = new DefaultParser();
				CommandLine cmd = clp.parse(options, args.toArray(new String[args.size()]));

				if (cmd.hasOption("s")) {
					stage = cmd.getOptionValue('s');
				}
				if (cmd.hasOption("showtree")) {
					showTree = true;
				}
				if (cmd.hasOption("out")) {
					do_out = true;
				}

				final String[] args2 = cmd.getArgs();

				for (int i = 0; i < args2.length; i++) {
					final File f = new File(args2[i]);
					if (f.isDirectory()) {
						List<CompilerInstructions> ez_files = searchEzFiles(f);
						if (ez_files.size() > 1) {
							eee.reportError("9999 Too many .ez files found, using first");
							use(ez_files.get(0), do_out);
						} else if (ez_files.size() == 0) {
							eee.info("9998 No .ez files found. Using defaults");
							useDefaults(args2);
						}
					}
//					doFile(f, errSink, do_out);
				}

				for (CompilerInstructions ci : cis) {
					use(ci, do_out);
				}

				//
				if (stage.equals("E")) {
					// do nothing. job over
				} else {
					for (OS_Module module : modules) {
						new DeduceTypes(module).deduce();
						for (OS_Element2 item : module.items()) {
							if (item instanceof ClassStatement || item instanceof NamespaceStatement)
								System.err.println("8001 "+item);
						}
						new TranslateModule(module).translate();
//						new ExpandFunctions(module).expand();
//
//      				final JavaCodeGen visit = new JavaCodeGen();
//		        		module.visitGen(visit);
					}
				}
			} else {
				System.err.println("Usage: eljc [--showtree] [-sE|O] <directory or file names>");
			}
		} catch (Exception e) {
			errSink.exception(e);
		}
	}

	private void useDefaults(String[] args2) {
		CompilerInstructions ci = new CompilerInstructions();
		final LibraryStatementPart lsp = new LibraryStatementPart();
		for (String s : args2) {
			lsp.setDirName(Helpers.makeToken(s));
		}
		ci.add(lsp);
		add_ci(ci);
	}

	private void add_ci(CompilerInstructions ci) {
		cis.add(ci);
	}

	private void use(CompilerInstructions compilerInstructions, boolean do_out) throws Exception {
		for (LibraryStatementPart lsp : compilerInstructions.lsps) {
			String dir_name = lsp.getDirName();
			File dir = new File(dir_name);
			if (dir.isDirectory())
				doDirectory(dir, eee, do_out);
		}
		int y=2;
	}

	private List<CompilerInstructions> searchEzFiles(File directory) {
		final List<CompilerInstructions> R = new ArrayList<CompilerInstructions>();
		final FilenameFilter f = new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				final boolean matches2 = Pattern.matches(".+\\.ez$", s);
				return matches2;
			}
		};
		for (String file_name : directory.list(f)) {
			try {
				R.add(parseEzFile(new File(directory, file_name), file_name, eee));
			} catch (Exception e) {
				eee.exception(e);
			}
		}
		return R;
	}

	public void doFile(@NotNull File f, ErrSink errSink, boolean do_out) throws Exception {
		if (f.isDirectory()) {
			doDirectory(f, errSink, do_out);
		} else {
			final String file_name = f.toString();
			final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
					                        || Pattern.matches(".+\\.elijjah$", file_name);
//			if (f.isDirectory()) return; // TODO testing idea tools (failed)
			if (matches) {
				parseElijjahFile(f, file_name, errSink, do_out);
				return;
			}

			final boolean matches2 = Pattern.matches(".+\\.ez$", file_name);
			if (matches2) {
				parseEzFile(f, file_name, errSink);
			}
		}
	}

	private void doDirectory(@NotNull File f, ErrSink errSink, boolean do_out) throws Exception {
		String[] files = f.list();
		for (int i = 0; i < files.length; i++)
			doFile(new File(f, files[i]), errSink, do_out); // recursion, backpressure
	}

	private CompilerInstructions parseEzFile(File f, String file_name, ErrSink errSink) throws Exception {
		System.out.println((String.format("   %s", f.getAbsolutePath())));
		if (f.exists()) {
			CompilerInstructions m = realParseEzFile(file_name, io.readFile(f), f);
			return m;
//			m.prelude = this.findPrelude("c"); // TODO extract Prelude for all modules from here
		} else {
			errSink.reportError(
					"File doesn't exist " + f.getAbsolutePath());
		}
		return null;
	}

	private void parseElijjahFile(@NotNull File f, String file_name, ErrSink errSink, boolean do_out) throws Exception {
		System.out.println((String.format("   %s", f.getAbsolutePath())));
		if (f.exists()) {
			OS_Module m = realParseElijjahFile(file_name, io.readFile(f), f, do_out);
			m.prelude = this.findPrelude("c"); // TODO we dont know which prelude to find yet
		} else {
			errSink.reportError(
					"File doesn't exist " + f.getAbsolutePath());
		}
	}

	public OS_Module realParseElijjahFile(String f, InputStream s, File file, boolean do_out) throws Exception {
		if (fn2m.containsKey(f)) { // don't parse twice
			return fn2m.get(f);
		}
		try {
			OS_Module R = parseFile_(f, s, do_out);
			s.close();
			fn2m.put(f, R);
			return R;
		} catch (ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	public CompilerInstructions realParseEzFile(String f, InputStream s, File file) throws Exception {
		if (fn2ci.containsKey(f)) { // don't parse twice
			return fn2ci.get(f);
		}
		try {
			CompilerInstructions R = parseEzFile_(f, s);
			s.close();
			fn2ci.put(f, R);
			return R;
		} catch (ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	private OS_Module parseFile_(String f, InputStream s, boolean do_out) throws RecognitionException, TokenStreamException {
		ElijjahLexer lexer = new ElijjahLexer(s);
		lexer.setFilename(f);
		ElijjahParser parser = new ElijjahParser(lexer);
		parser.out = new Out(f, this, do_out);
		parser.setFilename(f);
		parser.program();
		final OS_Module module = parser.out.module();
		parser.out = null;
		return module;
	}

	private CompilerInstructions parseEzFile_(String f, InputStream s) throws RecognitionException, TokenStreamException {
		EzLexer lexer = new EzLexer(s);
		lexer.setFilename(f);
		EzParser parser = new EzParser(lexer);
		parser.setFilename(f);
		parser.program();
		final CompilerInstructions instructions = parser.ci;
		return instructions;
	}

	boolean showTree = false;

	public OS_Element findClass(String string) {
		for (OS_Module module : modules) {
			if (module.hasClass(string)) {
				return module.findClass(string);
			}
		}
		return null;
	}

	public void addModule(OS_Module module, String fn) {
		modules.add(module);
		fn2m.put(fn, module);
	}

	public int errorCount() {
		return eee.errorCount();
	}

	public OS_Module findPrelude(String prelude_name) {
		File local_prelude = new File("lib_elijjah/lib-"+prelude_name+"/Prelude.elijjah");
		if (local_prelude.exists()) {
			try {
				return realParseElijjahFile(local_prelude.getName(), io.readFile(local_prelude), local_prelude, false);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

    public OS_Module fileNameToModule(String fileName) {
        if (fn2m.containsKey(fileName)) {
            return fn2m.get(fileName);
        }
        return null;
    }

    //
	//  CLASS AND FUNCTION CODES
	//

	private static int _classCode = 101;
	private static int _functionCode = 1001;

	public int nextClassCode() {
		return _classCode++;
	}

	public int nextFunctionCode() {
		return _functionCode++;
	}

	//
	//  PACKAGES
	//

	public boolean isPackage(String pkg) {
		return _packages.containsKey(pkg);
	}

	public OS_Package getPackage(Qualident pkg_name) {
		return _packages.get(pkg_name.toString());
	}

	public OS_Package makePackage(Qualident pkg_name) {
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


}

//
//
//
