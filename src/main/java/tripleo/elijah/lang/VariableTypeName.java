/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Sep 1, 2005 4:55:12 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class VariableTypeName extends AbstractTypeName implements NormalTypeName {

	private TypeNameList genericPart;
	private Context _ctx;
	//private OS_Type _resolved;
	private OS_Element _resolvedElement;

	@Override
	public Type kindOfType() {
		return Type.NORMAL;
	}

	@Override
	public void addGenericPart(TypeNameList tn2) {
		genericPart = tn2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/* @ requires pr_name != null; */
	// pr_name is null when first created
	@Override
	public String toString() {
		String result;
		if (pr_name != null) {
			if (genericPart != null) {
				result = String.format("%s[%s]", pr_name.toString(), genericPart.toString());
			} else {
				result = pr_name.toString();
			}
		} else {
			result = "<VariableTypeName null>";
		}
		return result;
	}

	@Override
	public void setContext(Context ctx) {
		_ctx = ctx;
	}

	@Override
	public Collection<TypeModifiers> getModifiers() {
		return (tm != null ? List.of(tm)  : new ArrayList<TypeModifiers>());
	}

	@Override
	public Context getContext() {
		return _ctx;
	}

	@Override
	public boolean hasResolvedElement() {
		return _resolvedElement != null;
	}

	@Override
	public OS_Element getResolvedElement() {
		return _resolvedElement;
	}

	@Override
	public void setResolvedElement(OS_Element element) {
		_resolvedElement = element;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VariableTypeName)) return false;
		if (!super.equals(o)) return false;
		VariableTypeName that = (VariableTypeName) o;
		return Objects.equals(genericPart, that.genericPart);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), genericPart);
	}
}

//
//
//
