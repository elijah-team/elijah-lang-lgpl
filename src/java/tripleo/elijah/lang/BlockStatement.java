package tripleo.elijah.lang;

// Referenced classes of package pak2:
//			Statement, StatementClosure, FormalArgList

public class BlockStatement implements Statement, StatementItem {

	final private Scope parent;
	private TypeName tn=new RegularTypeName();
	private FormalArgList fal=new FormalArgList();
	private StatementClosure scope;
	
	public BlockStatement(Scope aParent) {
		parent = aParent;
		scope=new AbstractStatementClosure(parent);
	}

	public StatementClosure scope() {
		return scope;
	}

	public FormalArgList opfal() {
		return fal;
	}

	public TypeName returnType() {
		return tn;
	}
}
