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
 *
 * This class represents all the different type of types in the system possible
 *
 *
 * @author Tripleo(sb)
 *
 */
public class OS_Type {

	public OS_Type() {
	}

	public static boolean isConcreteType(final OS_Element element) {
		if (element instanceof ClassStatement) return true;
		// enum
		// type
		return false;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final OS_Type os_type = (OS_Type) o;
/*		switch (kind) {
			case USER: return (((OS_Type) o).getTypeName()).equals(getTypeName());
			case BUILT_IN: return (((OS_Type) o).type).equals(type);
			case USER_CLASS: return (((OS_Type) o).etype).equals(etype);
			default: throw new IllegalStateException("Cant be here");
		}
*/
		final boolean b = type == os_type.type &&
				type_of_type == os_type.type_of_type &&
				Objects.equals(etype, os_type.etype) &&
				Objects.equals(ttype, os_type.ttype);
		return b;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, type_of_type, etype.hashCode(), ttype.hashCode());
	}

	public ClassStatement getClassOf() {
		if (etype != null && etype instanceof ClassStatement)
			return (ClassStatement) etype;
		System.err.println("3001 "+etype+" "+toString());
		throw new IllegalArgumentException();
//		return null;
	}

	public OS_Element getElement() {
		if (type_of_type != Type.FUNCTION || type_of_type != Type.USER_CLASS)
			throw new IllegalArgumentException();
		return etype;
	}

	public OS_Type resolve(final Context ctx) {
		assert ctx != null;
		switch (getType()) {
		case BUILT_IN:
			{
				//
				// TODO These are technically not right
				//
				switch (getBType()) {
				case SystemInteger:
					{
						final LookupResultList r;
						final OS_Element best;

						r = ctx.lookup("SystemInteger");
						best = r.chooseBest(null);
						return new OS_Type((ClassStatement) best);
					}
				case Boolean:
					{
						final LookupResultList r;
						final OS_Element best;

						r = ctx.lookup("Boolean");
						best = r.chooseBest(null);
						return new OS_Type((ClassStatement) best);
					}
				default:
					throw new NotImplementedException();
				}
			}
		case USER:
			{
				final LookupResultList r = ctx.lookup(getTypeName().toString()); // TODO
				final OS_Element best = r.chooseBest(null);
				return new OS_Type((ClassStatement) best);
			}
		case USER_CLASS:
		case FUNCTION:
			return this;
		default:
			throw new IllegalStateException("can't be here.");
		}
	}

	public enum Type {
		BUILT_IN, USER, USER_CLASS, FUNCTION
	}

	public Type getType() {
		return type_of_type;
	}

	private BuiltInTypes type;
	protected Type type_of_type;
	private OS_Element etype;
	private TypeName ttype;

	/*@ ensures type_of_type = Type.BUILT_IN; */
	public OS_Type(final BuiltInTypes aType) {
		this.type = aType;
		this.type_of_type = Type.BUILT_IN;
	}

	/*@ ensures type_of_type = Type.USER_CLASS; */
	public OS_Type(final ClassStatement klass) {
		assert klass != null;
		this.etype = klass;
		this.type_of_type = Type.USER_CLASS;
	}

	/*@ ensures type_of_type = Type.USER; */
	public OS_Type(/*Normal*/final TypeName typeName) {
		this.ttype = typeName;
		this.type_of_type = Type.USER;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("<OS_Type %s %s %s %s>", ttype, etype, type, type_of_type);
	}

	/*@ requires type_of_type = Type.BUILT_IN; */
	public BuiltInTypes getBType() {
		return type;
	}

	/*@ requires type_of_type = Type.USER; */
	public TypeName getTypeName() {
		return ttype;
	}

}

//
//
//
