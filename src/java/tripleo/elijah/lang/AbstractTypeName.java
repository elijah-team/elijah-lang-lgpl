/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			TypeName

public abstract class AbstractTypeName implements TypeName {

	public AbstractTypeName() {
	}

	@Override
	public boolean isNull() {
		return !pr_constant && !pr_reference && !pr_out && !pr_in
				&& pr_name == "";
	}

	@Override
	public boolean getConstant() {
		return pr_constant;
	}

	@Override
	public void setConstant(boolean s) {
		pr_constant = s;
	}

	@Override
	public boolean getReference() {
		return pr_reference;
	}

	@Override
	public void setReference(boolean s) {
		pr_reference = s;
	}

	@Override
	public boolean getOut() {
		return pr_out;
	}

	@Override
	public void setOut(boolean s) {
		pr_out = s;
	}

	@Override
	public boolean getIn() {
		return pr_in;
	}

	@Override
	public void setIn(boolean s) {
		pr_in = s;
	}

	@Override
	public String getName() {
		return pr_name;
	}

	@Override
	public void setName(String s) {
		pr_name = s;
	}

	private TypeModifiers tm;

	@Override
	public void type(TypeModifiers atm) {
tm=atm;		
	}
	
	public void set(int aType) {
		type = aType;
	}

	protected boolean pr_constant;

	protected boolean pr_reference;

	protected boolean pr_out;

	protected boolean pr_in;

	protected String pr_name;

	int type;
}
