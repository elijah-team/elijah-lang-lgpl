/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 2, 2005 2:28:42 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.gen.nodes;

import java.util.List;

import antlr.CommonToken;
import antlr.Token;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.Node;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

import static tripleo.elijah.gen.TypeRef.*;

/**
 * Please consider that there is no such thing as an ExpressionNode
 */
public class ExpressionNodeBuilder {
	
	/**
	 * Return a parser-level OS_ELement for std integer {@param i}
	 *
	 * @param string string in question
	 * @return OS_Ident
	 */
	@NotNull
	@Contract("_ -> new")
	public static OS_Ident ident(String string) {
		// TODO Parser level elements should not be used here
		// TODO consider IdentExpression anyway
		return new OS_Ident(string);
	}
	
	/**
	 * Return a parser-level OS_ELement for std integer {@param i}
	 *
	 * @param i integer in question
	 * @return OS_Integer
	 */
	@NotNull
	@Contract("_ -> new")
	public static OS_Integer integer(int i) {
		// TODO Parser level elements should not be used here
		// TODO consider IdentExpression anyway
		return new OS_Integer(i);
	}

	@NotNull
	@Contract("_, _, _ -> new")
	public static VariableReferenceNode3 varref(String string, Node container, TypeRef typeRef) {
 		return new VariableReferenceNode3(string, container, typeRef);
	}

	@NotNull
	@Contract("_, _, _ -> new")
	public static IExpression binex(TypeRef rt, VariableReference left, ExpressionOperators middle, IExpression right) {
		// TODO Auto-generated method stub
		ExpressionType middle1 = Helpers.ExpressionOperatorToExpressionType(middle);
		return new AbstractBinaryExpression(left, middle1, right);
	}
	
	public static IExpressionNode fncall(String string, List<LocalAgnTmpNode> of) { // todo wrong
		// TODO Auto-generated method stub
		final ProcedureCallExpression pce1 = new ProcedureCallExpression();
		final Qualident xyz = new Qualident();
		final Token t = new CommonToken();
		t.setText(string);
		xyz.append(t);
		pce1.identifier(xyz);
		//
		final ExpressionList expl = Helpers.LocalAgnTmpNodeToListVarRef(of);
		pce1.setArgs(expl);
		//
		return new IExpressionNode() {
			@Override
			public IExpression getExpr() {
				return pce1;
			}
			
			@Override
			public boolean is_const_expr() {
				return false;
			}
			
			@Override
			public boolean is_underscore() {
				return false;
			}
			
			@Override
			public boolean is_var_ref() {
				return false;
			}
			
			@Override
			public boolean is_simple() {
				return false;
			}
			
			@Override
			public String genText(CompilerContext cctx) {
				NotImplementedException.raise();
				return null;
			}
			
			@Override
			public String genType() {
				NotImplementedException.raise();
				return null;
			}
			
			@Override
			public String genText() {
				NotImplementedException.raise();
				return null;
			}
			
			@Override
			public TypeRef getType() {
				return null;
			}
		};
	}
	
	public static IExpressionNode fncall(final MethRef aMeth, List<LocalAgnTmpNode> of) { // TODO no so wrong anymore
		// TODO Auto-generated method stub
		final ProcedureCallExpression pce1 = new ProcedureCallExpression();
		final Qualident xyz = new Qualident();
		final Token t = new CommonToken();
		t.setText(aMeth.getTitle());
		xyz.append(t);
		pce1.identifier(xyz);
		//
		//
		ExpressionList expl = Helpers.LocalAgnTmpNodeToListVarRef(of);
		pce1.setArgs(expl);
		//
		//
		return new IExpressionNode() {
			@Override
			public IExpression getExpr() {
				return pce1;
			}
			
			@Override
			public boolean is_const_expr() {
				return false;
			}
			
			@Override
			public boolean is_underscore() {
				return false;
			}
			
			@Override
			public boolean is_var_ref() {
				return false;
			}
			
			@Override
			public boolean is_simple() {
				return false;
			}
			
			@Override
			public String genText(CompilerContext cctx) {
				TypeRef p = aMeth.getParent();
				int code = p.getCode();
				
				String s = String.format("z%d%s", code, pce1.getLeft().toString());
				StringBuilder sb = new StringBuilder();
				sb.append('(');
				for (IExpression arg : pce1.getArgs()) {
					String s2;
					if (arg instanceof VariableReference)
						s2 = ((VariableReference) arg).getName();
					else
						s2 = (arg.toString());
					sb.append(s2);
					sb.append(',');
				}
				sb.append(')');
//				NotImplementedException.raise();
				return s;
			}
			
			@Override
			public String genType() {
				NotImplementedException.raise();
				return null;
			}
			
			@Override
			public String genText() {
				NotImplementedException.raise();
				return null;
			}
			
			@Override
			public TypeRef getType() {
				return null;
			}
		};
	}
	
