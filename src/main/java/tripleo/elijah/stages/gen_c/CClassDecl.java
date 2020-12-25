/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_fn.GeneratedClass;

/**
 * Created 12/24/20 7:42 AM
 */
public class CClassDecl {
	private final GeneratedClass generatedClass;
	protected String prim_decl;
	protected boolean prim = false;

	public CClassDecl(GeneratedClass generatedClass) {
		this.generatedClass = generatedClass;
	}

	public void setDecl(String str) {
		prim_decl = str;
	}

	public void setPrimitive() {
		prim = true;
	}
}

//
//
//
