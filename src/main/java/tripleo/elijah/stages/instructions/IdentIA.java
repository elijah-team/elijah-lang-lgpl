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

/**
 * Created 10/2/20 2:36 PM
 */
public class IdentIA implements InstructionArgument {
	private final int id;
	private final GeneratedFunction gf;
//	private InstructionArgument prev;

/*
	public IdentIA(int x) {
		this.id = x;
		this.gf = null;  // TODO watch out
	}
*/

	public IdentIA(final int ite, final GeneratedFunction generatedFunction) {
		this.gf = generatedFunction;
		this.id = ite;
	}

	public void setPrev(final InstructionArgument ia) {
		gf.getIdentTableEntry(id).backlink = ia;
	}

	@Override
	public String toString() {
		return "IdentIA{" +
				"id=" + id +
//				", prev=" + prev +
				'}';
	}

	public int getIndex() {
		return id;
	}
}

//
//
//