	@NotNull
	@Contract("_, _, _ -> new")
	public static IExpression binex(TypeRef rt, VariableReference left, ExpressionOperators middle, TmpSSACtxNode right) { // todo wrong again
		// TODO Auto-generated method stub
		ExpressionType middle1 = Helpers.ExpressionOperatorToExpressionType(middle);
		return new AbstractBinaryExpression(left, middle1, new StringExpression(right.text())); // TODO !!!
	}
	
	@NotNull
	public static IExpressionNode binex(TypeRef rt, VariableReferenceNode3 n, ExpressionOperators opMinus, OS_Integer integer) {
		TypeRef typeRef = new TypeRef(null, null,"int", 80);  // TODO smells
		//
		return new MyIExpressionNode1(n, opMinus, new IntegerNode(integer, typeRef));
	}
	
	@NotNull
	@Contract(value = "_, _, _ -> new", pure = true)
	public static IExpressionNode binex(final TypeRef rt, VariableReferenceNode3 varref, ExpressionOperators operators, TmpSSACtxNode node) {
		return new IExpressionNode() {
			@Override
			public IExpression getExpr() {
				return null;
			}
			
			@Override
			public boolean is_const_expr() {
				return false;
			}
			
			@Override
			public boolean is_underscore() {
				return false;
			}
			
			@Override
			public boolean is_var_ref() {
				return false;
			}
			
			@Override
			public boolean is_simple() {
				return false;
			}
			
			@Override
			public String genText(CompilerContext cctx) {
				return null;
			}
			
			@Override
			public String genType() {
				return null; //rt.getName(); // TODO
			}
			
			@Override
			public String genText() {
				return null;
			}
			
			@Override
			public TypeRef getType() {
				return rt;
			}
		};
	}
	
	private static class MyIExpressionNode1 implements IExpressionNode {
		private final VariableReferenceNode3 _left;
		private final IExpressionNode _right;
		private final ExpressionOperators _middle;
		
		public MyIExpressionNode1(VariableReferenceNode3 left, ExpressionOperators middle, IExpressionNode right) {
			_left = left;
			_middle = middle;
			_right = right;
		}
		
		@Override
		public IExpression getExpr() {
			return null;
		}
		
		@Override
		public boolean is_const_expr() {
			return false;
		}
		
		@Override
		public boolean is_underscore() {
			return false;
		}
		
		@Override
		public boolean is_var_ref() {
			return false;
		}
		
		@Override
		public boolean is_simple() {
			return false;
		}
		
		@Override
		public String genText(CompilerContext cctx) {
			String left = _left.genText();
			String middle1 = _middle.getSymbol();
			String right = printableExpression(_right.getExpr());
			
			return String.format("%s %s %s", left, middle1, right);
		}
		
		static String printableExpression(@NotNull IExpression expression) {
			if (expression instanceof OS_Integer) {
				return Integer.toString(((OS_Integer) expression).getValue());
			}
			return "-------------7";
		}
		
		@Override
		public String genType() {
			// TODO need lookup somewhere, prolly not here tho...
			if (_middle == ExpressionOperators.OP_MINUS) {
				if (_left.getType().getCode() == CODE_U64 &&
						(_right.getType().getCode() == CODE_U64
						|| _right.getType().getCode() == CODE_SYS_INT)) {
					return _left.getType().getName();//"u64";
				}
			}
			return null;
		}
		
		@Override
		public String genText() {
			return null;
		}
		
		@Override
		public TypeRef getType() {
			return null;
		}
	}
	
	private static final int CODE_SYS_INT = 80;
	static final int CODE_U64 = 81;
}

//
//
//
