/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.deduce.IInvocation;

/**
 * Created 5/31/21 1:32 PM
 */
public class GenType {
	public NamespaceStatement resolvedn;
	public OS_Type typeName; // TODO or just TypeName ??
	public TypeName nonGenericTypeName;
	public OS_Type resolved;
	public IInvocation ci;
	public GeneratedNode node;
	public FunctionInvocation functionInvocation;

	public GenType(NamespaceStatement aNamespaceStatement) {
		resolvedn = /*new OS_Type*/(aNamespaceStatement);
	}

	public GenType(ClassStatement aClassStatement) {
		resolved = aClassStatement.getOS_Type();
	}

	@Override
	public boolean equals(final Object aO) {
		if (this == aO) return true;
		if (aO == null || getClass() != aO.getClass()) return false;

		final GenType genType = (GenType) aO;

		if (resolvedn != null ? !resolvedn.equals(genType.resolvedn) : genType.resolvedn != null) return false;
		if (typeName != null ? !typeName.equals(genType.typeName) : genType.typeName != null) return false;
		if (nonGenericTypeName != null ? !nonGenericTypeName.equals(genType.nonGenericTypeName) : genType.nonGenericTypeName != null)
			return false;
		if (resolved != null ? !resolved.equals(genType.resolved) : genType.resolved != null) return false;
		if (ci != null ? !ci.equals(genType.ci) : genType.ci != null) return false;
		if (node != null ? !node.equals(genType.node) : genType.node != null) return false;
		return functionInvocation != null ? functionInvocation.equals(genType.functionInvocation) : genType.functionInvocation == null;
	}

	@Override
	public int hashCode() {
		int result = resolvedn != null ? resolvedn.hashCode() : 0;
		result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
		result = 31 * result + (nonGenericTypeName != null ? nonGenericTypeName.hashCode() : 0);
		result = 31 * result + (resolved != null ? resolved.hashCode() : 0);
		result = 31 * result + (ci != null ? ci.hashCode() : 0);
		result = 31 * result + (node != null ? node.hashCode() : 0);
		result = 31 * result + (functionInvocation != null ? functionInvocation.hashCode() : 0);
		return result;
	}

	public GenType() {
	}

	public String asString() {
		final StringBuffer sb = new StringBuffer("GenType{");
		sb.append("resolvedn=").append(resolvedn);
		sb.append(", typeName=").append(typeName);
		sb.append(", nonGenericTypeName=").append(nonGenericTypeName);
		sb.append(", resolved=").append(resolved);
		sb.append(", ci=").append(ci);
		sb.append(", node=").append(node);
		sb.append(", functionInvocation=").append(functionInvocation);
		sb.append('}');
		return sb.toString();
	}

	public void set(OS_Type aType) {
		switch (aType.getType()) {
		case USER:
			typeName = aType;
			break;
		case USER_CLASS:
			resolved = aType;
		default:
			System.err.println("48 Unknown in set: "+aType);
		}
	}

	public void copy(GenType aGenType) {
		if (resolvedn == null) resolvedn = aGenType.resolvedn;
		if (typeName == null) typeName = aGenType.typeName;
		if (nonGenericTypeName == null) nonGenericTypeName = aGenType.nonGenericTypeName;
		if (resolved == null) resolved = aGenType.resolved;
		if (ci == null) ci = aGenType.ci;
		if (node == null) node = aGenType.node;
	}

	public boolean isNull() {
		if (resolvedn != null) return false;
		if (typeName != null) return false;
		if (nonGenericTypeName != null) return false;
		if (resolved != null) return false;
		if (ci != null) return false;
		if (node != null) return false;
		return true;
	}
}

//
//
//
