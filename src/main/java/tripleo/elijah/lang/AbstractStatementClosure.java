/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */

package tripleo.elijah.lang;

import antlr.Token;
import org.jetbrains.annotations.Contract;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;


// Referenced classes of package pak2:
//			StatementClosure, VariableSequence, ProcedureCallExpression, Loop, 
//			YieldExpression, IfExpression

public final class AbstractStatementClosure implements StatementClosure, StatementItem {

	private ClassStatement realParent;

	public AbstractStatementClosure(Scope aParent) {
		// TODO Auto-generated constructor stub
		parent = aParent;
	}

	public AbstractStatementClosure(final ClassStatement classStatement) {
		// TODO check final member
		realParent = classStatement;
		parent = new Scope() {

			@Override
			public void addDocString(Token s1) {
				classStatement.addDocString(s1);
			}

			@Override
			public void statementWrapper(IExpression aExpr) {
				throw new NotImplementedException();
			}

			@Override
			public StatementClosure statementClosure() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public BlockStatement blockStatement() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public void add(StatementItem aItem) {
				// TODO Auto-generated method stub
//				NotImplementedException.raise();
				classStatement.add((ClassItem) aItem);
			}

			@Override
			public TypeAliasExpression typeAlias() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public InvariantStatement invariantStatement() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public OS_Element getElement() {
				return realParent;
			}

		};
	}

	@Override
	public BlockStatement blockClosure() {
		bs=new BlockStatement(this.parent);
		add(bs);
		return bs;
	}
	public IExpression constructExpression() {
		ctex=new ConstructExpression(this.parent); 
		add(ctex);
		return ctex;
	}
	@Override
	public void constructExpression(IExpression aExpr, FormalArgList aO) {
		add((StatementItem) aExpr);
	}
	@Override
 	public IfConditional ifConditional() {
		ifex=new IfConditional(this.parent);
		add(ifex);
		return ifex;
	}
	@Override
	public Loop loop() {
		loop = new Loop(this.parent.getElement());
		add(loop);
		return loop;
	}
/*
	@Override
	public StatementClosure procCallExpr() {
		pcex=new AbstractStatementClosure(parent); //TODO:
//		add(pcex);
		return pcex;
	}
*/

	@Override
	public ProcedureCallExpression procedureCallExpression() {
		pce=new ProcedureCallExpression();
		add(new StatementWrapper(pce));
		return pce;
	}

	@Override
	public VariableSequence varSeq() {
		vsq=new VariableSequence();
		vsq.setParent(parent.getParent()/*this.getParent()*/);
		return (VariableSequence) add(vsq);
	}

	private OS_Element getParent() {
		return realParent;
	}

	@Override
	public void yield(IExpression aExpr) {
		add(new YieldExpression(aExpr));
	}

	@Contract("_ -> param1")
	private StatementItem add(StatementItem aItem) {
		parent.add(aItem);
		return aItem;
	}
	
	private BlockStatement bs;
	private ConstructExpression ctex;
	private IfConditional ifex;
	
	private Loop loop;
	private ProcedureCallExpression pce;
	private AbstractStatementClosure pcex;
	private VariableSequence vsq;
	private YieldExpression yiex;

	final List<StatementItem> items =new ArrayList<StatementItem>();

	final Scope parent;

	@Override
	public CaseConditional caseConditional() {
		final CaseConditional caseConditional = new CaseConditional(getParent());
		add(caseConditional);
		return caseConditional;
	}

	@Override
	public MatchConditional matchConditional() {
		final MatchConditional matchConditional = new MatchConditional(getParent());
		add(matchConditional);
		return matchConditional;
	}

	@Override
	public void statementWrapper(IExpression expr) {
		parent.statementWrapper(expr);
	}
}

//
//
//
