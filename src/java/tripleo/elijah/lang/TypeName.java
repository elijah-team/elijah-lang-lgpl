// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TypeName.java

package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			TypeNameList

public interface TypeName {

	public abstract boolean isNull();

	public abstract boolean getConstant();

	public abstract void setConstant(boolean flag);

	public abstract boolean getReference();

	public abstract void setReference(boolean flag);

	public abstract boolean getOut();

	public abstract void setOut(boolean flag);

	public abstract boolean getIn();

	public abstract void setIn(boolean flag);

	public abstract String getName();

	public abstract void setName(String s);

	public abstract void set(TypeModifiers aModifiers);

	public abstract TypeName typeName(String s);

	public abstract TypeName typeof(String s);

	public abstract TypeName returnValue();

	public abstract void type(TypeModifiers modifiers);

	public abstract TypeNameList argList();

//	public static final int NORMAL = 0;
//
//	public static final int CONST = 1;
//
//	public static final int GC = 2;
//
//	public static final int TAGGED = 3;
//
//	public static final int POOLED = 4;
//
//	public static final int MANUAL = 5;
//
//	public static final int LOCAL = 6;
//
//	public static final int ONCE = 7;
//
//	public static final int PROCEDURE = 32;
//
//	public static final int FUNCTION = 33;

}
