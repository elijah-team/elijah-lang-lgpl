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
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.stages.expand.ExpandFunctions;
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
	private List<OS_Module> modules = new ArrayList<OS_Module>();
	private Map<String, OS_Module> fn2m = new HashMap<String, OS_Module>();

	public Compilation(ErrSink eee, IO io) {
		this.eee = eee;
		this.io  = io;
	}

	public void feedCmdLine(List<String> args) {
		// TODO Auto-generated method stub
		//throw new NotImplementedException();
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
		try {
			if (args.size() > 0) {
				Options options = new Options();
				options.addOption("s", true, "stage: E: parse; O: output");
				options.addOption("showtree", false, "show tree");
				CommandLineParser clp = new DefaultParser();
				CommandLine cmd = clp.parse(options, args.toArray(new String[0]));

				if (cmd.hasOption("s")) {
					stage = cmd.getOptionValue('s');
				}
				if (cmd.hasOption("showtree")) {
					showTree = true;
				}

				final String[] args2 = cmd.getArgs();

				for (int i = 0; i < args2.length; i++)
					doFile(new File(args2[i]), errSink);

				//
				if (stage.equals("E")) {
					// do nothing. job over
				} else {
					for (OS_Module module : modules) {
						new DeduceTypes(module).deduce();
						new ExpandFunctions(module).expand();
					}
				}
//				final JavaCodeGen visit = new JavaCodeGen();
//				module.visitGen(visit);

			} else {
				System.err.println("Usage: eljc [--showtree] [-sE] <directory or file names>");
			}
		} catch (Exception e) {
			errSink.exception(e);
		}
	}

	public void doFile(@NotNull File f, ErrSink errSink) throws Exception {
		if (f.isDirectory()) {
			String[] files = f.list();
			for (int i = 0; i < files.length; i++)
				doFile(new File(f, files[i]), errSink); // recursion, backpressure

		} else {
			final String file_name = f.toString();
			final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
								 || Pattern.matches(".+\\.elijjah$", file_name);
			if (f.isDirectory()) return; // TODO testing idea tools (failed)
			if (!matches) return;
			//
			System.out.println((String.format("   %s", f.getAbsolutePath().toString())));
			if (f.exists()) {
				OS_Module m = parseFile(file_name, io.readFile(f), f);
				m.prelude = this.findPrelude("c"); // TODO we dont know which prelude to find yet
			} else {
				errSink.reportError(
						"File doesn't exist " + f.getAbsolutePath().toString());
			}
		}
	}

	public OS_Module parseFile(String f, InputStream s, File file) throws Exception {
		if (fn2m.containsKey(f)) { // don't parse twice
			return fn2m.get(f);
		}
		try {
			OS_Module R = parseFile_(f, s);
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

	private OS_Module parseFile_(String f, InputStream s) throws RecognitionException, TokenStreamException {
		ElijjahLexer lexer = new ElijjahLexer(s);
		lexer.setFilename(f);
		ElijjahParser parser = new ElijjahParser(lexer);
		parser.out = new Out(f, this);
		parser.setFilename(f);
		parser.program();
		final OS_Module module = parser.out.module();
		parser.out = null;
		return module;
	}

	boolean showTree = false;

	public OS_Element findClass(String string) {
		// TODO Auto-generated method stub
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
				return parseFile(local_prelude.getName(), io.readFile(local_prelude), local_prelude);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

    public OS_Module moduleFor(String fileName) {
        if (fn2m.containsKey(fileName)) {
            return fn2m.get(fileName);
        }
        return null;
    }
}

//
//
//
