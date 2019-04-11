/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Mar 13, 2019 at 11:04:59 AM
 *
 */
package tripleo.elijah.gen.nodes;

/**
 * @author SBUSER
 *
 */
public class CloseCaseNode {

//	public CloseCaseNode(CaseHdrNode csn, ChoiceOptions break1) {
//		// TODO Auto-generated constructor stub
//	}

	public CaseChoiceNode hdr_node;

	public CloseCaseNode(CaseChoiceNode csn, ChoiceOptions break1) {
		// TODO Auto-generated constructor stub
		hdr_node=csn;
	}

	public CloseCaseNode(CaseDefaultNode csn2, ChoiceOptions break1) {
		// TODO Auto-generated constructor stub
//		hdr_node=csn2;
	}

}
