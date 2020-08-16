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

public interface NormalTypeName extends TypeName {

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

	/*@ requires modifiers = TypeModifiers.PROCEDURE
			  || modifiers = TypeModifiers.FUNCTION; */
	void type(TypeModifiers modifiers);

	// TODO new
	void addGenericPart(NormalTypeName tn2);

	// TODO new
	void typeName(Qualident xy);

	// TODO new
	void typeof(Qualident xyz);
	
	void setGeneric(boolean value);

    void setNullable();

	void setContext(Context cur);

	boolean hasResolvedElement();

	OS_Element getResolvedElement();

	void setResolvedElement(OS_Element element);
}

//
//
//
