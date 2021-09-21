/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jdeferred2.DoneCallback;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.stages.deduce.DeducePath;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.OnType;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.stages.instructions.IntegerIA;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 9/12/20 10:27 PM
 */
public class IdentTableEntry extends BaseTableEntry1 implements Constructable, TableEntryIV, DeduceTypes2.ExpectationBase {
    private final int index;
    private final IdentExpression ident;
	private final Context pc;
	/**
	 * Either an {@link IntegerIA} which is a vte
	 * or a {@link IdentIA} which is an idte
	 */
	public InstructionArgument backlink;
	public @NotNull Map<Integer, TypeTableEntry> potentialTypes = new HashMap<Integer, TypeTableEntry>();
	public TypeTableEntry type;
	public GeneratedNode externalRef;
	public boolean fefi = false;
	private GeneratedNode resolvedType;
	public ProcTableEntry constructable_pte;

	public DeduceTypes2.PromiseExpectation<String> resolveExpectation;

	public IdentTableEntry(final int index, final IdentExpression ident, Context pc) {
        this.index  = index;
        this.ident  = ident;
        this.pc     = pc;
        addStatusListener(new StatusListener() {
			@Override
			public void onChange(IElementHolder eh, Status newStatus) {
				if (newStatus == Status.KNOWN) {
					setResolvedElement(eh.getElement());
				}
			}
		});
    }

	public void addPotentialType(final int instructionIndex, final TypeTableEntry tte) {
		potentialTypes.put(instructionIndex, tte);
	}

	@SuppressFBWarnings("NP_NONNULL_RETURN_VIOLATION")
	public @NotNull Collection<TypeTableEntry> potentialTypes() {
		return potentialTypes.values();
	}

	@Override
	public @NotNull String toString() {
		return "IdentTableEntry{" +
				"index=" + index +
				", ident=" + ident +
				", backlink=" + backlink +
				", potentialTypes=" + potentialTypes +
				", status=" + status +
				", type=" + type +
				", resolved=" + resolvedType +
				'}';
	}

	public IdentExpression getIdent() {
		return ident;
	}

	@Override
	public void resolveTypeToClass(GeneratedNode gn) {
		resolvedType = gn;
		if (type != null) // TODO maybe find a more robust solution to this, like another Promise? or just setType? or onPossiblesResolve?
			type.resolve(gn); // TODO maybe this obviates the above?
	}

	@Override
	public void setGenType(GenType aGenType) {
		if (type != null) {
			type.genType.copy(aGenType);
		}
	}

	public boolean isResolved() {
		return resolvedType != null;
	}

	public GeneratedNode resolvedType() {
		return resolvedType;
	}

	public boolean hasResolvedElement() {
		return resolved_element != null;
	}

	public int getIndex() {
		return index;
	}

	public Context getPC() {
		return pc;
	}

	public void onType(@NotNull DeducePhase phase, OnType callback) {
		phase.onType(this, callback);
	}

	@Override
	public void setConstructable(ProcTableEntry aPte) {
		constructable_pte = aPte;
	}

	public DeducePath buildDeducePath(BaseGeneratedFunction generatedFunction) {
		@NotNull List<InstructionArgument> x = generatedFunction._getIdentIAPathList(new IdentIA(index, generatedFunction));
		return new DeducePath(this, x);
	}

	@Override
	public String expectationString() {
		return "IdentTableEntry{" +
				"index=" + index +
				", ident=" + ident +
				", backlink=" + backlink +
				"}";
	}

	private DeferredObject<GenType, Void, Void> fefiDone = new DeferredObject<GenType, Void, Void>();

	public void fefiDone(final GenType aGenType) {
		if (fefiDone.isPending())
			fefiDone.resolve(aGenType);
	}

	public void onFefiDone(DoneCallback<GenType> aCallback) {
		fefiDone.then(aCallback);
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
