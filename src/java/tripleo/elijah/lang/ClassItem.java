/**
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.TabbedOutputStream;

public interface ClassItem extends OS_Element {

	void print_osi(TabbedOutputStream aTos) throws IOException;

	void visit(JavaCodeGen gen);

}
