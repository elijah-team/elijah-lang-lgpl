/**
 * 
 */
package tripleo.elijah.lang;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:33:31 AM
 */
public class ModuleContext extends Context {

	private OS_Module carrier;

	public ModuleContext(OS_Module module) {
		this.carrier = module;
	}

}
