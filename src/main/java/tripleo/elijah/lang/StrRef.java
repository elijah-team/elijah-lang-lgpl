// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StrRef.java

package tripleo.elijah.lang;

// Referenced classes of package pak:
//			AbstractExpression

public class StrRef extends AbstractExpression {

	public StrRef(String n) {
		value = n;
	}

	public String repr_() {
		return (new StringBuilder("StrRef (")).append(value).append(")")
				.toString();
	}

	public String value;
}
