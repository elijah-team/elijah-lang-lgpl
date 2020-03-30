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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tripleo.elijah.CharSink;
import tripleo.elijah.CharSource;
import tripleo.elijah.FileOption;

public class IO {

	// exists, delete, isType ....

	List<File> recordedreads  = new ArrayList<File>();
	List<File> recordedwrites = new ArrayList<File>();
	
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
	
	private void record(FileOption read, Path p) {
		// TODO Auto-generated method stub
		Map<FileOption, File> options11 = new HashMap<FileOption, File>();
		options11.put(read, p.toFile());

	}
}

//
//
//
