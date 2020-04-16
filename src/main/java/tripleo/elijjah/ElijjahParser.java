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
		
		ExpressionList el=null;	ParserClosure pc = out.closure();
		
		try {      // for error handling
			{
			_loop3:
			do {
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
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_alias:
				{
					programStatement(pc);
					break;
				}
				default:
				{
					break _loop3;
				}
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
				consumeUntil(_tokenSet_0);
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
			_loop6:
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
					break _loop6;
				}
				
			} while (true);
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
			_loop11:
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
					break _loop11;
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
		return q;
	}
	
	public final void programStatement(
		ProgramClosure pc
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_from:
			case LITERAL_import:
			{
				importStatement(pc.importStatement(out.module()));
				break;
			}
			case LITERAL_namespace:
			{
				namespaceStatement(pc.namespaceStatement(out.module()));
				break;
			}
			case LITERAL_class:
			{
				classStatement(pc.classStatement(out.module()));
				break;
			}
			case LITERAL_alias:
			{
				aliasStatement(pc.aliasStatement(out.module()));
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
				consumeUntil(_tokenSet_3);
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
			_loop89:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expr=expression();
					if ( inputState.guessing==0 ) {
						el.next(expr);
					}
				}
				else {
					break _loop89;
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
				consumeUntil(_tokenSet_2);
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
				consumeUntil(_tokenSet_0);
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
		VariableReference vr=new VariableReference();ProcedureCallExpression pcx;ee=null;
		
		try {      // for error handling
			r1 = LT(1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				vr.setMain(r1);
			}
			{
			switch ( LA(1)) {
			case DOT:
			{
				match(DOT);
				r2 = LT(1);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					vr.addIdentPart(r2);
				}
				break;
			}
			case LBRACK:
			{
				match(LBRACK);
				expr=expression();
				match(RBRACK);
				if ( inputState.guessing==0 ) {
					vr.addArrayPart(expr);
				}
				break;
			}
			case LPAREN:
			{
				pcx=procCallEx2();
				if ( inputState.guessing==0 ) {
					vr.addProcCallPart(pcx);
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
				ee=vr;
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_0);
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
			match(LITERAL_class);
			{
			switch ( LA(1)) {
			case LITERAL_interface:
			{
				match(LITERAL_interface);
				break;
			}
			case LITERAL_struct:
			{
				match(LITERAL_struct);
				break;
			}
			case LITERAL_signature:
			{
				match(LITERAL_signature);
				break;
			}
			case LITERAL_abstract:
			{
				match(LITERAL_abstract);
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
			classScope(cls);
			match(RCURLY);
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
	
	public final void classInheritance_(
		ClassInheritance ci
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			inhTypeName(ci.next());
			{
			_loop26:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					inhTypeName(ci.next());
				}
				else {
					break _loop26;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
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
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void classScope(
		ClassStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Token  x1 = null;
		Scope sc=null;ConstructorDef cd=null;DestructorDef dd=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop39:
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
					varStmt(cr.statementClosure());
					break;
				}
				case LITERAL_struct:
				case LITERAL_type:
				{
					typeAlias(cr.typeAlias());
					break;
				}
				case LITERAL_class:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_alias:
				{
					programStatement(cr.XXX());
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
					break _loop39;
				}
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
			else if ((LA(1)==LCURLY) && (_tokenSet_9.member(LA(2)))) {
				if ( inputState.guessing==0 ) {
					cls.setType(NamespaceTypes.MODULE);
				}
			}
			else if ((LA(1)==LCURLY) && (_tokenSet_9.member(LA(2)))) {
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
				consumeUntil(_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void namespaceScope(
		NamespaceStatement cr
	) throws RecognitionException, TokenStreamException {
		
		Scope sc=null;
		
		try {      // for error handling
			docstrings(cr);
			{
			_loop42:
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
					varStmt(cr.statementClosure());
					break;
				}
				case LITERAL_struct:
				case LITERAL_type:
				{
					typeAlias(cr.typeAlias());
					break;
				}
				case LITERAL_class:
				case LITERAL_namespace:
				case LITERAL_from:
				case LITERAL_import:
				case LITERAL_alias:
				{
					programStatement(cr.XXX());
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
					break _loop42;
				}
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
				_loop20:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						importPart(pc);
					}
					else {
						break _loop20;
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
				consumeUntil(_tokenSet_3);
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
			_loop85:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					qid=qualident();
					if ( inputState.guessing==0 ) {
						qal.add(qid);
					}
				}
				else {
					break _loop85;
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
				boolean synPredMatched23 = false;
				if (((LA(1)==IDENT) && (LA(2)==DOT||LA(2)==LCURLY))) {
					int _m23 = mark();
					synPredMatched23 = true;
					inputState.guessing++;
					try {
						{
						qualident();
						match(LCURLY);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched23 = false;
					}
					rewind(_m23);
					inputState.guessing--;
				}
				if ( synPredMatched23 ) {
					q3=qualident();
					match(LCURLY);
					if ( inputState.guessing==0 ) {
						il=cr.addSelectivePart(q3);
					}
					identList(il);
					match(RCURLY);
				}
				else if ((LA(1)==IDENT) && (_tokenSet_10.member(LA(2)))) {
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
					consumeUntil(_tokenSet_11);
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
			_loop69:
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
					break _loop69;
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
				consumeUntil(_tokenSet_12);
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
			boolean synPredMatched31 = false;
			if (((LA(1)==STRING_LITERAL) && (_tokenSet_13.member(LA(2))))) {
				int _m31 = mark();
				synPredMatched31 = true;
				inputState.guessing++;
				try {
					{
					match(STRING_LITERAL);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched31 = false;
				}
				rewind(_m31);
				inputState.guessing--;
			}
			if ( synPredMatched31 ) {
				{
				int _cnt33=0;
				_loop33:
				do {
					if ((LA(1)==STRING_LITERAL) && (_tokenSet_13.member(LA(2)))) {
						s1 = LT(1);
						match(STRING_LITERAL);
						if ( inputState.guessing==0 ) {
							sc.addDocString(s1);
						}
					}
					else {
						if ( _cnt33>=1 ) { break _loop33; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt33++;
				} while (true);
				}
			}
			else if ((_tokenSet_13.member(LA(1))) && (_tokenSet_14.member(LA(2)))) {
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
				consumeUntil(_tokenSet_13);
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
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void scope(
		Scope sc
	) throws RecognitionException, TokenStreamException {
		
		IExpression expr;
		
		try {      // for error handling
			match(LCURLY);
			docstrings(sc);
			{
			_loop49:
			do {
				if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
					statement(sc.statementClosure());
				}
				else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
					expr=expression();
					if ( inputState.guessing==0 ) {
						sc.statementWrapper(expr);
					}
				}
				else if ((LA(1)==LITERAL_class)) {
					classStatement(new ClassStatement(sc.getParent()));
				}
				else {
					break _loop49;
				}
				
			} while (true);
			}
			match(RCURLY);
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
			scope(fd.scope());
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_21);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void varStmt(
		StatementClosure cr
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
			_loop59:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					varStmt_i(vsq.next());
				}
				else {
					break _loop59;
				}
				
			} while (true);
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
				consumeUntil(_tokenSet_21);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void invariantStatement(
		InvariantStatement cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_invariant);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_21);
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
				consumeUntil(_tokenSet_21);
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
			if ((LA(1)==LBRACK) && (_tokenSet_23.member(LA(2)))) {
				match(LBRACK);
				typeName(rtn);
				if ( inputState.guessing==0 ) {
					tn.addGenericPart(rtn);
				}
				match(RBRACK);
			}
			else if ((_tokenSet_2.member(LA(1))) && (_tokenSet_24.member(LA(2)))) {
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
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void typeName(
		TypeName cr
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			if ((_tokenSet_25.member(LA(1))) && (_tokenSet_26.member(LA(2)))) {
				structTypeName(cr);
			}
			else if ((LA(1)==LITERAL_function||LA(1)==LITERAL_procedure)) {
				funcTypeExpr(cr);
			}
			else if ((LA(1)==IDENT) && (_tokenSet_2.member(LA(2)))) {
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
				consumeUntil(_tokenSet_2);
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
			{
				genericQualifiers(cr);
				{
				switch ( LA(1)) {
				case LITERAL_generic:
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
				consumeUntil(_tokenSet_2);
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
				if ((LA(1)==LPAREN) && (_tokenSet_23.member(LA(2)))) {
					match(LPAREN);
					typeNameList(pc.argList());
					match(RPAREN);
				}
				else if ((_tokenSet_27.member(LA(1))) && (_tokenSet_24.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
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
					typeName(pc.returnValue());
					break;
				}
				case EOF:
				case LITERAL_package:
				case LITERAL_indexing:
				case IDENT:
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
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				case LITERAL_type:
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
				case BOR:
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
				case LITERAL_this:
				case LITERAL_true:
				case LITERAL_false:
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
				break;
			}
			case LITERAL_procedure:
			{
				match(LITERAL_procedure);
				if ( inputState.guessing==0 ) {
						pc.type(TypeModifiers.PROCEDURE);	
				}
				{
				if ((LA(1)==LPAREN) && (_tokenSet_23.member(LA(2)))) {
					match(LPAREN);
					typeNameList(pc.argList());
					match(RPAREN);
				}
				else if ((_tokenSet_2.member(LA(1))) && (_tokenSet_24.member(LA(2)))) {
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
				consumeUntil(_tokenSet_2);
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
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void statement(
		StatementClosure cr
	) throws RecognitionException, TokenStreamException {
		
		Qualident q=null;FormalArgList o=null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENT:
			{
				procedureCallStatement(cr.procCallExpr());
				break;
			}
			case LITERAL_if:
			{
				ifConditional(cr.ifExpression());
				break;
			}
			case LITERAL_match:
			{
				matchConditional(cr.matchConditional());
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
				varStmt(cr);
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
				consumeUntil(_tokenSet_28);
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
				consumeUntil(_tokenSet_29);
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
			expr=expression();
			if ( inputState.guessing==0 ) {
				pc.setExpression(expr);
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
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case BECOMES:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_type:
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_type:
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_30);
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
			{
				formalArgListItem_priv(fal.next());
				{
				_loop208:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						formalArgListItem_priv(fal.next());
					}
					else {
						break _loop208;
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
				consumeUntil(_tokenSet_31);
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
				consumeUntil(_tokenSet_32);
			} else {
			  throw ex;
			}
		}
		return fal;
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
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void ifConditional(
		IfExpression ifex
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_if);
			expr=expression();
			if ( inputState.guessing==0 ) {
				ifex.expr(expr);
			}
			scope(ifex.scope());
			{
			_loop169:
			do {
				switch ( LA(1)) {
				case LITERAL_else:
				{
					match(LITERAL_else);
					scope(ifex.else_().scope());
					break;
				}
				case LITERAL_elseif:
				{
					elseif_part(ifex.elseif());
					break;
				}
				default:
				{
					break _loop169;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void matchConditional(
		MatchConditional mc
	) throws RecognitionException, TokenStreamException {
		
		Token  i1 = null;
		MatchConditional.MatchConditionalPart1 mcp1=null;
				 MatchConditional.MatchConditionalPart2 mcp2=null;
		
		try {      // for error handling
			match(LITERAL_match);
			expr=expression();
			if ( inputState.guessing==0 ) {
				mc.expr(expr);
			}
			match(LCURLY);
			{
			int _cnt172=0;
			_loop172:
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
				else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
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
					if ( _cnt172>=1 ) { break _loop172; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt172++;
			} while (true);
			}
			match(RCURLY);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_33);
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
				consumeUntil(_tokenSet_33);
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
				consumeUntil(_tokenSet_33);
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void opt_semi() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SEMI:
			{
				match(SEMI);
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_28);
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
			case LITERAL_namespace:
			case LITERAL_from:
			case LITERAL_import:
			case COMMA:
			case LITERAL_constructor:
			case LITERAL_ctor:
			case LITERAL_destructor:
			case LITERAL_dtor:
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_type:
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_29);
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
				consumeUntil(_tokenSet_0);
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
				consumeUntil(_tokenSet_34);
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
			_loop80:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					typeName(cr.next());
				}
				else {
					break _loop80;
				}
				
			} while (true);
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
				consumeUntil(_tokenSet_0);
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
				consumeUntil(_tokenSet_0);
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
			_loop92:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expr=expression();
					if ( inputState.guessing==0 ) {
						el.next(expr);
					}
				}
				else {
					break _loop92;
				}
				
			} while (true);
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
				consumeUntil(_tokenSet_0);
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
				consumeUntil(_tokenSet_36);
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
			_loop104:
			do {
				if ((LA(1)==LOR)) {
					match(LOR);
					e3=logicalAndExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LOR, e3);
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
				consumeUntil(_tokenSet_36);
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
			_loop107:
			do {
				if ((LA(1)==LAND)) {
					match(LAND);
					e3=inclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.LAND, e3);
					}
				}
				else {
					break _loop107;
				}
				
			} while (true);
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
	
	public final IExpression  inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;IExpression e3=null;
		
		try {      // for error handling
			ee=exclusiveOrExpression();
			{
			_loop110:
			do {
				if ((LA(1)==BOR)) {
					match(BOR);
					e3=exclusiveOrExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BOR, e3);
					}
				}
				else {
					break _loop110;
				}
				
			} while (true);
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
		return ee;
	}
	
	public final IExpression  exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=andExpression();
			{
			_loop113:
			do {
				if ((LA(1)==BXOR)) {
					match(BXOR);
					e3=andExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BXOR, e3);
					}
				}
				else {
					break _loop113;
				}
				
			} while (true);
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
		return ee;
	}
	
	public final IExpression  andExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=equalityExpression();
			{
			_loop116:
			do {
				if ((LA(1)==BAND)) {
					match(BAND);
					e3=equalityExpression();
					if ( inputState.guessing==0 ) {
						ee = ExpressionBuilder.build(ee, ExpressionKind.BAND, e3);
					}
				}
				else {
					break _loop116;
				}
				
			} while (true);
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
			_loop120:
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
					break _loop120;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_41);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  relationalExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				ExpressionKind e2;
				IExpression e3=null;
		
		try {      // for error handling
			ee=shiftExpression();
			{
			switch ( LA(1)) {
			case EOF:
			case LITERAL_package:
			case LITERAL_indexing:
			case IDENT:
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
			case LITERAL_const:
			case LITERAL_var:
			case LITERAL_val:
			case LITERAL_type:
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
			case BOR:
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				_loop125:
				do {
					if ((_tokenSet_42.member(LA(1)))) {
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
					}
					else {
						break _loop125;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_is_a:
			{
				match(LITERAL_is_a);
				typeName(null);
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
				consumeUntil(_tokenSet_43);
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
			_loop129:
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
					break _loop129;
				}
				
			} while (true);
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
	
	public final IExpression  additiveExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;ExpressionKind e2=null;
				IExpression e3=null;
		
		try {      // for error handling
			ee=multiplicativeExpression();
			{
			_loop133:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS) && (_tokenSet_18.member(LA(2)))) {
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
					break _loop133;
				}
				
			} while (true);
			}
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
		return ee;
	}
	
	public final IExpression  multiplicativeExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		ee=null;
				IExpression e3=null;ExpressionKind e2=null;
		
		try {      // for error handling
			ee=unaryExpression();
			{
			_loop137:
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
					break _loop137;
				}
				
			} while (true);
			}
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final IExpression  unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  lpb = null;
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_function:
			case LITERAL_procedure:
			{
				{
				if ((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double))) {
					lpb = LT(1);
					match(LPAREN);
					builtInTypeSpec(true);
					match(RPAREN);
					ee=unaryExpression();
				}
				else if ((_tokenSet_47.member(LA(1))) && (_tokenSet_2.member(LA(2)))) {
					ee=postfixExpression();
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
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_46);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void builtInTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		Token  lb = null;
		
		try {      // for error handling
			builtInType();
			{
			_loop163:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					match(LBRACK);
					match(RBRACK);
				}
				else {
					break _loop163;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				
							if ( addImagNode ) {
				//				#builtInTypeSpec = #(#[TYPE,"TYPE"], #builtInTypeSpec);
							}
						
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
	
	public final IExpression  postfixExpression() throws RecognitionException, TokenStreamException {
		IExpression ee;
		
		Token  e = null;
		Token  lp2 = null;
		Token  lbc = null;
		Token  lb = null;
		Token  lp = null;
		Token  in = null;
		Token  de = null;
		ee=null;
				IExpression e3=null;ExpressionList el=null;
		
		try {      // for error handling
			ee=primaryExpression();
			{
			_loop149:
			do {
				if ((LA(1)==DOT)) {
					match(DOT);
					{
					switch ( LA(1)) {
					case IDENT:
					{
						e = LT(1);
						match(IDENT);
						if ( inputState.guessing==0 ) {
							ee=new DotExpression(ee, new IdentExpression(e));
						}
						{
						if ((LA(1)==LPAREN) && (_tokenSet_48.member(LA(2)))) {
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
							case LITERAL_this:
							case LITERAL_true:
							case LITERAL_false:
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
						else if ((_tokenSet_2.member(LA(1))) && (_tokenSet_49.member(LA(2)))) {
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						break;
					}
					case LITERAL_this:
					{
						match(LITERAL_this);
						break;
					}
					case LITERAL_class:
					{
						match(LITERAL_class);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
				}
				else if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
					{
					int _cnt147=0;
					_loop147:
					do {
						if ((LA(1)==LBRACK)) {
							lbc = LT(1);
							match(LBRACK);
							match(RBRACK);
						}
						else {
							if ( _cnt147>=1 ) { break _loop147; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt147++;
					} while (true);
					}
					match(DOT);
					match(LITERAL_class);
				}
				else if ((LA(1)==LBRACK) && (_tokenSet_18.member(LA(2)))) {
					lb = LT(1);
					match(LBRACK);
					expr=expression();
					match(RBRACK);
				}
				else if ((LA(1)==LPAREN) && (_tokenSet_48.member(LA(2)))) {
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
					case LITERAL_this:
					case LITERAL_true:
					case LITERAL_false:
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
					break _loop149;
				}
				
			} while (true);
			}
			{
			if ((LA(1)==INC) && (_tokenSet_46.member(LA(2)))) {
				in = LT(1);
				match(INC);
			}
			else if ((LA(1)==DEC) && (_tokenSet_46.member(LA(2)))) {
				de = LT(1);
				match(DEC);
			}
			else if ((_tokenSet_46.member(LA(1))) && (_tokenSet_49.member(LA(2)))) {
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
				consumeUntil(_tokenSet_46);
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
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		return ee;
	}
	
	public final void funcExpr(
		FuncExpr pc
	) throws RecognitionException, TokenStreamException {
		
		Scope0 sc = new Scope0();
		
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
					typeName(pc.returnValue());
					break;
				}
				case EOF:
				case LITERAL_package:
				case LITERAL_indexing:
				case IDENT:
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
				case LITERAL_const:
				case LITERAL_var:
				case LITERAL_val:
				case LITERAL_type:
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
				case BOR:
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
				case LITERAL_this:
				case LITERAL_true:
				case LITERAL_false:
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
				case LITERAL_this:
				case LITERAL_true:
				case LITERAL_false:
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
				_loop160:
				do {
					if ((_tokenSet_16.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
						statement(sc.statementClosure());
					}
					else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
						expr=expression();
						if ( inputState.guessing==0 ) {
							sc.statementWrapper(expr);
						}
					}
					else if ((LA(1)==LITERAL_class)) {
						classStatement(new ClassStatement(sc.getParent()));
					}
					else {
						break _loop160;
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
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void builtInType() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_void:
			{
				match(LITERAL_void);
				break;
			}
			case LITERAL_boolean:
			{
				match(LITERAL_boolean);
				break;
			}
			case LITERAL_byte:
			{
				match(LITERAL_byte);
				break;
			}
			case LITERAL_char:
			{
				match(LITERAL_char);
				break;
			}
			case LITERAL_short:
			{
				match(LITERAL_short);
				break;
			}
			case LITERAL_int:
			{
				match(LITERAL_int);
				break;
			}
			case LITERAL_float:
			{
				match(LITERAL_float);
				break;
			}
			case LITERAL_long:
			{
				match(LITERAL_long);
				break;
			}
			case LITERAL_double:
			{
				match(LITERAL_double);
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
				consumeUntil(_tokenSet_50);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(IDENT);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_0);
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
			case LITERAL_this:
			case LITERAL_true:
			case LITERAL_false:
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
				consumeUntil(_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	public final void elseif_part(
		IfExpression ifex
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_elseif);
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
				consumeUntil(_tokenSet_51);
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
	
	public final void abstractGenericTypeName_xx(
		TypeName tn
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_generic);
			xy=qualident();
			if ( inputState.guessing==0 ) {
				tn.typeName(xy); tn.set(TypeModifiers.GENERIC);
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
				consumeUntil(_tokenSet_53);
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
				consumeUntil(_tokenSet_0);
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
			if ((_tokenSet_54.member(LA(1))) && (_tokenSet_55.member(LA(2)))) {
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
			else if ((LA(1)==LITERAL_generic) && (LA(2)==IDENT)) {
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
				consumeUntil(_tokenSet_53);
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
		"\"tokens\"",
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
		"\"const\"",
		"\"typeof\"",
		"\"immutable\"",
		"TOK_ARROW",
		"\"var\"",
		"\"val\"",
		"\"type\"",
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
		"BOR",
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
		"\"this\"",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"\"function\"",
		"\"procedure\"",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
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
		"\"def\"",
		"QUESTION",
		"LT",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long _tokenSet_0_data_[] = { 2L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { 4398281457762L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { -71987706331791646L, 4477228528173055L, 0L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { 216181095274971362L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { 4398285668578L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { 216207483564531426L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { 12582912L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 8388608L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { 16777216L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { 216181063062717056L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { 216181095543439586L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { 216181095543406818L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { 281018368L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { 216207483564531328L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	private static final long _tokenSet_14_data_[] = { -71987294013227038L, 9007199254740991L, 0L, 0L };
	public static final BitSet _tokenSet_14 = new BitSet(_tokenSet_14_data_);
	private static final long _tokenSet_15_data_[] = { 274886295552L, 0L };
	public static final BitSet _tokenSet_15 = new BitSet(_tokenSet_15_data_);
	private static final long _tokenSet_16_data_[] = { 28071906246784L, 1099511627776000L, 0L, 0L };
	public static final BitSet _tokenSet_16 = new BitSet(_tokenSet_16_data_);
	private static final long _tokenSet_17_data_[] = { 77635200L, 1125917071507456L, 0L, 0L };
	public static final BitSet _tokenSet_17 = new BitSet(_tokenSet_17_data_);
	private static final long _tokenSet_18_data_[] = { 10493568L, 17164664832L, 0L, 0L };
	public static final BitSet _tokenSet_18 = new BitSet(_tokenSet_18_data_);
	private static final long _tokenSet_19_data_[] = { -288202302607475072L, 1108307720798207L, 0L, 0L };
	public static final BitSet _tokenSet_19 = new BitSet(_tokenSet_19_data_);
	private static final long _tokenSet_20_data_[] = { -71987431453884446L, 8998420341587967L, 0L, 0L };
	public static final BitSet _tokenSet_20 = new BitSet(_tokenSet_20_data_);
	private static final long _tokenSet_21_data_[] = { 216181095274971264L, 0L };
	public static final BitSet _tokenSet_21 = new BitSet(_tokenSet_21_data_);
	private static final long _tokenSet_22_data_[] = { 216242667936620160L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_22 = new BitSet(_tokenSet_22_data_);
	private static final long _tokenSet_23_data_[] = { 54043298607661184L, 12884901888L, 0L, 0L };
	public static final BitSet _tokenSet_23 = new BitSet(_tokenSet_23_data_);
	private static final long _tokenSet_24_data_[] = { -4433230883192862L, 9007199254740991L, 0L, 0L };
	public static final BitSet _tokenSet_24 = new BitSet(_tokenSet_24_data_);
	private static final long _tokenSet_25_data_[] = { 54043298607661184L, 0L };
	public static final BitSet _tokenSet_25 = new BitSet(_tokenSet_25_data_);
	private static final long _tokenSet_26_data_[] = { -17944510803345694L, 4477228528173055L, 0L, 0L };
	public static final BitSet _tokenSet_26 = new BitSet(_tokenSet_26_data_);
	private static final long _tokenSet_27_data_[] = { -71987431453884446L, 4477228528173055L, 0L, 0L };
	public static final BitSet _tokenSet_27 = new BitSet(_tokenSet_27_data_);
	private static final long _tokenSet_28_data_[] = { 28071933582976L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_28 = new BitSet(_tokenSet_28_data_);
	private static final long _tokenSet_29_data_[] = { 216242668209266402L, 4477228512968704L, 0L, 0L };
	public static final BitSet _tokenSet_29 = new BitSet(_tokenSet_29_data_);
	private static final long _tokenSet_30_data_[] = { 216242668205055616L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_30 = new BitSet(_tokenSet_30_data_);
	private static final long _tokenSet_31_data_[] = { 4194304L, 128L, 0L, 0L };
	public static final BitSet _tokenSet_31 = new BitSet(_tokenSet_31_data_);
	private static final long _tokenSet_32_data_[] = { 63256842542720L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_32 = new BitSet(_tokenSet_32_data_);
	private static final long _tokenSet_33_data_[] = { 63256305671808L, 1099528792440832L, 0L, 0L };
	public static final BitSet _tokenSet_33 = new BitSet(_tokenSet_33_data_);
	private static final long _tokenSet_34_data_[] = { 128L, 0L };
	public static final BitSet _tokenSet_34 = new BitSet(_tokenSet_34_data_);
	private static final long _tokenSet_35_data_[] = { 4194304L, 0L };
	public static final BitSet _tokenSet_35 = new BitSet(_tokenSet_35_data_);
	private static final long _tokenSet_36_data_[] = { -71987707405574430L, 4477228512968735L, 0L, 0L };
	public static final BitSet _tokenSet_36 = new BitSet(_tokenSet_36_data_);
	private static final long _tokenSet_37_data_[] = { -71987707405574430L, 4477228512968767L, 0L, 0L };
	public static final BitSet _tokenSet_37 = new BitSet(_tokenSet_37_data_);
	private static final long _tokenSet_38_data_[] = { -71987707405574430L, 4477228512968831L, 0L, 0L };
	public static final BitSet _tokenSet_38 = new BitSet(_tokenSet_38_data_);
	private static final long _tokenSet_39_data_[] = { -71987707405574430L, 4477228512968959L, 0L, 0L };
	public static final BitSet _tokenSet_39 = new BitSet(_tokenSet_39_data_);
	private static final long _tokenSet_40_data_[] = { -71987707405574430L, 4477228512969215L, 0L, 0L };
	public static final BitSet _tokenSet_40 = new BitSet(_tokenSet_40_data_);
	private static final long _tokenSet_41_data_[] = { -71987707405574430L, 4477228512969727L, 0L, 0L };
	public static final BitSet _tokenSet_41 = new BitSet(_tokenSet_41_data_);
	private static final long _tokenSet_42_data_[] = { 1073741824L, 28672L, 0L, 0L };
	public static final BitSet _tokenSet_42 = new BitSet(_tokenSet_42_data_);
	private static final long _tokenSet_43_data_[] = { -71987707405574430L, 4477228512972799L, 0L, 0L };
	public static final BitSet _tokenSet_43 = new BitSet(_tokenSet_43_data_);
	private static final long _tokenSet_44_data_[] = { -71987706331832606L, 4477228513034239L, 0L, 0L };
	public static final BitSet _tokenSet_44 = new BitSet(_tokenSet_44_data_);
	private static final long _tokenSet_45_data_[] = { -71987706331832606L, 4477228513492991L, 0L, 0L };
	public static final BitSet _tokenSet_45 = new BitSet(_tokenSet_45_data_);
	private static final long _tokenSet_46_data_[] = { -71987706331832606L, 4477228528173055L, 0L, 0L };
	public static final BitSet _tokenSet_46 = new BitSet(_tokenSet_46_data_);
	private static final long _tokenSet_47_data_[] = { 10493568L, 16911433728L, 0L, 0L };
	public static final BitSet _tokenSet_47 = new BitSet(_tokenSet_47_data_);
	private static final long _tokenSet_48_data_[] = { 14687872L, 17164664832L, 0L, 0L };
	public static final BitSet _tokenSet_48 = new BitSet(_tokenSet_48_data_);
	private static final long _tokenSet_49_data_[] = { -17944029765304350L, 9007199254740991L, 0L, 0L };
	public static final BitSet _tokenSet_49 = new BitSet(_tokenSet_49_data_);
	private static final long _tokenSet_50_data_[] = { 4202496L, 0L };
	public static final BitSet _tokenSet_50 = new BitSet(_tokenSet_50_data_);
	private static final long _tokenSet_51_data_[] = { 63256305671808L, 5620720605855744L, 0L, 0L };
	public static final BitSet _tokenSet_51 = new BitSet(_tokenSet_51_data_);
	private static final long _tokenSet_52_data_[] = { 36028797018964096L, 0L };
	public static final BitSet _tokenSet_52 = new BitSet(_tokenSet_52_data_);
	private static final long _tokenSet_53_data_[] = { 272629760L, 128L, 0L, 0L };
	public static final BitSet _tokenSet_53 = new BitSet(_tokenSet_53_data_);
	private static final long _tokenSet_54_data_[] = { 67554028770295936L, 0L };
	public static final BitSet _tokenSet_54 = new BitSet(_tokenSet_54_data_);
	private static final long _tokenSet_55_data_[] = { 54043230160814464L, 128L, 0L, 0L };
	public static final BitSet _tokenSet_55 = new BitSet(_tokenSet_55_data_);
	
	}
