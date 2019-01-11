package tripleo.elijah.lang;
//
//package pak;
//
//import java.io.IOException;
//import java.io.PrintStream;
//import java.util.Iterator;
//import java.util.Vector;
//import org.oluworld.util.TabbedOutputStream;
//
//// Referenced classes of package pak:
////			_Scope, ScopeElement, ExprListListener
//
//public class Klass implements _Scope {
//
//	public Klass(String s) {
//		className = s;
//		elts = new Vector();
//	}
//
//	public String getName() {
//		return className;
//	}
//
//	public String repr_() {
//		return (new StringBuilder("Klass (")).append(getName()).append(")")
//				.toString();
//	}
//
//	public ExprListListener getListener() {
//		return new _Scope.ScopeExprListener() {
//
//			String getScopeListenerName() {
//				return "ClassScopeListener";
//			}
//
//		};
//	}
//
//	public void print_osi(TabbedOutputStream tos) throws IOException {
//		System.out.println("Klass print_osi");
//		tos.incr_tabs();
//		tos.put_string("Class (");
//		tos.put_string(className);
//		tos.put_string_ln(") {");
//		tos.put_string_ln("//");
//		synchronized (elts) {
//			for (Iterator e = elts.iterator(); e.hasNext(); ((ScopeElement) (ScopeElement) e
//					.next()).print_osi(tos))
//				;
//		}
//		tos.dec_tabs();
//		tos.put_string_ln((new StringBuilder("} // class ")).append(className)
//				.toString());
//	}
//
//	public void push(ScopeElement se) {
//		elts.add(se);
//	}
//
//	String className;
//
//	Vector elts;
//}
