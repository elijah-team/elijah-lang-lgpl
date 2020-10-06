/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang2;

/**
 * Created 10/6/20 3:20 PM
 */
public class SpecialVariables {
	public static boolean contains(String name) {
		if (name.equals("self")) return true;
		if (name.equals("Result")) return true;
		System.err.println("SpecialVariables: test: "+name);
		return false;
	}
}

//
//
//
