/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp.functionality.f202;

import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.stages.logging.LogEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created 8/11/21 6:04 AM
 */
public class DefaultProcessLogEntryBehavior implements ProcessLogEntryBehavior {
	private PrintStream ps;
	private String s1;

	@Override
	public void processLogEntry(LogEntry entry) {
		final String logentry = String.format("[%s] [%tD %tT] %s %s", s1, entry.time, entry.time, entry.level, entry.message);
		ps.println(logentry);
	}

	@Override
	public void initialize(File psf, String aS1, ErrSink aErrSink) {
		try {
			ps = new PrintStream(psf);
			s1 = aS1;
		} catch (FileNotFoundException exception) {
			aErrSink.exception(exception);
		}
	}

	@Override
	public void start() {

	}

	@Override
	public void finish() {

	}
}

//
//
//
