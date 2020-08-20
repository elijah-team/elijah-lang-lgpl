package tripleo.elijah.gen;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.OS_Module;

public interface ICodeGen {


	void addClass(ClassStatement klass) ;

	void addModule(OS_Module module) ;

//	private void addModuleItem(ModuleItem element) ;

//	private void addImport(ImportStatement imp) ;

//	private void addClassItem(ClassItem element) ;

	void addFunctionItem(FunctionItem element) ;
}

//
//
//
