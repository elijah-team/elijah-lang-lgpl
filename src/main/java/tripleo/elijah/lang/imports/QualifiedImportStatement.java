package tripleo.elijah.lang.imports;

import tripleo.elijah.contexts.ImportContext;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/7/20 2:09 AM
 */
public class QualifiedImportStatement implements ImportStatement {
	final OS_Element parent;
	private List<Part> _parts = new ArrayList<Part>();
	private Context _ctx;

	class Part {
		public Qualident base;
		public IdentList idents;
	}

	public QualifiedImportStatement(OS_Element aParent) {
		parent = aParent;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else
			throw new NotImplementedException();
	}

	public void addSelectivePart(Qualident aQualident, IdentList il) {
		Part p = new Part();
		p.base = aQualident;
		p.idents = il;
		addPart(p);
	}

	private void addPart(Part p) {
		_parts.add(p);
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
		for (Part part : _parts) {
			r.add(part.base);
		}
		return r;
	}

	public void setContext(ImportContext ctx) {
		_ctx = ctx;
	}

}

//
//
//
