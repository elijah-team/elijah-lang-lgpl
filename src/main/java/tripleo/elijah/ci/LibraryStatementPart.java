package tripleo.elijah.ci;

import antlr.Token;
import tripleo.elijah.lang.IExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/6/20 12:06 PM
 */
public class LibraryStatementPart {
	private String name;
	private String dirName;
	private List<Directive> dirs = new ArrayList<Directive>();

	public void setName(Token i1) {
		name = i1.getText();
	}

	public void setDirName(Token dirName) {
		this.dirName = dirName.getText();
	}

	public String getName() {
		return name;
	}

	public String getDirName() {
		return dirName;
	}

	public void addDirective(Token token, IExpression iExpression) {
		dirs.add(new Directive(token, iExpression));
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
