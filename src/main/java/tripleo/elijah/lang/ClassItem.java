/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

/**
 * Marker interface to represent elements that can be added to a class
 * or a namespace or an enum
 *
 * @see {@link ClassStatement#add(OS_Element) }
 * @see {@link NamespaceStatement#add(OS_Element) }
 * @see {@link EnumStatement#add(OS_Element) }
 */
public interface ClassItem extends OS_Element {

	void setCategory(El_Category aCategory);
	void setAccess(AccessNotation aNotation);

	El_Category getCategory();
	AccessNotation getAccess();

}

//
//
//
