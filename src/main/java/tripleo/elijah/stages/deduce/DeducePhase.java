/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
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
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.entrypoints.EntryPoint;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.declarations.DeferredMember;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.work.WorkList;

import java.util.*;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 12/24/20 3:59 AM
 */
public class DeducePhase {

	private final List<FoundElement> foundElements = new ArrayList<FoundElement>();
	private final Map<IdentTableEntry, OnType> idte_type_callbacks = new HashMap<IdentTableEntry, OnType>();
	public @NotNull GeneratedClasses generatedClasses = new GeneratedClasses();
	public final GeneratePhase generatePhase;

	final PipelineLogic pipelineLogic;

	private final @NotNull ElLog LOG;

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

	@NotNull Multimap<OS_Element, ResolvedVariables> resolved_variables = ArrayListMultimap.create();

	public void registerResolvedVariable(IdentTableEntry identTableEntry, OS_Element parent, String varName) {
		resolved_variables.put(parent, new ResolvedVariables(identTableEntry, parent, varName));
	}

	@NotNull Multimap<ClassStatement, OnClass> onclasses = ArrayListMultimap.create();

	public void onClass(ClassStatement aClassStatement, OnClass callback) {
		onclasses.put(aClassStatement, callback);
	}

//	Multimap<GeneratedClass, ClassInvocation> generatedClasses1 = ArrayListMultimap.create();
@NotNull Multimap<ClassStatement, ClassInvocation> classInvocationMultimap = ArrayListMultimap.create();

