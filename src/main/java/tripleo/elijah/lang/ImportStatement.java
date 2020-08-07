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

import java.util.List;

public interface ImportStatement extends ModuleItem {

	@Override
	default public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public List<Qualident> parts();// {
//		return importList.parts;
//	}

//	public Iterable<? extends Qualident> getItemNames() {
//		List<Qualident> a = new ArrayList<Qualident>();
//		if (parts() == null) {
//			if (getRoot() != null) {
//				a.add(getRoot());
//			} else
//				throw new NotImplementedException();
//		} else {
//			for (Qualident part : parts()) {
//				System.err.println("2004 "+part);
//				a.add(part);
//			}
//		}
//		return a;
//	}
}

//
//
//
