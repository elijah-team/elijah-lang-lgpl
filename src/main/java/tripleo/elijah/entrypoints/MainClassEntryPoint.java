/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.entrypoints;

import tripleo.elijah.lang.ClassItem;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;

import java.util.Collection;

/**
 * Created 6/14/21 7:28 AM
 */
public class MainClassEntryPoint extends AbstractEntryPoint {
	private FunctionDef main_function;
	private final ClassStatement klass;

	public MainClassEntryPoint(ClassStatement aKlass) {
		final Collection<ClassItem> main = aKlass.findFunction("main");
		for (ClassItem classItem : main) {
			FunctionDef fd = (FunctionDef) classItem;
			if (fd.getArgs().size() == 0 && fd.returnType().isNull()) {
				main_function = fd;
			}
		}
		if (main_function == null)
			throw new IllegalArgumentException("Class does not define main");
		klass = aKlass;
	}

	public FunctionDef getmainFunction() {
		return main_function;
	}

	public ClassStatement getKlass() {
		return klass;
	}
}

//
//
//
