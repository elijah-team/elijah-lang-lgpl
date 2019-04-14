/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 8:16:32 PM
 * 
 * $Id$
 *
 */
/* Created on Aug 30, 2005 8:21:52 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.Qualident;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OS_Module implements OS_Element {

	public void add(ModuleItem aStatement) {
		items.add(aStatement);
	}
	
	public void finish(TabbedOutputStream tos) throws IOException {
		tos.close();
	}

/*
	public String packageName() {
		if (packageName != null)
			return packageName;
		StringBuffer pn = new StringBuffer();
//		for (Qualident t : packageNames_q) {
//			pn .append(t.toString());
//			pn .append( '.');
//		}
		packageName = packageNames_q.peek().toString();
		return packageName;
	}
*/
	
	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Module print_osi");
//		if (packageName != null) {
//			tos.put_string("package ");
//			tos.put_string_ln(packageName);
//			tos.put_string_ln("");
//		}
		tos.put_string_ln("//");
		synchronized (items) {
			for (ModuleItem element : items)
				element.print_osi(tos);

		}
	}

	public List<ModuleItem> items=new ArrayList<ModuleItem>();
	public List<IndexingItem> indexingItems=new ArrayList<IndexingItem>();
//	public String packageName;
	public String moduleName="default";

	@Override
	public void visitGen(ICodeGen visit) {
		visit.addModule(this);
	}

	public void addIndexingItem(Token i1, IExpression c1) {
		indexingItems.add(new IndexingItem(i1, c1));
	}

//	public void pushPackageName(String xy) {
//		// TODO Auto-generated method stub
//		packageNames.push(xy);
//	}
//	
//	Stack<String> packageNames = new Stack<String>();
	Stack<Qualident> packageNames_q = new Stack<Qualident>();

	public void pushPackageName(Qualident xyz) {
		packageNames_q.push(xyz);
	}

//	public void visitGen(JavaCodeGen visit) {
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
//	}
}

//
//
//
