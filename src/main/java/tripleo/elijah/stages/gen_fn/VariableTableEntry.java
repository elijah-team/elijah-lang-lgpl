/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.instructions.VariableTableType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 9/10/20 4:51 PM
 */
public class VariableTableEntry {
	private final int index;
	private final String name;
	public TypeTableEntry type;
	public final VariableTableType vtt;
	public @NotNull Map<Integer, TypeTableEntry> potentialTypes = new HashMap<Integer, TypeTableEntry>();
	public int tempNum = -1;

	public VariableTableEntry(final int index, final VariableTableType var1, final String name, final TypeTableEntry type) {
		this.index = index;
		this.name = name;
		this.vtt = var1;
		this.type = type;
	}

	@Override
	public @NotNull String toString() {
		return "VariableTableEntry{" +
				"index=" + index +
				", name='" + name + '\'' +
				", type=" + type.index +
				", vtt=" + vtt +
				", potentialTypes=" + potentialTypes +
				'}';
	}

	public String getName() {
		return name;
	}

	public void addPotentialType(final int instructionIndex, final TypeTableEntry tte) {
		if (!potentialTypes.containsKey(instructionIndex))
			potentialTypes.put(instructionIndex, tte);
		else {
			TypeTableEntry v = potentialTypes.get(instructionIndex);
			if (v.attached == null)
				v.attached = tte.attached;
			else if (tte.lifetime == TypeTableEntry.Type.TRANSIENT && v.lifetime == TypeTableEntry.Type.SPECIFIED) {
				//v.attached = v.attached; // leave it as is
			} else if (tte.lifetime == v.lifetime && v.attached == tte.attached) {
				// leave as is
			} else if (tte.attached.equals(v.attached)) {
				// leave as is
			} else
				assert false;
		}
	}

	public @NotNull Collection<TypeTableEntry> potentialTypes() {
		return potentialTypes.values();
	}

	public int getIndex() {
		return index;
	}
}

//
//
//
