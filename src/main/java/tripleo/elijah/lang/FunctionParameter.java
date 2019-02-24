// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FunctionParameter.java

package tripleo.elijah.lang;


public class FunctionParameter extends AbstractTypeName implements TypeName {

	public FunctionParameter() {
		pr_constant = false;
		pr_reference = false;
		pr_out = false;
		pr_in = false;
		pr_name = "";
	}

	public TypeName typeof() {
		return null;
	}

	public TypeName returnValue() {
		return null;
	}

	public void type(int i) {
	}

	public TypeNameList argList() {
		return null;
	}

	public TypeName typeof(String aXy) {
		return null;
	}

	public TypeName typeName(String aTypeName) {
		return null;
	}

	public void set(TypeModifiers aModifiers) {
		// TODO Auto-generated method stub
		
	}
}
