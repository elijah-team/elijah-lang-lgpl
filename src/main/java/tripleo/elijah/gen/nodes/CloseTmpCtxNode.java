/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.CompilerContext;

/**
 * @author SBUSER
 *
 */
public class CloseTmpCtxNode {
	
	CompilerContext cctx;
	TmpSSACtxNode   tmpNode;
	
	public CloseTmpCtxNode(CompilerContext cctx, TmpSSACtxNode tmpNode) {
		this.cctx = cctx;
		this.tmpNode = tmpNode;
	}
}
