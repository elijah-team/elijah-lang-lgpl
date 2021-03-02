/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import org.jetbrains.annotations.Contract;
import tripleo.elijah.gen.ICodeGen;

public interface OS_Element {
	void visitGen(ICodeGen visit);

	@Contract(pure = true)
	Context getContext();

	@Contract(pure = true)
	OS_Element getParent();
}

//
//
//
