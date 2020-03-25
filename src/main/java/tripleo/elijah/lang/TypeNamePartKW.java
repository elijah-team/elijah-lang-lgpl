/**
 * 
 */
package tripleo.elijah.lang;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tripleo(sb)
 *
 * Created 	Dec 9, 2019 at 3:16:12 PM
 */
public class TypeNamePartKW implements TypeNamePart {
	Set<TypeNamePartKW> kws = new HashSet<TypeNamePartKW>();
	Qualident name = null;
	TypeNameList genericPart = null;
}

//
//
//
