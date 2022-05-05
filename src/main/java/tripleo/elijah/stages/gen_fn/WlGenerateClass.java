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
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.util.Holder;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkManager;

/**
 * Created 5/16/21 12:41 AM
 */
public class WlGenerateClass implements WorkJob {
	private final ClassStatement classStatement;
	private final GenerateFunctions generateFunctions;
	private final ClassInvocation classInvocation;
	private final DeducePhase.GeneratedClasses coll;
	private boolean _isDone = false;
	private GeneratedClass Result;

	public WlGenerateClass(GenerateFunctions aGenerateFunctions,
						   ClassInvocation aClassInvocation,
						   DeducePhase.GeneratedClasses coll) {
		classStatement = aClassInvocation.getKlass();
		generateFunctions = aGenerateFunctions;
		classInvocation = aClassInvocation;
		this.coll = coll;
	}

	@Override
	public void run(WorkManager aWorkManager) {
		final DeferredObject<GeneratedClass, Void, Void> resolvePromise = classInvocation.resolveDeferred();
		switch (resolvePromise.state()) {
		case PENDING:
			@NotNull GeneratedClass kl = generateFunctions.generateClass(classStatement, classInvocation);
			kl.setCode(generateFunctions.module.getCompilation().nextClassCode());
			if (coll != null)
				coll.add(kl);

			resolvePromise.resolve(kl);
			Result = kl;
			break;
		case RESOLVED:
			Holder<GeneratedClass> hgc = new Holder<GeneratedClass>();
			resolvePromise.then(new DoneCallback<GeneratedClass>() {
				@Override
				public void onDone(GeneratedClass result) {
//					assert result == kl;
					hgc.set(result);
				}
			});
			Result = hgc.get();
			break;
		case REJECTED:
			throw new NotImplementedException();
		}
		_isDone = true;
	}

	@Override
	public boolean isDone() {
		return _isDone;
	}

	public GeneratedClass getResult() {
		return Result;
	}
}

//
//
//
