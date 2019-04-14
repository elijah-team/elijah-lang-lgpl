/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

public interface _Scope extends ScopeElement {
	public static abstract class ScopeExprListener implements ExprListListener {

		abstract String getScopeListenerName();

		@Override
		public void change(IExpression e) {
			System.out
					.println((new StringBuilder(String.valueOf(getScopeListenerName()))).append(" changed").toString());
			changed++;
		}

		@Override
		public String repr_() {
			return (new StringBuilder("{")).append(getScopeListenerName()).append("}").toString();
		}

		@Override
		public boolean isEmpty() {
			return changed == 0;
		}

		int changed;

		public ScopeExprListener() {
		}
	}

	public abstract String repr_();

	public abstract void push(ScopeElement scopeelement);

	public abstract ExprListListener getListener();
}
