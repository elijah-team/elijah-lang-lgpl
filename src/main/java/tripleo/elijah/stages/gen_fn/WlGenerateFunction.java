/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkManager;

/**
 * Created 5/16/21 12:46 AM
 */
public class WlGenerateFunction implements WorkJob {
	private final FunctionDef functionDef;
	private final GenerateFunctions generateFunctions;
	private final FunctionInvocation functionInvocation;
	private boolean _isDone = false;

	public WlGenerateFunction(GenerateFunctions aGenerateFunctions, FunctionInvocation aFunctionInvocation) {
		functionDef = aFunctionInvocation.getFunction();
		generateFunctions = aGenerateFunctions;
		functionInvocation = aFunctionInvocation;
	}

	@Override
	public void run(WorkManager aWorkManager) {
		OS_Element classStatement = functionDef.getParent();
		@NotNull GeneratedFunction gf = generateFunctions.generateFunction(functionDef, classStatement, functionInvocation);
//		lgf.add(gf);

		final ClassInvocation ci = functionInvocation.getClassInvocation();
		ci.resolvePromise().done(new DoneCallback<GeneratedClass>() {
			@Override
			public void onDone(GeneratedClass result) {
				gf.setCode(generateFunctions.module.parent.nextFunctionCode());
				gf.setClass(result);
				result.addFunction(functionDef, gf);
			}
		});

		_isDone = true;
	}

	@Override
	public boolean isDone() {
		return _isDone;
	}
}

//
//
//
