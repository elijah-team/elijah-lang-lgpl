package tripleo.elijah;
 
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Main.java

import java.io.*;

import antlr.ANTLRException;

public class Main {

	public static void main(String args[]) {
		try {
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++)
					if (args[i].equals("-showtree"))
						showTree = true;
					else
						doFile(new File(args[i]));

			} else {
				System.err
						.println("Usage: java Main [-showtree] <directory or file name>");
			}
		} catch (Exception e) {
			System.err.println((new StringBuilder("exception: ")).append(e)
					.toString());
			e.printStackTrace(System.err);
		}
	}

	public static void doFile(File f) throws Exception {
		if (f.isDirectory()) {
			String files[] = f.list();
			for (int i = 0; i < files.length; i++)
				doFile(new File(f, files[i]));

		} else if (f.getName().length() > 3
				&& f.getName().substring(f.getName().length() - 3)
						.equals(".os")) {
			System.out.println((new StringBuilder("   ")).append(
					f.getAbsolutePath()).toString());
			parseFile(f.getName(), new FileInputStream(f));
		}
	}

	public static void parseFile(String f, InputStream s) throws Exception {
		try {
			ElijahLexer lexer = new ElijahLexer(s);
			lexer.setFilename(f);
			ElijahParser parser = new ElijahParser(lexer);
			parser.out = new Out();
			parser.setFilename(f);
			parser.program();
		} catch (ANTLRException e) {
			System.err.println(("parser exception: "+e));
			e.printStackTrace();
		}
	}

	static boolean showTree = false;

}
