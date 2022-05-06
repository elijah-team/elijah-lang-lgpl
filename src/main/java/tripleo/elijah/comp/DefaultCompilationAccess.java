package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.logging.ElLog;

class DefaultCompilationAccess implements ICompilationAccess {
	private final Compilation compilation;

	public DefaultCompilationAccess(final Compilation aCompilation) {
		compilation = aCompilation;
	}

	@Override
	public void setPipelineLogic(final PipelineLogic pl) {
		compilation.pipelineLogic = pl;
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
