// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScopeElement.java

package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public interface ScopeElement {

	public abstract void print_osi(TabbedOutputStream tabbedoutputstream)
			throws IOException;
}
