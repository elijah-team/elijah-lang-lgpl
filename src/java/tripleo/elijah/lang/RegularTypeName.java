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

import tripleo.elijah.Qualident;
import tripleo.elijah.util.NotImplementedException;

public class RegularTypeName implements TypeName {

@Override
public boolean isNull() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return false;
}

@Override
public boolean getConstant() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return false;
}

@Override
public void setConstant(boolean aFlag) {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	
}

@Override
public boolean getReference() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return false;
}

@Override
public void setReference(boolean aFlag) {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	
}

@Override
public boolean getOut() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return false;
}

@Override
public void setOut(boolean aFlag) {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	
}

@Override
public boolean getIn() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return false;
}

@Override
public void setIn(boolean aFlag) {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
}

@Override
public String getName() {
	// TODO Auto-generated method stub
	NotImplementedException.raise();
	return null;
}

@Override
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


private TypeModifiers tm;
private Qualident typeName;

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
	// TODO Auto-generated method stub
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
}

