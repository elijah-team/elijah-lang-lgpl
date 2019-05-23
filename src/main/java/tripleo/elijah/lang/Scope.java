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

import antlr.Token;

public interface Scope {

	void statementWrapper(IExpression aExpr);

	void addDocString(Token s);

	StatementClosure statementClosure();

	BlockStatement blockStatement();

	void add(StatementItem aItem);
	
	TypeAliasExpression typeAlias();
	
	InvariantStatement invariantStatement();
}
