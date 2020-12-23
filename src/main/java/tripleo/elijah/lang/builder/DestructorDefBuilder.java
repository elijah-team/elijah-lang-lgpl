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
 * Created 12/23/20 12:13 AM
 */
public class DestructorDefBuilder extends ElBuilder {
	private Context _context;
	private ClassStatement _parent;
	private FormalArgList _fal;
	private DestructorDefScope _scope = new DestructorDefScope();

	@Override
	protected DestructorDef build() {
		DestructorDef destructorDef = new DestructorDef(_parent, _context);
		destructorDef.setFal(_fal);
		for (ElBuilder item : _scope.items()) {
			item.setParent(destructorDef);
			item.setContext(_context);
			destructorDef.add(item.build());
		}
		destructorDef.postConstruct();
		return destructorDef;
	}

	@Override
	protected void setContext(Context context) {
		_context = context;
	}

	public void fal(FormalArgList fal) {
		_fal = fal;
	}

	public DestructorDefScope scope() {
		return _scope;
	}
}

//
//
//
