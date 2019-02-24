// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExprListListener.java

package tripleo.elijah.lang;

// Referenced classes of package pak:
//			IExpression

public interface ExprListListener {

	public abstract void change(IExpression iexpression);

	public abstract String repr_();

	public abstract boolean isEmpty();
}
