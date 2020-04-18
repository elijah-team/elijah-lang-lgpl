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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import tripleo.util.io.CharSink;
import tripleo.util.io.CharSource;
import tripleo.elijah.FileOption;

public class IO {

	// exists, delete, isType ....

	final List<File> recordedreads  = new ArrayList<File>();
	final List<File> recordedwrites = new ArrayList<File>();
	
	public boolean recordedRead(File file) {
		return recordedreads.contains(file);
	}

	public boolean recordedWrite(File file) {
		return recordedwrites.contains(file);
	}

	public CharSource openRead(Path p) {
		record(FileOption.READ, p);
		return null;
	}

	public CharSink openWrite(Path p) {
		record(FileOption.WRITE, p);				
		return null;
	}

	private void record(FileOption read, @NotNull Path p) {
		record(read, p.toFile());
	}

	private void record(@NotNull FileOption read, @NotNull File file) {
		switch (read) {
			case WRITE:
				recordedwrites.add(file);
				break;
			case READ:
				recordedreads.add(file);
				break;
			default:
				throw new IllegalStateException("Cant be here");
		}
	}

	public InputStream readFile(File f) throws FileNotFoundException {
		record(FileOption.READ, f);
		return new FileInputStream(f);
	}
}

//
//
//
