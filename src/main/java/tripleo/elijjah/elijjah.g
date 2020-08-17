header {
  package tripleo.elijjah;
}

{
import tripleo.elijah.lang.*;
import tripleo.elijah.contexts.*;
import tripleo.elijah.lang.imports.*;
import tripleo.elijah.lang2.*;
import tripleo.elijah.*;
}

class ElijjahParser extends Parser;
options {
	exportVocab=Elijjah;
//	defaultErrorHandler=false;
	k=2;
//	buildAST = true;
}

tokens {
    //"tokens"; 
    AS="as"; CAST_TO="cast_to";
}

{
Qualident xy;
public Out out;
IExpression expr;
Context cur;
}

program
        {ParserClosure pc = out.closure();out.module().setContext(new ModuleContext(out.module()));}
    : (( indexingStatement[pc.indexingStatement()]
	  |"package" xy=qualident opt_semi {pc.packageName(xy);cur=new PackageContext(cur);}
	  |programStatement[pc, out.module()]) opt_semi)*
	  EOF {out.module().postConstruct();out.FinishModule();}
	;
indexingStatement[IndexingStatement idx]
		{ExpressionList el=null;}
	: "indexing" 
		(i1:IDENT 			{idx.setName(i1);}
		 TOK_COLON 			{el=new ExpressionList();} 
		 expressionList[el]	{idx.setExprs(el);})*
	;
constantValue returns [IExpression e]
	 {e=null;}
	:s:STRING_LITERAL {e=new StringExpression(s);}
	|c:CHAR_LITERAL   {e=new CharLitExpression(c);}
	|n:NUM_INT        {e=new NumericExpression(n);}
	|f:NUM_FLOAT      {e=new FloatExpression(f);}
	;
primitiveExpression  returns [IExpression e]
		{e=null;ExpressionList el=null;}
	: e=constantValue
	| e=variableReference
	| LBRACK        {e=new ListExpression();el=new ExpressionList();}
	    expressionList[el]  {((ListExpression)e).setContents(el);}
	  RBRACK
	;
qualident returns [Qualident q]
    {q=new Qualident();}
	:
     r1:IDENT /*ident2 */ {q.append(r1);}
      (d1:DOT r2:IDENT {q.appendDot(d1); q.append(r2);})*
    ;
classStatement [ClassStatement cls]
		{AnnotationClause a=null;ClassContext ctx=null;IdentExpression i1=null;}
	: (a=annotation_clause  {cls.addAnnotation(a);})*
    "class"                 {ctx=new ClassContext(cur, cls);cls.setContext(ctx);cur=ctx;}
            ("interface"    {cls.setType(ClassTypes.INTERFACE);}
            |"struct"       {cls.setType(ClassTypes.STRUCTURE);}
            |"signature"    {cls.setType(ClassTypes.SIGNATURE);}
            |"abstract"     {cls.setType(ClassTypes.ABSTRACT);})?
      i1=ident {cls.setName(i1);}
    ((LPAREN classInheritance_ [cls.classInheritance()] RPAREN)
    | classInheritanceRuby [cls.classInheritance()] )?
    LCURLY
     (classScope[cls]
     |"abstract"         {cls.setType(ClassTypes.ABSTRACT);}
      (invariantStatement[cls.invariantStatement()])?
     )
    RCURLY {cls.postConstruct();cur=ctx.getParent();}
    ;
annotation_clause returns [AnnotationClause a]
		{Qualident q=null;ExpressionList el=null;a=new AnnotationClause();AnnotationPart ap=null;}
	: ANNOT
		(                                       {ap=new AnnotationPart();}
		 q=qualident                            {ap.setClass(q);}
			(LPAREN el=expressionList2 RPAREN   {ap.setExprs(el);}
			)?                                  {a.add(ap);}
		)+ RBRACK
	;
namespaceStatement [NamespaceStatement cls]
		{AnnotationClause a=null;NamespaceContext ctx=null;IdentExpression i1=null;}
	: (a=annotation_clause      {cls.addAnnotation(a);})*
    "namespace"                 {ctx=new NamespaceContext(cur, cls);cls.setContext(ctx);cur=ctx;}
    (  i1=ident  	            {cls.setName(i1);}
    | 				            {cls.setType(NamespaceTypes.MODULE);}
    )?
    LCURLY
     namespaceScope[cls]
    RCURLY {cls.postConstruct();cur=ctx.getParent();}
    ;
importStatement[OS_Element el] returns [ImportStatement pc]
	 {pc=null;ImportContext ctx=null;}
    : "from" {pc=new RootedImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;}
        xy=qualident "import" qualidentList[((RootedImportStatement)pc).importList()] {((RootedImportStatement)pc).importRoot(xy);} opt_semi
    | "import"
        ( (IDENT BECOMES) =>
                {pc=new AssigningImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;}
            importPart1[(AssigningImportStatement)pc] (COMMA importPart1[(AssigningImportStatement)pc])*
        | (qualident /*DOT*/ LCURLY) =>
                {pc=new QualifiedImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;}
            importPart2[(QualifiedImportStatement)pc] (COMMA importPart2[(QualifiedImportStatement)pc])*
        |       {pc=new NormalImportStatement(el);ctx=new ImportContext(cur, pc);pc.setContext(ctx);cur=ctx;}
            importPart3[(NormalImportStatement)pc] (COMMA importPart3[(NormalImportStatement)pc])*
        ) opt_semi
    ;
