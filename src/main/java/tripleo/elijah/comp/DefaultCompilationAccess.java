package tripleo.elijah.comp;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.gen_fn.DeferredObject2;
import tripleo.elijah.stages.logging.ElLog;
import io.reactivex.rxjava3.functions.Consumer;

class DefaultCompilationAccess implements ICompilationAccess {
	private final Compilation compilation;
	private         DeferredObject2<PipelineLogic, Void, Void> pipelineLogicDeferred = new DeferredObject2<>();

	public DefaultCompilationAccess(final Compilation aCompilation) {
		compilation = aCompilation;
	}

	void registerPipelineLogic(final Consumer<PipelineLogic> aPipelineLogicConsumer) {
		pipelineLogicDeferred.then(new DoneCallback<PipelineLogic>() {
			@Override
			public void onDone(final PipelineLogic result) {
				try {
					aPipelineLogicConsumer.accept(result);
				} catch (Throwable aE) {
					throw new RuntimeException(aE);
				}
			}
		});
	}

	@Override
	public void setPipelineLogic(final PipelineLogic pl) {
		compilation.pipelineLogic = pl;

		pipelineLogicDeferred.resolve(pl);
	}

	@Override
	public void addPipeline(final PipelineMember pl) {
		compilation.pipelines.add(pl);
	}

	@Override
	@NotNull
	public ElLog.Verbosity testSilence() {
		//final boolean isSilent = C.silent;
		final boolean isSilent = false; // TODO fix this

		return isSilent ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	@Override
	public Compilation getCompilation() {
		return compilation;
	}

	@Override
	public void writeLogs() {
		final boolean silent = testSilence() == ElLog.Verbosity.SILENT;

		compilation.writeLogs(silent, compilation.pipelineLogic.elLogs);
	}
}
