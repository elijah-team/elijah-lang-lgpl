/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.util.buffer;

public interface Buffer {

	void append(String string);

	void append_s(String string);

	void append_cb(String string);

	void decr_i();
	
	void incr_i();
	
	void append_nl_i(String string);

	void append_nl(String string);

	void append_ln(String string);

	String getText();

}