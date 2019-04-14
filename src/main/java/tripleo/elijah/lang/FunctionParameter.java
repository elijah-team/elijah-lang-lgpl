/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

import tripleo.elijah.Qualident;
import tripleo.elijah.util.NotImplementedException;

public class FunctionParameter extends AbstractTypeName implements TypeName {

	public FunctionParameter() {
		pr_constant = false;
		pr_reference = false;
		pr_out = false;
		pr_in = false;
		pr_name = "";
	}

	public TypeName typeof() {
		return null;
	}

	@Override
	public TypeName returnValue() {
		return null;
	}

	public void type(int i) {
	}

	@Override
	public TypeNameList argList() {
		return null;
	}

	@Override
	public TypeName typeof(String aXy) {
		return null;
	}

	@Override
	public TypeName typeName(String aTypeName) {
		return null;
	}

	@Override
	public void set(TypeModifiers aModifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGenericPart(TypeName tn2) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
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
}
