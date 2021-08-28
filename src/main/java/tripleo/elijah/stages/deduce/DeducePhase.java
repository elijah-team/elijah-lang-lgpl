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
import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.entrypoints.EntryPoint;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.OS_UnknownType;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.deduce.declarations.DeferredMember;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.NotImplementedException;
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

	final PipelineLogic pipelineLogic;

	private final ElLog LOG;

	public DeducePhase(GeneratePhase aGeneratePhase, PipelineLogic aPipelineLogic, ElLog.Verbosity verbosity) {
		generatePhase = aGeneratePhase;
		pipelineLogic = aPipelineLogic;
		//
		LOG = new ElLog("(DEDUCE_PHASE)", verbosity, "DeducePhase");
		pipelineLogic.addLog(LOG);
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

	List<FunctionMapHook> functionMapHooks = new ArrayList<FunctionMapHook>();

	public void addFunctionMapHook(FunctionMapHook aFunctionMapHook) {
		functionMapHooks.add(aFunctionMapHook);
	}

	List<DeferredMember> deferredMembers = new ArrayList<DeferredMember>();

	public void addDeferredMember(DeferredMember aDeferredMember) {
		deferredMembers.add(aDeferredMember);
	}

//	public List<ElLog> deduceLogs = new ArrayList<ElLog>();

	public void addLog(ElLog aLog) {
		//deduceLogs.add(aLog);
		pipelineLogic.addLog(aLog);
	}

	static class ResolvedVariables {
		final IdentTableEntry identTableEntry;
		final OS_Element parent; // README tripleo.elijah.lang._CommonNC, but that's package-private
		final String varName;

		public ResolvedVariables(IdentTableEntry aIdentTableEntry, OS_Element aParent, String aVarName) {
			assert aParent instanceof ClassStatement || aParent instanceof NamespaceStatement;

			identTableEntry = aIdentTableEntry;
			parent = aParent;
			varName = aVarName;
		}
	}

	private final Multimap<FunctionDef, GeneratedFunction> functionMap = ArrayListMultimap.create();

	public DeduceTypes2 deduceModule(OS_Module m, Iterable<GeneratedNode> lgf, ElLog.Verbosity verbosity) {
		final DeduceTypes2 deduceTypes2 = new DeduceTypes2(m, this, verbosity);
//		LOG.err("196 DeduceTypes "+deduceTypes2.getFileName());
		deduceTypes2.deduceFunctions(lgf);
		return deduceTypes2;
	}

	public DeduceTypes2 deduceModule(OS_Module m, ElLog.Verbosity verbosity) {
		final GenerateFunctions gfm = generatePhase.getGenerateFunctions(m);

		@NotNull List<EntryPoint> epl = m.entryPoints;
		gfm.generateFromEntryPoints(epl, this);

		List<GeneratedNode> lgc = generatedClasses;

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

		return deduceModule(m, lgf, verbosity);
	}

	/**
	 * Use this when you have already called generateAllTopLevelClasses
	 * @param m the module
	 * @param lgc the result of generateAllTopLevelClasses
	 * @param _unused is unused
	 * @param verbosity
	 */
	public void deduceModule(OS_Module m, Iterable<GeneratedNode> lgc, boolean _unused, ElLog.Verbosity verbosity) {
		final List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();

		for (GeneratedNode lgci : lgc) {
			if (lgci.module() != m) continue;

			if (lgci instanceof GeneratedClass) {
				final Collection<GeneratedFunction> generatedFunctions = ((GeneratedClass) lgci).functionMap.values();
					for (GeneratedFunction generatedFunction : generatedFunctions) {
//						generatedFunction.setClass(lgci); // TODO delete when done
						assert generatedFunction.getGenClass() == lgci;
					}
				lgf.addAll(generatedFunctions);
			} else if (lgci instanceof GeneratedNamespace) {
				final Collection<GeneratedFunction> generatedFunctions = ((GeneratedNamespace) lgci).functionMap.values();
				for (GeneratedFunction generatedFunction : generatedFunctions) {
					generatedFunction.setClass(lgci);
				}
				lgf.addAll(generatedFunctions);
			}
		}

		deduceModule(m, lgf, verbosity);
	}

	public void forFunction(DeduceTypes2 deduceTypes2, FunctionInvocation fi, ForFunction forFunction) {
//		LOG.err("272 forFunction\n\t"+fi.getFunction()+"\n\t"+fi.pte);
		fi.generateDeferred().promise().then(new DoneCallback<BaseGeneratedFunction>() {
			@Override
			public void onDone(BaseGeneratedFunction result) {
				result.typePromise().then(new DoneCallback<GenType>() {
					@Override
					public void onDone(GenType result) {
						forFunction.typeDecided(result);
					}
				});
			}
		});
	}

//	Map<GeneratedFunction, OS_Type> typeDecideds = new HashMap<GeneratedFunction, OS_Type>();

	public void typeDecided(GeneratedFunction gf, final GenType aType) {
		gf.typeDeferred().resolve(aType);
//		typeDecideds.put(gf, aType);
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
					idte.type.getAttached() != null)
				entry.getValue().typeDeduced(idte.type.getAttached());
			else
				entry.getValue().noTypeFound();
		}
