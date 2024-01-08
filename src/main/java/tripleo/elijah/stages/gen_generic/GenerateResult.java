/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_generic;

import tripleo.elijah.stages.gen_fn.BaseEvaFunction;
import tripleo.elijah.stages.gen_fn.EvaClass;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.gen_fn.EvaNamespace;
import tripleo.elijah.stages.gen_fn.EvaNode;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 4/27/21 1:11 AM
 */
public class GenerateResult {
	private int bufferCounter = 0;

	final List<GenerateResultItem> res = new ArrayList<GenerateResultItem>();

	public void add(Buffer b, EvaNode n, TY ty) {
		res.add(new GenerateResultItem(ty, b, n, ++bufferCounter));
	}

	public List<GenerateResultItem> results() {
		return res;
	}

	public void addFunction(BaseEvaFunction aGeneratedFunction, Buffer aBuffer, TY aTY) {
		add(aBuffer, aGeneratedFunction, aTY);
	}

	public void addConstructor(GeneratedConstructor aGeneratedFunction, Buffer aBuffer, TY aTY) {
		addFunction(aGeneratedFunction, aBuffer, aTY);
	}

	public enum TY {
		HEADER, IMPL, PRIVATE_HEADER
	}

	public void addClass(TY ty, EvaClass aClass, Buffer aBuf) {
		add(aBuf, aClass, ty);
	}

	public void addNamespace(TY ty, EvaNamespace aNamespace, Buffer aBuf) {
		add(aBuf, aNamespace, ty);
	}

}

//
//
//
