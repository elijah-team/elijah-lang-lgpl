/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			TypeNameList

public interface TypeName {

	boolean isNull();

	boolean getConstant();
	void setConstant(boolean flag);

	boolean getReference();
	void setReference(boolean flag);

	boolean getOut();
	void setOut(boolean flag);

	boolean getIn();
	void setIn(boolean flag);

	String getName();
	void setName(Qualident s);

	void set(TypeModifiers aModifiers);

	TypeName returnValue();

	/*@ requires modifiers = TypeModifiers.PROCEDURE
			  || modifiers = TypeModifiers.FUNCTION; */
	void type(TypeModifiers modifiers);

	TypeNameList argList();

	// TODO new
	void addGenericPart(TypeName tn2);

	// TODO new
	void typeName(Qualident xy);

	// TODO new
	void typeof(Qualident xyz);
	
	void setGeneric(boolean value);

    void setNullable();

//	public static final int NORMAL = 0;
//	public static final int CONST = 1;
//	public static final int GC = 2;
//	public static final int TAGGED = 3;
//	public static final int POOLED = 4;
//	public static final int MANUAL = 5;
//	public static final int LOCAL = 6;
//	public static final int ONCE = 7;
//	public static final int PROCEDURE = 32;
//	public static final int FUNCTION = 33;

}

//
//
//
