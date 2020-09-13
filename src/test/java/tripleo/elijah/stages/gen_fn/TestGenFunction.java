/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.instructions.InstructionName;

import java.io.File;
import java.util.List;

/**
 * Created 9/10/20 2:20 PM
 */
public class TestGenFunction {

	@Test
	public void testDemoElNormalFact1Elijah() throws Exception {
		StdErrSink eee = new StdErrSink();
		Compilation c = new Compilation(eee, new IO());

		String f = "test/demo-el-normal/fact1.elijah";
		File file = new File(f);
		OS_Module m = c.realParseElijjahFile(f, file, false);
		Assert.assertTrue("Method parsed correctly", m != null);

		final GenerateFunctions gfm = new GenerateFunctions(m);
		List<GeneratedFunction> lgf = gfm.generateAllTopLevelFunctions();

		for (GeneratedFunction gf : lgf) {
			System.err.println("7000 "+gf);

			if (gf.name().equals("main")) {
				Assert.assertEquals(InstructionName.E,    gf.getInstruction(0).getName());
				Assert.assertEquals(InstructionName.AGN,  gf.getInstruction(1).getName());
				Assert.assertEquals(InstructionName.CALL, gf.getInstruction(2).getName());
				Assert.assertEquals(InstructionName.X,    gf.getInstruction(3).getName());
			} else if (gf.name().equals("factorial")) {
				Assert.assertEquals(InstructionName.E,    gf.getInstruction(0).getName());
			}
		}
	}
}
