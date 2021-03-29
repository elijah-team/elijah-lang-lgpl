/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 3/29/21 5:11 PM
 */
abstract class _CommonNC {
	protected final List<ClassItem> items = new ArrayList<ClassItem>();
	private final List<String> mDocs = new ArrayList<String>();
	public Attached _a = new Attached();
	protected IdentExpression nameToken;
	protected OS_Package _packageName;
	List<AnnotationClause> annotations = null;
	private List<AccessNotation> accesses = new ArrayList<AccessNotation>();

	public void setPackageName(final OS_Package aPackageName) {
		_packageName = aPackageName;
	}

	public OS_Package getPackageName() {
		return _packageName;
	}

	public void addDocString(final Token aText) {
		mDocs.add(aText.getText());
	}

	public List<ClassItem> getItems() {
		return items ;
	}

	public String getName() {
		if (nameToken == null) return "";
		return nameToken.getText();
	}

	// OS_Container
	public List<OS_Element2> items() {
		final ArrayList<OS_Element2> a = new ArrayList<OS_Element2>();
		for (final ClassItem functionItem : getItems()) {
			final boolean b = functionItem instanceof OS_Element2;
			if (b) a.add((OS_Element2) functionItem);
		}
		return a;
	}

	// OS_Element2
	public String name() {
		return getName();
	}

	public void setName(final IdentExpression i1) {
		nameToken = i1;
	}

	public void addAnnotation(final AnnotationClause a) {
		if (annotations == null)
			annotations = new ArrayList<AnnotationClause>();
		annotations.add(a);
	}

	public void addAccess(final AccessNotation acs) {
		accesses.add(acs);
	}

	public void walkAnnotations(AnnotationWalker annotationWalker) {
		if (annotations == null) return;
		for (AnnotationClause annotationClause : annotations) {
			for (AnnotationPart annotationPart : annotationClause.aps) {
				annotationWalker.annotation(annotationPart);
			}
		}
	}

	public boolean hasItem(OS_Element element) {
		if (!(element instanceof ClassItem)) return false;
		return items.contains(element);
	}

	public void addAnnotations(List<AnnotationClause> as) {
		if (as == null) return;
		for (AnnotationClause annotationClause : as) {
			addAnnotation(annotationClause);
		}
	}

	// region ClassItem
	private AccessNotation access_note;
	private El_Category category;

	public void setCategory(El_Category aCategory) {
		category = aCategory;
	}

	public void setAccess(AccessNotation aNotation) {
		access_note = aNotation;
	}

	public El_Category getCategory() {
		return category;
	}

	public AccessNotation getAccess() {
		return access_note;
	}

	// endregion
}

//
//
//
