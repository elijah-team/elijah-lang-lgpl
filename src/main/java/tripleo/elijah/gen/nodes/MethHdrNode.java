/**
 * 
 */
package tripleo.elijah.gen.nodes;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import tripleo.elijah.lang.OS_Ident;
import tripleo.elijah.lang.TypeModifiers;
import tripleo.elijah.util.NotImplementedException;

/**
 * @author SBUSER
 *
 */
public class MethHdrNode {

	public int argCount;
	final public MethNameNode methName;
	public TypeNameNode returnType;
	private List<ArgumentNode> argument_types;

	public MethHdrNode(@NonNull OS_Ident return_type, String method_name, List<ArgumentNode> argument_types) {
		// TODO Auto-generated constructor stub
		methName = new MethNameNode(method_name);
		argCount = argument_types.size();
		this.argument_types = argument_types;//.stream().map(ArgumentNode::make).collect(Collections);
		returnType=new TypeNameNode(return_type);
	}

	public ArgumentNode argument(int c) {
		// TODO Auto-generated method stub
//		if (c>=argument_types.size()) return null;
		return (argument_types.get(c));
	}
	
	public void addAnnotation(AnnotationNode node) {
		NotImplementedException.raise();
	}
	
	public void addModifier(TypeModifiers mod) {
		NotImplementedException.raise();
	}
	
	public Iterable<ArgumentNode> ArgumentsIterator() {
		// TODO Auto-generated method stub
		return new Iterable<ArgumentNode>() {
			
			@Override
			public Iterator<ArgumentNode> iterator() {
			
				return new Iterator<ArgumentNode>() {		
				
					private int c = 0;
		
					@Override
					public boolean hasNext() {
						MethHdrNode node=MethHdrNode.this;
						return c<node.argCount;
					}
		
					@Override
					public ArgumentNode next() {
						MethHdrNode node=MethHdrNode.this;
						return node.argument(c++);
					}
				};
			};
		};
	}

}
