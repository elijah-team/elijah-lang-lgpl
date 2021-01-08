/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 9:05:24 PM
 * 
 * $Id$
 *
 */
package tripleo.elijah.lang;

import java.io.File;

public class RegularTypeName extends AbstractTypeName2 implements NormalTypeName {

	private TypeNameList genericPart;
	private Context _ctx;
//	private OS_Type _resolved;
	private OS_Element _resolvedElement;

	public RegularTypeName(final Context cur) {
		super();
		_ctx = cur;
	}

	@Deprecated public RegularTypeName() { // TODO remove this
		super();
		_ctx = null;
	}

	@Override
	public void addGenericPart(final TypeNameList tn2) {
		genericPart = tn2;
	}

@Override
public String getName() {
	if (typeName == null) return null;
	return this.typeName.asSimpleString();
}

	@Override
	public void setContext(final Context ctx) {
		_ctx = ctx;
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
	public void setResolvedElement(final OS_Element element) {
		_resolvedElement = element;
	}


	@Override
	public void setName(final Qualident aS) {
		this.typeName=aS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final TypeModifiers modifier : _ltm) {
			switch (modifier) {
				case CONST:      sb.append("const "); break;
				case REFPAR:     sb.append("ref "); break;
				case FUNCTION:   sb.append("fn "); break; // TODO
				case PROCEDURE:  sb.append("proc "); break; // TODO
				case GC:		 sb.append("gc "); break;
				case ONCE:		 sb.append("once "); break;
				case INPAR:		 sb.append("in "); break;
				case LOCAL:		 sb.append("local "); break;
				case MANUAL:	 sb.append("manual "); break;
				case OUTPAR:	 sb.append("out "); break;
				case POOLED:	 sb.append("pooled "); break;
				case TAGGED:	 sb.append("tagged "); break;
				case GENERIC:	 sb.append("generic "); break; // TODO
				case NORMAL:	 break;
				default: 		 throw new IllegalStateException("Cant be here!");
			}
		}
		if (typeName != null) {
			if (genericPart != null) {
				sb.append(String.format("%s[%s]", getName(), genericPart.toString()));
			} else
				sb.append(getName());
		} else
			sb.append("<RegularTypeName empty>");
		return sb.toString();
	}

	@Override
	public Type kindOfType() {
		return Type.NORMAL;
	}

	@Override
	public TypeNameList getGenericPart() {
		return genericPart;
	}

	@Override
	public Qualident getRealName() {
		return typeName;
	}

	// region Locatable

	@Override
	public int getLine() {
		return getRealName().parts().get(0).getLine();
	}

	@Override
	public int getColumn() {
		return getRealName().parts().get(0).getColumn();
	}

	// TODO what about generic part
	@Override
	public int getColumnEnd() {
		return getRealName().parts().get(getRealName().parts().size()).getColumnEnd();
	}

	// TODO what about generic part
	@Override
	public int getLineEnd() {
		return getRealName().parts().get(getRealName().parts().size()).getLineEnd();
	}

	@Override
	public File getFile() {
		return getRealName().parts().get(0).getFile();
	}

	// endregion

}

//
//
//
