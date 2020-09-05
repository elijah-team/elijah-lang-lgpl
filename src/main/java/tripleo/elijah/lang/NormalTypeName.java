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

public interface NormalTypeName extends TypeName, Resolvable {

	@Override
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

	/*@ requires modifiers = TypeModifiers.PROCEDURE
			  || modifiers = TypeModifiers.FUNCTION; */
//	@Override
//	void type(TypeModifiers modifiers);

	// TODO new
	void addGenericPart(TypeName tn2);

    void setNullable();

	@Override
	void setContext(Context cur);

	Collection<TypeModifiers> getModifiers();
}

//
//
//
