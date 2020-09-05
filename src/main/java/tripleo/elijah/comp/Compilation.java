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
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.stages.translate.TranslateModule;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;

import java.io.File;
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
	private final Map<String, OS_Package> _packages = new HashMap<String, OS_Package>();
	private int _packageCode = 1;

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
				CommandLine cmd = clp.parse(options, args.toArray(new String[0]));

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

				for (int i = 0; i < args2.length; i++)
					doFile(new File(args2[i]), errSink, do_out);

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

	public void doFile(@NotNull File f, ErrSink errSink, boolean do_out) throws Exception {
		if (f.isDirectory()) {
			String[] files = f.list();
			for (int i = 0; i < files.length; i++)
				doFile(new File(f, files[i]), errSink, do_out); // recursion, backpressure

		} else {
			final String file_name = f.toString();
			final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
								 || Pattern.matches(".+\\.elijjah$", file_name);
//			if (f.isDirectory()) return; // TODO testing idea tools (failed)
			if (!matches) return;
			//
			System.out.println((String.format("   %s", f.getAbsolutePath())));
			if (f.exists()) {
				OS_Module m = parseFile(file_name, io.readFile(f), f, do_out);
				m.prelude = this.findPrelude("c"); // TODO we dont know which prelude to find yet
			} else {
				errSink.reportError(
						"File doesn't exist " + f.getAbsolutePath());
			}
		}
	}

	public OS_Module parseFile(String f, InputStream s, File file, boolean do_out) throws Exception {
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
				return parseFile(local_prelude.getName(), io.readFile(local_prelude), local_prelude, false);
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
