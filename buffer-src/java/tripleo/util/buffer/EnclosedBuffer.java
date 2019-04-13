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

	public void setPayload(BufferSequenceBuilder sb3) {
		// TODO Auto-generated method stub
		_payload = sb3.build(); // TODO lazy or not?
	}

}
