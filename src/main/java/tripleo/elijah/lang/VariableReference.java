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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak:
//			AbstractExpression, ProcedureCall, IExpression

public class VariableReference extends AbstractBinaryExpression implements OS_Expression {

	/** Called from ElijahParser.variableReference.  Will `setMain' later */
	public VariableReference() {
		//NotImplementedException.raise();
	}

	public VariableReference(String m) {
		NotImplementedException.raise();
		setMain(m);
	}

	public void addArrayPart(IExpression p) {
//		System.out.println("~~ VarRef addArrayPart");
		NotImplementedException.raise();
		parts.add(new VR_ArrayPart(p));
	}

	public void addIdentPart(String s) {
//		System.out.println((new StringBuilder("~~ VarRef addIdentPart ("))
//				.append(s).append(")").toString());
		NotImplementedException.raise();
		parts.add(new VR_IdentPart(s));
	}

	public void addProcCallPart(ProcedureCall p) {
//		System.out.println("~~ VarRef addProcCallPart");
		NotImplementedException.raise();
		parts.add(new VR_ProcCallPart(p));
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		tos.put_string_ln(
				(new StringBuilder("VariableReference { name = \""))
					.append(main)
					.append("\"}")
					.toString());
	}

	@Override
	public String repr_() {
		return (new StringBuilder("VariableReference (")).append(main).append(
				")").toString();
	}

	public void setMain(String s) {
		main = s;
		System.out.println(repr_());
	}

	@Override
	public String toString() {
		return repr_();
	}

	String main;

	public void addColonIdentPart(String aText) {
//		StringBuilder ss = new StringBuilder("~~ VarRef addColonIdentPart (");
//		ss.append(aText);
//		ss.append(")");
//		System.out.println(ss.toString());
		NotImplementedException.raise();
		parts.add(new VR_ColonIdentPart(aText));
	}
	
	List<VR_Parts> parts = new ArrayList<VR_Parts>();
	
	interface VR_Parts {
		
	}

	class VR_ArrayPart implements VR_Parts {

		@NonNull
		private IExpression p;

		public VR_ArrayPart(IExpression p) {
			// TODO Auto-generated constructor stub
			NotImplementedException.raise();
			this.p=p;
		}
		
	}

	class VR_ProcCallPart implements VR_Parts {

		@NonNull 
		private ProcedureCall p;

		public VR_ProcCallPart(ProcedureCall p) {
			// TODO Auto-generated constructor stub
			NotImplementedException.raise();
			this.p=p;
		}
		
	}

	class VR_IdentPart implements VR_Parts {

		@NonNull 
		private String s;

		public VR_IdentPart(String s) {
			// TODO Auto-generated constructor stub
			NotImplementedException.raise();
			this.s=s;
		}
		
	}
	
	class VR_ColonIdentPart implements VR_Parts {

		@NonNull
		private String text;

		public VR_ColonIdentPart(String aText) {
			// TODO Auto-generated constructor stub
			NotImplementedException.raise();
			this.text=aText;
		}
		
	}

	public void addProcCallPart(ProcedureCallExpression pce1) {
		// TODO Auto-generated method stub
//		NotImplementedException.raise();
		parts.add(new VR_ProcCallPart(null));
	}

	public ProcedureCallExpression procCallPart() {
		// TODO Auto-generated method stub
//		NotImplementedException.raise();
		return new ProcedureCallExpression();
	}
}


//
//
//