importPart1 [AssigningImportStatement cr] //current rule
		{Qualident q1;}
    : i1:IDENT BECOMES q1=qualident {cr.addAssigningPart(i1,q1);}
    ;
importPart2 [QualifiedImportStatement cr] //current rule
		{Qualident q3;IdentList il=new IdentList();}
    : q3=qualident /*DOT*/ LCURLY identList[il] { cr.addSelectivePart(q3, il);} RCURLY
    ;
importPart3 [NormalImportStatement cr] //current rule
		{Qualident q2;}
    : q2=qualident {cr.addNormalPart(q2);}
    ;
classInheritance_[ClassInheritance ci]
		{TypeName tn=null;}
	:
    tn=inhTypeName {ci.add(tn);}
      (COMMA tn=inhTypeName {ci.add(tn);})*
    ;
classInheritanceRuby[ClassInheritance ci]:
    LT_ classInheritance_[ci]
    ;
docstrings[Documentable sc]:
    ((STRING_LITERAL)=> (s1:STRING_LITERAL {if (sc!=null) sc.addDocString(s1);})+
    |)
    ;
classScope[ClassStatement cr]
    : docstrings[cr]
    ( constructorDef[cr]
    | destructorDef[cr]
    | functionDef[cr.funcDef()]
    | varStmt[cr.statementClosure(), cr]
    | "type" IDENT BECOMES IDENT ( BOR IDENT)*
    | typeAlias[cr.typeAlias()]
    | programStatement[cr.XXX(), cr]
    | invariantStatement[cr.invariantStatement()]
    | accessNotation)*
    ;
constructorDef[ClassStatement cr]
        {ConstructorDef cd=null;IdentExpression x1=null;}
	: ("constructor"|"ctor")
		(x1=ident   {cd=cr.addCtor(x1);}
		|           {cd=cr.addCtor(null);}
		)
		opfal[cd.fal()]
		scope[cd.scope()]
					{cd.postConstruct();}
	;
destructorDef[ClassStatement cr]
        {DestructorDef dd=null;}
	: ("destructor"|"dtor") {dd=cr.addDtor();}
		opfal[dd.fal()]
		scope[dd.scope()]
					{dd.postConstruct();}
	;
namespaceScope[NamespaceStatement cr]
        //{Scope sc=null;}
    : docstrings[cr]
    (( functionDef[cr.funcDef()]
    | varStmt[cr.statementClosure(), cr]
    | typeAlias[cr.typeAlias()]
    | programStatement[cr.XXX(), cr]
    | invariantStatement[cr.invariantStatement()]
    | accessNotation ) opt_semi )*
    ;
scope[Scope sc]
      //{IExpression expr;}
    : LCURLY docstrings[sc]
      ((statement[sc.statementClosure(), sc.getParent()]
      | expr=expression {sc.statementWrapper(expr);} //expr.setContext(cur);
      | classStatement[new ClassStatement(sc.getParent())]
      | "continue"
      | "break" // opt label?
      | "return" ((expression) => (expr=expression)|)
      ) opt_semi )*
      RCURLY
    ;
functionScope[Scope sc]
      //{IExpression expr;}
    : LCURLY docstrings[sc]
      (
        (
            ( statement[sc.statementClosure(), sc.getParent()]
            | expr=expression {sc.statementWrapper(expr);}
            | classStatement[new ClassStatement(sc.getParent())]
            | "continue"
            | "break" // opt label?
            | "return" ((expression) => (expr=expression)|)
            )
            opt_semi
        )*
      | "abstract" opt_semi {((FunctionDef)((FunctionDef.FunctionDefScope)sc).getParent()).setAbstract(true);}
      ) RCURLY
    ;

functionDef[FunctionDef fd]
    	{AnnotationClause a=null;FunctionContext ctx=null;IdentExpression i1=null;TypeName tn=null;}
    : (a=annotation_clause      {fd.addAnnotation(a);})*
    i1=ident                    {fd.setName(i1);}
                                {ctx=new FunctionContext(cur, fd);fd.setContext(ctx);cur=ctx;}
    ( "const"                   {fd.set(FunctionModifiers.CONST);}
    | "immutable"               {fd.set(FunctionModifiers.IMMUTABLE);})?
    opfal[fd.fal()]
    (TOK_ARROW tn=typeName2 {fd.setReturnType(tn);})?
    functionScope[fd.scope()] // TODO what about pre/post??
    {fd.postConstruct();}
    ;
programStatement[ProgramClosure pc, OS_Element cont]
		{ImportStatement imp=null;}
    : imp=importStatement[cont] {pc.addImportStatement(imp);}
    | namespaceStatement[pc.namespaceStatement(cont)]
    | classStatement[pc.classStatement(cont)]
    | aliasStatement[pc.aliasStatement(cont)]
    ;
varStmt[StatementClosure cr, OS_Element aParent]
        {VariableSequence vsq=null;}
    : ("var" {vsq=cr.varSeq(cur);}
    | ("const"|"val") {vsq=cr.varSeq(cur);vsq.defaultModifiers(TypeModifiers.CONST);})
    ( varStmt_i[vsq.next()] (COMMA varStmt_i[vsq.next()])*
  //    LPAREN identList[cr.identList()] RPAREN BECOMES eee=expression // TODO what is this?
    )
    ;
