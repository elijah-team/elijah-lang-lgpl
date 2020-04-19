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
