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

import tripleo.elijah.util.NotImplementedException;

public class RegularTypeName extends AbstractTypeName2 implements NormalTypeName {

	private /*Normal*/TypeName genericPart = null;
	private Context _ctx;
	private OS_Type _resolved;
	private OS_Element _resolvedElement;

	public RegularTypeName(Context cur) {
		super();
		_ctx = cur;
	}

	public RegularTypeName() {
		super();
		_ctx = null;
	}

	@Override
	public void addGenericPart(TypeName tn2) {
		genericPart = tn2;
	}

public String getName() {
	if (typeName == null) return null;
	return this.typeName.asSimpleString();
}

//	@Override
public String getTypeName() {
	return this.typeName.toString();
}  // TODO is this right?

@Override
public void set(TypeModifiers aModifiers) {
	_ltm.add(aModifiers);
}

@Override
public void setGeneric(boolean value) {
	_ltm.add(TypeModifiers.GENERIC);
}

	@Override
	public void setContext(Context ctx) {
//		_a.setContext(ctx);
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
	public void setResolvedElement(OS_Element element) {
		_resolvedElement = element;
	}


	public void setName(Qualident aS) {
		this.typeName=aS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (TypeModifiers modifier : _ltm) {
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
public void type(TypeModifiers atm) {
tm=atm;		
}

	@Override
	public Type kindOfType() {
		return Type.NORMAL;
	}

	@Override
public void typeName(Qualident xy) {
	// TODO Auto-generated method stub
//	NotImplementedException.raise();
	this.typeName = xy;
}
	

@Override
public void typeof(Qualident xyz) {
	throw new NotImplementedException();
// //	 TODO Auto-generated method stub
	
}
}

//
//
//
