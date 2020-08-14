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
import tripleo.elijah.Documentable;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.contexts.NamespaceContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo(sb)
 *
 */
public class NamespaceStatement implements Documentable, ModuleItem, ClassItem, StatementItem, FunctionItem, OS_Container, OS_Element2 {

	private Token nsName;
	private OS_Element parent;
	public Attached _a = new Attached(new NamespaceContext(this));
	private List<ClassItem> items = new ArrayList<ClassItem>();
	private NamespaceTypes _kind;
	private OS_Package _packageName;

	public NamespaceStatement(OS_Element aElement) {
		parent = aElement; // setParent
		if (aElement instanceof  OS_Module) {
			final OS_Module module = (OS_Module) aElement;
			//
			this.setPackageName(module.pullPackageName());
			_packageName.addElement(this);
			module.add(this);
		} else if (aElement instanceof OS_Container) {
			((OS_Container) aElement).add(this);
		} else {
			throw new IllegalStateException(String.format("Cant add NamespaceStatement to %s", aElement));
		}
	}

	public void setPackageName(OS_Package aPackageName) {
		_packageName = aPackageName;
	}

	public OS_Package getPackageName() {
		return _packageName;
	}

	public void setName(Token i1) {
		nsName = i1;
	}

//	@Override
//	public void statementWrapper(IExpression aExpr) {
//		// TODO Auto-generated method stub
//		
//	}

	private final List<String> mDocs = new ArrayList<String>();

	@Override
	public void addDocString(Token aText) {
		mDocs.add(aText.getText());
	}

	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(new Scope() {
			@Override
			public void statementWrapper(IExpression aExpr) {
				throw new NotImplementedException();
			}

			@Override
			public StatementClosure statementClosure() {
				throw new NotImplementedException();
//				return null;
			}

			@Override
			public BlockStatement blockStatement() {
				throw new NotImplementedException();
//				return null;
			}

			@Override
			public void add(StatementItem aItem) {
				NamespaceStatement.this.add((OS_Element) aItem);
			}

			@Override
			public TypeAliasExpression typeAlias() {
				return NamespaceStatement.this.typeAlias();
			}

			@Override
			public InvariantStatement invariantStatement() {
				return NamespaceStatement.this.invariantStatement();
			}

			@Override
			public OS_Element getElement() {
				return NamespaceStatement.this;
			}

			@Override
			public void addDocString(Token s1) {
				NamespaceStatement.this.addDocString(s1);
			}
		});
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
		return new ProgramClosure() {
			@Override
			public void addImportStatement(ImportStatement imp) {
				add(imp);
			}
		};
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
		if (nsName == null) return "";
		return nsName.getText();
	}

	public void setType(NamespaceTypes aType) {
		_kind = aType;
	}

	public NamespaceTypes getKind() { return _kind; }

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
