/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jdeferred2.*;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.instructions.VariableTableType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created 9/10/20 4:51 PM
 */
public class VariableTableEntry extends BaseTableEntry implements Constructable, TableEntryIV {
	private final int index;
	private final String name;
	public final OS_Element el;
	public TypeTableEntry type;
	public final VariableTableType vtt;
	public @NotNull Map<Integer, TypeTableEntry> potentialTypes = new HashMap<Integer, TypeTableEntry>();
	public int tempNum = -1;
	public ProcTableEntry constructable_pte;

	public VariableTableEntry(final int index, final VariableTableType var1, final String name, final TypeTableEntry type, final OS_Element el) {
		this.index = index;
		this.name = name;
		this.vtt = var1;
		this.type = type;
		this.el = el;
	}

	@Override
	public @NotNull String toString() {
		return "VariableTableEntry{" +
				"index=" + index +
				", name='" + name + '\'' +
				", status=" + status +
				", type=" + type.index +
				", vtt=" + vtt +
				", potentialTypes=" + potentialTypes +
				'}';
	}

	public String getName() {
		return name;
	}

	public void addPotentialType(final int instructionIndex, final TypeTableEntry tte) {
		assert typeDeferred.isPending();
		//
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
			} else if (v.attached.equals(tte.attached)) {
				// leave as is
			} else {
				//
				// Make sure you check the differences between USER and USER_CLASS types
				// May not be any
				//
//				System.err.println("v.attached: " + v.attached);
//				System.err.println("tte.attached: " + tte.attached);
				System.out.println("72 WARNING two types at the same location.");
				if ((tte.attached != null && tte.attached.getType() != OS_Type.Type.USER) || v.attached.getType() != OS_Type.Type.USER_CLASS) {
					// TODO prefer USER_CLASS as we are assuming it is a resolved version of the other one
					if (tte.attached == null)
						tte.attached = v.attached;
					else if (tte.attached.getType() == OS_Type.Type.USER_CLASS)
						v.attached = tte.attached;
				}
			}
		}
	}

	public @NotNull Collection<TypeTableEntry> potentialTypes() {
		return potentialTypes.values();
	}

	public int getIndex() {
		return index;
	}

	public DeferredObject<TypeTableEntry, Void, Void> typeDeferred = new DeferredObject<TypeTableEntry, Void, Void>();

	public Promise<TypeTableEntry, Void, Void> promise() {
		return typeDeferred.promise();
	}

	@Override
	public void setConstructable(ProcTableEntry aPte) {
		constructable_pte = aPte;
	}
}

//
//
//
