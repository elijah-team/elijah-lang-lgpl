/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang.builder;

import antlr.Token;
import tripleo.elijah.lang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 12/22/20 8:38 PM
 */
public class ClassScope extends ClassOrNamespaceScope implements Documentable {
	private final List<OS_Element> _items = new ArrayList<OS_Element>();
	private final List<Token> _docstrings = new ArrayList<Token>();

//	@Override
//	public Iterable<OS_Element> items() {
//		return _items;
//	}

	public void add(OS_Element item) {
		_items.add(item);
	}

	@Override
	public void addDocString(Token s1) {
		_docstrings.add(s1);
	}

	public void addAccess(AccessNotation acs) {
		// TODO find something to do with this
		// _items.add(acs);
	}

	public FunctionDefBuilder funcDef() {
		FunctionDefBuilder functionDefBuilder = new FunctionDefBuilder();
		add(functionDefBuilder);
		return functionDefBuilder;
	}

	public TypeAliasBuilder typeAlias() {
		return new TypeAliasBuilder();
	}

	public void addCtor(ConstructorDef cd) {
	}
}

//
//
//
