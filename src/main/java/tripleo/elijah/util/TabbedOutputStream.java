/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.util;

import java.io.*;

public class TabbedOutputStream {
	
	public int t() {
		return tabwidth;
	}
	
	void doIndent()
			throws IOException {
		for (int i = 0; i < tabwidth; i++)
			myStream.write('\t');
		
	}
	
	public TabbedOutputStream(OutputStream os) {
		tabwidth = 0;
		myStream = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	public boolean is_connected() {
		return myStream != null;
	}
	
	public void put_string_ln(String s)
			throws IOException {
		if (!is_connected())
			throw new InvalidObjectException("is_connected assertion failed");
		
		myStream.write(s);
		myStream.write('\n');
		put_newline();
	}
	
	public void put_newline()
			throws IOException {
		doIndent();
	}
	
	public void put_string(String s)
			throws IOException {
		if (!is_connected())
			throw new InvalidObjectException("is_connected assertion failed");
		
		myStream.write(s);
	}
	
	public void quote_string(String s)
			throws IOException {
		if (!is_connected())
			throw new InvalidObjectException("is_connected assertion failed");
		
		myStream.write(34);
		myStream.write(s);
		myStream.write(34);
	}
	
	public void close()
			throws IOException {
		if (!is_connected())
			throw new InvalidObjectException("is_connected assertion failed; closinf twice");
		
		myStream.close();
		myStream = null;
	}
	
	public void incr_tabs() {
		tabwidth++;
	}
	
	public void dec_tabs() {
		tabwidth--;
	}
	
	public static void main(String[] args) {
		TabbedOutputStream tos = new TabbedOutputStream(System.out);
		int i = 0;
		int j = 0;
		try {
			for (; i < 10; i++) {
				tos.put_string_ln((Integer.valueOf(i)).toString());
				tos.incr_tabs();
			}
			
			tos.close();
		} catch (IOException ex) {
			System.out.println("error");
		}
	}
	
	int tabwidth;
	Writer myStream;
	
	public void flush() throws IOException {
		myStream.flush();
	}
	
	public Writer getStream() {
		return myStream;
	}
}
