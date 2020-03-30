/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.NamespaceStatement;

/**
 * @author Tripleo
 *
 * Created 	Mar 29, 2020 at 8:59:42 PM
 */
public class NamespaceContext extends Context {

	private NamespaceStatement carrier;

	public NamespaceContext(NamespaceStatement namespaceStatement) {
		carrier = namespaceStatement;
	}

}
