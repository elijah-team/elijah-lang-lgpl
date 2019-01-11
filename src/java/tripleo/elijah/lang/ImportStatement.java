package tripleo.elijah.lang;

import java.io.IOException;


import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.*;

// Referenced classes of package pak2:
//			IdentList

public class ImportStatement implements ModuleItem {

	final OS_Module parent;

	public ImportStatement(OS_Module aParent) {
		parent = aParent;
		parent.add(this);
	}

	public void importRoot(String aRoot) {
		root = aRoot;
	}

	public IdentList importList() {
		assert importList==null; // TODO??
		importList=new IdentList();
		return importList;
	}

	private String root;

	private IdentList importList;

	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public void visitGen(JavaCodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		
	}
}


