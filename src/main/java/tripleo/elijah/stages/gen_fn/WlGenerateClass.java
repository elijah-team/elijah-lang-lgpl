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
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkManager;

import java.util.List;

/**
 * Created 5/16/21 12:41 AM
 */
public class WlGenerateClass implements WorkJob {
	private final ClassStatement classStatement;
	private final GenerateFunctions generateFunctions;
	private final ClassInvocation classInvocation;
	private final List<GeneratedNode> coll;
	private boolean _isDone = false;
	private GeneratedClass Result;

	public WlGenerateClass(GenerateFunctions aGenerateFunctions,
						   ClassInvocation aClassInvocation,
						   List<GeneratedNode> coll) {
		classStatement = aClassInvocation.getKlass();
		generateFunctions = aGenerateFunctions;
		classInvocation = aClassInvocation;
		this.coll = coll;
	}

	@Override
	public void run(WorkManager aWorkManager) {
		@NotNull GeneratedClass kl = generateFunctions.generateClass(classStatement);
		kl.setCode(generateFunctions.module.parent.nextClassCode());
		if (coll != null)
			coll.add(kl);
		final DeferredObject<GeneratedClass, Void, Void> resolvePromise = (DeferredObject<GeneratedClass, Void, Void>) classInvocation.resolvePromise();
		if (resolvePromise.isPending())
			resolvePromise.resolve(kl);
		else
			// debugging. can remove
			resolvePromise.then(new DoneCallback<GeneratedClass>() {
				@Override
				public void onDone(GeneratedClass result) {
					assert result == kl;
				}
			});
		Result = kl;
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
