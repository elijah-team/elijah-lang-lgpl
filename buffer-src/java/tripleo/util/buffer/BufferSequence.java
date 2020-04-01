/**
 * 
 */
package tripleo.util.buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 31, 2020 at 7:07:40 PM
 */
public class BufferSequence {

	private List<Buffer> parts = new ArrayList<Buffer>();

	public void add(Buffer element) {
		parts.add(element);
	}

	public String getText() {
//		if (_built != null)
//			return _built;
//		//
		StringBuffer sb = new StringBuffer();
		for (Buffer element : parts) {
			sb.append(element.getText());
		}
		return sb.toString();
	}

}

//
//
//
