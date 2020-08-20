package tripleo.elijah.lang;

public class StatementWrapper implements StatementItem, FunctionItem {

    private final IExpression expr;

    public StatementWrapper(IExpression aexpr) {
        expr = aexpr;
    }

//		@Override
//		public Context getContext() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public OS_Element getParent() {
//			// TODO Auto-generated method stub
//			return null;
//		}

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        if (expr instanceof AbstractBinaryExpression) {
            return expr.toString();
//        } else throw new NotImplementedException();
        //return sb.toString();
    }

    /**
     * @return the expr
     */
    public IExpression getExpr() {
        return expr;
    }

//		@Override
//		public void visitGen(ICodeGen visit) {
//			// TODO Auto-generated method stub
//
//		}

}
