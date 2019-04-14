package tripleo.elijah.gen;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.OS_Module;

public interface ICodeGen {


	public void addClass(ClassStatement klass) ;

	public void addModule(OS_Module module) ;

//	private void addModuleItem(ModuleItem element) ;

//	private void addImport(ImportStatement imp) ;

//	private void addClassItem(ClassItem element) ;

	public void addFunctionItem(FunctionItem element) ;
}

//
//
//
