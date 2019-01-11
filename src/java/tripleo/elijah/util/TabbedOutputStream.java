// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TabbedOutputStream.java

package tripleo.elijah.util;

import java.io.*;

public class TabbedOutputStream
{

	public int t()
	{
		return tabwidth;
	}

	void doIndent()
		throws IOException
	{
		for(int i = 0; i < tabwidth; i++)
			myStream.write(9);

	}

	public TabbedOutputStream(OutputStream os)
	{
		tabwidth = 0;
		myStream = new BufferedWriter(new OutputStreamWriter(os));
	}

	public boolean is_connected()
	{
		return myStream != null;
	}

	public void put_string_ln(String s)
		throws IOException
	{
		myStream.write(s);
		myStream.write(10);
	}

	public void put_newline()
		throws IOException
	{
		doIndent();
	}

	public void put_string(String s)
		throws InvalidObjectException, IOException
	{
		if(!is_connected())
		{
			throw new InvalidObjectException("is_connected assertion failed");
		} else
		{
			myStream.write(s);
			return;
		}
	}

	public void quote_string(String s)
		throws IOException
	{
		myStream.write(34);
		myStream.write(s);
		myStream.write(34);
	}

	public void close()
		throws IOException
	{
		if(myStream != null)
		{
			myStream.close();
			myStream = null;
		} else
		{
			System.out.println("closing twice");
		}
	}

	public void incr_tabs()
	{
		tabwidth++;
	}

	public void dec_tabs()
	{
		tabwidth--;
	}

	public static void main(String args[])
	{
		TabbedOutputStream tos = new TabbedOutputStream(System.out);
		int i = 0;
		int j = 0;
		try
		{
			for(; i < 10; i++)
			{
				tos.put_string_ln((new Integer(i)).toString());
				tos.incr_tabs();
			}

			tos.close();
		}
		catch(IOException ex)
		{
			System.out.println("error");
		}
	}

	int tabwidth;
	Writer myStream;
}
