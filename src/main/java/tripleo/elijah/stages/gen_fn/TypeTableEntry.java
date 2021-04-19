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

/**
 * Created 9/12/20 10:26 PM
 */
public class TypeTableEntry {
    final int index;
    public final Type lifetime;
    public final TableEntryIV tableEntry;
    public @Nullable OS_Type attached;
    public final IExpression expression;
    private GeneratedContainerNC _resolved;

    public TypeTableEntry(final int index, final Type lifetime, @Nullable final OS_Type attached, final IExpression expression, TableEntryIV aTableEntryIV) {
        this.index = index;
        this.lifetime = lifetime;
        if (attached == null || (attached.getType() == OS_Type.Type.USER && attached.getTypeName() == null))
            this.attached = null;
        else
            this.attached = attached;
        this.expression = expression;
        this.tableEntry = aTableEntryIV;
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

    public void resolve(GeneratedContainerNC aResolved) {
        _resolved = aResolved;
    }

    public GeneratedContainerNC resolved() {
        return _resolved;
    }

	public enum Type {
        SPECIFIED, TRANSIENT
    }

}

//
//
//
