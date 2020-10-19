/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

/**
 * Created 9/10/20 3:35 PM
 */
public class IntegerIA implements InstructionArgument {
	@Override
	public String toString() {
		return "IntegerIA{" +
				"index=" + index +
				'}';
	}

	private final int index;

	public IntegerIA(final int e1) {
		index = e1;
	}

	public int getIndex() {
		return index;
	}
}

//
//
//
