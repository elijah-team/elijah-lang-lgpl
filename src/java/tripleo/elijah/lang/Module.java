package tripleo.elijah.lang;
//// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
//// Jad home page: http://www.geocities.com/kpdus/jad.html
//// Decompiler options: packimports(3) 
//// Source File Name:   Module.java
//
//package pak;
//
//import antlr.collections.Stack;
//import antlr.collections.impl.LList;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.util.*;
//import org.oluworld.util.TabbedOutputStream;
//
//// Referenced classes of package pak:
////			_Scope, Klass, ScopeElement, ExprListListener
//
//public class Module implements _Scope {
//
//	public Module() {
//		classStack = new LList();
//		elts = new Vector();
//	}
//
//	public String getPackageName() {
//		return packageName;
//	}
//
//	public void setPackageName(String s) {
//		packageName = s;
//	}
//
//	public Klass pushClass(String s) {
//		Klass k = new Klass(s);
//		classStack.push(k);
//		addElement(k);
//		return k;
//	}
//
//	public void addElement(ScopeElement e) {
//		elts.add(e);
//	}
//
//	public ExprListListener getListener() {
//		return new _Scope.ScopeExprListener() {
//
//			String getScopeListenerName() {
//				return "ModuleScopeListener";
//			}
//
//		};
//	}
//
//	public String repr_() {
//		return String.format("Module (%s)",packageName);
//	}
//
//	public void finish(TabbedOutputStream tos) throws IOException {
//		tos.close();
//	}
//
//	public void print_osi(TabbedOutputStream tos) throws IOException {
//		System.out.println("Module print_osi");
//		if (packageName != null) {
//			tos.put_string("package ");
//			tos.put_string_ln(packageName);
//			tos.put_string_ln("");
//		}
//		tos.put_string_ln("//");
//		synchronized (elts) {
//			for (ScopeElement element : elts)
//				element.print_osi(tos);
//
//		}
//	}
//
//	public void push(ScopeElement se) {
//		addElement(se);
//	}
//
//	public void end() {
//	}
//
//	private String packageName;
//	Stack classStack;
//	List<ScopeElement> elts;
//}
