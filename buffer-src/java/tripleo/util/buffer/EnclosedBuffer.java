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

/**
 * @author olu
 *
 */
public class EnclosedBuffer extends DefaultBuffer {

	private String _payload;
	private String _right;
	private String _left;

	public EnclosedBuffer(String left, String right) {
		super("");
		// TODO Auto-generated constructor stub
		_left=left;
		_right=right;
	}

	public EnclosedBuffer(String left, XX right) {
		super("");
		_left=left;
		_right=right.getText();
	}

	public void setPayload(BufferSequenceBuilder sequence) {
		// TODO Auto-generated method stub
		_payload = sequence.build(); // TODO lazy or not?
	}
	
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return //super.toString();
			String.format("%s%s%s", _left, _payload, _right);
	}
}
