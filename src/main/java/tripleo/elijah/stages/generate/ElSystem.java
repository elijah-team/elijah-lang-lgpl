/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.generate;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_c.CDependencyRef;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 1/8/21 11:02 PM
 */
public class ElSystem {
	private OutputStrategy outputStrategy;
	private Compilation compilation;
	private final Map<GeneratedFunction, String> gfm_map = new HashMap<GeneratedFunction, String>();
	public boolean verbose = true;

	public void generateOutputs(GenerateResult gr) {
		final OutputStrategyC outputStrategyC = new OutputStrategyC(this.outputStrategy);

		for (GenerateResultItem ab : gr.results()) {
			String filename = getFilenameForNode(ab.node, ab.ty, outputStrategyC);
			assert filename != null;
			ab.output = filename;
			if (ab.ty == GenerateResult.TY.HEADER)
				ab.getDependency().setRef(new CDependencyRef(filename));
			gr.completeItem(ab);
		}

		if (verbose) {
			for (GenerateResultItem ab : gr.results()) {
				if (ab.node instanceof GeneratedFunction) continue;
				System.out.println("** "+ab.node+" "+ ab.output/*((CDependencyRef)ab.getDependency().getRef()).getHeaderFile()*/);
			}
		}

		gr.signalDone();
	}

	String getFilenameForNode(GeneratedNode node, GenerateResult.TY ty, OutputStrategyC outputStrategyC) {
		String s, ss;
		if (node instanceof GeneratedNamespace) {
			final GeneratedNamespace generatedNamespace = (GeneratedNamespace) node;
			s = outputStrategyC.nameForNamespace(generatedNamespace, ty);
//			System.out.println("41 "+generatedNamespace+" "+s);
			for (GeneratedFunction gf : generatedNamespace.functionMap.values()) {
				ss = getFilenameForNode(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedClass) {
			final GeneratedClass generatedClass = (GeneratedClass) node;
			s = outputStrategyC.nameForClass(generatedClass, ty);
//			System.out.println("48 "+generatedClass+" "+s);
			for (GeneratedFunction gf : generatedClass.functionMap.values()) {
				ss = getFilenameForNode(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedFunction) {
			final GeneratedFunction generatedFunction = (GeneratedFunction) node;
			s = outputStrategyC.nameForFunction(generatedFunction, ty);
//			System.out.println("55 "+generatedFunction+" "+s);
		} else
			throw new IllegalStateException("Can't be here.");
		return s;
	}

	public void setOutputStrategy(OutputStrategy aOutputStrategy) {
		outputStrategy = aOutputStrategy;
	}

	public void setCompilation(Compilation aCompilation) {
		compilation = aCompilation;
	}
}

//
//
//
