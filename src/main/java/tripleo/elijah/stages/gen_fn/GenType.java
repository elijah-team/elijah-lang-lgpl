/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.deduce.ClassInvocation;

/**
 * Created 5/31/21 1:32 PM
 */
public class GenType {
	OS_Type typeName; // TODO or just TypeName ??
	TypeName nonGenericTypeName;
	OS_Type resolved;
	ClassInvocation ci;
	GeneratedNode node;
}

//
//
//
