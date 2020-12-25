/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 12/24/20 3:59 AM
 */
public class DeducePhase {

	public void addFunction(GeneratedFunction generatedFunction, FunctionDef fd) {
		functionMap.put(fd, generatedFunction);
	}

	static class Triplet {

		private final DeduceTypes2 deduceTypes2;
		private final FunctionDef fd;
		private final ForFunction forFunction;

		public Triplet(DeduceTypes2 deduceTypes2, FunctionDef fd, ForFunction forFunction) {
			this.deduceTypes2 = deduceTypes2;
			this.fd = fd;
			this.forFunction = forFunction;
		}
	}

	private List<Triplet> forFunctions = new ArrayList<>();
	private Multimap<FunctionDef, GeneratedFunction> functionMap = ArrayListMultimap.create();

	public void deduceModule(OS_Module m, Iterable<GeneratedNode> lgf) {
		new DeduceTypes2(m, this).deduceFunctions(lgf);
	}

	public void forFunction(DeduceTypes2 deduceTypes2, FunctionDef fd, ForFunction forFunction) {
		forFunctions.add(new Triplet(deduceTypes2, fd, forFunction));
	}

	public void typeDecided(FunctionDef fd, final OS_Type aType) {
		for (Triplet triplet : forFunctions) {
			Collection<GeneratedFunction> x = functionMap.get(fd);
			synchronized (triplet.deduceTypes2) {
				triplet.forFunction.typeDecided(aType);
			}
		}
	}

	public void finish() {
		for (Triplet triplet : forFunctions) {
			Collection<GeneratedFunction> x = functionMap.get(triplet.fd);
//			triplet.forFunction.finish();
		}
	}
}

//
//
//
