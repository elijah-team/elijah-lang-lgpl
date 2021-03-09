/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.OS_UnknownType;
import tripleo.elijah.lang.TypeName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 3/5/21 3:51 AM
 */
public class ClassInvocation {
	private final NamespaceStatement ns;
	private final ClassStatement cls;
	Map<TypeName, OS_Type> genericPart;
	private String constructorName;

	public ClassInvocation(NamespaceStatement aNamespaceStatement) {
		ns = aNamespaceStatement;
		genericPart = null;
		cls = null;
	}

	public ClassInvocation(ClassStatement aClassStatement, String aConstructorName) {
		ns = null;
		cls = aClassStatement;
		final List<TypeName> genericPart1 = aClassStatement.getGenericPart();
		if (genericPart1.size() > 0) {
			genericPart = new HashMap<TypeName, OS_Type>(genericPart1.size());
			for (TypeName typeName : genericPart1) {
				genericPart.put(typeName, new OS_UnknownType(null));
			}
		} else {
			genericPart = null;
		}
		constructorName = aConstructorName;
	}

	public void set(int aIndex, TypeName aTypeName, OS_Type aType) {
		genericPart.put(aTypeName, aType);
	}

	public ClassStatement getKlass() {
		return cls;
	}
}

//
//
//
