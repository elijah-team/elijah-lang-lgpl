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
import tripleo.elijah.lang.DotExpression;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.Qualident;
import tripleo.elijjah.ElijjahTokenTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/10/20 3:44 PM
 */
public class Helpers {
	public static void printXML(final Object obj, @NotNull final TabbedOutputStream tos) {
		final XStream x= new XStream();
		//x.setMode(XStream.ID_REFERENCES);
		x.toXML(obj, tos.getStream());
	}

	@NotNull
	public static <E> List<E> List_of(@NotNull final E... e1) {
		final List<E> r = new ArrayList<E>();
		for (final E e : e1) {
			r.add(e);
		}
		return r;
	}

	public static Token makeToken(final String aText) {
		final CommonToken t = new CommonToken();
		t.setText(aText);
		return t;
	}

	public static IExpression qualidentToDotExpression2(@NotNull final Qualident q) {
		return qualidentToDotExpression2(q.parts(), 1);
	}

	public static IExpression qualidentToDotExpression2(@NotNull final List<Token> ts) {
		return qualidentToDotExpression2(ts, 1);
	}

	public static IExpression qualidentToDotExpression2(@NotNull final List<Token> ts, int i) {
		if (ts.size() == 1) return new IdentExpression(ts.get(0));
		if (ts.size() == 0) return null;
		IExpression r = new IdentExpression(ts.get(0));
//		int i=1;
		while (ts.size() > i) {
			final IExpression dotExpression = qualidentToDotExpression2(ts.subList(i++, ts.size()), i+1);
			if (dotExpression == null) break;
//			r.setRight(dotExpression);
			r = new DotExpression(r, dotExpression);
		}
		return r;
	}

	@NotNull
	public static IdentExpression string_to_ident(final String txt) {
		final CommonToken t = new CommonToken(ElijjahTokenTypes.IDENT, txt);
		return new IdentExpression(t);
	}

	@NotNull
	public static String remove_single_quotes_from_string(final String s) {
		return s.substring(1, s.length()-1);
	}

	public static String String_join(String s, Iterable<String> stringIterable) {
		if (false) {
			final StringBuilder sb = new StringBuilder();

			for (final String part : stringIterable) {
				sb.append(part);
				sb.append(s);
			}
			final String ss = sb.toString();
			final String substring = s.substring(0, ss.length() - s.length());
			return substring;
		}
		// since Java 1.8
		return String.join(s, stringIterable);
	}
}

//
//
//
