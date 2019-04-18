/**
 * 
 */
package tripleo.elijah;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.nodes.ArgumentNode;
import tripleo.elijah.gen.nodes.BreakInCaseStatementNode;
import tripleo.elijah.gen.nodes.CaseChoiceNode;
import tripleo.elijah.gen.nodes.CaseDefaultNode;
import tripleo.elijah.gen.nodes.CaseHdrNode;
import tripleo.elijah.gen.nodes.CaseNode;
import tripleo.elijah.gen.nodes.ChoiceOptions;
import tripleo.elijah.gen.nodes.CloseCaseNode;
import tripleo.elijah.gen.nodes.CloseTmpCtxNode;
import tripleo.elijah.gen.nodes.ExpressionOperators;
import tripleo.elijah.gen.nodes.ImportNode;
import tripleo.elijah.gen.nodes.LocalAgnMathNode;
import tripleo.elijah.gen.nodes.LocalAgnTmpNode;
import tripleo.elijah.gen.nodes.LocalValAgnFnCallNode;
import tripleo.elijah.gen.nodes.MethHdrNode;
import tripleo.elijah.gen.nodes.ReturnAgnNode;
import tripleo.elijah.gen.nodes.ReturnAgnSimpleIntNode;
import tripleo.elijah.gen.nodes.SwitchNode;
import tripleo.elijah.gen.nodes.TmpSSACtxNode;
import tripleo.elijah.lang.ExpressionBuilder;
import tripleo.util.buffer.*;

/**
 * @author SBUSER
 *
 */
public class FindBothSourceFiles /* extends TestCase */ {

	public FindBothSourceFiles(String name) {
//		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected void tearDown() throws Exception {
	}

	/**
	 * Compiler should find both parse files
	 * 
	 * Test method for {@link tripleo.elijah.Main#parseFile(java.lang.String, java.io.InputStream)}.
	 */
	public final void testParseFile() {
		List<String> args = List.of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
//		ErrSink eee = JMock.of(ErrSink.class);
		ErrSink eee = new StdErrSink();
		var c = new Compilation(eee, new IO() {
			public CharSource openRead(Path p) {
				record(FileOption.READ, p);
				return null;
			}
			public CharSink openWrite(Path p) {
				record(FileOption.WRITE, p);				
				return null;
			}
			private void record(FileOption read, Path p) {
				// TODO Auto-generated method stub
				Map<FileOption, File> options11 = new HashMap<FileOption, File>();
				options11.put(read, p.toFile());
			}
			// exists, delete, isType ....
		});
		c.feedCmdLine(args);
		
		//fail("Not yet implemented"); // TODO
		Assert.assertTrue(c.getIO().recordedRead(new File(".", "factdemo.elijah")));
		Assert.assertTrue(c.getIO().recordedRead(new File(new File(".", "main2"), "main2.elijah")));
//		fail("Not yet implemented"); // TODO
	}

