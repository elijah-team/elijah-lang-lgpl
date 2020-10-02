/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang2;

import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 10/2/20 10:16 AM
 */
public class SpecialFunctions {
	public static String of(ExpressionKind kind) {
		switch (kind) {
		case LT_: return "__lt__";
		case INCREMENT: return "__preinc__";
		default: throw new NotImplementedException();
		}
	}
}

//
//
//
