/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.util.buffer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author olu
 *
 */
public class FileBackedBuffer implements Buffer {

	Buffer backing=new DefaultBuffer(""); // TODO bad api
	private String fn;

	@Override
	public void finalize() {
		dispose();
	}
	
	public void dispose() {
		try {
			final FileOutputStream fileOutputStream = new FileOutputStream(fn);
			fileOutputStream.write(backing.toString().getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileBackedBuffer(String fn) {
		this.fn=fn;
	}

	@Override
	public void append(String string) {
		backing.append(string);
	}

	@Override
	public void append_s(String string) {
		backing.append_s(string);
	}

	@Override
	public void append_cb(String string) {
		backing.append_cb(string);
	}

	@Override
	public void decr_i() {
		backing.decr_i();
	}
	
	@Override
	public void incr_i() {
		backing.incr_i();
	}
	
	@Override
	public void append_nl_i(String string) {
		// TODO Auto-generated method stub
		backing.append_nl_i(string);
	}

	@Override
	public void append_nl(String string) {
		// TODO Auto-generated method stub
		backing.append_nl(string);
	}

	@Override
	public void append_ln(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
}
