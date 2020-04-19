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
import tripleo.elijah.util.NotImplementedException;

import java.util.Objects;

/**
 * @author Tripleo(sb)
 *
 */
public class OS_Type {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OS_Type os_type = (OS_Type) o;
/*		switch (kind) {
			case USER: return (((OS_Type) o).getTypeName()).equals(getTypeName());
			case BUILT_IN: return (((OS_Type) o).type).equals(type);
			case USER_CLASS: return (((OS_Type) o).etype).equals(etype);
			default: throw new IllegalStateException("Cant be here");
		}
*/
		final boolean b = type == os_type.type &&
				kind == os_type.kind &&
				Objects.equals(etype, os_type.etype) &&
				Objects.equals(ttype, os_type.ttype);
		return b;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, kind, etype.hashCode(), ttype.hashCode());
	}

	public ClassStatement getClassOf() {
		if (etype != null && etype instanceof ClassStatement)
			return (ClassStatement) etype;
		throw new NotImplementedException();
//		return null;
	}

	public enum Type {
		BUILT_IN, USER, USER_CLASS
	}

	private BuiltInTypes type;
	private Type kind;
	private OS_Element etype;
	private TypeName ttype;

	/*@ ensures kind = Type.BUILT_IN; */
	public OS_Type(BuiltInTypes aType) {
		this.type = aType;
		this.kind = Type.BUILT_IN;
	}

	/*@ ensures kind = Type.USER_CLASS; */
	public OS_Type(ClassStatement klass) {
		this.etype = klass;
		this.kind = Type.USER_CLASS;
	}

	/*@ ensures kind = Type.USER; */
	public OS_Type(TypeName typeName) {
		this.ttype = typeName;
		this.kind = Type.USER;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("<OS_Type %s %s %s %s>", ttype, etype, type, kind);
	}

	/*@ requires kind = Type.BUILT_IN; */
	public BuiltInTypes getBType() {
		return type;
	}

	/*@ requires kind = Type.USER; */
	public TypeName getTypeName() {
		return ttype;
	}

}

//
//
//
