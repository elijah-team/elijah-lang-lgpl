/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.util.TabbedOutputStream;

public class TypeRef {

	public TypeRef(String n) {
		basicName = n;
	}

	public String getTypeString() {
		return basicName;
	}

	public String repr_() {
		return (new StringBuilder("TypeRef (")).append(getTypeString()).append(
				")").toString();
	}

	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println(tos.t());
		tos.put_string_ln((new StringBuilder("TypeRef { basicName = ")).append(
				getTypeString()).append(" } ").toString());
		System.out.println(tos.t());
	}

	public String basicName;
}
