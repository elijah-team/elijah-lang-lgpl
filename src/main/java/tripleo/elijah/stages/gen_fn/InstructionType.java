/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;

/**
 * Created 9/13/20 12:26 AM
 */
public class InstructionType {
    private final IExpression arg;
    private final OS_Type type;

    public InstructionType(IExpression arg) {
        this.arg = arg;
        this.type = null;
    }

    public InstructionType(OS_Type type) {
        this.arg = null;
        this.type = type;
    }

    public boolean isUnknown() {
        return arg != null && type == null;
    }

    public static InstructionType unknown(IExpression arg) {
        return new InstructionType(arg);
    }

    public static InstructionType known(OS_Type type) {
        assert type != null;
        return new InstructionType(type);
    }

    public IExpression getArg() {
        return arg;
    }

    public OS_Type getType() {
        return type;
    }

    @Override
    public String toString() {
        if (isUnknown())
            return "Unknown";

        return "InstructionType{" +
                "type=" + type +
                '}';
    }
}

//
//
//
