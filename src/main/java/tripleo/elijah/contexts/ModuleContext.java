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
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:33:31 AM
 */
public class ModuleContext extends Context {

	private OS_Module carrier;

	public ModuleContext(final OS_Module module) {
		this.carrier = module;
	}

	@Override public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		alreadySearched.add(carrier.getContext());
		// TODO look all this up in a table, not by iteration
		for (final ModuleItem item: carrier.getItems()) {
			//
//			List<String> items;
//			if ((item instanceof ClassStatement)) {
//				items = ((ClassStatement) item).getItems()
//						        .stream()
//						        .filter((s) -> s instanceof OS_Element2)
//						        .map((s) -> ((OS_Element2)s).name())
//						        .collect(Collectors.toList());
//			} else if (item instanceof NamespaceStatement) {
//				items = ((NamespaceStatement) item).getItems()
//						        .stream()
//						        .filter((s) -> s instanceof OS_Element2)
//						        .map((s) -> ((OS_Element2)s).name())
//						        .collect(Collectors.toList());
//			} else {
//				items = new ArrayList<String>();
//			}
//			System.err.println("101 Searching "+items.toString()+" for "+name);
			//
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof AliasStatement) //&&
//				!(item instanceof VariableSequence)
				// TODO what about imports
			) continue;
			if (item instanceof OS_Element2) {
//				LogEvent.logEvent(102, ((OS_Element2) item).name());
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item, this);
				}
			}
			if (item instanceof NamespaceStatement && ((NamespaceStatement) item).getKind() == NamespaceTypes.MODULE) {
//				System.err.println(103);
				final NamespaceContext namespaceContext = (NamespaceContext) item.getContext();
				namespaceContext.lookup(name, level, Result, alreadySearched, true);
			}
		}
		/*for (ModuleItem item : carrier.getItems()) {
			if (item instanceof ImportStatement) {
				final ImportStatement importStatement = (ImportStatement) item;
				searchImports(name, level, Result, alreadySearched, importStatement);
			}
		}*/
//		System.err.println("2003 "+carrier.getItems());
		if (carrier.prelude != null && alreadySearched.contains(carrier.prelude.getContext()))
			return Result;
		if (carrier.prelude == null || one)
			return Result;
		return carrier.prelude.getContext().lookup(name, level+1, Result, alreadySearched, false);
	}

	@Override
	public Context getParent() {
		return null;
	}

	public OS_Module getCarrier() {
		return carrier;
	}

	public void setCarrier(OS_Module aCarrier) {
		carrier = aCarrier;
	}

	/*private void searchImports(String name, int level, LookupResultList Result, List<Context> alreadySearched, ImportStatement importStatement) {
//		System.err.println("2002 "+importStatement.importList());
		for (Qualident importStatementItem : importStatement.parts()) {
//			System.err.println("2005 "+importStatementItem);
			if (carrier.parent.isPackage(importStatementItem.toString())) {
				List<OS_Element> l = new ArrayList<>();
				OS_Package aPackage = carrier.parent.getPackage(importStatementItem);
				LogEvent.logEvent(4001 , ""+aPackage.getElements());
				for (OS_Element element : aPackage.getElements()) {
//					System.err.println("4000 "+element);
					if (element instanceof NamespaceStatement && ((NamespaceStatement) element).getKind() == NamespaceTypes.MODULE) {
//		                LogEvent.logEvent(4103, "");
						final NamespaceContext namespaceContext = (NamespaceContext) element.getContext();
						alreadySearched.add(namespaceContext);
						namespaceContext.lookup(name, level, Result, alreadySearched, true);
					}
				}
			}
		}
	}*/

}

//
//
//
