/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

import java.util.List;

/**
 * Created 9/10/20 3:36 PM
 */
public class FnCallIA implements InstructionArgument {
    private final Instruction expression_to_call;
    private final List<InstructionArgument> args;

    public FnCallIA(Instruction expression_to_call, List<InstructionArgument> args) {
        this.expression_to_call = expression_to_call;
        this.args = args;
    }

    @Override
    public String toString() {
        return "FnCallIA{" +
                "expression_to_call=" + expression_to_call +
                ", args=" + args +
                '}';
    }

    public FnCallIA(Instruction expression_to_call) {
        this.expression_to_call = expression_to_call;
        this.args = null;
    }
}

//
//
//
