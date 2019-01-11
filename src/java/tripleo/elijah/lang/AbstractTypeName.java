// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractTypeName.java

package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			TypeName

public abstract class AbstractTypeName implements TypeName {

	public AbstractTypeName() {
	}

	public boolean isNull() {
		return !pr_constant && !pr_reference && !pr_out && !pr_in
				&& pr_name == "";
	}

	public boolean getConstant() {
		return pr_constant;
	}

	public void setConstant(boolean s) {
		pr_constant = s;
	}

	public boolean getReference() {
		return pr_reference;
	}

	public void setReference(boolean s) {
		pr_reference = s;
	}

	public boolean getOut() {
		return pr_out;
	}

	public void setOut(boolean s) {
		pr_out = s;
	}

	public boolean getIn() {
		return pr_in;
	}

	public void setIn(boolean s) {
		pr_in = s;
	}

	public String getName() {
		return pr_name;
	}

	public void setName(String s) {
		pr_name = s;
	}

	private TypeModifiers tm;

	public void type(TypeModifiers atm) {
tm=atm;		
	}
	
	public void set(int aType) {
		type = aType;
	}

	protected boolean pr_constant;

	protected boolean pr_reference;

	protected boolean pr_out;

	protected boolean pr_in;

	protected String pr_name;

	int type;
}
