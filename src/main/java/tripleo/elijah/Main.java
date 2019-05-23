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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;

public class Main {

	public static void main(String[] args) {
		StdErrSink errSink = new StdErrSink();
		Compilation cc = new Compilation(errSink, new IO());
		List<String> ls = new ArrayList<String>();
		ls.addAll(Arrays.asList(args));
		cc.main(ls);
//		try {
//			//
//			if (args.length > 0) {
//				for (int i = 0; i < args.length; i++) {
//					if (args[i].equals("-showtree")) {
//						showTree = true;
//					} else {
//						doFile(new File(args[i]), errSink);
//					}
//				}
//
//			} else {
//				System.err.println("Usage: eljc [-showtree] <directory or file name>");
//			}
//		} catch (Exception e) {
//			errSink.exception(e);
//		}
	}
/*
	public static void doFile(File f, StdErrSink aErrSink) throws Exception {
		Compilation c = new Compilation(aErrSink, new IO());
		c.doFile(f);
//		String EXTENSION = ".elijah";
//
//		if (f.isDirectory()) {
//			String[] files = f.list();
//			for (int i = 0; i < files.length; i++)
//				doFile(new File(f, files[i]));
//
//		} else if (f.getName().length() > EXTENSION.length()
//				&& f.getName().substring(f.getName().length() - EXTENSION.length())
//						.equals(EXTENSION)) {
//			System.out.println((new StringBuilder("   ")).append(
//					f.getAbsolutePath()).toString());
//			parseFile(f.getName(), new FileInputStream(f));
//		}
	}

//	public static void parseFile(String f, InputStream s) throws Exception {
//		try {
//			ElijahLexer lexer = new ElijahLexer(s);
//			lexer.setFilename(f);
//			ElijahParser parser = new ElijahParser(lexer);
//			parser.out = new Out(f);  // TODO can we grab this f from parser? DRY...
//			parser.setFilename(f);
//			parser.program();
//		} catch (ANTLRException e) {
//			System.err.println(("parser exception: "+e));
//			e.printStackTrace();
//		}
//	}

	static boolean showTree = false;
*/
}

//
//
//
