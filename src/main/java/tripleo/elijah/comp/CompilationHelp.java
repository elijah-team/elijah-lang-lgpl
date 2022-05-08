/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.base.Preconditions;
import tripleo.elijah.stages.logging.ElLog;

import java.util.ArrayList;
import java.util.List;

interface RuntimeProcess {
	void run(final Compilation aCompilation);

	void postProcess();
}

interface ICompilationAccess {
	void setPipelineLogic(final PipelineLogic pl);

	void addPipeline(final PipelineMember pl);

	ElLog.Verbosity testSilence();

	Compilation getCompilation();

	void writeLogs();
}

class StageToRuntime {
	public static RuntimeProcess get(final String stage, final ICompilationAccess ca, final ProcessRecord aPr) {
		if (stage.equals("E"))
			return new EmptyProcess(ca, aPr);
		if (stage.equals("O"))
			return new OStageProcess(ca, aPr);
		if (stage.equals("D"))
			return new DStageProcess(ca, aPr);

		return null;
	}
}

class RuntimeProcesses {
	private final List<RuntimeProcess> processes = new ArrayList<>();
	private Compilation comp;

	public RuntimeProcesses(final Compilation aCompilation) {
		comp = aCompilation;
	}

	public void run() {
		for (RuntimeProcess runtimeProcess : processes) {
			System.err.println("***** RuntimeProcess named " + runtimeProcess);
			runtimeProcess.run(comp);
		}
	}

	public void add(final RuntimeProcess aProcess) {
		processes.add(aProcess);
	}

	public void postProcess(ProcessRecord pr, final ICompilationAccess ca) {
		for (RuntimeProcess runtimeProcess : processes) {
			runtimeProcess.postProcess();
		}

		final boolean silent = false; // TODO

		if (!(pr.stage.equals("E"))) {
			ca.writeLogs();
		}
	}
}

final class EmptyProcess implements RuntimeProcess {
	public EmptyProcess(final ICompilationAccess aCompilationAccess, final ProcessRecord aPr) {

	}

	@Override
	public void run(final Compilation aCompilation) {

	}

	@Override
	public void postProcess() {

	}
}

abstract class ODPrim {
	private ProcessRecord pr;

	//public void setPr(final ProcessRecord aPr) {
	//	pr = aPr;
	//}

	void part1(final ICompilationAccess ca) {
		Preconditions.checkNotNull(pr);
//		assert pr != null;

//--		ca.setPipelineLogic(new PipelineLogic(ca.testSilence()));
		//if (pr.dpl == null) {  // TODO fix this
		//	pr.dpl = new DeducePipeline(ca.getCompilation());
			ca.addPipeline(pr.dpl);
		//}
	}

	void part2_O(final ICompilationAccess ca) {
		Preconditions.checkNotNull(pr);
		//assert pr != null;

		final Compilation comp = ca.getCompilation();

		final GeneratePipeline gpl = new GeneratePipeline(comp, pr.dpl);
		ca.addPipeline(gpl);
		final WritePipeline wpl = new WritePipeline(comp, pr.pipelineLogic.gr);
		ca.addPipeline(wpl);

		final WriteMesonPipeline wmpl = new WriteMesonPipeline(comp, pr.pipelineLogic.gr, wpl);
		ca.addPipeline(wmpl);
	}

	void part2_D(final ICompilationAccess ca) {
		assert pr.stage.equals("D");
	}

	void part2(final ICompilationAccess ca) {
		final String stage = pr.stage;

		if (stage.equals("O")) {
			part2_O(ca);
		} else if (stage.equals("D")) {
			part2_D(ca);
		} else {
			throw new IllegalStateException("invalid state");
		}
	}
}

class DStageProcess implements RuntimeProcess {
	private final ODPrim             prim = new ODPrim() {
	};
	private final ICompilationAccess ca;
	private final ProcessRecord pr;

	public DStageProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
		ca = aCa;
		pr = aPr;

		prim.pr = pr;
	}

	@Override
	public void run(final Compilation aCompilation) {
		int y=2;
	}

	@Override
	public void postProcess() {
		prim.part2_D(ca);
	}
}

class ProcessRecord {
	final DeducePipeline dpl;
	final PipelineLogic  pipelineLogic;
	final String         stage;

	public ProcessRecord(final ICompilationAccess ca) {
		final Compilation compilation = ca.getCompilation();

		pipelineLogic = new PipelineLogic(ca.testSilence());
		ca.setPipelineLogic(pipelineLogic);

		dpl           = new DeducePipeline(compilation);
		stage         = compilation.stage;
	}
}

class OStageProcess implements RuntimeProcess {
	private final ODPrim        prim = new ODPrim() {
	};
	private final ProcessRecord pr;
	private final ICompilationAccess ca;

	OStageProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
		ca = aCa;
		pr = aPr;

		prim.pr = pr;
	}

	@Override
	public void run(final Compilation aCompilation) {
		prim.part1(ca);
	}

	@Override
	public void postProcess() {
		prim.part2_O(ca);
	}
}

//
//
//
