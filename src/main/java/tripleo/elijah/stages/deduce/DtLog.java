/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/3/21 3:46 AM
 */
public class DtLog {
	private final String fileName;
	private final boolean verbose;
	private final List<LogEntry> entries = new ArrayList<>();

	public enum Verbosity {
		SILENT, VERBOSE
	}

	enum Level {
		INFO, ERROR
	}

	public class LogEntry {
		public long time;
		public Level level;
		public String message;

		public LogEntry(long aTime, Level aLevel, String aS) {
			time = aTime;
			level = aLevel;
			message = aS;
		}
	}

	public DtLog(String aFileName, boolean aVerbose) {
		fileName = aFileName;
		verbose = aVerbose;
	}

	public void err(String aMessage) {
		long time = System.currentTimeMillis();
		entries.add(new LogEntry(time, Level.ERROR, aMessage));
		if (verbose)
			System.err.println(aMessage);
	}

	public void info(String aMessage) {
		long time = System.currentTimeMillis();
		entries.add(new LogEntry(time, Level.INFO, aMessage));
		if (verbose)
			System.out.println(aMessage);
	}

	public String getFileName() {
		return fileName;
	}

	public List<LogEntry> getEntries() {
		return entries;
	}
}

//
//
//
