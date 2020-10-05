/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tripleo(envy)
 *
 */
public class TestBasic {

	@Test
	public final void testBasicParse() throws IOException {
		List<String> ez_files = Files.readLines(new File("test/basic/ez_files.txt"), Charsets.UTF_8);
		List<String> args = new ArrayList<String>();
		args.addAll(ez_files);
		args.add("-sE");
		ErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		Assert.assertEquals(0, c.errorCount());
	}

	@Test
	public final void testBasic() throws IOException {
		List<String> ez_files = Files.readLines(new File("test/basic/ez_files.txt"), Charsets.UTF_8);
		List<String> args = new ArrayList<String>();
		args.addAll(ez_files);
		args.add("-sO");
//		List<String> args = List_of("test/basic", "-sO"/*, "-out"*/);
		ErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

//		Assert.fail("Not yet implemented"); // TODO
		Assert.assertEquals(14, c.errorCount());
	}

}
	
//
//
//
