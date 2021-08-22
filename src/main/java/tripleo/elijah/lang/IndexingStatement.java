/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.lang.ExpressionList;
import tripleo.elijah.lang.OS_Module;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Apr 15, 2020 at 4:59:21 AM
 */
public class IndexingStatement {

	private OS_Module parent;
	private final List<IndexingItem> items = new ArrayList<IndexingItem>();
	
	public void add(final IndexingItem i) {
		items.add(i);
	}

	public void setParent(OS_Module aParent) {
		parent = aParent;
	}
}

//
//
//
