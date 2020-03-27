/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import antlr.Token;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			TypeRef, IExpression

public class VariableStatement {

	private final VariableSequence parent;
	public String name;
	
	private IExpression initialValue = IExpression.UNASSIGNED;
	private TypeModifiers type;
	private TypeName typeName = new VariableTypeName();

	public VariableStatement(VariableSequence aSequence) {
		parent = aSequence;
	}

	public String getName() {
		return name;
	}
	
	public void setName(Token s) {
		name = s.getText();
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
/*
		if (typeRef != null) {
			tos.incr_tabs();
			tos.put_string_ln("type = {");
			tos.dec_tabs();
			typeRef.print_osi(tos);
			tos.dec_tabs();
			tos.put_string_ln("} // type = ...");
		}
*/
		tos.dec_tabs();
		tos.put_string_ln("} // VariableDeclaration");
	}

/*
	public void printDeclare() {
		System.out.print("** Declare Variable: ");
		System.out.print(name);
		System.out.print(" as ");
//		System.out.print(getTypeString());
		System.out.println(" (agn not shown at all) **");
	}
*/

	public void set(TypeModifiers y) {
		type = y;
	}

	public TypeName typeName() {
		return typeName;
	}
	
	public IExpression initialValue() {
		return initialValue;
	}
	
	public String initialValueType() {
		if (initialValue instanceof NumericExpression)
			return "int";
		else if (initialValue instanceof ProcedureCallExpression)
			return ((ProcedureCallExpression) initialValue).getReturnTypeString();
		else if (initialValue instanceof CharLitExpression)
			return "char";
		else if (initialValue instanceof StringExpression)
			return "char*";
		else if (initialValue instanceof IdentExpression)
//			return "Z"+((IdentExpression)initialValue).getText();
			return "---------------10";
		else if (initialValue instanceof Qualident)
			return "---------------8";
//		else if (initialValue instanceof AbstractExpression)
//			return "---------------9";
		else if (initialValue instanceof VariableReference)
			return "---------------11";
		else if (initialValue instanceof OS_Integer)
			return "int";
		else if (initialValue instanceof ExpressionWrapper)
			return "---------------12";
		else if (initialValue instanceof ListExpression)
			return "void*"; // TODO
		
		else
			return "Z0*";
	}

}

//
//
//
