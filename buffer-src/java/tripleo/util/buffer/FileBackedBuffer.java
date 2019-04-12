/**
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

	public void finalize() {
		dispose();
	}
	
	public void dispose() {
		try {
			new FileOutputStream(fn).write(backing.toString().getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FileBackedBuffer(String fn) {
		// TODO Auto-generated constructor stub
		this.fn=fn;
	}

	@Override
	public void append(String string) {
		// TODO Auto-generated method stub
		backing.append(string);
	}

	@Override
	public void append_s(String string) {
		// TODO Auto-generated method stub
		backing.append_s(string);
	}

	@Override
	public void append_cb(String string) {
		// TODO Auto-generated method stub
		backing.append_cb(string);
	}

	@Override
	public void decr_i() {
		// TODO Auto-generated method stub
		backing.decr_i();
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
