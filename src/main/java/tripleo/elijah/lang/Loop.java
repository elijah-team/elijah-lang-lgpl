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

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak2:
//			Statement, LoopTypes, Scope

public class Loop implements Statement, LoopTypes, StatementItem, FunctionItem {

	public Loop() {
	}

	public void type(int aType) {
		type = aType;
	}

	public Scope scope() {
		return null;
	}

	public void expr(IExpression aExpr) {
		expr=aExpr;
	}

	public void topart(IExpression aExpr) {
		topart=aExpr;
	}

	public void frompart(IExpression aExpr) {
		frompart=aExpr;
	}

	public void iterName(String s) {
//		assert type == ITER_TYPE;
		iterName=s;
	}

	String iterName;
	int type;
IExpression topart,expr; 
IExpression frompart;

	public final int FROM_TO_TYPE = 82;

	public final int TO_TYPE = 81;
	public final int ITER_TYPE = 86;

	public final int EXPR_TYPE = 83;

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
}
