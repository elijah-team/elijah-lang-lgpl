/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;


/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:04:02 AM
 */
public class ClassContext extends Context { // TODO is this right, or should be interface??

	private final ClassStatement carrier;

	public ClassContext(ClassStatement classStatement) {
		carrier = classStatement;
	}

}
