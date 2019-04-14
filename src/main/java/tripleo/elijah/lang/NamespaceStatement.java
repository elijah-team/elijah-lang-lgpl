/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Apr 2, 2019 at 11:08:12 AM
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

/**
 * @author SBUSER
 *
 */
public class NamespaceStatement implements Scope {

	private Token name;
	private OS_Module parent;

	public NamespaceStatement(OS_Module module) {
		this.parent = module;
	}

	public void setName(Token i1) {
		// TODO Auto-generated method stub
		name = i1;
	}

	@Override
	public void statementWrapper(IExpression aExpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDocString(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StatementClosure statementClosure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockStatement blockStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(StatementItem aItem) {
		// TODO Auto-generated method stub
		
	}

	public FunctionDef funcDef() {
		// TODO Auto-generated method stub
		return null;
	}

}

//
//
//
