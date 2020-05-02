/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.expand;

import tripleo.elijah.lang.ExpressionList;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.VariableStatement;

/**
 * @author Tripleo
 *
 * Created 	Apr 30, 2020 at 04:19:23
 */
public class IntroducedVariable implements FunctionPrelimInstruction {

    private VariableStatement vardecl;
    Type kind;
    ExpressionList args;
    IExpression variable;

    public IntroducedVariable(IExpression variable) {
        this.variable = variable;
        this.kind = Type.VARREF;
    }

    public IntroducedVariable(VariableStatement variable) {
        this.vardecl = variable;
        this.kind = Type.VARDECL;
    }

    public void makeIntoFunctionCall(ExpressionList args) {
        this.args = args == null ? new ExpressionList() : args; // TODO is this really necessary?
        this.kind = Type.PROCEDURE_CALL;
        System.out.println("[#makeIntoFunctionCall] " + this + " " + args);
    }

    public enum Type {
        PROCEDURE_CALL, VARDECL, VARREF

    }

}

//
//
//
