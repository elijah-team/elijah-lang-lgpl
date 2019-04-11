/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.comp;

import java.util.HashMap;
import java.util.Map;

import tripleo.elijah.gen.CompilerContext;
import tripleo.elijah.gen.nodes.ImportNode;
import tripleo.elijah.util.NotImplementedException;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.FileBackedBuffer;

public class GenBuffer {

	private Map<String, Buffer> hdr_bufs = new HashMap<String, Buffer>();

	public void GenImportStmt(CompilerContext cctx, ImportNode impn) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		
	}

	public void InitMod(CompilerContext cctx, String string) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		
	}

	public Buffer moduleBufHdr(String module) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		Buffer buf = hdr_bufs.getOrDefault(module, new FileBackedBuffer(module+".h"));
		return buf;
	}

	public Buffer moduleBufImpl(String module) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return null;
	}

}
