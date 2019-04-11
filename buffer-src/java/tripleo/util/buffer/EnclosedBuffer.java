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
		// TODO Auto-generated constructor stub
		_left=left;
		_right=right;
	}

	public void setPayload(BufferSequenceBuilder sb3) {
		// TODO Auto-generated method stub
		_payload = sb3.build(); // TODO lazy or not?
	}

}
