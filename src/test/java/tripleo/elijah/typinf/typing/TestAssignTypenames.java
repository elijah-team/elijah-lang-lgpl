/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.typinf.typing;

import org.junit.Test;
import tripleo.elijah.typinf.TypInf;
import tripleo.elijah.typinf.ast;
import tripleo.elijah.typinf.parser.ParseError;
import tripleo.elijah.typinf.parser.Parser;

import static org.junit.Assert.assertEquals;

/**
 * # Eli Bendersky [http://eli.thegreenplace.net]
 * # This code is in the public domain.
 *
 * Created 9/3/21 9:54 PM
 */
public class TestAssignTypenames {

	public String str(Object a) {
		return a.toString();
	}

	@Test
	public void test_decl() throws ParseError {
		Parser p = new Parser();
		ast.Decl e = p.parse_decl("foo f x = f(3) - f(x)");
		TypInf.assign_typenames(e.expr);

		final ast.LambdaExpr lambdaExpr = (ast.LambdaExpr) e.expr;
		final ast.OpExpr opExpr = (ast.OpExpr) lambdaExpr.expr;

		final ast.AppExpr leftExpr = (ast.AppExpr) opExpr.left;
		final ast.AppExpr rightExpr = (ast.AppExpr) opExpr.right;

		assertEquals("t1", str(leftExpr.func._type));
		assertEquals("t1", str(rightExpr.func._type));
		assertEquals("t2", str(rightExpr.args.get(0)._type));

		assertEquals("t3", str(opExpr._type));
		assertEquals("t4", str(leftExpr._type));
		assertEquals("t5", str(rightExpr._type));
	}
}

//
//
//
