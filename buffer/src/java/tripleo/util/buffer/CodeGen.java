/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.util.buffer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo(ecl)
 *
 */
public class CodeGen {

	public void appendHeader(String module, String build) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		FileOutputStream fileOutputStream = null;
		try {
			try {
				fileOutputStream = new FileOutputStream(module + ".h", true); // append
				fileOutputStream.write(build.toString().getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} finally {
			// TODO: handle finally clause
			if (fileOutputStream != null)
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
