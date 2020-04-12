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
	public TypeName typeName(String aS) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

	@Override
	public TypeName typeof(String aS) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

	@Override
	public TypeName returnValue() {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

	public void type(int aI) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public TypeNameList argList() {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

	@Override
	public void set(TypeModifiers aModifiers) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public void addGenericPart(TypeName tn2) {
		genericPart = tn2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (genericPart != null)
			return String.format("%s[%s]", pr_name.toString(), genericPart.toString());
		return pr_name.toString();
	}

	@Override
	public void typeName(Qualident xy) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public void typeof(Qualident xyz) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}
	
	@Override
	public void setGeneric(boolean value) {
		NotImplementedException.raise();
	}
	
}

