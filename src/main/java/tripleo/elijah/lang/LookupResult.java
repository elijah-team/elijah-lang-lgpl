/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:16:07 PM
 */
public class LookupResult {

	private String name;
	private OS_Element element;
	private int level;

	public LookupResult(String name, OS_Element element, int level) {
		this.name = name;
		this.element = element;
		this.level = level;
	}

	public String toString() {
		return String.format("<%s %s %d>", element, name, level);
	}
}