typeAlias[TypeAliasExpression cr]
		{Qualident q=null;}
	:
	"type" "alias" i:IDENT {cr.setIdent(i);}
		BECOMES q=qualident {cr.setBecomes(q);}
	| "struct" name:IDENT opfal[null] scope[null] // TODO shouldnt be here
	;
opfal[FormalArgList fal]:
	LPAREN formalArgList[fal] RPAREN
	;
opfal2 returns [FormalArgList fal]
		{fal=new FormalArgList();}
	: LPAREN formalArgList[fal] RPAREN
	;
statement[StatementClosure cr, OS_Element aParent]
		{Qualident q=null;FormalArgList o=null;}
	:
	( expr=postfixExpression {cr.statementWrapper(expr);}
	//procedureCallStatement[cr.procCallExpr()]
	| ifConditional[cr.ifConditional()]
	| matchConditional[cr.matchConditional(), aParent]
	| caseConditional[cr.caseConditional()]
	| varStmt[cr, aParent]
	| whileLoop[cr]
	| frobeIteration[cr]
	| "construct" q=qualident o=opfal2 {cr.constructExpression(q,o);}
	| "yield" expr=expression {cr.yield(expr);}
	) opt_semi
	;
opt_semi: (SEMI|);
identList[IdentList ail]
		//{String s=null;}
	: s:IDENT {ail.push(new IdentExpression(s));}
	(COMMA s2:IDENT {ail.push(new IdentExpression(s2));})*
	;
expression returns [IExpression ee]
		{ee=null;}
	: ee=assignmentExpression
	;
/*
ident2 returns [String ident]
		{ident=null;}
	:
	r1:IDENT {ident=r1.getText();}
	;
*/
aliasStatement[AliasStatement pc]
	: "alias" i1:IDENT {pc.setName(i1);} BECOMES xy=qualident {pc.setExpression(xy);}
	;
qualidentList[QualidentList qal]
		{Qualident qid;}
	: qid=qualident {qal.add(qid);} (COMMA qid=qualident {qal.add(qid);})*
	;
ident returns [IdentExpression id]
		{id=null;}
	: r1:IDENT {id=new IdentExpression(r1, cur);}
	;
expressionList[ExpressionList el]
	: expr=expression {el.next(expr);} (COMMA expr=expression {el.next(expr);})*
	;
expressionList2 returns [ExpressionList el]
		{el = new ExpressionList();}
	: expr=expression {el.next(expr);} (COMMA expr=expression {el.next(expr);})*
	;
variableReference returns [IExpression ee]
		{ProcedureCallExpression pcx;ExpressionList el=null;ee=null;}
	: r1:IDENT  {ee=new IdentExpression(r1);}
	( DOT r2:IDENT {ee=new DotExpression(ee, new IdentExpression(r2));}
	| LBRACK expr=expression RBRACK {ee=new GetItemExpression(ee, expr);}
	| lp:LPAREN	(el=expressionList2)?
      {ProcedureCallExpression pce=new ProcedureCallExpression();
      pce.identifier(ee);
      pce.setArgs(el);
      ee=pce;} RPAREN

	//pcx=procCallEx2 {pcx.setLeft(ee);ee=pcx;}
	) //{ee=vr;}
	;
procCallEx2 returns [ProcedureCallExpression pce]
		{pce=null;ExpressionList el=null;}
	: lp:LPAREN el=expressionList2 rp:RPAREN {pce=new ProcedureCallExpression(lp,el,rp);}
	;
invariantStatement[InvariantStatement cr]
        {InvariantStatementPart isp=null;}
	: "invariant"
        ((i1:IDENT)? 		{isp = new InvariantStatementPart(cr, i1);}
         TOK_COLON 			//{el=new ExpressionList();}
         expr=expression    {isp.setExpr(expr);})*
    ;
accessNotation
	: "access" IDENT
	;


// expressions
// Note that most of these expressions follow the pattern
//   thisLevelExpression :
//       nextHigherPrecedenceExpression
//           (OPERATOR nextHigherPrecedenceExpression)*
// which is a standard recursive definition for a parsing an expression.
// The operators in java have the following precedences:
//    lowest  (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
//            (12)  ?:
//            (11)  ||
//            (10)  &&
//            ( 9)  |
//            ( 8)  ^
//            ( 7)  &
//            ( 6)  == !=
//            ( 5)  < <= > >=
//            ( 4)  << >>
//            ( 3)  +(binary) -(binary)
//            ( 2)  * / %
//            ( 1)  ++ -- +(unary) -(unary)  ~  !  (type)
//                  []   () (method call)  . (dot -- identifier qualification)
//                  new   ()  (explicit parenthesis)
//
// the last two are not usually on a precedence chart; I put them in
// to point out that new has a higher precedence than '.', so you
// can validy use
//     new Frame().show()
//
// Note that the above precedence levels map to the rules below...
// Once you have a precedence chart, writing the appropriate rules as below
//   is usually very straightfoward


