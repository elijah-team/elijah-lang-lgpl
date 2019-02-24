// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IExpression.java

package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public class StringExpression extends AbstractExpression {

public StringExpression(String g) {
set(g);
}

	public  void print_osi(TabbedOutputStream tabbedoutputstream)
			throws IOException {
		assert false;
	}

	public  IExpression getLeft() {
		assert false;
		return null;
	}

	public void setLeft(IExpression iexpression) {
		assert false;
	}

	public  IExpression getRight() {
		assert false;
		return null;
	}

	public  void setRight(IExpression iexpression){
		assert false;
	}

	public  String repr_() {return repr_;}

	public void set(String g) {repr_ = g;}
	String repr_;
}
