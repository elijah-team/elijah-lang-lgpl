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
 * Created 9/10/20 3:17 PM
 */
public class Label {
	String name;
	long index;

	public Label(String name) {
		this.name = name;
	}

	public void setIndex(long index) {
		this.index = index;
	}
}

//
//
//
