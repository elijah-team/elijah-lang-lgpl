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

	ADDITION, 
	ASSIGNMENT,
	AUG_PLUS,
	AUG_MINUS,
	AUG_MULT,
	AUG_DIV,
	AUG_MOD,
	AUG_SR,
	AUG_BSR,
	AUG_SL,
	AUG_BAND,
	AUG_BXOR,
	AUG_BOR,
	IS_A, //xy,
	QIDENT,
	INCREMENT,
	DECREMENT,
	NEGATION,
	POSITIVITY,
	POST_INCREMENT,
	POST_DECREMENT, SUBTRACTION, BNOT, LNOT, 
//	SIMPLE // TODO

	
	
	
	
	MULTIPLY, MODULO, DIVIDE, STRING_LITERAL, PROCEDURE_CALL,
	
	/*QUALIDENT,*/ VARREF, IDENT, NUMERIC, FLOAT, 
	
	BAND, BOR, BXOR, NOT_EQUAL, EQUAL, LT_, GT, LE, GE, INC, DEC, NEG, POS, LAND, LOR, BSHIFTR, LSHIFT, RSHIFT, 
	
	DOT_EXP, INDEX_OF, TO_EXPR
}

//
//
//
