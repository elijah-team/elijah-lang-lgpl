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
	public GenType genType = new GenType();
	private GeneratedNode _resolvedType;

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
			if (v.getAttached() == null)
				v.setAttached(tte.getAttached());
			else if (tte.lifetime == TypeTableEntry.Type.TRANSIENT && v.lifetime == TypeTableEntry.Type.SPECIFIED) {
				//v.attached = v.attached; // leave it as is
			} else if (tte.lifetime == v.lifetime && v.getAttached() == tte.getAttached()) {
				// leave as is
			} else if (v.getAttached().equals(tte.getAttached())) {
				// leave as is
			} else {
				//
				// Make sure you check the differences between USER and USER_CLASS types
				// May not be any
				//
//				System.err.println("v.attached: " + v.attached);
//				System.err.println("tte.attached: " + tte.attached);
				System.out.println("72 WARNING two types at the same location.");
				if ((tte.getAttached() != null && tte.getAttached().getType() != OS_Type.Type.USER) || v.getAttached().getType() != OS_Type.Type.USER_CLASS) {
					// TODO prefer USER_CLASS as we are assuming it is a resolved version of the other one
					if (tte.getAttached() == null)
						tte.setAttached(v.getAttached());
					else if (tte.getAttached().getType() == OS_Type.Type.USER_CLASS)
						v.setAttached(tte.getAttached());
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

	private DeferredObject<GenType, Void, Void> typeDeferred = new DeferredObject<GenType, Void, Void>();

	public Promise<GenType, Void, Void> typePromise() {
		return typeDeferred.promise();
	}

	public DeferredObject<GenType, Void, Void> typeDeferred() {
		return typeDeferred;
	}

	@Override
	public void setConstructable(ProcTableEntry aPte) {
		constructable_pte = aPte;
	}

	@Override
	public void resolveType(GeneratedNode aNode) {
		_resolvedType = aNode;
		type.resolve(aNode); // TODO maybe this obviates above
	}

	public GeneratedNode resolvedType() {
		return _resolvedType;
	}
}

//
//
//
