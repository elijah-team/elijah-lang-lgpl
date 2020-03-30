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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the element
	 */
	public OS_Element getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(OS_Element element) {
		this.element = element;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

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
