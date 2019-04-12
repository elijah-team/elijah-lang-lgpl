/**
 * 
 */
package tripleo.util.buffer;

import tripleo.elijah.util.NotImplementedException;

/**
 * @author olu
 *
 */
public class DefaultBuffer implements Buffer {

	public DefaultBuffer(String string) {
		append(string);
	}

	/* (non-Javadoc)
	 * @see tripleo.util.buffer.IBuffer#append(java.lang.String)
	 */
	@Override
	public void append(String string) {
		text.append(string);
	}

	/**
	 * Append string with space
	 * 
	 * @see tripleo.util.buffer.IBuffer#append_s(java.lang.String)
	 */
	@Override
	public void append_s(String string) {
		text.append(string);
		text.append(" ");
	}

	/**
	 * Appemd string with closing brace
	 * 
	 * @see tripleo.util.buffer.IBuffer#append_cb(java.lang.String)
	 */
	@Override
	public void append_cb(String string) {
		// TODO Auto-generated method stub
		text.append(string);
		text.append("}");
	}

	StringBuilder text = new StringBuilder();
	private int incr=0;

	@Override
	public void decr_i() {
		// TODO Auto-generated method stub
		incr--;
	}

	@Override
	public void append_nl_i(String string) {
		// TODO Auto-generated method stub
		text.append(string);
		text.append("\n");
		text.append(new_String('\t', incr));

	}

	private String new_String(char c, int incr2) {
		// TODO Auto-generated method stub
		StringBuilder s=new StringBuilder(incr2);
		while (incr2-->0) s.append(('\t'));
		return s.toString();
	}

	@Override
	public void append_nl(String string) {
		text.append(string);
		text.append("\n");
	}

	public void append_s(String genType, XX space) {
		// TODO Auto-generated method stub
		
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
