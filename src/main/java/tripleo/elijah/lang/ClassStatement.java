/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import antlr.Token;
import com.thoughtworks.xstream.XStream;
import tripleo.elijah.ProgramClosure;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak2:
//			Scope, ExpressionWrapper, AbstractStatementClosure, StatementClosure, 
//			BlockStatement

/**
 * Represents a "class"
 * 
 * items -> ClassItems
 * docstrings 
 * variables
 * 
 */
public class ClassStatement implements ClassItem, Scope, ModuleItem, OS_Element {
	
	private OS_Package _packageName;
	
	/**
	 * For XMLBeans. Must use setParent.
	 */
	public ClassStatement() {
	}
	
	public ClassStatement(OS_Element aElement) {
		parent = aElement; // setParent
		if (aElement instanceof  OS_Module) {
			final OS_Module module;
			module = (OS_Module) aElement;
			//
			this.setPackageName(module.pullPackageName());
			module.add(this);
		}
	}

	public void add(ClassItem aDef) {
		items.add (aDef);
	}

	@Override
	public void add(StatementItem aItem) {
		if (aItem instanceof ClassItem) {
			ClassItem new_name = (ClassItem) aItem;
			items.add (new_name);
		} else
			throw new IllegalArgumentException(""+aItem.getClass().getName()+" cannot be added to class");
	}

	@Override
	public void addDocString(Token aText) {
		mDocs.add(aText.getText());
	}

	@Override
	public BlockStatement blockStatement() {
		return null;
	}

	public ClassInheritance classInheritance() {
		return new ClassInheritance(this);
	}

//	public IExpression fixme_expr() {
//		IExpression R = new ExpressionWrapper();
//		mExprs.add(R);
//		return R;
//	}

	public FunctionDef funcDef() {
		return new FunctionDef(this);
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		XStream x= new XStream();
		x.toXML(this, tos.getStream());
		//
		System.out.println("Klass print_osi");
		tos.incr_tabs();
		tos.put_string("Class (");
		tos.put_string(clsName);
		tos.put_string_ln(") {");
		tos.put_string_ln("//");
		for (ClassItem item : items) {
			item.print_osi(tos);
		}
		tos.dec_tabs();
		tos.put_string_ln(String.format("} // class %s ", clsName));
		tos.flush();
	}
	
	public String getName() {
		return clsName;
	}
	
	public List<ClassItem> getItems() {
		return items;
	}
	
	public ArrayList<String> getDocstrings() {
		return mDocs;
	}
	
	public OS_Element getParent() {
		return parent;
	}
	
	public void setName(Token aText) {
		clsName=aText.getText();
	}

	@Override
	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

//	public IExpression statementWrapper() {
//		IExpression R = new ExpressionWrapper();
//		mExprs.add(R);
//		return R;
//	}

	@Override
	public void statementWrapper(IExpression aExpr) {
		// TODO Auto-generated method stub
		System.err.println("** adding "+aExpr.repr_()+"to class");
		mExprs.add(aExpr);
//		throw new NotImplementedException();
	}

	public String clsName;

	private final List<ClassItem> items=new ArrayList<ClassItem>();
	private final ArrayList<String> mDocs = new ArrayList<String>();
	private final ArrayList<IExpression> mExprs = new ArrayList<IExpression>();

	public /*final*/ OS_Element parent;

	public synchronized Collection<ClassItem> items() {
		return items;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		visit.addClass(this);
	}
	
	public void setPackageName(OS_Package aPackageName) {
		_packageName = aPackageName;
	}
	
	public OS_Package getPackageName() {
		return _packageName;
	}
	
	public Scope addCtor(Token aX1) {
		NotImplementedException.raise();
		return null;
	}
	
	public Scope addDtor() {
		NotImplementedException.raise();
		return null;
	}
	
	public TypeAliasExpression typeAlias() {
		NotImplementedException.raise();
		return null;
	}
	
	public InvariantStatement invariantStatement() {
		NotImplementedException.raise();
		return null;
	}
	
	public ProgramClosure XXX() {
		return null;
	}
}

//
//
//
