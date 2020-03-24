/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

public class Context {

	private OS_Container attached;

	public Context() {
	}
	
	LookupResultList lookup(String name) {
		final LookupResultList Result = new LookupResultList();
		int level = 0;
		
		for (OS_Element i : attached.items()) {
			if (i.name().equals(name)) {
//				Result.add (i, i.sig, level);
			}
		}
		
//		lookup(name, attached, parent, Result ,level+1);
		return Result;
	}
	
	void lookup(String name, OS_Container attached, OS_Element2 parent, LookupResultList Result, int level) {
		throw new NotImplementedException();
	}
}

//
//
//
