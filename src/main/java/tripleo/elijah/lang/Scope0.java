///*
// * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
// *
// * The contents of this library are released under the LGPL licence v3,
// * the GNU Lesser General Public License text was downloaded from
// * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
// *
// */
//package tripleo.elijah.lang;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Scope0 extends AbstractScope2 {
//	private final List<FunctionItem> items = new ArrayList<FunctionItem>();
//
//	private final AbstractStatementClosure asc = new AbstractStatementClosure(this);
//
//	public Scope0(final FuncExpr funcExpr) {
//		super(funcExpr);
//	}
//
//	@Override
//	public void add(final StatementItem aItem) {
//		if (aItem instanceof FunctionItem)
//			items.add((FunctionItem) aItem);
//		else
//			System.err.println(String.format("106 adding false FunctionItem %s",
//				aItem.getClass().getName()));
//	}
//
//	@Override
//	public StatementClosure statementClosure() {
//		return asc;
//	}
//
//	final FormalArgList formalArgList = new FormalArgList(); // TODO this is never accessed anywhere
//
//	public FormalArgList fal() {
//		return formalArgList;
//	}
//}
//
////
////
////
