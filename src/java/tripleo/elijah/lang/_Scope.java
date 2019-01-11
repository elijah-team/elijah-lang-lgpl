// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   _Scope.java

package tripleo.elijah.lang;


// Referenced classes of package pak:
//			ScopeElement, ExprListListener, IExpression

public interface _Scope extends ScopeElement {
	public static abstract class ScopeExprListener implements ExprListListener {

		abstract String getScopeListenerName();

		public void change(IExpression e) {
			System.out.println((new StringBuilder(String
					.valueOf(getScopeListenerName()))).append(" changed")
					.toString());
			changed++;
		}

		public String repr_() {
			return (new StringBuilder("{")).append(getScopeListenerName())
					.append("}").toString();
		}

		public boolean isEmpty() {
			return changed == 0;
		}

		int changed;

		public ScopeExprListener() {
		}
	}

	public abstract String repr_();

	public abstract void push(ScopeElement scopeelement);

	public abstract ExprListListener getListener();
}
