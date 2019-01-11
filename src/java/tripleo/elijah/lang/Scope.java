// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Scope.java

package tripleo.elijah.lang;


// Referenced classes of package pak2:
//			StatementClosure, BlockStatement

public interface Scope {

	public abstract void statementWrapper(IExpression aExpr);

	public abstract void addDocString(String s);

	public abstract StatementClosure statementClosure();

	public abstract BlockStatement blockStatement();

	public abstract void add(StatementItem aItem);
}
