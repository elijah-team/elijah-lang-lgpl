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
	SPACE ,//( " "),
	RPAREN,// ( ")"),
	COMMA //( ",");
	;
	//String value;

	public String getText() {
		// TODO Auto-generated method stub
		if (this == SPACE) {
			return " ";
		} else if (this == RPAREN) {
			return ")";
		} else if (this == COMMA) {
			return ",";
		} else
			NotImplementedException.raise();


}
