/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.elijah;

import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.gen.CompilerContext;

import java.io.IOException;

/**
 * @author tripleoacer
 *
 */
public class FindBothSourceFilesMain {

	public static void main(String[] args) {
		FindBothSourceFiles f=new FindBothSourceFiles(/*"xx"*/);
		try {
			f.compilerShouldFindBothParseFiles();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GenBuffer gbn = new GenBuffer();
		CompilerContext cctx = new CompilerContext("fact.elijah");
		new FactorialR().factorial_r(cctx , gbn);
		try {
			gbn.writeBuffers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

//
//
//