	public @Nullable ClassInvocation registerClassInvocation(@NotNull ClassInvocation aClassInvocation) {
		boolean put = false;
		@Nullable ClassInvocation Result = null;

		// 1. select which to return
		ClassStatement c = aClassInvocation.getKlass();
		Collection<ClassInvocation> cis = classInvocationMultimap.get(c);
		for (@NotNull ClassInvocation ci : cis) {
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
		for (@NotNull ClassInvocation ci : cls) {
			if (equivalentGenericPart(ci, Result)) {
				return ci;
			}
		}

		if (put) {
			classInvocationMultimap.put(aClassInvocation.getKlass(), aClassInvocation);
		}

		// 3. Generate new GeneratedClass
		final @NotNull WorkList wl = new WorkList();
		final @NotNull OS_Module mod = Result.getKlass().getContext().module();
		wl.addJob(new WlGenerateClass(generatePhase.getGenerateFunctions(mod), Result, generatedClasses)); // TODO why add now?
		generatePhase.wm.addJobs(wl);
		generatePhase.wm.drain(); // TODO find a better place to put this

		// 4. Return it
		return Result;
	}

	public boolean equivalentGenericPart(@NotNull ClassInvocation first, @NotNull ClassInvocation second) {
		Map<TypeName, OS_Type> firstGenericPart = first.genericPart;
		Map<TypeName, OS_Type> secondGenericPart = second.genericPart;
		if (secondGenericPart == null && (firstGenericPart == null || firstGenericPart.size() == 0)) return true;
		//
		int i = secondGenericPart.entrySet().size();
		for (Map.@NotNull Entry<TypeName, OS_Type> entry : secondGenericPart.entrySet()) {
			final OS_Type entry_type = firstGenericPart.get(entry.getKey());
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

		@NotNull NamespaceInvocation nsi = new NamespaceInvocation(aNamespaceStatement);
		namespaceInvocationMap.put(aNamespaceStatement, nsi);
		return nsi;
	}

	@NotNull List<FunctionMapHook> functionMapHooks = new ArrayList<FunctionMapHook>();

	public void addFunctionMapHook(FunctionMapHook aFunctionMapHook) {
		functionMapHooks.add(aFunctionMapHook);
	}

	@NotNull List<DeferredMember> deferredMembers = new ArrayList<DeferredMember>();

	public void addDeferredMember(DeferredMember aDeferredMember) {
		deferredMembers.add(aDeferredMember);
	}

//	public List<ElLog> deduceLogs = new ArrayList<ElLog>();

	public void addLog(ElLog aLog) {
		//deduceLogs.add(aLog);
		pipelineLogic.addLog(aLog);
	}

	// helper function. no generics!
	public @Nullable ClassInvocation registerClassInvocation(ClassStatement aParent, String aO) {
		@Nullable ClassInvocation ci = new ClassInvocation((ClassStatement) aParent, aO);
		ci = registerClassInvocation(ci);
		return ci;
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

	public @NotNull DeduceTypes2 deduceModule(@NotNull OS_Module m, @NotNull Iterable<GeneratedNode> lgf, ElLog.Verbosity verbosity) {
		final @NotNull DeduceTypes2 deduceTypes2 = new DeduceTypes2(m, this, verbosity);
//		LOG.err("196 DeduceTypes "+deduceTypes2.getFileName());
		{
			final ArrayList<GeneratedNode> p = new ArrayList<GeneratedNode>();
			Iterables.addAll(p, lgf);
			LOG.info("197 lgf.size " + p.size());
		}
		deduceTypes2.deduceFunctions(lgf);
		deduceTypes2.deduceClasses(generatedClasses.copy().stream()
				.filter(c -> c.module() == m)
				.collect(Collectors.toList()));
		return deduceTypes2;
	}

	public @NotNull DeduceTypes2 deduceModule(@NotNull OS_Module m, ElLog.Verbosity verbosity) {
		final @NotNull GenerateFunctions gfm = generatePhase.getGenerateFunctions(m);

		@NotNull List<EntryPoint> epl = m.entryPoints;
		gfm.generateFromEntryPoints(epl, this);

		@NotNull GeneratedClasses lgc = generatedClasses;

		final @NotNull List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();
		for (GeneratedNode lgci : lgc) {
			if (lgci instanceof GeneratedClass) {
				final @NotNull Collection<GeneratedFunction> generatedFunctions = ((GeneratedClass) lgci).functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : generatedFunctions) {
					generatedFunction.setClass(lgci);
				}
				lgf.addAll(generatedFunctions);
			}
			if (lgci instanceof GeneratedNamespace) {
				final @NotNull Collection<GeneratedFunction> generatedFunctions = ((GeneratedNamespace) lgci).functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : generatedFunctions) {
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
	public void deduceModule(@NotNull OS_Module m, @NotNull Iterable<GeneratedNode> lgc, boolean _unused, ElLog.Verbosity verbosity) {
		final @NotNull List<GeneratedNode> lgf = new ArrayList<GeneratedNode>();

		for (@NotNull GeneratedNode lgci : lgc) {
			if (lgci.module() != m) continue;

			if (lgci instanceof GeneratedClass) {
				final @NotNull Collection<GeneratedFunction> generatedFunctions = ((GeneratedClass) lgci).functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : generatedFunctions) {
//					generatedFunction.setClass(lgci); // TODO delete when done
					assert generatedFunction.getGenClass() == lgci;
				}
				lgf.addAll(generatedFunctions);
			} else if (lgci instanceof GeneratedNamespace) {
				final @NotNull Collection<GeneratedFunction> generatedFunctions = ((GeneratedNamespace) lgci).functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : generatedFunctions) {
					generatedFunction.setClass(lgci);
				}
				lgf.addAll(generatedFunctions);
			}
		}

		deduceModule(m, lgf, verbosity);
	}

	public void forFunction(DeduceTypes2 deduceTypes2, @NotNull FunctionInvocation fi, @NotNull ForFunction forFunction) {
//		LOG.err("272 forFunction\n\t"+fi.getFunction()+"\n\t"+fi.pte);
		fi.generateDeferred().promise().then(new DoneCallback<BaseGeneratedFunction>() {
			@Override
			public void onDone(@NotNull BaseGeneratedFunction result) {
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

	public void typeDecided(@NotNull GeneratedFunction gf, final GenType aType) {
		gf.typeDeferred().resolve(aType);
//		typeDecideds.put(gf, aType);
	}

	public void finish() {
		// TODO all GeneratedFunction nodes have a genClass member
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final @NotNull GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				@NotNull Collection<GeneratedFunction> functions = generatedClass.functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : functions) {
					generatedFunction.setParent(generatedClass);
				}
			} else if (generatedNode instanceof GeneratedNamespace) {
				final @NotNull GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				@NotNull Collection<GeneratedFunction> functions = generatedNamespace.functionMap.values();
				for (@NotNull GeneratedFunction generatedFunction : functions) {
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
					final @NotNull GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					if (generatedClass.getKlass() == classStatement) {
						Collection<OnClass> ks = onclasses.get(classStatement);
						for (@NotNull OnClass k : ks) {
							k.classFound(generatedClass);
						}
					} else {
						@NotNull Collection<GeneratedClass> cmv = generatedClass.classMap.values();
						for (@NotNull GeneratedClass aClass : cmv) {
							if (aClass.getKlass() == classStatement) {
								Collection<OnClass> ks = onclasses.get(classStatement);
								for (@NotNull OnClass k : ks) {
									k.classFound(generatedClass);
								}
							}
						}
					}
				}
			}
		}
		for (Map.@NotNull Entry<IdentTableEntry, OnType> entry : idte_type_callbacks.entrySet()) {
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
		for (@NotNull FoundElement foundElement : foundElements) {
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
				final @NotNull GeneratedContainer generatedContainer = (GeneratedContainer) generatedNode;
				Collection<ResolvedVariables> x = resolved_variables.get(generatedContainer.getElement());
				for (@NotNull ResolvedVariables resolvedVariables : x) {
					final GeneratedContainer.VarTableEntry variable = generatedContainer.getVariable(resolvedVariables.varName);
					assert variable != null;
					final TypeTableEntry type = resolvedVariables.identTableEntry.type;
					if (type != null)
						variable.addPotentialTypes(List_of(type));
					variable.addPotentialTypes(resolvedVariables.identTableEntry.potentialTypes());
				}
			}
		}
		@NotNull List<GeneratedClass> gcs = new ArrayList<GeneratedClass>();
		boolean all_resolve_var_table_entries = false;
		while (!all_resolve_var_table_entries) {
			if (generatedClasses.size() == 0) break;
			for (GeneratedNode generatedNode : generatedClasses.copy()) {
				if (generatedNode instanceof GeneratedClass) {
					final @NotNull GeneratedClass generatedClass = (GeneratedClass) generatedNode;
					all_resolve_var_table_entries = generatedClass.resolve_var_table_entries(this); // TODO use a while loop to get all classes
				}
			}
		}
		for (@NotNull DeferredMember deferredMember : deferredMembers) {
			if (deferredMember.getParent() instanceof NamespaceStatement) {
				final @NotNull NamespaceStatement parent = (NamespaceStatement) deferredMember.getParent();
				final NamespaceInvocation nsi = registerNamespaceInvocation(parent);
				nsi.resolveDeferred()
						.done(new DoneCallback<GeneratedNamespace>() {
							@Override
							public void onDone(@NotNull GeneratedNamespace result) {
								GeneratedContainer.@Nullable VarTableEntry v = result.getVariable(deferredMember.getVariableStatement().getName());
								assert v != null;
								// TODO varType, potentialTypes and _resolved: which?
								final OS_Type varType = v.varType;
								final @NotNull GenType genType = new GenType();
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
				final ClassStatement parent = (ClassStatement) deferredMember.getParent();
				final String name = deferredMember.getVariableStatement().getName();

				// because deferredMember.invocation is null, we must create one here
				final @Nullable ClassInvocation ci = registerClassInvocation(parent, null);
				ci.resolvePromise().then(new DoneCallback<GeneratedClass>() {
					@Override
					public void onDone(final GeneratedClass result) {
						final List<GeneratedContainer.VarTableEntry> vt = result.varTable;
						for (GeneratedContainer.VarTableEntry gc_vte : vt) {
							if (gc_vte.nameToken.getText().equals(name)) {
								// check connections
								// unify pot. types (prol. shuld be done already -- we don't want to be reporting errors here)
								// call typePromises and externalRefPromisess

								// TODO just getting first element here (without processing of any kind); HACK
								final GenType ty = gc_vte.connectionPairs.get(0).vte.type.genType;
								assert ty.resolved != null;
								gc_vte.varType = ty.resolved; // TODO make sure this is right in all cases
								if (deferredMember.typeResolved().isPending())
									deferredMember.typeResolved().resolve(ty);
								break;
							}
						}
					}
				});
			} else
				throw new NotImplementedException();
		}
		sanityChecks();
		for (Map.@NotNull Entry<FunctionDef, Collection<GeneratedFunction>> entry : functionMap.asMap().entrySet()) {
			for (@NotNull FunctionMapHook functionMapHook : functionMapHooks) {
				if (functionMapHook.matches(entry.getKey())) {
					functionMapHook.apply(entry.getValue());
				}
			}
		}
	}

	private void sanityChecks() {
		for (GeneratedNode generatedNode : generatedClasses) {
			if (generatedNode instanceof GeneratedClass) {
				final @NotNull GeneratedClass generatedClass = (GeneratedClass) generatedNode;
				sanityChecks(generatedClass.functionMap.values());
//				sanityChecks(generatedClass.constructors.values()); // TODO reenable
			} else if (generatedNode instanceof GeneratedNamespace) {
				final @NotNull GeneratedNamespace generatedNamespace = (GeneratedNamespace) generatedNode;
				sanityChecks(generatedNamespace.functionMap.values());
//				sanityChecks(generatedNamespace.constructors.values());
			}
		}
	}

	private void sanityChecks(@NotNull Collection<GeneratedFunction> aGeneratedFunctions) {
		for (@NotNull GeneratedFunction generatedFunction : aGeneratedFunctions) {
			for (@NotNull IdentTableEntry identTableEntry : generatedFunction.idte_list) {
				switch (identTableEntry.getStatus()) {
					case UNKNOWN:
						assert identTableEntry.getResolvedElement() == null;
						LOG.err(String.format("250 UNKNOWN idte %s in %s", identTableEntry, generatedFunction));
						break;
					case KNOWN:
						assert identTableEntry.getResolvedElement() != null;
						if (identTableEntry.type == null) {
							LOG.err(String.format("258 null type in KNOWN idte %s in %s", identTableEntry, generatedFunction));
						}
						break;
					case UNCHECKED:
						LOG.err(String.format("255 UNCHECKED idte %s in %s", identTableEntry, generatedFunction));
						break;
				}
				for (@NotNull TypeTableEntry pot_tte : identTableEntry.potentialTypes()) {
					if (pot_tte.getAttached() == null) {
						LOG.err(String.format("267 null potential attached in %s in %s in %s", pot_tte, identTableEntry, generatedFunction));
					}
				}
			}
		}
	}

	public static class GeneratedClasses implements Iterable<GeneratedNode> {
		@NotNull List<GeneratedNode> generatedClasses = new ArrayList<GeneratedNode>();

		public void add(GeneratedNode aClass) {
			generatedClasses.add(aClass);
		}

		@Override
		public Iterator<GeneratedNode> iterator() {
			return generatedClasses.iterator();
		}

		public int size() {
			return generatedClasses.size();
		}

		public List<GeneratedNode> copy() {
			return new ArrayList<GeneratedNode>(generatedClasses);
		}

		public void addAll(List<GeneratedNode> lgc) {
			// TODO is this method really needed
			generatedClasses.addAll(lgc);
		}
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
