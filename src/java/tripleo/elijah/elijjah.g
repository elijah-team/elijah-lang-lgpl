program:
	("indexing" (IDENT TOK_COLON expressionList)*
	|"package xy=qualident {pc.packageName(xy);}
	|programStatement[pc])*
	EOF {out.FinishModule();}
	;
constantValue returns [IExpression e]
	: {e=null;}
	 s=STRING_LITERAL {e=new StringExpression(s);}
	|c=CHAR_LITERAL {e=new CharLitExpression(c);}
	|n=NUM_INT      {e=new NumericExpression(n);}
	|f=FLOATING     {e=new FloatExpression(f);}
	;
primitiveExpression  returns [IExpression e]
	: e=constantValue
	| e=variableReference
	| LBRACK        {e=new ListExpression();}
	    expressionList((ListExpression)e).contents());
	  RBRACK
	;