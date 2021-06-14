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
import tripleo.elijah.entrypoints.MainClassEntryPoint;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.util.Helpers;

import java.util.List;

/**
 * @author Tripleo
 *
 */
public class FindClassesInDemoElNormal {

	@Test
	public final void testParseFile() {
		final List<String> args = tripleo.elijah.util.Helpers.List_of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
		final ErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		final List<ClassStatement> aClassList = c.findClass("Main");
		for (final ClassStatement classStatement : aClassList) {
			System.out.println(classStatement.getPackageName().getName());
		}
		Assert.assertEquals(3, aClassList.size());  // NOTE this may change. be aware
	}


	@Test
	public final void testListFolders() {
		final List<String> args = Helpers.List_of("test/demo-el-normal/listfolders/", "-sE");
		final ErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		final List<ClassStatement> aClassList = c.findClass("Main");
		Assert.assertEquals(1, aClassList.size());

		Assert.assertFalse("isMainClass", MainClassEntryPoint.isMainClass(aClassList.get(0)));
	}

}
	
//
//
//
