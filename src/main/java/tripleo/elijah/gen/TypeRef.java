/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/*
 * Created on 4/23/2019 at 03:44
 *
 * $Id$
 *
 */
package tripleo.elijah.gen;

public class TypeRef  implements Node {
	private final ModuleRef _module;
	private final RefElemenet _parent;
	private final String _name;
	private final int _code;
	
	public TypeRef(ModuleRef moduleRef, RefElemenet parent, String name, int code) {
		this._module = moduleRef;
		this._parent = parent;
		this._name   = name;
		this._code   = code;
	}

	public String genType() {
		return String.format("Z%d", _code);
	}
	
	@Override
	public int getCode() {
		return _code;
	}
	
	/**
	 * Returns the type name
	 *
	 * @see #genType()
	 *
	 * @return the type name as referenced in the code
	 */
	public String getName() {
		return _name;
	}
}
