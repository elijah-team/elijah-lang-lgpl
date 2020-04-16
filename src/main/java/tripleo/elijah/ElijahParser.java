/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah;

import org.eclipse.jdt.annotation.NonNull;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
// $ANTLR 2.7.1: "osc.g" -> "OScriptParser.java"$
import tripleo.elijah.lang.*;

public class ElijahParser extends antlr.debug.LLkDebuggingParser implements ElijahTokenTypes {
	private static final String[] _ruleNames = {"program", "constantValue", "qualident", "programStatement",
			"classStatement", "importStatement", "classInheritance", "docstrings", "classScope", "typeName", "scope",
			"functionDef", "varStmt", "typeAlias", "opfal", "baseStatement", "statement", "identList", "expression",
			"functionScope", "functionStatement", "block", "formalArgList", "procedureCallExpression", "ifConditional",
			"whileLoop", "frobeIteration", "procCallEx", "varStmt_i", "elseif_part", "structTypeName",
			"genericQualifiers", "abstractGenericTypeName_xx", "specifiedGenericTypeName_xx", "formalArgTypeName",
			"funcTypeExpr", "simpleTypeName_xx", "variableQualifiers", "regularQualifiers", "typeNameList", "ident2",
			"qualidentList", "formalArgListItem_priv", "ident", "assignmentExpression", "expressionList",
			"conditionalExpression", "logicalOrExpression", "logicalAndExpression", "inclusiveOrExpression",
			"exclusiveOrExpression", "andExpression", "equalityExpression", "relationalExpression", "shiftExpression",
			"additiveExpression", "multiplicativeExpression", "qidentExpression", "unaryExpression",
			"unaryExpressionNotPlusMinus", "postfixExpression_priv", "primitiveElement", "variableReference",
			"procCallEx2",};

	public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "PROCEDURE_CALL",
			"EXPR_LIST", "NUM_FLOAT", "\"indexing\"", "IDENT", "TOK_COLON", "\"package\"", "SEMI", "\"namespace\"",
			"LPAREN", "RPAREN", "\"imports\"", "\"class\"", "LCURLY", "RCURLY", "COMMA", "STRING_LITERAL",
			"\"constructor\"", "\"ctor\"", "\"destructor\"", "\"dtor\"", "\"from\"", "\"import\"", "\"construct\"",
			"\"yield\"", "\"while\"", "\"do\"", "\"iterate\"", "\"to\"", "\"with\"", "\"var\"", "\"const\"", "BECOMES",
			"\"if\"", "\"else\"", "\"elseif\"", "\"typeof\"", "\"once\"", "\"local\"", "\"tagged\"", "\"pooled\"",
			"\"manual\"", "\"gc\"", "\"ref\"", "\"in\"", "\"out\"", "\"generic\"", "LBRACK", "RBRACK", "\"function\"",
			"TOK_ARROW", "\"procedure\"", "DOT", "PLUS_ASSIGN", "MINUS_ASSIGN", "STAR_ASSIGN", "DIV_ASSIGN",
			"MOD_ASSIGN", "SR_ASSIGN", "BSR_ASSIGN", "SL_ASSIGN", "BAND_ASSIGN", "BXOR_ASSIGN", "BOR_ASSIGN",
			"QUESTION", "LOR", "LAND", "BOR", "BXOR", "BAND", "EQUALITY", "NOT_EQUALS", "LT_", "GT", "LTE", "GTE",
			"\"is_a\"", "SL", "SR", "BSR", "PLUS", "MINUS", "STAR", "DIV", "MOD", "QIDENT", "INC", "DEC", "BNOT",
			"LNOT", "CHAR_LITERAL", "NUM_INT", "\"block\"", "\"closure\"", "\"type\"", "\"alias\"", "\"struct\"",
			"VOCAB", "WS_", "TIMES", "SL_COMMENT", "ML_COMMENT", "TQUOT", "ESC", "HEX_DIGIT", "EXPONENT",
			"FLOAT_SUFFIX" };
	private static final long[] _tokenSet_0_data_ = {100765696L, 0L};
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	
	private static final long[] _tokenSet_1_data_ = {46303767540793600L, 0L};

	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	
	private static final long[] _tokenSet_2_data_ = {2251992985772288L, 94425317376L, 0L, 0L};

	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	
	private static final long[] _tokenSet_3_data_ = {1267771266564352L, 0L};

	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	
	private static final long[] _tokenSet_4_data_ = {-18998186670658816L, 8525975551L, 0L, 0L};

	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	
	private static final long[] _tokenSet_5_data_ = {2251992954315008L, 4231004160L, 0L, 0L};

	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	
	private static final long[] _tokenSet_6_data_ = {-69804703401760000L, 8589934591L, 0L, 0L};

	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	
	private static final long[] _tokenSet_7_data_ = {193139310848L, 0L};

	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	
	private static final long[] _tokenSet_8_data_ = {74309398181323008L, 4231004160L, 0L, 0L};

	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	
	private static final long[] _tokenSet_9_data_ = {2251799814742272L, 4231004160L, 0L, 0L};

	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	
	private static final long[] _tokenSet_10_data_ = {-69805532364135680L, 4294967295L, 0L, 0L};

	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	
	private static final long[] _tokenSet_11_data_ = {193139310848L, 4294967296L, 0L, 0L};

	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	
	private static final long[] _tokenSet_12_data_ = {46303767540269312L, 0L};

	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	
	private static final long[] _tokenSet_13_data_ = {-91055780708586752L, 8525975551L, 0L, 0L};

	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	
	private static final long[] _tokenSet_14_data_ = {-138538496557310L, 17179869183L, 0L, 0L};

	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	
	private static final long[] _tokenSet_15_data_ = {46303767540285696L, 0L};

	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	
	private static final long[] _tokenSet_16_data_ = {-73041382199104768L, 8525975551L, 0L, 0L};

	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	
	private static final long[] _tokenSet_17_data_ = {-137359513889117440L, 8589934591L, 0L, 0L};

	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	
	private static final long[] _tokenSet_18_data_ = {-18997361936171262L, 17179869183L, 0L, 0L};

	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	
	private static final long[] _tokenSet_19_data_ = {2251799814758656L, 4231004160L, 0L, 0L};

	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);

	public Out out;

	private String xy;

	private IExpression expr;
