/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.lang.Context;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;

/**
 * Created 11/30/21 11:56 PM
 */
public class DeduceProcCall {
	private final ProcTableEntry procTableEntry;
	private DeduceTypes2 deduceTypes2;
	private Context context;
	private BaseGeneratedFunction generatedFunction;

	public DeduceProcCall(final ProcTableEntry aProcTableEntry) {
		procTableEntry = aProcTableEntry;
	}

	public void setDeduceTypes2(final DeduceTypes2 aDeduceTypes2, final Context aContext, final BaseGeneratedFunction aGeneratedFunction) {
		deduceTypes2 = aDeduceTypes2;
		context = aContext;
		generatedFunction = aGeneratedFunction;
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
