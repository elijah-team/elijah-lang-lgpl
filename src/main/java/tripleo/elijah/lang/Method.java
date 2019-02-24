// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Method.java

package tripleo.elijah.lang;

import java.io.IOException;
import java.util.*;

import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			_Scope, ScopeElement, TypeRef, ExprListListener

public class Method implements _Scope {

	public Method(String name, int type) {
		methodName = name;
		methodType = type;
		elts = new Vector<ScopeElement>();
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Method print_osi");
		String r;
		if (methodType == 2 || methodType == 0)
			r = "Function (";
		else
			r = "Procedure (";
		tos.put_string(r);
		tos.put_string(methodName);
		tos.put_string_ln(") {");
		synchronized (elts) {
			for (Iterator<ScopeElement> e = elts.iterator(); e.hasNext(); e
					.next().print_osi(tos))
				;
		}
		tos.dec_tabs();
		tos.put_string_ln("} // Method");
	}

	public String typeString() {
		switch (methodType) {
		case 0: // '\0'
			return "UNSPECIFIED";

		case 1: // '\001'
			return "PROCEDURE";

		case 2: // '\002'
			return "FUNCTION";

		case 3: // '\003'
			return "HARDMETHOD";

		case 4: // '\004'
			return "HARDFUNCTION";
		}
		throw new IllegalStateException("bad type on Method");
	}

	public String repr_() {
		return (new StringBuilder("Method (")).append(methodName).append(" {")
				.append(typeString()).append("})").toString();
	}

	public void push(ScopeElement se) {
		if (se == null)
			System.out.println("se is null");
		try {
			se.print_osi(new TabbedOutputStream(System.out));
			elts.add(se);
		} catch (IOException ioexception) {
		}
	}

	public ExprListListener getListener() {
		return new _Scope.ScopeExprListener() {

			String getScopeListenerName() {
				return "MethodScopeListener";
			}
		};
	}

	public static final int UNSPECIFIED = 0;

	public static final int PROCEDURE = 1;

	public static final int FUNCTION = 2;

	public static final int COMMAND = 3;

	public static final int HARDMETHOD = 3;

	public static final int HARDFUNCTION = 4;

	String methodName;

	int methodType;

	TypeRef returnType;

	Vector args;

	Vector<ScopeElement> elts;
}
