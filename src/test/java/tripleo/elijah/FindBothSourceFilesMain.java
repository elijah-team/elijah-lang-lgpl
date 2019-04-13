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
		try {
			f.testParseFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GenBuffer gbn = new GenBuffer();
		CompilerContext cctx = new CompilerContext("fact.elijah");
		f.factorial_r(cctx , gbn);
	}

}

//
//
//
