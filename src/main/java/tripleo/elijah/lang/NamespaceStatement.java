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
public class NamespaceStatement implements Documentable, ModuleItem, ClassItem, StatementItem, FunctionItem, OS_Container, OS_Element2 {

	private Token nsName;
	private OS_Module parent;
	private NamespaceTypes type; // TODO implement setter
	public Attached _a = new Attached(new NamespaceContext(this));
	private List<ClassItem> items = new ArrayList<ClassItem>();

	public NamespaceStatement(OS_Module module) {
		this.parent = module;
	}

	public void setName(Token i1) {
		nsName = i1;
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
		return new FunctionDef(this);
	}
	
	public ProgramClosure XXX() {
		return new ProgramClosure() {};
	}

	@Override // OS_Element, FunctionItem
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override // OS_Element
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override // OS_Element
	public OS_Element getParent() {
		return parent;
	}

	@Override // OS_Element
	public Context getContext() {
		return _a.getContext();
	}

	public List<ClassItem> getItems() {
		return items ;
	}
	
	public String getName() {
		return nsName.getText();
	}

	public void setType(NamespaceTypes aType) {
		type = aType;
	}

	@Override // OS_Container
	public List<OS_Element2> items() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // OS_Container
	public void add(OS_Element anElement) {
		if (anElement instanceof ClassItem)
			items.add((ClassItem) anElement);
		else
			System.err.println(String.format("[NamespaceStatement#add] not a ClassItem: %s", anElement));
	}

	@Override // OS_Element2
	public String name() {
		return getName();
	}
}

//
//
//
