/*
 * Created on Aug 30, 2005 9:01:37 PM
 * 
 * $Id$
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tripleo.elijah.lang;

import java.util.*;

public class ClassInheritance {

	private ClassStatement parent;

	public ClassInheritance(ClassStatement aStatement) {
		parent=aStatement;
	}

	List<TypeName> tns=new ArrayList<TypeName>();
	
	public TypeName next() {
		TypeName tn = new RegularTypeName();
		tns.add(tn);
		return tn;
	}
}