	void main2_el(CompilerContext cctx, GenBuffer gbn) {
		gbn.InitMod(cctx, "main2/main2.elijjah");
		ImportNode impn = new ImportNode("wprust.demo.fact");
		gbn.GenImportStmt(cctx, impn);
		
		
		
	}
	
	
	void factorial_r(CompilerContext cctx, GenBuffer gbn) {
		MethHdrNode mhn=new MethHdrNode(ExpressionBuilder.ident("u64"), "factorial_r", 
				List.of(new ArgumentNode("i","u64")));	 
		GenMethHdr(cctx, mhn, gbn);
		BeginMeth(cctx, mhn, gbn);
		
//		SwitchNode shn=new SwitchNode();
//		BeginSwitchStatement(cctx, shn, gbn);
		
		CaseHdrNode shn=new CaseHdrNode(ExpressionBuilder.varref("i"));
		BeginCaseStatement(cctx, shn, gbn);
		
		CaseChoiceNode csn=new CaseChoiceNode(ExpressionBuilder.integer(0));
		BeginCaseChoice(cctx, csn, gbn);
		
		ReturnAgnSimpleIntNode rasin=new ReturnAgnSimpleIntNode(ExpressionBuilder.integer(1)); 
		GenReturnAgnSimpleInt(cctx, rasin, gbn);
		
//		BreakInCaseStatementNode bicsn=new BreakInCaseStatementNode();
//		BreakInCaseStatement(cctx, bicsn, gbn);
		
		CloseCaseNode cccn1 = new CloseCaseNode(csn, ChoiceOptions.BREAK); 
		CloseCaseChoice(cctx, cccn1, gbn);

		CaseDefaultNode csn2 = new CaseDefaultNode(cctx, ExpressionBuilder.varref("n")); 
		BeginDefaultCaseStatement(cctx, csn2, gbn);
		
		TmpSSACtxNode tccssan = new TmpSSACtxNode();   
		LocalAgnTmpNode lamn=new LocalAgnTmpNode(tccssan, ExpressionBuilder.binex(
				ExpressionBuilder.varref("n"), ExpressionOperators.OP_MINUS, ExpressionBuilder.integer(1)));
		BeginTmpSSACtx(cctx, tccssan, gbn);
		
		TmpSSACtxNode tccssan2 = new TmpSSACtxNode();
		LocalAgnTmpNode latn2=new LocalAgnTmpNode(tccssan2, ExpressionBuilder.fncall(
				"factorial_r", List.of(lamn)));
		GenLocalAgn(cctx, latn2, gbn);
		
		TmpSSACtxNode tccssan3 = new TmpSSACtxNode();
		LocalAgnTmpNode latn3=new LocalAgnTmpNode(tccssan3, ExpressionBuilder.binex(
				ExpressionBuilder.varref("n"), ExpressionOperators.OP_MULT, tccssan2));
		GenLocalAgn(cctx, latn3, gbn);
		
//		ReturnAgnSimpleIntNode rsin=new ReturnAgnSimpleIntNode(latn3); // TODO Not a simple int
		ReturnAgnNode rsin=new ReturnAgnNode(latn3); // TODO Not an expression!!!
		GenReturnAgn(cctx, rsin, gbn);
		
		// TODO look at this
//		CloseTmpCtx(cctx, tccssan3, gbn);
//		CloseTmpCtx(cctx, tccssan2, gbn);
//		CloseTmpCtx(cctx, tccssan, gbn);
		CloseTmpCtx(cctx, latn3, gbn);
		CloseTmpCtx(cctx, latn2, gbn);
		CloseTmpCtx(cctx, lamn, gbn);

//		CloseCaseChoiceNode cccn2=new CloseCaseChoiceNode(csn2, ChoiceOptions.BREAK);
		CloseCaseNode cccn2=new CloseCaseNode(csn2, ChoiceOptions.BREAK);
		CloseCaseChoice(cctx, cccn2, gbn);
		
		EndCaseStatement(cctx, shn, gbn);
		EndMeth(cctx, mhn, gbn);

//		LocalAgnTmpNode latn1=new LocalAgnTmpNode (tccssan, ExpressionBuilder.bin_expr(
//		 ExpressionBuilder.varref(n), ExpressionOperators.OP_MINUS, ExpressionBuilder.integer(1)));

	}
	
	private void EndCaseStatement(CompilerContext cctx, CaseHdrNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.decr_i();
		buf.append_nl("} // close select "+node.getExpr().genText+"");
	}

	private void GenReturnAgn(CompilerContext cctx, ReturnAgnNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append("vsr =");
		buf.append(node.expr.genText);
		buf.append_ln(";");
		// if(node.usesRetKW()) {buf.append ("return vsr;");}
	}

	private void CloseTmpCtx(CompilerContext cctx, LocalAgnTmpNode lamn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append_cb (""); // close-brace
	}

	private void GenLocalAgn(CompilerContext cctx, LocalAgnTmpNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		if (node instanceof LocalAgnTmpNode) {
			BufferSequenceBuilder sb=new BufferSequenceBuilder(6).
					named("open").named("type").named("name").
					named("equality").named("value").semieol();
			sb.set("open", "{", XX.INDENT);
			sb.set("type", node.getRight().genType, XX.SPACE);
		}
	}

