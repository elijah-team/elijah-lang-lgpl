package tripleo.elijah.lang;

import java.io.IOException;


import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			TypeRef, IExpression

public class VariableStatement {

	public VariableStatement(VariableSequence aSequence) {
		parent = aSequence;
	}

	public String getName() {
		return name;
	}

	public String getTypeString() {
		return typeRef.getTypeString();
	}

	public void initial(IExpression aExpr) {
		initialValue=aExpr;
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		tos.incr_tabs();
		tos.put_string_ln("VariableDeclaration {");
		tos.put_string("name = \"");
		tos.put_string(getName());
		tos.put_string_ln("\"");
		if (typeRef != null) {
			tos.incr_tabs();
			tos.put_string_ln("type = {");
			tos.dec_tabs();
			typeRef.print_osi(tos);
			tos.dec_tabs();
			tos.put_string_ln("} // type = ...");
		}
		tos.put_string_ln("} // VariableDeclaration");
	}

	public void printDeclare() {
		System.out.print("** Declare Variable: ");
		System.out.print(name);
		System.out.print(" as ");
		System.out.print(getTypeString());
		System.out.println(" (agn not shown at all) **");
	}

	public void set(TypeModifiers y) {
		type = y;
	}

	public void setInitialValue(IExpression e) {
		initialValue = e;
	}

	public void setName(String s) {
		name = s;
	}

	public void setTypeObject(TypeRef t) {
		typeRef = t;
	}

	public TypeName typeName() {
		return typeName;
	}

	IExpression initialValue = IExpression.UNASSIGNED;
	public String name;
	private final VariableSequence parent;
	TypeModifiers type;
	TypeName typeName = new VariableTypeName();
	TypeRef typeRef;

}
