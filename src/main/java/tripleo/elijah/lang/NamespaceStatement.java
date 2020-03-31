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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.contexts.NamespaceContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;
import tripleo.elijah.Documentable;

/**
 * @author Tripleo(sb)
 *
 */
public class NamespaceStatement implements Documentable, ModuleItem, ClassItem, StatementItem {

	private Token name;
	private OS_Module parent;
	private NamespaceTypes type; // TODO implement setter
	public Attached _a = new Attached(new NamespaceContext(this));
	private List<ClassItem> items = new ArrayList<ClassItem>();

	public NamespaceStatement(OS_Module module) {
		this.parent = module;
	}

	public void setName(Token i1) {
		// TODO Auto-generated method stub
		name = i1;
	}

//	@Override
//	public void statementWrapper(IExpression aExpr) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void addDocString(Token s) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public StatementClosure statementClosure() {
		throw new NotImplementedException();
	}

	public TypeAliasExpression typeAlias() { 
		throw new NotImplementedException();
	}
	
	public InvariantStatement invariantStatement() {
		throw new NotImplementedException();
	}
	
	public FunctionDef funcDef() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
//		NotImplementedException.raise();
//		return null;
	}
	
	public ProgramClosure XXX() {
		throw new NotImplementedException();
//		NotImplementedException.raise();
//		return null;
	}

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _a.getContext();
	}

	public List<ClassItem> getItems() {
		return items ;
	}
	
	public String getName() {
		return name.getText();
	}
	
}

//
//
//
