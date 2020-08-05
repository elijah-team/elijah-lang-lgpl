/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.List;

/**
 * 
 */
public class ImportStatement implements ModuleItem {
	final OS_Element parent;
	/** Used in from syntax
	 * @category from
	 */
	private Qualident root;
	private QualidentList importList = new QualidentList();

	public ImportStatement(OS_Element aParent) {
		parent = aParent;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else
			throw new NotImplementedException();
	}

	/** Used in from syntax
	 * @category from
	 */
	public void importRoot(Qualident xyz) {
		setRoot(xyz);
	}

	public QualidentList importList() {
		return importList;
	}

	@Override
	public void print_osi(TabbedOutputStream aTos) throws IOException {
		// TODO Auto-generated method stub
//		throw new NotImplementedException();
		aTos.put_string(String.format("%s %s", getRoot(), importList.toString()));
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	public void addAssigningPart(Token aToken, Qualident aQualident) {
		throw new NotImplementedException();
	}
	
	public IdentList addSelectivePart(Qualident aQualident) {
		throw new NotImplementedException();
//		return null;
	}
	
	public void addNormalPart(Qualident aQualident) {
//		throw new NotImplementedException();
		importList.add(aQualident);		
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Qualident getRoot() {
		return root;
	}

	public void setRoot(Qualident root) {
		this.root = root;
	}

	public List<Qualident> parts() {
		return importList.parts;
	}

	
}

//
//
//
