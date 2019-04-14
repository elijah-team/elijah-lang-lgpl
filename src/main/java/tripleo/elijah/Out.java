/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah;
 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.lang.ParserClosure;
import tripleo.elijah.util.TabbedOutputStream;

public class Out {

	public void FinishModule() {
		TabbedOutputStream tos;
		println("** FinishModule");
//		tos = null;
		try {
			tos = new TabbedOutputStream(new FileOutputStream("oscc.out"));
			pc.module.print_osi(tos);
			pc.module.finish(tos);
			//
			final JavaCodeGen visit = new JavaCodeGen();
			pc.module.visitGen(visit);
		} catch (FileNotFoundException fnfe) {
			println("&& FileNotFoundException");
		} catch (IOException ioe) {
			println("&& IOException");
		}
//		return;
	}

//	private void print(String s) {
//		System.out.print(s);
//	}
//
//	private void println(int s) {
//		System.out.println(s);
//	}

	public void println(String s) {
		System.out.println(s);
	}

//	private void print3(String s1, String s2, String s3) {
//		System.out.print(s1);
//		System.out.print(s2);
//		System.out.print(s3);
//	}
//
//	private void println(String s1, String s2) {
//		System.out.print(s1);
//		System.out.println(s2);
//	}
//
//	private void printpln(String s1, String s2) {
//		System.out.print(s1);
//		System.out.print('(');
//		System.out.print(s2);
//		System.out.println(')');
//	}

	private ParserClosure pc=new ParserClosure();

	public ParserClosure closure() {
		return pc;
	}

}
