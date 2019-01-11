/**
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.TabbedOutputStream;

public interface ModuleItem {

	void print_osi(TabbedOutputStream aTos) throws IOException;

	void visitGen(JavaCodeGen visit);
}
