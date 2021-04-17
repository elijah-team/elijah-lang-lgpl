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

import java.util.ArrayList;
import java.util.List;

public class DefFunctionDef extends FunctionDef {

	public DefFunctionDef(OS_Element aElement, Context aContext) {
		super(aElement, aContext);
	}

	private IExpression _expr;
//	private FormalArgList fal;

	// wont use parent scope.items.add so this is ok
	public void setExpr(final IExpression aExpr) {
		_expr = aExpr;
		_items.add((FunctionItem) _expr);
	}

	List<FunctionItem> _items = new ArrayList<FunctionItem>();


	@Override
	public List<FunctionItem> getItems() {
		return _items; // TODO what about scope?
	}

	/**
	 * see {@link #_expr} for why getItems().size should be 0, or
	*/
	@Override
	public void postConstruct() {
		super.postConstruct();
		if (getItems().size() != 1)
			throw new IllegalStateException("Too many items"); // TODO convert to diagnostic?
	}
}

//
//
//
