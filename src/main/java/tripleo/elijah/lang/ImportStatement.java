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

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

/**
 * 
 */
public class ImportStatement implements ModuleItem {
	final OS_Element parent;
	/** Used in from syntax
	 * @category from
	 */
	private Qualident root;
	private QualidentList importList;

	public ImportStatement(OS_Element aParent) {
		parent = aParent;
		if (parent instanceof OS_Module)
			((OS_Module) parent).add(this);
		else
			throw new NotImplementedException();
		//
		importList=new QualidentList();
	}

	/** Used in from syntax
	 * @category from
	 */
	public void importRoot(Qualident xyz) {
		root = xyz;
	}

	public QualidentList importList() {
		return importList;
	}

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