// assignment expression (level 13)
assignmentExpression returns [IExpression ee]
		{ee=null;IExpression e=null;IExpression e2;ExpressionKind ek=null;}
	:	ee=conditionalExpression
		(

			(	BECOMES/*^*/				{ek= (ExpressionKind.ASSIGNMENT);}
            |   PLUS_ASSIGN/*^*/		    {ek= (ExpressionKind.AUG_PLUS);}
            |   MINUS_ASSIGN/*^*/			{ek= (ExpressionKind.AUG_MINUS);}
            |   STAR_ASSIGN/*^*/			{ek= (ExpressionKind.AUG_MULT);}
            |   DIV_ASSIGN/*^*/				{ek= (ExpressionKind.AUG_DIV);}
            |   MOD_ASSIGN/*^*/				{ek= (ExpressionKind.AUG_MOD);}
            |   SR_ASSIGN/*^*/				{ek= (ExpressionKind.AUG_SR);}
            |   BSR_ASSIGN/*^*/				{ek= (ExpressionKind.AUG_BSR);}
            |   SL_ASSIGN/*^*/			    {ek= (ExpressionKind.AUG_SL);}
            |   BAND_ASSIGN/*^*/			{ek= (ExpressionKind.AUG_BAND);}
            |   BXOR_ASSIGN/*^*/			{ek= (ExpressionKind.AUG_BXOR);}
            |   BOR_ASSIGN/*^*/				{ek= (ExpressionKind.AUG_BOR);}
            )

			e2=assignmentExpression 		{ ee = ExpressionBuilder.build(ee, ek, e2);}
		)?
	;


// conditional test (level 12)
conditionalExpression returns [IExpression ee]
		{ee=null;}
	:	ee=logicalOrExpression
//		( QUESTION/*^*/ assignmentExpression COLON/*!*/ conditionalExpression )? // TODO ignoring this for now
	;


// logical or (||)  (level 11)
logicalOrExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=logicalAndExpression
		(LOR/*^*/ e3=logicalAndExpression   	{ee = ExpressionBuilder.build(ee, ExpressionKind.LOR, e3);})*
	;


// logical and (&&)  (level 10)
logicalAndExpression returns [IExpression ee]
		{ee=null;IExpression e3=null;}
	:	ee=inclusiveOrExpression
		(LAND/*^*/ e3=inclusiveOrExpression	{ee = ExpressionBuilder.build(ee, ExpressionKind.LAND, e3);})*
	;


// bitwise or non-short-circuiting or (|)  (level 9)
inclusiveOrExpression returns [IExpression ee]
		{ee=null;IExpression e3=null;}
	:	ee=exclusiveOrExpression
		(BOR/*^*/ e3=exclusiveOrExpression		{ee = ExpressionBuilder.build(ee, ExpressionKind.BOR, e3);})*
	;


// exclusive or (^)  (level 8)
exclusiveOrExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=andExpression
		(BXOR/*^*/ e3=andExpression				{ee = ExpressionBuilder.build(ee, ExpressionKind.BXOR, e3);})*
	;


// bitwise or non-short-circuiting and (&)  (level 7)
andExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	ee=equalityExpression
		(BAND/*^*/ e3=equalityExpression	{ee = ExpressionBuilder.build(ee, ExpressionKind.BAND, e3);})*
	;


