/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.Loop;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:40:43 PM
 */
public class LoopContext extends Context {

	private final Loop carrier;

	public LoopContext(Loop loop) {
		carrier = loop;
	}

}
