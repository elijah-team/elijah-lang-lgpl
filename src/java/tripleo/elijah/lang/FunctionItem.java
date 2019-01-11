/**
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public interface FunctionItem extends OS_Element {

	void print_osi(TabbedOutputStream aTos) throws IOException;

}