// equality/inequality (==/!=) (level 6)
equalityExpression  returns [IExpression ee]
		{ee=null;
		ExpressionKind e2=null;
		IExpression e3=null;}
	:	ee=relationalExpression
		((NOT_EQUAL/*^*/                   {e2=ExpressionKind.NOT_EQUAL;}
		| EQUAL/*^*/                       {e2=ExpressionKind.EQUAL;}
		) e3=relationalExpression 				{ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// boolean relational expressions (level 5)
relationalExpression returns [IExpression ee]
		{ee=null;
		ExpressionKind e2=null; // should never be null (below)
		IExpression e3=null;
		TypeName tn=null;}

	:	ee=shiftExpression
		(	(	(	LT_/*^*/            {e2=ExpressionKind.LT_;}
				|	GT/*^*/             {e2=ExpressionKind.GT;}
				|	LE/*^*/             {e2=ExpressionKind.LE;}
				|	GE/*^*/             {e2=ExpressionKind.GE;}
				)
				e3=shiftExpression      {ee=ExpressionBuilder.build(ee,e2,e3);
										ee.setType(new OS_Type(BuiltInTypes.Boolean));}
			)*
		|	"is_a"/*^*/ tn=typeName2 //typeSpec[true]
										{ee=new TypeCheckExpression(ee, tn);}
		)
	;


// bit shift expressions (level 4)
shiftExpression returns [IExpression ee]
		{ee=null;ExpressionKind e2=null;
		IExpression e3=null;}
	:	ee=additiveExpression
	((SL/*^*/ {e2=ExpressionKind.LSHIFT;}
	 | SR/*^*/ {e2=ExpressionKind.RSHIFT;}
	 | BSR/*^*/ {e2=ExpressionKind.BSHIFTR;}
	 ) e3=additiveExpression				{ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// binary addition/subtraction (level 3)
additiveExpression returns [IExpression ee]
		{ee=null;ExpressionKind e2=null;
		IExpression e3=null;}
	:	ee=multiplicativeExpression 
	(
		( PLUS/*^*/  {e2=ExpressionKind.ADDITION;}
		| MINUS/*^*/ {e2=ExpressionKind.SUBTRACTION;}) 
		e3=multiplicativeExpression {ee = ExpressionBuilder.build(ee, e2, e3);})*
	;


// multiplication/division/modulo (level 2)
multiplicativeExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;ExpressionKind e2=null;}
	:	ee=unaryExpression 
	((STAR/*^*/ {e2=ExpressionKind.MULTIPLY;}
	| DIV/*^*/  {e2=ExpressionKind.DIVIDE;}
	| MOD/*^*/  {e2=ExpressionKind.MODULO;}
	) e3=unaryExpression {ee = ExpressionBuilder.build(ee, e2, e3);})*
	;

unaryExpression returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	INC/*^*/ ee=unaryExpression {ee.setKind(ExpressionKind.INC);}
	|	DEC/*^*/ ee=unaryExpression {ee.setKind(ExpressionKind.DEC);}
	|	MINUS/*^*/ /*{#MINUS.setType(UNARY_MINUS);}*/ ee=unaryExpression {ee.setKind(ExpressionKind.NEG);}
	|	PLUS/*^*/  /*{#PLUS.setType(UNARY_PLUS);}*/ ee=unaryExpression {ee.setKind(ExpressionKind.POS);}
	|	ee=unaryExpressionNotPlusMinus
	;

unaryExpressionNotPlusMinus returns [IExpression ee]
		{ee=null;
		IExpression e3=null;}
	:	BNOT/*^*/ ee=unaryExpression {ee.setKind(ExpressionKind.BNOT);}
	|	LNOT/*^*/ ee=unaryExpression {ee.setKind(ExpressionKind.LNOT);}
	|	ee=postfixExpression
	;

// qualified names, array expressions, method invocation, post inc/dec
postfixExpression returns [IExpression ee]
		{ee=null;TypeCastExpression tc=null;TypeName tn=null;
		IExpression e3=null;ExpressionList el=null;}
	:	ee=primaryExpression // start with a primary

		(	// qualified id (id.id.id.id...) -- build the name
			DOT/*^*/ 
				( ee=dot_expression_or_procedure_call[ee]
//				| "this"
//				| "class"
//				| newExpression
//				| "inherit" LPAREN ( expressionList2 )? RPAREN
				)
			// the above line needs a semantic check to make sure "class"
			//   is the _last_ qualifier.

			// allow ClassName[].class
//		|	( lbc:LBRACK/*^*/ /*{#lbc.setType(ARRAY_DECLARATOR);}*/ RBRACK/*!*/ )+
//			DOT/*^*/ "class"

			// an array indexing operation
		|	lb:LBRACK/*^*/ /*{#lb.setType(INDEX_OP);}*/ expr=expression rb:RBRACK/*!*/
			{ee=new GetItemExpression(ee, expr);((GetItemExpression)ee).parens(lb,rb);}
			( BECOMES expr=expression {ee=new SetItemExpression((GetItemExpression)ee, expr);}
			)?

			// method invocation
		|	lp:LPAREN/*^*/ /*{#lp.setType(METHOD_CALL);}*/
				(el=expressionList2)? 
{ProcedureCallExpression pce=new ProcedureCallExpression();
pce.identifier(ee);
pce.setArgs(el);
ee=pce;}
			RPAREN/*!*/
		)*

		// possibly add on a post-increment or post-decrement.
		// allows INC/DEC on too much, but semantics can check
		(	in:INC/*^*/ /*{#in.setType(POST_INC);}*/
	 	|	de:DEC/*^*/ /*{#de.setType(POST_DEC);}*/
		|	// nothing
		)

		( (AS|CAST_TO) {tc=new TypeCastExpression();ee=tc;} tn=typeName2 {tc.setTypeName(tn);})?
		
		// look for int.class and int[].class
//	|	builtInType
//		( lbt:LBRACK/*^*/ /*{#lbt.setType(ARRAY_DECLARATOR);}*/ RBRACK/*!*/ )*
//		DOT/*^*/ "class"
	;



dot_expression_or_procedure_call [IExpression e1] returns [IExpression ee]
		{ee=null;ExpressionList el=null;}
	: e:IDENT {ee=new DotExpression(e1, new IdentExpression(e));}

    ( lp2:LPAREN/*^*/ /*{#lp.setType(METHOD_CALL);}*/
      (el=expressionList2)?
            {ProcedureCallExpression pce=new ProcedureCallExpression();
            pce.identifier(ee);
            pce.setArgs(el);
            ee=pce;}
     RPAREN/*!*/)?
	;



// the basic element of an expression
primaryExpression returns [IExpression ee]
		{ee=null;FuncExpr ppc=null;}
		//IExpression e3=null;*/}
	:	e:IDENT {ee=new IdentExpression(e);}
//	|	newExpression
	|	ee=constantValue
//	|	"super"
	|	"true"
	|	"false"
	|	"this"
	|	"null"
	|	LPAREN/*!*/ ee=assignmentExpression RPAREN/*!*/ {ee=new SubExpression(ee);}
	|   {ppc=new FuncExpr();} funcExpr[ppc] {ee=ppc;}
	;
funcExpr[FuncExpr pc] // remove scope to use in `typeName's
		{Scope0 sc = new Scope0(pc);TypeName tn=null;}
	:
	( "function"  {	pc.type(TypeModifiers.FUNCTION);	}
	  (opfal[pc.argList()]) scope[pc.scope()]
	  ((TOK_ARROW|TOK_COLON) tn=typeName2 {pc.setReturnType(tn);} )?
	| "procedure" {	pc.type(TypeModifiers.PROCEDURE);	}
	  (opfal[pc.argList()]) scope[pc.scope()]
	| 
	  LCURLY ( BOR formalArgList[sc.fal()] BOR )? 
	  (statement[sc.statementClosure(), sc.getParent()]
      | expr=expression {sc.statementWrapper(expr);}
      | classStatement[new ClassStatement(sc.getParent())]
      )*
      RCURLY
	
	)
	;



procedureCallStatement[StatementClosure cr]
	 {ProcedureCallExpression pce=cr.procedureCallExpression();}
//	 {ProcedureCallStatement pce=cr.procedureCallStatement();}
	: xy=qualident {pce.identifier(xy);}
	  procCallEx[pce]
	;
ifConditional[IfConditional ifex]
	: "if" expr=expression {ifex.expr(expr);}
	scope[ifex.scope()]
	("else" scope[ifex.else_().scope()]
	| elseif_part[ifex.elseif()]
	)*
	;
matchConditional[MatchConditional mc, OS_Element aParent]
		{MatchConditional.MatchConditionalPart1 mcp1=null;
		 MatchConditional.MatchConditionalPart2 mcp2=null;
		 TypeName tn=null;}
    : "match" expr=expression {mc.setParent(aParent);mc.expr(expr);}
      LCURLY // TODO MatchContext
      ( { mcp1 = mc.typeMatch();} 
      		i1:IDENT {mcp1.ident(i1);} TOK_COLON tn=typeName2 {mcp1.setTypeName(tn);} scope[mcp1.scope()]
      | { mcp2 = mc.normal();}
      		expr=expression {mcp2.expr(expr);} scope[mcp2.scope()]
      )+
      RCURLY
    ;
caseConditional[CaseConditional mc]
    : "case" expr=expression {mc.expr(expr);}
/*      LCURLY
      ( i1:IDENT TOK_COLON typeName scope
      | expression scope
      )
      RCURLY
*/    ;

whileLoop[StatementClosure cr]
	 {Loop loop=cr.loop();}
	:( "while"                 {loop.type(LoopTypes2.WHILE);}
	  expr=expression         {loop.expr(expr);}
	  scope[loop.scope()]
	| "do"                    {loop.type(LoopTypes2.DO_WHILE);}
	  scope[loop.scope()]
      "while" expr=expression {loop.expr(expr);})
    ;
frobeIteration[StatementClosure cr]
	 {Loop loop=cr.loop();}
	:"iterate"
	( "from"                   {loop.type(LoopTypes2.FROM_TO_TYPE);}
	  expr=expression          {loop.frompart(expr);}
	  "to" expr=expression     {loop.topart(expr);}
      	("with" i1:IDENT       {loop.iterName(i1);})?
	| "to"                     {loop.type(LoopTypes2.TO_TYPE);}
	  expr=expression          {loop.topart(expr);}
      	("with" i2:IDENT       {loop.iterName(i2);})?
	|                          {loop.type(LoopTypes2.EXPR_TYPE);}
	  expr=expression          {loop.topart(expr);}
      	("with" i3:IDENT       {loop.iterName(i3);})?
    )
    scope[loop.scope()]
    ;
procCallEx[ProcedureCallExpression pce]
	: LPAREN (expressionList[pce.exprList()])? RPAREN
	;
varStmt_i[VariableStatement vs]
		{TypeName tn=null;}
	: i:IDENT                   {vs.setName(i);}
	( TOK_COLON tn=typeName2    {vs.setTypeName(tn);})?
	( BECOMES expr=expression   {vs.initial(expr);})?
	;
elseif_part[IfConditional ifex]
	: ("elseif" | "else" "if") expr=expression {ifex.expr(expr);}
	scope[ifex.scope()]
	;

//
// TYPENAMES
//
/*
variableQualifiers[NormalTypeName cr]:
	( "once"  {cr.set(TypeModifiers.ONCE);}
	| "local"  {cr.set(TypeModifiers.LOCAL);}
	| "tagged"  {cr.set(TypeModifiers.TAGGED);}
	| "const"  {cr.set(TypeModifiers.CONST);}
	| "pooled"  {cr.set(TypeModifiers.POOLED);}
	| "manual"  {cr.set(TypeModifiers.MANUAL);}
	| "gc"  {cr.set(TypeModifiers.GC);})
	;
regularQualifiers[NormalTypeName fp]
		{IdentExpression i1=null;}
	:
	( "in"            {fp.setIn(true);}
	| "out"           {fp.setOut(true);})?
	( ("const"        {fp.setConstant(true);}
	   ("ref"		  {fp.setReference(true);})?)
	| "ref"           {fp.setReference(true);}
	| "generic" i2:IDENT 	  {fp.setGeneric(true);
        RegularTypeName rtn = new RegularTypeName();
        Qualident q = new Qualident();
        q.append(i2);
        rtn.setName(q);
        fp.addGenericPart(rtn);}
	)
	;

typeName[NormalTypeName cr]:
    ( structTypeName[cr]
//    | funcTypeExpr[cr]
    | simpleTypeName_xx[cr]
    ) {cr.setContext(cur);}
    ;
structTypeName[NormalTypeName cr]
	:
	( genericQualifiers[cr]
	  ( abstractGenericTypeName_xx[cr]
	  | specifiedGenericTypeName_xx[cr] )
	| "typeof" xy=qualident {cr.typeof(xy);}
	)
	;
genericQualifiers[NormalTypeName cr]
	: ( "const"    {cr.set(TypeModifiers.CONST);})?
	  ( "ref"      {cr.set(TypeModifiers.REFPAR);})?
	;

abstractGenericTypeName_xx[NormalTypeName tn]
	: "generic" xy=qualident {tn.typeName(xy); tn.set(TypeModifiers.GENERIC);}
	| QUESTION xy=qualident {tn.typeName(xy); tn.set(TypeModifiers.GENERIC);}
	;
specifiedGenericTypeName_xx[NormalTypeName tn]
		{RegularTypeName rtn=new RegularTypeName();}
	: simpleTypeName_xx [tn]
	  (LBRACK typeName[rtn] {tn.addGenericPart(rtn);} RBRACK)? // TODO what about  multi-generics?
	  (QUESTION {tn.setNullable();})?
	;
formalArgTypeName[NormalTypeName tn]
	: structTypeName[tn]
//	| funcTypeExpr[null/*tn* /]
	;
simpleTypeName_xx[NormalTypeName tn]
	: xy=qualident  {tn.setName(xy);}
	;

typeNameList[TypeNameList cr]:
	typeName [cr.next()] (COMMA typeName [cr.next()])*
	;
*/
inhTypeName returns [TypeName tn]
		{tn=null;}
	:
	( tn=typeOfTypeName2
	| tn=normalTypeName2
	)
/*    (("const" {tn.set(TypeModifiers.CONST);})? specifiedGenericTypeName_xx[tn]
    | "typeof" xy=qualident {tn.typeof(xy);}
    )
*/
        {tn.setContext(cur);}
    ;
/*
funcTypeExpr[FuncTypeName pc]
	:
	( "function"  {	pc.type(TypeModifiers.FUNCTION);	}
	  (LPAREN typeNameList[pc.argList()] RPAREN)?
	  ((TOK_ARROW|TOK_COLON) typeName[pc.returnValue()] )?
	| "procedure" {	pc.type(TypeModifiers.PROCEDURE);	}
	  (LPAREN typeNameList[pc.argList()] RPAREN)?
	)
	;
*/

typeName2 returns [TypeName cr]
		{cr=null;}
	: cr=genericTypeName2
	| cr=typeOfTypeName2
	| cr=normalTypeName2
	| cr=functionTypeName2
	;
genericTypeName2 returns [GenericTypeName tn]
		{tn=new GenericTypeName(cur);}
	: ("generic"|QUESTION) xy=qualident
		{tn.typeName(xy); tn.set(TypeModifiers.GENERIC);}
	;
typeOfTypeName2 returns [TypeOfTypeName tn]
		{tn=new TypeOfTypeName(cur);}
	: "typeof" xy=qualident
		{tn.typeOf(xy); tn.set(TypeModifiers.TYPE_OF);}
	;
normalTypeName2 returns [NormalTypeName tn]
		{tn=new RegularTypeName(cur); TypeName rtn=null;}
	: regularQualifiers2[tn]
	  xy=qualident          {tn.setName(xy);}
	  (LBRACK rtn=typeName2 {tn.addGenericPart(rtn);} RBRACK)? // TODO what about  multi-generics?
	  (QUESTION {tn.setNullable();})?
	;
functionTypeName2 returns [FuncTypeName tn]
		{tn=new FuncTypeName(cur); TypeName rtn=null; TypeNameList tnl=new TypeNameList();}
	: ( ("function"|"func")                         { tn.type(TypeModifiers.FUNCTION); }
	  (LPAREN tnl=typeNameList2 RPAREN)            { tn.argList(tnl); }
	  ((TOK_ARROW|TOK_COLON) rtn=typeName2          { tn.returnValue(rtn);} )?
	| ("procedure"|"proc")                          { tn.type(TypeModifiers.PROCEDURE);	}
	  (LPAREN tnl=typeNameList2 RPAREN)            { tn.argList(tnl); }
	)
	;
regularQualifiers2[NormalTypeName fp]
	:
	( "in"            {fp.setIn(true);}
	| "out"           {fp.setOut(true);})?
	( ("const"        {fp.setConstant(true);}
	   ("ref"		  {fp.setReference(true);})?)
	| "ref"           {fp.setReference(true);}
	)?
	;
typeNameList2 returns [TypeNameList cr]
		{TypeName tn=null;cr=new TypeNameList();}
	: tn=typeName2                  {cr.add(tn);}
	    (COMMA tn=typeName2)*       {cr.add(tn);}
	;

//
//
//

defFunctionDef[DefFunctionDef fd]
		{FormalArgList op=null;}
	: "def" i1:IDENT op=opfal2/*[fd.fal()]*/  BECOMES expr=expression
	   {fd.setType(DefFunctionDef.DEF_FUN); fd.setName(i1); fd.setOpfal(op); fd.setExpr(expr); }
	;
formalArgList[FormalArgList fal]
	: (formalArgListItem_priv[fal.next()]
	  (COMMA formalArgListItem_priv[fal.next()])*)?
	;
formalArgListItem_priv[FormalArgListItem fali]
		{ TypeName tn=null; }
	:
		( (regularQualifiers2[(NormalTypeName)fali.typeName()])?
		  i:IDENT  {	fali.setName(i);	}
		  ( TOK_COLON tn=typeName2  { fali.setTypeName(tn); } )?
//		  ( TOK_COLON formalArgTypeName[fali.typeName()])?
//		| abstractGenericTypeName_xx[fali.typeName()]
		)
	;



//----------------------------------------------------------------------------
// The Elijjah scanner
//----------------------------------------------------------------------------
class ElijjahLexer extends Lexer;

options {
	exportVocab=Elijjah;      // call the vocabulary "Java"
	testLiterals=false;    // don't automatically test for literals
	k=4;                   // four characters of lookahead
//	charVocabulary='\u0003'..'\uFFFF';
	interactive=true;
}



// OPERATORS
QUESTION		:	'?'		;
LPAREN			:	'('		;
RPAREN			:	')'		;
LBRACK			:	'['		;
RBRACK			:	']'		;
LCURLY			:	'{'		;
RCURLY			:	'}'		;
TOK_COLON		:	':'		;
COMMA			:	','		;
//DOT			:	'.'		;
BECOMES			:	'='		;
EQUAL			:	"=="	;
LNOT			:	'!'		;
BNOT			:	'~'		;
NOT_EQUAL		:	"!="	;
DIV				:	'/'		;
DIV_ASSIGN		:	"/="	;
PLUS			:	'+'		;
PLUS_ASSIGN		:	"+="	;
INC				:	"++"	;
TOK_ARROW  		:   "->"	;
MINUS			:	'-' ('>' 		{_ttype=TOK_ARROW;})?;
MINUS_ASSIGN	:	"-="	;
DEC				:	"--"	;
STAR			:	'*'		;
STAR_ASSIGN		:	"*="	;
MOD				:	'%'		;
MOD_ASSIGN		:	"%="	;
SR				:	">>"	;
SR_ASSIGN		:	">>="	;
BSR				:	">>>"	;
BSR_ASSIGN		:	">>>="	;
GE				:	">="	;
GT				:	">"		;
SL				:	"<<"	;
SL_ASSIGN		:	"<<="	;
LE				:	"<="	;
LT_				:	'<'		;
BXOR			:	'^'		;
BXOR_ASSIGN		:	"^="	;
BOR				:	'|'		;
BOR_ASSIGN		:	"|="	;
LOR				:	"||"	;
BAND			:	'&'		;
BAND_ASSIGN		:	"&="	;
LAND			:	"&&"	;
SEMI			:	';'		;
ANNOT           :   "#["    ;

// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
		// handle newlines
		|	(	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)
		{ _ttype = Token.SKIP; }
	;

// Single-line comments
SL_COMMENT
	:	"//"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
		{$setType(Token.SKIP); newline();}
	;

// multiple-line comments
ML_COMMENT
	:	"/*"
		(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*/"
		{$setType(Token.SKIP);}
	;


// character literals
CHAR_LITERAL
	:	'\'' ( ESC | ~'\'' ) '\''
	;

// string literals
STRING_LITERAL
	:	'"' (ESC|~('"'|'\\'))* '"'
	;


// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected
ESC
	:	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
		|	('0'..'3')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'7')
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	('4'..'7')
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	('0'..'9')
			)?
		)
	;


// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;


// a dummy rule to force vocabulary to be all characters (except special
//   ones that ANTLR uses internally (0 to 2)
protected
VOCAB
	:	'\3'..'\377'
	;


// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT
	options {testLiterals=true;}
	:	('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$')*
	;


// a numeric literal
NUM_INT
	{boolean isDecimal=false;}
	:	'.' {_ttype = DOT;}
			(('0'..'9')+ (EXPONENT)? (FLOAT_SUFFIX)? { _ttype = NUM_FLOAT; })?
	|	(	'0' {isDecimal = true;} // special case for just '0'
			(	('x'|'X')
				(											// hex
					// the 'e'|'E' and float suffix stuff look
					// like hex digits, hence the (...)+ doesn't
					// know when to stop: ambig.  ANTLR resolves
					// it correctly by matching immediately.  It
					// is therefor ok to hush warning.
					options {
						warnWhenFollowAmbig=false;
					}
				:	HEX_DIGIT
				)+
			|	('0'..'7')+									// octal
			)?
		|	('1'..'9') ('0'..'9'|'_')*  {isDecimal=true;}		// non-zero decimal
		)
		(	('l'|'L')
		|   //INTLIT_TY
			('u'|'i') ("8"|"16"|"32"|"64"|"size")
		// only check to see if it's a float if looks like decimal so far
		|	{isDecimal}?
			(	'.' ('0'..'9')* (EXPONENT)? (FLOAT_SUFFIX)?
			|	EXPONENT (FLOAT_SUFFIX)?
			|	FLOAT_SUFFIX
			)
			{ _ttype = NUM_FLOAT; }
		)?
	;

/*
INTLIT_TY
	: ('u'|'i') ("8"|"16"|"32"|"64"|"size")
	;
*/

// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
	:	('e'|'E') ('+'|'-')? ('0'..'9')+
	;


protected
FLOAT_SUFFIX
	:	'f'|'F'|'d'|'D'
	;

