/**
 * 
 */
package tripleo.elijah.lang;

import org.jetbrains.annotations.Contract;

/**
 * @author Tripleo
 *
 * Created 	Mar 23, 2020 at 12:40:27 AM
 */
public interface OS_Element2 {

	/**
	 * The name of the element
	 * TODO Should this be a {@link IdentExpression}?
	 *
	 * @return a String
	 */
	@Contract(pure = true)
	String name();

}

//
//
//
