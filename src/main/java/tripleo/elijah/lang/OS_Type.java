/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */
/**
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.lang2.BuiltInTypes;

/**
 * @author Tripleo(sb)
 *
 */
public class OS_Type {

	public enum Type {
		BUILT_IN, USER
	}

	private BuiltInTypes type;
	private Type kind;
	private OS_Element etype;
	private TypeName ttype;
	
	public OS_Type(BuiltInTypes aType) {
		this.type = aType;
		this.kind = Type.BUILT_IN;
	}

	public OS_Type(ClassStatement klass, Type user) {
		this.etype = klass;
		this.kind = user;
	}

	public OS_Type(TypeName typeName) {
		this.ttype = typeName;
		this.kind = Type.USER;
	}

}

//
//
//
