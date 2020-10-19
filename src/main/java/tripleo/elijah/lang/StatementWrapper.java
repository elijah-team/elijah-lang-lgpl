package tripleo.elijah.lang;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

public class StatementWrapper implements StatementItem, FunctionItem, OS_Element {

    private final IExpression expr;
    private Context _ctx;
    private OS_Element _parent;

    public StatementWrapper(final IExpression aexpr, final Context ctx, final OS_Element parent) {
        expr = aexpr;
        _ctx = ctx;
        _parent = parent;
    }

    @Override
    public Context getContext() {
        return _ctx;
    }

    @Override
    public OS_Element getParent() {
        return _parent;
    }

    @Override
    public String toString() {
        return expr.toString();
    }

    /**
     * @return the expr
     */
    public IExpression getExpr() {
        return expr;
    }

    @Override
    public void visitGen(final ICodeGen visit) {
        throw new NotImplementedException();
    }

}

//
//
//
