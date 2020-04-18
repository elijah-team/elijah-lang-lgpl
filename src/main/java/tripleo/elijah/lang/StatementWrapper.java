package tripleo.elijah.lang;

import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;

public class StatementWrapper implements StatementItem, FunctionItem {

    private IExpression expr;

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

//		@Override
    public void print_osi(TabbedOutputStream aTos) throws IOException {
        // TODO Auto-generated method stub
        int y=2;
        if (expr instanceof BasicBinaryExpression) {
            expr.print_osi(aTos);
        }
    }

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
