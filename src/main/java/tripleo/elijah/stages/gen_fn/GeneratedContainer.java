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

    GeneratedClass.VarTableEntry getVariable(String aVarName);

    public class VarTableEntry {
        public final IdentExpression nameToken;
        public final IExpression initialValue;
        TypeName typeName;
        public OS_Type varType;
        List<TypeTableEntry> potentialTypes = new ArrayList<TypeTableEntry>();
        private GeneratedNode _resolved;

        public VarTableEntry(IdentExpression aNameToken, IExpression aInitialValue, @NotNull TypeName aTypeName) {
            this.nameToken = aNameToken;
            this.initialValue = aInitialValue;
            this.typeName = aTypeName;
        }

        public void addPotentialTypes(Collection<TypeTableEntry> aPotentialTypes) {
            potentialTypes.addAll(aPotentialTypes);
        }

        public void resolve(GeneratedNode aResolved) {
            _resolved = aResolved;
        }

        public GeneratedNode resolved() {
            return _resolved;
        }
    }
}

//
//
//
