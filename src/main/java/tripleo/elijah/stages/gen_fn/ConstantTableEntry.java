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
 * Created 9/10/20 4:47 PM
 */
public class ConstantTableEntry {
	final int index;
	private final String name;
	private final IExpression initialValue;
	private final OS_Type type;

	@Override
	public String toString() {
		return "ConstantTableEntry{" +
				"index=" + index +
				", name='" + name + '\'' +
				", initialValue=" + initialValue +
				", type=" + type +
				'}';
	}

	public ConstantTableEntry(int index, String name, IExpression initialValue, OS_Type type) {
		this.index = index;
		this.name = name;
		this.initialValue = initialValue;
		this.type = type;
	}

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
}

//
//
//
