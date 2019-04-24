/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.OS_Ident;

/**
 * @author olu
 *
 */
public class ArgumentNode {
	
	private final TypeRef _typeRef;
	private OS_Ident ident;
	private String genType;
	private String _varName;

//	public ArgumentNode(OS_Ident ident) {
//		// TODO Auto-generated constructor stub
//		this.ident = ident;
//		_typeRef = null;
//	}
//
//	/**
//	 * @param _varName
//	 * @param genType
//	 */
//	public ArgumentNode(String _varName, String genType) {
//		super();
//		this._varName = _varName;
//		this.genType = genType;
//		_typeRef = null;
//	}

	public ArgumentNode(String varName, TypeRef genType) {
		super();
		this.setVarName(varName);
		this._typeRef = genType;
	}
	
	public TypeRef getTypeRef() {
		return _typeRef;
	}
	
	public OS_Ident getIdent() {
		return ident;
	}
	
	public void setIdent(OS_Ident ident) {
		this.ident = ident;
	}
	
	public String getGenType() {
		return _typeRef.genType();
	}
	
	public void setGenType(String genType) {
		this.genType = genType;
	}
	
	public String getVarName() {
		return _varName;
	}
	
	public void setVarName(String _varName) {
		this._varName = _varName;
	}
	
	public String getGenName() {
		return String.format("va%s", getVarName());
	}
}

//
//
//
