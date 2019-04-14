/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 8:43:27 PM
 * 
 * $Id$
 */
package tripleo.elijah.lang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.TabbedOutputStream;

public class FunctionDef implements ClassItem {
	static class StatementWrapper implements StatementItem, FunctionItem {

		private IExpression expr;

		public StatementWrapper(IExpression aexpr) {
			expr = aexpr;
		}

		@Override
		public void print_osi(TabbedOutputStream aTos) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitGen(ICodeGen visit) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private final class FunctionDefScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		@Override
		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false StatementItem %s",
					aItem.getClass().getName()));
		}

		@Override
		public void addDocString(String aS) {
			docstrings.add(aS);
		}

		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(IExpression aExpr) {
			add(new StatementWrapper(aExpr));
//			throw new NotImplementedException(); // TODO
		}
	}

	public List<String> docstrings = new ArrayList<String>();
	public String funName;
	List<FunctionItem> items = new ArrayList<FunctionItem>();
	private final FormalArgList mFal = new FormalArgList();
//	private FunctionDefScope mScope;
	private ClassStatement parent;
	private final FunctionDefScope mScope2 = new FunctionDefScope();

	public FunctionDef(ClassStatement aStatement) {
		parent = aStatement;
		parent.add(this);
	}

	public FormalArgList fal() {
		return mFal;
	}

	@Override
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
		//assert mScope == null;
		return mScope2;
	}

	public void setName(String aText) {
		funName = aText;
	}

	public void visit(JavaCodeGen gen) {
		// TODO Auto-generated method stub
		for (FunctionItem element : items)
			gen.addFunctionItem(element);
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}
}
