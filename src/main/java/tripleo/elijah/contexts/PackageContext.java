/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.LookupResultList;

import java.util.List;

/**
 * Created 8/15/20 6:32 PM
 */
public class PackageContext extends Context {
	private final Context _parent;

	public PackageContext(final Context aParent) {
		_parent = aParent;
	}

	@Override
	public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		// TODO since we are not maintaining an item list, pass to parent
		// TODO implement me
		alreadySearched.add(this);
		return getParent().lookup(name, level, Result, alreadySearched, one);
	}

	@Override
	public Context getParent() {
		return _parent;
	}
}

//
//
//
