/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.elijah.stages.gen_fn;

/**
 * Created 4/13/21 11:43 AM
 */
public interface Constructable {
	void setConstructable(ProcTableEntry aPte);

	void resolveTypeToClass(GeneratedNode aNode);

	void setGenType(GenType aGenType);
}

//
//
//
