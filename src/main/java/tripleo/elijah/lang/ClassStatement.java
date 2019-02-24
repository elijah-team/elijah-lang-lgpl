package tripleo.elijah.lang;

import java.io.IOException;
import java.util.*;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.*;

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
public class ClassStatement implements Scope, ModuleItem, OS_Element {

	public ClassStatement(OS_Module aModule) {
		parent = aModule;
		aModule.add(this);
	}

	public void add(ClassItem aDef) {
		items.add (aDef);
	}

	public void add(StatementItem aItem) {
		if (aItem instanceof ClassItem) {
			ClassItem new_name = (ClassItem) aItem;
			items.add (new_name);
		} else assert false;
	}

	public void addDocString(String aText) {
		mDocs.add(aText);
	}

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

	public StatementClosure statementClosure() {
		return new AbstractStatementClosure(this);
	}

	public IExpression statementWrapper() {
		IExpression R = new ExpressionWrapper();
		mExprs.add(R);
		return R;
	}

	public void statementWrapper(IExpression aExpr) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		
	}
	public void visitGen(JavaCodeGen visit) {
		visit.addClass(this);
	}

	public String clsName;

	private final List<ClassItem> items=new ArrayList<ClassItem>();
	private final ArrayList<String> mDocs = new ArrayList<String>();
	private final ArrayList<IExpression> mExprs = new ArrayList<IExpression>();

	public final OS_Module parent;

	public synchronized Collection<ClassItem> items() {
		return items;
	}
}
