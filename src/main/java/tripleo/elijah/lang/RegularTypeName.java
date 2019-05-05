/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 9:05:24 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

public class RegularTypeName extends AbstractTypeName2 implements TypeName {
	
	public String getName() {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

	public void setName(String aS) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	public void set(int aI) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

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
	
	
@Override
public void type(TypeModifiers atm) {
tm=atm;		
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
	// TODO Should this be TypeNameList?
	NotImplementedException.raise();

}

@Override
public void typeName(Qualident xy) {
	// TODO Auto-generated method stub
//	NotImplementedException.raise();
	this.typeName = xy;
	
}

@Override
public void typeof(Qualident xyz) {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	
}
	
//	@Override
	public String getTypeName() {
		return this.typeName.toString();
	}  // TODO is this right?
	
//	@Override
	public void print_osi(TabbedOutputStream aTos) {
		NotImplementedException.raise();
	}
}

