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
import junit.framework.TestCase;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.MethHeaderNode;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.nodes.BreakInCaseStatementNode;
import tripleo.elijah.gen.nodes.CaseNode;
import tripleo.elijah.gen.nodes.CloseTmpCtxNode;
import tripleo.elijah.gen.nodes.ImportNode;
import tripleo.elijah.gen.nodes.LocalAgnMathNode;
import tripleo.elijah.gen.nodes.LocalAgnTmpNode;
import tripleo.elijah.gen.nodes.LocalValAgnFnCallNode;
import tripleo.elijah.gen.nodes.MethHdrNode;
import tripleo.elijah.gen.nodes.ReturnAgnSimpleIntNode;
import tripleo.elijah.gen.nodes.SwitchNode;
import tripleo.elijah.gen.nodes.TmpSSACtxNode;
import tripleo.elijah.lang.ExpressionBuilder;

/**
 * @author SBUSER
 *
 */
public class FindBothSourceFiles extends TestCase {

	public FindBothSourceFiles(String name) {
		super(name);
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
		List<String> args = List.of(".", "main2", "-sE");
		ErrSink eee = JMock.of(ErrSink.class);
		var c = new Compilation(eee, new IO() {
			public CharSource openRead(Path p) {
				record(FileOption.READ, p);				
			}
			public CharSink openWrite(Path p) {
				record(FileOption.WRITE, p);				
			}
			private void record(FileOption read, Path p) {
				// TODO Auto-generated method stub
				Map<FileOption, File> options11 = new HashMap();
				options11.put(read, p.toFile());
			}
			// exists, delete, isType ....
		});
		c.feedCmdLine(args);
		
		//fail("Not yet implemented"); // TODO
		Assert.isTrue(c.io.recordedRead(new File(".", "factdemo.elijah")));
		Assert.isTrue(c.io.recordedRead(new File(new File(".", "main2"), "main2.elijah")));
//		fail("Not yet implemented"); // TODO
	}

	void main2_el(CompilerContext cctx, GenBuffer gbn) {
		gbn.InitMod(cctx, "main2/main2.elijjah");
		ImportNode impn = new ImportNode("wprust.demo.fact");
		gbn.GenImportStmt(cctx, impn);
		
		
		
	}
	
	void factorial_r(CompilerContext cctx, GenBuffer gbn) {
		MethHdrNode mhn=new MethHdrNode(); // todo	 
		GenMethHdr(cctx, mhn, gbn);
		BeginMeth(cctx, mhn, gbn);
		
		SwitchNode shn=new SwitchNode();
		BeginSwitchStatement(cctx, shn, gbn);
		
		CaseNode csn=new CaseNode();
		BeginCaseStatement(cctx, csn, gbn);
		
		ReturnAgnSimpleIntNode rasin=new ReturnAgnSimpleIntNode(); 
		GenReturnAgnSimpleInt(cctx, rasin, gbn);
		
		BreakInCaseStatementNode bicsn=new BreakInCaseStatementNode();
		BreakInCaseStatement(cctx, bicsn, gbn);
		
		CaseNode csn2 = new CaseNode(); 
		BeginDefaultCaseStatement(cctx, csn2, gbn);
		
		TmpSSACtxNode tccssan = new TmpSSACtxNode();   
		BeginTmpSSACtx(cctx, tccssan, gbn);
		
		LocalAgnMathNode lamn=new LocalAgnMathNode();
		GenLocalAgnMath(cctx, lamn, gbn);
		
		LocalValAgnFnCallNode lvafcn = new LocalValAgnFnCallNode();
		GenLocalValAgnFnCall(cctx, lvafcn, gbn);
		
		LocalAgnMathNode lamn2=new LocalAgnMathNode();
		GenLocalAgnMath(cctx, lamn2, gbn);
		
		ReturnAgnSimpleIntNode rsin=new ReturnAgnSimpleIntNode(); 
		GenReturnAgnSimpleInt(cctx, rsin, gbn);
		
		CloseTmpCtxNode tmpctxn =  new CloseTmpCtxNode();
		CloseTmpCtx(cctx, tmpctxn, gbn);
		
		BreakInCaseStatementNode biccsn2 = new BreakInCaseStatementNode();
		BreakInCaseStatement(cctx, biccsn2, gbn);
		
		EndMeth(cctx, mhn, gbn);

		LocalAgnTmpNode latn1=new LocalAgnTmpNode (tccssan, ExpressionBuilder.bin_expr(
		 ExpressionBuilder.varref(n), OP_MINUS, ExpressionBuilder.integer(1)));

	}
	private void EndMeth(CompilerContext cctx, MethHdrNode mhn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void CloseTmpCtx(CompilerContext cctx, CloseTmpCtxNode tmpctxn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void GenLocalValAgnFnCall(CompilerContext cctx, LocalValAgnFnCallNode lvafcn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void GenLocalAgnMath(CompilerContext cctx, LocalAgnMathNode lamn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BeginTmpSSACtx(CompilerContext cctx, TmpSSACtxNode tccssan, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BeginDefaultCaseStatement(CompilerContext cctx, CaseNode csn2, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BreakInCaseStatement(CompilerContext cctx, BreakInCaseStatementNode bicsn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void GenReturnAgnSimpleInt(CompilerContext cctx, ReturnAgnSimpleIntNode rasin, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BeginCaseStatement(CompilerContext cctx, CaseNode csn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BeginSwitchStatement(CompilerContext cctx, SwitchNode shn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	private void BeginMeth(CompilerContext cctx, MethHdrNode mhn, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	public void GenMethHdr(CompilerContext cctx, MethHdrNode mhn, GenBuffer gbn) {
		
	}
}

enum FileOption  { READ, WRITE };

public class IO {

	public Object recordedRead(File file) {
		// TODO Auto-generated method stub
		return null;
	}}

//
//
//
