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
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.NamespaceInvocation;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkJob;
import tripleo.elijah.work.WorkManager;

import java.util.List;

/**
 * Created 5/31/21 3:01 AM
 */
public class WlGenerateNamespace implements WorkJob {
	private final GenerateFunctions generateFunctions;
	private final NamespaceStatement namespaceStatement;
	private final NamespaceInvocation namespaceInvocation;
	private final DeducePhase.@Nullable GeneratedClasses coll;
	private boolean _isDone = false;
	private GeneratedNamespace result;

	public WlGenerateNamespace(@NotNull GenerateFunctions aGenerateFunctions,
							   @NotNull NamespaceInvocation aNamespaceInvocation,
							   @Nullable DeducePhase.GeneratedClasses aColl) {
		generateFunctions = aGenerateFunctions;
		namespaceStatement = aNamespaceInvocation.getNamespace();
		namespaceInvocation = aNamespaceInvocation;
		coll = aColl;
	}

	@Override
	public void run(WorkManager aWorkManager) {
		final DeferredObject<GeneratedNamespace, Void, Void> resolvePromise = namespaceInvocation.resolveDeferred();
		switch (resolvePromise.state()) {
		case PENDING:
			@NotNull GeneratedNamespace ns = generateFunctions.generateNamespace(namespaceStatement);
			ns.setCode(generateFunctions.module.getCompilation().nextClassCode());
			if (coll != null)
				coll.add(ns);

			resolvePromise.resolve(ns);
			result = ns;
			break;
		case RESOLVED:
			resolvePromise.then(new DoneCallback<GeneratedNamespace>() {
				@Override
				public void onDone(GeneratedNamespace result) {
					WlGenerateNamespace.this.result = result;
				}
			});
			break;
		case REJECTED:
			throw new NotImplementedException();
		}
		_isDone = true;
//		System.out.println(String.format("** GenerateNamespace %s at %s", namespaceInvocation.getNamespace().getName(), this));
	}

	@Override
	public boolean isDone() {
		return _isDone;
	}

	public GeneratedNode getResult() {
		return result;
	}
}

//
//
//
