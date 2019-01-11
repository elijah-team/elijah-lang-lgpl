// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcedureCall.java

package tripleo.elijah.lang;

import tripleo.elijah.util.TabbedOutputStream;

import antlr.collections.impl.LList;

// Referenced classes of package pak:
//			AbstractExpression, ExprListListener, ScopeElement, VariableReference, 
//			IExpression

public class ProcedureCall extends AbstractExpression implements
		ExprListListener, ScopeElement {

	public ProcedureCall(VariableReference ref) {
		name = ref;
	}

	public void setArgs(LList s) {
		args = s;
	}

	public boolean isEmpty() {
		return _args == null;
	}

	public void change(IExpression e) {
		if (!isEmpty()) {
			throw new IllegalStateException("_args!=null");
		} else {
			_args = e;
			return;
		}
	}

	public void print_osi(TabbedOutputStream tos) {
		try {
			tos.incr_tabs();
			tos.put_string_ln("ProcedureCall {");
			tos.put_string("name = ");
			name.print_osi(tos);
			tos.put_string("name = ");
			tos.dec_tabs();
			tos.put_string_ln("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VariableReference name;

	private LList args;

	private IExpression _args;
}
