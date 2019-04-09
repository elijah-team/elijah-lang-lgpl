/**
 * 
 */
package tripleo.elijah;

import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.gen.CompilerContext;

/**
 * @author tripleoacer
 *
 */
public class FindBothSourceFilesMain {

	public static void main(String[] args) {
		FindBothSourceFiles f=new FindBothSourceFiles("xx");
		f.testParseFile();
		GenBuffer gbn = new GenBuffer();
		CompilerContext cctx = new CompilerContext();
		f.factorial_r(cctx , gbn);
	}

}

//
//
//
