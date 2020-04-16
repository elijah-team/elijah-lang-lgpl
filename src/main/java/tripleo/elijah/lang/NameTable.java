/**
 * 
 */
package tripleo.elijah.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tripleo
 *
 * Created 	Mar 29, 2020 at 7:00:10 PM
 */
public class NameTable {

	class TypedElement {
		public TypedElement(OS_Element element2, OS_Type dtype) {
			this.element = element2;
			this.type    = dtype;
		}
		OS_Element element;
		OS_Type type;

		@Override
		public String toString() {
			return "TypedElement{" +
					"element=" + element +
					", type=" + type +
					'}';
		}
	}
	
	Map<String, TypedElement> members = new HashMap<String, TypedElement>();
	
	public void add(OS_Element element, String name, OS_Type dtype) {
//		element.setType(dtype);
		members.put(name, new TypedElement(element, dtype));
		System.err.println("[NameTable#add] "+members);
	}
	
}

//
//
//
