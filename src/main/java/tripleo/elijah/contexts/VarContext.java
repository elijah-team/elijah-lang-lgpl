/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;

import java.util.List;

/**
 * Created 8/30/20 1:39 PM
 */
public class VarContext extends Context {

	private final VariableSequence carrier;
	private final Context _parent;

	public VarContext(VariableSequence carrier, Context _parent) {
		this.carrier = carrier;
		this._parent = _parent;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one) {
		alreadySearched.add(carrier.getContext());

		for (VariableStatement vs : carrier.items()) {
			if (vs.getName().equals(name)) {
				IdentExpression ie = new IdentExpression(Helpers.makeToken(vs.getName()));
				Result.add(name, level, ie); // TODO getNameToken
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
