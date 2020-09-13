/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.IdentExpression;

import java.util.List;

/**
 * Created 9/13/20 1:22 AM
 */
public class FuncTableEntry {
    final int index;
    private final IdentExpression name;
    private final List<InstructionType> args_types;

    @Override
    public String toString() {
        return "FuncTableEntry{" +
                "index=" + index +
                ", name=" + name +
                ", args_types=" + args_types +
                '}';
    }

    public FuncTableEntry(int index, IdentExpression name, List<InstructionType> args_types) {
        this.index = index;
        this.name = name;
        this.args_types = args_types;
    }
}

//
//
//
