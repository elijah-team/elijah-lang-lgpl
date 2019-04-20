/**
 * 
 */
package tripleo.elijah.gen.nodes;

import org.eclipse.jdt.annotation.NonNull;

import tripleo.elijah.lang.OS_Ident;

/**
 * @author olu
 *
 */
public class TypeNameNode {

	public TypeNameNode(@NonNull OS_Ident return_type) {
		// TODO Auto-generated constructor stub
		genType=return_type.getText(); // TODO wrong prolly
	}

	public String genType;
	
	public String getText() {
		return null;
	}
}
