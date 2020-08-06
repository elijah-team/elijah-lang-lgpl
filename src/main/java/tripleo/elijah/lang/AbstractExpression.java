/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

public abstract class AbstractExpression implements IExpression, ScopeElement {

	public AbstractExpression() {
		left  = null;
		_kind  = null;
	}

	public AbstractExpression(IExpression aLeft, ExpressionKind aType) {
		left = aLeft;
		_kind = aType;
	}

	@Override
	public IExpression getLeft() {
		return left;
	}
	
	@Override
	public ExpressionKind getKind() {
		return _kind;
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s>", left,_kind);
	}

//	@Override
//	public void set(IBinaryExpression aEx) {
//		left=aEx.getLeft();
//		type=aEx.getType();
//	}

	@Override
	public void setLeft(IExpression aLeft) {
		left = aLeft;
	}

//	@Override
//	public void shift(ExpressionType aType) {
//		left=new AbstractExpression(left,type,right); //TODO
//		type=aType;
//		right=null;
//	}

	public IExpression left;
	public ExpressionKind _kind;

	@Override
	public void setKind(ExpressionKind type1) {
		_kind=type1;
	}
}

//
//
//
