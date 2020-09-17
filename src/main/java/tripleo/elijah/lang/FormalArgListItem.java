/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

public class FormalArgListItem implements OS_Element, OS_Element2 {
	
	public IdentExpression name;
	public TypeName tn=null;

	public TypeName typeName() {
		return tn;
	}

	public void setName(IdentExpression s) {
		name=s;
	}

    @Override // OS_Element
    public void visitGen(ICodeGen visit) {
        throw new NotImplementedException();
    }

    @Override // OS_Element
    public OS_Element getParent() {
        throw new NotImplementedException();
//        return null;
    }

    @Override // OS_Element
    public Context getContext() {
        throw new NotImplementedException();
//        return null;
    }

	@Override // OS_Element2
	public String name() {
		return name.getText();
	}

	public IdentExpression getNameToken() {
		return name;
	}

	public void setTypeName(TypeName tn1) {
		tn = tn1;
	}
}

//
//
//
