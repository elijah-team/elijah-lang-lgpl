// $ANTLR 2.7.1: "elijjah.g" -> "ElijjahParser.java"$

  package tripleo.elijjah;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.*;
import tripleo.elijah.*;

public class ElijjahParser extends antlr.LLkParser
       implements ElijjahTokenTypes
 {

Qualident xy;
public Out out;
IExpression expr;

protected ElijjahParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public ElijjahParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected ElijjahParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public ElijjahParser(TokenStream lexer) {
  this(lexer,2);
}

public ElijjahParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void program() throws RecognitionException, TokenStreamException {
		
		ParserClosure pc = out.closure();
		
		try {      // for error handling
			{
			_loop4:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_indexing:
					{
						indexingStatement(pc.indexingStatement());
						break;
					}
					case LITERAL_package:
					{
						match(LITERAL_package);
						xy=qualident();
						if ( inputState.guessing==0 ) {
							pc.packageName(xy);
						}
						break;
					}
					case LITERAL_class:
					case ANNOT:
					case LITERAL_namespace:
					case LITERAL_from:
					case LITERAL_import:
					case LITERAL_alias:
					{
						programStatement(pc, out.module());
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop4;
				}
				
			} while (true);
			}
			match(Token.EOF_TYPE);
			if ( inputState.guessing==0 ) {
				out.FinishModule();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void indexingStatement(
		IndexingStatement idx
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		ExpressionList el=null;
		
		try {      // for error handling
			match(LITERAL_indexing);
			{
			_loop7:
			do {
				if ((LA(1)==IDENT)) {
					i1 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						idx.setName(i1);
					}
					match(TOK_COLON);
					if ( inputState.guessing==0 ) {
						el=new ExpressionList();
					}
					expressionList(el);
					if ( inputState.guessing==0 ) {
						idx.setExprs(el);
					}
				}
				else {
					break _loop7;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Qualident  qualident() throws RecognitionException, TokenStreamException {
		Qualident q;
		
		Token  r1 = null;
		Token  d1 = null;
		Token  r2 = null;
		q=new Qualident();
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				q.append(r1);
			}
			{
			_loop12:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					d1 = LT(1);
					match(DOT);
					r2 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						q.appendDot(d1); q.append(r2);
					}
				}
				else {
					break _loop12;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_3);
			} else {
			  throw ex;
			}
		}
		return q;
	}
	
	public final void programStatement(
		ProgramClosure pc, OS_Element cont
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			case LITERAL_import:
			{
				importStatement(pc.importStatement(cont));
				break;
			}
			case LITERAL_namespace:
			{
				namespaceStatement(pc.namespaceStatement(cont));
				break;
			}
			case LITERAL_class:
			case ANNOT:
			{
				classStatement(pc.classStatement(cont));
				break;
			}
			case LITERAL_alias:
			{
				aliasStatement(pc.aliasStatement(cont));
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void opt_semi() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			if ((LA(1)==SEMI) && (_tokenSet_5.member(LA(2)))) {
				match(SEMI);
			}
			else if ((_tokenSet_5.member(LA(1))) && (_tokenSet_6.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_5);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void expressionList(
		ExpressionList el
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			expr=expression();
			if ( inputState.guessing==0 ) {
				el.next(expr);
			}
			{
			_loop108:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expr=expression();
					if ( inputState.guessing==0 ) {
						el.next(expr);
					}
				}
				else {
					break _loop108;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
	}
	
	public final IExpression  constantValue() throws RecognitionException, TokenStreamException {
		IExpression e;
		
		Token  s = null;
		Token  c = null;
		Token  n = null;
		Token  f = null;
		e=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				s = LT(1);
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					e=new StringExpression(s);
				}
				break;
			}
			case CHAR_LITERAL:
			{
				c = LT(1);
				match(CHAR_LITERAL);
				if ( inputState.guessing==0 ) {
					e=new CharLitExpression(c);
				}
				break;
			}
			case NUM_INT:
			{
				n = LT(1);
				match(NUM_INT);
				if ( inputState.guessing==0 ) {
					e=new NumericExpression(n);
				}
				break;
			}
			case NUM_FLOAT:
			{
				f = LT(1);
				match(NUM_FLOAT);
				if ( inputState.guessing==0 ) {
					e=new FloatExpression(f);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		return e;
	}
	
	public final IExpression  primitiveExpression() throws RecognitionException, TokenStreamException {
		IExpression e;
		
		e=null;ExpressionList el=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			{
				e=constantValue();
				break;
			}
			case IDENT:
			{
				e=variableReference();
				break;
			}
			case LBRACK:
			{
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					e=new ListExpression();el=new ExpressionList();
				}
				expressionList(el);
				if ( inputState.guessing==0 ) {
					((ListExpression)e).setContents(el);
				}
				match(RBRACK);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return e;
	}
	
	public final IExpression  variableReference() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  r1 = null;
		Token  r2 = null;
		Token  lp = null;
		ProcedureCallExpression pcx;ExpressionList el=null;ee=null;
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				ee=new IdentExpression(r1);
			}
			{
			switch ( LA(1)) {
			case DOT:
			{
				match(DOT);
				r2 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					ee=new DotExpression(ee, new IdentExpression(r2));
				}
				break;
			}
			case LBRACK:
			{
				match(LBRACK);
				expr=expression();
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					ee=new GetItemExpression(ee, expr);
				}
				break;
			}
			case LPAREN:
			{
				lp = LT(1);
				match(LPAREN);
				{
				switch ( LA(1)) {
				case IDENT:
				case STRING_LITERAL:
				case CHAR_LITERAL:
				case NUM_INT:
				case NUM_FLOAT:
				case LPAREN:
				case LCURLY:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_this:
				case LITERAL_null:
				case LITERAL_function:
				case LITERAL_procedure:
				{
					el=expressionList2();
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					ProcedureCallExpression pce=new ProcedureCallExpression();
					pce.identifier(ee);
					pce.setArgs(el);
					ee=pce;
				}
				match(RPAREN);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void classStatement(
		ClassStatement cls
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		
		try {      // for error handling
			{
			_loop15:
			do {
				if ((LA(1)==ANNOT)) {
					annotation_clause();
				}
				else {
					break _loop15;
				}
				
			} while (true);
			}
			match(LITERAL_class);
			{
			switch ( LA(1)) {
			case LITERAL_interface:
			{
				match(LITERAL_interface);
				if ( inputState.guessing==0 ) {
					cls.setType(ClassTypes.INTERFACE);
				}
				break;
			}
			case LITERAL_struct:
			{
				match(LITERAL_struct);
				if ( inputState.guessing==0 ) {
					cls.setType(ClassTypes.STRUCTURE);
				}
				break;
			}
			case LITERAL_signature:
			{
				match(LITERAL_signature);
				if ( inputState.guessing==0 ) {
					cls.setType(ClassTypes.SIGNATURE);
				}
				break;
			}
			case LITERAL_abstract:
			{
				match(LITERAL_abstract);
				if ( inputState.guessing==0 ) {
					cls.setType(ClassTypes.ABSTRACT);
				}
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			i1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				cls.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				{
				match(LPAREN);
				classInheritance_(cls.classInheritance());
				match(RPAREN);
				}
				break;
			}
			case LT_:
			{
				classInheritanceRuby(cls.classInheritance());
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LCURLY);
			{
			switch ( LA(1)) {
			case IDENT:
			case STRING_LITERAL:
			case LITERAL_class:
			case LITERAL_struct:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_invariant:
			case LITERAL_access:
			{
				classScope(cls);
				break;
			}
			case LITERAL_abstract:
			{
				match(LITERAL_abstract);
				if ( inputState.guessing==0 ) {
					cls.setType(ClassTypes.ABSTRACT);
				}
				{
				switch ( LA(1)) {
				case LITERAL_invariant:
				{
					invariantStatement(cls.invariantStatement());
					break;
				}
				case RCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void annotation_clause() throws RecognitionException, TokenStreamException {
		
		Qualident q=null;ExpressionList el=null;
		
		try {      // for error handling
			match(ANNOT);
			{
			int _cnt24=0;
			_loop24:
			do {
				if ((LA(1)==IDENT)) {
					q=qualident();
					{
					switch ( LA(1)) {
					case LPAREN:
					{
						match(LPAREN);
						if ( inputState.guessing==0 ) {
							el=new ExpressionList();
						}
						expressionList(el);
						match(RPAREN);
						break;
					}
					case IDENT:
					case RBRACK:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else {
					if ( _cnt24>=1 ) { break _loop24; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt24++;
			} while (true);
			}
			match(RBRACK);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classInheritance_(
		ClassInheritance ci
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			inhTypeName(ci.next());
			{
			_loop35:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					inhTypeName(ci.next());
				}
				else {
					break _loop35;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_11);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classInheritanceRuby(
		ClassInheritance ci
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LT_);
			classInheritance_(ci);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_12);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classScope(
		ClassStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Token  x1 = null;
		ConstructorDef cd=null;DestructorDef dd=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop50:
			do {
				switch ( LA(1)) {
				case LITERAL_constructor:
				case LITERAL_ctor:
				{
					{
					switch ( LA(1)) {
					case LITERAL_constructor:
					{
						match(LITERAL_constructor);
						break;
					}
					case LITERAL_ctor:
					{
						match(LITERAL_ctor);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					{
					switch ( LA(1)) {
					case IDENT:
					{
						x1 = LT(1);
						match(IDENT);
						if ( inputState.guessing==0 ) {
							cd=cr.addCtor(x1);
						}
						break;
					}
					case LPAREN:
					{
						if ( inputState.guessing==0 ) {
							cd=cr.addCtor(null);
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opfal(cd.fal());
					scope(cd.scope());
					break;
				}
				case LITERAL_destructor:
				case LITERAL_dtor:
				{
					{
					switch ( LA(1)) {
					case LITERAL_destructor:
					{
						match(LITERAL_destructor);
						break;
					}
					case LITERAL_dtor:
					{
						match(LITERAL_dtor);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						dd=cr.addDtor();
					}
					opfal(dd.fal());
					scope(dd.scope());
					break;
				}
				case IDENT:
				{
					functionDef(cr.funcDef());
					break;
				}
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				{
					varStmt(cr.statementClosure(), cr);
					break;
				}
				case LITERAL_class:
				case ANNOT:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_alias:
				{
					programStatement(cr.XXX(), cr);
					break;
				}
				case LITERAL_invariant:
				{
					invariantStatement(cr.invariantStatement());
					break;
				}
				case LITERAL_access:
				{
					accessNotation();
					break;
				}
				default:
					if ((LA(1)==LITERAL_type) && (LA(2)==IDENT)) {
						match(LITERAL_type);
						match(IDENT);
						match(BECOMES);
						match(IDENT);
						{
						_loop49:
						do {
							if ((LA(1)==BOR)) {
								match(BOR);
								match(IDENT);
							}
							else {
								break _loop49;
							}
							
						} while (true);
						}
					}
					else if ((LA(1)==LITERAL_struct||LA(1)==LITERAL_type) && (LA(2)==IDENT||LA(2)==LITERAL_alias)) {
						typeAlias(cr.typeAlias());
					}
				else {
					break _loop50;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void invariantStatement(
		InvariantStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		InvariantStatementPart isp=null;
		
		try {      // for error handling
			match(LITERAL_invariant);
			{
			_loop119:
			do {
				if ((LA(1)==IDENT||LA(1)==TOK_COLON) && (_tokenSet_14.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case IDENT:
					{
						i1 = LT(1);
						match(IDENT);
						break;
					}
					case TOK_COLON:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						isp = new InvariantStatementPart(cr, i1);
					}
					match(TOK_COLON);
					expr=expression();
					if ( inputState.guessing==0 ) {
						isp.setExpr(expr);
					}
				}
				else {
					break _loop119;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void namespaceStatement(
		NamespaceStatement cls
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		
		try {      // for error handling
			match(LITERAL_namespace);
			{
			if ((LA(1)==IDENT)) {
				i1 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					cls.setName(i1);/*cls.findType();*/
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_16.member(LA(2)))) {
				if ( inputState.guessing==0 ) {
					cls.setType(NamespaceTypes.MODULE);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_16.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(LCURLY);
			namespaceScope(cls);
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void namespaceScope(
		NamespaceStatement cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop53:
			do {
				switch ( LA(1)) {
				case IDENT:
				{
					functionDef(cr.funcDef());
					break;
				}
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				{
					varStmt(cr.statementClosure(), cr);
					break;
				}
				case LITERAL_struct:
				case LITERAL_type:
				{
					typeAlias(cr.typeAlias());
					break;
				}
				case LITERAL_class:
				case ANNOT:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_alias:
				{
					programStatement(cr.XXX(), cr);
					break;
				}
				case LITERAL_invariant:
				{
					invariantStatement(cr.invariantStatement());
					break;
				}
				case LITERAL_access:
				{
					accessNotation();
					break;
				}
				default:
				{
					break _loop53;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importStatement(
		ImportStatement pc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				xy=qualident();
				match(LITERAL_import);
				qualidentList(pc.importList());
				if ( inputState.guessing==0 ) {
					pc.importRoot(xy);
				}
				break;
			}
			case LITERAL_import:
			{
				match(LITERAL_import);
				importPart(pc);
				{
				_loop29:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						importPart(pc);
					}
					else {
						break _loop29;
					}
					
				} while (true);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void qualidentList(
		QualidentList qal
	) throws RecognitionException, TokenStreamException {
		
		Qualident qid;
		
		try {      // for error handling
			qid=qualident();
			if ( inputState.guessing==0 ) {
				qal.add(qid);
			}
			{
			_loop104:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					qid=qualident();
					if ( inputState.guessing==0 ) {
						qal.add(qid);
					}
				}
				else {
					break _loop104;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importPart(
		ImportStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		Qualident q1,q2,q3;IdentList il=null;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (LA(2)==BECOMES)) {
				i1 = LT(1);
				match(IDENT);
				match(BECOMES);
				q1=qualident();
				if ( inputState.guessing==0 ) {
					cr.addAssigningPart(i1,q1);
				}
			}
			else {
				boolean synPredMatched32 = false;
				if (((LA(1)==IDENT) && (LA(2)==DOT||LA(2)==LCURLY))) {
					int _m32 = mark();
					synPredMatched32 = true;
					inputState.guessing++;
					try {
						{
						qualident();
						match(LCURLY);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched32 = false;
					}
					rewind(_m32);
					inputState.guessing--;
				}
				if ( synPredMatched32 ) {
					q3=qualident();
					match(LCURLY);
					if ( inputState.guessing==0 ) {
						il=cr.addSelectivePart(q3);
					}
					identList(il);
					match(RCURLY);
				}
				else if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
					q2=qualident();
					if ( inputState.guessing==0 ) {
						cr.addNormalPart(q2);
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					consume();
					consumeUntil(_tokenSet_18);
				} else {
				  throw ex;
				}
			}
		}
		
	public final void identList(
		IdentList ail
	) throws RecognitionException, TokenStreamException {
		
		Token  s = null;
		Token  s2 = null;
		
		try {      // for error handling
			s = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				ail.push(new IdentExpression(s));
			}
			{
			_loop88:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					s2 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						ail.push(new IdentExpression(s2));
					}
				}
				else {
					break _loop88;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void inhTypeName(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_const:
			{
				{
				switch ( LA(1)) {
				case LITERAL_const:
				{
					match(LITERAL_const);
					if ( inputState.guessing==0 ) {
						tn.set(TypeModifiers.CONST);
					}
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				specifiedGenericTypeName_xx(tn);
				break;
			}
			case LITERAL_typeof:
			{
				match(LITERAL_typeof);
				xy=qualident();
				if ( inputState.guessing==0 ) {
					tn.typeof(xy);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void docstrings(
		Documentable sc
	) throws RecognitionException, TokenStreamException {
		
		Token  s1 = null;
		
		try {      // for error handling
			{
			boolean synPredMatched40 = false;
			if (((LA(1)==STRING_LITERAL) && (_tokenSet_20.member(LA(2))))) {
				int _m40 = mark();
				synPredMatched40 = true;
				inputState.guessing++;
				try {
					{
					match(STRING_LITERAL);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched40 = false;
				}
				rewind(_m40);
				inputState.guessing--;
			}
			if ( synPredMatched40 ) {
				{
				int _cnt42=0;
				_loop42:
				do {
					if ((LA(1)==STRING_LITERAL) && (_tokenSet_20.member(LA(2)))) {
						s1 = LT(1);
						match(STRING_LITERAL);
						if ( inputState.guessing==0 ) {
							if (sc!=null) sc.addDocString(s1);
						}
					}
					else {
						if ( _cnt42>=1 ) { break _loop42; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt42++;
				} while (true);
				}
			}
			else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_20);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void opfal(
		FormalArgList fal
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LPAREN);
			formalArgList(fal);
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_22);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void scope(
		Scope sc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			_loop65:
			do {
				if ((_tokenSet_23.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_class:
					case ANNOT:
					{
						classStatement(new ClassStatement(sc.getParent()));
						break;
					}
					case LITERAL_continue:
					{
						match(LITERAL_continue);
						break;
					}
					case LITERAL_break:
					{
						match(LITERAL_break);
						break;
					}
					case LITERAL_return:
					{
						match(LITERAL_return);
						{
						boolean synPredMatched63 = false;
						if (((_tokenSet_24.member(LA(1))) && (_tokenSet_25.member(LA(2))))) {
							int _m63 = mark();
							synPredMatched63 = true;
							inputState.guessing++;
							try {
								{
								expression();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched63 = false;
							}
							rewind(_m63);
							inputState.guessing--;
						}
						if ( synPredMatched63 ) {
							{
							expr=expression();
							}
						}
						else if ((_tokenSet_26.member(LA(1))) && (_tokenSet_6.member(LA(2)))) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						break;
					}
					default:
						if ((_tokenSet_27.member(LA(1))) && (_tokenSet_28.member(LA(2)))) {
							statement(sc.statementClosure(), sc.getParent());
						}
						else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_25.member(LA(2)))) {
							expr=expression();
							if ( inputState.guessing==0 ) {
								sc.statementWrapper(expr);
							}
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop65;
				}
				
			} while (true);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_29);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void functionDef(
		FunctionDef fd
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		
		try {      // for error handling
			i1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				fd.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LITERAL_const:
			{
				match(LITERAL_const);
				break;
			}
			case LITERAL_immutable:
			{
				match(LITERAL_immutable);
				break;
			}
			case LPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			opfal(fd.fal());
			{
			switch ( LA(1)) {
			case TOK_ARROW:
			{
				match(TOK_ARROW);
				typeName(fd.returnType());
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			if ((LA(1)==LCURLY) && (_tokenSet_30.member(LA(2)))) {
				scope(fd.scope());
			}
			else {
				boolean synPredMatched71 = false;
				if (((LA(1)==LCURLY) && (LA(2)==STRING_LITERAL||LA(2)==LITERAL_abstract))) {
					int _m71 = mark();
					synPredMatched71 = true;
					inputState.guessing++;
					try {
						{
						match(LCURLY);
						docstrings(null);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched71 = false;
					}
					rewind(_m71);
					inputState.guessing--;
				}
				if ( synPredMatched71 ) {
					match(LCURLY);
					docstrings(fd.scope());
					match(LITERAL_abstract);
					if ( inputState.guessing==0 ) {
						fd.setAbstract(true);
					}
					match(RCURLY);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					consume();
					consumeUntil(_tokenSet_15);
				} else {
				  throw ex;
				}
			}
		}
		
	public final void varStmt(
		StatementClosure cr, OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		VariableSequence vsq=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_var:
			{
				match(LITERAL_var);
				if ( inputState.guessing==0 ) {
					vsq=cr.varSeq();
				}
				break;
			}
			case LITERAL_const:
			case LITERAL_val:
			{
				{
				switch ( LA(1)) {
				case LITERAL_const:
				{
					match(LITERAL_const);
					break;
				}
				case LITERAL_val:
				{
					match(LITERAL_val);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					vsq=cr.varSeq();vsq.defaultModifiers(TypeModifiers.CONST);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			varStmt_i(vsq.next());
			{
			_loop78:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					varStmt_i(vsq.next());
				}
				else {
					break _loop78;
				}
				
			} while (true);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_31);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void typeAlias(
		TypeAliasExpression cr
	) throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		Token  name = null;
		Qualident q=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_type:
			{
				match(LITERAL_type);
				match(LITERAL_alias);
				i = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					cr.setIdent(i);
				}
				match(BECOMES);
				q=qualident();
				if ( inputState.guessing==0 ) {
					cr.setBecomes(q);
				}
				break;
			}
			case LITERAL_struct:
			{
				match(LITERAL_struct);
				name = LT(1);
				match(IDENT);
				opfal(null);
				scope(null);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void accessNotation() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_access);
			match(IDENT);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void specifiedGenericTypeName_xx(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		RegularTypeName rtn=new RegularTypeName();
		
		try {      // for error handling
			simpleTypeName_xx(tn);
			{
			if ((LA(1)==LBRACK) && (_tokenSet_32.member(LA(2)))) {
				match(LBRACK);
				typeName(rtn);
				if ( inputState.guessing==0 ) {
					tn.addGenericPart(rtn);
				}
				match(RBRACK);
			}
			else if ((_tokenSet_3.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case QUESTION:
			{
				match(QUESTION);
				if ( inputState.guessing==0 ) {
					tn.setNullable();
				}
				break;
			}
			case EOF:
			case AS:
			case CAST_TO:
			case LITERAL_package:
			case LITERAL_indexing:
			case IDENT:
			case TOK_COLON:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LBRACK:
			case RBRACK:
			case DOT:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case RPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case BECOMES:
			case LT_:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case BOR:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
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
			case LOR:
			case LAND:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case GT:
			case LE:
			case GE:
			case LITERAL_is_a:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			case LITERAL_to:
			case LITERAL_with:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void typeName(
		TypeName cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			if ((_tokenSet_34.member(LA(1))) && (_tokenSet_35.member(LA(2)))) {
				structTypeName(cr);
			}
			else if ((LA(1)==LITERAL_function||LA(1)==LITERAL_procedure)) {
				funcTypeExpr(cr);
			}
			else if ((LA(1)==IDENT) && (_tokenSet_8.member(LA(2)))) {
				simpleTypeName_xx(cr);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void structTypeName(
		TypeName cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_const:
			case LITERAL_ref:
			case LITERAL_generic:
			case QUESTION:
			{
				genericQualifiers(cr);
				{
				switch ( LA(1)) {
				case LITERAL_generic:
				case QUESTION:
				{
					abstractGenericTypeName_xx(cr);
					break;
				}
				case IDENT:
				{
					specifiedGenericTypeName_xx(cr);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LITERAL_typeof:
			{
				match(LITERAL_typeof);
				xy=qualident();
				if ( inputState.guessing==0 ) {
					cr.typeof(xy);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void funcTypeExpr(
		TypeName pc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_function:
			{
				match(LITERAL_function);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.FUNCTION);	
				}
				{
				if ((LA(1)==LPAREN) && (_tokenSet_32.member(LA(2)))) {
					match(LPAREN);
					typeNameList(pc.argList());
					match(RPAREN);
				}
				else if ((_tokenSet_36.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				{
				if ((LA(1)==TOK_COLON||LA(1)==TOK_ARROW) && (_tokenSet_32.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case TOK_ARROW:
					{
						match(TOK_ARROW);
						break;
					}
					case TOK_COLON:
					{
						match(TOK_COLON);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					typeName(pc.returnValue());
				}
				else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			case LITERAL_procedure:
			{
				match(LITERAL_procedure);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.PROCEDURE);	
				}
				{
				if ((LA(1)==LPAREN) && (_tokenSet_32.member(LA(2)))) {
					match(LPAREN);
					typeNameList(pc.argList());
					match(RPAREN);
				}
				else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_33.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void simpleTypeName_xx(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			xy=qualident();
			if ( inputState.guessing==0 ) {
				tn.setName(xy);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void statement(
		StatementClosure cr, OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		Qualident q=null;FormalArgList o=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LPAREN:
			case LCURLY:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				expr=postfixExpression();
				if ( inputState.guessing==0 ) {
					cr.statementWrapper(expr);
				}
				break;
			}
			case LITERAL_if:
			{
				ifConditional(cr.ifConditional());
				break;
			}
			case LITERAL_match:
			{
				matchConditional(cr.matchConditional(), aParent);
				break;
			}
			case LITERAL_case:
			{
				caseConditional(cr.caseConditional());
				break;
			}
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			{
				varStmt(cr, aParent);
				break;
			}
			case LITERAL_while:
			case LITERAL_do:
			{
				whileLoop(cr);
				break;
			}
			case LITERAL_iterate:
			{
				frobeIteration(cr);
				break;
			}
			case LITERAL_construct:
			{
				match(LITERAL_construct);
				q=qualident();
				o=opfal2();
				if ( inputState.guessing==0 ) {
					cr.constructExpression(q,o);
				}
				break;
			}
			case LITERAL_yield:
			{
				match(LITERAL_yield);
				expr=expression();
				if ( inputState.guessing==0 ) {
					cr.yield(expr);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			opt_semi();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final IExpression  expression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
		
		try {      // for error handling
			ee=assignmentExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void aliasStatement(
		AliasStatement pc
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		
		try {      // for error handling
			match(LITERAL_alias);
			i1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				pc.setName(i1);
			}
			match(BECOMES);
			xy=qualident();
			if ( inputState.guessing==0 ) {
				pc.setExpression(xy);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void varStmt_i(
		VariableStatement vs
	) throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		
		try {      // for error handling
			i = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				vs.setName(i);
			}
			{
			switch ( LA(1)) {
			case TOK_COLON:
			{
				match(TOK_COLON);
				typeName(vs.typeName());
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case BECOMES:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case BECOMES:
			{
				match(BECOMES);
				expr=expression();
				if ( inputState.guessing==0 ) {
					vs.initial(expr);
				}
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_38);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void formalArgList(
		FormalArgList fal
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_const:
			case LITERAL_in:
			case LITERAL_out:
			case LITERAL_ref:
			case LITERAL_generic:
			case QUESTION:
			{
				formalArgListItem_priv(fal.next());
				{
				_loop228:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						formalArgListItem_priv(fal.next());
					}
					else {
						break _loop228;
					}
					
				} while (true);
				}
				break;
			}
			case RPAREN:
			case BOR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_39);
			} else {
			  throw ex;
			}
		}
	}
	
	public final FormalArgList  opfal2() throws RecognitionException, TokenStreamException {
		FormalArgList fal;
		
		fal=new FormalArgList();
		
		try {      // for error handling
			match(LPAREN);
			formalArgList(fal);
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
		return fal;
	}
	
	public final IExpression  postfixExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  lb = null;
		Token  rb = null;
		Token  lp = null;
		Token  in = null;
		Token  de = null;
		ee=null;TypeCastExpression tc=null;
				IExpression e3=null;ExpressionList el=null;
		
		try {      // for error handling
			ee=primaryExpression();
			{
			_loop167:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					match(DOT);
					{
					ee=dot_expression_or_procedure_call(ee);
					}
					break;
				}
				case LBRACK:
				{
					lb = LT(1);
					match(LBRACK);
					expr=expression();
					rb = LT(1);
					match(RBRACK);
					if ( inputState.guessing==0 ) {
						ee=new GetItemExpression(ee, expr);((GetItemExpression)ee).parens(lb,rb);
					}
					break;
				}
				default:
					if ((LA(1)==LPAREN) && (_tokenSet_41.member(LA(2)))) {
						lp = LT(1);
						match(LPAREN);
						{
						switch ( LA(1)) {
						case IDENT:
						case STRING_LITERAL:
						case CHAR_LITERAL:
						case NUM_INT:
						case NUM_FLOAT:
						case LPAREN:
						case LCURLY:
						case PLUS:
						case MINUS:
						case INC:
						case DEC:
						case BNOT:
						case LNOT:
						case LITERAL_true:
						case LITERAL_false:
						case LITERAL_this:
						case LITERAL_null:
						case LITERAL_function:
						case LITERAL_procedure:
						{
							el=expressionList2();
							break;
						}
						case RPAREN:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						if ( inputState.guessing==0 ) {
							ProcedureCallExpression pce=new ProcedureCallExpression();
							pce.identifier(ee);
							pce.setArgs(el);
							ee=pce;
						}
						match(RPAREN);
					}
				else {
					break _loop167;
				}
				}
			} while (true);
			}
			{
			if ((LA(1)==INC) && (_tokenSet_42.member(LA(2)))) {
				in = LT(1);
				match(INC);
			}
			else if ((LA(1)==DEC) && (_tokenSet_42.member(LA(2)))) {
				de = LT(1);
				match(DEC);
			}
			else if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case AS:
			case CAST_TO:
			{
				{
				switch ( LA(1)) {
				case AS:
				{
					match(AS);
					break;
				}
				case CAST_TO:
				{
					match(CAST_TO);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					tc=new TypeCastExpression();ee=tc;
				}
				typeName(tc.typeName());
				break;
			}
			case EOF:
			case LITERAL_package:
			case LITERAL_indexing:
			case IDENT:
			case TOK_COLON:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case RBRACK:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case RPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case BECOMES:
			case LT_:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case BOR:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
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
			case LOR:
			case LAND:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case GT:
			case LE:
			case GE:
			case LITERAL_is_a:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			case LITERAL_to:
			case LITERAL_with:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void ifConditional(
		IfConditional ifex
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_if);
			expr=expression();
			if ( inputState.guessing==0 ) {
				ifex.expr(expr);
			}
			scope(ifex.scope());
			{
			_loop187:
			do {
				if ((LA(1)==LITERAL_else) && (LA(2)==LCURLY)) {
					match(LITERAL_else);
					scope(ifex.else_().scope());
				}
				else if ((LA(1)==LITERAL_else||LA(1)==LITERAL_elseif) && (_tokenSet_45.member(LA(2)))) {
					elseif_part(ifex.elseif());
				}
				else {
					break _loop187;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void matchConditional(
		MatchConditional mc, OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		MatchConditional.MatchConditionalPart1 mcp1=null;
				 MatchConditional.MatchConditionalPart2 mcp2=null;
		
		try {      // for error handling
			match(LITERAL_match);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.setParent(aParent);mc.expr(expr);
			}
			match(LCURLY);
			{
			int _cnt190=0;
			_loop190:
			do {
				if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
					if ( inputState.guessing==0 ) {
						mcp1 = mc.typeMatch();
					}
					i1 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						mcp1.ident(i1);
					}
					match(TOK_COLON);
					typeName(mcp1.typeName());
					scope(mcp1.scope());
				}
				else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
					if ( inputState.guessing==0 ) {
						mcp2 = mc.normal();
					}
					expr=expression();
					if ( inputState.guessing==0 ) {
						mcp2.expr(expr);
					}
					scope(mcp2.scope());
				}
				else {
					if ( _cnt190>=1 ) { break _loop190; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt190++;
			} while (true);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void caseConditional(
		CaseConditional mc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_case);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.expr(expr);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void whileLoop(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		Loop loop=cr.loop();
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_while:
			{
				match(LITERAL_while);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes2.WHILE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.expr(expr);
				}
				scope(loop.scope());
				break;
			}
			case LITERAL_do:
			{
				match(LITERAL_do);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes2.DO_WHILE);
				}
				scope(loop.scope());
				match(LITERAL_while);
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.expr(expr);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void frobeIteration(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		Token  i2 = null;
		Token  i3 = null;
		Loop loop=cr.loop();
		
		try {      // for error handling
			match(LITERAL_iterate);
			{
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes2.FROM_TO_TYPE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.frompart(expr);
				}
				match(LITERAL_to);
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.topart(expr);
				}
				{
				switch ( LA(1)) {
				case LITERAL_with:
				{
					match(LITERAL_with);
					i1 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						loop.iterName(i1);
					}
					break;
				}
				case LCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LITERAL_to:
			{
				match(LITERAL_to);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes2.TO_TYPE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.topart(expr);
				}
				{
				switch ( LA(1)) {
				case LITERAL_with:
				{
					match(LITERAL_with);
					i2 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						loop.iterName(i2);
					}
					break;
				}
				case LCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LPAREN:
			case LCURLY:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes2.EXPR_TYPE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.topart(expr);
				}
				{
				switch ( LA(1)) {
				case LITERAL_with:
				{
					match(LITERAL_with);
					i3 = LT(1);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						loop.iterName(i3);
					}
					break;
				}
				case LCURLY:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			scope(loop.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final IExpression  assignmentExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e=null;IExpression e2;ExpressionKind ek=null;
		
		try {      // for error handling
			ee=conditionalExpression();
			{
			switch ( LA(1)) {
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
			case BOR_ASSIGN:
			{
				{
				switch ( LA(1)) {
				case BECOMES:
				{
					match(BECOMES);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.ASSIGNMENT);
					}
					break;
				}
				case PLUS_ASSIGN:
				{
					match(PLUS_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_PLUS);
					}
					break;
				}
				case MINUS_ASSIGN:
				{
					match(MINUS_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_MINUS);
					}
					break;
				}
				case STAR_ASSIGN:
				{
					match(STAR_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_MULT);
					}
					break;
				}
				case DIV_ASSIGN:
				{
					match(DIV_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_DIV);
					}
					break;
				}
				case MOD_ASSIGN:
				{
					match(MOD_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_MOD);
					}
					break;
				}
				case SR_ASSIGN:
				{
					match(SR_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_SR);
					}
					break;
				}
				case BSR_ASSIGN:
				{
					match(BSR_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_BSR);
					}
					break;
				}
				case SL_ASSIGN:
				{
					match(SL_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_SL);
					}
					break;
				}
				case BAND_ASSIGN:
				{
					match(BAND_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_BAND);
					}
					break;
				}
				case BXOR_ASSIGN:
				{
					match(BXOR_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_BXOR);
					}
					break;
				}
				case BOR_ASSIGN:
				{
					match(BOR_ASSIGN);
					if ( inputState.guessing==0 ) {
						ek= (ExpressionKind.AUG_BOR);
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				e2=assignmentExpression();
				if ( inputState.guessing==0 ) {
					ee = ExpressionBuilder.build(ee, ek, e2);
				}
				break;
			}
			case EOF:
			case LITERAL_package:
			case LITERAL_indexing:
			case IDENT:
			case TOK_COLON:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case RBRACK:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case RPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			case LITERAL_to:
			case LITERAL_with:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_37);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void variableQualifiers(
		TypeName cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_once:
			{
				match(LITERAL_once);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.ONCE);
				}
				break;
			}
			case LITERAL_local:
			{
				match(LITERAL_local);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.LOCAL);
				}
				break;
			}
			case LITERAL_tagged:
			{
				match(LITERAL_tagged);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.TAGGED);
				}
				break;
			}
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.CONST);
				}
				break;
			}
			case LITERAL_pooled:
			{
				match(LITERAL_pooled);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.POOLED);
				}
				break;
			}
			case LITERAL_manual:
			{
				match(LITERAL_manual);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.MANUAL);
				}
				break;
			}
			case LITERAL_gc:
			{
				match(LITERAL_gc);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.GC);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void regularQualifiers(
		TypeName fp
	) throws RecognitionException, TokenStreamException {
		
		Token  i2 = null;
		IdentExpression i1=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_in:
			{
				match(LITERAL_in);
				if ( inputState.guessing==0 ) {
					fp.setIn(true);
				}
				break;
			}
			case LITERAL_out:
			{
				match(LITERAL_out);
				if ( inputState.guessing==0 ) {
					fp.setOut(true);
				}
				break;
			}
			case LITERAL_const:
			case LITERAL_ref:
			case LITERAL_generic:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LITERAL_const:
			{
				{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					fp.setConstant(true);
				}
				{
				switch ( LA(1)) {
				case LITERAL_ref:
				{
					match(LITERAL_ref);
					if ( inputState.guessing==0 ) {
						fp.setReference(true);
					}
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				break;
			}
			case LITERAL_ref:
			{
				match(LITERAL_ref);
				if ( inputState.guessing==0 ) {
					fp.setReference(true);
				}
				break;
			}
			case LITERAL_generic:
			{
				match(LITERAL_generic);
				i2 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					fp.setGeneric(true);
					RegularTypeName rtn = new RegularTypeName();
					Qualident q = new Qualident();
					q.append(i2);
					rtn.setName(q);
					fp.addGenericPart(rtn);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_47);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void typeNameList(
		TypeNameList cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			typeName(cr.next());
			{
			_loop99:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					typeName(cr.next());
				}
				else {
					break _loop99;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_48);
			} else {
			  throw ex;
			}
		}
	}
	
	public final String  ident2() throws RecognitionException, TokenStreamException {
		String ident;
		
		Token  r1 = null;
		ident=null;
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				ident=r1.getText();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return ident;
	}
	
	public final IdentExpression  ident() throws RecognitionException, TokenStreamException {
		IdentExpression id;
		
		Token  r1 = null;
		id=null;
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				id=new IdentExpression(r1);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return id;
	}
	
	public final ExpressionList  expressionList2() throws RecognitionException, TokenStreamException {
		ExpressionList el;
		
		el = new ExpressionList();
		
		try {      // for error handling
			expr=expression();
			if ( inputState.guessing==0 ) {
				el.next(expr);
			}
			{
			_loop111:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expr=expression();
					if ( inputState.guessing==0 ) {
						el.next(expr);
					}
				}
				else {
					break _loop111;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_48);
			} else {
			  throw ex;
			}
		}
		return el;
	}
	
	public final ProcedureCallExpression  procCallEx2() throws RecognitionException, TokenStreamException {
		ProcedureCallExpression pce;
		
		Token  lp = null;
		Token  rp = null;
		pce=null;ExpressionList el=null;
		
		try {      // for error handling
			lp = LT(1);
			match(LPAREN);
			el=expressionList2();
			rp = LT(1);
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				pce=new ProcedureCallExpression(lp,el,rp);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return pce;
	}
	
	public final IExpression  conditionalExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
		
		try {      // for error handling
			ee=logicalOrExpression();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_49);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  logicalOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=logicalAndExpression();
			{
			_loop127:
			do {
				if ((LA(1)==LOR)) {
					match(LOR);
					e3=logicalAndExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LOR, e3);
					}
				}
				else {
					break _loop127;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_49);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  logicalAndExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e3=null;
		
		try {      // for error handling
			ee=inclusiveOrExpression();
			{
			_loop130:
			do {
				if ((LA(1)==LAND)) {
					match(LAND);
					e3=inclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LAND, e3);
					}
				}
				else {
					break _loop130;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_50);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e3=null;
		
		try {      // for error handling
			ee=exclusiveOrExpression();
			{
			_loop133:
			do {
				if ((LA(1)==BOR)) {
					match(BOR);
					e3=exclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BOR, e3);
					}
				}
				else {
					break _loop133;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_51);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=andExpression();
			{
			_loop136:
			do {
				if ((LA(1)==BXOR)) {
					match(BXOR);
					e3=andExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BXOR, e3);
					}
				}
				else {
					break _loop136;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_52);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  andExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=equalityExpression();
			{
			_loop139:
			do {
				if ((LA(1)==BAND)) {
					match(BAND);
					e3=equalityExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BAND, e3);
					}
				}
				else {
					break _loop139;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_53);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  equalityExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				ExpressionKind e2=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=relationalExpression();
			{
			_loop143:
			do {
				if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
					{
					switch ( LA(1)) {
					case NOT_EQUAL:
					{
						match(NOT_EQUAL);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.NOT_EQUAL;
						}
						break;
					}
					case EQUAL:
					{
						match(EQUAL);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.EQUAL;
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e3=relationalExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, e2, e3);
					}
				}
				else {
					break _loop143;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_54);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  relationalExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				ExpressionKind e2=null; // should never be null (below)
				IExpression e3=null;
				TypeName tn=new RegularTypeName();
		
		try {      // for error handling
			ee=shiftExpression();
			{
			switch ( LA(1)) {
			case EOF:
			case LITERAL_package:
			case LITERAL_indexing:
			case IDENT:
			case TOK_COLON:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case RBRACK:
			case LITERAL_class:
			case LITERAL_struct:
			case LPAREN:
			case RPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case BECOMES:
			case LT_:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_type:
			case BOR:
			case LITERAL_const:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
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
			case LOR:
			case LAND:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_if:
			case LITERAL_match:
			case LITERAL_case:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_iterate:
			case LITERAL_to:
			case LITERAL_with:
			{
				{
				_loop148:
				do {
					if ((_tokenSet_55.member(LA(1)))) {
						{
						switch ( LA(1)) {
						case LT_:
						{
							match(LT_);
							if ( inputState.guessing==0 ) {
								e2=ExpressionKind.LT_;
							}
							break;
						}
						case GT:
						{
							match(GT);
							if ( inputState.guessing==0 ) {
								e2=ExpressionKind.GT;
							}
							break;
						}
						case LE:
						{
							match(LE);
							if ( inputState.guessing==0 ) {
								e2=ExpressionKind.LE;
							}
							break;
						}
						case GE:
						{
							match(GE);
							if ( inputState.guessing==0 ) {
								e2=ExpressionKind.GE;
							}
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						e3=shiftExpression();
						if ( inputState.guessing==0 ) {
							ee=ExpressionBuilder.build(ee,e2,e3);
																	ee.setType(new OS_Type(BuiltInTypes.Boolean));
						}
					}
					else {
						break _loop148;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_is_a:
			{
				match(LITERAL_is_a);
				typeName(tn);
				if ( inputState.guessing==0 ) {
					ee=new TypeCheckExpression(ee, tn);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_56);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  shiftExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;ExpressionKind e2=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=additiveExpression();
			{
			_loop152:
			do {
				if (((LA(1) >= SL && LA(1) <= BSR))) {
					{
					switch ( LA(1)) {
					case SL:
					{
						match(SL);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.LSHIFT;
						}
						break;
					}
					case SR:
					{
						match(SR);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.RSHIFT;
						}
						break;
					}
					case BSR:
					{
						match(BSR);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.BSHIFTR;
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e3=additiveExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, e2, e3);
					}
				}
				else {
					break _loop152;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_57);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  additiveExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;ExpressionKind e2=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=multiplicativeExpression();
			{
			_loop156:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_24.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						match(PLUS);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.ADDITION;
						}
						break;
					}
					case MINUS:
					{
						match(MINUS);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.SUBTRACTION;
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e3=multiplicativeExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, e2, e3);
					}
				}
				else {
					break _loop156;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_58);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  multiplicativeExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;ExpressionKind e2=null;
		
		try {      // for error handling
			ee=unaryExpression();
			{
			_loop160:
			do {
				if (((LA(1) >= STAR && LA(1) <= MOD))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						match(STAR);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.MULTIPLY;
						}
						break;
					}
					case DIV:
					{
						match(DIV);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.DIVIDE;
						}
						break;
					}
					case MOD:
					{
						match(MOD);
						if ( inputState.guessing==0 ) {
							e2=ExpressionKind.MODULO;
						}
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					e3=unaryExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, e2, e3);
					}
				}
				else {
					break _loop160;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_58);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  unaryExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INC:
			{
				match(INC);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.INC);
				}
				break;
			}
			case DEC:
			{
				match(DEC);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.DEC);
				}
				break;
			}
			case MINUS:
			{
				match(MINUS);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.NEG);
				}
				break;
			}
			case PLUS:
			{
				match(PLUS);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.POS);
				}
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LPAREN:
			case LCURLY:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				ee=unaryExpressionNotPlusMinus();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BNOT:
			{
				match(BNOT);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.BNOT);
				}
				break;
			}
			case LNOT:
			{
				match(LNOT);
				ee=unaryExpression();
				if ( inputState.guessing==0 ) {
					ee.setKind(ExpressionKind.LNOT);
				}
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LPAREN:
			case LCURLY:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				ee=postfixExpression();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  primaryExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  e = null;
		ee=null;FuncExpr ppc=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				e = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					ee=new IdentExpression(e);
				}
				break;
			}
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			{
				ee=constantValue();
				break;
			}
			case LITERAL_true:
			{
				match(LITERAL_true);
				break;
			}
			case LITERAL_false:
			{
				match(LITERAL_false);
				break;
			}
			case LITERAL_this:
			{
				match(LITERAL_this);
				break;
			}
			case LITERAL_null:
			{
				match(LITERAL_null);
				break;
			}
			case LPAREN:
			{
				match(LPAREN);
				ee=assignmentExpression();
				match(RPAREN);
				if ( inputState.guessing==0 ) {
					ee=new SubExpression(ee);
				}
				break;
			}
			case LCURLY:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				if ( inputState.guessing==0 ) {
					ppc=new FuncExpr();
				}
				funcExpr(ppc);
				if ( inputState.guessing==0 ) {
					ee=ppc;
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  dot_expression_or_procedure_call(
		IExpression e1
	) throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  e = null;
		Token  lp2 = null;
		ee=null;ExpressionList el=null;
		
		try {      // for error handling
			e = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				ee=new DotExpression(e1, new IdentExpression(e));
			}
			{
			if ((LA(1)==LPAREN) && (_tokenSet_41.member(LA(2)))) {
				lp2 = LT(1);
				match(LPAREN);
				{
				switch ( LA(1)) {
				case IDENT:
				case STRING_LITERAL:
				case CHAR_LITERAL:
				case NUM_INT:
				case NUM_FLOAT:
				case LPAREN:
				case LCURLY:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_this:
				case LITERAL_null:
				case LITERAL_function:
				case LITERAL_procedure:
				{
					el=expressionList2();
					break;
				}
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					ProcedureCallExpression pce=new ProcedureCallExpression();
					pce.identifier(ee);
					pce.setArgs(el);
					ee=pce;
				}
				match(RPAREN);
			}
			else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void funcExpr(
		FuncExpr pc
	) throws RecognitionException, TokenStreamException {
		
		Scope0 sc = new Scope0(pc);
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_function:
			{
				match(LITERAL_function);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.FUNCTION);	
				}
				{
				opfal(pc.argList());
				}
				scope(pc.scope());
				{
				if ((LA(1)==TOK_COLON||LA(1)==TOK_ARROW) && (_tokenSet_32.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case TOK_ARROW:
					{
						match(TOK_ARROW);
						break;
					}
					case TOK_COLON:
					{
						match(TOK_COLON);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					typeName(pc.returnType());
				}
				else if ((_tokenSet_8.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			case LITERAL_procedure:
			{
				match(LITERAL_procedure);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.PROCEDURE);	
				}
				{
				opfal(pc.argList());
				}
				scope(pc.scope());
				break;
			}
			case LCURLY:
			{
				match(LCURLY);
				{
				switch ( LA(1)) {
				case BOR:
				{
					match(BOR);
					formalArgList(sc.fal());
					match(BOR);
					break;
				}
				case IDENT:
				case STRING_LITERAL:
				case CHAR_LITERAL:
				case NUM_INT:
				case NUM_FLOAT:
				case LITERAL_class:
				case LPAREN:
				case LCURLY:
				case RCURLY:
				case ANNOT:
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				case LITERAL_construct:
				case LITERAL_yield:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_this:
				case LITERAL_null:
				case LITERAL_function:
				case LITERAL_procedure:
				case LITERAL_if:
				case LITERAL_match:
				case LITERAL_case:
				case LITERAL_while:
				case LITERAL_do:
				case LITERAL_iterate:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop183:
				do {
					if ((_tokenSet_27.member(LA(1))) && (_tokenSet_59.member(LA(2)))) {
						statement(sc.statementClosure(), sc.getParent());
					}
					else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_46.member(LA(2)))) {
						expr=expression();
						if ( inputState.guessing==0 ) {
							sc.statementWrapper(expr);
						}
					}
					else if ((LA(1)==LITERAL_class||LA(1)==ANNOT)) {
						classStatement(new ClassStatement(sc.getParent()));
					}
					else {
						break _loop183;
					}
					
				} while (true);
				}
				match(RCURLY);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void procedureCallStatement(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		ProcedureCallExpression pce=cr.procedureCallExpression();
		
		try {      // for error handling
			xy=qualident();
			if ( inputState.guessing==0 ) {
				pce.identifier(xy);
			}
			procCallEx(pce);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void procCallEx(
		ProcedureCallExpression pce
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LPAREN:
			case LCURLY:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				expressionList(pce.exprList());
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void elseif_part(
		IfConditional ifex
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_elseif:
			{
				match(LITERAL_elseif);
				break;
			}
			case LITERAL_else:
			{
				match(LITERAL_else);
				match(LITERAL_if);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expr=expression();
			if ( inputState.guessing==0 ) {
				ifex.expr(expr);
			}
			scope(ifex.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_60);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void genericQualifiers(
		TypeName cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.CONST);
				}
				break;
			}
			case IDENT:
			case LITERAL_ref:
			case LITERAL_generic:
			case QUESTION:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LITERAL_ref:
			{
				match(LITERAL_ref);
				if ( inputState.guessing==0 ) {
					cr.set(TypeModifiers.REFPAR);
				}
				break;
			}
			case IDENT:
			case LITERAL_generic:
			case QUESTION:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_61);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void abstractGenericTypeName_xx(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_generic:
			{
				match(LITERAL_generic);
				xy=qualident();
				if ( inputState.guessing==0 ) {
					tn.typeName(xy); tn.set(TypeModifiers.GENERIC);
				}
				break;
			}
			case QUESTION:
			{
				match(QUESTION);
				xy=qualident();
				if ( inputState.guessing==0 ) {
					tn.typeName(xy); tn.set(TypeModifiers.GENERIC);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void formalArgTypeName(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			case LITERAL_const:
			case LITERAL_typeof:
			case LITERAL_ref:
			case LITERAL_generic:
			case QUESTION:
			{
				structTypeName(tn);
				break;
			}
			case LITERAL_function:
			case LITERAL_procedure:
			{
				funcTypeExpr(tn);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_62);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void defFunctionDef(
		DefFunctionDef fd
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		FormalArgList op=null;
		
		try {      // for error handling
			match(LITERAL_def);
			i1 = LT(1);
			match(IDENT);
			op=opfal2();
			match(BECOMES);
			expr=expression();
			if ( inputState.guessing==0 ) {
				fd.setType(DefFunctionDef.DEF_FUN); fd.setName(i1); fd.setOpfal(op); fd.setExpr(expr);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void formalArgListItem_priv(
		FormalArgListItem fali
	) throws RecognitionException, TokenStreamException {
		
		Token  i = null;
		
		try {      // for error handling
			{
			if ((_tokenSet_63.member(LA(1))) && (_tokenSet_64.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case LITERAL_const:
				case LITERAL_in:
				case LITERAL_out:
				case LITERAL_ref:
				case LITERAL_generic:
				{
					regularQualifiers(fali.typeName());
					break;
				}
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				i = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
						fali.setName(i);	
				}
				{
				switch ( LA(1)) {
				case TOK_COLON:
				{
					match(TOK_COLON);
					formalArgTypeName(fali.typeName());
					break;
				}
				case RPAREN:
				case COMMA:
				case BOR:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else if ((LA(1)==LITERAL_generic||LA(1)==QUESTION) && (LA(2)==IDENT)) {
				abstractGenericTypeName_xx(fali.typeName());
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_62);
			} else {
			  throw ex;
			}
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"as\"",
		"\"cast_to\"",
		"\"package\"",
		"\"indexing\"",
		"IDENT",
		"TOK_COLON",
		"STRING_LITERAL",
		"CHAR_LITERAL",
		"NUM_INT",
		"NUM_FLOAT",
		"LBRACK",
		"RBRACK",
		"DOT",
		"\"class\"",
		"\"interface\"",
		"\"struct\"",
		"\"signature\"",
		"\"abstract\"",
		"LPAREN",
		"RPAREN",
		"LCURLY",
		"RCURLY",
		"ANNOT",
		"\"namespace\"",
		"\"from\"",
		"\"import\"",
		"COMMA",
		"BECOMES",
		"LT_",
		"\"constructor\"",
		"\"ctor\"",
		"\"destructor\"",
		"\"dtor\"",
		"\"type\"",
		"BOR",
		"\"const\"",
		"\"typeof\"",
		"\"continue\"",
		"\"break\"",
		"\"return\"",
		"\"immutable\"",
		"TOK_ARROW",
		"\"var\"",
		"\"val\"",
		"\"alias\"",
		"\"construct\"",
		"\"yield\"",
		"SEMI",
		"\"once\"",
		"\"local\"",
		"\"tagged\"",
		"\"pooled\"",
		"\"manual\"",
		"\"gc\"",
		"\"in\"",
		"\"out\"",
		"\"ref\"",
		"\"generic\"",
		"\"invariant\"",
		"\"access\"",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"LOR",
		"LAND",
		"BXOR",
		"BAND",
		"NOT_EQUAL",
		"EQUAL",
		"GT",
		"LE",
		"GE",
		"\"is_a\"",
		"SL",
		"SR",
		"BSR",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"BNOT",
		"LNOT",
		"\"true\"",
		"\"false\"",
		"\"this\"",
		"\"null\"",
		"\"function\"",
		"\"procedure\"",
		"\"if\"",
		"\"else\"",
		"\"match\"",
		"\"case\"",
		"\"while\"",
		"\"do\"",
		"\"iterate\"",
		"\"to\"",
		"\"with\"",
		"\"elseif\"",
		"QUESTION",
		"\"def\"",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long _tokenSet_0_data_[] = { 281475983474880L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { 2L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { 2533275797160130L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { -4607236294873186318L, 843325418504191L, 0L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { -4608940820319829566L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { 4449174863101378L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { -4607201110497689614L, 562949953421311L, 0L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 2533275805581762L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { -4607236294873186318L, 280375465082879L, 0L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { -4607236577275789886L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { 67239936L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { 25165824L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { 16777216L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { 33554432L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	private static final long _tokenSet_14_data_[] = { 20987648L, 549269274624L, 0L, 0L };
	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	private static final long _tokenSet_15_data_[] = { -4611192620133515008L, 0L };
	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	private static final long _tokenSet_16_data_[] = { -4611192748982532864L, 0L };
	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	private static final long _tokenSet_17_data_[] = { -4608940819246022206L, 0L };
	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	private static final long _tokenSet_18_data_[] = { -4608940819246087742L, 0L };
	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	private static final long _tokenSet_19_data_[] = { 1098907648L, 0L };
	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);
	private static final long _tokenSet_20_data_[] = { -4609488377087378176L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_20 = new BitSet(_tokenSet_20_data_);
	private static final long _tokenSet_21_data_[] = { -4607183518311645198L, 562949953421311L, 0L, 0L };
	public static final BitSet _tokenSet_21 = new BitSet(_tokenSet_21_data_);
	private static final long _tokenSet_22_data_[] = { 35184388866048L, 0L };
	public static final BitSet _tokenSet_22 = new BitSet(_tokenSet_22_data_);
	private static final long _tokenSet_23_data_[] = { 1915899099626752L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_23 = new BitSet(_tokenSet_23_data_);
	private static final long _tokenSet_24_data_[] = { 20987136L, 549269274624L, 0L, 0L };
	public static final BitSet _tokenSet_24 = new BitSet(_tokenSet_24_data_);
	private static final long _tokenSet_25_data_[] = { 4167980267306288L, 69269232549887L, 0L, 0L };
	public static final BitSet _tokenSet_25 = new BitSet(_tokenSet_25_data_);
	private static final long _tokenSet_26_data_[] = { 4167698946866432L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_26 = new BitSet(_tokenSet_26_data_);
	private static final long _tokenSet_27_data_[] = { 1900505869597952L, 69260642615296L, 0L, 0L };
	public static final BitSet _tokenSet_27 = new BitSet(_tokenSet_27_data_);
	private static final long _tokenSet_28_data_[] = { 4167974093290800L, 139637490188288L, 0L, 0L };
	public static final BitSet _tokenSet_28 = new BitSet(_tokenSet_28_data_);
	private static final long _tokenSet_29_data_[] = { -4607201110501097486L, 562949953421311L, 0L, 0L };
	public static final BitSet _tokenSet_29 = new BitSet(_tokenSet_29_data_);
	private static final long _tokenSet_30_data_[] = { 1915899133181184L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_30 = new BitSet(_tokenSet_30_data_);
	private static final long _tokenSet_31_data_[] = { -4607236577275790080L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_31 = new BitSet(_tokenSet_31_data_);
	private static final long _tokenSet_32_data_[] = { 3458766163087982848L, 563362270281728L, 0L, 0L };
	public static final BitSet _tokenSet_32 = new BitSet(_tokenSet_32_data_);
	private static final long _tokenSet_33_data_[] = { -283726776524341262L, 1125899906842623L, 0L, 0L };
	public static final BitSet _tokenSet_33 = new BitSet(_tokenSet_33_data_);
	private static final long _tokenSet_34_data_[] = { 3458766163087982848L, 562949953421312L, 0L, 0L };
	public static final BitSet _tokenSet_34 = new BitSet(_tokenSet_34_data_);
	private static final long _tokenSet_35_data_[] = { -1148471781052645390L, 843325418504191L, 0L, 0L };
	public static final BitSet _tokenSet_35 = new BitSet(_tokenSet_35_data_);
	private static final long _tokenSet_36_data_[] = { -4607201110501097486L, 280375465082879L, 0L, 0L };
	public static final BitSet _tokenSet_36 = new BitSet(_tokenSet_36_data_);
	private static final long _tokenSet_37_data_[] = { -4607236576193626174L, 280374978543616L, 0L, 0L };
	public static final BitSet _tokenSet_37 = new BitSet(_tokenSet_37_data_);
	private static final long _tokenSet_38_data_[] = { -4607236576202048256L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_38 = new BitSet(_tokenSet_38_data_);
	private static final long _tokenSet_39_data_[] = { 274886295552L, 0L };
	public static final BitSet _tokenSet_39 = new BitSet(_tokenSet_39_data_);
	private static final long _tokenSet_40_data_[] = { 4167701094350080L, 69268746010624L, 0L, 0L };
	public static final BitSet _tokenSet_40 = new BitSet(_tokenSet_40_data_);
	private static final long _tokenSet_41_data_[] = { 29375744L, 549269274624L, 0L, 0L };
	public static final BitSet _tokenSet_41 = new BitSet(_tokenSet_41_data_);
	private static final long _tokenSet_42_data_[] = { -4607236294873268238L, 280375465082879L, 0L, 0L };
	public static final BitSet _tokenSet_42 = new BitSet(_tokenSet_42_data_);
	private static final long _tokenSet_43_data_[] = { -1148417904979476494L, 1125899906842623L, 0L, 0L };
	public static final BitSet _tokenSet_43 = new BitSet(_tokenSet_43_data_);
	private static final long _tokenSet_44_data_[] = { -4607236294873268286L, 280375465082879L, 0L, 0L };
	public static final BitSet _tokenSet_44 = new BitSet(_tokenSet_44_data_);
	private static final long _tokenSet_45_data_[] = { 20987136L, 1099025088512L, 0L, 0L };
	public static final BitSet _tokenSet_45 = new BitSet(_tokenSet_45_data_);
	private static final long _tokenSet_46_data_[] = { 1900787290832176L, 69269232549887L, 0L, 0L };
	public static final BitSet _tokenSet_46 = new BitSet(_tokenSet_46_data_);
	private static final long _tokenSet_47_data_[] = { 256L, 0L };
	public static final BitSet _tokenSet_47 = new BitSet(_tokenSet_47_data_);
	private static final long _tokenSet_48_data_[] = { 8388608L, 0L };
	public static final BitSet _tokenSet_48 = new BitSet(_tokenSet_48_data_);
	private static final long _tokenSet_49_data_[] = { -4607236574046142526L, 280374978545663L, 0L, 0L };
	public static final BitSet _tokenSet_49 = new BitSet(_tokenSet_49_data_);
	private static final long _tokenSet_50_data_[] = { -4607236574046142526L, 280374978547711L, 0L, 0L };
	public static final BitSet _tokenSet_50 = new BitSet(_tokenSet_50_data_);
	private static final long _tokenSet_51_data_[] = { -4607236574046142526L, 280374978551807L, 0L, 0L };
	public static final BitSet _tokenSet_51 = new BitSet(_tokenSet_51_data_);
	private static final long _tokenSet_52_data_[] = { -4607236299168235582L, 280374978551807L, 0L, 0L };
	public static final BitSet _tokenSet_52 = new BitSet(_tokenSet_52_data_);
	private static final long _tokenSet_53_data_[] = { -4607236299168235582L, 280374978559999L, 0L, 0L };
	public static final BitSet _tokenSet_53 = new BitSet(_tokenSet_53_data_);
	private static final long _tokenSet_54_data_[] = { -4607236299168235582L, 280374978576383L, 0L, 0L };
	public static final BitSet _tokenSet_54 = new BitSet(_tokenSet_54_data_);
	private static final long _tokenSet_55_data_[] = { 4294967296L, 917504L, 0L, 0L };
	public static final BitSet _tokenSet_55 = new BitSet(_tokenSet_55_data_);
	private static final long _tokenSet_56_data_[] = { -4607236299168235582L, 280374978674687L, 0L, 0L };
	public static final BitSet _tokenSet_56 = new BitSet(_tokenSet_56_data_);
	private static final long _tokenSet_57_data_[] = { -4607236294873268286L, 280374980640767L, 0L, 0L };
	public static final BitSet _tokenSet_57 = new BitSet(_tokenSet_57_data_);
	private static final long _tokenSet_58_data_[] = { -4607236294873268286L, 280374995320831L, 0L, 0L };
	public static final BitSet _tokenSet_58 = new BitSet(_tokenSet_58_data_);
	private static final long _tokenSet_59_data_[] = { 4152580930501936L, 139637490188288L, 0L, 0L };
	public static final BitSet _tokenSet_59 = new BitSet(_tokenSet_59_data_);
	private static final long _tokenSet_60_data_[] = { 4167698946866432L, 351843234349056L, 0L, 0L };
	public static final BitSet _tokenSet_60 = new BitSet(_tokenSet_60_data_);
	private static final long _tokenSet_61_data_[] = { 2305843009213694208L, 562949953421312L, 0L, 0L };
	public static final BitSet _tokenSet_61 = new BitSet(_tokenSet_61_data_);
	private static final long _tokenSet_62_data_[] = { 275960037376L, 0L };
	public static final BitSet _tokenSet_62 = new BitSet(_tokenSet_62_data_);
	private static final long _tokenSet_63_data_[] = { 4323456192031490304L, 0L };
	public static final BitSet _tokenSet_63 = new BitSet(_tokenSet_63_data_);
	private static final long _tokenSet_64_data_[] = { 3458765339536392960L, 0L };
	public static final BitSet _tokenSet_64 = new BitSet(_tokenSet_64_data_);
	
	}
