/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;

import java.util.List;

import static tripleo.elijah.gen.nodes.Helpers.List_of;

/**
 * @author Tripleo(envy)
 *
 */
public class TestBasic {

	@Test
	public final void testBasic() {
		List<String> args = List_of("test/basic", "-sE");
		ErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);
		
		Assert.fail("Not yet implemented"); // TODO
	}

}
	
//
//
//
