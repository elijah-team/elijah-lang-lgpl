/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.slir;

import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.OS_Element;

/**
 * Created 11/6/21 9:03 AM
 */
public class SlirFunctionNode implements SlirElement {
	private final SlirElement parent;
	private final String functionName;
	private final FunctionDef functionDef;

	public SlirFunctionNode(final SlirClass aSlirClass, final String aFunctionName, final FunctionDef aFunctionDef) {
		parent = aSlirClass;
		functionName = aFunctionName;
		functionDef = aFunctionDef;
	}

	public SlirFunctionNode(final SlirNamespaceNode aParent, final String aFunctionName, final FunctionDef aFunctionDef) {
		parent = aParent;
		functionName = aFunctionName;
		functionDef = aFunctionDef;
	}

	public void annotate(final SlirAnnotations aAnnotation) {
		// TODO implement me
	}

	@Override
	public String name() {
		return functionName;
	}

	@Override
	public SlirPos partOfSpeech() {
		return SlirPos.FUNCTION;
	}

	@Override
	public OS_Element element() {
		return functionDef;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
