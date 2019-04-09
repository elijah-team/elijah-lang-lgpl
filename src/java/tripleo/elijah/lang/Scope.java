/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			StatementClosure, BlockStatement

public interface Scope {

	public abstract void statementWrapper(IExpression aExpr);

	public abstract void addDocString(String s);

	public abstract StatementClosure statementClosure();

	public abstract BlockStatement blockStatement();

	public abstract void add(StatementItem aItem);
}
