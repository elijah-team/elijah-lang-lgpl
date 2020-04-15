/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 4:55:12 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

public class VariableTypeName extends AbstractTypeName implements TypeName {

	private TypeName genericPart = null;

	@Override
	public TypeName returnValue() {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
//		return null;
	}

	public void type(int aI) {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
	}

	@Override
	public TypeNameList argList() {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
//		return null;
	}

	@Override
	public void set(TypeModifiers aModifiers) {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
	}

	@Override
	public void addGenericPart(TypeName tn2) {
		genericPart = tn2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/*@ requires pr_name != null; */
	@Override
	public String toString() {
		String result;
		if (genericPart != null) {
			result = String.format("%s[%s]", pr_name.toString(), genericPart.toString());
		} else {
			result = pr_name.toString();
		}
		return result;
	}

	@Override
	public void typeName(Qualident xy) {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
	}

	@Override
	public void typeof(Qualident xyz) {
		throw new NotImplementedException();
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
	}
	
	@Override
	public void setGeneric(boolean value) {
		throw new NotImplementedException();
//		NotImplementedException.raise();
	}
	
}

//
//
//
