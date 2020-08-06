/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.Documentable;
import tripleo.elijah.gen.ICodeGen;

// Referenced classes of package pak2:
//			StatementClosure, BlockStatement

public interface Scope extends Documentable, OS_Element {

	void statementWrapper(IExpression aExpr);

	StatementClosure statementClosure();

	BlockStatement blockStatement();

	void add(StatementItem aItem);
	
	TypeAliasExpression typeAlias();
	
	InvariantStatement invariantStatement();
	
	@Override
	default Context getContext() {
		// TODO Auto-generated method stub
		return null;
	} 
	
	@Override
	default OS_Element getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	default void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}

	OS_Element getElement();
}

//
//
//
