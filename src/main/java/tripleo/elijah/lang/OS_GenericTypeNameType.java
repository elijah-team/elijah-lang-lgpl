/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.contexts.ClassContext;

/**
 * Created 7/8/21 6:00 AM
 */
public class OS_GenericTypeNameType extends OS_Type {

	private final ClassContext.OS_TypeNameElement genericTypename;

	public OS_GenericTypeNameType(ClassContext.OS_TypeNameElement aGenericTypename) {
		super(Type.GENERIC_TYPENAME);
		genericTypename = aGenericTypename;
	}

	@Override
	public OS_Element getElement() {
		return genericTypename;
	}
}

//
//
//
