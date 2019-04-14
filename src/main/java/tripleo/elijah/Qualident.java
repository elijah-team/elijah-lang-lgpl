/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Mar 27, 2019 at 2:24:09 PM
 *
 */
package tripleo.elijah;

import java.util.ArrayList;
import java.util.List;

import antlr.Token;

/**
 * @author SBUSER
 *
 */
public class Qualident {

	public void append(Token r1) {
		parts.add(r1);		
	}
	public void appendDot(Token d1) {
//		parts.add(d1);
	}
	
	List<Token> parts = new ArrayList<Token>();

	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (Token part : parts) {
			sb.append(part.getText());
			sb.append('.');
		}
		String s = sb.toString();
		String substring = s.substring(0, s.length() - 1);
		return substring;
	}
}
