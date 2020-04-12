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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.ParserClosure;
import tripleo.elijah.stages.deduce.DeduceTypes;
import tripleo.elijah.util.TabbedOutputStream;

public class Out {
	
	public Out(String fn, Compilation compilation) {
		pc = new ParserClosure(fn, compilation);
	}
	
	public void FinishModule() {
		TabbedOutputStream tos;
		println("** FinishModule");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			final String filename = String.format("eljc-%s.out", sdf.format(new Date())
					/*Calendar.getInstance().get(Calendar.YEAR),
					Calendar.getInstance().get(Calendar.MONTH),
					Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
					Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
					Calendar.getInstance().get(Calendar.MINUTE),
					Calendar.getInstance().get(Calendar.SECOND)*/);
			tos = new TabbedOutputStream(new FileOutputStream(filename));
			tos.put_string_ln(pc.module.getFileName());
//			pc.module.print_osi(tos);
			pc.module.finish(tos);
			//
//			XStream x = new XStream();
//			x.setMode(XStream.ID_REFERENCES);
//			x.toXML(pc.module, tos.getStream());
			//
//			final JavaCodeGen visit = new JavaCodeGen();
//			pc.module.visitGen(visit);
			//
			new DeduceTypes(pc.module).deduce();
		} catch (FileNotFoundException fnfe) {
			println("&& FileNotFoundException");
		} catch (IOException ioe) {
			println("&& IOException");
		}
	}

	public void println(String s) {
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
