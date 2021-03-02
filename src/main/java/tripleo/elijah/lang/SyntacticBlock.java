/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.contexts.SyntacticBlockContext;
import tripleo.elijah.gen.ICodeGen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/30/20 1:49 PM
 */
public class SyntacticBlock implements OS_Element, OS_Container, FunctionItem, StatementItem {

	private final List<FunctionItem> _items = new ArrayList<FunctionItem>();
	private final OS_Element _parent;
	private SyntacticBlockContext ctx;
	//	private Scope _scope = new /*SyntacticBlockScope*/AbstractBlockScope(this) {
//		@Override
//		public Context getContext() {
//			return SyntacticBlock.this.getContext();
//		}
//	};
	private Scope3 scope3;

	public SyntacticBlock(final OS_Element aParent) {
		_parent = aParent;
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		visit.visitSyntacticBlock(this);
	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		return ctx;
	}

	public List<FunctionItem> getItems() {
		List<FunctionItem> collection = new ArrayList<FunctionItem>();
		for (OS_Element element : scope3.items()) {
			if (element instanceof FunctionItem)
				collection.add((FunctionItem) element);
		}
		return collection;
		//return _items;
	}

	public void setContext(final SyntacticBlockContext ctx) {
		this.ctx = ctx;
	}

	public void postConstruct() {
	}

	@Override
	public List<OS_Element2> items() {
		return null;
	}

	@Override
	public void add(final OS_Element anElement) {
		if (!(anElement instanceof FunctionItem))
			return;
		_items.add((FunctionItem) anElement);
	}

	private final List<Token> _docstrings = new ArrayList<Token>();

	@Override
	public void addDocString(Token s1) {
		_docstrings.add(s1);
	}

	public void scope(Scope3 sco) {
		scope3 = sco;
	}

}

//
//
//