/*
		for (Map.Entry<GeneratedFunction, OS_Type> entry : typeDecideds.entrySet()) {
			for (Triplet triplet : forFunctions) {
				if (triplet.gf.getGenerated() == entry.getKey()) {
					synchronized (triplet.deduceTypes2) {
						triplet.forFunction.typeDecided(entry.getValue());
					}
				}
			}
		}
*/
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
		for (DeferredMember deferredMember : deferredMembers) {
			if (deferredMember.getParent() instanceof NamespaceStatement) {
				final NamespaceStatement parent = (NamespaceStatement) deferredMember.getParent();
				final NamespaceInvocation nsi = registerNamespaceInvocation(parent);
				nsi.resolveDeferred()
						.done(new DoneCallback<GeneratedNamespace>() {
							@Override
							public void onDone(GeneratedNamespace result) {
								GeneratedContainer.VarTableEntry v = result.getVariable(deferredMember.getVariableStatement().getName());
								assert v != null;
								// TODO varType, potentialTypes and _resolved: which?
								final OS_Type varType = v.varType;
								final GenType genType = new GenType();
								genType.set(varType);

//								if (deferredMember.getInvocation() instanceof NamespaceInvocation) {
//									((NamespaceInvocation) deferredMember.getInvocation()).resolveDeferred().done(new DoneCallback<GeneratedNamespace>() {
//										@Override
//										public void onDone(GeneratedNamespace result) {
//											result;
//										}
//									});
//								}

								deferredMember.externalRefDeferred().resolve(result);
/*
								if (genType.resolved == null) {
									// HACK need to resolve, but this shouldn't be here
									try {
										@NotNull OS_Type rt = DeduceTypes2.resolve_type(null, varType, varType.getTypeName().getContext());
										genType.set(rt);
									} catch (ResolveError aResolveError) {
										aResolveError.printStackTrace();
									}
								}
								deferredMember.typeResolved().resolve(genType);
*/
							}
						});
			} else if (deferredMember.getParent() instanceof ClassStatement) {
				// TODO do something
			} else
				throw new NotImplementedException();
		}
		sanityChecks();
		for (Map.Entry<FunctionDef, Collection<GeneratedFunction>> entry : functionMap.asMap().entrySet()) {
			for (FunctionMapHook functionMapHook : functionMapHooks) {
				if (functionMapHook.matches(entry.getKey())) {
					functionMapHook.apply(entry.getValue());
				}
			}
		}
	}

	private void sanityChecks() {
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				sanityChecks(generatedClass.functionMap.values());
//				sanityChecks(generatedClass.constructors.values()); // TODO reenable
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
						LOG.err(String.format("250 UNKNOWN idte %s in %s", identTableEntry, generatedFunction));
						break;
					case KNOWN:
						assert identTableEntry.resolved_element != null;
						if (identTableEntry.type == null) {
							LOG.err(String.format("258 null type in KNOWN idte %s in %s", identTableEntry, generatedFunction));
						}
						break;
					case UNCHECKED:
						LOG.err(String.format("255 UNCHECKED idte %s in %s", identTableEntry, generatedFunction));
						break;
				}
				for (TypeTableEntry pot_tte : identTableEntry.potentialTypes()) {
					if (pot_tte.getAttached() == null) {
						LOG.err(String.format("267 null potential attached in %s in %s in %s", pot_tte, identTableEntry, generatedFunction));
					}
				}
			}
		}
	}

}

//
//
//
