/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.util.Objects;

public abstract class AbstractTypeName implements TypeName {

	private boolean isNullable = false;

	@Override
	public boolean isNull() {
		return !pr_constant && !pr_reference && !pr_out && !pr_in
				&& (pr_name == null);
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
		return pr_name.toString();
	}

	@Override
	public void setName(Qualident s) {
		pr_name = s;
	}
	
	@Override
	public void type(TypeModifiers atm) {
tm=atm;		
	}

	@Override
	public void setNullable() {
		this.isNullable = true;
	}

	public void set(int aType) {
		type = aType;		// TODO where is this used at?
	}
	
	private TypeModifiers tm;
	
	protected boolean pr_constant;
	protected boolean pr_reference;
	protected boolean pr_out;
	protected boolean pr_in;
	protected Qualident pr_name;

	int type;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbstractTypeName)) return false;
		AbstractTypeName that = (AbstractTypeName) o;
		return pr_constant == that.pr_constant &&
				pr_reference == that.pr_reference &&
				pr_out == that.pr_out &&
				pr_in == that.pr_in &&
				type == that.type &&
				tm == that.tm &&
				pr_name.equals(that.pr_name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tm, pr_constant, pr_reference, pr_out, pr_in, pr_name, type);
	}
}

//
//
//
