/**
 * 
 */
package tripleo.util.buffer;

import tripleo.elijah.util.NotImplementedException;

/**
 * @author olu
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
