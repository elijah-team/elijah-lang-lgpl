package tripleo.elijah.lang;


// Referenced classes of package pak2:
//			BinaryExpression, StatementClosure, ClassStatement, ImportStatement, 
//			ExpressionType

public class ParserClosure {

	public ParserClosure() {
		module = new OS_Module();
	}

	public ClassStatement classStatement() {
		return new ClassStatement(module());
	}

	public ImportStatement importStatement() {
		return new ImportStatement(module());
	}

	private OS_Module module() {
		return module;
	}

	public void packageName(String aXy) {
		assert module.packageName ==null;
		module.packageName = aXy;
	}

	public final OS_Module module;

}

