/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.*;

import java.util.List;

/**
 * Created 8/21/20 3:16 AM
 */
public class IfConditionalContext extends Context {
	private final Context _parent;
	private final IfConditional carrier;
	private final Context _prev_ctx;

	public IfConditionalContext(Context cur, IfConditional ifConditional) {
		_parent = cur;
		carrier = ifConditional;
		_prev_ctx = null; // TOP if statement
	}

	public IfConditionalContext(Context ctx, IfConditional ifConditional, boolean _ignored) {
		_prev_ctx = ctx;
		_parent = ((IfConditionalContext)ctx)._parent;
		carrier = ifConditional;
	}

	@Override
	public LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one) {
		alreadySearched.add(carrier.getContext());
		for (OS_Element/*StatementItem*/ item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
			    !(item instanceof NamespaceStatement) &&
			    !(item instanceof FunctionDef) &&
			    !(item instanceof VariableSequence) &&
			    !(item instanceof AliasStatement)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item, this);
				}
			}
			if (item instanceof VariableSequence) {
				System.out.println("1102 "+item);
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs, this);
				}
			}
		}
		if (getParent() != null) {
			final Context context = getParent();
			if (!alreadySearched.contains(context) || !one)
				return context.lookup(name, level + 1, Result, alreadySearched, false);
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
