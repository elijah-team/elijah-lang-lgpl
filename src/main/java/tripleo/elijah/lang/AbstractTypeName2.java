/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import java.util.*;

/*
 * Created on 5/4/2019 at 12:25
 *
 * $$Id$
 *
 */
public abstract class AbstractTypeName2 implements TypeName {
	
	protected Collection<TypeModifiers> _ltm = new LinkedHashSet<TypeModifiers>();
	protected TypeModifiers tm;
	protected Qualident typeName;
	
	public boolean isNull() {
		//return tm == null && (typeName == null /*|| typeName.isNull()*/);
		return _ltm.isEmpty() && typeName == null; // TODO check for correctness
	}
	
	public boolean getConstant() {
		return _ltm.contains(TypeModifiers.CONST);
	}
	
	public void setConstant(boolean aFlag) {
		_ltm.add(TypeModifiers.CONST);
	}
	
	public boolean getReference() {
		return _ltm.contains(TypeModifiers.REFPAR);
	}
	
	public void setReference(boolean aFlag) {
		_ltm.add(TypeModifiers.REFPAR);
	}
	
	public boolean getOut() {
		return _ltm.contains(TypeModifiers.OUTPAR);
	}
	
	public void setOut(boolean aFlag) {
		_ltm.add(TypeModifiers.OUTPAR);
	}
	
	public boolean getIn() {
		return _ltm.contains(TypeModifiers.INPAR);
	}
	
	public void setIn(boolean aFlag) {
		_ltm.add(TypeModifiers.INPAR);
	}
	
}

//
//
//
