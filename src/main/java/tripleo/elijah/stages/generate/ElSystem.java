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
import tripleo.elijah.stages.gen_fn.EvaClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.EvaNamespace;
import tripleo.elijah.stages.gen_fn.EvaNode;
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
			String s = generateOutputs_Internal(ab.node, ab.ty, outputStrategyC);
			assert s != null;
			ab.output = s;
		}

		if (verbose) {
			for (GenerateResultItem ab : gr.results()) {
				if (ab.node instanceof GeneratedFunction) continue;
				System.out.println("** "+ab.node+" "+ab.output);
			}
		}
	}

	String generateOutputs_Internal(EvaNode node, GenerateResult.TY ty, OutputStrategyC outputStrategyC) {
		String s, ss;
		if (node instanceof EvaNamespace) {
			final EvaNamespace EvaNamespace = (EvaNamespace) node;
			s = outputStrategyC.nameForNamespace(EvaNamespace, ty);
//			System.out.println("41 "+EvaNamespace+" "+s);
			for (GeneratedFunction gf : EvaNamespace.functionMap.values()) {
				ss = generateOutputs_Internal(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof EvaClass) {
			final EvaClass EvaClass = (EvaClass) node;
			s = outputStrategyC.nameForClass(EvaClass, ty);
//			System.out.println("48 "+EvaClass+" "+s);
			for (GeneratedFunction gf : EvaClass.functionMap.values()) {
				ss = generateOutputs_Internal(gf, ty, outputStrategyC);
				gfm_map.put(gf, ss);
			}
		} else if (node instanceof GeneratedFunction) {
			final GeneratedFunction generatedFunction = (GeneratedFunction) node;
			s = outputStrategyC.nameForFunction(generatedFunction, ty);
//			System.out.println("55 "+generatedFunction+" "+s);
		} else if (node instanceof GeneratedConstructor) {
			final GeneratedConstructor generatedConstructor = (GeneratedConstructor) node;
			s = outputStrategyC.nameForConstructor(generatedConstructor, ty);
//			System.out.println("55 "+generatedConstructor+" "+s);
		} else
			throw new IllegalStateException("Can't be here.");
		return s;
	}

	public void setOutputStrategy(OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public void setCompilation(Compilation compilation) {
		this.compilation = compilation;
	}
}

//
//
//
