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

import tripleo.elijah.Qualident;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

/**
 * 
 */
public class ImportStatement implements ModuleItem {
	final OS_Module parent;

	public ImportStatement(OS_Module aParent) {
		parent = aParent;
		parent.add(this);
		//
		importList=new QualidentList();
	}

	public void importRoot(Qualident xyz) {
		root = xyz;
	}

	public QualidentList importList() {
		//assert importList==null; // TODO??
		return importList;
	}

	private Qualident root;
	private QualidentList importList;

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}

//
//
//
