// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TypeRef.java

package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public class TypeRef {

	public TypeRef(String n) {
		basicName = n;
	}

	public String getTypeString() {
		return basicName;
	}

	public String repr_() {
		return (new StringBuilder("TypeRef (")).append(getTypeString()).append(
				")").toString();
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println(tos.t());
		tos.put_string_ln((new StringBuilder("TypeRef { basicName = ")).append(
				getTypeString()).append(" } ").toString());
		System.out.println(tos.t());
	}

	public String basicName;
}
