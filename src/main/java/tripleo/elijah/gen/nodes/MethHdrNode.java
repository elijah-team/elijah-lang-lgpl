/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah.gen.nodes;

import org.eclipse.jdt.annotation.NonNull;
import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.Node;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.TypeModifiers;
import tripleo.elijah.util.NotImplementedException;

import java.util.Iterator;
import java.util.List;

/**
 * @author Tripleo(sb)
 *
 */
public class MethHdrNode implements Node {
	
	private final TypeRef returnType2;
	private final Node _parent;
	public int argCount;
	final public MethNameNode methName;
	public TypeNameNode returnType;
	private final List<ArgumentNode> argument_types;
	private final int _code;
	
	public MethHdrNode(Node parent, @NonNull IdentExpression return_type, String method_name, List<ArgumentNode> argument_types, int code) {
		_parent = parent;
		_code = code;
		methName = new MethNameNode(method_name, this);
		argCount = argument_types.size();
		this.argument_types = argument_types;//.stream().map(ArgumentNode::make).collect(Collections);
		returnType=new TypeNameNode(return_type);
		//
		returnType2 = null;
	}
	
	public MethHdrNode(TypeRef retType, Node parent, String methodName, List<ArgumentNode> argumentTypes, int code) {
		_parent = parent;
		_code = code;
		methName=new MethNameNode(methodName, this);
		argCount=argumentTypes.size();
		argument_types=argumentTypes;
		returnType2=retType;
		//
		returnType=null;
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
			}
		};
	}
	
	public TypeRef returnType() {
		return returnType2;
	}
	
	@Override
	public int getCode() {
		return _code;
	}
	
	public Node getParent() {
		return _parent;
	}
	
	public String genName() {
		return methName.genName;
	}

	public void GenMethHdr(CompilerContext cctx, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	public void BeginMeth(CompilerContext cctx, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

	public void EndMeth(CompilerContext cctx, GenBuffer gbn) {
		// TODO Auto-generated method stub
		
	}

}
