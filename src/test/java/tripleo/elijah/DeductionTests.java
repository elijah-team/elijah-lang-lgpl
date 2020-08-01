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
import tripleo.elijah.comp.ListErrSink;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.OS_Element;

import java.util.List;

/**
 * @author Tripleo
 *
 */
public class DeductionTests {

	@Test
	public final void testParseFile() {
		List<String> args = Helpers.List_of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
		ErrSink eee = new ListErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		final OS_Element aClass = c.findClass("Main");
		Assert.assertNotNull(aClass);
	}


	@Test
	public final void testListFolders() {
		List<String> args = Helpers.List_of("test/demo-el-normal/listfolders2.elijah", "test/demo-el-normal/listfolders.elijah", "-sE");
		ErrSink eee = new ListErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		final OS_Element aClass = c.findClass("Main");
		Assert.assertNotNull(aClass);
	}

}
	
//
//
//
