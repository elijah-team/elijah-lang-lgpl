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
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.gen_fn.GenerateFunctions;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedContainer;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 12/24/20 3:59 AM
 */
public class DeducePhase {

	private final List<FoundElement> foundElements = new ArrayList<FoundElement>();
	private final Map<IdentTableEntry, OnType> idte_type_callbacks = new HashMap<IdentTableEntry, OnType>();
	private List<GeneratedNode> generatedClasses;

	public void addFunction(GeneratedFunction generatedFunction, FunctionDef fd) {
		functionMap.put(fd, generatedFunction);
	}

	public void registerFound(FoundElement foundElement) {
		foundElements.add(foundElement);
	}

	public void onType(IdentTableEntry entry, OnType callback) {
		idte_type_callbacks.put(entry, callback);
	}

	Multimap<OS_Element, ResolvedVariables> resolved_variables = ArrayListMultimap.create();

	public void registerResolvedVariable(IdentTableEntry identTableEntry, OS_Element parent, String varName) {
		resolved_variables.put(parent, new ResolvedVariables(identTableEntry, parent, varName));
	}

	static class ResolvedVariables {
		final IdentTableEntry identTableEntry;
		final OS_Element parent;
		final String varName;

		public ResolvedVariables(IdentTableEntry aIdentTableEntry, OS_Element aParent, String aVarName) {
			identTableEntry = aIdentTableEntry;
			parent = aParent;
			varName = aVarName;
		}
	}

	static class Triplet {

		private final DeduceTypes2 deduceTypes2;
		private final FunctionInvocation gf;
		private final ForFunction forFunction;

		public Triplet(DeduceTypes2 deduceTypes2, FunctionInvocation gf, ForFunction forFunction) {
			this.deduceTypes2 = deduceTypes2;
			this.gf = gf;
			this.forFunction = forFunction;
		}
	}

	private final List<Triplet> forFunctions = new ArrayList<Triplet>();
	private final Multimap<FunctionDef, GeneratedFunction> functionMap = ArrayListMultimap.create();

	public DeduceTypes2 deduceModule(OS_Module m, Iterable<GeneratedNode> lgf) {
		final DeduceTypes2 deduceTypes2 = new DeduceTypes2(m, this);
		deduceTypes2.deduceFunctions(lgf);
		return deduceTypes2;
	}

	public DeduceTypes2 deduceModule(OS_Module m) {
		final GenerateFunctions gfm = new GenerateFunctions(m);
		List<GeneratedNode> lgc = gfm.generateAllTopLevelClasses();

		final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
		for (GeneratedNode lgci : lgc) {
			if (lgci instanceof GeneratedClass) {
				lgf.addAll(((GeneratedClass) lgci).functionMap.values());
			}
		}

		generatedClasses = lgc;

		return deduceModule(m, lgf);
	}

	/**
	 * Use this when you have already called generateAllTopLevelClasses
	 * @param m the module
	 * @param lgc the result of generateAllTopLevelClasses
	 * @param _unused is unused
	 */
	public void deduceModule(OS_Module m, Iterable<GeneratedNode> lgc, boolean _unused) {
//		final GenerateFunctions gfm = new GenerateFunctions(m);
//		List<GeneratedNode> lgc = gfm.generateAllTopLevelClasses();


		if (false) {
			final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
			for (GeneratedNode lgci : lgc) {
				if (lgci instanceof GeneratedClass) {
					lgf.addAll(((GeneratedClass) lgci).functionMap.values());
				}
			}

			deduceModule(m, lgf);
		} else
			deduceModule(m); // TODO what a controversial change
	}

/*
	public void forFunction(DeduceTypes2 deduceTypes2, GeneratedFunction gf, ForFunction forFunction) {
		forFunctions.add(new Triplet(deduceTypes2, gf, forFunction));
	}
*/

	public void forFunction(DeduceTypes2 deduceTypes2, FunctionInvocation gf, ForFunction forFunction) {
		forFunctions.add(new Triplet(deduceTypes2, gf, forFunction));
	}

	Map<GeneratedFunction, OS_Type> typeDecideds = new HashMap<GeneratedFunction, OS_Type>();

	public void typeDecided(GeneratedFunction gf, final OS_Type aType) {
		typeDecideds.put(gf, aType);
	}

	public void finish() {
		for (Map.Entry<IdentTableEntry, OnType> entry : idte_type_callbacks.entrySet()) {
			IdentTableEntry idte = entry.getKey();
			if (idte.type.attached != null)
				entry.getValue().typeDeduced(idte.type.attached);
			else
				entry.getValue().noTypeFound();
		}
		for (Map.Entry<GeneratedFunction, OS_Type> entry : typeDecideds.entrySet()) {
			for (Triplet triplet : forFunctions) {
				if (triplet.gf.getGenerated() == entry.getKey()) {
					synchronized (triplet.deduceTypes2) {
						triplet.forFunction.typeDecided(entry.getValue());
					}
				}
			}
		}
		for (Triplet triplet : forFunctions) {
//			Collection<GeneratedFunction> x = functionMap.get(triplet.gf);
//			triplet.forFunction.finish();
		}
		for (FoundElement foundElement : foundElements) {
			// TODO As we are using this, didntFind will never fail because
			//  we call doFoundElement manually in resolveIdentIA
			//  As the code matures, maybe this will change and the interface
			//  will be improved, namely calling doFoundElement from here as well
			if (foundElement.didntFind()) {
				foundElement.doNoFoundElement();
			}
		}
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedContainer) {
				final GeneratedContainer generatedContainer = (GeneratedContainer) generatedNode;
				Collection<ResolvedVariables> x = resolved_variables.get(generatedContainer.getElement());
				for (ResolvedVariables resolvedVariables : x) {
					final GeneratedContainer.VarTableEntry variable = generatedContainer.getVariable(resolvedVariables.varName);
					assert variable != null;
					final TypeTableEntry type = resolvedVariables.identTableEntry.type;
					if (type != null)
						variable.addPotentialTypes(List_of(type));
					variable.addPotentialTypes(resolvedVariables.identTableEntry.potentialTypes());
				}
			}
		}
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				generatedClass.resolve_var_table_entries();
			}
		}
	}
}

//
//
//
