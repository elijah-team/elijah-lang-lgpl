package tripleo.elijah.lang;

import java.io.IOException;
import java.util.*;

import tripleo.elijah.util.*;


// Referenced classes of package pak2:
//			BlockMember

public class VariableSequence implements BlockMember, StatementItem, FunctionItem {

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

	public void print_osi(TabbedOutputStream aTos) throws IOException {
		NotImplementedException.raise();
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
}

