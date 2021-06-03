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
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.OS_UnknownType;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.work.WorkList;

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
	public List<GeneratedNode> generatedClasses = new ArrayList<GeneratedNode>();
	public final GeneratePhase generatePhase;

	public DeducePhase(GeneratePhase aGeneratePhase) {
		generatePhase = aGeneratePhase;
	}

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

	Multimap<ClassStatement, OnClass> onclasses = ArrayListMultimap.create();

	public void onClass(ClassStatement aClassStatement, OnClass callback) {
		onclasses.put(aClassStatement, callback);
	}

//	Multimap<GeneratedClass, ClassInvocation> generatedClasses1 = ArrayListMultimap.create();
	Multimap<ClassStatement, ClassInvocation> classInvocationMultimap = ArrayListMultimap.create();

	public ClassInvocation registerClassInvocation(ClassInvocation aClassInvocation) {
		boolean put = false;
		ClassInvocation Result = null;

		// 1. select which to return
		ClassStatement c = aClassInvocation.getKlass();
		Collection<ClassInvocation> cis = classInvocationMultimap.get(c);
		for (ClassInvocation ci : cis) {
			// don't lose information
			if (ci.getConstructorName() != null)
				if (!(ci.getConstructorName().equals(aClassInvocation.getConstructorName())))
					continue;

			boolean i = equivalentGenericPart(aClassInvocation, ci);
			if (i) {
				Result = ci;
				break;
			}
		}

		if (Result == null) {
			put = true;
			Result = aClassInvocation;
		}

		// 2. Check and see if already done
		Collection<ClassInvocation> cls = classInvocationMultimap.get(Result.getKlass());
		for (ClassInvocation ci : cls) {
			if (equivalentGenericPart(ci, Result)) {
				return ci;
			}
		}

		if (put) {
			classInvocationMultimap.put(aClassInvocation.getKlass(), aClassInvocation);
		}

		// 3. Generate new GeneratedClass
		final WorkList wl = new WorkList();
		final OS_Module mod = Result.getKlass().getContext().module();
		wl.addJob(new WlGenerateClass(generatePhase.getGenerateFunctions(mod), Result, generatedClasses)); // TODO why add now?
		generatePhase.wm.addJobs(wl);
		generatePhase.wm.drain(); // TODO find a better place to put this

		// 4. Return it
		return Result;
	}

	public boolean equivalentGenericPart(ClassInvocation ci0, ClassInvocation ci) {
		Map<TypeName, OS_Type> map = ci0.genericPart;
		Map<TypeName, OS_Type> gp = ci.genericPart;
		if (gp == null && (map == null || map.size() == 0)) return true;
		//
		int i = gp.entrySet().size();
		for (Map.Entry<TypeName, OS_Type> entry : gp.entrySet()) {
			final OS_Type entry_type = map.get(entry.getKey());
			assert !(entry_type instanceof OS_UnknownType);
			if (entry_type.equals(entry.getValue()))
				i--;
//				else
//					return aClassInvocation;
		}
		return i == 0;
	}

	final Map<NamespaceStatement, NamespaceInvocation> namespaceInvocationMap = new HashMap<NamespaceStatement, NamespaceInvocation>();

	public NamespaceInvocation registerNamespaceInvocation(NamespaceStatement aNamespaceStatement) {
		if (namespaceInvocationMap.containsKey(aNamespaceStatement))
			return namespaceInvocationMap.get(aNamespaceStatement);

		NamespaceInvocation nsi = new NamespaceInvocation(aNamespaceStatement);
		namespaceInvocationMap.put(aNamespaceStatement, nsi);
		return nsi;
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
		final GenerateFunctions gfm = generatePhase.getGenerateFunctions(m);
		List<GeneratedNode> lgc = gfm.generateAllTopLevelClasses();

		final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
		for (GeneratedNode lgci : lgc) {
			if (lgci instanceof GeneratedClass) {
				final Collection<GeneratedFunction> generatedFunctions = ((GeneratedClass) lgci).functionMap.values();
				for (GeneratedFunction generatedFunction : generatedFunctions) {
					generatedFunction.setClass(lgci);
				}
				lgf.addAll(generatedFunctions);
			}
			if (lgci instanceof GeneratedNamespace) {
				final Collection<GeneratedFunction> generatedFunctions = ((GeneratedNamespace) lgci).functionMap.values();
				for (GeneratedFunction generatedFunction : generatedFunctions) {
					generatedFunction.setClass(lgci);
				}
				lgf.addAll(generatedFunctions);
			}
		}

//		generatedClasses = lgc;

		return deduceModule(m, lgf);
	}

	/**
	 * Use this when you have already called generateAllTopLevelClasses
	 * @param m the module
	 * @param lgc the result of generateAllTopLevelClasses
	 * @param _unused is unused
	 */
	public void deduceModule(OS_Module m, Iterable<GeneratedNode> lgc, boolean _unused) {
		if (false) {
			final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
			for (GeneratedNode lgci : lgc) {
				if (lgci instanceof GeneratedClass) {
					lgf.addAll(((GeneratedClass) lgci).functionMap.values());
				}
			}

			deduceModule(m, lgf);
		} else {
//			deduceModule(m); // TODO what a controversial change

			final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();

			for (GeneratedNode lgci : lgc) {
				if (lgci.module() != m) continue;

				if (lgci instanceof GeneratedClass) {
					final Collection<GeneratedFunction> generatedFunctions = ((GeneratedClass) lgci).functionMap.values();
//					for (GeneratedFunction generatedFunction : generatedFunctions) {
//						generatedFunction.setClass(lgci); // TODO delete when done
//					}
					lgf.addAll(generatedFunctions);
				}
				if (lgci instanceof GeneratedNamespace) {
					final Collection<GeneratedFunction> generatedFunctions = ((GeneratedNamespace) lgci).functionMap.values();
					for (GeneratedFunction generatedFunction : generatedFunctions) {
						generatedFunction.setClass(lgci);
					}
					lgf.addAll(generatedFunctions);
				}
			}

			List<GeneratedNode> lgcc = new ArrayList<GeneratedNode>();

			for (GeneratedNode generatedNode : lgc) {
				if (!(generatedNode instanceof GeneratedClass || generatedNode instanceof GeneratedNamespace)) continue;
				lgcc.add(generatedNode);
			}

//			generatedClasses = lgcc;

			deduceModule(m, lgf);
		}
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
		// TODO all GeneratedFunction nodes have a genClass member
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				Collection<GeneratedFunction> functions = generatedClass.functionMap.values();
				for (GeneratedFunction generatedFunction : functions) {
					generatedFunction.setParent(generatedClass);
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				Collection<GeneratedFunction> functions = generatedNamespace.functionMap.values();
				for (GeneratedFunction generatedFunction : functions) {
					generatedFunction.setParent(generatedNamespace);
				}
			}
		}
