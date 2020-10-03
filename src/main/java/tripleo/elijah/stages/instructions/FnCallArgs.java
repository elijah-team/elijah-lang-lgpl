/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.qual.Nullable;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;

import java.util.Collection;
import java.util.List;

/**
 * Created 9/10/20 3:36 PM
 */
public class FnCallArgs implements InstructionArgument {
    private final Instruction expression_to_call;
    private final GeneratedFunction gf;

    @Override
    public String toString() {
        final int index = ((IntegerIA) expression_to_call.args.get(0)).getIndex();
        final List<InstructionArgument> instructionArguments = expression_to_call.args.subList(1, expression_to_call.args.size());
/*
        final List<String> collect = instructionArguments
                .stream()
                .map((instructionArgument -> instructionArgument.toString()))
                .collect(Collectors.toList());
*/
        final Collection<String> collect2 = Collections2.transform(instructionArguments, new Function<InstructionArgument, String>() {
            @Nullable
            @Override
            public String apply(@Nullable InstructionArgument input) {
                return input.toString();
            }
        });
        return String.format("(call %d [%s(%s)] %s)",
                index, gf.prte_list.get(index).expression, gf.prte_list.get(index).args,
                String.join(" ", collect2));

    }

    public FnCallArgs(Instruction expression_to_call, GeneratedFunction generatedFunction) {
        this.expression_to_call = expression_to_call;
        this.gf = generatedFunction;
    }

    public InstructionArgument getArg(int i) {
        return expression_to_call.getArg(i);
    }
}

//
//
//
