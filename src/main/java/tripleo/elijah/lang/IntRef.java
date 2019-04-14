/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

// Referenced classes of package pak:
//			AbstractExpression

public class IntRef extends AbstractBinaryExpression {

	public IntRef(int n) {
		value = n;
	}

	@Override
	public String repr_() {
		return (new StringBuilder("IntRef (")).append(
				(new Integer(value)).toString()).append(")").toString();
	}

	public int value;
}
