package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			AbstractExpression, ProcedureCall, IExpression

public class VariableReference extends AbstractExpression {

	public VariableReference() {
	}

	public VariableReference(String m) {
		setMain(m);
	}

	public void addArrayPart(IExpression p) {
		System.out.println("~~ VarRef addArrayPart");
	}

	public void addIdentPart(String s) {
		System.out.println((new StringBuilder("~~ VarRef addIdentPart ("))
				.append(s).append(")").toString());
	}

	public void addProcCallPart(ProcedureCall p) {
		System.out.println("~~ VarRef addProcCallPart");
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		tos.put_string_ln((new StringBuilder("VariableReference { name = \""))
				.append(main).append("\"}").toString());
	}

	public String repr_() {
		return (new StringBuilder("VariableReference (")).append(main).append(
				")").toString();
	}

	public void setMain(String s) {
		main = s;
		System.out.println(repr_());
	}

	public String toString() {
		return repr_();
	}

	String main;

	public void addColonIdentPart(String aText) {
		System.out.println((new StringBuilder("~~ VarRef addColonIdentPart ("))
				.append(aText).append(")").toString());
	}
}
