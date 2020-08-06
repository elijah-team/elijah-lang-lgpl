/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

public class AliasStatement implements ModuleItem, ClassItem, FunctionItem, OS_Element2 {
    private final OS_Element parent;
	private IExpression expr;
	private String name;

    public AliasStatement(OS_Element aParent) {
        this.parent = aParent;
    }

	public void setExpression(IExpression expr) {
		if (expr.getKind() != ExpressionKind.IDENT &&
			expr.getKind() != ExpressionKind.QIDENT &&
			expr.getKind() != ExpressionKind.DOT_EXP) // TODO need DOT_EXP to QIDENT
		{
			throw new NotImplementedException();
//			System.out.println(String.format("[AliasStatement#setExpression] %s %s", expr, expr.getKind()));
		}
		this.expr = expr;
	}

	public void setName(@NotNull Token i1) {
		this.name = i1.getText();
	}

	@Override // OS_Element
	public void visitGen(ICodeGen visit) {
    	throw new NotImplementedException();
	}

	@Override // OS_Element
	public OS_Element getParent() {
		return this.parent;
	}

	@Override // OS_Element
	public @NotNull Context getContext() {
		throw new NotImplementedException();
//		return null;
	}

	@Override // OS_Element2
	public String name() {
		return this.name;
	}
}

//
//
//
