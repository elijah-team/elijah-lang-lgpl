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

	private final Context context;
	private String name;
	private OS_Element element;
	private int level;

	public LookupResult(final String name, final OS_Element element, final int level, final Context aContext) {
		this.name = name;
		this.element = element;
		this.level = level;
		this.context = aContext;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
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
	public void setElement(final OS_Element element) {
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
	public void setLevel(final int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return String.format("<%s %s %d>", element, name, level);
	}
}
