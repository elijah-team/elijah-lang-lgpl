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
import java.util.List;

import antlr.ANTLRException;
import tripleo.elijah.ElijahLexer;
import tripleo.elijah.ElijahParser;
import tripleo.elijah.Out;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;

public class Compilation {

	private IO io;
	private ErrSink eee;

	public Compilation(ErrSink eee, IO io) {
		// TODO Auto-generated constructor stub
		this.eee = eee;
		this.setIO(io);
				
//		throw new NotImplementedException();
	}

	public void feedCmdLine(List<String> args) {
		// TODO Auto-generated method stub
		//throw new NotImplementedException();
		main(args);
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
	
	public void main(List<String> args) {
		StdErrSink errSink = new StdErrSink();
		try {
			if (args.size() > 0) {
				for (int i = 0; i < args.size(); i++)
					if (args.get(i).equals("-showtree"))
						showTree = true;
					else if (args.get(i).charAt(0) == '-') {
						NotImplementedException.raise();
					} else {
						doFile(new File(args.get(i)));
					}
			} else {
				System.err.println("Usage: eljc [-showtree] <directory or file name>");
			}
		} catch (Exception e) {
			errSink.exception(e);
		}
	}

	public static void doFile(File f) throws Exception {
		final String EXTENSION = ".elijah";
		
		if (f.isDirectory()) {
			String[] files = f.list();
			for (int i = 0; i < files.length; i++)
				doFile(new File(f, files[i]));

		} else {
			final String file_name = f.getName();
			int fnl = file_name.length();
			if (fnl > EXTENSION.length()
					&& file_name.substring(fnl - EXTENSION.length())
							.equals(EXTENSION)) {
				System.out.println((new StringBuilder("   ")).append(
						f.getAbsolutePath()).toString());
				parseFile(file_name, new FileInputStream(f));
			}
		}
	}

	public static void parseFile(String f, InputStream s) throws Exception {
		try {
			ElijjahLexer lexer = new ElijjahLexer(s);
			lexer.setFilename(f);
			ElijjahParser parser = new ElijjahParser(lexer);
			parser.out = new Out(f);
			parser.setFilename(f);
			parser.program();
		} catch (ANTLRException e) {
			System.err.println(("parser exception: "+e));
			e.printStackTrace();
		}
	}

	static boolean showTree = false;

}
