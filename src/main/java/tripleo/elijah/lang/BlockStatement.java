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
//			Statement, StatementClosure, FormalArgList

public class BlockStatement implements Statement, StatementItem {

	final private Scope parent;
	private TypeName tn=new RegularTypeName();
	private FormalArgList fal=new FormalArgList();
	private StatementClosure scope;
	
	public BlockStatement(Scope aParent) {
		parent = aParent;
		scope=new AbstractStatementClosure(parent);
	}

	public StatementClosure scope() {
		return scope;
	}

	public FormalArgList opfal() {
		return fal;
	}

	public TypeName returnType() {
		return tn;
	}
}
