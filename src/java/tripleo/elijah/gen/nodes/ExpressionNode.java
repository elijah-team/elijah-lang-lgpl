/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.OS_Integer;

/**
 * @author olu
 *
 */
public class ExpressionNode {

	public String genName;

	public ExpressionNode(OS_Integer expr1) {
		// TODO Auto-generated constructor stub
		// TODO should  be interface
		genName=expr1.toString(); // TODO likely wrong
	}

}
