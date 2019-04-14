/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah;

import java.io.*;

import antlr.ANTLRException;

public class Main {

	public static void main(String args[]) {
		try {
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++)
					if (args[i].equals("-showtree"))
						showTree = true;
					else
						doFile(new File(args[i]));

			} else {
				System.err
						.println("Usage: java Main [-showtree] <directory or file name>");
			}
		} catch (Exception e) {
			System.err.println((new StringBuilder("exception: ")).append(e)
					.toString());
			e.printStackTrace(System.err);
		}
	}

	public static void doFile(File f) throws Exception {
		var EXTENSION = ".elijah";
		
		if (f.isDirectory()) {
			String files[] = f.list();
			for (int i = 0; i < files.length; i++)
				doFile(new File(f, files[i]));

		} else if (f.getName().length() > EXTENSION.length()
				&& f.getName().substring(f.getName().length() - EXTENSION.length())
						.equals(EXTENSION)) {
			System.out.println((new StringBuilder("   ")).append(
					f.getAbsolutePath()).toString());
			parseFile(f.getName(), new FileInputStream(f));
		}
	}

	public static void parseFile(String f, InputStream s) throws Exception {
		try {
			ElijahLexer lexer = new ElijahLexer(s);
			lexer.setFilename(f);
			ElijahParser parser = new ElijahParser(lexer);
			parser.out = new Out();
			parser.setFilename(f);
			parser.program();
		} catch (ANTLRException e) {
			System.err.println(("parser exception: "+e));
			e.printStackTrace();
		}
	}

	static boolean showTree = false;

}

//
//
//
