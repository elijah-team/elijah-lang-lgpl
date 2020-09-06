package tripleo.elijah.ci;

import antlr.Token;
import tripleo.elijah.lang.IExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/6/20 12:04 PM
 */
public class GenerateStatement {
	private final List<Directive> dirs = new ArrayList<Directive>();

	public void addDirective(Token token, IExpression expression) {
		dirs.add(new Directive(token, expression));
	}

	public class Directive {

		private final IExpression expression;
		private final String name;

		public Directive(Token token_, IExpression expression_) {
			name = token_.getText();
			expression = expression_;
		}
	}
}

//
//
//
