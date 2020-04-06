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
import java.util.Collection;
import java.util.List;
//import java.util.stream.Collectors;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.TabbedOutputStream;


// Referenced classes of package pak2:
//			BlockMember

public class VariableSequence implements BlockMember, StatementItem, FunctionItem, ClassItem {

	public VariableSequence() {
		stmts = new ArrayList<VariableStatement>();
	}

	private TypeModifiers def;

	public void defaultModifiers(TypeModifiers aModifiers) {def=aModifiers;}

	public VariableStatement next() {
		VariableStatement st = new VariableStatement(this);
		st.set(def);
		stmts.add(st);
		return st;
	}

	List<VariableStatement> stmts;

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
//		NotImplementedException.raise();
		//
		aTos.incr_tabs();
		aTos.put_string_ln("var");
		for (VariableStatement stmt: stmts) {
			stmt.print_osi(aTos);
		}
		aTos.dec_tabs();
	}

	public Collection<VariableStatement> items() {
		return stmts;
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OS_Element getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public String toString() {
		List<String> r = new ArrayList<String>();
		for (VariableStatement stmt : stmts) {
			r.add(stmt.getName());
		}
		return r.toString();
//		return (stmts.stream().map(n -> n.getName()).collect(Collectors.toList())).toString();
	}
}

