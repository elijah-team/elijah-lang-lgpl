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
public class AssigningImportStatement implements ImportStatement {
	final OS_Element parent;
	private final List<Part> _parts = new ArrayList<Part>();
	private Context _ctx;

	class Part {
		Token name;
		Qualident value;
	}

	public AssigningImportStatement(OS_Element aParent) {
		parent = aParent;
		if (parent instanceof OS_Container) {
			((OS_Container) parent).add(this);
		} else
			throw new NotImplementedException();
	}

	public void addAssigningPart(Token aToken, Qualident aQualident) {
		Part p = new Part();
		p.name = aToken;
		p.value = aQualident;
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
			r.add(tokenToQualident(part.name));
		}
		return r;
	}

	@Override
	public void setContext(ImportContext ctx) {
		_ctx = ctx;
	}

	private static Qualident tokenToQualident(Token token) {
		Qualident r = new Qualident();
		r.append(token);
		return r;
	}

}

//
//
//
