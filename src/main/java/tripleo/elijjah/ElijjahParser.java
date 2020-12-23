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
import tripleo.elijah.lang.builder.*;
import tripleo.elijah.contexts.*;
import tripleo.elijah.lang.imports.*;
import tripleo.elijah.lang2.*;
import tripleo.elijah.*;

public class ElijjahParser extends antlr.LLkParser
       implements ElijjahTokenTypes
 {

Qualident xy;
public Out out;
IExpression expr;
Context cur;

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
		
		ParserClosure pc = out.closure();cur=new ModuleContext(out.module());out.module().setContext((ModuleContext)cur);
		
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
						opt_semi();
						if ( inputState.guessing==0 ) {
							pc.packageName(xy);cur=new PackageContext(cur);
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
				out.module().postConstruct();out.FinishModule();
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
					el=expressionList2();
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
		
		Token  d1 = null;
		q=new Qualident();IdentExpression r1=null, r2=null;
		
		try {      // for error handling
			r1=ident();
			if ( inputState.guessing==0 ) {
				q.append(r1);
			}
			{
			_loop11:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					d1 = LT(1);
					match(DOT);
					r2=ident();
					if ( inputState.guessing==0 ) {
						q.appendDot(d1); q.append(r2);
					}
				}
				else {
					break _loop11;
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
	
	public final void opt_semi() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			if ((LA(1)==SEMI) && (_tokenSet_4.member(LA(2)))) {
				match(SEMI);
			}
			else if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2)))) {
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
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void programStatement(
		ProgramClosure pc, OS_Element cont
	) throws RecognitionException, TokenStreamException {
		
		ImportStatement imp=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			case LITERAL_import:
			{
				imp=importStatement(cont);
				break;
			}
			case LITERAL_alias:
			{
				aliasStatement(pc.aliasStatement(cont));
				break;
			}
			default:
				if ((LA(1)==ANNOT||LA(1)==LITERAL_namespace) && (LA(2)==IDENT||LA(2)==LCURLY)) {
					namespaceStatement(pc.namespaceStatement(cont));
				}
				else if ((LA(1)==LITERAL_class||LA(1)==ANNOT) && (_tokenSet_6.member(LA(2)))) {
					classStatement(cont, pc.classStatement(cont));
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
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
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
			_loop245:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expr=expression();
					if ( inputState.guessing==0 ) {
						el.next(expr);
					}
				}
				else {
					break _loop245;
				}
				
			} while (true);
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
		return el;
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return e;
	}
	
	public final IdentExpression  ident() throws RecognitionException, TokenStreamException {
		IdentExpression id;
		
		Token  r1 = null;
		id=null;
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				id=new IdentExpression(r1, cur);
			}
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
		return id;
	}
	
	public final void classStatement(
		OS_Element parent, ClassStatement cls
	) throws RecognitionException, TokenStreamException {
		
		AnnotationClause a=null;ClassContext ctx=null;IdentExpression i1=null;ClassBuilder cb=null;
		
		try {      // for error handling
			{
			_loop14:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						cls.addAnnotation(a);
					}
				}
				else {
					break _loop14;
				}
				
			} while (true);
			}
			{
			if ((LA(1)==LITERAL_class) && (_tokenSet_11.member(LA(2)))) {
				match(LITERAL_class);
				{
				switch ( LA(1)) {
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
				i1=ident();
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
				if ( inputState.guessing==0 ) {
					ctx=new ClassContext(cur, cls);cls.setContext(ctx);cur=ctx;
				}
				{
				switch ( LA(1)) {
				case IDENT:
				case STRING_LITERAL:
				case LITERAL_class:
				case RCURLY:
				case LITERAL_type:
				case ANNOT:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_constructor:
				case LITERAL_ctor:
				case LITERAL_destructor:
				case LITERAL_dtor:
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
				if ( inputState.guessing==0 ) {
					cls.postConstruct();cur=ctx.getParent();
				}
			}
			else if ((LA(1)==LITERAL_class) && (LA(2)==LITERAL_interface)) {
				if ( inputState.guessing==0 ) {
					cb = new ClassBuilder();cb.annotations(a);
				}
				classDefinition_interface(cb);
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
				consumeUntil(_tokenSet_12);
			} else {
			  throw ex;
			}
		}
	}
	
	public final AnnotationClause  annotation_clause() throws RecognitionException, TokenStreamException {
		AnnotationClause a;
		
		Qualident q=null;ExpressionList el=null;a=new AnnotationClause();AnnotationPart ap=null;
		
		try {      // for error handling
			match(ANNOT);
			{
			int _cnt57=0;
			_loop57:
			do {
				if ((LA(1)==IDENT)) {
					if ( inputState.guessing==0 ) {
						ap=new AnnotationPart();
					}
					q=qualident();
					if ( inputState.guessing==0 ) {
						ap.setClass(q);
					}
					{
					switch ( LA(1)) {
					case LPAREN:
					{
						match(LPAREN);
						el=expressionList2();
						match(RPAREN);
						if ( inputState.guessing==0 ) {
							ap.setExprs(el);
						}
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
					if ( inputState.guessing==0 ) {
						a.add(ap);
					}
				}
				else {
					if ( _cnt57>=1 ) { break _loop57; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt57++;
			} while (true);
			}
			match(RBRACK);
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
		return a;
	}
	
	public final void classInheritance_(
		ClassInheritance ci
	) throws RecognitionException, TokenStreamException {
		
		TypeName tn=null;
		
		try {      // for error handling
			tn=inhTypeName();
			if ( inputState.guessing==0 ) {
				ci.add(tn);
			}
			{
			_loop98:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					tn=inhTypeName();
					if ( inputState.guessing==0 ) {
						ci.add(tn);
					}
				}
				else {
					break _loop98;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_14);
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
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classScope(
		ClassStatement cr
	) throws RecognitionException, TokenStreamException {
		
		AccessNotation acs=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop46:
			do {
				switch ( LA(1)) {
				case LITERAL_constructor:
				case LITERAL_ctor:
				{
					constructorDef(cr);
					break;
				}
				case LITERAL_destructor:
				case LITERAL_dtor:
				{
					destructorDef(cr);
					break;
				}
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				{
					varStmt(cr.statementClosure(), cr);
					break;
				}
				case LITERAL_access:
				{
					acs=accessNotation();
					if ( inputState.guessing==0 ) {
						cr.addAccess(acs);
					}
					break;
				}
				default:
					if ((LA(1)==IDENT||LA(1)==ANNOT) && (_tokenSet_16.member(LA(2)))) {
						functionDef(cr.funcDef());
					}
					else if ((LA(1)==LITERAL_type) && (LA(2)==IDENT)) {
						match(LITERAL_type);
						match(IDENT);
						match(BECOMES);
						match(IDENT);
						{
						_loop45:
						do {
							if ((LA(1)==BOR)) {
								match(BOR);
								match(IDENT);
							}
							else {
								break _loop45;
							}
							
						} while (true);
						}
					}
					else if ((LA(1)==LITERAL_type) && (LA(2)==LITERAL_alias)) {
						typeAlias(cr.typeAlias());
					}
					else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))) {
						programStatement(cr.XXX(), cr);
					}
				else {
					break _loop46;
				}
				}
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_invariant:
			{
				invariantStatement(cr.invariantStatement());
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
	
	public final void invariantStatement(
		InvariantStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		InvariantStatementPart isp=null;
		
		try {      // for error handling
			match(LITERAL_invariant);
			{
			_loop249:
			do {
				if ((_tokenSet_20.member(LA(1)))) {
					if ( inputState.guessing==0 ) {
						isp = new InvariantStatementPart(cr, i1);
					}
					{
					if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
						i1 = LT(1);
						match(IDENT);
						match(TOK_COLON);
					}
					else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					expr=expression();
					if ( inputState.guessing==0 ) {
						isp.setExpr(expr);
					}
				}
				else {
					break _loop249;
				}
				
			} while (true);
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
	
	public final void classDefinition_interface(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		ClassStatement cls=null;IdentExpression i1=null;ClassContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_class);
			match(LITERAL_interface);
			if ( inputState.guessing==0 ) {
				cb.setType(ClassTypes.INTERFACE);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				cb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			case LT_:
			{
				classDefinition_inheritance(cb);
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
			classScope2(cb.getScope());
			}
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
	
	public final void classStatement2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		AnnotationClause a=null;ClassBuilder cb=null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				cb = new ClassBuilder();
			}
			{
			_loop23:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						cb.annotations(a);
					}
				}
				else {
					break _loop23;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				cb.annotations(a);
			}
			{
			if ((LA(1)==LITERAL_class) && (LA(2)==IDENT)) {
				classDefinition_normal(cb);
			}
			else if ((LA(1)==LITERAL_class) && (LA(2)==LITERAL_struct)) {
				classDefinition_struct(cb);
			}
			else if ((LA(1)==LITERAL_class) && (LA(2)==LITERAL_signature)) {
				classDefinition_signature(cb);
			}
			else if ((LA(1)==LITERAL_class) && (LA(2)==LITERAL_abstract)) {
				classDefinition_abstract(cb);
			}
			else if ((LA(1)==LITERAL_class) && (LA(2)==LITERAL_interface)) {
				classDefinition_interface(cb);
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
				consumeUntil(_tokenSet_22);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classDefinition_normal(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		ClassStatement cls=null;IdentExpression i1=null;ClassContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_class);
			if ( inputState.guessing==0 ) {
				cb.setType(ClassTypes.NORMAL);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				cb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			case LT_:
			{
				classDefinition_inheritance(cb);
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
			classScope2(cb.getScope());
			}
			match(RCURLY);
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
	
	public final void classDefinition_struct(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		ClassStatement cls=null;IdentExpression i1=null;ClassContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_class);
			match(LITERAL_struct);
			if ( inputState.guessing==0 ) {
				cb.setType(ClassTypes.STRUCTURE);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				cb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			case LT_:
			{
				classDefinition_inheritance(cb);
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
			classScope2(cb.getScope());
			}
			match(RCURLY);
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
	
	public final void classDefinition_signature(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		ClassStatement cls=null;IdentExpression i1=null;ClassContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_class);
			match(LITERAL_signature);
			if ( inputState.guessing==0 ) {
				cb.setType(ClassTypes.SIGNATURE);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				cb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			case LT_:
			{
				classDefinition_inheritance(cb);
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
			classScope2(cb.getScope());
			}
			match(RCURLY);
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
	
	public final void classDefinition_abstract(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		ClassStatement cls=null;IdentExpression i1=null;ClassContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_class);
			match(LITERAL_abstract);
			if ( inputState.guessing==0 ) {
				cb.setType(ClassTypes.ABSTRACT);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				cb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LPAREN:
			case LT_:
			{
				classDefinition_inheritance(cb);
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
			classScope2(cb.getScope());
			}
			match(RCURLY);
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
	
	public final void classDefinition_inheritance(
		ClassBuilder cb
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LPAREN:
			{
				{
				match(LPAREN);
				classInheritance_(cb.classInheritance());
				match(RPAREN);
				}
				break;
			}
			case LT_:
			{
				classInheritanceRuby(cb.classInheritance());
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
	
	public final void classScope2(
		ClassScope cr
	) throws RecognitionException, TokenStreamException {
		
		AccessNotation acs=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop52:
			do {
				switch ( LA(1)) {
				case LITERAL_constructor:
				case LITERAL_ctor:
				{
					constructorDef2(cr);
					break;
				}
				case LITERAL_destructor:
				case LITERAL_dtor:
				{
					destructorDef2(cr);
					break;
				}
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				{
					varStmt2(cr);
					break;
				}
				case LITERAL_access:
				{
					acs=accessNotation();
					if ( inputState.guessing==0 ) {
						cr.addAccess(acs);
					}
					break;
				}
				default:
					if ((LA(1)==IDENT||LA(1)==ANNOT) && (_tokenSet_16.member(LA(2)))) {
						functionDef2(cr.funcDef());
					}
					else if ((LA(1)==LITERAL_type) && (LA(2)==IDENT)) {
						match(LITERAL_type);
						match(IDENT);
						match(BECOMES);
						match(IDENT);
						{
						_loop51:
						do {
							if ((LA(1)==BOR)) {
								match(BOR);
								match(IDENT);
							}
							else {
								break _loop51;
							}
							
						} while (true);
						}
					}
					else if ((LA(1)==LITERAL_type) && (LA(2)==LITERAL_alias)) {
						typeAlias2(cr.typeAlias());
					}
					else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_23.member(LA(2)))) {
						programStatement2(cr);
					}
				else {
					break _loop52;
				}
				}
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_invariant:
			{
				invariantStatement2(cr);
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
			boolean synPredMatched103 = false;
			if (((LA(1)==STRING_LITERAL) && (_tokenSet_24.member(LA(2))))) {
				int _m103 = mark();
				synPredMatched103 = true;
				inputState.guessing++;
				try {
					{
					match(STRING_LITERAL);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched103 = false;
				}
				rewind(_m103);
				inputState.guessing--;
			}
			if ( synPredMatched103 ) {
				{
				int _cnt105=0;
				_loop105:
				do {
					if ((LA(1)==STRING_LITERAL) && (_tokenSet_24.member(LA(2)))) {
						s1 = LT(1);
						match(STRING_LITERAL);
						if ( inputState.guessing==0 ) {
							if (sc!=null) sc.addDocString(s1);
						}
					}
					else {
						if ( _cnt105>=1 ) { break _loop105; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt105++;
				} while (true);
				}
			}
			else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_5.member(LA(2)))) {
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
				consumeUntil(_tokenSet_24);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void constructorDef(
		ClassStatement cr
	) throws RecognitionException, TokenStreamException {
		
		ConstructorDef cd=null;IdentExpression x1=null;
		
		try {      // for error handling
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
				x1=ident();
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
			if ( inputState.guessing==0 ) {
				cd.postConstruct();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void destructorDef(
		ClassStatement cr
	) throws RecognitionException, TokenStreamException {
		
		DestructorDef dd=null;
		
		try {      // for error handling
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
			if ( inputState.guessing==0 ) {
				dd.postConstruct();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void functionDef(
		FunctionDef fd
	) throws RecognitionException, TokenStreamException {
		
		AnnotationClause a=null;FunctionContext ctx=null;IdentExpression i1=null;TypeName tn=null;
		
		try {      // for error handling
			{
			_loop192:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						fd.addAnnotation(a);
					}
				}
				else {
					break _loop192;
				}
				
			} while (true);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				fd.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					fd.set(FunctionModifiers.CONST);
				}
				break;
			}
			case LITERAL_immutable:
			{
				match(LITERAL_immutable);
				if ( inputState.guessing==0 ) {
					fd.set(FunctionModifiers.IMMUTABLE);
				}
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
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					fd.setReturnType(tn);
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
			if ( inputState.guessing==0 ) {
				ctx=new FunctionContext(cur, fd);fd.setContext(ctx);cur=ctx;
			}
			functionScope(fd.scope());
			if ( inputState.guessing==0 ) {
				fd.postConstruct();
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
	
	public final void varStmt(
		StatementClosure cr, OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		VariableSequence vsq=null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				vsq=cr.varSeq(cur);
			}
			{
			switch ( LA(1)) {
			case LITERAL_var:
			{
				match(LITERAL_var);
				break;
			}
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					vsq.defaultModifiers(TypeModifiers.CONST);
				}
				break;
			}
			case LITERAL_val:
			{
				match(LITERAL_val);
				if ( inputState.guessing==0 ) {
					vsq.defaultModifiers(TypeModifiers.VAL);
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
			_loop206:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					varStmt_i(vsq.next());
				}
				else {
					break _loop206;
				}
				
			} while (true);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_27);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void typeAlias(
		TypeAliasStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q=null;IdentExpression i=null;
		
		try {      // for error handling
			match(LITERAL_type);
			match(LITERAL_alias);
			i=ident();
			if ( inputState.guessing==0 ) {
				cr.setIdent(i);
			}
			match(BECOMES);
			q=qualident();
			if ( inputState.guessing==0 ) {
				cr.setBecomes(q);
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
	
	public final AccessNotation  accessNotation() throws RecognitionException, TokenStreamException {
		AccessNotation acs;
		
		Token  category = null;
		Token  shorthand = null;
		Token  category1 = null;
		Token  shorthand1 = null;
		TypeNameList tnl=null;acs=new AccessNotation();
		
		try {      // for error handling
			match(LITERAL_access);
			{
			if ((LA(1)==STRING_LITERAL) && (LA(2)==IDENT||LA(2)==LCURLY)) {
				category = LT(1);
				match(STRING_LITERAL);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					shorthand = LT(1);
					match(IDENT);
					match(EQUAL);
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
				tnl=typeNameList2();
				match(RCURLY);
				if ( inputState.guessing==0 ) {
					acs.setCategory(category);acs.setShortHand(shorthand);acs.setTypeNames(tnl);
				}
			}
			else if ((LA(1)==STRING_LITERAL) && (_tokenSet_26.member(LA(2)))) {
				category1 = LT(1);
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					acs.setCategory(category1);
				}
			}
			else if ((LA(1)==IDENT||LA(1)==LCURLY)) {
				{
				switch ( LA(1)) {
				case IDENT:
				{
					shorthand1 = LT(1);
					match(IDENT);
					match(EQUAL);
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
				tnl=typeNameList2();
				match(RCURLY);
				if ( inputState.guessing==0 ) {
					acs.setShortHand(shorthand1);acs.setTypeNames(tnl);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
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
		return acs;
	}
	
	public final void constructorDef2(
		ClassScope cr
	) throws RecognitionException, TokenStreamException {
		
		ConstructorDefBuilder cd=new ConstructorDefBuilder();IdentExpression x1=null;FormalArgList fal=null;
		
		try {      // for error handling
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
				x1=ident();
				if ( inputState.guessing==0 ) {
					cd.setName(x1);
				}
				break;
			}
			case LPAREN:
			{
				if ( inputState.guessing==0 ) {
					cd.setName(null);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			fal=opfal2();
			if ( inputState.guessing==0 ) {
				cd.fal(fal);
			}
			constructor_scope2(cd.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void destructorDef2(
		ClassScope cr
	) throws RecognitionException, TokenStreamException {
		
		DestructorDefBuilder dd=new DestructorDefBuilder();FormalArgList fal=null;
		
		try {      // for error handling
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
			fal=opfal2();
			if ( inputState.guessing==0 ) {
				dd.fal(fal);
			}
			scope2(dd.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void functionDef2(
		FunctionDefBuilder fb
	) throws RecognitionException, TokenStreamException {
		
		AnnotationClause a=null;/*FunctionContext ctx=null;*/IdentExpression i1=null;TypeName tn=null;FormalArgList fal=null;
		
		try {      // for error handling
			{
			_loop197:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						fb.addAnnotation(a);
					}
				}
				else {
					break _loop197;
				}
				
			} while (true);
			}
			i1=ident();
			if ( inputState.guessing==0 ) {
				fb.setName(i1);
			}
			{
			switch ( LA(1)) {
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					fb.set(FunctionModifiers.CONST);
				}
				break;
			}
			case LITERAL_immutable:
			{
				match(LITERAL_immutable);
				if ( inputState.guessing==0 ) {
					fb.set(FunctionModifiers.IMMUTABLE);
				}
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
			fal=opfal2();
			if ( inputState.guessing==0 ) {
				fb.fal(fal);
			}
			{
			switch ( LA(1)) {
			case TOK_ARROW:
			{
				match(TOK_ARROW);
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					fb.setReturnType(tn);
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
			functionScope2(fb.scope());
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
	
	public final void varStmt2(
		BaseScope cs
	) throws RecognitionException, TokenStreamException {
		
		VariableSequenceBuilder vsqb=new VariableSequenceBuilder();
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_var:
			{
				match(LITERAL_var);
				break;
			}
			case LITERAL_const:
			{
				match(LITERAL_const);
				if ( inputState.guessing==0 ) {
					vsqb.defaultModifiers(TypeModifiers.CONST);
				}
				break;
			}
			case LITERAL_val:
			{
				match(LITERAL_val);
				if ( inputState.guessing==0 ) {
					vsqb.defaultModifiers(TypeModifiers.VAL);
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
			varStmt_i2(vsqb);
			{
			_loop214:
			do {
				if ((LA(1)==COMMA)) {
					if ( inputState.guessing==0 ) {
						vsqb.next();
					}
					match(COMMA);
					varStmt_i2(vsqb);
				}
				else {
					break _loop214;
				}
				
			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				cs.add(vsqb);
			}
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
	
	public final void typeAlias2(
		TypeAliasBuilder tab
	) throws RecognitionException, TokenStreamException {
		
		Qualident q=null;IdentExpression i=null;
		
		try {      // for error handling
			match(LITERAL_type);
			match(LITERAL_alias);
			i=ident();
			if ( inputState.guessing==0 ) {
				tab.setIdent(i);
			}
			match(BECOMES);
			q=qualident();
			if ( inputState.guessing==0 ) {
				tab.setBecomes(q);
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
	
	public final void programStatement2(
		ClassOrNamespaceScope cont
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			case LITERAL_import:
			{
				importStatement2(cont);
				break;
			}
			case LITERAL_alias:
			{
				aliasStatement2(cont);
				break;
			}
			default:
				if ((LA(1)==ANNOT||LA(1)==LITERAL_namespace) && (LA(2)==IDENT||LA(2)==LCURLY)) {
					namespaceStatement2(cont);
				}
				else if ((LA(1)==LITERAL_class||LA(1)==ANNOT) && (_tokenSet_6.member(LA(2)))) {
					classStatement2(cont);
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
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void invariantStatement2(
		ClassScope sc
	) throws RecognitionException, TokenStreamException {
		
		InvariantStatementPart isp=null;IdentExpression i1=null;
		
		try {      // for error handling
			match(LITERAL_invariant);
			{
			_loop253:
			do {
				if ((_tokenSet_20.member(LA(1)))) {
					{
					if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
						i1=ident();
						match(TOK_COLON);
					}
					else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
						if ( inputState.guessing==0 ) {
							i1=null;
						}
					}
					else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					expr=expression();
					if ( inputState.guessing==0 ) {
						sc.addInvariantStatementPart(i1, expr);
					}
				}
				else {
					break _loop253;
				}
				
			} while (true);
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
	
	public final void namespaceStatement(
		NamespaceStatement cls
	) throws RecognitionException, TokenStreamException {
		
		AnnotationClause a=null;NamespaceContext ctx=null;IdentExpression i1=null;
		
		try {      // for error handling
			{
			_loop60:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						cls.addAnnotation(a);
					}
				}
				else {
					break _loop60;
				}
				
			} while (true);
			}
			match(LITERAL_namespace);
			{
			if ((LA(1)==IDENT)) {
				i1=ident();
				if ( inputState.guessing==0 ) {
					cls.setName(i1);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_28.member(LA(2)))) {
				if ( inputState.guessing==0 ) {
					cls.setType(NamespaceTypes.MODULE);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_28.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				ctx=new NamespaceContext(cur, cls);cls.setContext(ctx);cur=ctx;
			}
			namespaceScope(cls);
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				cls.postConstruct();cur=ctx.getParent();
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
	
	public final void namespaceScope(
		NamespaceStatement cr
	) throws RecognitionException, TokenStreamException {
		
		AccessNotation acs=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop119:
			do {
				if ((_tokenSet_29.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_const:
					case LITERAL_var:
					case LITERAL_val:
					{
						varStmt(cr.statementClosure(), cr);
						break;
					}
					case LITERAL_type:
					{
						typeAlias(cr.typeAlias());
						break;
					}
					case LITERAL_access:
					{
						acs=accessNotation();
						if ( inputState.guessing==0 ) {
							cr.addAccess(acs);
						}
						break;
					}
					default:
						if ((LA(1)==IDENT||LA(1)==ANNOT) && (_tokenSet_16.member(LA(2)))) {
							functionDef(cr.funcDef());
						}
						else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))) {
							programStatement(cr.XXX(), cr);
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop119;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_invariant:
			{
				invariantStatement(cr.invariantStatement());
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
	
	public final void namespaceStatement2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		NamespaceStatementBuilder cls = new NamespaceStatementBuilder();AnnotationClause a=null;NamespaceContext ctx=null;IdentExpression i1=null;
		
		try {      // for error handling
			{
			_loop64:
			do {
				if ((LA(1)==ANNOT)) {
					a=annotation_clause();
					if ( inputState.guessing==0 ) {
						cls.annotations(a);
					}
				}
				else {
					break _loop64;
				}
				
			} while (true);
			}
			match(LITERAL_namespace);
			{
			if ((LA(1)==IDENT)) {
				i1=ident();
				if ( inputState.guessing==0 ) {
					cls.setName(i1);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_28.member(LA(2)))) {
				if ( inputState.guessing==0 ) {
					cls.setType(NamespaceTypes.MODULE);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_28.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			match(LCURLY);
			namespaceScope2(cls.scope());
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				sc.add(cls);
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
	
	public final void namespaceScope2(
		NamespaceScope cr
	) throws RecognitionException, TokenStreamException {
		
		AccessNotation acs=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop124:
			do {
				if ((_tokenSet_29.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_const:
					case LITERAL_var:
					case LITERAL_val:
					{
						varStmt2(cr);
						break;
					}
					case LITERAL_type:
					{
						typeAlias2(cr.typeAlias());
						break;
					}
					case LITERAL_access:
					{
						acs=accessNotation();
						if ( inputState.guessing==0 ) {
							cr.addAccess(acs);
						}
						break;
					}
					default:
						if ((LA(1)==IDENT||LA(1)==ANNOT) && (_tokenSet_16.member(LA(2)))) {
							functionDef2(cr.funcDef());
						}
						else if ((_tokenSet_17.member(LA(1))) && (_tokenSet_30.member(LA(2)))) {
							programStatement2(cr);
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop124;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_invariant:
			{
				invariantStatement(cr.invariantStatement());
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
	
	public final ImportStatement  importStatement(
		OS_Element el
	) throws RecognitionException, TokenStreamException {
		ImportStatement pc;
		
		pc=null;ImportContext ctx=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				if ( inputState.guessing==0 ) {
					pc=new RootedImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;
				}
				xy=qualident();
				match(LITERAL_import);
				qualidentList(((RootedImportStatement)pc).importList());
				if ( inputState.guessing==0 ) {
					((RootedImportStatement)pc).importRoot(xy);
				}
				opt_semi();
				break;
			}
			case LITERAL_import:
			{
				match(LITERAL_import);
				{
				boolean synPredMatched69 = false;
				if (((LA(1)==IDENT) && (LA(2)==BECOMES))) {
					int _m69 = mark();
					synPredMatched69 = true;
					inputState.guessing++;
					try {
						{
						match(IDENT);
						match(BECOMES);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched69 = false;
					}
					rewind(_m69);
					inputState.guessing--;
				}
				if ( synPredMatched69 ) {
					if ( inputState.guessing==0 ) {
						pc=new AssigningImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;
					}
					importPart1((AssigningImportStatement)pc);
					{
					_loop71:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							importPart1((AssigningImportStatement)pc);
						}
						else {
							break _loop71;
						}
						
					} while (true);
					}
				}
				else {
					boolean synPredMatched73 = false;
					if (((LA(1)==IDENT) && (LA(2)==DOT||LA(2)==LCURLY))) {
						int _m73 = mark();
						synPredMatched73 = true;
						inputState.guessing++;
						try {
							{
							qualident();
							match(LCURLY);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched73 = false;
						}
						rewind(_m73);
						inputState.guessing--;
					}
					if ( synPredMatched73 ) {
						if ( inputState.guessing==0 ) {
							pc=new QualifiedImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;
						}
						importPart2((QualifiedImportStatement)pc);
						{
						_loop75:
						do {
							if ((LA(1)==COMMA)) {
								match(COMMA);
								importPart2((QualifiedImportStatement)pc);
							}
							else {
								break _loop75;
							}
							
						} while (true);
						}
					}
					else if ((LA(1)==IDENT) && (_tokenSet_31.member(LA(2)))) {
						if ( inputState.guessing==0 ) {
							pc=new NormalImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;
						}
						importPart3((NormalImportStatement)pc);
						{
						_loop77:
						do {
							if ((LA(1)==COMMA)) {
								match(COMMA);
								importPart3((NormalImportStatement)pc);
							}
							else {
								break _loop77;
							}
							
						} while (true);
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
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
					consumeUntil(_tokenSet_7);
				} else {
				  throw ex;
				}
			}
			return pc;
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
			_loop238:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					qid=qualident();
					if ( inputState.guessing==0 ) {
						qal.add(qid);
					}
				}
				else {
					break _loop238;
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
	
	public final void importPart1(
		AssigningImportStatement cr
	) throws RecognitionException, TokenStreamException {
		
		IdentExpression i1=null;Qualident q1=null;
		
		try {      // for error handling
			i1=ident();
			match(BECOMES);
			q1=qualident();
			if ( inputState.guessing==0 ) {
				cr.addAssigningPart(i1,q1);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_32);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importPart2(
		QualifiedImportStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q3;IdentList il=new IdentList();
		
		try {      // for error handling
			q3=qualident();
			match(LCURLY);
			il=identList2();
			if ( inputState.guessing==0 ) {
				cr.addSelectivePart(q3, il);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_32);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importPart3(
		NormalImportStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q2;
		
		try {      // for error handling
			q2=qualident();
			if ( inputState.guessing==0 ) {
				cr.addNormalPart(q2);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_32);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importStatement2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		ImportStatementBuilder ib=new ImportStatementBuilder(); ImportStatement pc=null; QualidentList qil=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				xy=qualident();
				match(LITERAL_import);
				qil=qualidentList2();
				if ( inputState.guessing==0 ) {
					ib.rooted(xy, qil);
				}
				opt_semi();
				break;
			}
			case LITERAL_import:
			{
				match(LITERAL_import);
				{
				boolean synPredMatched81 = false;
				if (((LA(1)==IDENT) && (LA(2)==BECOMES))) {
					int _m81 = mark();
					synPredMatched81 = true;
					inputState.guessing++;
					try {
						{
						match(IDENT);
						match(BECOMES);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched81 = false;
					}
					rewind(_m81);
					inputState.guessing--;
				}
				if ( synPredMatched81 ) {
					importPart1_(ib);
					{
					_loop83:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							importPart1_(ib);
						}
						else {
							break _loop83;
						}
						
					} while (true);
					}
				}
				else {
					boolean synPredMatched85 = false;
					if (((_tokenSet_26.member(LA(1))) && (_tokenSet_33.member(LA(2))))) {
						int _m85 = mark();
						synPredMatched85 = true;
						inputState.guessing++;
						try {
							{
							qualident();
							match(LCURLY);
							}
						}
						catch (RecognitionException pe) {
							synPredMatched85 = false;
						}
						rewind(_m85);
						inputState.guessing--;
					}
					if ( synPredMatched85 ) {
					}
					else if ((LA(1)==IDENT) && (LA(2)==DOT||LA(2)==LCURLY)) {
						importPart2_(ib);
						{
						_loop87:
						do {
							if ((LA(1)==COMMA)) {
								match(COMMA);
								importPart2_(ib);
							}
							else {
								break _loop87;
							}
							
						} while (true);
						}
					}
					else if ((LA(1)==IDENT) && (_tokenSet_34.member(LA(2)))) {
						importPart3_(ib);
						{
						_loop89:
						do {
							if ((LA(1)==COMMA)) {
								match(COMMA);
								importPart3_(ib);
							}
							else {
								break _loop89;
							}
							
						} while (true);
						}
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
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
					consumeUntil(_tokenSet_26);
				} else {
				  throw ex;
				}
			}
		}
		
	public final QualidentList  qualidentList2() throws RecognitionException, TokenStreamException {
		QualidentList qal;
		
		Qualident qid;qal=new QualidentList();
		
		try {      // for error handling
			qid=qualident();
			if ( inputState.guessing==0 ) {
				qal.add(qid);
			}
			{
			_loop241:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					qid=qualident();
					if ( inputState.guessing==0 ) {
						qal.add(qid);
					}
				}
				else {
					break _loop241;
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
		return qal;
	}
	
	public final void importPart1_(
		ImportStatementBuilder cr
	) throws RecognitionException, TokenStreamException {
		
		IdentExpression i1=null;Qualident q1=null;
		
		try {      // for error handling
			i1=ident();
			match(BECOMES);
			q1=qualident();
			if ( inputState.guessing==0 ) {
				cr.addAssigningPart(i1,q1);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_35);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importPart2_(
		ImportStatementBuilder cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q3;IdentList il=new IdentList();
		
		try {      // for error handling
			q3=qualident();
			match(LCURLY);
			il=identList2();
			if ( inputState.guessing==0 ) {
				cr.addSelectivePart(q3, il);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_35);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void importPart3_(
		ImportStatementBuilder cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q2;
		
		try {      // for error handling
			q2=qualident();
			if ( inputState.guessing==0 ) {
				cr.addNormalPart(q2);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_35);
			} else {
			  throw ex;
			}
		}
	}
	
	public final IdentList  identList2() throws RecognitionException, TokenStreamException {
		IdentList ail;
		
		IdentExpression s=null;ail=new IdentList();
		
		try {      // for error handling
			s=ident();
			if ( inputState.guessing==0 ) {
				ail.push(s);
			}
			{
			_loop232:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					s=ident();
					if ( inputState.guessing==0 ) {
						ail.push(s);
					}
				}
				else {
					break _loop232;
				}
				
			} while (true);
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
		return ail;
	}
	
	public final TypeName  inhTypeName() throws RecognitionException, TokenStreamException {
		TypeName tn;
		
		tn=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_typeof:
			{
				tn=typeOfTypeName2();
				break;
			}
			case IDENT:
			case LITERAL_const:
			case LITERAL_in:
			case LITERAL_out:
			case LITERAL_ref:
			{
				tn=normalTypeName2();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				tn.setContext(cur);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_36);
			} else {
			  throw ex;
			}
		}
		return tn;
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
				consumeUntil(_tokenSet_37);
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
			_loop133:
			do {
				if ((_tokenSet_38.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_class:
					case ANNOT:
					{
						classStatement(sc.getParent(), new ClassStatement(sc.getParent()));
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
						boolean synPredMatched131 = false;
						if (((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2))))) {
							int _m131 = mark();
							synPredMatched131 = true;
							inputState.guessing++;
							try {
								{
								expression();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched131 = false;
							}
							rewind(_m131);
							inputState.guessing--;
						}
						if ( synPredMatched131 ) {
							{
							expr=expression();
							}
						}
						else if ((_tokenSet_40.member(LA(1))) && (_tokenSet_41.member(LA(2)))) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						break;
					}
					case LITERAL_with:
					{
						withStatement(sc.getParent());
						break;
					}
					default:
						if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
							statement(sc.statementClosure(), sc.getParent());
						}
						else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2)))) {
							expr=expression();
							if ( inputState.guessing==0 ) {
								sc.statementWrapper(expr);
							}
						}
						else if ((LA(1)==LCURLY) && (_tokenSet_44.member(LA(2)))) {
							syntacticBlockScope(sc.getParent());
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop133;
				}
				
			} while (true);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_45);
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
				consumeUntil(_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		return fal;
	}
	
	public final void constructor_scope2(
		ConstructorDefScope sc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			_loop149:
			do {
				if ((_tokenSet_38.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_class:
					case ANNOT:
					{
						classStatement2(sc);
						break;
					}
					case LITERAL_continue:
					{
						match(LITERAL_continue);
						if ( inputState.guessing==0 ) {
							sc.continue_statement();
						}
						break;
					}
					case LITERAL_break:
					{
						match(LITERAL_break);
						if ( inputState.guessing==0 ) {
							sc.break_statement();
						}
						break;
					}
					case LITERAL_return:
					{
						match(LITERAL_return);
						{
						boolean synPredMatched147 = false;
						if (((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2))))) {
							int _m147 = mark();
							synPredMatched147 = true;
							inputState.guessing++;
							try {
								{
								expression();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched147 = false;
							}
							rewind(_m147);
							inputState.guessing--;
						}
						if ( synPredMatched147 ) {
							{
							expr=expression();
							}
							if ( inputState.guessing==0 ) {
								sc.return_expression(expr);
							}
						}
						else if ((_tokenSet_40.member(LA(1))) && (_tokenSet_47.member(LA(2)))) {
							if ( inputState.guessing==0 ) {
								sc.return_expression(null);
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						break;
					}
					case LITERAL_with:
					{
						withStatement2(sc);
						break;
					}
					default:
						if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
							statement2(sc);
						}
						else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2)))) {
							expr=expression();
							if ( inputState.guessing==0 ) {
								sc.statementWrapper(expr);
							}
						}
						else if ((LA(1)==LCURLY) && (_tokenSet_44.member(LA(2)))) {
							syntacticBlockScope2(sc);
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop149;
				}
				
			} while (true);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void scope2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			_loop141:
			do {
				if ((_tokenSet_38.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case LITERAL_class:
					case ANNOT:
					{
						classStatement2(sc);
						break;
					}
					case LITERAL_continue:
					{
						match(LITERAL_continue);
						if ( inputState.guessing==0 ) {
							sc.continue_statement();
						}
						break;
					}
					case LITERAL_break:
					{
						match(LITERAL_break);
						if ( inputState.guessing==0 ) {
							sc.break_statement();
						}
						break;
					}
					case LITERAL_return:
					{
						match(LITERAL_return);
						{
						boolean synPredMatched139 = false;
						if (((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2))))) {
							int _m139 = mark();
							synPredMatched139 = true;
							inputState.guessing++;
							try {
								{
								expression();
								}
							}
							catch (RecognitionException pe) {
								synPredMatched139 = false;
							}
							rewind(_m139);
							inputState.guessing--;
						}
						if ( synPredMatched139 ) {
							{
							expr=expression();
							}
							if ( inputState.guessing==0 ) {
								sc.return_expression(expr);
							}
						}
						else if ((_tokenSet_40.member(LA(1))) && (_tokenSet_48.member(LA(2)))) {
							if ( inputState.guessing==0 ) {
								sc.return_expression(null);
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						break;
					}
					case LITERAL_with:
					{
						withStatement2(sc);
						break;
					}
					default:
						if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
							statement2(sc);
						}
						else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_39.member(LA(2)))) {
							expr=expression();
							if ( inputState.guessing==0 ) {
								sc.statementWrapper(expr);
							}
						}
						else if ((LA(1)==LCURLY) && (_tokenSet_44.member(LA(2)))) {
							syntacticBlockScope2(sc);
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					opt_semi();
				}
				else {
					break _loop141;
				}
				
			} while (true);
			}
			match(RCURLY);
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
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				expr=assignmentExpression();
				if ( inputState.guessing==0 ) {
					cr.statementWrapper(expr);
				}
				break;
			}
			case LITERAL_if:
			{
				ifConditional(cr.ifConditional(aParent, cur));
				break;
			}
			case LITERAL_match:
			{
				matchConditional(cr.matchConditional(cur), aParent);
				break;
			}
			case LITERAL_case:
			{
				caseConditional(cr.caseConditional(cur));
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
				consumeUntil(_tokenSet_40);
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void withStatement(
		OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		WithStatement ws=new WithStatement(aParent);WithContext ctx=null;
		
		try {      // for error handling
			match(LITERAL_with);
			varStmt_i(ws.nextVarStmt());
			{
			match(COMMA);
			varStmt_i(ws.nextVarStmt());
			}
			if ( inputState.guessing==0 ) {
				ctx=new WithContext(ws, cur);ws.setContext(ctx);cur=ctx;
			}
			scope(ws.scope());
			if ( inputState.guessing==0 ) {
				ws.postConstruct();cur=cur.getParent();
			}
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
	}
	
	public final void syntacticBlockScope(
		OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		SyntacticBlock sb=new SyntacticBlock(aParent);SyntacticBlockContext ctx=null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				ctx=new SyntacticBlockContext(sb, cur);sb.setContext(ctx);cur=ctx;
			}
			scope(sb.scope());
			if ( inputState.guessing==0 ) {
				sb.postConstruct();cur=cur.getParent();
			}
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
	}
	
	public final void statement2(
		BaseScope cr
	) throws RecognitionException, TokenStreamException {
		
		
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
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				expr=assignmentExpression();
				if ( inputState.guessing==0 ) {
					cr.statementWrapper(expr);
				}
				break;
			}
			case LITERAL_if:
			{
				ifConditional2(cr);
				break;
			}
			case LITERAL_match:
			{
				matchConditional2(cr);
				break;
			}
			case LITERAL_case:
			{
				caseConditional2(cr);
				break;
			}
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			{
				varStmt2(cr);
				break;
			}
			case LITERAL_while:
			case LITERAL_do:
			{
				whileLoop2(cr);
				break;
			}
			case LITERAL_iterate:
			{
				frobeIteration2(cr);
				break;
			}
			case LITERAL_construct:
			{
				constructExpression(cr);
				break;
			}
			case LITERAL_yield:
			{
				yieldExpression(cr);
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
				consumeUntil(_tokenSet_50);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void withStatement2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		WithStatementBuilder ws=new WithStatementBuilder();VariableSequenceBuilder vsqb=ws.sb();
		
		try {      // for error handling
			match(LITERAL_with);
			varStmt_i2(vsqb);
			{
			if ( inputState.guessing==0 ) {
				vsqb.next();
			}
			match(COMMA);
			varStmt_i2(vsqb);
			}
			scope2(ws.scope());
			if ( inputState.guessing==0 ) {
				sc.add(ws);
			}
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
	}
	
	public final void syntacticBlockScope2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		SyntacticBlockBuilder sbb=new SyntacticBlockBuilder();
		
		try {      // for error handling
			scope2(sbb.scope());
			if ( inputState.guessing==0 ) {
				sc.add(sbb);
			}
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
	}
	
	public final void varStmt_i(
		VariableStatement vs
	) throws RecognitionException, TokenStreamException {
		
		TypeName tn=null;IdentExpression i=null;
		
		try {      // for error handling
			i=ident();
			if ( inputState.guessing==0 ) {
				vs.setName(i);
			}
			{
			switch ( LA(1)) {
			case TOK_COLON:
			{
				match(TOK_COLON);
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					vs.setTypeName(tn);
				}
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
			case LITERAL_type:
			case BECOMES:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_const:
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
			case LBRACK:
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
			case LPAREN:
			case LCURLY:
			case RCURLY:
			case LITERAL_type:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_const:
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
			case LBRACK:
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
				consumeUntil(_tokenSet_51);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void varStmt_i2(
		VariableSequenceBuilder vsb
	) throws RecognitionException, TokenStreamException {
		
		TypeName tn=null;IdentExpression i=null;
		
		try {      // for error handling
			i=ident();
			if ( inputState.guessing==0 ) {
				vsb.setName(i);
			}
			{
			switch ( LA(1)) {
			case TOK_COLON:
			{
				match(TOK_COLON);
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					vsb.setTypeName(tn);
				}
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
			case LITERAL_type:
			case BECOMES:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_post:
			case LITERAL_const:
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
			case LBRACK:
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
					vsb.setInitial(expr);
				}
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
			case LITERAL_type:
			case ANNOT:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_post:
			case LITERAL_const:
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
			case LBRACK:
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
				consumeUntil(_tokenSet_52);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void functionScope(
		Scope sc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			switch ( LA(1)) {
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
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
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
			case LBRACK:
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
				{
				_loop164:
				do {
					if ((_tokenSet_53.member(LA(1)))) {
						{
						switch ( LA(1)) {
						case LITERAL_class:
						case ANNOT:
						{
							classStatement(sc.getParent(), new ClassStatement(sc.getParent()));
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
							boolean synPredMatched162 = false;
							if (((_tokenSet_20.member(LA(1))) && (_tokenSet_54.member(LA(2))))) {
								int _m162 = mark();
								synPredMatched162 = true;
								inputState.guessing++;
								try {
									{
									expression();
									}
								}
								catch (RecognitionException pe) {
									synPredMatched162 = false;
								}
								rewind(_m162);
								inputState.guessing--;
							}
							if ( synPredMatched162 ) {
								{
								expr=expression();
								}
							}
							else if ((_tokenSet_55.member(LA(1))) && (_tokenSet_56.member(LA(2)))) {
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
							
							}
							break;
						}
						default:
							if ((_tokenSet_42.member(LA(1))) && (_tokenSet_57.member(LA(2)))) {
								statement(sc.statementClosure(), sc.getParent());
							}
							else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_54.member(LA(2)))) {
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
						break _loop164;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_abstract:
			{
				match(LITERAL_abstract);
				opt_semi();
				if ( inputState.guessing==0 ) {
					((FunctionDef)((FunctionDef.FunctionDefScope)sc).getParent()).setAbstract(true);
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
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void functionScope2(
		FunctionDefScope sc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			switch ( LA(1)) {
			case LITERAL_pre:
			{
				preConditionSegment(sc);
				break;
			}
			case IDENT:
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case LITERAL_class:
			case LITERAL_abstract:
			case LPAREN:
			case LCURLY:
			case RCURLY:
			case ANNOT:
			case LITERAL_return:
			case LITERAL_post:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
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
			case LITERAL_return:
			case LITERAL_post:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
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
				{
				_loop175:
				do {
					if ((_tokenSet_58.member(LA(1))) && (_tokenSet_59.member(LA(2)))) {
						{
						if ((_tokenSet_42.member(LA(1))) && (_tokenSet_60.member(LA(2)))) {
							statement2(sc);
						}
						else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_61.member(LA(2)))) {
							expr=expression();
							if ( inputState.guessing==0 ) {
								sc.statementWrapper(expr);
							}
						}
						else if ((LA(1)==LITERAL_class||LA(1)==ANNOT) && (_tokenSet_6.member(LA(2)))) {
							classStatement2(sc);
						}
						else if ((_tokenSet_58.member(LA(1))) && (_tokenSet_59.member(LA(2)))) {
							{
							if ((LA(1)==LITERAL_return) && (_tokenSet_20.member(LA(2)))) {
								match(LITERAL_return);
								{
								{
								expr=expression();
								}
								if ( inputState.guessing==0 ) {
									sc.return_expression(expr);
								}
								}
							}
							else if ((_tokenSet_58.member(LA(1))) && (_tokenSet_59.member(LA(2)))) {
								if ( inputState.guessing==0 ) {
									sc.return_expression(null);
								}
							}
							else {
								throw new NoViableAltException(LT(1), getFilename());
							}
							
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						opt_semi();
					}
					else {
						break _loop175;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_abstract:
			{
				match(LITERAL_abstract);
				opt_semi();
				if ( inputState.guessing==0 ) {
					sc.setAbstract();
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
			switch ( LA(1)) {
			case LITERAL_post:
			{
				postConditionSegment(sc);
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
	
	public final void preConditionSegment(
		FunctionDefScope sc
	) throws RecognitionException, TokenStreamException {
		
		Precondition p=null;
		
		try {      // for error handling
			match(LITERAL_pre);
			match(LCURLY);
			{
			_loop179:
			do {
				if ((_tokenSet_20.member(LA(1)))) {
					p=precondition();
					if ( inputState.guessing==0 ) {
						sc.addPreCondition(p);
					}
				}
				else {
					break _loop179;
				}
				
			} while (true);
			}
			match(RCURLY);
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
	
	public final void postConditionSegment(
		FunctionDefScope sc
	) throws RecognitionException, TokenStreamException {
		
		Postcondition po=null;
		
		try {      // for error handling
			match(LITERAL_post);
			{
			if ((LA(1)==LCURLY) && (_tokenSet_63.member(LA(2)))) {
				match(LCURLY);
				{
				_loop183:
				do {
					if ((_tokenSet_20.member(LA(1)))) {
						po=postcondition();
						if ( inputState.guessing==0 ) {
							sc.addPostCondition(po);
						}
					}
					else {
						break _loop183;
					}
					
				} while (true);
				}
				match(RCURLY);
			}
			else if ((_tokenSet_63.member(LA(1))) && (_tokenSet_64.member(LA(2)))) {
				{
				_loop185:
				do {
					if ((_tokenSet_20.member(LA(1)))) {
						po=postcondition();
						if ( inputState.guessing==0 ) {
							sc.addPostCondition(po);
						}
					}
					else {
						break _loop185;
					}
					
				} while (true);
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
				consumeUntil(_tokenSet_19);
			} else {
			  throw ex;
			}
		}
	}
	
	public final Precondition  precondition() throws RecognitionException, TokenStreamException {
		Precondition prec;
		
		prec=new Precondition();IdentExpression id=null;
		
		try {      // for error handling
			{
			if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
				id=ident();
				match(TOK_COLON);
				if ( inputState.guessing==0 ) {
					prec.id(id);
				}
			}
			else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			expr=expression();
			if ( inputState.guessing==0 ) {
				prec.expr(expr);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		return prec;
	}
	
	public final Postcondition  postcondition() throws RecognitionException, TokenStreamException {
		Postcondition postc;
		
		postc = new Postcondition();IdentExpression id=null;
		
		try {      // for error handling
			{
			if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
				id=ident();
				match(TOK_COLON);
				if ( inputState.guessing==0 ) {
					postc.id(id);
				}
			}
			else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			expr=expression();
			if ( inputState.guessing==0 ) {
				postc.expr(expr);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		return postc;
	}
	
	public final TypeName  typeName2() throws RecognitionException, TokenStreamException {
		TypeName cr;
		
		cr=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_generic:
			case QUESTION:
			{
				cr=genericTypeName2();
				break;
			}
			case LITERAL_typeof:
			{
				cr=typeOfTypeName2();
				break;
			}
			case IDENT:
			case LITERAL_const:
			case LITERAL_in:
			case LITERAL_out:
			case LITERAL_ref:
			{
				cr=normalTypeName2();
				break;
			}
			case LITERAL_function:
			case LITERAL_procedure:
			case LITERAL_func:
			case LITERAL_proc:
			{
				cr=functionTypeName2();
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return cr;
	}
	
	public final void aliasStatement(
		AliasStatement pc
	) throws RecognitionException, TokenStreamException {
		
		IdentExpression i1=null;
		
		try {      // for error handling
			match(LITERAL_alias);
			i1=ident();
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
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void aliasStatement2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		AliasStatementBuilder pc = new AliasStatementBuilder();IdentExpression i1=null;
		
		try {      // for error handling
			match(LITERAL_alias);
			i1=ident();
			if ( inputState.guessing==0 ) {
				pc.setName(i1);
			}
			match(BECOMES);
			xy=qualident();
			if ( inputState.guessing==0 ) {
				pc.setExpression(xy);
			}
			if ( inputState.guessing==0 ) {
				sc.add(pc);
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
			{
				formalArgListItem_priv(fal.next());
				{
				_loop393:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						formalArgListItem_priv(fal.next());
					}
					else {
						break _loop393;
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
				consumeUntil(_tokenSet_65);
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
			if ((_tokenSet_66.member(LA(1))) && (_tokenSet_20.member(LA(2)))) {
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
			}
			else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void ifConditional(
		IfConditional ifex
	) throws RecognitionException, TokenStreamException {
		
		IfConditionalContext ifc_top=null,ifc=null;IfConditional else_=null;
		
		try {      // for error handling
			match(LITERAL_if);
			expr=expression();
			if ( inputState.guessing==0 ) {
				ifex.expr(expr);cur=ifex.getContext();
			}
			scope(ifex.scope());
			if ( inputState.guessing==0 ) {
				cur=cur.getParent();
			}
			{
			_loop326:
			do {
				boolean synPredMatched325 = false;
				if (((LA(1)==LITERAL_else||LA(1)==LITERAL_elseif) && (_tokenSet_68.member(LA(2))))) {
					int _m325 = mark();
					synPredMatched325 = true;
					inputState.guessing++;
					try {
						{
						match(LITERAL_else);
						match(LITERAL_if);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched325 = false;
					}
					rewind(_m325);
					inputState.guessing--;
				}
				if ( synPredMatched325 ) {
					elseif_part(ifex.elseif());
				}
				else {
					break _loop326;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_else:
			{
				match(LITERAL_else);
				if ( inputState.guessing==0 ) {
					else_=ifex.else_();cur=else_.getContext();
				}
				scope(else_!=null?else_.scope():null);
				if ( inputState.guessing==0 ) {
					cur=cur.getParent();
				}
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
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
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
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void matchConditional(
		MatchConditional mc, OS_Element aParent
	) throws RecognitionException, TokenStreamException {
		
		MatchConditional.MatchConditionalPart1 mcp1=null;
				 MatchConditional.MatchConditionalPart2 mcp2=null;
				 MatchConditional.MatchConditionalPart3 mcp3=null;
				 TypeName tn=null;
				 IdentExpression i1=null;
				 MatchContext ctx = null;
		
		try {      // for error handling
			match(LITERAL_match);
			expr=expression();
			if ( inputState.guessing==0 ) {
				/*mc.setParent(aParent);*/mc.expr(expr);
			}
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				ctx=new MatchContext(cur, mc);mc.setContext(ctx);cur=ctx;
			}
			{
			int _cnt332=0;
			_loop332:
			do {
				if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
					if ( inputState.guessing==0 ) {
						mcp1 = mc.typeMatch();
					}
					i1=ident();
					if ( inputState.guessing==0 ) {
						mcp1.ident(i1);
					}
					match(TOK_COLON);
					tn=typeName2();
					if ( inputState.guessing==0 ) {
						mcp1.setTypeName(tn);
					}
					scope(mcp1.scope());
				}
				else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
					if ( inputState.guessing==0 ) {
						mcp2 = mc.normal();
					}
					expr=expression();
					if ( inputState.guessing==0 ) {
						mcp2.expr(expr);
					}
					scope(mcp2.scope());
				}
				else if ((LA(1)==LITERAL_val)) {
					if ( inputState.guessing==0 ) {
						mcp3 = mc.valNormal();
					}
					match(LITERAL_val);
					i1=ident();
					if ( inputState.guessing==0 ) {
						mcp3.expr(i1);
					}
					scope(mcp3.scope());
				}
				else {
					if ( _cnt332>=1 ) { break _loop332; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt332++;
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				mc.postConstruct();cur=ctx.getParent();
			}
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
	}
	
	public final void caseConditional(
		CaseConditional mc
	) throws RecognitionException, TokenStreamException {
		
		CaseContext ctx = null;
		
		try {      // for error handling
			match(LITERAL_case);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.expr(expr);
			}
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				ctx=new CaseContext(cur, mc);mc.setContext(ctx);cur=ctx;
			}
			{
			_loop335:
			do {
				if ((_tokenSet_20.member(LA(1)))) {
					expr=expression();
					scope(mc.scope(expr));
				}
				else {
					break _loop335;
				}
				
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				mc.postConstruct();cur=ctx.getParent();
			}
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
	}
	
	public final void whileLoop(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		Loop loop=cr.loop();LoopContext ctx;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_while:
			{
				match(LITERAL_while);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.WHILE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.expr(expr);
				}
				if ( inputState.guessing==0 ) {
					ctx=new LoopContext(cur, loop);loop.setContext((LoopContext)ctx);cur=ctx;
				}
				scope(loop.scope());
				break;
			}
			case LITERAL_do:
			{
				match(LITERAL_do);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.DO_WHILE);
				}
				if ( inputState.guessing==0 ) {
					ctx=new LoopContext(cur, loop);loop.setContext((LoopContext)ctx);cur=ctx;
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
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void frobeIteration(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		Loop loop=cr.loop();LoopContext ctx=null;IdentExpression i1=null, i2=null, i3=null;
		
		try {      // for error handling
			match(LITERAL_iterate);
			if ( inputState.guessing==0 ) {
				ctx=new LoopContext(cur, loop);loop.setContext(ctx);cur=ctx;
			}
			{
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.FROM_TO_TYPE);
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
					i1=ident();
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
					loop.type(LoopTypes.TO_TYPE);
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
					i2=ident();
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
			case LBRACK:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.EXPR_TYPE);
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
					i3=ident();
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
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void ifConditional2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		IfConditionalBuilder ifb = new IfConditionalBuilder();
		
		try {      // for error handling
			match(LITERAL_if);
			expr=expression();
			if ( inputState.guessing==0 ) {
				ifb.base_expr.expr(expr);
			}
			scope2(ifb.base_expr.scope());
			{
			_loop345:
			do {
				if ((LA(1)==LITERAL_else||LA(1)==LITERAL_elseif) && (_tokenSet_68.member(LA(2)))) {
					elseif_part2(ifb.new_expr());
				}
				else {
					break _loop345;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_else:
			{
				match(LITERAL_else);
				scope2(ifb.else_part.scope());
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
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_post:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LBRACK:
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
			if ( inputState.guessing==0 ) {
				sc.add(ifb);
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
	}
	
	public final void matchConditional2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		MatchConditionalBuilder mc = new MatchConditionalBuilder();
				 TypeName tn=null;
				 IdentExpression i1=null;
		
		try {      // for error handling
			match(LITERAL_match);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.expr(expr);
			}
			match(LCURLY);
			{
			int _cnt351=0;
			_loop351:
			do {
				if ((LA(1)==IDENT) && (LA(2)==TOK_COLON)) {
					i1=ident();
					match(TOK_COLON);
					tn=typeName2();
					scope2(mc.typeMatchscope(i1, tn));
				}
				else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
					expr=expression();
					scope2(mc.normalscope(expr));
				}
				else if ((LA(1)==LITERAL_val)) {
					match(LITERAL_val);
					i1=ident();
					scope2(mc.valNormalscope(i1));
				}
				else {
					if ( _cnt351>=1 ) { break _loop351; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt351++;
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				sc.add(mc);
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
	}
	
	public final void caseConditional2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		CaseConditionalBuilder mc = new CaseConditionalBuilder();
		
		try {      // for error handling
			match(LITERAL_case);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.expr(expr);
			}
			match(LCURLY);
			{
			_loop354:
			do {
				if ((_tokenSet_20.member(LA(1)))) {
					expr=expression();
					scope2(mc.scope(expr));
				}
				else {
					break _loop354;
				}
				
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				sc.add(mc);
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
	}
	
	public final void whileLoop2(
		BaseScope sc
	) throws RecognitionException, TokenStreamException {
		
		LoopBuilder loop=new LoopBuilder();LoopContext ctx;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_while:
			{
				match(LITERAL_while);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.WHILE);
				}
				expr=expression();
				if ( inputState.guessing==0 ) {
					loop.expr(expr);
				}
				scope2(loop.scope());
				break;
			}
			case LITERAL_do:
			{
				match(LITERAL_do);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.DO_WHILE);
				}
				scope2(loop.scope());
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
			if ( inputState.guessing==0 ) {
				sc.add(loop);
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
	}
	
	public final void frobeIteration2(
		BaseScope cr
	) throws RecognitionException, TokenStreamException {
		
		LoopBuilder loop=new LoopBuilder();/*LoopContext ctx=null;*/IdentExpression i1=null, i2=null, i3=null;
		
		try {      // for error handling
			match(LITERAL_iterate);
			{
			switch ( LA(1)) {
			case LITERAL_from:
			{
				match(LITERAL_from);
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.FROM_TO_TYPE);
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
					i1=ident();
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
					loop.type(LoopTypes.TO_TYPE);
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
					i2=ident();
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
			case LBRACK:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_this:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				if ( inputState.guessing==0 ) {
					loop.type(LoopTypes.EXPR_TYPE);
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
					i3=ident();
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
			scope2(loop.scope());
			if ( inputState.guessing==0 ) {
				cr.add(loop);
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
	}
	
	public final void constructExpression(
		BaseScope cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q=null;FormalArgList o=null;
		
		try {      // for error handling
			match(LITERAL_construct);
			q=qualident();
			o=opfal2();
			if ( inputState.guessing==0 ) {
				cr.constructExpression(q,o);
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
	}
	
	public final void yieldExpression(
		BaseScope cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_yield);
			expr=expression();
			if ( inputState.guessing==0 ) {
				cr.yield(expr);
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
	}
	
	public final TypeNameList  typeNameList2() throws RecognitionException, TokenStreamException {
		TypeNameList cr;
		
		TypeName tn=null;cr=new TypeNameList();
		
		try {      // for error handling
			tn=typeName2();
			if ( inputState.guessing==0 ) {
				cr.add(tn);
			}
			{
			_loop386:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					tn=typeName2();
				}
				else {
					break _loop386;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				cr.add(tn);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_69);
			} else {
			  throw ex;
			}
		}
		return cr;
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
				consumeUntil(_tokenSet_9);
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
			_loop264:
			do {
				if ((LA(1)==LOR) && (_tokenSet_20.member(LA(2)))) {
					match(LOR);
					e3=logicalAndExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LOR, e3);
					}
				}
				else {
					break _loop264;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  logicalAndExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e3=null;
		
		try {      // for error handling
			ee=inclusiveOrExpression();
			{
			_loop267:
			do {
				if ((LA(1)==LAND) && (_tokenSet_20.member(LA(2)))) {
					match(LAND);
					e3=inclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LAND, e3);
					}
				}
				else {
					break _loop267;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e3=null;
		
		try {      // for error handling
			ee=exclusiveOrExpression();
			{
			_loop270:
			do {
				if ((LA(1)==BOR) && (_tokenSet_20.member(LA(2)))) {
					match(BOR);
					e3=exclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BOR, e3);
					}
				}
				else {
					break _loop270;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=andExpression();
			{
			_loop273:
			do {
				if ((LA(1)==BXOR) && (_tokenSet_20.member(LA(2)))) {
					match(BXOR);
					e3=andExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BXOR, e3);
					}
				}
				else {
					break _loop273;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  andExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=equalityExpression();
			{
			_loop276:
			do {
				if ((LA(1)==BAND) && (_tokenSet_20.member(LA(2)))) {
					match(BAND);
					e3=equalityExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BAND, e3);
					}
				}
				else {
					break _loop276;
				}
				
			} while (true);
			}
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
			_loop280:
			do {
				if ((LA(1)==EQUAL||LA(1)==NOT_EQUAL) && (_tokenSet_20.member(LA(2)))) {
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
					break _loop280;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  relationalExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				ExpressionKind e2=null; // should never be null (below)
				IExpression e3=null;
				TypeName tn=null;
		
		try {      // for error handling
			ee=shiftExpression();
			{
			if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
				{
				_loop285:
				do {
					if ((_tokenSet_70.member(LA(1))) && (_tokenSet_20.member(LA(2)))) {
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
						break _loop285;
					}
					
				} while (true);
				}
			}
			else if ((LA(1)==LITERAL_is_a) && (_tokenSet_71.member(LA(2)))) {
				match(LITERAL_is_a);
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					ee=new TypeCheckExpression(ee, tn);
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
				consumeUntil(_tokenSet_9);
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
			_loop289:
			do {
				if (((LA(1) >= SL && LA(1) <= BSR)) && (_tokenSet_20.member(LA(2)))) {
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
					break _loop289;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  additiveExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;ExpressionKind e2=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=multiplicativeExpression();
			{
			_loop293:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_20.member(LA(2)))) {
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
					break _loop293;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  multiplicativeExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;ExpressionKind e2=null;
		
		try {      // for error handling
			ee=unaryExpression();
			{
			_loop297:
			do {
				if (((LA(1) >= STAR && LA(1) <= MOD)) && (_tokenSet_20.member(LA(2)))) {
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
					break _loop297;
				}
				
			} while (true);
			}
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
			case LBRACK:
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
				consumeUntil(_tokenSet_9);
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
			case LBRACK:
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  postfixExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  lb = null;
		Token  rb = null;
		Token  lp = null;
		Token  in = null;
		Token  de = null;
		ee=null;TypeCastExpression tc=null;TypeName tn=null;
				IExpression e3=null;ExpressionList el=null;
		
		try {      // for error handling
			ee=primaryExpression();
			{
			_loop305:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					match(DOT);
					{
					ee=dot_expression_or_procedure_call(ee);
					}
				}
				else if ((LA(1)==LBRACK) && (_tokenSet_20.member(LA(2)))) {
					lb = LT(1);
					match(LBRACK);
					expr=expression();
					rb = LT(1);
					match(RBRACK);
					if ( inputState.guessing==0 ) {
						ee=new GetItemExpression(ee, expr);((GetItemExpression)ee).parens(lb,rb);
					}
					{
					if ((LA(1)==BECOMES) && (_tokenSet_20.member(LA(2)))) {
						match(BECOMES);
						expr=expression();
						if ( inputState.guessing==0 ) {
							ee=new SetItemExpression((GetItemExpression)ee, expr);
						}
					}
					else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
				}
				else if ((LA(1)==LPAREN) && (_tokenSet_72.member(LA(2)))) {
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
					case LBRACK:
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
					break _loop305;
				}
				
			} while (true);
			}
			{
			if ((LA(1)==INC) && (_tokenSet_9.member(LA(2)))) {
				in = LT(1);
				match(INC);
			}
			else if ((LA(1)==DEC) && (_tokenSet_9.member(LA(2)))) {
				de = LT(1);
				match(DEC);
			}
			else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			if ((LA(1)==AS||LA(1)==CAST_TO) && (_tokenSet_71.member(LA(2)))) {
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
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					tc.setTypeName(tn);
				}
			}
			else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  primaryExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;FuncExpr ppc=null;IdentExpression e=null;
				ExpressionList el=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				ee=ident();
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
			case LBRACK:
			{
				match(LBRACK);
				if ( inputState.guessing==0 ) {
					ee=new ListExpression();el=new ExpressionList();
				}
				el=expressionList2();
				if ( inputState.guessing==0 ) {
					((ListExpression)ee).setContents(el);
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
				consumeUntil(_tokenSet_9);
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
		
		Token  lp2 = null;
		ee=null;ExpressionList el=null;IdentExpression e=null;
		
		try {      // for error handling
			e=ident();
			if ( inputState.guessing==0 ) {
				ee=new DotExpression(e1, e);
			}
			{
			if ((LA(1)==LPAREN) && (_tokenSet_72.member(LA(2)))) {
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
				case LBRACK:
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
			else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_67.member(LA(2)))) {
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void funcExpr(
		FuncExpr pc
	) throws RecognitionException, TokenStreamException {
		
		Scope0 sc = new Scope0(pc);TypeName tn=null;FuncExprContext ctx=null;
		
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
				if ( inputState.guessing==0 ) {
					ctx=new FuncExprContext(cur, pc);pc.setContext(ctx);cur=ctx;
				}
				scope(pc.scope());
				{
				switch ( LA(1)) {
				case TOK_COLON:
				case TOK_ARROW:
				{
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
					tn=typeName2();
					if ( inputState.guessing==0 ) {
						pc.setReturnType(tn);
					}
					break;
				}
				case EOF:
				case AS:
				case CAST_TO:
				case LITERAL_package:
				case LITERAL_indexing:
				case IDENT:
				case STRING_LITERAL:
				case CHAR_LITERAL:
				case NUM_INT:
				case NUM_FLOAT:
				case DOT:
				case LITERAL_class:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case LITERAL_type:
				case BECOMES:
				case BOR:
				case ANNOT:
				case RBRACK:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case COMMA:
				case LT_:
				case LITERAL_constructor:
				case LITERAL_ctor:
				case LITERAL_destructor:
				case LITERAL_dtor:
				case LITERAL_continue:
				case LITERAL_break:
				case LITERAL_return:
				case LITERAL_with:
				case LITERAL_post:
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				case LITERAL_alias:
				case LITERAL_construct:
				case LITERAL_yield:
				case SEMI:
				case LITERAL_invariant:
				case LITERAL_access:
				case EQUAL:
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
				case LBRACK:
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
			case LITERAL_procedure:
			{
				match(LITERAL_procedure);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.PROCEDURE);	
				}
				{
				opfal(pc.argList());
				}
				if ( inputState.guessing==0 ) {
					ctx=new FuncExprContext(cur, pc);pc.setContext(ctx);cur=ctx;
				}
				scope(pc.scope());
				break;
			}
			case LCURLY:
			{
				match(LCURLY);
				if ( inputState.guessing==0 ) {
					ctx=new FuncExprContext(cur, pc);pc.setContext(ctx);cur=ctx;
				}
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
				case LBRACK:
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
				_loop321:
				do {
					if ((_tokenSet_42.member(LA(1))) && (_tokenSet_73.member(LA(2)))) {
						statement(sc.statementClosure(), sc.getParent());
					}
					else if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2)))) {
						expr=expression();
						if ( inputState.guessing==0 ) {
							sc.statementWrapper(expr);
						}
					}
					else if ((LA(1)==LITERAL_class||LA(1)==ANNOT)) {
						classStatement(sc.getParent(), new ClassStatement(sc.getParent()));
					}
					else {
						break _loop321;
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
			if ( inputState.guessing==0 ) {
				pc.postConstruct();cur=cur.getParent();
			}
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
				ifex.expr(expr);cur=ifex.getContext();
			}
			scope(ifex.scope());
			if ( inputState.guessing==0 ) {
				cur=cur.getParent();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_74);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void elseif_part2(
		IfConditionalBuilder.Doublet ifex
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
			scope2(ifex.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_75);
			} else {
			  throw ex;
			}
		}
	}
	
	public final TypeOfTypeName  typeOfTypeName2() throws RecognitionException, TokenStreamException {
		TypeOfTypeName tn;
		
		tn=new TypeOfTypeName(cur);
		
		try {      // for error handling
			match(LITERAL_typeof);
			xy=qualident();
			if ( inputState.guessing==0 ) {
				tn.typeOf(xy); tn.set(TypeModifiers.TYPE_OF);
			}
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
		return tn;
	}
	
	public final NormalTypeName  normalTypeName2() throws RecognitionException, TokenStreamException {
		NormalTypeName tn;
		
		tn=new RegularTypeName(cur); TypeNameList rtn=null;
		
		try {      // for error handling
			regularQualifiers2(tn);
			xy=qualident();
			if ( inputState.guessing==0 ) {
				tn.setName(xy);
			}
			{
			if ((LA(1)==LBRACK) && (_tokenSet_71.member(LA(2)))) {
				match(LBRACK);
				rtn=typeNameList2();
				if ( inputState.guessing==0 ) {
					tn.addGenericPart(rtn);
				}
				match(RBRACK);
			}
			else if ((_tokenSet_3.member(LA(1))) && (_tokenSet_76.member(LA(2)))) {
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
			case STRING_LITERAL:
			case CHAR_LITERAL:
			case NUM_INT:
			case NUM_FLOAT:
			case DOT:
			case LITERAL_class:
			case LPAREN:
			case RPAREN:
			case LCURLY:
			case RCURLY:
			case LITERAL_type:
			case BECOMES:
			case BOR:
			case ANNOT:
			case RBRACK:
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LT_:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_continue:
			case LITERAL_break:
			case LITERAL_return:
			case LITERAL_with:
			case LITERAL_post:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_alias:
			case LITERAL_construct:
			case LITERAL_yield:
			case SEMI:
			case LITERAL_invariant:
			case LITERAL_access:
			case EQUAL:
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
			case LBRACK:
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return tn;
	}
	
	public final GenericTypeName  genericTypeName2() throws RecognitionException, TokenStreamException {
		GenericTypeName tn;
		
		tn=new GenericTypeName(cur);
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_generic:
			{
				match(LITERAL_generic);
				break;
			}
			case QUESTION:
			{
				match(QUESTION);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			xy=qualident();
			if ( inputState.guessing==0 ) {
				tn.typeName(xy); tn.set(TypeModifiers.GENERIC);
			}
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
		return tn;
	}
	
	public final FuncTypeName  functionTypeName2() throws RecognitionException, TokenStreamException {
		FuncTypeName tn;
		
		tn=new FuncTypeName(cur); TypeName rtn=null; TypeNameList tnl=new TypeNameList();
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_function:
			case LITERAL_func:
			{
				{
				switch ( LA(1)) {
				case LITERAL_function:
				{
					match(LITERAL_function);
					break;
				}
				case LITERAL_func:
				{
					match(LITERAL_func);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					tn.type(TypeModifiers.FUNCTION);
				}
				{
				match(LPAREN);
				tnl=typeNameList2();
				match(RPAREN);
				}
				if ( inputState.guessing==0 ) {
					tn.argList(tnl);
				}
				{
				switch ( LA(1)) {
				case TOK_COLON:
				case TOK_ARROW:
				{
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
					rtn=typeName2();
					if ( inputState.guessing==0 ) {
						tn.returnValue(rtn);
					}
					break;
				}
				case EOF:
				case AS:
				case CAST_TO:
				case LITERAL_package:
				case LITERAL_indexing:
				case IDENT:
				case STRING_LITERAL:
				case CHAR_LITERAL:
				case NUM_INT:
				case NUM_FLOAT:
				case DOT:
				case LITERAL_class:
				case LPAREN:
				case RPAREN:
				case LCURLY:
				case RCURLY:
				case LITERAL_type:
				case BECOMES:
				case BOR:
				case ANNOT:
				case RBRACK:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case COMMA:
				case LT_:
				case LITERAL_constructor:
				case LITERAL_ctor:
				case LITERAL_destructor:
				case LITERAL_dtor:
				case LITERAL_continue:
				case LITERAL_break:
				case LITERAL_return:
				case LITERAL_with:
				case LITERAL_post:
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				case LITERAL_alias:
				case LITERAL_construct:
				case LITERAL_yield:
				case SEMI:
				case LITERAL_invariant:
				case LITERAL_access:
				case EQUAL:
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
				case LBRACK:
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
			case LITERAL_procedure:
			case LITERAL_proc:
			{
				{
				switch ( LA(1)) {
				case LITERAL_procedure:
				{
					match(LITERAL_procedure);
					break;
				}
				case LITERAL_proc:
				{
					match(LITERAL_proc);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					tn.type(TypeModifiers.PROCEDURE);	
				}
				{
				match(LPAREN);
				tnl=typeNameList2();
				match(RPAREN);
				}
				if ( inputState.guessing==0 ) {
					tn.argList(tnl);
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
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		return tn;
	}
	
	public final void regularQualifiers2(
		NormalTypeName fp
	) throws RecognitionException, TokenStreamException {
		
		
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
			case IDENT:
			case LITERAL_const:
			case LITERAL_ref:
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
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_77);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void defFunctionDef(
		DefFunctionDef fd
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		FormalArgList op=null;TypeName tn=null;
		
		try {      // for error handling
			match(LITERAL_def);
			i1 = LT(1);
			match(IDENT);
			op=opfal2();
			{
			switch ( LA(1)) {
			case TOK_COLON:
			case TOK_ARROW:
			{
				{
				switch ( LA(1)) {
				case TOK_COLON:
				{
					match(TOK_COLON);
					break;
				}
				case TOK_ARROW:
				{
					match(TOK_ARROW);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					fd.setReturnType(tn);
				}
				break;
			}
			case BECOMES:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
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
		
		TypeName tn=null;IdentExpression i=null;
		
		try {      // for error handling
			{
			{
			if ((_tokenSet_78.member(LA(1))) && (_tokenSet_79.member(LA(2)))) {
				regularQualifiers2((NormalTypeName)fali.typeName());
			}
			else if ((LA(1)==IDENT) && (_tokenSet_80.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			i=ident();
			if ( inputState.guessing==0 ) {
					fali.setName(i);	
			}
			{
			switch ( LA(1)) {
			case TOK_COLON:
			{
				match(TOK_COLON);
				tn=typeName2();
				if ( inputState.guessing==0 ) {
					fali.setTypeName(tn);
				}
				break;
			}
			case RPAREN:
			case BOR:
			case COMMA:
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
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_81);
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
		"DOT",
		"\"class\"",
		"\"struct\"",
		"\"signature\"",
		"\"abstract\"",
		"LPAREN",
		"RPAREN",
		"LCURLY",
		"RCURLY",
		"\"interface\"",
		"\"type\"",
		"BECOMES",
		"BOR",
		"ANNOT",
		"RBRACK",
		"\"namespace\"",
		"\"from\"",
		"\"import\"",
		"COMMA",
		"LT_",
		"\"constructor\"",
		"\"ctor\"",
		"\"destructor\"",
		"\"dtor\"",
		"\"continue\"",
		"\"break\"",
		"\"return\"",
		"\"with\"",
		"\"pre\"",
		"\"post\"",
		"\"const\"",
		"\"immutable\"",
		"TOK_ARROW",
		"\"var\"",
		"\"val\"",
		"\"alias\"",
		"\"construct\"",
		"\"yield\"",
		"SEMI",
		"\"invariant\"",
		"\"access\"",
		"EQUAL",
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
		"LBRACK",
		"\"true\"",
		"\"false\"",
		"\"this\"",
		"\"null\"",
		"\"function\"",
		"\"procedure\"",
		"\"if\"",
		"\"else\"",
		"\"elseif\"",
		"\"match\"",
		"\"case\"",
		"\"while\"",
		"\"do\"",
		"\"iterate\"",
		"\"to\"",
		"\"generic\"",
		"QUESTION",
		"\"typeof\"",
		"\"func\"",
		"\"proc\"",
		"\"in\"",
		"\"out\"",
		"\"ref\"",
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
	
	private static final long _tokenSet_0_data_[] = { 562953845768384L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { 2L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { 5066553473138882L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { -109951171625486L, 3285649981439L, 0L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { 35918832592272834L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { -4398046511118L, 1099511627775L, 0L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { 8847616L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 32528213607481794L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { 5066553742623170L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { -109951171625486L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { -74766799536142L, 3285649981439L, 0L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { 459008L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { 35910036499250626L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { 671121664L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	private static final long _tokenSet_14_data_[] = { 3145728L, 0L };
	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	private static final long _tokenSet_15_data_[] = { 2097152L, 0L };
	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	private static final long _tokenSet_16_data_[] = { 52776558657792L, 0L };
	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	private static final long _tokenSet_17_data_[] = { 562953845768192L, 0L };
	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	private static final long _tokenSet_18_data_[] = { 10944768L, 0L };
	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	private static final long _tokenSet_19_data_[] = { 4194304L, 0L };
	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);
	private static final long _tokenSet_20_data_[] = { 2637056L, 2146533376L, 0L, 0L };
	public static final BitSet _tokenSet_20 = new BitSet(_tokenSet_20_data_);
	private static final long _tokenSet_21_data_[] = { -32211283815629520L, 536870911999L, 0L, 0L };
	public static final BitSet _tokenSet_21 = new BitSet(_tokenSet_21_data_);
	private static final long _tokenSet_22_data_[] = { 35918832592272640L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_22 = new BitSet(_tokenSet_22_data_);
	private static final long _tokenSet_23_data_[] = { 32528213618426112L, 0L };
	public static final BitSet _tokenSet_23 = new BitSet(_tokenSet_23_data_);
	private static final long _tokenSet_24_data_[] = { 35923230639045888L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_24 = new BitSet(_tokenSet_24_data_);
	private static final long _tokenSet_25_data_[] = { 28024613980111104L, 0L };
	public static final BitSet _tokenSet_25 = new BitSet(_tokenSet_25_data_);
	private static final long _tokenSet_26_data_[] = { 32528213607481600L, 0L };
	public static final BitSet _tokenSet_26 = new BitSet(_tokenSet_26_data_);
	private static final long _tokenSet_27_data_[] = { 35910036499250432L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_27 = new BitSet(_tokenSet_27_data_);
	private static final long _tokenSet_28_data_[] = { 28024356282074368L, 0L };
	public static final BitSet _tokenSet_28 = new BitSet(_tokenSet_28_data_);
	private static final long _tokenSet_29_data_[] = { 19017157023138048L, 0L };
	public static final BitSet _tokenSet_29 = new BitSet(_tokenSet_29_data_);
	private static final long _tokenSet_30_data_[] = { 32527955920388352L, 0L };
	public static final BitSet _tokenSet_30 = new BitSet(_tokenSet_30_data_);
	private static final long _tokenSet_31_data_[] = { 32528217902465474L, 0L };
	public static final BitSet _tokenSet_31 = new BitSet(_tokenSet_31_data_);
	private static final long _tokenSet_32_data_[] = { 32528217902449090L, 0L };
	public static final BitSet _tokenSet_32 = new BitSet(_tokenSet_32_data_);
	private static final long _tokenSet_33_data_[] = { 35954016973209026L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_33 = new BitSet(_tokenSet_33_data_);
	private static final long _tokenSet_34_data_[] = { 32528217902465280L, 0L };
	public static final BitSet _tokenSet_34 = new BitSet(_tokenSet_34_data_);
	private static final long _tokenSet_35_data_[] = { 32528217902448896L, 0L };
	public static final BitSet _tokenSet_35 = new BitSet(_tokenSet_35_data_);
	private static final long _tokenSet_36_data_[] = { 4298113024L, 0L };
	public static final BitSet _tokenSet_36 = new BitSet(_tokenSet_36_data_);
	private static final long _tokenSet_37_data_[] = { 70368746274816L, 0L };
	public static final BitSet _tokenSet_37 = new BitSet(_tokenSet_37_data_);
	private static final long _tokenSet_38_data_[] = { 3821627677129984L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_38 = new BitSet(_tokenSet_38_data_);
	private static final long _tokenSet_39_data_[] = { -27703561019654864L, 536870911999L, 0L, 0L };
	public static final BitSet _tokenSet_39 = new BitSet(_tokenSet_39_data_);
	private static final long _tokenSet_40_data_[] = { 8325227308694784L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_40 = new BitSet(_tokenSet_40_data_);
	private static final long _tokenSet_41_data_[] = { -39582418599950L, 1099511627775L, 0L, 0L };
	public static final BitSet _tokenSet_41 = new BitSet(_tokenSet_41_data_);
	private static final long _tokenSet_42_data_[] = { 3817504374275328L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_42 = new BitSet(_tokenSet_42_data_);
	private static final long _tokenSet_43_data_[] = { -27703559945913040L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_43 = new BitSet(_tokenSet_43_data_);
	private static final long _tokenSet_44_data_[] = { 3821627681324288L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_44 = new BitSet(_tokenSet_44_data_);
	private static final long _tokenSet_45_data_[] = { -39582427447310L, 1099511627775L, 0L, 0L };
	public static final BitSet _tokenSet_45 = new BitSet(_tokenSet_45_data_);
	private static final long _tokenSet_46_data_[] = { 8404392179449600L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_46 = new BitSet(_tokenSet_46_data_);
	private static final long _tokenSet_47_data_[] = { -118751820251856L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_47 = new BitSet(_tokenSet_47_data_);
	private static final long _tokenSet_48_data_[] = { -109955727229648L, 1099511627775L, 0L, 0L };
	public static final BitSet _tokenSet_48 = new BitSet(_tokenSet_48_data_);
	private static final long _tokenSet_49_data_[] = { 35918832592272640L, 549754863616L, 0L, 0L };
	public static final BitSet _tokenSet_49 = new BitSet(_tokenSet_49_data_);
	private static final long _tokenSet_50_data_[] = { 8334023401716992L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_50 = new BitSet(_tokenSet_50_data_);
	private static final long _tokenSet_51_data_[] = { 35910040794217728L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_51 = new BitSet(_tokenSet_51_data_);
	private static final long _tokenSet_52_data_[] = { 35918836887239936L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_52 = new BitSet(_tokenSet_52_data_);
	private static final long _tokenSet_53_data_[] = { 3819428653874432L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_53 = new BitSet(_tokenSet_53_data_);
	private static final long _tokenSet_54_data_[] = { -27705760042910416L, 536870911999L, 0L, 0L };
	public static final BitSet _tokenSet_54 = new BitSet(_tokenSet_54_data_);
	private static final long _tokenSet_55_data_[] = { 8323028285439232L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_55 = new BitSet(_tokenSet_55_data_);
	private static final long _tokenSet_56_data_[] = { -120950843507408L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_56 = new BitSet(_tokenSet_56_data_);
	private static final long _tokenSet_57_data_[] = { -27705758969168592L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_57 = new BitSet(_tokenSet_57_data_);
	private static final long _tokenSet_58_data_[] = { 8330999744740608L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_58 = new BitSet(_tokenSet_58_data_);
	private static final long _tokenSet_59_data_[] = { -112979384206032L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_59 = new BitSet(_tokenSet_59_data_);
	private static final long _tokenSet_60_data_[] = { -27697787509867216L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_60 = new BitSet(_tokenSet_60_data_);
	private static final long _tokenSet_61_data_[] = { -27697788583609040L, 536870911999L, 0L, 0L };
	public static final BitSet _tokenSet_61 = new BitSet(_tokenSet_61_data_);
	private static final long _tokenSet_62_data_[] = { 8330999745002752L, 536869961728L, 0L, 0L };
	public static final BitSet _tokenSet_62 = new BitSet(_tokenSet_62_data_);
	private static final long _tokenSet_63_data_[] = { 6831360L, 2146533376L, 0L, 0L };
	public static final BitSet _tokenSet_63 = new BitSet(_tokenSet_63_data_);
	private static final long _tokenSet_64_data_[] = { -122874997702864L, 536870911999L, 0L, 0L };
	public static final BitSet _tokenSet_64 = new BitSet(_tokenSet_64_data_);
	private static final long _tokenSet_65_data_[] = { 68157440L, 0L };
	public static final BitSet _tokenSet_65 = new BitSet(_tokenSet_65_data_);
	private static final long _tokenSet_66_data_[] = { -72057594004373504L, 7L, 0L, 0L };
	public static final BitSet _tokenSet_66 = new BitSet(_tokenSet_66_data_);
	private static final long _tokenSet_67_data_[] = { -4398046511118L, 281474976710655L, 0L, 0L };
	public static final BitSet _tokenSet_67 = new BitSet(_tokenSet_67_data_);
	private static final long _tokenSet_68_data_[] = { 2637056L, 4294017024L, 0L, 0L };
	public static final BitSet _tokenSet_68 = new BitSet(_tokenSet_68_data_);
	private static final long _tokenSet_69_data_[] = { 273678336L, 0L };
	public static final BitSet _tokenSet_69 = new BitSet(_tokenSet_69_data_);
	private static final long _tokenSet_70_data_[] = { 8589934592L, 1792L, 0L, 0L };
	public static final BitSet _tokenSet_70 = new BitSet(_tokenSet_70_data_);
	private static final long _tokenSet_71_data_[] = { 17592186044672L, 280377075695616L, 0L, 0L };
	public static final BitSet _tokenSet_71 = new BitSet(_tokenSet_71_data_);
	private static final long _tokenSet_72_data_[] = { 3685632L, 2146533376L, 0L, 0L };
	public static final BitSet _tokenSet_72 = new BitSet(_tokenSet_72_data_);
	private static final long _tokenSet_73_data_[] = { -27707683114517200L, 1086626725887L, 0L, 0L };
	public static final BitSet _tokenSet_73 = new BitSet(_tokenSet_73_data_);
	private static final long _tokenSet_74_data_[] = { 8325227308694784L, 549754863616L, 0L, 0L };
	public static final BitSet _tokenSet_74 = new BitSet(_tokenSet_74_data_);
	private static final long _tokenSet_75_data_[] = { 8334023401716992L, 549754863616L, 0L, 0L };
	public static final BitSet _tokenSet_75 = new BitSet(_tokenSet_75_data_);
	private static final long _tokenSet_76_data_[] = { -14L, 281474976710655L, 0L, 0L };
	public static final BitSet _tokenSet_76 = new BitSet(_tokenSet_76_data_);
	private static final long _tokenSet_77_data_[] = { 256L, 0L };
	public static final BitSet _tokenSet_77 = new BitSet(_tokenSet_77_data_);
	private static final long _tokenSet_78_data_[] = { 17592186044672L, 246290604621824L, 0L, 0L };
	public static final BitSet _tokenSet_78 = new BitSet(_tokenSet_78_data_);
	private static final long _tokenSet_79_data_[] = { 17596549169920L, 140737488355328L, 0L, 0L };
	public static final BitSet _tokenSet_79 = new BitSet(_tokenSet_79_data_);
	private static final long _tokenSet_80_data_[] = { 4363125248L, 0L };
	public static final BitSet _tokenSet_80 = new BitSet(_tokenSet_80_data_);
	private static final long _tokenSet_81_data_[] = { 4363124736L, 0L };
	public static final BitSet _tokenSet_81 = new BitSet(_tokenSet_81_data_);
	
	}
