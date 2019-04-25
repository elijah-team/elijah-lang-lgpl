package tripleo.elijah.gen.nodes;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.ModuleRef;
import tripleo.elijah.gen.TypeRef;

import java.util.List;

import static org.junit.Assert.*;

public class FindBothSourceFilesTest {
	
	@Test
	public void factorial_r() {
		ModuleRef prelude = new ModuleRef(null, -1);
		TypeRef u64     = new TypeRef(prelude, prelude, "u64", 81);
		ModuleRef main_m  = new ModuleRef("fact.elijah", -2);
		TypeRef   main_k  = new TypeRef(main_m, main_m, "Main", 100);
		
		
		final ArgumentNode argumentNode = new ArgumentNode("i", u64);
		Assert.assertEquals(argumentNode.getGenName(), "vai");
		
		MethHdrNode mhn=new MethHdrNode(u64, main_k, "factorial_r",
				List.of(argumentNode), 1000);
		Assert.assertEquals(mhn.genName(), "z100factorial_r");
	}
	
	@Test
	public void nMinusOne() {
		CompilerContext cctx = new CompilerContext("ll");
		ModuleRef prelude = new ModuleRef(null, -1);
		TypeRef   u64     = new TypeRef(prelude, prelude, "u64", 81);
		ModuleRef main_m  = new ModuleRef("fact.elijah", -2);
		TypeRef   main_k  = new TypeRef(main_m, main_m, "Main", 100);
		
		
		MethHdrNode mhn=new MethHdrNode(u64, main_k, "factorial_r",
				List.of(new ArgumentNode("i", u64)), 1000);
		CaseHdrNode shn=new CaseHdrNode(ExpressionNodeBuilder.varref("i", mhn, u64));


		TmpSSACtxNode tccssan = new TmpSSACtxNode(cctx);
		LocalAgnTmpNode lamn=new LocalAgnTmpNode(tccssan, ExpressionNodeBuilder.binex(
				ExpressionNodeBuilder.varref("n", shn, u64), ExpressionOperators.OP_MINUS, ExpressionNodeBuilder.integer(1)));
//		BeginTmpSSACtx(cctx, tccssan, gbn);
		
		String s = tccssan.getType().genText(cctx);
		Assert.assertEquals(s, "898989");
		
	}
}