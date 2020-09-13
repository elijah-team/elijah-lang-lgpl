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

import java.util.List;

/**
 * Created 9/12/20 10:07 PM
 */
public class ProcTableEntry {
    @Override
    public String toString() {
        return "ProcTableEntry{" +
                "index=" + index +
                ", expression=" + expression +
                ", expression_num=" + expression_num+
                ", args=" + args +
                '}';
    }


    public final int index;
    private final int expression_num;
    private final List<InstructionType> args;
    private final IExpression expression;

    public ProcTableEntry(int index, IExpression iExpression, int expression_num, List<InstructionType> args) {
        this.index = index;
        this.expression = iExpression;
        this.expression_num = expression_num;
        this.args = args;
    }
}

//
//
//
