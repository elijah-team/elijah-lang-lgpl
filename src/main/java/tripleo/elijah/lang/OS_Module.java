/*
 * Created on Aug 30, 2005 8:21:52 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.*;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.TabbedOutputStream;

public class OS_Module implements OS_Element {

	public void add(ModuleItem aStatement) {
		items.add(aStatement);
	}
	
	public void finish(TabbedOutputStream tos) throws IOException {
		tos.close();
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Module print_osi");
		if (packageName != null) {
			tos.put_string("package ");
			tos.put_string_ln(packageName);
			tos.put_string_ln("");
		}
		tos.put_string_ln("//");
		synchronized (items) {
			for (ModuleItem element : items)
				element.print_osi(tos);

		}
	}

	public List<ModuleItem> items=new ArrayList<ModuleItem>();

	public String packageName;

	public String moduleName="default";

	public void visitGen(JavaCodeGen visit) {
		visit.addModule(this);
	}
}
