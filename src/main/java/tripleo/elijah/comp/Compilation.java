/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import antlr.ANTLRException;
import com.thoughtworks.xstream.core.AbstractReferenceMarshaller;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.Out;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;

public class Compilation {

	private IO io;
	private ErrSink eee;
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
			final String file_name = f.getName();
			final boolean matches = Pattern.matches(".+\\.elijah$", file_name);
			if (matches) {
				System.out.println((String.format("   %s", f.getAbsolutePath().toString())));
				if (f.exists()) {
					if (!fn2m.containsKey(f.getAbsolutePath())) // don't parse twice
						parseFile(file_name, io.readFile(f));
				} else
					errSink.reportError(ErrSink.Errors.ERROR,
							"File doesn't exist " + f.getAbsolutePath().toString());
			}
		}
	}

	public void parseFile(String f, InputStream s) throws Exception {
		try {
			ElijjahLexer lexer = new ElijjahLexer(s);
			lexer.setFilename(f);
			ElijjahParser parser = new ElijjahParser(lexer);
			parser.out = new Out(f, this);
			parser.setFilename(f);
			parser.program();
		} catch (ANTLRException e) {
			System.err.println(("parser exception: "+e));
			e.printStackTrace();
		}
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
}

//
//
//
