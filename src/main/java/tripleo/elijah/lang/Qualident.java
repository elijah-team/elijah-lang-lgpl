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
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import tripleo.elijah.lang.ExpressionType;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.util.TabbedOutputStream;

/**
 * @author SBUSER
 *
 */
public class Qualident  implements IExpression {

	public void append(Token r1) {
		parts.add(r1);		
	}
	public void appendDot(Token d1) {
//		parts.add(d1);
	}
	
	List<Token> parts = new ArrayList<Token>();

	@Override
	public String toString() {
		final StringBuilder sb=new StringBuilder();
		for (Token part : parts) {
			sb.append(part.getText());
			sb.append('.');
		}
		final String s = sb.toString();
		final String substring = s.substring(0, s.length() - 1);
		return substring;
	}
	
	@Override
	public void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException {
		tabbedoutputstream.put_string_ln(String.format("Qualident (%s)", toString()));
	}
	
	@Override
	public ExpressionType getType() {
		return ExpressionType.QUALIDENT;
	}
	
	@Override
	public void set(ExpressionType aIncrement) {
		throw new IllegalArgumentException(); // TODO is this right?
	}
	
	@Override
	public IExpression getLeft() {
		return this;
	}
	
	/** Not sure what this should do */
	@Override
	public void setLeft(IExpression iexpression) {
		throw new IllegalArgumentException(); // TODO is this right?
	}
	
	@Override
	public String repr_() {
		return String.format("Qualident (%s)", toString());
	}
}
