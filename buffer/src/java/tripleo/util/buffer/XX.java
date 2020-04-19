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
package tripleo.util.buffer;

import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo(acer)
 *
 */
public enum XX {
	SPACE, // ( " "),
	LPAREN, RPAREN, // ( ")"),

	COMMA // ( ",");
	, INDENT
	// String value;
	, LBRACE

	, RBRACE;

	//
	
	@Override
	public String toString() {
		return getText();
	}
	
	public String getText() {
		// TODO Auto-generated method stub
		if (this == SPACE) {
			return " ";
		} else if (this == LPAREN) {
			return "(";
		} else if (this == RPAREN) {
			return ")";
		} else if (this == LBRACE) {
			return "{";
		} else if (this == RBRACE) {
			return "}";
		} else if (this == COMMA) {
			return ",";
		} else {
			NotImplementedException.raise();
			return null;
		}
	}

}

//
//
//
