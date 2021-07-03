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
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.ClassInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/12/20 10:26 PM
 */
public class TypeTableEntry {
	final int index;
	public final Type lifetime;
	public final TableEntryIV tableEntry;
	@Nullable
	private OS_Type attached;
	private final GenType genType = new GenType();
	public final IExpression expression;
	private GeneratedNode _resolved;
	private final List<OnSetAttached> osacbs = new ArrayList<OnSetAttached>();

	public interface OnSetAttached {
		void onSetAttached(TypeTableEntry aTypeTableEntry);
	}

	public TypeTableEntry(final int index,
						  final Type lifetime,
						  @Nullable final OS_Type aAttached,
						  final IExpression expression,
						  TableEntryIV aTableEntryIV) {
		this.index = index;
		this.lifetime = lifetime;
		if (aAttached == null || (aAttached.getType() == OS_Type.Type.USER && aAttached.getTypeName() == null)) {
			attached = null;
			// do nothing with genType
		} else {
			attached = aAttached;
			_settingAttached(aAttached);
		}
		this.expression = expression;
		this.tableEntry = aTableEntryIV;
	}

	private void _settingAttached(@NotNull OS_Type aAttached) {
		switch (aAttached.getType()) {
		case USER:
			if (genType.typeName != null)
				genType.nonGenericTypeName = aAttached.getTypeName();
			else
				genType.typeName = aAttached/*.getTypeName()*/;
			break;
		case USER_CLASS:
//			ClassStatement c = attached.getClassOf();
			genType.resolved = attached; // c
			break;
		case UNIT_TYPE:
			genType.resolved = aAttached;
		default:
//			throw new NotImplementedException();
			System.err.println("73 "+aAttached);
			break;
		}
	}

	@Override @NotNull
	public String toString() {
		return "TypeTableEntry{" +
				"index=" + index +
				", lifetime=" + lifetime +
				", attached=" + attached +
				", expression=" + expression +
				'}';
	}

	public int getIndex() {
		return index;
	}

	public void resolve(GeneratedNode aResolved) {
		_resolved = aResolved;
	}

	public GeneratedNode resolved() {
		return _resolved;
	}

	public boolean isResolved() {
		return _resolved != null;
	}

	public OS_Type getAttached() {
		return attached;
	}

	public void setAttached(OS_Type aAttached) {
		attached = aAttached;
		if (aAttached != null)
			_settingAttached(aAttached);

		for (OnSetAttached cb : osacbs) {
			cb.onSetAttached(this);
		}
	}

	public void addSetAttached(OnSetAttached osa) {
		osacbs.add(osa);
	}

	public void genTypeCI(ClassInvocation aClsinv) {
		genType.ci = aClsinv;
	}

	public enum Type {
		SPECIFIED, TRANSIENT
	}

}

//
//
//
