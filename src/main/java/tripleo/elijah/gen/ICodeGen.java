package tripleo.elijah.gen;

import java.util.*;

import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

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
