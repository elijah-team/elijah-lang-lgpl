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

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {

	private final FunctionDef carrier;

	public FunctionContext(FunctionDef functionDef) {
		carrier = functionDef;
	}

	@Override public LookupResultList lookup(String name, int level, LookupResultList Result) {
//		final LookupResultList Result = new LookupResultList();
		for (FunctionItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof VariableSequence)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, (OS_Element) item);
				}
			}
			if (item instanceof VariableSequence) {
				System.out.println("[FunctionContext#lookup] VariableSequence "+item);
				for (VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs);
				}
			} /*else if (((OS_Element2)item).name() != null) {
				if (((OS_Element2)item).name().equals(name)) {
					Result.add(name, level, (OS_Element) item); // TODO exception waiting to happen
				}
			}*/
		}
		for (FormalArgListItem arg : carrier.getArgs()) {
			if (arg.name.getText().equals(name)) {
				Result.add(name, level, arg);
			}
		}
		if (carrier.getParent() != null)
			carrier.getParent().getContext().lookup(name, level+1, Result);
		return Result;
		
	}

    public void introduceVariable(IExpression variable) {
		System.out.println("77 "+variable);
    }

	public void introduceVariable(VariableStatement variable) {
		System.out.println("77 "+variable);
	}
}

//
//
//
