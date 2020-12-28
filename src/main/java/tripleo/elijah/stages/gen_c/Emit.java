/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

/**
 * Created 12/28/20 7:41 AM
 */
public class Emit {
	public static boolean emitting = true;

	public static String emit(String s) {
		if (emitting)
			return s;
		else
			return "";
	}
}

//
//
//
