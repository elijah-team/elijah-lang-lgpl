/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.TabbedOutputStream;

public interface OS_Element {
	void print_osi(TabbedOutputStream aTos) throws IOException;
	void visitGen(ICodeGen visit);
}

//
//
//
