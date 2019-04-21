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

import antlr.CommonToken;
import antlr.Token;
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
	
}

//
//
//
