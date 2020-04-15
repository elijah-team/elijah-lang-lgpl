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
import java.text.SimpleDateFormat;
import java.util.Date;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.ParserClosure;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.util.TabbedOutputStream;

public class Out {

	private final Compilation compilation;

	public Out(String fn, Compilation compilation) {
		pc = new ParserClosure(fn, compilation);
		this.compilation = compilation;
	}
	
	public void FinishModule() {
		TabbedOutputStream tos;
		println("** FinishModule");
		try {
			tos = getTOSLog();
//			tos.put_string_ln(pc.module.getFileName());
//			pc.module.print_osi(tos);
			pc.module.finish();
			//
//			XStream x = new XStream();
//			x.setMode(XStream.ID_REFERENCES);
//			x.toXML(pc.module, tos.getStream());
			//
//			tos.close();
//			final JavaCodeGen visit = new JavaCodeGen();
//			pc.module.visitGen(visit);
			//
			if (compilation.stage.equals("E")) {
				// do nothing. job over
			} else {
				new DeduceTypes(pc.module).deduce();
			}
		} catch (FileNotFoundException fnfe) {
			println("&& FileNotFoundException");
		} catch (IOException ioe) {
			println("&& IOException");
		}
	}

	private static TabbedOutputStream getTOSLog() throws FileNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		final String filename = String.format("eljc-%s.out", sdf.format(new Date()));
		return new TabbedOutputStream(new FileOutputStream(filename));
	}

	public static void println(String s) {
		System.out.println(s);
	}

	private ParserClosure pc;

	public ParserClosure closure() {
		return pc;
	}

	public OS_Module module() {
		return pc.module;
	}
}

//
//
//
