package tripleo.elijah.nextgen.inputtree;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.*;
import tripleo.elijah.stages.logging.*;
import tripleo.elijah.util.*;
import tripleo.elijah.work.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class EIT_ModuleList {
//
//	// TODO use WorldModule here
//	// TODO 24/01/03 Use CM_Module here!
//	public List<OS_Module> getMods() {
//		return mods;
//	}
//
//	private final List<OS_Module> mods;
//
//	@Contract(pure = true)
//	public EIT_ModuleList(final List<OS_Module> aMods) {
//		mods = aMods;
//	}
//
//	private void __process__PL__each(final @NotNull _ProcessParams plp) {
//		final List<EvaNode> resolved_nodes = new ArrayList<EvaNode>();
//
//		final OS_Module mod = plp.getMod();
//		final DeducePhase.EvaClasses lgc = plp.getLgc();
//
//		// assert lgc.size() == 0;
//
//		final int size = lgc.size();
//
//		if (size != 0) {
//			NotImplementedException.raise();
//			SimplePrintLoggerToRemoveSoon.println_err(String.format("lgc.size() != 0: %d", size));
//		}
//
//		plp.generate();
//
//		final ICodeRegistrar codeRegistrar = plp.pipelineLogic.generatePhase.getCodeRegistrar();
//		assert codeRegistrar != null;
//		final Coder coder = new Coder(codeRegistrar);
//
//		for (final EvaNode evaNode : lgc) {
//			coder.codeNodes(mod, resolved_nodes, evaNode);
//		}
//
//		resolved_nodes.forEach(EvaNode -> coder.codeNode(EvaNode, mod));
//
//		plp.deduceModule();
//	}
//
//	public void add(final OS_Module m) {
//		mods.add(m);
//	}
//
//	public void process__PL(final @NotNull Function<OS_Module, GenerateFunctions> ggf, final @NotNull PipelineLogic pipelineLogic) {
//		for (final OS_Module mod : mods) {
//			final @NotNull EntryPointList epl = null; //mod.entryPoints;
//
//
//			//
//			//
//			//
//			//
//			//
//			//
//			// imposed NULL 09/01
//			//
//			//
//			//
//			//
//			//
//			//
//			//
//
//
//			if (epl.size() == 0) {
//				continue;
//			}
//
//
//			final GenerateFunctions gfm = ggf.apply(mod);
//
//			final DeducePhase deducePhase = pipelineLogic.dp;
//			//final DeducePhase.@NotNull EvaClasses lgc            = deducePhase.EvaClasses;
//
//			final _ProcessParams plp = new _ProcessParams(mod, pipelineLogic, gfm, epl, deducePhase);
//
//			__process__PL__each(plp);
//		}
//	}
//
//	public Stream<OS_Module> stream() {
//		return mods.stream();
//	}
//
//	private static class _ProcessParams {
//		private final @NotNull DeducePhase       deducePhase;
//		@NotNull
//		private final          EntryPointList    epl;
//		private final @NotNull GenerateFunctions gfm;
//		private final @NotNull OS_Module         mod;
//		private final @NotNull PipelineLogic     pipelineLogic;
//
//		@Contract(pure = true)
//		private _ProcessParams(@NotNull final OS_Module aModule,
//							   @NotNull final PipelineLogic aPipelineLogic,
//							   @NotNull final GenerateFunctions aGenerateFunctions,
//							   @NotNull final EntryPointList aEntryPointList,
//							   @NotNull final DeducePhase aDeducePhase) {
//			mod           = aModule;
//			pipelineLogic = aPipelineLogic;
//			gfm           = aGenerateFunctions;
//			epl           = aEntryPointList;
//			deducePhase   = aDeducePhase;
//		}
//
//		public void deduceModule() {
//			final DeducePhase_deduceModule_Request rq = new DeducePhase_deduceModule_Request(mod, getLgc(), getVerbosity(), deducePhase);
//
//			deducePhase.deduceModule(rq);
//		}
//
//		@Contract(pure = true)
//		public DeducePhase.@NotNull EvaClasses getLgc() {
//			return deducePhase.EvaClasses;
//		}
//
//		@Contract(pure = true)
//		public ElLog.@NotNull Verbosity getVerbosity() {
//			return pipelineLogic.getVerbosity();
//		}
//
//		public void generate() {
//			epl.generate(gfm, deducePhase, getWorkManagerSupplier());
//		}
//
//		@Contract(pure = true)
//		public @NotNull Supplier<WorkManager> getWorkManagerSupplier() {
//			return () -> pipelineLogic.generatePhase.getWm();
//		}
//
//		@Contract(pure = true)
//		public @NotNull OS_Module getMod() {
//			return mod;
//		}
//
//	}
}
