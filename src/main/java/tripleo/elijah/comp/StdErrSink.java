/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Mar 25, 2019 at 3:00:39 PM
 *
 */
package tripleo.elijah.comp;

/**
 * @author tripleo(sb)
 *
 */
public class StdErrSink implements ErrSink {

	private int _errorCount;

	@Override
	public void exception(Exception e) {
		_errorCount++;
		System.err.println((new StringBuilder("exception: ")).append(e)
				.toString());
		e.printStackTrace(System.err);
	}

	@Override
	public void reportError(String s) {
		_errorCount++;
		System.err.println(String.format("ERROR: %s", s));
	}

	@Override
	public int errorCount() {
		return _errorCount;
	}
}

//
//
//