/*
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				final ClassStatement cs = generatedClass.getKlass();
				Collection<ClassInvocation> cis = classInvocationMultimap.get(cs);
				for (ClassInvocation ci : cis) {
					if (equivalentGenericPart(generatedClass.ci, ci)) {
						final DeferredObject<GeneratedClass, Void, Void> deferredObject = (DeferredObject<GeneratedClass, Void, Void>) ci.promise();
						deferredObject.then(new DoneCallback<GeneratedClass>() {
							@Override
							public void onDone(GeneratedClass result) {
								assert result == generatedClass;
							}
						});
//						deferredObject.resolve(generatedClass);
					}
				}
			}
		}
*/
		// TODO rewrite with classInvocationMultimap
		for (ClassStatement classStatement : onclasses.keySet()) {
			for (GeneratedNode generatedNode : generatedClasses) {
				if (generatedNode instanceof GeneratedClass) {
					final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					if (generatedClass.getKlass() == classStatement) {
						Collection<OnClass> ks = onclasses.get(classStatement);
						for (OnClass k : ks) {
							k.classFound(generatedClass);
						}
					} else {
						Collection<GeneratedClass> cmv = generatedClass.classMap.values();
						for (GeneratedClass aClass : cmv) {
							if (aClass.getKlass() == classStatement) {
								Collection<OnClass> ks = onclasses.get(classStatement);
								for (OnClass k : ks) {
									k.classFound(generatedClass);
								}
							}
						}
					}
				}
			}
		}
		for (Map.Entry<IdentTableEntry, OnType> entry : idte_type_callbacks.entrySet()) {
			IdentTableEntry idte = entry.getKey();
			if (idte.type !=null && // TODO make a stage where this gets set (resolvePotentialTypes)
					idte.type.attached != null)
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
/*
		for (Map.Entry<FunctionDef, GeneratedFunction> entry : functionMap.entries()) {
			FunctionInvocation fi = new FunctionInvocation(entry.getKey(), null);
			for (Triplet triplet : forFunctions) {
//				Collection<GeneratedFunction> x = functionMap.get(fi);
				triplet.forFunction.finish();
			}
		}
*/
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
		List<GeneratedClass> gcs = new ArrayList<GeneratedClass>();
		boolean all_resolve_var_table_entries = false;
		while (!all_resolve_var_table_entries) {
			if (generatedClasses.size() == 0) break;
			for (GeneratedNode generatedNode : new ArrayList<>(generatedClasses)) {
				if (generatedNode instanceof GeneratedClass) {
					final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					all_resolve_var_table_entries = generatedClass.resolve_var_table_entries(this); // TODO use a while loop to get all classes
				}
			}
		}
		sanityChecks();
	}

	private void sanityChecks() {
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				sanityChecks(generatedClass.functionMap.values());
				sanityChecks(generatedClass.constructors.values());
			} else if (generatedNode instanceof GeneratedNamespace) {
				final GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				sanityChecks(generatedNamespace.functionMap.values());
//				sanityChecks(generatedNamespace.constructors.values());
			}
		}
	}

	private void sanityChecks(@NotNull Collection<GeneratedFunction> aGeneratedFunctions) {
		for (GeneratedFunction generatedFunction : aGeneratedFunctions) {
			for (IdentTableEntry identTableEntry : generatedFunction.idte_list) {
				switch (identTableEntry.getStatus()) {
					case UNKNOWN:
						assert identTableEntry.resolved_element == null;
						System.err.println(String.format("250 UNKNOWN idte %s in %s", identTableEntry, generatedFunction));
						break;
					case KNOWN:
						assert identTableEntry.resolved_element != null;
						if (identTableEntry.type == null) {
							System.err.println(String.format("258 null type in KNOWN idte %s in %s", identTableEntry, generatedFunction));
						}
						break;
					case UNCHECKED:
						System.err.println(String.format("255 UNCHECKED idte %s in %s", identTableEntry, generatedFunction));
						break;
				}
				for (TypeTableEntry pot_tte : identTableEntry.potentialTypes()) {
					if (pot_tte.attached == null) {
						System.err.println(String.format("267 null potential attached in %s in %s in %s", pot_tte, identTableEntry, generatedFunction));
					}
				}
			}
		}
	}

}

//
//
//
