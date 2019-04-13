/**
 * 
 */
package tripleo.elijah.gen.nodes;

import tripleo.elijah.lang.OS_Ident;

/**
 * @author olu
 *
 */
public class ArgumentNode {

	private OS_Ident ident;
	public String genType;
	public String genName;

	public ArgumentNode(OS_Ident ident) {
		// TODO Auto-generated constructor stub
		this.ident = ident;
	}

	/**
	 * @param genName
	 * @param genType
	 */
	public ArgumentNode(String genName, String genType) {
		super();
		this.genName = genName;
		this.genType = genType;
	}

}
