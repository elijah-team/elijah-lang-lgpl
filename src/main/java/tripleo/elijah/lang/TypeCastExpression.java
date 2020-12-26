/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

/**
 * @author olu
 *
 * Created 	Apr 16, 2020 at 8:42:54 AM
 */
public class TypeCastExpression extends AbstractExpression implements IExpression {

    @Override
    public boolean is_simple() {
        return false;
    }

    @Override
    public void setType(final OS_Type deducedExpression) {

    }

    @Override
    public OS_Type getType() {
        return null;
    }

	TypeName tn;
	
	public TypeName typeName() {
		return tn;
	}

	public void setTypeName(final TypeName typeName) {
		tn = typeName;
	}

	public TypeName getTypeName() {
		return tn;
	}
}

//
//
//
