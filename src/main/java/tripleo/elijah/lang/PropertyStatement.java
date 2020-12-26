/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.contexts.PropertyStatementContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

/**
 * Created 8/6/20 4:00 PM
 */
public class PropertyStatement implements OS_Element, OS_Element2, ClassItem {

	private final Context context;
	private final OS_Element parent;

	public FunctionDef set_fn;
	public FunctionDef get_fn;
	private IdentExpression prop_name;
	private TypeName typeName;

	public PropertyStatement(OS_Element parent, Context cur) {
		this.parent = parent;
		this.context = new PropertyStatementContext(cur, this);
	}

	@NotNull
	private FunctionDef createSetFunction() {
		FunctionDef functionDef = new FunctionDef(this, getContext());
		functionDef.setName(Helpers.string_to_ident(String.format("<prop_set %s>", prop_name)));
		functionDef.setType(FunctionDef.Type.PROP_SET);
		FormalArgList fal = new FormalArgList();
		FormalArgListItem fali = fal.next();
		fali.setName(Helpers.string_to_ident("Value"));
		fali.setTypeName(this.typeName);
		RegularTypeName unitType = new RegularTypeName();
		unitType.setName(Helpers.string_to_qualident("Unit"));
		functionDef.setReturnType(unitType/*BuiltInTypes.Unit*/);
		functionDef.setFal(fal);
		return functionDef;
	}

	@NotNull
	private FunctionDef createGetFunction() {
		FunctionDef functionDef = new FunctionDef(this, getContext());
		functionDef.setName(Helpers.string_to_ident(String.format("<prop_get %s>", prop_name)));
		functionDef.setType(FunctionDef.Type.PROP_GET);
		functionDef.setReturnType(typeName);
		return functionDef;
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
		return context;
	}

	public void setName(IdentExpression prop_name) {
		this.prop_name = prop_name;
	}

//	public TypeName typeName() {
//		return tn;
//	}

	public Scope get_scope() {
		return get_fn.scope();
	}

	public Scope set_scope() {
		return set_fn.scope();
	}

	@Override
	public String name() {
		return prop_name.getText();
	}

	public void setTypeName(TypeName typeName) {
//		System.err.println("** setting TypeName in PropertyStatement to "+typeName);
		this.typeName = typeName;
		this.set_fn = createSetFunction();
		this.get_fn = createGetFunction();
	}

	public TypeName getTypeName() {
		return typeName;
	}

}

//
//
//
