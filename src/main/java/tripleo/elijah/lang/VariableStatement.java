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
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package pak:
//			TypeRef, IExpression

public class VariableStatement implements OS_Element {

	private final VariableSequence _parent;

	private TypeName typeName = new VariableTypeName();
	private IExpression initialValue = IExpression.UNASSIGNED;
	private IdentExpression name;
	private TypeModifiers typeModifiers;

	public VariableStatement(final VariableSequence aSequence) {
		_parent = aSequence;
	}

	public String getName() {
		return name.getText();
	}

	public IdentExpression getNameToken() {
		return name;
	}

	public void setName(final IdentExpression s) {
		name = s;
	}

	public void initial(final IExpression aExpr) {
		initialValue=aExpr;
	}

	public void set(final TypeModifiers y) {
		typeModifiers = y;
	}

	public TypeModifiers getTypeModifiers() {
		return typeModifiers;
	}

	@NotNull public TypeName typeName() {
		return typeName;
	}

	public void setTypeName(@NotNull final TypeName tn) {
		typeName = tn;
	}

	@NotNull public IExpression initialValue() {
		return initialValue;
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		return _parent;
	}

	@Override
	public Context getContext() {
		return getParent().getContext();
	}

	// region annotations

	List<AnnotationClause> annotations = null;

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	public void walkAnnotations(AnnotationWalker annotationWalker) {
		if (_parent.annotations != null) {
			for (AnnotationClause annotationClause : _parent.annotations) {
				for (AnnotationPart annotationPart : annotationClause.aps) {
					annotationWalker.annotation(annotationPart);
				}
			}
		}
		if (annotations == null) return;
		for (AnnotationClause annotationClause : annotations) {
			for (AnnotationPart annotationPart : annotationClause.aps) {
				annotationWalker.annotation(annotationPart);
			}
		}
	}

	// endregion
}

//
//
//
