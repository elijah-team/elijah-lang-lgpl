package tripleo.elijah;

// $ANTLR 2.7.1: "osc.g" -> "elijahLexer.java"$

import java.io.*;
import java.util.Hashtable;

import antlr.*;
import antlr.collections.impl.BitSet;

public class ElijahLexer extends antlr.debug.DebuggingCharScanner implements
		ElijahTokenTypes, TokenStream {
	public ElijahLexer(InputStream in) {
		this(new ByteBuffer(in));
	}

	public ElijahLexer(Reader in) {
		this(new CharBuffer(in));
	}

	public ElijahLexer(InputBuffer ib) {
		this(
				new LexerSharedInputState(new antlr.debug.DebuggingInputBuffer(
						ib)));
	}

	public ElijahLexer(LexerSharedInputState state) {
		super(state);
		ruleNames = _ruleNames;
		semPredNames = _semPredNames;
		setupDebugging();
		literals = new Hashtable();
		literals.put(new ANTLRHashString("block", this), new Integer(96));
		literals.put(new ANTLRHashString("class", this), new Integer(16));
		literals.put(new ANTLRHashString("procedure", this), new Integer(55));
		literals.put(new ANTLRHashString("tagged", this), new Integer(43));
		literals.put(new ANTLRHashString("to", this), new Integer(32));
		literals.put(new ANTLRHashString("package", this), new Integer(10));
		literals.put(new ANTLRHashString("indexing", this), new Integer(7));
		literals.put(new ANTLRHashString("iterate", this), new Integer(31));
		literals.put(new ANTLRHashString("yield", this), new Integer(28));
		literals.put(new ANTLRHashString("import", this), new Integer(26));
		literals.put(new ANTLRHashString("dtor", this), new Integer(24));
		literals.put(new ANTLRHashString("const", this), new Integer(35));
		literals.put(new ANTLRHashString("gc", this), new Integer(46));
		literals.put(new ANTLRHashString("local", this), new Integer(42));
		literals.put(new ANTLRHashString("while", this), new Integer(29));
		literals.put(new ANTLRHashString("namespace", this), new Integer(12));
		literals.put(new ANTLRHashString("generic", this), new Integer(50));
		literals.put(new ANTLRHashString("ctor", this), new Integer(22));
		literals.put(new ANTLRHashString("alias", this), new Integer(99));
		literals.put(new ANTLRHashString("type", this), new Integer(98));
		literals.put(new ANTLRHashString("do", this), new Integer(30));
		literals.put(new ANTLRHashString("in", this), new Integer(48));
		literals.put(new ANTLRHashString("pooled", this), new Integer(44));
		literals.put(new ANTLRHashString("function", this), new Integer(53));
		literals.put(new ANTLRHashString("imports", this), new Integer(15));
		literals.put(new ANTLRHashString("once", this), new Integer(41));
		literals.put(new ANTLRHashString("closure", this), new Integer(97));
		literals.put(new ANTLRHashString("is_a", this), new Integer(80));
		literals.put(new ANTLRHashString("elseif", this), new Integer(39));
		literals.put(new ANTLRHashString("ref", this), new Integer(47));
		literals.put(new ANTLRHashString("from", this), new Integer(25));
		literals.put(new ANTLRHashString("typeof", this), new Integer(40));
		literals.put(new ANTLRHashString("out", this), new Integer(49));
		literals.put(new ANTLRHashString("if", this), new Integer(37));
		literals.put(new ANTLRHashString("constructor", this), new Integer(21));
		literals.put(new ANTLRHashString("struct", this), new Integer(100));
		literals.put(new ANTLRHashString("manual", this), new Integer(45));
		literals.put(new ANTLRHashString("construct", this), new Integer(27));
		literals.put(new ANTLRHashString("else", this), new Integer(38));
		literals.put(new ANTLRHashString("var", this), new Integer(34));
		literals.put(new ANTLRHashString("with", this), new Integer(33));
		literals.put(new ANTLRHashString("destructor", this), new Integer(23));
		caseSensitiveLiterals = true;
		setCaseSensitive(true);
	}

	private static final String _ruleNames[] = { "mVOCAB", "mWS_",
			"mTOK_ARROW", "mLPAREN", "mRPAREN", "mLBRACK", "mRBRACK",
			"mLCURLY", "mRCURLY", "mPLUS", "mMINUS", "mEQUALITY",
			"mNOT_EQUALS", "mBECOMES", "mGT", "mLT_", "mGTE", "mLTE", "mTIMES",
			"mDIV", "mMOD", "mLNOT", "mDOT", "mQUESTION", "mTOK_COLON",
			"mSEMI", "mLAND", "mLOR", "mCOMMA", "mBNOT", "mDIV_ASSIGN",
			"mPLUS_ASSIGN", "mINC", "mMINUS_ASSIGN", "mDEC", "mSTAR_ASSIGN",
			"mMOD_ASSIGN", "mSR", "mSR_ASSIGN", "mSL", "mSL_ASSIGN", "mBXOR",
			"mBXOR_ASSIGN", "mBOR", "mBOR_ASSIGN", "mBAND", "mBAND_ASSIGN",
			"mSL_COMMENT", "mML_COMMENT", "mCHAR_LITERAL", "mESC", "mTQUOT",
			"mSTRING_LITERAL", "mHEX_DIGIT", "mIDENT", "mQIDENT", "mNUM_INT",
			"mEXPONENT", "mFLOAT_SUFFIX", };

	public Token nextToken() throws TokenStreamException {
		Token theRetToken = null;
		tryAgain: for (;;) {
			Token _token = null;
			int _ttype = Token.INVALID_TYPE;
			resetText();
			try { // for char stream error handling
				try { // for lexical error handling
					switch (LA(1)) {
					case '\t':
					case '\n':
					case '\u000c':
					case '\r':
					case ' ': {
						mWS_(true);
						theRetToken = _returnToken;
						break;
					}
					case '(': {
						mLPAREN(true);
						theRetToken = _returnToken;
						break;
					}
					case ')': {
						mRPAREN(true);
						theRetToken = _returnToken;
						break;
					}
					case '[': {
						mLBRACK(true);
						theRetToken = _returnToken;
						break;
					}
					case ']': {
						mRBRACK(true);
						theRetToken = _returnToken;
						break;
					}
					case '{': {
						mLCURLY(true);
						theRetToken = _returnToken;
						break;
					}
					case '}': {
						mRCURLY(true);
						theRetToken = _returnToken;
						break;
					}
					case '?': {
						mQUESTION(true);
						theRetToken = _returnToken;
						break;
					}
					case ':': {
						mTOK_COLON(true);
						theRetToken = _returnToken;
						break;
					}
					case ';': {
						mSEMI(true);
						theRetToken = _returnToken;
						break;
					}
					case ',': {
						mCOMMA(true);
						theRetToken = _returnToken;
						break;
					}
					case '~': {
						mBNOT(true);
						theRetToken = _returnToken;
						break;
					}
					case '\'': {
						mCHAR_LITERAL(true);
						theRetToken = _returnToken;
						break;
					}
					case '"': {
						mSTRING_LITERAL(true);
						theRetToken = _returnToken;
						break;
					}
					case '$':
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
					case 'G':
					case 'H':
					case 'I':
					case 'J':
					case 'K':
					case 'L':
					case 'M':
					case 'N':
					case 'O':
					case 'P':
					case 'Q':
					case 'R':
					case 'S':
					case 'T':
					case 'U':
					case 'V':
					case 'W':
					case 'X':
					case 'Y':
					case 'Z':
					case '_':
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z': {
						mIDENT(true);
						theRetToken = _returnToken;
						break;
					}
					case '`': {
						mQIDENT(true);
						theRetToken = _returnToken;
						break;
					}
					default:
						if ((LA(1) == '/') && (LA(2) == '*')
								&& ((LA(3) >= '\u0003' && LA(3) <= '\u00ff'))
								&& ((LA(4) >= '\u0003' && LA(4) <= '\u00ff'))) {
							mML_COMMENT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '-') && (LA(2) == '-')
								&& (_tokenSet_0.member(LA(3))) && (true)) {
							mDEC(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (LA(2) == '>')
								&& (LA(3) == '=')) {
							mSR_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (LA(2) == '<')
								&& (LA(3) == '=')) {
							mSL_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '-') && (LA(2) == '>') && (true)
								&& (true)) {
							mTOK_ARROW(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '=') && (LA(2) == '=')) {
							mEQUALITY(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '!') && (LA(2) == '=')) {
							mNOT_EQUALS(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (LA(2) == '=')) {
							mGTE(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (LA(2) == '=')) {
							mLTE(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '&') && (LA(2) == '&')) {
							mLAND(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '|') && (LA(2) == '|')) {
							mLOR(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '/') && (LA(2) == '=') && (true)
								&& (true)) {
							mDIV_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '+') && (LA(2) == '=')) {
							mPLUS_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '+') && (LA(2) == '+')) {
							mINC(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '-') && (LA(2) == '=') && (true)
								&& (true)) {
							mMINUS_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '*') && (LA(2) == '=')) {
							mSTAR_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '%') && (LA(2) == '=')) {
							mMOD_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (LA(2) == '>') && (true)) {
							mSR(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (LA(2) == '<') && (true)) {
							mSL(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '^') && (LA(2) == '=')) {
							mBXOR_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '|') && (LA(2) == '=')) {
							mBOR_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '&') && (LA(2) == '=')) {
							mBAND_ASSIGN(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '#' || LA(1) == '-' || LA(1) == '/')
								&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))
								&& (true) && (true)) {
							mSL_COMMENT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '+') && (true)) {
							mPLUS(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '-') && (true)) {
							mMINUS(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '=') && (true)) {
							mBECOMES(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '>') && (true)) {
							mGT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '<') && (true)) {
							mLT_(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '*') && (true)) {
							mTIMES(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '/') && (true)) {
							mDIV(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '%') && (true)) {
							mMOD(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '!') && (true)) {
							mLNOT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '.') && (true) && (true) && (true)) {
							mDOT(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '^') && (true)) {
							mBXOR(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '|') && (true)) {
							mBOR(true);
							theRetToken = _returnToken;
						} else if ((LA(1) == '&') && (true)) {
							mBAND(true);
							theRetToken = _returnToken;
						} else if ((_tokenSet_1.member(LA(1))) && (true) && (true) && (true)) {
							mNUM_INT(true);
							theRetToken = _returnToken;
						} else {
							if (LA(1) == EOF_CHAR) {
								uponEOF();
								_returnToken = makeToken(Token.EOF_TYPE);
							} else {
								throw new NoViableAltForCharException(
										(char) LA(1), getFilename(), getLine());
							}
						}
					}
					if (_returnToken == null)
						continue tryAgain; // found SKIP token
					_ttype = _returnToken.getType();
					_ttype = testLiteralsTable(_ttype);
					_returnToken.setType(_ttype);
					return _returnToken;
				} catch (RecognitionException e) {
					throw new TokenStreamRecognitionException(e);
				}
			} catch (CharStreamException cse) {
				if (cse instanceof CharStreamIOException) {
					throw new TokenStreamIOException(
							((CharStreamIOException) cse).io);
				} else {
					throw new TokenStreamException(cse.getMessage());
				}
			}
		}
	}

	protected final void mVOCAB(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = VOCAB;
		int _saveIndex;
		fireEnterRule(0, _ttype);
		try { // debugging

			matchRange('\3', '\377');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(0, _ttype);
		}
	}

	public final void mWS_(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = WS_;
		int _saveIndex;
		fireEnterRule(1, _ttype);
		try { // debugging

			{
				switch (LA(1)) {
				case ' ': {
					match(' ');
					break;
				}
				case '\t': {
					match('\t');
					break;
				}
				case '\u000c': {
					match('\f');
					break;
				}
				case '\n':
				case '\r': {
					{
						if ((LA(1) == '\r') && (LA(2) == '\n')) {
							match("\r\n");
						} else if ((LA(1) == '\r') && (true)) {
							match('\r');
						} else if ((LA(1) == '\n')) {
							match('\n');
						} else {
							throw new NoViableAltForCharException((char) LA(1),
									getFilename(), getLine());
						}

					}
					newline();
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			_ttype = Token.SKIP;
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(1, _ttype);
		}
	}

	public final void mTOK_ARROW(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = TOK_ARROW;
		int _saveIndex;
		fireEnterRule(2, _ttype);
		try { // debugging

			match("->");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(2, _ttype);
		}
	}

	public final void mLPAREN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LPAREN;
		int _saveIndex;
		fireEnterRule(3, _ttype);
		try { // debugging

			match('(');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(3, _ttype);
		}
	}

	public final void mRPAREN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = RPAREN;
		int _saveIndex;
		fireEnterRule(4, _ttype);
		try { // debugging

			match(')');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(4, _ttype);
		}
	}

	public final void mLBRACK(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LBRACK;
		int _saveIndex;
		fireEnterRule(5, _ttype);
		try { // debugging

			match('[');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(5, _ttype);
		}
	}

	public final void mRBRACK(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = RBRACK;
		int _saveIndex;
		fireEnterRule(6, _ttype);
		try { // debugging

			match(']');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(6, _ttype);
		}
	}

	public final void mLCURLY(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LCURLY;
		int _saveIndex;
		fireEnterRule(7, _ttype);
		try { // debugging

			match('{');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(7, _ttype);
		}
	}

	public final void mRCURLY(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = RCURLY;
		int _saveIndex;
		fireEnterRule(8, _ttype);
		try { // debugging

			match('}');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(8, _ttype);
		}
	}

	public final void mPLUS(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = PLUS;
		int _saveIndex;
		fireEnterRule(9, _ttype);
		try { // debugging

			match('+');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(9, _ttype);
		}
	}

	public final void mMINUS(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MINUS;
		int _saveIndex;
		fireEnterRule(10, _ttype);
		try { // debugging

			match('-');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(10, _ttype);
		}
	}

	public final void mEQUALITY(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = EQUALITY;
		int _saveIndex;
		fireEnterRule(11, _ttype);
		try { // debugging

			match("==");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(11, _ttype);
		}
	}

	public final void mNOT_EQUALS(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = NOT_EQUALS;
		int _saveIndex;
		fireEnterRule(12, _ttype);
		try { // debugging

			match("!=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(12, _ttype);
		}
	}

	public final void mBECOMES(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BECOMES;
		int _saveIndex;
		fireEnterRule(13, _ttype);
		try { // debugging

			match('=');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(13, _ttype);
		}
	}

	public final void mGT(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = GT;
		int _saveIndex;
		fireEnterRule(14, _ttype);
		try { // debugging

			match(">");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(14, _ttype);
		}
	}

	public final void mLT_(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LT_;
		int _saveIndex;
		fireEnterRule(15, _ttype);
		try { // debugging

			match("<");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(15, _ttype);
		}
	}

	public final void mGTE(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = GTE;
		int _saveIndex;
		fireEnterRule(16, _ttype);
		try { // debugging

			match(">=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(16, _ttype);
		}
	}

	public final void mLTE(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LTE;
		int _saveIndex;
		fireEnterRule(17, _ttype);
		try { // debugging

			match("<=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(17, _ttype);
		}
	}

	public final void mTIMES(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = TIMES;
		int _saveIndex;
		fireEnterRule(18, _ttype);
		try { // debugging

			match('*');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(18, _ttype);
		}
	}

	public final void mDIV(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DIV;
		int _saveIndex;
		fireEnterRule(19, _ttype);
		try { // debugging

			match('/');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(19, _ttype);
		}
	}

	public final void mMOD(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MOD;
		int _saveIndex;
		fireEnterRule(20, _ttype);
		try { // debugging

			match('%');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(20, _ttype);
		}
	}

	public final void mLNOT(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LNOT;
		int _saveIndex;
		fireEnterRule(21, _ttype);
		try { // debugging

			match('!');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(21, _ttype);
		}
	}

	public final void mDOT(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DOT;
		int _saveIndex;
		fireEnterRule(22, _ttype);
		try { // debugging

			match('.');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(22, _ttype);
		}
	}

	public final void mQUESTION(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = QUESTION;
		int _saveIndex;
		fireEnterRule(23, _ttype);
		try { // debugging

			match('?');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(23, _ttype);
		}
	}

	public final void mTOK_COLON(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = TOK_COLON;
		int _saveIndex;
		fireEnterRule(24, _ttype);
		try { // debugging

			match(':');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(24, _ttype);
		}
	}

	public final void mSEMI(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SEMI;
		int _saveIndex;
		fireEnterRule(25, _ttype);
		try { // debugging

			match(';');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(25, _ttype);
		}
	}

	public final void mLAND(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LAND;
		int _saveIndex;
		fireEnterRule(26, _ttype);
		try { // debugging

			match("&&");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(26, _ttype);
		}
	}

	public final void mLOR(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = LOR;
		int _saveIndex;
		fireEnterRule(27, _ttype);
		try { // debugging

			match("||");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(27, _ttype);
		}
	}

	public final void mCOMMA(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = COMMA;
		int _saveIndex;
		fireEnterRule(28, _ttype);
		try { // debugging

			match(',');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(28, _ttype);
		}
	}

	public final void mBNOT(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BNOT;
		int _saveIndex;
		fireEnterRule(29, _ttype);
		try { // debugging

			match('~');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(29, _ttype);
		}
	}

	public final void mDIV_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DIV_ASSIGN;
		int _saveIndex;
		fireEnterRule(30, _ttype);
		try { // debugging

			match("/=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(30, _ttype);
		}
	}

	public final void mPLUS_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = PLUS_ASSIGN;
		int _saveIndex;
		fireEnterRule(31, _ttype);
		try { // debugging

			match("+=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(31, _ttype);
		}
	}

	public final void mINC(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = INC;
		int _saveIndex;
		fireEnterRule(32, _ttype);
		try { // debugging

			match("++");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(32, _ttype);
		}
	}

	public final void mMINUS_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MINUS_ASSIGN;
		int _saveIndex;
		fireEnterRule(33, _ttype);
		try { // debugging

			match("-=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(33, _ttype);
		}
	}

	public final void mDEC(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = DEC;
		int _saveIndex;
		fireEnterRule(34, _ttype);
		try { // debugging

			match("--");
			{
				{
					match(_tokenSet_0);
				}
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(34, _ttype);
		}
	}

	public final void mSTAR_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = STAR_ASSIGN;
		int _saveIndex;
		fireEnterRule(35, _ttype);
		try { // debugging

			match("*=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(35, _ttype);
		}
	}

	public final void mMOD_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = MOD_ASSIGN;
		int _saveIndex;
		fireEnterRule(36, _ttype);
		try { // debugging

			match("%=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(36, _ttype);
		}
	}

	public final void mSR(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SR;
		int _saveIndex;
		fireEnterRule(37, _ttype);
		try { // debugging

			match(">>");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(37, _ttype);
		}
	}

	public final void mSR_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SR_ASSIGN;
		int _saveIndex;
		fireEnterRule(38, _ttype);
		try { // debugging

			match(">>=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(38, _ttype);
		}
	}

	public final void mSL(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SL;
		int _saveIndex;
		fireEnterRule(39, _ttype);
		try { // debugging

			match("<<");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(39, _ttype);
		}
	}

	public final void mSL_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SL_ASSIGN;
		int _saveIndex;
		fireEnterRule(40, _ttype);
		try { // debugging

			match("<<=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(40, _ttype);
		}
	}

	public final void mBXOR(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BXOR;
		int _saveIndex;
		fireEnterRule(41, _ttype);
		try { // debugging

			match('^');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(41, _ttype);
		}
	}

	public final void mBXOR_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BXOR_ASSIGN;
		int _saveIndex;
		fireEnterRule(42, _ttype);
		try { // debugging

			match("^=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(42, _ttype);
		}
	}

	public final void mBOR(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BOR;
		int _saveIndex;
		fireEnterRule(43, _ttype);
		try { // debugging

			match('|');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(43, _ttype);
		}
	}

	public final void mBOR_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BOR_ASSIGN;
		int _saveIndex;
		fireEnterRule(44, _ttype);
		try { // debugging

			match("|=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(44, _ttype);
		}
	}

	public final void mBAND(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BAND;
		int _saveIndex;
		fireEnterRule(45, _ttype);
		try { // debugging

			match('&');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(45, _ttype);
		}
	}

	public final void mBAND_ASSIGN(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = BAND_ASSIGN;
		int _saveIndex;
		fireEnterRule(46, _ttype);
		try { // debugging

			match("&=");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(46, _ttype);
		}
	}

	public final void mSL_COMMENT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		fireEnterRule(47, _ttype);
		try { // debugging

			{
				switch (LA(1)) {
				case '/': {
					match("//");
					break;
				}
				case '#': {
					match("#");
					break;
				}
				case '-': {
					match("--");
					{
						switch (LA(1)) {
						case ' ': {
							match(' ');
							break;
						}
						case '\t': {
							match('\t');
							break;
						}
						case '*': {
							match('*');
							break;
						}
						default: {
							throw new NoViableAltForCharException((char) LA(1),
									getFilename(), getLine());
						}
						}
					}
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			{
				_loop240: do {
					if ((_tokenSet_2.member(LA(1)))) {
						{
							match(_tokenSet_2);
						}
					} else {
						break _loop240;
					}

				} while (true);
			}
			{
				switch (LA(1)) {
				case '\n': {
					match('\n');
					break;
				}
				case '\r': {
					match('\r');
					{
						if ((LA(1) == '\n')) {
							match('\n');
						} else {
						}

					}
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			_ttype = Token.SKIP;
			newline();
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(47, _ttype);
		}
	}

	public final void mML_COMMENT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		fireEnterRule(48, _ttype);
		try { // debugging

			match("/*");
			{
				_loop246: do {
					if ((LA(1) == '\r') && (LA(2) == '\n')
							&& ((LA(3) >= '\u0003' && LA(3) <= '\u00ff'))
							&& ((LA(4) >= '\u0003' && LA(4) <= '\u00ff'))) {
						match('\r');
						match('\n');
						newline();
					} else if (((LA(1) == '*')
							&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff')) && ((LA(3) >= '\u0003' && LA(3) <= '\u00ff')))
							&& fireSemanticPredicateEvaluated(
									antlr.debug.SemanticPredicateEvent.PREDICTING,
									0, LA(2) != '/')) {
						match('*');
					} else if ((LA(1) == '\r')
							&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))
							&& ((LA(3) >= '\u0003' && LA(3) <= '\u00ff'))
							&& (true)) {
						match('\r');
						newline();
					} else if ((LA(1) == '\n')) {
						match('\n');
						newline();
					} else if ((_tokenSet_3.member(LA(1)))) {
						{
							match(_tokenSet_3);
						}
					} else {
						break _loop246;
					}

				} while (true);
			}
			match("*/");
			_ttype = Token.SKIP;
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(48, _ttype);
		}
	}

	public final void mCHAR_LITERAL(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = CHAR_LITERAL;
		int _saveIndex;
		fireEnterRule(49, _ttype);
		try { // debugging

			match('\'');
			{
				if ((LA(1) == '\\')) {
					mESC(false);
				} else if ((_tokenSet_4.member(LA(1)))) {
					matchNot('\'');
				} else {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}

			}
			match('\'');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(49, _ttype);
		}
	}

	protected final void mESC(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = ESC;
		int _saveIndex;
		fireEnterRule(50, _ttype);
		try { // debugging

			match('\\');
			{
				switch (LA(1)) {
				case 'n': {
					match('n');
					break;
				}
				case 'r': {
					match('r');
					break;
				}
				case 't': {
					match('t');
					break;
				}
				case 'b': {
					match('b');
					break;
				}
				case 'f': {
					match('f');
					break;
				}
				case '"': {
					match('"');
					break;
				}
				case '\'': {
					match('\'');
					break;
				}
				case '\\': {
					match('\\');
					break;
				}
				case 'u': {
					{
						int _cnt257 = 0;
						_loop257: do {
							if ((LA(1) == 'u')) {
								match('u');
							} else {
								if (_cnt257 >= 1) {
									break _loop257;
								} else {
									throw new NoViableAltForCharException(
											(char) LA(1), getFilename(),
											getLine());
								}
							}

							_cnt257++;
						} while (true);
					}
					mHEX_DIGIT(false);
					mHEX_DIGIT(false);
					mHEX_DIGIT(false);
					mHEX_DIGIT(false);
					break;
				}
				case '0':
				case '1':
				case '2':
				case '3': {
					{
						matchRange('0', '3');
					}
					{
						if (((LA(1) >= '0' && LA(1) <= '7'))
								&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))
								&& (true) && (true)) {
							{
								matchRange('0', '7');
							}
							{
								if (((LA(1) >= '0' && LA(1) <= '7'))
										&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))
										&& (true) && (true)) {
									matchRange('0', '7');
								} else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true) && (true) && (true)) {
								} else {
									throw new NoViableAltForCharException(
											(char) LA(1), getFilename(),
											getLine());
								}

							}
						} else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true) && (true) && (true)) {
						} else {
							throw new NoViableAltForCharException((char) LA(1),
									getFilename(), getLine());
						}

					}
					break;
				}
				case '4':
				case '5':
				case '6':
				case '7': {
					{
						matchRange('4', '7');
					}
					{
						if (((LA(1) >= '0' && LA(1) <= '9'))
								&& ((LA(2) >= '\u0003' && LA(2) <= '\u00ff'))
								&& (true) && (true)) {
							{
								matchRange('0', '9');
							}
						} else if (((LA(1) >= '\u0003' && LA(1) <= '\u00ff')) && (true) && (true) && (true)) {
						} else {
							throw new NoViableAltForCharException((char) LA(1),
									getFilename(), getLine());
						}

					}
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(50, _ttype);
		}
	}

	protected final void mTQUOT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = TQUOT;
		int _saveIndex;
		fireEnterRule(51, _ttype);
		try { // debugging

			match("\"\"\"");
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(51, _ttype);
		}
	}

	public final void mSTRING_LITERAL(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		fireEnterRule(52, _ttype);
		try { // debugging

			match('"');
			{
				_loop253: do {
					if ((LA(1) == '\\')) {
						mESC(false);
					} else if ((_tokenSet_5.member(LA(1)))) {
						{
							match(_tokenSet_5);
						}
					} else {
						break _loop253;
					}

				} while (true);
			}
			match('"');
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(52, _ttype);
		}
	}

	protected final void mHEX_DIGIT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		fireEnterRule(53, _ttype);
		try { // debugging

			{
				switch (LA(1)) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9': {
					matchRange('0', '9');
					break;
				}
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F': {
					matchRange('A', 'F');
					break;
				}
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f': {
					matchRange('a', 'f');
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(53, _ttype);
		}
	}

	public final void mIDENT(boolean _createToken) throws RecognitionException,
			CharStreamException, TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = IDENT;
		int _saveIndex;
		fireEnterRule(54, _ttype);
		try { // debugging

			{
				switch (LA(1)) {
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z': {
					matchRange('a', 'z');
					break;
				}
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				case 'N':
				case 'O':
				case 'P':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
				case 'U':
				case 'V':
				case 'W':
				case 'X':
				case 'Y':
				case 'Z': {
					matchRange('A', 'Z');
					break;
				}
				case '_': {
					match('_');
					break;
				}
				case '$': {
					match('$');
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			{
				_loop270: do {
					switch (LA(1)) {
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z': {
						matchRange('a', 'z');
						break;
					}
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
					case 'G':
					case 'H':
					case 'I':
					case 'J':
					case 'K':
					case 'L':
					case 'M':
					case 'N':
					case 'O':
					case 'P':
					case 'Q':
					case 'R':
					case 'S':
					case 'T':
					case 'U':
					case 'V':
					case 'W':
					case 'X':
					case 'Y':
					case 'Z': {
						matchRange('A', 'Z');
						break;
					}
					case '_': {
						match('_');
						break;
					}
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9': {
						matchRange('0', '9');
						break;
					}
					default: {
						break _loop270;
					}
					}
				} while (true);
			}
			_ttype = testLiteralsTable(_ttype);
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(54, _ttype);
		}
	}

	public final void mQIDENT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = QIDENT;
		int _saveIndex;
		fireEnterRule(55, _ttype);
		try { // debugging

			match('`');
			mIDENT(false);
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(55, _ttype);
		}
	}

	public final void mNUM_INT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = NUM_INT;
		int _saveIndex;
		fireEnterRule(56, _ttype);
		try { // debugging
			boolean isDecimal = false;

			switch (LA(1)) {
			case '.': {
				match('.');
				_ttype = DOT;
				{
					if (((LA(1) >= '0' && LA(1) <= '9'))) {
						{
							int _cnt275 = 0;
							_loop275: do {
								if (((LA(1) >= '0' && LA(1) <= '9'))) {
									matchRange('0', '9');
								} else {
									if (_cnt275 >= 1) {
										break _loop275;
									} else {
										throw new NoViableAltForCharException(
												(char) LA(1), getFilename(),
												getLine());
									}
								}

								_cnt275++;
							} while (true);
						}
						{
							if ((LA(1) == 'E' || LA(1) == 'e')) {
								mEXPONENT(false);
							} else {
							}

						}
						{
							if ((_tokenSet_6.member(LA(1)))) {
								mFLOAT_SUFFIX(false);
							} else {
							}

						}
						_ttype = NUM_FLOAT;
					} else {
					}

				}
				break;
			}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9': {
				{
					switch (LA(1)) {
					case '0': {
						match('0');
						isDecimal = true;
						{
							switch (LA(1)) {
							case 'X':
							case 'x': {
								{
									switch (LA(1)) {
									case 'x': {
										match('x');
										break;
									}
									case 'X': {
										match('X');
										break;
									}
									default: {
										throw new NoViableAltForCharException(
												(char) LA(1), getFilename(),
												getLine());
									}
									}
								}
								{
									int _cnt282 = 0;
									_loop282: do {
										if ((_tokenSet_7.member(LA(1))) && (true) && (true) && (true)) {
											mHEX_DIGIT(false);
										} else {
											if (_cnt282 >= 1) {
												break _loop282;
											} else {
												throw new NoViableAltForCharException(
														(char) LA(1),
														getFilename(),
														getLine());
											}
										}

										_cnt282++;
									} while (true);
								}
								break;
							}
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7': {
								{
									int _cnt284 = 0;
									_loop284: do {
										if (((LA(1) >= '0' && LA(1) <= '7'))) {
											matchRange('0', '7');
										} else {
											if (_cnt284 >= 1) {
												break _loop284;
											} else {
												throw new NoViableAltForCharException(
														(char) LA(1),
														getFilename(),
														getLine());
											}
										}

										_cnt284++;
									} while (true);
								}
								break;
							}
							default: {
							}
							}
						}
						break;
					}
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9': {
						{
							matchRange('1', '9');
						}
						{
							_loop287: do {
								if (((LA(1) >= '0' && LA(1) <= '9'))) {
									matchRange('0', '9');
								} else {
									break _loop287;
								}

							} while (true);
						}
						isDecimal = true;
						break;
					}
					default: {
						throw new NoViableAltForCharException((char) LA(1),
								getFilename(), getLine());
					}
					}
				}
				{
					if ((LA(1) == 'L' || LA(1) == 'l')) {
						{
							switch (LA(1)) {
							case 'l': {
								match('l');
								break;
							}
							case 'L': {
								match('L');
								break;
							}
							default: {
								throw new NoViableAltForCharException(
										(char) LA(1), getFilename(), getLine());
							}
							}
						}
					} else if (((_tokenSet_8.member(LA(1))))
							&& fireSemanticPredicateEvaluated(
									antlr.debug.SemanticPredicateEvent.PREDICTING,
									1, isDecimal)) {
						{
							switch (LA(1)) {
							case '.': {
								match('.');
								{
									_loop292: do {
										if (((LA(1) >= '0' && LA(1) <= '9'))) {
											matchRange('0', '9');
										} else {
											break _loop292;
										}

									} while (true);
								}
								{
									if ((LA(1) == 'E' || LA(1) == 'e')) {
										mEXPONENT(false);
									} else {
									}

								}
								{
									if ((_tokenSet_6.member(LA(1)))) {
										mFLOAT_SUFFIX(false);
									} else {
									}

								}
								break;
							}
							case 'E':
							case 'e': {
								mEXPONENT(false);
								{
									if ((_tokenSet_6.member(LA(1)))) {
										mFLOAT_SUFFIX(false);
									} else {
									}

								}
								break;
							}
							case 'D':
							case 'F':
							case 'd':
							case 'f': {
								mFLOAT_SUFFIX(false);
								break;
							}
							default: {
								throw new NoViableAltForCharException(
										(char) LA(1), getFilename(), getLine());
							}
							}
						}
						_ttype = NUM_FLOAT;
					} else {
					}

				}
				break;
			}
			default: {
				throw new NoViableAltForCharException((char) LA(1),
						getFilename(), getLine());
			}
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(56, _ttype);
		}
	}

	protected final void mEXPONENT(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = EXPONENT;
		int _saveIndex;
		fireEnterRule(57, _ttype);
		try { // debugging

			{
				switch (LA(1)) {
				case 'e': {
					match('e');
					break;
				}
				case 'E': {
					match('E');
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			{
				switch (LA(1)) {
				case '+': {
					match('+');
					break;
				}
				case '-': {
					match('-');
					break;
				}
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9': {
					break;
				}
				default: {
					throw new NoViableAltForCharException((char) LA(1),
							getFilename(), getLine());
				}
				}
			}
			{
				int _cnt300 = 0;
				_loop300: do {
					if (((LA(1) >= '0' && LA(1) <= '9'))) {
						matchRange('0', '9');
					} else {
						if (_cnt300 >= 1) {
							break _loop300;
						} else {
							throw new NoViableAltForCharException((char) LA(1),
									getFilename(), getLine());
						}
					}

					_cnt300++;
				} while (true);
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(57, _ttype);
		}
	}

	protected final void mFLOAT_SUFFIX(boolean _createToken)
			throws RecognitionException, CharStreamException,
			TokenStreamException {
		int _ttype;
		Token _token = null;
		int _begin = text.length();
		_ttype = FLOAT_SUFFIX;
		int _saveIndex;
		fireEnterRule(58, _ttype);
		try { // debugging

			switch (LA(1)) {
			case 'f': {
				match('f');
				break;
			}
			case 'F': {
				match('F');
				break;
			}
			case 'd': {
				match('d');
				break;
			}
			case 'D': {
				match('D');
				break;
			}
			default: {
				throw new NoViableAltForCharException((char) LA(1),
						getFilename(), getLine());
			}
			}
			if (_createToken && _token == null && _ttype != Token.SKIP) {
				_token = makeToken(_ttype);
				_token.setText(new String(text.getBuffer(), _begin, text
						.length()
						- _begin));
			}
			_returnToken = _token;
		} finally { // debugging
			fireExitRule(58, _ttype);
		}
	}

	private String _semPredNames[] = { " LA(2)!='/' ", "isDecimal", };

	private static final long _tokenSet_0_data_[] = { -4402341478920L, -1L,
			-1L, -1L, 0L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);

	private static final long _tokenSet_1_data_[] = { 288019269919178752L, 0L,
			0L, 0L, 0L };

	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);

	private static final long _tokenSet_2_data_[] = { -9224L, -1L, -1L, -1L,
			0L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);

	private static final long _tokenSet_3_data_[] = { -4398046520328L, -1L,
			-1L, -1L, 0L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);

	private static final long _tokenSet_4_data_[] = { -549755813896L,
			-268435457L, -1L, -1L, 0L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);

	private static final long _tokenSet_5_data_[] = { -17179869192L,
			-268435457L, -1L, -1L, 0L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);

	private static final long _tokenSet_6_data_[] = { 0L, 343597383760L, 0L,
			0L, 0L };

	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);

	private static final long _tokenSet_7_data_[] = { 287948901175001088L,
			541165879422L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);

	private static final long _tokenSet_8_data_[] = { 70368744177664L,
			481036337264L, 0L, 0L, 0L };

	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);

}
