/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang.builder;

import tripleo.elijah.lang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/22/20 7:59 PM
 */
public class ClassBuilder {
	private List<AnnotationClause> annotations = new ArrayList<AnnotationClause>();
	private ClassTypes _type;
	private OS_Element _parent;
	private Context _parent_context;
	private IdentExpression _name;
	private ClassScope _scope = new ClassScope();
	private ClassInheritance _inh = new ClassInheritance();

	public void setType(ClassTypes classTypes) {
		_type = classTypes;
	}

	public ClassStatement build() {
		ClassStatement cs = new ClassStatement(_parent, _parent_context);
		cs.setType(_type);
		cs.setName(_name);
		for (AnnotationClause annotation : annotations) {
			cs.addAnnotation(annotation);
		}
		for (ElBuilder builder : _scope.items()) {
//			if (builder instanceof AccessNotation) {
//				cs.addAccess((AccessNotation) builder);
//			} else {
//				cs.add(builder);
//			}
			OS_Element built;
			builder.setParent(cs);
			builder.setContext(cs.getContext());
			built = builder.build();
			cs.add(built);
		}
		_inh.setParent(cs);
		cs.setInheritance(_inh);
		return cs;
	}

	public void annotations(AnnotationClause a) {
		annotations.add(a);
	}

	public void setName(IdentExpression identExpression) {
		_name = identExpression;
	}

	public void setParent(OS_Element o) {
		_parent = o;
	}

	public void setParentContext(Context o) {
		_parent_context = o;
	}

	public ClassScope getScope() {
		return _scope;
	}

	public ClassInheritance classInheritance() {
		return _inh;
	}

}

//
//
//
