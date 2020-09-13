/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

import tripleo.elijah.stages.gen_fn.GeneratedFunction;

import java.util.stream.Collectors;

/**
 * Created 9/10/20 3:36 PM
 */
public class FnCallArgs implements InstructionArgument {
    private final Instruction expression_to_call;
    private final GeneratedFunction gf;

    @Override
    public String toString() {
        final int index = ((IntegerIA) expression_to_call.args.get(0)).getIndex();
        return String.format("(call %d [%s(%s)] %s)",
                index, gf.prte_list.get(index).expression, gf.prte_list.get(index).args,
                String.join(" ", expression_to_call.args.subList(1, expression_to_call.args.size())
                        .stream()
                        .map((instructionArgument -> instructionArgument.toString()))
                        .collect(Collectors.toList())));

    }

    public FnCallArgs(Instruction expression_to_call, GeneratedFunction generatedFunction) {
        this.expression_to_call = expression_to_call;
        this.gf = generatedFunction;
    }
}

//
//
//
