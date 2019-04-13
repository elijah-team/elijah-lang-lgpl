/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 2, 2005 2:28:42 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import java.util.List;

import antlr.Token;
import tripleo.elijah.Qualident;
import tripleo.elijah.gen.nodes.ExpressionOperators;
import tripleo.elijah.gen.nodes.LocalAgnTmpNode;
import tripleo.elijah.gen.nodes.TmpSSACtxNode;
import tripleo.elijah.util.NotImplementedException;

public class ExpressionBuilder {

	public static IBinaryExpression buildPartial(IExpression aE, ExpressionType aAssignment) {
		// TODO Auto-generated method stub
		return new AbstractBinaryExpression(aE, aAssignment, null);
	}

	public static IBinaryExpression build(IExpression aE, ExpressionType aIs_a, IExpression aExpression) {
		return new AbstractBinaryExpression(aE, aIs_a, aExpression);
	}

	public static IExpression build(IExpression aE, ExpressionType aPost_decrement) {
		// TODO Auto-generated method stub
		return null;
	}

	public static OS_Ident ident(String string) {
		// TODO Auto-generated method stub
		return new OS_Ident(string);
	}

	public static OS_Integer integer(int i) {
		// TODO Auto-generated method stub
		return new OS_Integer(i);
	}

	public static VariableReference varref(String string) {
		// TODO Auto-generated method stub
		return new VariableReference(string);
	}

	public static IExpression binex(VariableReference left, ExpressionOperators middle, IExpression right) {
		// TODO Auto-generated method stub
		ExpressionType middle1;
		switch (middle) {
		case OP_MINUS: middle1 = ExpressionType.SUBTRACTION; break;
		case OP_MULT:  middle1 = ExpressionType.MULTIPLY; break;
		default: throw new NotImplementedException();
		}
		return new AbstractBinaryExpression(left, middle1, right);
	}

	public static IExpression fncall(String string, List<LocalAgnTmpNode> of) { // todo wrong
		// TODO Auto-generated method stub
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		final Qualident xyz = new Qualident();
		final Token t = new Token();
		t.setText(string);
		xyz.append(t);
		pce.identifier(xyz);
//		pce.setArgs(of);
		return pce;
	}

	public static IExpression binex(VariableReference varref, ExpressionOperators opMult, TmpSSACtxNode tccssan2) { // todo wrong again
		// TODO Auto-generated method stub
		return null;
	}

}

//
//
//
