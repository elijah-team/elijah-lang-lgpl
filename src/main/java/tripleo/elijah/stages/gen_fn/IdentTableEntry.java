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
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.stages.instructions.InstructionArgument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 9/12/20 10:27 PM
 */
public class IdentTableEntry {
    final int index;
    private final IdentExpression ident;
	/**
	 * Either an {@link tripleo.elijah.stages.instructions.IntegerIA} which is a vte
	 * or a {@link tripleo.elijah.stages.instructions.IdentIA} which is an idte
	 */
	public InstructionArgument backlink;
	public @NotNull Map<Integer, TypeTableEntry> potentialTypes = new HashMap<Integer, TypeTableEntry>();
	public TypeTableEntry type;
	private GeneratedNode resolved;

	public IdentTableEntry(final int index, final IdentExpression ident) {
        this.index = index;
        this.ident = ident;
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
				", type=" + type +
				", resolved=" + resolved +
				'}';
	}

	public IdentExpression getIdent() {
		return ident;
	}

	public void resolve(GeneratedNode gn) {
		resolved = gn;
	}

	public boolean isResolved() {
		return resolved != null;
	}
}

//
//
//
