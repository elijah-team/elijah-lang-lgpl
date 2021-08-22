/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 8/21/21 10:09 PM
 */
public class Pipeline {
	List<PipelineMember> pls = new ArrayList<>();

	public void add(PipelineMember aPipelineMember) {
		pls.add(aPipelineMember);
	}


	public void run() throws Exception {
		for (PipelineMember pl : pls) {
			pl.run();
		}
	}
}

//
//
//
