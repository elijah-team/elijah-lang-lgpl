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
package tripleo.elijah.gen.nodes;

/**
 * @author olu
 *
 */
public class MethNameNode {

	private MethHdrNode _header;
	private String ident;
	public String genName;

	public MethNameNode(String method_name, MethHdrNode header) {
		// TODO Auto-generated constructor stub
		this.ident = method_name;
		_header = header;
		genName = String.format("z%d%s", _header.getParent().getCode(), method_name); // TODO still not implememting everything
		//"z__"+method_name/*.getText()*/; // TODO wrong
	}

}
