/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah;
 
// $ANTLR 2.7.1: "osc.g" -> "OScriptLexer.java"$


public interface ElijahTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int PROCEDURE_CALL = 4;
	int EXPR_LIST = 5;
	int NUM_FLOAT = 6;
	int LITERAL_indexing = 7;
	int IDENT = 8;
	int TOK_COLON = 9;
	int LITERAL_package = 10;
	int SEMI = 11;
	int LITERAL_namespace = 12;
	int LPAREN = 13;
	int RPAREN = 14;
	int LITERAL_imports = 15;
	int LITERAL_class = 16;
	int LCURLY = 17;
	int RCURLY = 18;
	int COMMA = 19;
	int STRING_LITERAL = 20;
	int LITERAL_constructor = 21;
	int LITERAL_ctor = 22;
	int LITERAL_destructor = 23;
	int LITERAL_dtor = 24;
	int LITERAL_from = 25;
	int LITERAL_import = 26;
	int LITERAL_construct = 27;
	int LITERAL_yield = 28;
	int LITERAL_while = 29;
	int LITERAL_do = 30;
	int LITERAL_iterate = 31;
	int LITERAL_to = 32;
	int LITERAL_with = 33;
	int LITERAL_var = 34;
	int LITERAL_const = 35;
	int BECOMES = 36;
	int LITERAL_if = 37;
	int LITERAL_else = 38;
	int LITERAL_elseif = 39;
	int LITERAL_typeof = 40;
	int LITERAL_once = 41;
	int LITERAL_local = 42;
	int LITERAL_tagged = 43;
	int LITERAL_pooled = 44;
	int LITERAL_manual = 45;
	int LITERAL_gc = 46;
	int LITERAL_ref = 47;
	int LITERAL_in = 48;
	int LITERAL_out = 49;
	int LITERAL_generic = 50;
	int LBRACK = 51;
	int RBRACK = 52;
	int LITERAL_function = 53;
	int TOK_ARROW = 54;
	int LITERAL_procedure = 55;
	int DOT = 56;
	int PLUS_ASSIGN = 57;
	int MINUS_ASSIGN = 58;
	int STAR_ASSIGN = 59;
	int DIV_ASSIGN = 60;
	int MOD_ASSIGN = 61;
	int SR_ASSIGN = 62;
	int BSR_ASSIGN = 63;
	int SL_ASSIGN = 64;
	int BAND_ASSIGN = 65;
	int BXOR_ASSIGN = 66;
	int BOR_ASSIGN = 67;
	int QUESTION = 68;
	int LOR = 69;
	int LAND = 70;
	int BOR = 71;
	int BXOR = 72;
	int BAND = 73;
	int EQUALITY = 74;
	int NOT_EQUALS = 75;
	int LT_ = 76;
	int GT = 77;
	int LTE = 78;
	int GTE = 79;
	int LITERAL_is_a = 80;
	int SL = 81;
	int SR = 82;
	int BSR = 83;
	int PLUS = 84;
	int MINUS = 85;
	int STAR = 86;
	int DIV = 87;
	int MOD = 88;
	int QIDENT = 89;
	int INC = 90;
	int DEC = 91;
	int BNOT = 92;
	int LNOT = 93;
	int CHAR_LITERAL = 94;
	int NUM_INT = 95;
	int LITERAL_block = 96;
	int LITERAL_closure = 97;
	int LITERAL_type = 98;
	int LITERAL_alias = 99;
	int LITERAL_struct = 100;
	int VOCAB = 101;
	int WS_ = 102;
	int TIMES = 103;
	int SL_COMMENT = 104;
	int ML_COMMENT = 105;
	int TQUOT = 106;
	int ESC = 107;
	int HEX_DIGIT = 108;
	int EXPONENT = 109;
	int FLOAT_SUFFIX = 110;
}
