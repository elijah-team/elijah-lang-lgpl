/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

// Referenced classes of package pak:
//			TypeRef, IExpression

public class VariableStatement implements OS_Element {

	private final VariableSequence _parent;

	private TypeName typeName = new VariableTypeName();
	private IExpression initialValue = IExpression.UNASSIGNED;
	private IdentExpression name;
	private TypeModifiers typeModifiers;

	public VariableStatement(final VariableSequence aSequence) {
		_parent = aSequence;
	}

	public String getName() {
		return name.getText();
	}

	public IdentExpression getNameToken() {
		return name;
	}

	public void setName(final IdentExpression s) {
		name = s;
	}

	public void initial(final IExpression aExpr) {
		initialValue=aExpr;
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

	public void set(final TypeModifiers y) {
		typeModifiers = y;
	}

	public TypeName typeName() {
		return typeName;
	}
	
	public IExpression initialValue() {
		return initialValue;
	}
	
/*	public String initialValueType() {
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
//		else if (initialValue instanceof OS_Integer)
//			return "int";
		else if (initialValue instanceof ListExpression)
			return "void*"; // TODO
		
		else
			return "Z0*";
	}*/

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		// TODO is this correct?
		return getParent().getContext();
	}

	public void setTypeName(final TypeName tn) {
		typeName = tn;
	}

	public TypeModifiers getTypeModifiers() {
		return typeModifiers;
	}
}

//
//
//
