/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/15/20 7:09 PM
 */
public class ImportContext extends Context {
	private final Context _parent;
	private final ImportStatement carrier;

	public ImportContext(final Context aParent, final ImportStatement imp) {
		_parent = aParent;
		carrier = imp;
	}

	@Override
	public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		alreadySearched.add(this);
//		System.err.println("2002 "+importStatement.importList());
		for (final Qualident importStatementItem : carrier.parts()) {
//			System.err.println("2005 "+importStatementItem);
			if (module().isPackage(importStatementItem.toString())) {
				final List<OS_Element> l = new ArrayList<>();
				final OS_Package aPackage = module().getPackage(importStatementItem);
//				LogEvent.logEvent(4003 , ""+aPackage.getElements());
				for (final OS_Element element : aPackage.getElements()) {
//					System.err.println("4002 "+element);
					if (element instanceof NamespaceStatement && ((NamespaceStatement) element).getKind() == NamespaceTypes.MODULE) {
//		                LogEvent.logEvent(4103, "");
						final NamespaceContext namespaceContext = (NamespaceContext) element.getContext();
						alreadySearched.add(namespaceContext);
						namespaceContext.lookup(name, level, Result, alreadySearched, true);
					}
				}
			}
		}
		if (carrier.getParent() != null) {
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

	private Compilation module() {
		Context ctx = _parent;
		while (!(ctx instanceof ModuleContext))
			ctx = ctx.getParent();
		return ((ModuleContext) ctx).carrier.parent;
	}
}

//
//
//
