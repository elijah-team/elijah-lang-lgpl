/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.util.NotImplementedException;

public class VariableReferenceNode2 extends ExpressionNode {
	private final String _type;
	private  String _declared;
	private  boolean _perm;
	
	public VariableReferenceNode2(String declared, String t, boolean b) {
		super();
		this._declared = declared;
		this._perm = b;
		this._type = t;
//		final Token ct = new CommonToken();
//		ct.setText(declared);
//		setText(new IdentExpression(ct)); // TODO why call this?
	}
	
	@Override
	public String genText(CompilerContext cctx) {
		if (getExpr()/*iex*/ == null) {
			return _declared;
		} else {
			NotImplementedException.raise();
//			return super.genText(cctx);
		}
		return super.genText(cctx);
	}
	
	private void setText(IdentExpression identExpression) {
		//NotImplementedException.raise();
		_declared = identExpression.getText();
		_perm = true;
	}
	
//	public TypeNameNode getType() {
//		NotImplementedException.raise();
//		return null;
//	}
	
	public String genText() {
		if (_perm) return _declared;
		NotImplementedException.raise();
		return "vtn"; // TODO hardcoded
	}
}

//
//
//
