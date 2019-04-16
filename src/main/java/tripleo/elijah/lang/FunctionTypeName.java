/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

// Referenced classes of package pak2:
//			AbstractTypeName, TypeNameList, TypeName

public class FunctionTypeName extends AbstractTypeName {

	public FunctionTypeName() {
	}

//	public TypeNameList argList() {
//		return null;
//	}
//
//	public TypeName returnValue() {
//		return null;
//	}
//
//	public TypeName typeName() {
//		return null;
//	}
//
//	public TypeName typeof(String aXy) {
//		return null;
//	}
//
//	public void type(int i) {
//	}
//
//	public TypeName typeName(String aTypeName) {
//		return null;
//	}
//
//	public void set(TypeModifiers aModifiers) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void addGenericPart(TypeName tn2) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void typeName(Qualident xy) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void typeof(Qualident xyz) {
//		// TODO Auto-generated method stub
//		
//	}
	
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
