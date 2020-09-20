/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.stages.instructions.VariableTableType;

/**
 * Created 9/10/20 4:51 PM
 */
public class VariableTableEntry {
	public final int index;
	private final String name;
	public TypeTableEntry type;
	private final VariableTableType vtt;
	Map<Integer, TypeTableEntry> potentialTypes = new HashMap<Integer, TypeTableEntry>();

	public VariableTableEntry(int index, VariableTableType var1, String name, TypeTableEntry type) {
		this.index = index;
		this.name = name;
		this.vtt = var1;
		this.type = type;
	}

	@Override
	public String toString() {
		return "VariableTableEntry{" +
				"index=" + index +
				", name='" + name + '\'' +
				", type=" + type +
				", vtt=" + vtt +
				'}';
	}

	public String getName() {
		return name;
	}

	public void addPotentialType(int instructionIndex, TypeTableEntry tte) {
		potentialTypes.put(instructionIndex, tte);
	}
}

//
//
//
