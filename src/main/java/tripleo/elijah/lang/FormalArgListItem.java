/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.nodes.Helpers;

public class FormalArgListItem {
	
	Token name;
	TypeName tn=new RegularTypeName(); // TODO why make the choice for the program?

	public TypeName typeName() {
		return tn;
	}

	@Deprecated public void setName(String s) {
		name=Helpers.makeToken(s);
	}

	public void setName(Token s) {
		name=s;
	}
}

//
//
//
