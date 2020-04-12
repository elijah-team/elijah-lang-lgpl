/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo(envy)
 *
 * Created 	Mar 27, 2020 at 12:59:41 AM
 */
public class DotExpression extends AbstractBinaryExpression {

	public DotExpression(IExpression ee, IdentExpression identExpression) {
		left = ee;
		right = identExpression;
		kind = ExpressionKind.DOT_EXP;
	}

	//
	// all this garbage below
	//
	
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#print_osi(tripleo.elijah.util.TabbedOutputStream)
//	 */
//	@Override
//	public void print_osi(TabbedOutputStream tabbedoutputstream) throws IOException {
//		// TODO Auto-generated method stub
//
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#getType()
//	 */
//	@Override
//	public ExpressionType getType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#set(tripleo.elijah.lang.ExpressionType)
//	 */
//	@Override
//	public void set(ExpressionType aIncrement) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#getLeft()
//	 */
//	@Override
//	public IExpression getLeft() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#setLeft(tripleo.elijah.lang.IExpression)
//	 */
//	@Override
//	public void setLeft(IExpression iexpression) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#repr_()
//	 */
//	@Override
//	public String repr_() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see tripleo.elijah.lang.IExpression#is_simple()
//	 */
//	@Override
//	public boolean is_simple() {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
