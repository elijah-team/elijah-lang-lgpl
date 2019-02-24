/*
 * Created on Aug 30, 2005 8:43:27 PM
 * 
 * $Id$
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.*;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.*;

public class FunctionDef implements ClassItem {
	static class StatementWrapper implements StatementItem, FunctionItem {

		private IExpression expr;

		public StatementWrapper(IExpression aexpr) {
			expr = aexpr;
		}

		public void print_osi(TabbedOutputStream aTos) throws IOException {
			// TODO Auto-generated method stub
			
		}
	}
	
	private final class FunctionDefScope implements Scope {

		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				items.add((FunctionItem) aItem);
			else
				System.err.println("adding false StatementItem "
						+ aItem.getClass().getName());
		}

		public void addDocString(String aS) {
			docstrings.add(aS);
		}

		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}

		public StatementClosure statementClosure() {
			return new AbstractStatementClosure(this);
		}

		public void statementWrapper(IExpression aExpr) {
			add(new StatementWrapper(aExpr));
//			throw new NotImplementedException(); // TODO
		}
	}

	public List<String> docstrings = new ArrayList<String>();
	public String funName;
	List<FunctionItem> items = new ArrayList<FunctionItem>();
	private final FormalArgList mFal = new FormalArgList();
	private FunctionDefScope mScope;
	private ClassStatement parent;

	public FunctionDef(ClassStatement aStatement) {
		parent = aStatement;
		parent.add(this);
	}

	public FormalArgList fal() {
		return mFal;
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Klass print_osi");
		tos.incr_tabs();
		tos.put_string("Class (");
		tos.put_string(funName);
		tos.put_string_ln(") {");
		tos.put_string_ln("//");
		for (FunctionItem item : items) {
			item.print_osi(tos);
		}
		tos.dec_tabs();
		tos.put_string_ln((("} // class ")) + (funName));
	}

	public Scope scope() {
		assert mScope == null;
		mScope = new FunctionDefScope();
		return mScope;
	}

	public void setName(String aText) {
		funName = aText;
	}

	public void visit(JavaCodeGen gen) {
		// TODO Auto-generated method stub
		for (FunctionItem element : items)
			gen.addFunctionItem(element);
	}
}
