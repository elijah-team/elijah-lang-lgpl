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

import org.eclipse.jdt.annotation.NonNull;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.lang.OS_Integer;
import tripleo.elijah.lang.VariableReference;

public class CaseChoiceNode {
	
	private final VariableReference _varref;
	public ExpressionNode left;
	public ScopeNode right;
	private String _defaultName;
	
	public CaseChoiceNode(OS_Integer expr1) {
		// TODO Auto-generated constructor stub
		left=new ExpressionNode(expr1);
		right=null;
		_defaultName = null;
		_varref = null;
	}
	
	/**
	 * Used for defaults where there is a varname
	 * Also can be used for _, tho not designed at this point.
	 *
	 * @param cctx      unused
	 * @param varref    the name specified
	 */
	public CaseChoiceNode(CompilerContext cctx, VariableReference varref) {
		left  = null;
		right = null;
		setDefaultName(varref.getName());
		_varref = varref;
	}
	
	public String getDefaultName() {
		return _defaultName;
	}
	
	public void setDefaultName(@NonNull String _defaultName) {
		this._defaultName = _defaultName;
	}
	
	public CaseChoiceNode(ExpressionNode left, ScopeNode right) {
		super();
		this.right = right;
		this.left = left;
		_defaultName =null;
		_varref = null;
	}
	
	public boolean is_simple() {
		return _defaultName != null;
	}
	
	public boolean is_default() {
		return _defaultName != null && _defaultName != "_";
	}
	
	public VariableReferenceNode varref() {
		return _varref;
	}
}
