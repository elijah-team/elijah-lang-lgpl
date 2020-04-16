/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijjah;

import antlr.Token;
import tripleo.elijah.lang.AbstractExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo
 *
 * Created 	Apr 16, 2020 at 7:58:36 AM
 */
public class IndexOpExpression extends AbstractExpression { // TODO binary?

	private IExpression index;
	private IExpression primary;

	public IndexOpExpression(IExpression ee, IExpression expr) {
		this.primary = ee;
		this.index = expr;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#getKind()
	 */
	@Override
	public ExpressionKind getKind() {
		// TODO Auto-generated method stub
		return ExpressionKind.INDEX_OF;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#is_simple()
	 */
	@Override
	public boolean is_simple() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#setType(tripleo.elijah.lang.OS_Type)
	 */
	@Override
	public void setType(OS_Type deducedExpression) {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#getType()
	 */
	@Override
	public OS_Type getType() {
		throw new NotImplementedException();
	}

	public void parens(Token lb, Token rb) {
		// TODO implement me later
		
	}

}
