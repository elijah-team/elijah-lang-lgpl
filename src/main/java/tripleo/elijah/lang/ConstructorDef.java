/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.Helpers;

/**
 * @author Tripleo
 *
 * Created 	Apr 16, 2020 at 7:34:07 AM
 */
public class ConstructorDef extends FunctionDef {

	@Deprecated public ConstructorDef(final IdentExpression aConstructorName, final ClassStatement aParent) {
		super(aParent);
		if (aConstructorName != null)
			setName(aConstructorName);
		else setName(Helpers.string_to_ident("<>")); // hack for Context#lookup
	}

	public ConstructorDef(final IdentExpression aConstructorName, final ClassStatement aParent, final Context cur) {
		super(aParent, cur);
		if (aConstructorName != null)
			setName(aConstructorName);
		else setName(Helpers.string_to_ident("<>")); // hack for Context#lookup
	}

}

//
//
//
