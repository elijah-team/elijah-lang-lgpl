/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.TypeName;

/**
 * Created 12/26/20 5:08 AM
 */
public class ResolveError extends Exception implements Diagnostic {
	private final TypeName typeName;
	private final LookupResultList lrl;

	public ResolveError(TypeName typeName, LookupResultList lrl) {
		this.typeName = typeName;
		this.lrl = lrl;
	}
}

//
//
//
