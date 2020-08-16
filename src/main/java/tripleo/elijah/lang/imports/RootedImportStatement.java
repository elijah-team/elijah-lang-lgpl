package tripleo.elijah.lang.imports;

import antlr.Token;
import tripleo.elijah.contexts.ImportContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/7/20 2:09 AM
 */
public class RootedImportStatement implements ImportStatement {
	final OS_Element parent;
	private QualidentList importList = new QualidentList();
	/** Used in from syntax
	 * @category from
	 */
	private Qualident root;
	private Context _ctx;

	public RootedImportStatement(OS_Element aParent) {
		parent = aParent;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else
			throw new NotImplementedException();
	}

	public Qualident getRoot() {
		return root;
	}

	public void setRoot(Qualident root) {
		this.root = root;
	}

	/** Used in from syntax
	 * @category from
	 */
	public void importRoot(Qualident xyz) {
		setRoot(xyz);
	}

	public QualidentList importList() {
		return importList;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return parent.getContext();
	}

	@Override
	public List<Qualident> parts() {
		List<Qualident> r = new ArrayList<Qualident>();
		for (Qualident qualident : importList.parts) {
			Qualident q = new Qualident();
			for (Token part : q.parts()) {
				q.append(part);
			}
			for (Token part : qualident.parts()) {
				q.append(part);
			}
			r.add(q);
		}
		return r;
	}

	public void setContext(ImportContext ctx) {
		_ctx = ctx;
	}

}
