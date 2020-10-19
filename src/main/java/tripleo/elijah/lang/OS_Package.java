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

import java.util.ArrayList;
import java.util.List;

/*
 * Created on 5/3/2019 at 21:41
 *
 * $Id$
 *
 */
public class OS_Package implements OS_Element {
	public final static OS_Package default_package = new OS_Package(null, 0);
	private OS_Module _module;
	private final List<OS_Element> elements = new ArrayList<OS_Element>();

	int _code;
	Qualident _name;

	@Override
	public void visitGen(final ICodeGen visit) {
		throw new NotImplementedException();
	}

	// TODO packages, elements

	public OS_Package(final Qualident aName, final int aCode) {
		_code = aCode;
		_name = aName;
	}

	public OS_Package(final Qualident aName, final int aCode, final OS_Module module) {
		_code = aCode;
		_name = aName;
		_module = module;
	}

	@Override
	public OS_Element getParent() {
		return _module;
	}

	@Override
	public Context getContext() {
		// TODO Do something with PackageContext ??
		return null; //_a._context;
	}

	//
	// ELEMENTS
	//

	public void addElement(final OS_Element element) {
		elements.add(element);
	}

	public List<OS_Element> getElements() {
		return elements;
	}

	//
	// NAME
	//

	public String getName() {
		if (_name == null) {
			System.err.println("*** name is null for package");
			return "";
		}
		return _name.toString();
	}
}

//
//
//
