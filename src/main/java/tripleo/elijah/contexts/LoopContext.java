/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah.contexts;

import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.Loop;

import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:40:43 PM
 */
public class LoopContext extends Context {

	private final Loop carrier;
	private final Context _parent;

	public LoopContext(Context cur, Loop loop) {
		carrier = loop;
		_parent = cur;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one) {
		alreadySearched.add(carrier.getContext());

		if (carrier.getIterName() != null) {
			if (name.equals(carrier.getIterName())) { // reversed to prevent NPEs
				String iterName = carrier.getIterName();
				IdentExpression ie = new IdentExpression(Helpers.makeToken(iterName));
				Result.add(name, level, ie, this); // TODO getIterNameToken
			}
		}

		if (carrier.getParent() != null) {
			final Context context = getParent();
			if (!alreadySearched.contains(context) || !one)
				context.lookup(name, level + 1, Result, alreadySearched, false); // TODO test this
		}
		return Result;
		
	}

	@Override
	public Context getParent() {
		return _parent;
	}

}

//
//
//
