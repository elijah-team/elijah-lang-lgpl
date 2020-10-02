/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

public enum ExpressionKind {

	ASSIGNMENT,

	ADDITION, SUBTRACTION, MULTIPLY, DIVIDE,
	AUG_PLUS, AUG_MINUS, AUG_MULT, AUG_DIV,

	AUG_MOD,

	AUG_SR, AUG_BSR,
	AUG_SL, // TODO missing AUG_BSL
	AUG_BAND,
	AUG_BXOR,
	AUG_BOR,
	IS_A, //xy,

	INC, DEC,
	INCREMENT, DECREMENT,
	POST_INCREMENT, POST_DECREMENT,

	NEGATION, POSITIVITY,
	NEG, POS,

	BNOT, LNOT,

	MODULO, STRING_LITERAL, PROCEDURE_CALL,
	
	VARREF, QIDENT, IDENT,

	NUMERIC, FLOAT,
	
	BAND, BXOR, BOR,

	NOT_EQUAL, EQUAL, LT_, GT, LE, GE,

	LAND, LOR, BSHIFTR, LSHIFT, RSHIFT,
	
	DOT_EXP, INDEX_OF, GET_ITEM, SET_ITEM, FUNC_EXPR, TO_EXPR
}

//
//
//
