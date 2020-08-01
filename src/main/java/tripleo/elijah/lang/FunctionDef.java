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

import antlr.Token;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FunctionDef implements ClassItem, OS_Container, OS_Element2 {

	public Iterable<FormalArgListItem> getArgs() {
		return mFal.items();
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
		public void addDocString(Token aS) {
			docstrings.add(aS.getText());
		}
		
		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
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

		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}

		/* (non-Javadoc)
		 * @see tripleo.elijah.lang.Scope#getParent()
		 */
		@Override
		public OS_Element getParent() {
			// TODO Auto-generated method stub
			return FunctionDef.this;
		}
		
		
	}

	public Attached _a = new Attached(new FunctionContext(this));
	private TypeName _returnType = new RegularTypeName();
	private List<String> docstrings = new ArrayList<String>();
	public String funName;
	private List<FunctionItem> items = new ArrayList<FunctionItem>();
	private final FormalArgList mFal = new FormalArgList();
	private final FunctionDefScope mScope2 = new FunctionDefScope();
	//	private FunctionDefScope mScope;
	private OS_Element/*ClassStatement*/ parent;

	public FunctionDef(OS_Element aElement) {
		parent = aElement;
		if (aElement instanceof ClassStatement) {
			((ClassStatement)parent).add(this);
		} else if (parent instanceof OS_Container) {
			((OS_Container) parent).add(aElement);
		} else {
			throw new IllegalStateException("adding FunctionDef to "+aElement.getClass().getName());
		}
	}

	public FormalArgList fal() {
		return mFal;
	}

	@Override
	public Context getContext() {
		return _a._context;
	}

	public List<FunctionItem> getItems() {
		return items;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	public List<OS_Element2> items() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(OS_Element anElement) {
		if (anElement instanceof FunctionItem)
			items.add((FunctionItem) anElement);
		else
			throw new IllegalStateException(String.format("Cant add %s to FunctionDef", anElement));
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Function print_osi");
		tos.put_string("Function (");
		tos.put_string(funName);
		tos.put_string_ln(") {");
		tos.put_string_ln("//");
		tos.incr_tabs();
		for (FunctionItem item : items) {
			item.print_osi(tos);
		}
		tos.dec_tabs();
		tos.put_string_ln((String.format("} // function %s",  funName)));
	}

	public TypeName returnType() {
		// TODO Auto-generated method stub
		return _returnType ;
	}

	public Scope scope() {
		//assert mScope == null;
		return mScope2;
	}

	public void setName(Token aText) {
		funName = aText.getText();
	}

//	public void visit(JavaCodeGen gen) {
//		// TODO Auto-generated method stub
//		for (FunctionItem element : items)
//			gen.addFunctionItem(element);
//	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}

	@Override // OS_Element2
	public String name() {
		return funName;
	}

}

//
//
//
