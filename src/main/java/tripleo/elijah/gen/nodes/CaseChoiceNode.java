/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/*
 * Created on Sep 2, 2005 2:28:42 PM
 *
 * $Id$
 *
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.OS_Integer;

public class CaseChoiceNode {

	public ExpressionNode left;
	public ScopeNode right;

	public CaseChoiceNode(OS_Integer expr1) {
		// TODO Auto-generated constructor stub
		left=new ExpressionNode(expr1);
		right=null;
	}

	public CaseChoiceNode(ExpressionNode left, ScopeNode right) {
		super();
		this.right = right;
		this.left = left;
	}

}
