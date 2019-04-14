/**
 * 
 */
package tripleo.elijah.gen.nodes;

/**
 * @author olu
 *
 */
public class MethNameNode {

	private String ident;
	public String genName;

	public MethNameNode(String method_name) {
		// TODO Auto-generated constructor stub
		this.ident = method_name;
		genName = "z__"+method_name/*.getText()*/; // TODO wrong
	}

}