//	public final static Object null/*fixme*/=null;
	
	private String[] _semPredNames = {};

	public ElijahParser(ParserSharedInputState state) {
		super(state, 2);
		tokenNames = _tokenNames;
	}

	public ElijahParser(TokenBuffer tokenBuf) {
		this(tokenBuf, 2);
	}

	protected ElijahParser(TokenBuffer tokenBuf, int k) {
		super(tokenBuf, k);
		tokenNames = _tokenNames;
		ruleNames = _ruleNames;
		semPredNames = _semPredNames;
		setupDebugging(tokenBuf);
	}

	public ElijahParser(TokenStream lexer) {
		this(lexer, 2);
	}

	protected ElijahParser(TokenStream lexer, int k) {
		super(lexer, k);
		tokenNames = _tokenNames;
		ruleNames = _ruleNames;
		semPredNames = _semPredNames;
		setupDebugging(lexer);
	}

	public final void abstractGenericTypeName_xx(TypeName tn) throws RecognitionException, TokenStreamException {

		fireEnterRule(32, 0);
		try { // debugging

			switch (LA(1)) {
				case LITERAL_generic: {
					match(LITERAL_generic);
					break;
				}
				case TOK_COLON: { // TODO wont work
					match(TOK_COLON);
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
			}
			Qualident xyz = qualident();
			if (inputState.guessing == 0) {
				tn.typeName(xyz);
			}
			if (inputState.guessing == 0) {
				tn.set(TypeModifiers.GENERIC);
			}
		} finally { // debugging
			fireExitRule(32, 0);
		}
	}

	public final IExpression additiveExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(55, 0);
		try { // debugging
			IExpression ex;

			e = multiplicativeExpression();
			{
				_loop155: do {
					if ((LA(1) == PLUS || LA(1) == MINUS) && (_tokenSet_9.member(LA(2)))) {
						{
							switch (LA(1)) {
							case PLUS: {
								match(PLUS);
								if (inputState.guessing == 0) {
									e = ExpressionBuilder.buildPartial(e, ExpressionKind.ADDITION);
								}
								break;
							}
							case MINUS: {
								match(MINUS);
								if (inputState.guessing == 0) {
									e = ExpressionBuilder.buildPartial(e, ExpressionKind.SUBTRACTION);
								}
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						ex = multiplicativeExpression();
						if (inputState.guessing == 0) {
							((IBinaryExpression) e).setRight(ex);
						}
					} else {
						break _loop155;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(55, 0);
		}
	}

	public final IExpression andExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(51, 0);
		try { // debugging

			e = equalityExpression();
			{
				_loop138: do {
					if ((LA(1) == BAND)) {
						match(BAND);
						equalityExpression();
					} else {
						break _loop138;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(51, 0);
		}
	}

	public final IExpression assignmentExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(44, 0);
		try { // debugging
			IExpression e2;

			e = conditionalExpression();
			{
				switch (LA(1)) {
				case BECOMES:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN: {
					{
						switch (LA(1)) {
						case BECOMES: {
							match(BECOMES);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.ASSIGNMENT);
							}
							break;
						}
						case PLUS_ASSIGN: {
							match(PLUS_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_PLUS);
							}
							break;
						}
						case MINUS_ASSIGN: {
							match(MINUS_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_MINUS);
							}
							break;
						}
						case STAR_ASSIGN: {
							match(STAR_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_MULT);
							}
							break;
						}
						case DIV_ASSIGN: {
							match(DIV_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_DIV);
							}
							break;
						}
						case MOD_ASSIGN: {
							match(MOD_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_MOD);
							}
							break;
						}
						case SR_ASSIGN: {
							match(SR_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_SR);
							}
							break;
						}
						case BSR_ASSIGN: {
							match(BSR_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_BSR);
							}
							break;
						}
						case SL_ASSIGN: {
							match(SL_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_SL);
							}
							break;
						}
						case BAND_ASSIGN: {
							match(BAND_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_BAND);
							}
							break;
						}
						case BXOR_ASSIGN: {
							match(BXOR_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_BXOR);
							}
							break;
						}
						case BOR_ASSIGN: {
							match(BOR_ASSIGN);
							if (inputState.guessing == 0) {
								e = ExpressionBuilder.buildPartial(e, ExpressionKind.AUG_BOR);
							}
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					e2 = assignmentExpression();
					if (inputState.guessing == 0) {
						((IBinaryExpression) e).setRight(e2);
					}
					break;
				}
				case IDENT:
				case TOK_COLON:
				case SEMI:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_to:
				case LITERAL_with:
				case LITERAL_var:
				case LITERAL_const:
				case LITERAL_if:
				case LBRACK:
				case RBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			return e;
		} finally { // debugging
			fireExitRule(44, 0);
		}
	}

	public final void baseStatement(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(15, 0);
		try { // debugging

			statement(pc);
		} finally { // debugging
			fireExitRule(15, 0);
		}
	}

	public final void block(BlockStatement pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(21, 0);
		try { // debugging

			match(LITERAL_block);
			{
				switch (LA(1)) {
				case LITERAL_closure: {
					match(LITERAL_closure);
					break;
				}
				case LPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			opfal(pc.opfal());
			{
				switch (LA(1)) {
				case TOK_ARROW: {  
					match(TOK_ARROW);
					typeName(pc.returnType());
					break;
				}
				case TOK_COLON: { // TODO wont work maybe
					match(TOK_COLON);
					typeName(pc.returnType());
					break;
				}
				case LCURLY: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(LCURLY);
			{
				_loop179: do {
					if ((_tokenSet_7.member(LA(1)))) {
						baseStatement(pc.scope());
					} else {
						break _loop179;
					}

				} while (true);
			}
			match(RCURLY);
		} finally { // debugging
			fireExitRule(21, 0);
		}
	}

	private final void classInheritance(ClassInheritance ci) throws RecognitionException, TokenStreamException {

		fireEnterRule(6, 0);
		try { // debugging

			match(LPAREN);
			typeName(ci.next());
			{
				_loop18: do {
					if ((_tokenSet_1.member(LA(1)))) {
						{
							switch (LA(1)) {
							case COMMA: {
								match(COMMA);
								break;
							}
							case IDENT:
							case LITERAL_const:
							case LITERAL_typeof:
							case LITERAL_ref:
							case LITERAL_generic:
							case LITERAL_function:
							case LITERAL_procedure: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						typeName(ci.next());
					} else {
						break _loop18;
					}

				} while (true);
			}
			match(RPAREN);
		} finally { // debugging
			fireExitRule(6, 0);
		}
	}

	public final void classScope(ClassStatement pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(8, 0);
		try { // debugging
			
			_loop3001: do {

			switch (LA(1)) {
			case LITERAL_constructor:
			case LITERAL_ctor: {
				{
					switch (LA(1)) {
					case LITERAL_constructor: {
						match(LITERAL_constructor);
						break;
					}
					case LITERAL_ctor: {
						match(LITERAL_ctor);
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				{
					switch (LA(1)) {
					case IDENT: {
						match(IDENT);
						break;
					}
					case LCURLY: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
					/* scope(pc); */
				break;
			}
			case LITERAL_destructor:
			case LITERAL_dtor: {
				{
					switch (LA(1)) {
					case LITERAL_destructor: {
						match(LITERAL_destructor);
						break;
					}
					case LITERAL_dtor: {
						match(LITERAL_dtor);
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
					/* scope(pc); */
				break;
			}
			case IDENT: {
				functionDef(pc.funcDef());
				break;
			}
			case LITERAL_var:
			case LITERAL_const: {
				varStmt(pc.statementClosure());
				break;
			}
			case LITERAL_type:
			case LITERAL_struct: {
				typeAlias();
				break;
			}
				case RCURLY: {
					break _loop3001;
				}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} while (true);
		} finally { // debugging
			fireExitRule(8, 0);
		}
	}

	public final void namespaceScope(NamespaceStatement cr) throws RecognitionException, TokenStreamException {

		fireEnterRule(1001, 0);
		try { // debugging

			switch (LA(1)) {
//			case LITERAL_constructor:
//			case LITERAL_ctor: {
//				{
//					switch (LA(1)) {
//					case LITERAL_constructor: {
//						match(LITERAL_constructor);
//						break;
//					}
//					case LITERAL_ctor: {
//						match(LITERAL_ctor);
//						break;
//					}
//					default: {
//						throw new NoViableAltException(LT(1), getFilename());
//					}
//					}
//				}
//				{
//					switch (LA(1)) {
//					case IDENT: {
//						match(IDENT);
//						break;
//					}
//					case LCURLY: {
//						break;
//					}
//					default: {
//						throw new NoViableAltException(LT(1), getFilename());
//					}
//					}
//				}
//				scope(cr);
//				break;
//			}
//			case LITERAL_destructor:
//			case LITERAL_dtor: {
//				{
//					switch (LA(1)) {
//					case LITERAL_destructor: {
//						match(LITERAL_destructor);
//						break;
//					}
//					case LITERAL_dtor: {
//						match(LITERAL_dtor);
//						break;
//					}
//					default: {
//						throw new NoViableAltException(LT(1), getFilename());
//					}
//					}
//				}
//				scope(cr);
//				break;
//			}
			case IDENT: {
				functionDef(cr.funcDef());
				break;
			}
			case LITERAL_var:
			case LITERAL_const: {
				varStmt(cr.statementClosure());
				break;
			}
			case LITERAL_type:
			case LITERAL_struct: {
				typeAlias();
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(1001, 0);
		}
	}

	public final void classStatement(ClassStatement cls) throws RecognitionException, TokenStreamException {

		fireEnterRule(4, 0);
		try { // debugging
			Token i1 = null;

			match(LITERAL_class);
			i1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
				cls.setName(i1);
			}
			{
				switch (LA(1)) {
				case LPAREN: {
					classInheritance(cls.classInheritance());
					break;
				}
				case LCURLY: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(LCURLY);
			{
				switch (LA(1)) {
				case STRING_LITERAL: {
					docstrings(cls);
					break;
				}
				case IDENT:
				case LITERAL_constructor:
				case LITERAL_ctor:
				case LITERAL_destructor:
				case LITERAL_dtor:
				case LITERAL_var:
				case LITERAL_const:
				case LITERAL_type:
				case LITERAL_struct: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			classScope(cls);
			match(RCURLY);
		} finally { // debugging
			fireExitRule(4, 0);
		}
	}

	private void docstrings(Documentable sc) throws RecognitionException, NoViableAltException, TokenStreamException {
		fireEnterRule(7, 0);
		try { // debugging
			Token s1 = null;

			{
				int _cnt25 = 0;
				_loop25: do {
					if ((LA(1) == STRING_LITERAL) && (_tokenSet_2.member(LA(2)))) {
						s1 = LT(1);
						match(STRING_LITERAL);
						if (inputState.guessing == 0) {
							sc.addDocString(s1);
						}
					} else {
						if (_cnt25 >= 1) {
							break _loop25;
						} else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}

					_cnt25++;
				} while (true);
			}
		} finally { // debugging
			fireExitRule(7, 0);
		}
	}

	public final void namespaceStatement(NamespaceStatement the_namespace) throws RecognitionException, TokenStreamException {

		fireEnterRule(1000, 0); // TODO wrong
		try { // debugging
			Token i1 = null;

			match(LITERAL_namespace);
			i1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
				the_namespace.setName(i1);
			}
//			{
//				switch (LA(1)) {
//				case LPAREN: {
//					classInheritance(the_namespace.classInheritance());
//					break;
//				}
//				case LCURLY: {
//					break;
//				}
//				default: {
//					throw new NoViableAltException(LT(1), getFilename());
//				}
//				}
//			}
			match(LCURLY);
			{
				switch (LA(1)) {
				case STRING_LITERAL: {
					docstrings(the_namespace);
					break;
				}
				case IDENT:
				case LITERAL_constructor:
				case LITERAL_ctor:
				case LITERAL_destructor:
				case LITERAL_dtor:
				case LITERAL_var:
				case LITERAL_const:
				case LITERAL_type:
				case LITERAL_struct: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			namespaceScope(the_namespace);
			match(RCURLY);
		} finally { // debugging
			fireExitRule(1000, 0);
		}
	}

	public final IExpression conditionalExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(46, 0);
		try { // debugging

			e = logicalOrExpression();
			{
				switch (LA(1)) {
				case QUESTION: {
					match(QUESTION);
					assignmentExpression();
					match(TOK_COLON);
					conditionalExpression();
					break;
				}
				case IDENT:
				case TOK_COLON:
				case SEMI:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_to:
				case LITERAL_with:
				case LITERAL_var:
				case LITERAL_const:
				case BECOMES:
				case LITERAL_if:
				case LBRACK:
				case RBRACK:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			return e;
		} finally { // debugging
			fireExitRule(46, 0);
		}
	}

	public final IExpression constantValue() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(1, 0);
		try { // debugging
			Token s = null;
			Token c = null;
			Token n = null;
			e = null;

			switch (LA(1)) {
			case STRING_LITERAL: {
				s = LT(1);
				match(STRING_LITERAL);
				if (inputState.guessing == 0) {
					e = new StringExpression(s);
				}
				break;
			}
			case CHAR_LITERAL: {
				c = LT(1);
				match(CHAR_LITERAL);
				if (inputState.guessing == 0) {
//					out.pushCharLit(c.getText());
					e=new CharLitExpression(c);
				}
				break;
			}
			case NUM_INT: {
				n = LT(1);
				match(NUM_INT);
				if (inputState.guessing == 0) {
//					e = new NumericExpression(Integer.parseInt(n.getText()));
					e = new NumericExpression(n);
				}
				break;
			}
			case IDENT: {
				e = variableReference();
				break;
			}
			case LBRACK: {
				match(LBRACK);
				if (inputState.guessing == 0) {
					e = new ListExpression();
				}
				expressionList(((ListExpression) e).contents());
				match(RBRACK);
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			return e;
		} finally { // debugging
			fireExitRule(1, 0);
		}
	}

	public final void docstrings(Scope sc) throws RecognitionException, TokenStreamException {

		fireEnterRule(7, 0);
		try { // debugging
			Token s1 = null;

			{
				int _cnt25 = 0;
				_loop25: do {
					if ((LA(1) == STRING_LITERAL) && (_tokenSet_2.member(LA(2)))) {
						s1 = LT(1);
						match(STRING_LITERAL);
						if (inputState.guessing == 0) {
							sc.addDocString(s1);
						}
					} else {
						if (_cnt25 >= 1) {
							break _loop25;
						} else {
							throw new NoViableAltException(LT(1), getFilename());
						}
					}

					_cnt25++;
				} while (true);
			}
		} finally { // debugging
			fireExitRule(7, 0);
		}
	}

	private final void elseif_part(IfExpression ifex) throws RecognitionException, TokenStreamException {

		fireEnterRule(29, 0);
		try { // debugging

			match(LITERAL_elseif);
			expr = expression();
			if (inputState.guessing == 0) {
				ifex.expr(expr);
			}
			scope(ifex.scope());
		} finally { // debugging
			fireExitRule(29, 0);
		}
	}

	public final IExpression equalityExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(52, 0);
		try { // debugging

			e = relationalExpression();
			{
				_loop142: do {
					if ((LA(1) == EQUALITY || LA(1) == NOT_EQUALS)) {
						{
							switch (LA(1)) {
							case EQUALITY: {
								match(EQUALITY);
								break;
							}
							case NOT_EQUALS: {
								match(NOT_EQUALS);
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						relationalExpression();
					} else {
						break _loop142;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(52, 0);
		}
	}

	public final IExpression exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(50, 0);
		try { // debugging

			e = andExpression();
			{
				_loop135: do {
					if ((LA(1) == BXOR)) {
						match(BXOR);
						andExpression();
					} else {
						break _loop135;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(50, 0);
		}
	}

	public final IExpression expression() throws RecognitionException, TokenStreamException {
		IExpression ex;

		fireEnterRule(18, 0);
		try { // debugging

			ex = assignmentExpression();
			return ex;
		} finally { // debugging
			fireExitRule(18, 0);
		}
	}

	public final void expressionList(ExpressionList el) throws RecognitionException, TokenStreamException {

		fireEnterRule(45, 0);
		try { // debugging

			expr = expression();
			if (inputState.guessing == 0) {
				el.next(expr);
			}
			{
				_loop118: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						expr = expression();
						if (inputState.guessing == 0) {
							el.next(expr);
						}
					} else {
						break _loop118;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(45, 0);
		}
	}

	public final ExpressionList expressionList2() throws RecognitionException, TokenStreamException {

		ExpressionList el = new ExpressionList();
		
		fireEnterRule(45, 0);
		try { // debugging

			expr = expression();
			if (inputState.guessing == 0) {
				el.next(expr);
			}
			{
				_loop118: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						expr = expression();
						if (inputState.guessing == 0) {
							el.next(expr);
						}
					} else {
						break _loop118;
					}

				} while (true);
			}
			return el;
		} finally { // debugging
			fireExitRule(45, 0);
		}
	}

	public final void formalArgList(FormalArgList fal) throws RecognitionException, TokenStreamException {

		fireEnterRule(22, 0);
		try { // debugging

			formalArgListItem_priv(fal.next());
			{
				_loop108: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						formalArgListItem_priv(fal.next());
					} else {
						break _loop108;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(22, 0);
		}
	}

	private final void formalArgListItem_priv(FormalArgListItem fali)
			throws RecognitionException, TokenStreamException {

		fireEnterRule(42, 0);
		try { // debugging
			Token i = null;

			{
				switch (LA(1)) {
				case LITERAL_const:
				case LITERAL_ref:
				case LITERAL_in:
				case LITERAL_out: {
					regularQualifiers(fali.typeName());
					break;
				}
				case IDENT:
				case LITERAL_generic: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case IDENT: {
					i = LT(1);
					match(IDENT);
					if (inputState.guessing == 0) {
						fali.setName(i);
					}
					{
						switch (LA(1)) {
						case TOK_COLON: {
							match(TOK_COLON);
							formalArgTypeName(fali.typeName());
							break;
						}
						case RPAREN:
						case COMMA: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					break;
				}
				case LITERAL_generic: {
					abstractGenericTypeName_xx(fali.typeName());
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} finally { // debugging
			fireExitRule(42, 0);
		}
	}

	public final void formalArgTypeName(TypeName tn) throws RecognitionException, TokenStreamException {

		fireEnterRule(34, 0);
		try { // debugging

			switch (LA(1)) {
			case IDENT:
			case LITERAL_const:
			case LITERAL_typeof:
			case LITERAL_ref:
			case LITERAL_generic: {
				structTypeName(tn);
				break;
			}
			case LITERAL_function:
			case LITERAL_procedure: {
				funcTypeExpr(tn);
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(34, 0);
		}
	}

	public final void frobeIteration(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(26, 0);
		try { // debugging
			Token i1 = null;
			Loop loop = pc.loop();

			match(LITERAL_iterate);
			{
				switch (LA(1)) {
				case LITERAL_from: {
					match(LITERAL_from);
					if (inputState.guessing == 0) {
						loop.type(LoopTypes2.FROM_TO_TYPE);
					}
					expr = expression();
					if (inputState.guessing == 0) {
						loop.frompart(expr);
					}
					match(LITERAL_to);
					expr = expression();
					if (inputState.guessing == 0) {
						loop.topart(expr);
					}
					break;
				}
				case LITERAL_to: {
					match(LITERAL_to);
					if (inputState.guessing == 0) {
						loop.type(LoopTypes2.TO_TYPE);
					}
					expr = expression();
					if (inputState.guessing == 0) {
						loop.topart(expr);
					}
					break;
				}
				case IDENT:
				case LPAREN:
				case STRING_LITERAL:
				case LBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT: {
					if (inputState.guessing == 0) {
						loop.type(LoopTypes2.EXPR_TYPE);
					}
					expr = expression();
					if (inputState.guessing == 0) {
						loop.topart(expr);
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case LITERAL_with: {
					match(LITERAL_with);
					i1 = LT(1);
					match(IDENT);
					if (inputState.guessing == 0) {
						loop.iterName(i1);
					}
					break;
				}
				case LCURLY: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			scope(loop.scope());
		} finally { // debugging
			fireExitRule(26, 0);
		}
	}

	public final void functionDef(FunctionDef fd) throws RecognitionException, TokenStreamException {

		fireEnterRule(11, 0);
		try { // debugging
			Token i1 = null;

			i1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
				fd.setName(i1);
			}
			opfal(fd.fal());
			switch (LA(1)) {
			case TOK_ARROW: 
			{
				match (TOK_ARROW);
				typeName(fd.returnType());
			}
			}
			scope(fd.scope());
		} finally { // debugging
			fireExitRule(11, 0);
		}
	}

	public final void functionScope(Scope pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(19, 0);
		try { // debugging

			match(LCURLY);
			{
				switch (LA(1)) {
				case STRING_LITERAL: {
					docstrings(pc);
					break;
				}
				case IDENT:
				case RCURLY:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_var:
				case LITERAL_const:
				case LITERAL_if:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				_loop41: do {
					if ((_tokenSet_11.member(LA(1)))) {
						functionStatement(pc.statementClosure());
					} else {
						break _loop41;
					}

				} while (true);
			}
			match(RCURLY);
		} finally { // debugging
			fireExitRule(19, 0);
		}
	}

	public final void functionStatement(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(20, 0);
		try { // debugging

			switch (LA(1)) {
			case IDENT:
			case LITERAL_construct:
			case LITERAL_yield:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			case LITERAL_var:
			case LITERAL_const:
			case LITERAL_if: {
				baseStatement(pc);
				break;
			}
			case LITERAL_block: {
				block(pc.blockClosure());
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(20, 0);
		}
	}

	public final void funcTypeExpr(TypeName pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(35, 0);
		try { // debugging

			switch (LA(1)) {
			case LITERAL_function: {
				match(LITERAL_function);
				if (inputState.guessing == 0) {
					pc.type(TypeModifiers.FUNCTION);
				}
				{
					if ((LA(1) == LPAREN) && (_tokenSet_15.member(LA(2)))) {
						match(LPAREN);
						{
							switch (LA(1)) {
							case IDENT:
							case LITERAL_const:
							case LITERAL_typeof:
							case LITERAL_ref:
							case LITERAL_generic:
							case LITERAL_function:
							case LITERAL_procedure: {
								typeNameList(pc.argList());
								break;
							}
							case RPAREN: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						match(RPAREN);
					} else if ((_tokenSet_16.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
					} else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				{
					switch (LA(1)) {
					case TOK_ARROW: {
						match(TOK_ARROW);
						typeName(pc.returnValue());
						break;
					}
					case TOK_COLON: {
						match(TOK_COLON);
						typeName(pc.returnValue());
						break;
					}
					case IDENT:
					case SEMI:
					case LPAREN:
					case RPAREN:
					case LCURLY:
					case RCURLY:
					case COMMA:
					case STRING_LITERAL:
					case LITERAL_construct:
					case LITERAL_yield:
					case LITERAL_while:
					case LITERAL_do:
					case LITERAL_iterate:
					case LITERAL_to:
					case LITERAL_with:
					case LITERAL_var:
					case LITERAL_const:
					case BECOMES:
					case LITERAL_if:
					case LITERAL_typeof:
					case LITERAL_ref:
					case LITERAL_generic:
					case LBRACK:
					case RBRACK:
					case LITERAL_function:
					case LITERAL_procedure:
					case PLUS_ASSIGN:
					case MINUS_ASSIGN:
					case STAR_ASSIGN:
					case DIV_ASSIGN:
					case MOD_ASSIGN:
					case SR_ASSIGN:
					case BSR_ASSIGN:
					case SL_ASSIGN:
					case BAND_ASSIGN:
					case BXOR_ASSIGN:
					case BOR_ASSIGN:
					case QUESTION:
					case LOR:
					case LAND:
					case BOR:
					case BXOR:
					case BAND:
					case EQUALITY:
					case NOT_EQUALS:
					case PLUS:
					case MINUS:
					case INC:
					case DEC:
					case BNOT:
					case LNOT:
					case CHAR_LITERAL:
					case NUM_INT:
					case LITERAL_block: {
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				break;
			}
			case LITERAL_procedure: {
				match(LITERAL_procedure);
				if (inputState.guessing == 0) {
					pc.type(TypeModifiers.PROCEDURE);
				}
				{
					if ((LA(1) == LPAREN) && (_tokenSet_15.member(LA(2)))) {
						match(LPAREN);
						{
							switch (LA(1)) {
							case IDENT:
							case LITERAL_const:
							case LITERAL_typeof:
							case LITERAL_ref:
							case LITERAL_generic:
							case LITERAL_function:
							case LITERAL_procedure: {
								typeNameList(pc.argList());
								break;
							}
							case RPAREN: {
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						match(RPAREN);
					} else if ((_tokenSet_13.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
					} else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(35, 0);
		}
	}

	public final void genericQualifiers(TypeName pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(31, 0);
		try { // debugging

			{
				switch (LA(1)) {
				case LITERAL_const: {
					match(LITERAL_const);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.CONST);
					}
					break;
				}
				case IDENT:
				case LITERAL_ref:
				case LITERAL_generic: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case LITERAL_ref: {
					match(LITERAL_ref);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.REFPAR);
					}
					break;
				}
				case IDENT:
				case LITERAL_generic: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} finally { // debugging
			fireExitRule(31, 0);
		}
	}

	public final IdentExpression ident() throws RecognitionException, TokenStreamException {
		IdentExpression id;

		fireEnterRule(43, 0);
		try { // debugging
			Token r1 = null;
			id = null;


			r1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
//				out.pushString(r1.getText());
				id = new IdentExpression(r1);
			}
			return id;
		} finally { // debugging
			fireExitRule(43, 0);
		}
	}

//	public final String ident2() throws RecognitionException, TokenStreamException {
//		String ident;
//
//		fireEnterRule(40, 0);
//		try { // debugging
//			Token r1 = null;
//			ident = null;
//
//			r1 = LT(1);
//			match(IDENT);
//			if (inputState.guessing == 0) {
//				ident = r1.getText();
//			}
//			return ident;
//		} finally { // debugging
//			fireExitRule(40, 0);
//		}
//	}

	public final void identList(IdentList ail) throws RecognitionException, TokenStreamException {

		fireEnterRule(17, 0);
		try { // debugging
			IdentExpression s = null;

			s = ident();
			if (inputState.guessing == 0) {
				ail.push(s);
			}
			{
				_loop91: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						s = ident();
						if (inputState.guessing == 0) {
							ail.push(s);
						}
					} else {
						break _loop91;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(17, 0);
		}
	}

	public final void ifConditional(IfExpression ifex) throws RecognitionException, TokenStreamException {

		fireEnterRule(24, 0);
		try { // debugging

			match(LITERAL_if);
			expr = expression();
			if (inputState.guessing == 0) {
				ifex.expr(expr);
			}
			scope(ifex.scope());
			{
				_loop63: do {
					switch (LA(1)) {
					case LITERAL_else: {
						match(LITERAL_else);
						scope(ifex.else_().scope());
						break;
					}
					case LITERAL_elseif: {
						elseif_part(ifex.elseif()); // TODO ??
						break;
					}
					default: {
						break _loop63;
					}
					}
				} while (true);
			}
		} finally { // debugging
			fireExitRule(24, 0);
		}
	}

	public final void importStatement(ImportStatement pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(5, 0);
		try { // debugging

			{
				switch (LA(1)) {
				case LITERAL_from: {
					match(LITERAL_from);
					Qualident xyz = qualident();
					if (inputState.guessing == 0) {
						pc.importRoot(xyz);
					}
					break;
				}
				case LITERAL_import: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(LITERAL_import);
			qualidentList(pc.importList());
		} finally { // debugging
			fireExitRule(5, 0);
		}
	}

	public final IExpression inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(49, 0);
		try { // debugging

			e = exclusiveOrExpression();
			{
				_loop132: do {
					if ((LA(1) == BOR)) {
						match(BOR);
						exclusiveOrExpression();
					} else {
						break _loop132;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(49, 0);
		}
	}

	public final IExpression logicalAndExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(48, 0);
		try { // debugging

			e = inclusiveOrExpression();
			{
				_loop129: do {
					if ((LA(1) == LAND)) {
						match(LAND);
						inclusiveOrExpression();
					} else {
						break _loop129;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(48, 0);
		}
	}

	public final IExpression logicalOrExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(47, 0);
		try { // debugging

			e = logicalAndExpression();
			{
				_loop126: do {
					if ((LA(1) == LOR)) {
						match(LOR);
						logicalAndExpression();
					} else {
						break _loop126;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(47, 0);
		}
	}

	public final IExpression multiplicativeExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(56, 0);
		try { // debugging

			e = qidentExpression();
			{
				_loop159: do {
					if (((LA(1) >= STAR && LA(1) <= MOD))) {
						{
							switch (LA(1)) {
							case STAR: {
								match(STAR);
								if (inputState.guessing == 0) {
									e=ExpressionBuilder.buildPartial(e, ExpressionKind.MULTIPLY);
								}
								break;
							}
							case DIV: {
								match(DIV);
								if (inputState.guessing == 0) {
									e=ExpressionBuilder.buildPartial(e, ExpressionKind.DIVIDE);
								}
								break;
							}
							case MOD: {
								match(MOD);
								if (inputState.guessing == 0) {
									e=ExpressionBuilder.buildPartial(e, ExpressionKind.MODULO);
								}
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						qidentExpression();
					} else {
						break _loop159;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(56, 0);
		}
	}

	public final void opfal(FormalArgList fal) throws RecognitionException, TokenStreamException {

		fireEnterRule(14, 0);
		try { // debugging

			match(LPAREN);
			{
				switch (LA(1)) {
				case IDENT:
				case LITERAL_const:
				case LITERAL_ref:
				case LITERAL_in:
				case LITERAL_out:
				case LITERAL_generic: {
					formalArgList(fal);
					break;
				}
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(RPAREN);
		} finally { // debugging
			fireExitRule(14, 0);
		}
	}

	public final FormalArgList opfal2() throws RecognitionException, TokenStreamException {
		FormalArgList fal = new FormalArgList();
		
		fireEnterRule(14, 0);
		try { // debugging

			match(LPAREN);
			{
				switch (LA(1)) {
				case IDENT:
				case LITERAL_const:
				case LITERAL_ref:
				case LITERAL_in:
				case LITERAL_out:
				case LITERAL_generic: {
					formalArgList(fal);
					break;
				}
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(RPAREN);
			
			return fal;
		} finally { // debugging
			fireExitRule(14, 0);
		}
	}

	public final IExpression postfixExpression_priv() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(60, 0);
		try { // debugging
			Token in = null;
			Token de = null;

			{
				e = primitiveElement();
				{
					if ((LA(1) == INC) && (_tokenSet_17.member(LA(2)))) {
						in = LT(1);
						match(INC);
						if (inputState.guessing == 0) {
							e = ExpressionBuilder.build(e, ExpressionKind.POST_INCREMENT);
						}
					} else if ((LA(1) == DEC) && (_tokenSet_17.member(LA(2)))) {
						de = LT(1);
						match(DEC);
						if (inputState.guessing == 0) {
							e = ExpressionBuilder.build(e, ExpressionKind.POST_DECREMENT);
						}
					} else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))) {
					} else {
						throw new NoViableAltException(LT(1), getFilename());
					}

				}
			}
			return e;
		} finally { // debugging
			fireExitRule(60, 0);
		}
	}

	public final IExpression primitiveElement() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(61, 0);
		try { // debugging
			e = null;

			switch (LA(1)) {
			case IDENT:
			case STRING_LITERAL:
			case LBRACK:
			case CHAR_LITERAL:
			case NUM_INT: {
				e = constantValue();
				break;
			}
			case LPAREN: {
				match(LPAREN);
				expr = expression();
				if (inputState.guessing == 0) {
					e = new SubExpression(expr);
				}
				match(RPAREN);
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			return e;
		} finally { // debugging
			fireExitRule(61, 0);
		}
	}

	public final void procCallEx(ProcedureCallExpression pce) throws RecognitionException, TokenStreamException {

		fireEnterRule(27, 0);
		try { // debugging

			match(LPAREN);
			{
				switch (LA(1)) {
				case IDENT:
				case LPAREN:
				case STRING_LITERAL:
				case LBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT: {
					expressionList(pce.exprList());
					break;
				}
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(RPAREN);
		} finally { // debugging
			fireExitRule(27, 0);
		}
	}

	public final void procCallEx2() throws RecognitionException, TokenStreamException {

		fireEnterRule(63, 0);
		try { // debugging

			match(LPAREN);
			{
				switch (LA(1)) {
				case IDENT:
				case LPAREN:
				case STRING_LITERAL:
				case LBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT: {
					expressionList(null/* fixme */);
					break;
				}
				case RPAREN: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			match(RPAREN);
		} finally { // debugging
			fireExitRule(63, 0);
		}
	}

	public final void procedureCallExpression(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(23, 0);
		try { // debugging
			ProcedureCallExpression pce = pc.procedureCallExpression();

			Qualident xyz = qualident();
			if (inputState.guessing == 0) {
				pce.identifier(xyz);
			}
			procCallEx(pce);
		} finally { // debugging
			fireExitRule(23, 0);
		}
	}

	public final void program() throws RecognitionException, TokenStreamException {

		fireEnterRule(0, 0);
		try { // debugging
			ParserClosure pc = out.closure();

			{
				switch (LA(1)) {
				case LITERAL_indexing: {
					match(LITERAL_indexing);
					{
						_loop4: do {
							if ((LA(1) == IDENT)) {
								Token i1 = LT(1);
								match(IDENT);
								match(TOK_COLON);
								IExpression c1 = constantValue();
								pc.module.addIndexingItem(i1, c1);
							} else {
								break _loop4;
							}

						} while (true);
					}
					break;
				}
				case EOF:
				case LITERAL_package:
				case LITERAL_namespace:
				case LITERAL_imports:
				case LITERAL_class:
				case LITERAL_from:
				case LITERAL_import: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case LITERAL_package: {
					match(LITERAL_package); // TODO packageDeclaration
					Qualident xyz = qualident();
					if (inputState.guessing == 0) {
//						pc.packageName(xyz); // TODO look here
						pc.module.pushPackageName(xyz);
					}
					{
						switch (LA(1)) {
						case SEMI: {
							match(SEMI);
							break;
						}
						case EOF:
						case LITERAL_namespace:
						case LITERAL_imports:
						case LITERAL_class:
						case LITERAL_from:
						case LITERAL_import: {
							break;
						}
						default: {
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
					}
					break;
				}
				case EOF:
				case LITERAL_namespace:
				case LITERAL_imports:
				case LITERAL_class:
				case LITERAL_from:
				case LITERAL_import: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				_loop8: do {
					if ((_tokenSet_0.member(LA(1)))) {
						programStatement(pc);
					} else {
						break _loop8;
					}

				} while (true);
			}
			{
				match(Token.EOF_TYPE);
				out.FinishModule();
			}
		} finally { // debugging
			fireExitRule(0, 0);
		}
	}

	public final void programStatement(ParserClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(3, 0);
		try { // debugging

			switch (LA(1)) {
			case LITERAL_namespace: {
				namespaceStatement(pc.namespaceStatement());
//				match(LITERAL_namespace);
//				{
//					switch (LA(1)) {
//					case IDENT: {
//						match(IDENT);
//						break;
//					}
//					case LPAREN: {
//						break;
//					}
//					default: {
//						throw new NoViableAltException(LT(1), getFilename());
//					}
//					}
//				}
//				match(LPAREN);
//				match(RPAREN);
				break;
			}
			case LITERAL_class: {
				classStatement(pc.classStatement());
				break;
			}
			case LITERAL_from:
			case LITERAL_import: {
				importStatement(pc.importStatement());
				break;
			}
			case LITERAL_imports: {
				match(LITERAL_imports);
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(3, 0);
		}
	}

	public final IExpression qidentExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(57, 0);
		try { // debugging
			IExpression e2;

			e = unaryExpression();
			{
				_loop162: do {
					if ((LA(1) == QIDENT)) {
						match(QIDENT);
						e2 = unaryExpression();
						if (inputState.guessing == 0) {
							e = ExpressionBuilder.build(e, ExpressionKind.QIDENT, (e2));
						}
					} else {
						break _loop162;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(57, 0);
		}
	}

	public final Qualident qualident() throws RecognitionException, TokenStreamException {
//		String pc;
		Qualident r = new Qualident();

		fireEnterRule(2, 0);
		try { // debugging
			Token r1 = null;
			Token r2 = null;
//			StringBuffer r = new StringBuffer();
//			pc = null;

			r1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
//				r.append(r1.getText());
				r.append(r1);
			}
			{
				switch (LA(1)) {
				case DOT: {
					{
						int _cnt99 = 0;
						_loop99: do {
							if ((LA(1) == DOT)) {
								Token d1 = LT(1);
								match(DOT);
								r2 = LT(1);
								match(IDENT);
								if (inputState.guessing == 0) {
//									r.append('.');
									r.appendDot(d1);
//									r.append(r2.getText());
									r.append(r2);
								}
							} else {
								if (_cnt99 >= 1) {
									break _loop99;
								} else {
									throw new NoViableAltException(LT(1), getFilename());
								}
							}

							_cnt99++;
						} while (true);
					}
//					if (inputState.guessing == 0) {
//						pc = r.toString();
//					}
					break;
				}
				case EOF:
				case IDENT:
				case TOK_COLON:
				case SEMI:
				case LITERAL_namespace:
				case LPAREN:
				case RPAREN:
				case LITERAL_imports:
				case LITERAL_class:
				case LCURLY:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_to:
				case LITERAL_with:
				case LITERAL_var:
				case LITERAL_const:
				case BECOMES:
				case LITERAL_if:
				case LITERAL_typeof:
				case LITERAL_ref:
				case LITERAL_generic:
				case LBRACK:
				case RBRACK:
				case LITERAL_function:
				case LITERAL_procedure:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case EQUALITY:
				case NOT_EQUALS:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			return r;
		} finally { // debugging
			fireExitRule(2, 0);
		}
	}

	public final void qualidentList(QualidentList qal) throws RecognitionException, TokenStreamException {

		fireEnterRule(41, 0);
		try { // debugging
			Qualident qid;

			qid = qualident();
			if (inputState.guessing == 0) {
				qal.add(qid);
			}
			{
				_loop102: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						qid = qualident();
						if (inputState.guessing == 0) {
							qal.add(qid);
						}
					} else {
						break _loop102;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(41, 0);
		}
	}

	public final void regularQualifiers(TypeName fp) throws RecognitionException, TokenStreamException {

		fireEnterRule(38, 0);
		try { // debugging

			{
				switch (LA(1)) {
				case LITERAL_in: {
					match(LITERAL_in);
					if (inputState.guessing == 0) {
						fp.setIn(true);
					}
					break;
				}
				case LITERAL_out: {
					match(LITERAL_out);
					if (inputState.guessing == 0) {
						fp.setOut(true);
					}
					break;
				}
				default:
					if ((LA(1) == LITERAL_const) && (LA(2) == IDENT || LA(2) == LITERAL_generic)) {
						match(LITERAL_const);
						if (inputState.guessing == 0) {
							fp.setConstant(true);
						}
					} else if ((LA(1) == LITERAL_const || LA(1) == LITERAL_ref)
							&& (LA(2) == IDENT || LA(2) == LITERAL_ref || LA(2) == LITERAL_generic)) {
						{
							{
								switch (LA(1)) {
								case LITERAL_const: {
									match(LITERAL_const);
									if (inputState.guessing == 0) {
										fp.setConstant(true);
									}
									break;
								}
								case LITERAL_ref: {
									break;
								}
								default: {
									throw new NoViableAltException(LT(1), getFilename());
								}
								}
							}
							match(LITERAL_ref);
							if (inputState.guessing == 0) {
								fp.setReference(true);
							}
						}
					} else {
						throw new NoViableAltException(LT(1), getFilename());
					}
				}
			}
		} finally { // debugging
			fireExitRule(38, 0);
		}
	}

	public final IExpression relationalExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(53, 0);
		try { // debugging
			TypeName tn = null;

			e = shiftExpression();
			{
				switch (LA(1)) {
				case IDENT:
				case TOK_COLON:
				case SEMI:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_to:
				case LITERAL_with:
				case LITERAL_var:
				case LITERAL_const:
				case BECOMES:
				case LITERAL_if:
				case LBRACK:
				case RBRACK:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case EQUALITY:
				case NOT_EQUALS:
				case LT_:
				case GT:
				case LTE:
				case GTE:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					{
						_loop147: do {
							if (((LA(1) >= LT_ && LA(1) <= GTE))) {
								{
									switch (LA(1)) {
									case LT_: {
										match(LT_);
										break;
									}
									case GT: {
										match(GT);
										break;
									}
									case LTE: {
										match(LTE);
										break;
									}
									case GTE: {
										match(GTE);
										break;
									}
									default: {
										throw new NoViableAltException(LT(1), getFilename());
									}
									}
								}
								shiftExpression();
							} else {
								break _loop147;
							}

						} while (true);
					}
					break;
				}
				case LITERAL_is_a: {
					match(LITERAL_is_a);
					if (inputState.guessing == 0) {
						tn = new RegularTypeName();
					}
					typeName(tn);
					if (inputState.guessing == 0) {
						e = ExpressionBuilder.build(e, ExpressionKind.IS_A, new TypeNameExpression(tn));
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			return e;
		} finally { // debugging
			fireExitRule(53, 0);
		}
	}

	public final void scope(Scope pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(10, 0);
		try { // debugging

			match(LCURLY);
			{
				if ((LA(1) == STRING_LITERAL) && (_tokenSet_5.member(LA(2)))) {
					docstrings(pc);
				} else if ((_tokenSet_5.member(LA(1))) && (_tokenSet_6.member(LA(2)))) {
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
			{
				_loop37: do {
					if ((_tokenSet_7.member(LA(1))) && (_tokenSet_8.member(LA(2)))) {
						baseStatement(pc.statementClosure());
					} else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2)))) {
						expr = expression();
						if (inputState.guessing == 0) {
							pc.statementWrapper(expr);
						}
					} else {
						break _loop37;
					}

				} while (true);
			}
			match(RCURLY);
		} finally { // debugging
			fireExitRule(10, 0);
		}
	}

	public final IExpression shiftExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(54, 0);
		try { // debugging

			e = additiveExpression();
			{
				_loop151: do {
					if (((LA(1) >= SL && LA(1) <= BSR))) {
						{
							switch (LA(1)) {
							case SL: {
								match(SL);
								break;
							}
							case SR: {
								match(SR);
								break;
							}
							case BSR: {
								match(BSR);
								break;
							}
							default: {
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
						}
						additiveExpression();
					} else {
						break _loop151;
					}

				} while (true);
			}
			return e;
		} finally { // debugging
			fireExitRule(54, 0);
		}
	}

	public final void simpleTypeName_xx(TypeName tn) throws RecognitionException, TokenStreamException {

		Qualident xyz;
		
		fireEnterRule(36, 0);
		try { // debugging

			xyz = qualident();
			if (inputState.guessing == 0) {
				tn.typeName(xyz);
			}
		} finally { // debugging
			fireExitRule(36, 0);
		}
	}

	public final void specifiedGenericTypeName_xx(TypeName tn) throws RecognitionException, TokenStreamException {

		fireEnterRule(33, 0);
		try { // debugging

			simpleTypeName_xx(tn);
			{
				if ((LA(1) == LBRACK) && (_tokenSet_12.member(LA(2)))) {
					match(LBRACK);
					TypeName tn2 = new RegularTypeName();
					typeName(tn2);
					tn.addGenericPart(tn2);
					match(RBRACK);
				} else if ((_tokenSet_13.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
				} else {
					throw new NoViableAltException(LT(1), getFilename());
				}

			}
		} finally { // debugging
			fireExitRule(33, 0);
		}
	}

	public final void statement(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(16, 0);
		try { // debugging

			{
				switch (LA(1)) {
				case IDENT: {
					procedureCallExpression(pc.procCallExpr());
					break;
				}
				case LITERAL_if: {
					ifConditional(pc.ifExpression());
					break;
				}
				case LITERAL_var:
				case LITERAL_const: {
					varStmt(pc);
					break;
				}
				case LITERAL_while:
				case LITERAL_do: {
					whileLoop(pc);
					break;
				}
				case LITERAL_iterate: {
					frobeIteration(pc);
					break;
				}
				case LITERAL_construct: {
					match(LITERAL_construct);
					expr = expression();
					if (inputState.guessing == 0) {
						FormalArgList o = null;
						pc.constructExpression(expr, o);
					}
					break;
				}
				case LITERAL_yield: {
					match(LITERAL_yield);
					expr = expression();
					if (inputState.guessing == 0) {
						pc.yield(expr);
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				_loop48: do {
					if ((LA(1) == SEMI)) {
						match(SEMI);
					} else {
						break _loop48;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(16, 0);
		}
	}

	public final void structTypeName(TypeName pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(30, 0);
		try { // debugging

			switch (LA(1)) {
			case IDENT:
			case LITERAL_const:
			case LITERAL_ref:
			case LITERAL_generic: {
				genericQualifiers(pc);
				{
					switch (LA(1)) {
					case LITERAL_generic: {
						abstractGenericTypeName_xx(pc);
						break;
					}
					case IDENT: {
						specifiedGenericTypeName_xx(pc);
						break;
					}
					default: {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
				}
				break;
			}
			case LITERAL_typeof: {
				match(LITERAL_typeof);
				Qualident xyz = qualident();
				if (inputState.guessing == 0) {
					pc.typeof(xyz);
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(30, 0);
		}
	}

	public final void typeAlias() throws RecognitionException, TokenStreamException {

		fireEnterRule(13, 0);
		try { // debugging

			switch (LA(1)) {
			case LITERAL_type: {
				match(LITERAL_type);
				match(LITERAL_alias);
				IdentExpression x = ident();
				match(BECOMES);
				Qualident y = qualident();
				TypeAliasExpression ty = new TypeAliasExpression();
				ty.make(x, y);
				break;
			}
			case LITERAL_struct: {
				match(LITERAL_struct);
				match(IDENT);
				FormalArgList z = opfal2(); // TODO look
				match(LCURLY);
				match(RCURLY);
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(13, 0);
		}
	}

	public final void typeName(TypeName pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(9, 0);
		try { // debugging

			if ((_tokenSet_3.member(LA(1))) && (_tokenSet_4.member(LA(2)))) {
				structTypeName(pc);
			} else if ((LA(1) == LITERAL_function || LA(1) == LITERAL_procedure)) {
				funcTypeExpr(pc);
			} else if ((LA(1) == IDENT) && (_tokenSet_4.member(LA(2)))) {
				simpleTypeName_xx(pc);
			} else {
				throw new NoViableAltException(LT(1), getFilename());
			}

		} finally { // debugging
			fireExitRule(9, 0);
		}
	}

	public final void typeNameList(TypeNameList pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(39, 0);
		try { // debugging

			typeName(pc.next());
			{
				_loop105: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						typeName(pc.next());
					} else {
						break _loop105;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(39, 0);
		}
	}

	public final IExpression unaryExpression() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(58, 0);
		try { // debugging

			switch (LA(1)) {
			case INC: {
				match(INC);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.INCREMENT);
				}
				break;
			}
			case DEC: {
				match(DEC);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.DECREMENT);
				}
				break;
			}
			case MINUS: {
				match(MINUS);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.NEGATION);
				}
				break;
			}
			case PLUS: {
				match(PLUS);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.POSITIVITY);
				}
				break;
			}
			case IDENT:
			case LPAREN:
			case STRING_LITERAL:
			case LBRACK:
			case BNOT:
			case LNOT:
			case CHAR_LITERAL:
			case NUM_INT: {
				e = unaryExpressionNotPlusMinus();
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			return e;
		} finally { // debugging
			fireExitRule(58, 0);
		}
	}

	public final IExpression unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		IExpression e;

		fireEnterRule(59, 0);
		try { // debugging

			switch (LA(1)) {
			case BNOT: {
				match(BNOT);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.BNOT);
				}
				break;
			}
			case LNOT: {
				match(LNOT);
				e = unaryExpression();
				if (inputState.guessing == 0) {
					e.setKind(ExpressionKind.LNOT);
				}
				break;
			}
			case IDENT:
			case LPAREN:
			case STRING_LITERAL:
			case LBRACK:
			case CHAR_LITERAL:
			case NUM_INT: {
				{
					e = postfixExpression_priv();
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			return e;
		} finally { // debugging
			fireExitRule(59, 0);
		}
	}

	public final void variableQualifiers(TypeName pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(37, 0);
		try { // debugging

			{
				switch (LA(1)) {
				case LITERAL_once: {
					match(LITERAL_once);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.ONCE);
					}
					break;
				}
				case EOF:
				case LITERAL_const:
				case LITERAL_local:
				case LITERAL_tagged:
				case LITERAL_pooled:
				case LITERAL_manual:
				case LITERAL_gc: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case LITERAL_local: {
					match(LITERAL_local);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.LOCAL);
					}
					break;
				}
				case LITERAL_tagged: {
					match(LITERAL_tagged);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.TAGGED);
					}
					break;
				}
				case LITERAL_const: {
					match(LITERAL_const);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.CONST);
					}
					break;
				}
				case LITERAL_pooled: {
					match(LITERAL_pooled);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.POOLED);
					}
					break;
				}
				case LITERAL_manual: {
					match(LITERAL_manual);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.MANUAL);
					}
					break;
				}
				case LITERAL_gc: {
					match(LITERAL_gc);
					if (inputState.guessing == 0) {
						pc.set(TypeModifiers.GC);
					}
					break;
				}
				case EOF: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} finally { // debugging
			fireExitRule(37, 0);
		}
	}

	public final IExpression variableReference() throws RecognitionException, TokenStreamException {

		fireEnterRule(62, 0);
		try { // debugging
			Token r1 = null;
			Token r2 = null;
			Token r3 = null;
			IExpression rr = null;
			VariableReference vr = new VariableReference();

			r1 = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
				vr.setMain(r1.getText());  
				rr=vr;
			}
			{
				_loop183: do {
					if ((LA(1) == DOT)) {
						match(DOT);
						r2 = LT(1);
						match(IDENT);
						if (inputState.guessing == 0) {
							vr.addIdentPart(r2.getText());
						}
					} else if ((LA(1) == TOK_COLON) && (LA(2) == TOK_COLON)) {
						match(TOK_COLON);
						match(TOK_COLON);
						r3 = LT(1);
						match(IDENT);
						if (inputState.guessing == 0) {
							vr.addColonIdentPart(r3.getText());
						}
					} else if ((LA(1) == LBRACK) && (_tokenSet_9.member(LA(2)))) {
						match(LBRACK);
						expr = expression();
						match(RBRACK);
						if (inputState.guessing == 0) {
							vr.addArrayPart(expr);
						}
					} else if ((LA(1) == LPAREN) && (_tokenSet_19.member(LA(2)))) {
						ProcedureCallExpression pce1 = //new ProcedureCallExpression();
								vr.procCallPart();
						procCallEx(pce1);
						if (inputState.guessing == 0) {
							pce1.setLeft(rr);
							vr.addProcCallPart(pce1);
						}
						rr=pce1;
					} else {
						break _loop183;
					}

				} while (true);
				return rr;
			}
		} finally { // debugging
			fireExitRule(62, 0);
		}
	}

	public final void varStmt(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(12, 0);
		try { // debugging
			@NonNull
			VariableSequence vsq = null;

			{
				switch (LA(1)) {
				case LITERAL_var: {
					match(LITERAL_var);
					if (inputState.guessing == 0) {
						vsq = pc.varSeq();/* = new VariableStatement(); */
					}
					break;
				}
				case LITERAL_const: {
					match(LITERAL_const);
					if (inputState.guessing == 0) {
						vsq = pc.varSeq();
						vsq.defaultModifiers(TypeModifiers.CONST);
					}
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			varStmt_i(vsq.next());
			{
				_loop57: do {
					if ((LA(1) == COMMA)) {
						match(COMMA);
						varStmt_i(vsq.next());
					} else {
						break _loop57;
					}

				} while (true);
			}
		} finally { // debugging
			fireExitRule(12, 0);
		}
	}

	private final void varStmt_i(VariableStatement vs) throws RecognitionException, TokenStreamException {

		fireEnterRule(28, 0);
		try { // debugging
			Token i = null;

			i = LT(1);
			match(IDENT);
			if (inputState.guessing == 0) {
				vs.setName(i);
			}
			{
				switch (LA(1)) {
				case TOK_COLON: {
					match(TOK_COLON);
					typeName(vs.typeName());
					break;
				}
				case IDENT:
				case SEMI:
				case LPAREN:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_var:
				case LITERAL_const:
				case BECOMES:
				case LITERAL_if:
				case LBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			{
				switch (LA(1)) {
				case BECOMES: {
					match(BECOMES);
					expr = expression();
					if (inputState.guessing == 0) {
						vs.initial(expr);
					}
					break;
				}
				case IDENT:
				case SEMI:
				case LPAREN:
				case RCURLY:
				case COMMA:
				case STRING_LITERAL:
				case LITERAL_construct:
				case LITERAL_yield:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				case LITERAL_var:
				case LITERAL_const:
				case LITERAL_if:
				case LBRACK:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case CHAR_LITERAL:
				case NUM_INT:
				case LITERAL_block: {
					break;
				}
				default: {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
		} finally { // debugging
			fireExitRule(28, 0);
		}
	}

	public final void whileLoop(StatementClosure pc) throws RecognitionException, TokenStreamException {

		fireEnterRule(25, 0);
		try { // debugging
			Loop loop = pc.loop();

			switch (LA(1)) {
			case LITERAL_while: {
				match(LITERAL_while);
				if (inputState.guessing == 0) {
					loop.type(LoopTypes2.WHILE);
				}
				expr = expression();
				if (inputState.guessing == 0) {
					loop.expr(expr);
				}
				scope(loop.scope());
				break;
			}
			case LITERAL_do: {
				match(LITERAL_do);
				if (inputState.guessing == 0) {
					loop.type(LoopTypes2.DO_WHILE);
				}
				scope(loop.scope());
				match(LITERAL_while);
				expr = expression();
				if (inputState.guessing == 0) {
					loop.expr(expr);
				}
				break;
			}
			default: {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		} finally { // debugging
			fireExitRule(25, 0);
		}
	}

}
