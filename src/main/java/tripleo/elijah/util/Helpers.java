/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.util;

import antlr.CommonToken;
import antlr.Token;
import com.thoughtworks.xstream.XStream;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/10/20 3:44 PM
 */
public class Helpers {
	public static void printXML(Object obj, @NotNull TabbedOutputStream tos) {
		XStream x= new XStream();
		//x.setMode(XStream.ID_REFERENCES);
		x.toXML(obj, tos.getStream());
	}

	@NotNull
	public static <E> List<E> List_of(@NotNull E... e1) {
		List<E> r = new ArrayList<E>();
		for (E e : e1) {
			r.add(e);
		}
		return r;
	}

	public static Token makeToken(String aText) {
		CommonToken t = new CommonToken();
		t.setText(aText);
		return t;
	}
}

//
//
//
