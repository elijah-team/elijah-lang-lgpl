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


// Referenced classes of package pak2:
//			BinaryExpression, StatementClosure, ClassStatement, ImportStatement, 
//			ExpressionType

import tripleo.elijah.ProgramClosure;

public class ParserClosure extends ProgramClosure {

	public ParserClosure(String fn) {
		module = new OS_Module();
		module.setFileName(fn);
	}

	public ClassStatement classStatement() {
		return new ClassStatement(module());
	}

	public ImportStatement importStatement() {
		return new ImportStatement(module());
	}

	private OS_Module module() {
		return module;
	}

	public void packageName(Qualident aXy) {
		//assert module.packageName ==null;
		module.pushPackageName(aXy);
	}

	public final OS_Module module;

	public NamespaceStatement namespaceStatement() {
		// TODO Auto-generated method stub
		return new NamespaceStatement(module());
	}

}

