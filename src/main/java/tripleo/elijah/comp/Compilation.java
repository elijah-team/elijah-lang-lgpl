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
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.stages.translate.TranslateModule;
import tripleo.elijah.util.Helpers;
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
	public final List<OS_Module> modules = new ArrayList<OS_Module>();
	private final Map<String, OS_Module> fn2m = new HashMap<String, OS_Module>();
	private final Map<String, CompilerInstructions> fn2ci = new HashMap<String, CompilerInstructions>();
	private final Map<String, OS_Package> _packages = new HashMap<String, OS_Package>();
	private int _packageCode = 1;
	public final List<CompilerInstructions> cis = new ArrayList<CompilerInstructions>();

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
					final String file_name = args2[i];
					final File f = new File(file_name);
					final boolean matches2 = Pattern.matches(".+\\.ez$", file_name);
					if (matches2)
						add_ci(parseEzFile(f, file_name, eee));
					else {
//						eee.reportError("9996 Not an .ez file "+file_name);
						if (f.isDirectory()) {
							List<CompilerInstructions> ezs = searchEzFiles(f);
							if (ezs.size() > 1) {
								eee.reportError("9998 Too many .ez files, using first.");
								add_ci(ezs.get(0));
							} else if (ezs.size() == 0) {
								eee.reportError("9999 No .ez files found.");
							} else {
								add_ci(ezs.get(0));
							}
						} else
							eee.reportError("9995 Not a directory "+f.getAbsolutePath());
					}
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

	private List<CompilerInstructions> searchEzFiles(File directory) {
		final List<CompilerInstructions> R = new ArrayList<CompilerInstructions>();
		final FilenameFilter f = new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				final boolean matches2 = Pattern.matches(".+\\.ez$", s);
				return matches2;
			}
		};
		final String[] list = directory.list(f);
		if (list != null) {
			for (String file_name : list) {
				try {
					final File file = new File(directory, file_name);
					final CompilerInstructions ezFile = parseEzFile(file, file.toString(), eee);
					if (ezFile != null)
						R.add(ezFile);
					else
						eee.reportError("9995 ezFile is null "+file.toString());
				} catch (Exception e) {
					eee.exception(e);
				}
			}
		}
		return R;
	}

	private void add_ci(CompilerInstructions ci) {
		cis.add(ci);
	}

	private void use(CompilerInstructions compilerInstructions, boolean do_out) throws Exception {
		final File instruction_dir = new File(compilerInstructions.getFilename()).getParentFile();
		for (LibraryStatementPart lsp : compilerInstructions.lsps) {
			String dir_name = Helpers.remove_single_quotes_from_string(lsp.getDirName());
			File dir = new File(dir_name);
			if (dir_name.equals(".."))
				dir = instruction_dir/*.getAbsoluteFile()*/.getParentFile();
			use_internal(dir, do_out);
		}
		use_internal(instruction_dir, do_out);
	}

	private void use_internal(File dir, boolean do_out) throws Exception {
		if (!dir.isDirectory()) {
			eee.reportError("9997 Not a directory " + dir.toString());
			return;
		}
		//
		final FilenameFilter accept_source_files = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String file_name) {
				final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
						             || Pattern.matches(".+\\.elijjah$", file_name);
				return matches;
			}
		};
		final File[] files = dir.listFiles(accept_source_files);
		if (files != null) {
			for (File file : files) {
				parseElijjahFile(file, file.toString(), eee, do_out);
			}
		}
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
			OS_Module m = realParseElijjahFile(file_name, f, do_out);
			m.prelude = this.findPrelude("c"); // TODO we dont know which prelude to find yet
		} else {
			errSink.reportError(
					"File doesn't exist " + f.getAbsolutePath());
		}
	}

	public OS_Module realParseElijjahFile(String f, File file, boolean do_out) throws Exception {
		if (fn2m.containsKey(f)) { // don't parse twice
			return fn2m.get(f);
		}
		final InputStream s = io.readFile(file);
		try {
			OS_Module R = parseFile_(f, s, do_out);
			fn2m.put(f, R);
			s.close();
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
			R.setFilename(file.toString());
			fn2ci.put(f, R);
			s.close();
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

	public List<ClassStatement> findClass(String string) {
		List<ClassStatement> l = new ArrayList<ClassStatement>();
		for (OS_Module module : modules) {
			if (module.hasClass(string)) {
				l.add((ClassStatement) module.findClass(string));
			}
		}
		return l;
	}

	public int errorCount() {
		return eee.errorCount();
	}

	public OS_Module findPrelude(String prelude_name) {
		File local_prelude = new File("lib_elijjah/lib-"+prelude_name+"/Prelude.elijjah");
		if (local_prelude.exists()) {
			try {
				return realParseElijjahFile(local_prelude.getName(), local_prelude, false);
			} catch (Exception e) {
				eee.exception(e);
				return null;
			}
		}
		return null;
	}

	public boolean findStdLib(String prelude_name) {
		File local_stdlib = new File("lib_elijjah/lib-"+prelude_name+"/stdlib.ez");
		if (local_stdlib.exists()) {
			try {
				CompilerInstructions ci = realParseEzFile(local_stdlib.getName(), io.readFile(local_stdlib), local_stdlib);
				add_ci(ci);
			} catch (Exception e) {
				eee.exception(e);
				return true;
			}
		}
		return false;
	}

	//
	// MODULE STUFF
	//

	public void addModule(OS_Module module, String fn) {
		modules.add(module);
		fn2m.put(fn, module);
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

	private int _classCode = 101;
	private int _functionCode = 1001;

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
