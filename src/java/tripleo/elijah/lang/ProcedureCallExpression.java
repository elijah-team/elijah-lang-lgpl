/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.Qualident;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak2:
//			ParserClosure, ExpressionList

public class ProcedureCallExpression implements StatementItem, FunctionItem, IBinaryExpression {

	public void identifier(Qualident xyz) {
		target=xyz;
	}

	public ExpressionList exprList() {
		return args;
	}
	
	Qualident target;
	ExpressionList args=new ExpressionList();
	
	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public ExpressionType getType() {
		// TODO Auto-generated method stub
		return ExpressionType.PROCEDURE_CALL;
	}

	@Override
	public void set(ExpressionType aIncrement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IExpression getLeft() {
		// TODO Auto-generated method stub
		return target;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		// TODO Auto-generated method stub
		target = iexpression;
	}

	@Override
	public String repr_() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IExpression getRight() {
		// TODO Auto-generated method stub
		return args;
	}

	@Override
	public void setRight(IExpression iexpression) {
		// TODO Auto-generated method stub
		args = iexpression;
	}

	@Override
	public void shift(ExpressionType aType) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void set(IBinaryExpression aEx) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}
