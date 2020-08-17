/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/16/20 7:42 AM
 */
public class GenericTypeName implements TypeName {
	private final Context _ctx;
	private Qualident _typeName;
	private TypeModifiers modifiers;

	public GenericTypeName(Context cur) {
		_ctx=cur;
	}

	public void typeName(Qualident xy) {
		_typeName = xy;
	}

	public void set(TypeModifiers modifiers_) {
		modifiers = modifiers_;
	}

	@Override
	public boolean isNull() {
		return _typeName == null;
	}

	@Override
	public void setContext(Context context) {
		throw new NotImplementedException();
	}

	@Override
	public Context getContext() {
		return _ctx;
	}

	@Override
	public void type(TypeModifiers typeModifiers) {
		modifiers = typeModifiers;
	}

	@Override
	public Type kindOfType() {
		return Type.GENERIC;
	}
}
