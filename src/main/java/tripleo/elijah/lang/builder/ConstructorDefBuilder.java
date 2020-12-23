/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang.builder;

import tripleo.elijah.lang.*;

/**
 * Created 12/22/20 10:55 PM
 */
public class ConstructorDefBuilder extends BaseFunctionDefBuilder {
//	private IdentExpression _name;
	private FormalArgList _fal;
	private ConstructorDefScope _scope = new ConstructorDefScope();
	private ClassStatement _parent;
	private Context _context;

	public ConstructorDefScope scope() {
		return _scope;
	}

	@Override
	public ConstructorDef build() {
		ConstructorDef cd = new ConstructorDef(_name, _parent, _context);
		cd.setName(_name);
		cd.setFal(_fal);
		for (ElBuilder item : _scope.items()) {
			item.setParent(cd);
			item.setContext(_context);
			cd.add(item.build());
		}
		cd.postConstruct();
		return cd;
	}

	@Override
	protected void setContext(Context context) {
		_context = context;
	}
}

//
//
//
