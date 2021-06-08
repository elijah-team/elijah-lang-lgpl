/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jdeferred2.impl.DeferredObject;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.WlGenerateFunction;

/**
 * Created 1/21/21 9:04 PM
 */
public class FunctionInvocation {
	private final FunctionDef fd;
	final ProcTableEntry pte;
	private ClassInvocation classInvocation;
	private NamespaceInvocation namespaceInvocation;
	private final DeferredObject<GeneratedFunction, Void, Void> generateDeferred = new DeferredObject<GeneratedFunction, Void, Void>();
	private GeneratedFunction _generated = null;

	public FunctionInvocation(FunctionDef aFunctionDef, ProcTableEntry aProcTableEntry, Object invocation, GeneratePhase phase) {
		this.fd = aFunctionDef;
		this.pte = aProcTableEntry;
		if (invocation instanceof ClassInvocation)
			setClassInvocation((ClassInvocation) invocation);
		else if (invocation instanceof NamespaceInvocation)
			setNamespaceInvocation((NamespaceInvocation) invocation);
		else if (invocation == null)
			;
		else
			throw new IllegalArgumentException("Unknown invocation");
		setPhase(phase);
	}

	public void setPhase(final GeneratePhase generatePhase) {
		if (pte != null)
			pte.completeDeferred().then(new DoneCallback<ProcTableEntry>() {
				@Override
				public void onDone(ProcTableEntry result) {
					makeGenerated(generatePhase);
				}
			});
		else
			makeGenerated(generatePhase);
	}

	private void makeGenerated(GeneratePhase generatePhase) {
		WlGenerateFunction wlgf = new WlGenerateFunction(generatePhase.getGenerateFunctions(fd.getContext().module()), this);
		wlgf.run(null);
		GeneratedFunction gf = wlgf.getResult();
		if (generateDeferred.isPending())
			generateDeferred.resolve(gf);
		_generated = gf;
	}

	public GeneratedFunction getGenerated() {
		return _generated;
	}

	public FunctionDef getFunction() {
		return fd;
	}

	public void setClassInvocation(ClassInvocation aClassInvocation) {
		classInvocation = aClassInvocation;
	}

	public ClassInvocation getClassInvocation() {
		return classInvocation;
	}

	public NamespaceInvocation getNamespaceInvocation() {
		return namespaceInvocation;
	}

	public void setNamespaceInvocation(NamespaceInvocation aNamespaceInvocation) {
		namespaceInvocation = aNamespaceInvocation;
	}

	public DeferredObject<GeneratedFunction, Void, Void> generateDeferred() {
		return generateDeferred;
	}

	public void setGenerated(GeneratedFunction aGeneratedFunction) {
		_generated = aGeneratedFunction;
	}
}

//
//
//
