/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;
import tripleo.elijah.stages.instructions.ProcIA;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static tripleo.elijah.stages.deduce.DeduceTypes2.to_int;

/**
 * Created 3/7/21 1:22 AM
 */
public class CtorReference {

	private List<String> args;
	List<CReference.Reference> refs = new ArrayList<CReference.Reference>();

	void addRef(String text, CReference.Ref type) {
		refs.add(new CReference.Reference(text, type));
	}

	public void getConstructorPath(InstructionArgument ia2, GeneratedFunction gf) {
		final List<InstructionArgument> s = CReference._getIdentIAPathList(ia2, gf);
		int y=2;
//		String text = "";
//		List<String> sl = new ArrayList<String>();
		for (int i = 0, sSize = s.size(); i < sSize; i++) {
			InstructionArgument ia = s.get(i);
			if (ia instanceof IntegerIA) {
				// should only be the first element if at all
				assert i == 0;
				final VariableTableEntry vte = gf.getVarTableEntry(to_int(ia));
				addRef(vte.getName(), CReference.Ref.LOCAL);
			} else if (ia instanceof IdentIA) {
				final IdentTableEntry idte = gf.getIdentTableEntry(to_int(ia));
				OS_Element resolved_element = idte.resolved_element;
				if (resolved_element != null) {
					int yy=2;
					if (resolved_element instanceof VariableStatement) {

					}
				}
			} else if (ia instanceof ProcIA) {
//				final ProcTableEntry prte = generatedFunction.getProcTableEntry(to_int(ia));
//				text = (prte.expression.getLeft()).toString();
////				assert i == sSize-1;
//				addRef(text, Ref.FUNCTION); // TODO needs to use name of resolved function
				throw new NotImplementedException();
			} else {
				throw new NotImplementedException();
			}
//			sl.add(text);
		}
	}

	public String build(ClassInvocation aClsinv) {
		StringBuilder sb = new StringBuilder();
		boolean open = false, needs_comma = false;
//		List<String> sl = new ArrayList<String>();
		String text = "";
		for (CReference.Reference ref : refs) {
			switch (ref.type) {
				case LOCAL:
					text = "vv" + ref.text;
					sb.append(text);
					break;
				case MEMBER:
					text = "->vm" + ref.text;
					sb.append(text);
					break;
				case INLINE_MEMBER:
					text = Emit.emit("/*219*/")+".vm" + ref.text;
					sb.append(text);
					break;
				case DIRECT_MEMBER:
					text = Emit.emit("/*124*/")+"vsc->vm" + ref.text;
					sb.append(text);
					break;
				case FUNCTION: {
					final String s = sb.toString();
					text = String.format("%s(%s", ref.text, s);
					sb = new StringBuilder();
					open = true;
					if (!s.equals("")) needs_comma = true;
					sb.append(text);
					break;
				}
				case CONSTRUCTOR: {
					final String s = sb.toString();
					text = String.format("%s(%s", ref.text, s);
					sb = new StringBuilder();
					open = true;
					if (!s.equals("")) needs_comma = true;
					sb.append(text);
					break;
				}
				case PROPERTY: {
					final String s = sb.toString();
					text = String.format("%s(%s", ref.text, s);
					sb = new StringBuilder();
					open = true;
					if (!s.equals("")) needs_comma = true;
					sb.append(text);
					break;
				}
				default:
					throw new IllegalStateException("Unexpected value: " + ref.type);
			}
//			sl.add(text);
		}
		{
			// Assuming constructor call
			int code = aClsinv.getKlass()._a.getCode();
			if (code == 0) {
				System.err.println("** 32135 ClassStatement with 0 code " + aClsinv.getKlass());
			}
			String text2 = String.format("ZC%d", code); // TODO what about named constructors
			sb.append(" = ");
			sb.append(text2);
			sb.append("(");
			assert open == false;
			open = true;
		}
//		return Helpers.String_join("->", sl);
		if (needs_comma && args != null && args.size() > 0)
			sb.append(", ");
		if (open) {
			if (args != null) {
				sb.append(Helpers.String_join(", ", args));
			}
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * Call before you call build
	 *
	 * @param sl3
	 */
	public void args(List<String> sl3) {
		args = sl3;
	}
}

//
//
//
