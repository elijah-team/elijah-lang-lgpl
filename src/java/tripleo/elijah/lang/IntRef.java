// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IntRef.java

package tripleo.elijah.lang;

// Referenced classes of package pak:
//			AbstractExpression

public class IntRef extends AbstractExpression {

	public IntRef(int n) {
		value = n;
	}

	public String repr_() {
		return (new StringBuilder("IntRef (")).append(
				(new Integer(value)).toString()).append(")").toString();
	}

	public int value;
}
