/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

public class OS_Integer extends AbstractExpression {
	
	@Override
	public String repr_() {
		return String.format("Integer (%d)", getValue());
	}
	
	@Override
	public String toString() {
		return repr_();
	}
	
	private int i;

	public OS_Integer(int i) {
		// TODO Auto-generated constructor stub
		this.i = i;
	}
	
	public int getValue() {
		return i;
	}

	public boolean is_simple() {
		return true;
	}
}
