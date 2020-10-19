/*
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/* Created on Aug 30, 2005 9:01:37 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.List;

public class ClassInheritance {

	private final ClassStatement parent;

	public ClassInheritance(final ClassStatement aStatement) {
		parent=aStatement;
	}

	public List<TypeName> tns=new ArrayList<TypeName>();
	
//	public NormalTypeName next() {
//		NormalTypeName tn = new RegularTypeName();
//		tn.setContext(parent.getContext());
//		tns.add(tn);
//		return tn;
//	}

	public void add(final TypeName tn) {
//		tn.setContext(parent.getContext());
		tns.add(tn);
	}
}

