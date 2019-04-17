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
import java.util.Calendar;

import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.lang.ParserClosure;
import tripleo.elijah.util.TabbedOutputStream;

public class Out {
	
	public Out(String fn) {
		pc = new ParserClosure(fn);
	}
	
	public void FinishModule() {
		TabbedOutputStream tos;
		println("** FinishModule");
//		tos = null;
		try {
			final String filename = String.format("oscc-%d-%d-%d.out",
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
					Calendar.getInstance().get(Calendar.MINUTE),
					Calendar.getInstance().get(Calendar.SECOND));
			tos = new TabbedOutputStream(new FileOutputStream(filename));
			tos.put_string_ln(pc.module.getFileName());
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

	private ParserClosure pc;

	public ParserClosure closure() {
		return pc;
	}

}
