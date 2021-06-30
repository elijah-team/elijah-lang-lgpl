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
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 2/28/21 3:23 AM
 */
public interface GeneratedContainer extends GeneratedNode {
    OS_Element getElement();

    VarTableEntry getVariable(String aVarName);

    public class VarTableEntry {
        public final IdentExpression nameToken;
        public final IExpression initialValue;
        private final OS_Element parent;
        TypeName typeName;
        public OS_Type varType;
        List<TypeTableEntry> potentialTypes = new ArrayList<TypeTableEntry>();
        private GeneratedNode _resolved;

        public VarTableEntry(@NotNull IdentExpression aNameToken,
                             IExpression aInitialValue,
                             @NotNull TypeName aTypeName,
                             @NotNull OS_Element aElement) {
            nameToken       = aNameToken;
            initialValue    = aInitialValue;
            typeName        = aTypeName;
            varType         = new OS_Type(typeName);
            parent          = aElement;
        }

        public void addPotentialTypes(@NotNull Collection<TypeTableEntry> aPotentialTypes) {
            potentialTypes.addAll(aPotentialTypes);
        }

        public void resolve(@NotNull GeneratedNode aResolved) {
            _resolved = aResolved;
        }

        public @Nullable GeneratedNode resolved() {
            return _resolved;
        }

        public @NotNull OS_Element getParent() {
            return parent;
        }
    }
}

//
//
//