	private void BeginDefaultCaseStatement(CompilerContext cctx, CaseDefaultNode csn2, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void CloseCaseChoice(CompilerContext cctx, CloseCaseNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf = gbn.moduleBufHdr(cctx.module());
		buf.decr_i();
		buf.append_nl("} // close select ("+node.hdr_node.left.genName+")"); // TODO left was expr
	}

	private void BeginCaseChoice(CompilerContext cctx, CaseChoiceNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		boolean is_simple = node.left.is_const_expr();
		boolean is_default = node.left.is_underscore() ||
				(node.left.is_var_ref() && node.left.ref_==null);
		if (is_simple ){
			buf.append("case "+node.left.genText(cctx));
			buf.append_s(": ");	
		}
		if (is_default) {
			buf.append_nl("default:");
		}
	}

	private void BeginCaseChoice(CompilerContext cctx, CaseHdrNode csn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void EndMeth(CompilerContext cctx, MethHdrNode mhn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void CloseTmpCtx(CompilerContext cctx, CloseTmpCtxNode tmpctxn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void GenLocalValAgnFnCall(CompilerContext cctx, LocalValAgnFnCallNode lvafcn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void GenLocalAgnMath(CompilerContext cctx, LocalAgnMathNode lamn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void BeginTmpSSACtx(CompilerContext cctx, TmpSSACtxNode tccssan, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void BeginDefaultCaseStatement(CompilerContext cctx, CaseNode csn2, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		
	}

	private void BreakInCaseStatement(CompilerContext cctx, BreakInCaseStatementNode bicsn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append_ln("break; }");
	}

	private void GenReturnAgnSimpleInt(CompilerContext cctx, ReturnAgnSimpleIntNode rasin, GenBuffer gbn) {
		// TODO Auto-generated method stub
		Buffer buf=gbn.moduleBufImpl(cctx.module());
		buf.append("vsr = ");
		buf.append(((Integer)rasin.getValue()).toString());
		buf.append_nl(")");
	}

	private void BeginCaseStatement(CompilerContext cctx, CaseHdrNode node, GenBuffer gbn) {
		Buffer buf = gbn.moduleBufHdr(cctx.module());
		boolean is_simple = node.getExpr().is_simple();
		buf.append_s("switch (");
		if (is_simple) {
			buf.append(node.getExpr().genText(cctx));
		} else {
			// TODO implement complex part
		}
		buf.append_nl_i(") {");
	}

	public class Transform1 implements Transform {
		/* (non-Javadoc)
		 * @see tripleo.util.buffer.Transform#transform(tripleo.elijah.gen.nodes.ArgumentNode, tripleo.util.buffer.DefaultBuffer)
		 */
		@Override
		public void transform(ArgumentNode na, DefaultBuffer bufbldr) {
			bufbldr.append_s(na.genType, XX.SPACE);
			bufbldr.append(na.genName);
		}
	}

	private void BeginMeth(CompilerContext cctx, MethHdrNode node, GenBuffer gbn) {
		// TODO Auto-generated method stub
//		Buffer buf=gbn.moduleBufImpl(cctx.module());
		BufferSequenceBuilder sb = new BufferSequenceBuilder(4).
				named("type").named("name").named("args").semieol();
		sb.set("type", node.returnType.genType, XX.SPACE);
		sb.set("name", node.methName.genName);
		EnclosedBuffer sb2 = new EnclosedBuffer("(", XX.RPAREN);
		Transform1 transform1 = new Transform1();
		BufferSequenceBuilder sb3 = new BufferSequenceBuilder(node.argCount,
				node.ArgumentsIterator(), transform1, XX.COMMA, gbn);
		sb2.setPayload(sb3);
		sb.set("args", sb2);
		CodeGen gbm = gbn.getCodeGen(); // TODO should be CSimpleGen
		gbm.appendHeader(cctx.module(), sb.build());
	}

	public void GenMethHdr(CompilerContext cctx, MethHdrNode node, GenBuffer gbn) {
		Buffer buf = gbn.moduleBufHdr(cctx.module());
		buf.append_s(node.returnType.genType);
		buf.append(node.methName.genName);
		buf.append("(");
		for (int c=0;c<node.argCount;c++) {
			buf.append_s(node.argument(c).genType);
			buf.append(node.argument(c).genName);
			if (c<node.argCount-1) {buf.append(",");}
		}
		buf.append(");");
	}
}
	
//
//
//
