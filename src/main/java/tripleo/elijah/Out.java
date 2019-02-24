package tripleo.elijah;


import java.io.*;

import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.TabbedOutputStream;

public class Out {

	public void FinishModule() {
		TabbedOutputStream tos;
		println("** FinishModule");
		tos = null;
		try {
			tos = new TabbedOutputStream(new FileOutputStream("oscc.out"));
//			pc.module.print_osi(tos);
			pc.module.finish(tos);
			//
			final JavaCodeGen visit = new JavaCodeGen();
			pc.module.visitGen(visit);
		} catch (FileNotFoundException fnfe) {
			println("&& FileNotFoundException");
		} catch (IOException ioe) {
			println("&& IOException");
//		} catch (Exception exception) {
//			try {
//				tos.close();
//			} catch (IOException ioe) {
//				println("&& IOException");
//			}
////			try {
////				println((new StringBuilder("[incorrect] stringstack.top = "))
////						.append((String) stringStack.top()).toString());
////			} catch (NoSuchElementException nsee) {
////				println("stringstack empty [correct]");
////			}
////			try {
////				println((new StringBuilder("[incorrect] exprstack.top = "))
////						.append(((IExpression) exprStack.top()).repr_())
////						.toString());
////			} catch (NoSuchElementException nsee) {
////				println("exprstack empty [correct]");
////			}
//			throw exception;
		}
//		try {
//			tos.close();
//		} catch (IOException ioe) {
//			println("&& IOException");
//		}
//		try {
//			println((new StringBuilder("[incorrect] stringstack.top = "))
//					.append((String) stringStack.top()).toString());
//		} catch (NoSuchElementException nsee) {
//			println("stringstack empty [correct]");
//		}
//		try {
//			println((new StringBuilder("[incorrect] exprstack.top = ")).append(
//					((IExpression) exprStack.top()).repr_()).toString());
//		} catch (NoSuchElementException nsee) {
//			println("exprstack empty [correct]");
//		}
		return;
	}

	public ProcedureCall popProcCall() {
		return null;
	}

	public void beginProcCallEx2() {
	}

	public void beginProcCall() {
	    //c=new ProcCall();
	    //pccs.push(c);
	}

	public void endProcCall() {
	}


	public void pushString(String s) {
		println((new StringBuilder("** Pushing Ident ")).append(s).toString());
	}

	public void pushCharLit(String s) {
	}

	public void pushMultiply() {
	}


	public void pushTypeRef(TypeRef tr) {
		println((new StringBuilder("** pushing typeref ")).append(tr.repr_())
				.toString());
	}

	//
	// print functions
	//
	private void print(String s) {
		System.out.print(s);
	}

	private void println(int s) {
		System.out.println(s);
	}

	public void println(String s) {
		System.out.println(s);
	}

	private void print3(String s1, String s2, String s3) {
		System.out.print(s1);
		System.out.print(s2);
		System.out.print(s3);
	}

	private void println(String s1, String s2) {
		System.out.print(s1);
		System.out.println(s2);
	}

	private void printpln(String s1, String s2) {
		System.out.print(s1);
		System.out.print('(');
		System.out.print(s2);
		System.out.println(')');
	}

	//
	// parser closure. soon to die!
	//
	private ParserClosure pc;

	public ParserClosure closure() {
		if (pc==null) 
		    pc=new ParserClosure();
	
	    return pc;
	}

}
