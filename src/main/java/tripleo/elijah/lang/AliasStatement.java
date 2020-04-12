package tripleo.elijah.lang;

import antlr.Token;

public class AliasStatement {
    private final OS_Element parent;
	private IExpression expr;
	private String name;

    public AliasStatement(OS_Element aParent) {
        this.parent = aParent;
    }

	public void setExpression(IExpression expr) {
		this.expr = expr;
	}

	public void setName(Token i1) {
		this.name = i1.getText();
	}
}

//
//
//
