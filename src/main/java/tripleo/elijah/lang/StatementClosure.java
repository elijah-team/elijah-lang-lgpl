// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StatementClosure.java

package tripleo.elijah.lang;


// Referenced classes of package pak2:
//			VariableSequence, ProcedureCallExpression, Loop, YieldExpression, 
//			IfExpression

public interface StatementClosure {

	public abstract VariableSequence varSeq();

	public abstract ProcedureCallExpression procedureCallExpression();

	public abstract Loop loop();

	public abstract StatementClosure procCallExpr();

	public abstract void constructExpression(IExpression aExpr);

	public abstract void yield(IExpression aExpr);

	public abstract IfExpression ifExpression();

	public abstract BlockStatement blockClosure();
}
