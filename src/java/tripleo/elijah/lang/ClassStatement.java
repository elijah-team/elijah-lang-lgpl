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

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.gen.java.JavaCodeGen;
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

	public ClassStatement(OS_Element aElement) {
		parent = aElement;
		((OS_Module) aElement).add(this);
	}

	public void add(ClassItem aDef) {
		items.add (aDef);
	}

	@Override
	public void add(StatementItem aItem) {
		if (aItem instanceof ClassItem) {
			ClassItem new_name = (ClassItem) aItem;
			items.add (new_name);
		} else assert false;
	}

	@Override
	public void addDocString(String aText) {
		mDocs.add(aText);
	}

	@Override
	public BlockStatement blockStatement() {
		return null;
	}

	public ClassInheritance classInheritance() {
		return new ClassInheritance(this);
	}

	public IExpression fixme_expr() {
		IExpression R = new ExpressionWrapper();
		mExprs.add(R);
		return R;
	}

	public FunctionDef funcDef() {
		return new FunctionDef(this);
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
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
		tos.put_string_ln((new StringBuilder("} // class ")).append(clsName)
				.toString());
	}

	public void setName(String aText) {
		clsName=aText;
	}

	@Override
	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

	public IExpression statementWrapper() {
		IExpression R = new ExpressionWrapper();
		mExprs.add(R);
		return R;
	}

	@Override
	public void statementWrapper(IExpression aExpr) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		
	}

	public String clsName;

	private final List<ClassItem> items=new ArrayList<ClassItem>();
	private final ArrayList<String> mDocs = new ArrayList<String>();
	private final ArrayList<IExpression> mExprs = new ArrayList<IExpression>();

	public final OS_Element parent;

	public synchronized Collection<ClassItem> items() {
		return items;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		visit.addClass(this);
	}

//	@Override
//	public void visit(ICodeGen gen) {
//		// TODO Auto-generated method stub
//		gen.addClass(this);
//	}

	public void visit(JavaCodeGen javaCodeGen) {
		// TODO remove remove
		javaCodeGen.addClass(this);
		
	}
}
