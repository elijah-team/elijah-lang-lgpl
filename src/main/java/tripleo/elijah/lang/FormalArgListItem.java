// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FormalArgListItem.java

package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			TypeName

public class FormalArgListItem {
	
	String name;
	TypeName tn=new RegularTypeName();

	public TypeName typeName() {
		return tn;
	}

	public void setName(String s) {
		name=s;
	}
}
