/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.lang2.BuiltInTypes;

/**
 * @author Tripleo
 *
 * Created 	Apr 18, 2020 at 2:43:00 AM
 */
public class TypeCheckExpression extends AbstractExpression implements IExpression {
    private final IExpression checking;
    private final NormalTypeName checkfor;

    public TypeCheckExpression(IExpression ee, NormalTypeName p1) {
        this.checking = ee;
        this.checkfor = p1;
    }

    @Override
    public boolean is_simple() {
        return true; // TODO is not const tho
    }

    @Override
    public void setType(OS_Type deducedExpression) {
        throw new IllegalStateException("Type of TypeCheckExpression is always boolean");
    }

    @Override
    public OS_Type getType() {
        return new OS_Type(BuiltInTypes.Boolean);
    }
}
