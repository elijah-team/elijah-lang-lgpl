package tripleo.elijah;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.ImportStatement;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;

public class ProgramClosure {
	public ClassStatement classStatement(OS_Element aParent) {
		final ClassStatement classStatement = new ClassStatement(aParent);
		return classStatement;
	}
	
	public ImportStatement importStatement(OS_Module aParent) {
		final ImportStatement importStatement = new ImportStatement(aParent);
		return importStatement;
	}
	
	public NamespaceStatement namespaceStatement(OS_Module aParent) {
		// TODO Auto-generated method stub
		final NamespaceStatement namespaceStatement = new NamespaceStatement(aParent);
		return namespaceStatement;
	}
	
}
