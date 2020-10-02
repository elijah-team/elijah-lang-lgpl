/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

public class BasicBinaryExpression implements IBinaryExpression, ScopeElement {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		BasicBinaryExpression abe = this;
		switch (abe.getKind()) {
			case ASSIGNMENT:
				sb.append(abe.getLeft().toString());
				sb.append("=");
				sb.append(abe.getRight().toString());
				break;
			case EQUAL:
				sb.append(abe.getLeft().toString());
				sb.append("==");
				sb.append(abe.getRight().toString());
				break;
			case NOT_EQUAL:
				sb.append(abe.getLeft().toString());
				sb.append("!=");
				sb.append(abe.getRight().toString());
				break;
			case ADDITION:
				sb.append(abe.getLeft().toString());
				sb.append("+");
				sb.append(abe.getRight().toString());
				break;
			case SUBTRACTION:
				sb.append(abe.getLeft().toString());
				sb.append("-");
				sb.append(abe.getRight().toString());
				break;
			case MULTIPLY:
				sb.append(abe.getLeft().toString());
				sb.append("*");
				sb.append(abe.getRight().toString());
				break;
			case DIVIDE:
				sb.append(abe.getLeft().toString());
				sb.append("/");
				sb.append(abe.getRight().toString());
				break;
			case AUG_PLUS:
				sb.append(abe.getLeft().toString());
				sb.append("+=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_MINUS:
				sb.append(abe.getLeft().toString());
				sb.append("-=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_MULT:
				sb.append(abe.getLeft().toString());
				sb.append("*=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_DIV:
				sb.append(abe.getLeft().toString());
				sb.append("/=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_MOD:
				sb.append(abe.getLeft().toString());
				sb.append("%=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_SR:
				sb.append(abe.getLeft().toString());
				sb.append(">>=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_BSR:
				sb.append(abe.getLeft().toString());
				sb.append(">>>=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_SL:
				sb.append(abe.getLeft().toString());
				sb.append("<<=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_BAND:
				sb.append(abe.getLeft().toString());
				sb.append("&=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_BXOR:
				sb.append(abe.getLeft().toString());
				sb.append("^=");
				sb.append(abe.getRight().toString());
				break;
			case AUG_BOR:
				sb.append(abe.getLeft().toString());
				sb.append("|=");
				sb.append(abe.getRight().toString());
				break;
			case IS_A:
				sb.append(abe.getLeft().toString());
				sb.append(" is_a ");
				sb.append(abe.getRight().toString());
				break;
			case QIDENT:
				break;
			case INCREMENT:
				sb.append("++");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case DECREMENT:
				sb.append("--");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case NEGATION:
				sb.append("-");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case POSITIVITY:
				sb.append("+");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case POST_INCREMENT:
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				sb.append("++");
				break;
			case POST_DECREMENT:
				sb.append(abe.getLeft().toString());
				sb.append("--");
				sb.append(abe.getRight().toString());
				break;
			case BNOT:
				sb.append(abe.getLeft().toString());
				sb.append("~");
				sb.append(abe.getRight().toString());
				break;
			case LNOT:
				sb.append("!");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case MODULO:
				sb.append(abe.getLeft().toString());
				sb.append("%");
				sb.append(abe.getRight().toString());
				break;
			case STRING_LITERAL:
				break;
			case PROCEDURE_CALL:
				break;
			case VARREF:
				break;
			case IDENT:
				break;
			case NUMERIC:
				break;
			case FLOAT:
				break;
			case BAND:
				sb.append(abe.getLeft().toString());
				sb.append(" & ");
				sb.append(abe.getRight().toString());
				break;
			case BOR:
				sb.append(abe.getLeft().toString());
				sb.append(" | ");
				sb.append(abe.getRight().toString());
				break;
			case BXOR:
				sb.append(abe.getLeft().toString());
				sb.append(" ^= ");
				sb.append(abe.getRight().toString());
				break;
			case LT_:
				sb.append(abe.getLeft().toString());
				sb.append(" < ");
				sb.append(abe.getRight().toString());
				break;
			case GT:
				sb.append(abe.getLeft().toString());
				sb.append(" > ");
				sb.append(abe.getRight().toString());
				break;
			case LE:
				sb.append(abe.getLeft().toString());
				sb.append(" <= ");
				sb.append(abe.getRight().toString());
				break;
			case GE:
				sb.append(abe.getLeft().toString());
				sb.append(" >= ");
				sb.append(abe.getRight().toString());
				break;
			case INC:
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				sb.append("++");
				break;
			case DEC:
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				sb.append("--");
				break;
			case NEG:
				sb.append("-");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case POS:
				sb.append("+");
				sb.append(abe.getLeft().toString());
				sb.append(abe.getRight().toString());
				break;
			case LAND:
				sb.append(abe.getLeft().toString());
				sb.append(" & ");
				sb.append(abe.getRight().toString());
				break;
			case LOR:
				sb.append(abe.getLeft().toString());
				sb.append(" | ");
				sb.append(abe.getRight().toString());
				break;
			case BSHIFTR:
				sb.append(abe.getLeft().toString());
				sb.append(" >>>= ");
				sb.append(abe.getRight().toString());
				break;
			case LSHIFT:
				sb.append(abe.getLeft().toString());
				sb.append(" << ");
				sb.append(abe.getRight().toString());
				break;
			case RSHIFT:
				sb.append(abe.getLeft().toString());
				sb.append(" >> ");
				sb.append(abe.getRight().toString());
				break;
			case DOT_EXP:
				sb.append(abe.getLeft().toString());
				sb.append(" DOT_EXP ");
				sb.append(abe.getRight().toString());
				break;
			case INDEX_OF:
				sb.append(abe.getLeft().toString());
				sb.append(" INDEX_OF ");
				sb.append(abe.getRight().toString());
				break;
			case GET_ITEM:
				sb.append(abe.getLeft().toString());
				sb.append(" GET_ITEM ");
				sb.append(abe.getRight().toString());
				break;
			case SET_ITEM:
				sb.append(abe.getLeft().toString());
				sb.append(" SET_ITEM ");
				sb.append(abe.getRight().toString());
				break;
			case FUNC_EXPR:
				sb.append(abe.getLeft().toString());
				sb.append(" FUNC_EXPR ");
				sb.append(abe.getRight().toString());
				break;
			case TO_EXPR:
				sb.append(abe.getLeft().toString());
				sb.append(" TO_EXPR ");
				sb.append(abe.getRight().toString());
				break;
		}
		return sb.toString();
	}

	public BasicBinaryExpression() {
		left  = null;
		right = null;
		_kind = null;
	}

	public BasicBinaryExpression(IExpression aLeft, ExpressionKind aType, IExpression aRight) {
		left = aLeft;
		_kind = aType;
		right = aRight;
	}

	@Override
	public IExpression getLeft() {
		return left;
	}

	@Override
	public IExpression getRight() {
		return right;
	}

	@Override
	public ExpressionKind getKind() {
		return _kind;
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s %s>", left, _kind,right);
	}

	@Override
	public void set(IBinaryExpression aEx) {
		left=aEx.getLeft();
		_kind =aEx.getKind();
		right=aEx.getRight();
	}

	@Override
	public void setLeft(IExpression aLeft) {
		left = aLeft;
	}

	@Override
	public void setRight(IExpression aRight) {
		right = aRight;
	}
	@Override
	public void shift(ExpressionKind aType) {
		left=new BasicBinaryExpression(left, _kind,right); //TODO
		_kind =aType;
		right=null;
	}

	public IExpression left;
	public IExpression right;
	public ExpressionKind _kind;

	@Override
	public void setKind(ExpressionKind aKind) {
		_kind = aKind;
	}

	@Override
	public boolean is_simple() {
		return getLeft().is_simple() && getRight().is_simple();
	}

	OS_Type _type;

	@Override
	public void setType(OS_Type deducedExpression) {
		_type = deducedExpression;
    }

	@Override
	public OS_Type getType() {
    	return _type;
	}

}

//
//
//
