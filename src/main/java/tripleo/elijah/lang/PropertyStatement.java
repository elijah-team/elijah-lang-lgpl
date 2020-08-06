/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/6/20 4:00 PM
 */
public class PropertyStatement implements OS_Element, OS_Element2, ClassItem {

	private IdentExpression prop_name;
	private final OS_Element parent;
	private final TypeName tn = new RegularTypeName();
	private Scope _set_scope = new SetScope(this);
	private Scope _get_scope = new GetScope(this);

	public PropertyStatement(OS_Element parent) {
		this.parent = parent;
	}

	@Override // OS_Element
	public void visitGen(ICodeGen visit) {
		throw new NotImplementedException();
	}

	@Override // OS_Element
	public OS_Element getParent() {
		return parent;
	}

	@Override // OS_Element
	public Context getContext() {
		// TODO do we want a PropertyStatementContext? If so, it will be very simple.
		return null;
	}

	public void setName(IdentExpression prop_name) {
		this.prop_name = prop_name;
	}

	public TypeName typeName() {
		return tn;
	}

	public Scope get_scope() {
		return _get_scope;
	}

	public Scope set_scope() {
		return _set_scope;
	}

	@Override
	public String name() {
		return prop_name.getText();
	}

	static class SetScope extends FunctionDef.FunctionDefScope {

		@Override public PropertyStatement getParent() {
			return _Parent;
		}

		private final PropertyStatement _Parent;

		public SetScope(PropertyStatement propertyStatement) {
			super(null);
			_Parent = propertyStatement;
		}
	}
	static class GetScope extends FunctionDef.FunctionDefScope {

		@Override public PropertyStatement getParent() {
			return _Parent;
		}

		private final PropertyStatement _Parent;

		public GetScope(PropertyStatement propertyStatement) {
			super(null);
			_Parent = propertyStatement;
		}
	}
}

//
//
//
