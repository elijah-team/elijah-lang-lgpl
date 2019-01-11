package tripleo.elijah.lang;



public interface IBinaryExpression extends IExpression {

	public abstract IExpression getRight();

	public abstract void setRight(IExpression iexpression);

	public abstract void shift(ExpressionType aType);

	void set(IBinaryExpression aEx);

}
