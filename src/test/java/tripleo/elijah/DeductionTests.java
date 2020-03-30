/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.ModuleRef;
import tripleo.elijah.gen.TypeRef;
import tripleo.elijah.gen.nodes.*;
import tripleo.elijah.lang.NumericExpression;
import tripleo.elijah.lang.OS_Integer;
import tripleo.elijah.util.NotImplementedException;
import tripleo.util.buffer.*;

/**
 * @author Tripleo
 *
 */
public class DeductionTests {

	@Test
	public final void testParseFile() {
		List<String> args = Helpers.List_of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
		ErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);
		
		Assert.assertNotNull(c.findClass("Main"));
	}

}
	
//
//
//
