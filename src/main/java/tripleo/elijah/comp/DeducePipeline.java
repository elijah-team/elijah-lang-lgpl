/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/21/21 10:10 PM
 */
public class DeducePipeline implements PipelineMember {
	private final Compilation c;
	private final List<PipelineLogicRunnable> plrs = new ArrayList<>();
	List<GeneratedNode> lgc = new ArrayList<GeneratedNode>();

	public DeducePipeline(Compilation aCompilation) {
		System.err.println("***** Hit DeducePipeline constructor");

		c = aCompilation;

		for (final OS_Module module : c.modules) {
			if (false) {
/*
				new DeduceTypes(module).deduce();
				for (final OS_Element2 item : module.items()) {
					if (item instanceof ClassStatement || item instanceof NamespaceStatement) {
						System.err.println("8001 "+item);
					}
				}
				new TranslateModule(module).translate();
*/
//				new ExpandFunctions(module).expand();
//
//  			final JavaCodeGen visit = new JavaCodeGen();
//	       		module.visitGen(visit);
			} else {
				//c.pipelineLogic.addModule(module);
				addRunnable(new PL_AddModule(module));
			}
		}

		addRunnable(new PL_EverythingBeforeGenerate());
		addRunnable(new PL_SaveGeneratedClasses());
	}

	@Override
	public void run() {
		// TODO move "futures" to ctor...
		//c.pipelineLogic.everythingBeforeGenerate(lgc);
		//lgc = c.pipelineLogic.dp.generatedClasses.copy();

		// TODO wait for these two to finish...
		// TODO make sure you call #setPipelineLogic...

		assert c.pipelineLogic != null;

		int y = 2;
	}

	public void setPipelineLogic(final PipelineLogic aPipelineLogic) {
		for (PipelineLogicRunnable plr : plrs) {
			plr.run(aPipelineLogic);
		}
	}

	private void addRunnable(final PipelineLogicRunnable plr) {
		plrs.add(plr);
	}

	private interface PipelineLogicRunnable {
		void run(final PipelineLogic pipelineLogic);
	}

	private class PL_AddModule implements PipelineLogicRunnable {
		private final OS_Module m;

		public PL_AddModule(final OS_Module aModule) {
			m = aModule;
		}

		@Override
		public void run(final @NotNull PipelineLogic pipelineLogic) {
			pipelineLogic.addModule(m);
		}
	}

	private class PL_EverythingBeforeGenerate implements PipelineLogicRunnable {
		@Override
		public void run(final @NotNull PipelineLogic pipelineLogic) {
			pipelineLogic.everythingBeforeGenerate(lgc);
		}
	}

	private class PL_SaveGeneratedClasses implements PipelineLogicRunnable {
		@Override
		public void run(final @NotNull PipelineLogic pipelineLogic) {
			lgc = pipelineLogic.dp.generatedClasses.copy();
		}
	}
}

//
//
//
