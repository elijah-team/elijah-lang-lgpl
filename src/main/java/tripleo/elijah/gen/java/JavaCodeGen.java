package tripleo.elijah.gen.java;

import java.util.*;

import javassist.*;
import tripleo.elijah.lang.*;
import tripleo.elijah.gen.*;
import tripleo.elijah.util.NotImplementedException;

public class JavaCodeGen implements ICodeGen {

	final ClassPool cp = new ClassPool();

	private List<OS_Element> finished = new ArrayList<OS_Element>();;

	public void addClass(ClassStatement klass) {
		String pn = klass.parent.packageName;
		if (pn != null)
			System.out.print("package " + pn + ";");
		System.out.print("class " + klass.clsName + "{\n");
		if (elementDone(klass))
			try {
				CtClass ctc = cp.makeClass(klass.clsName);
				// ctc.setPackagename(pacakageName); // TODO
				System.out.println(ctc.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			for (ClassItem element : klass.items())
				addClassItem(element);
		}
		System.out.print("}\n");
	}

	public void addModule(OS_Module module) {
		if (elementDone(module)) {
			try {
				CtClass ctc = cp.makeClass(module.moduleName);
				// ctc.setPackagename(pacakageName); // TODO
				System.out.println(ctc.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			synchronized (module.items) {
				for (ModuleItem element : module.items)
					addModuleItem(element);

			}

		}
	}

	private boolean elementDone(OS_Element module) {
		boolean contains = finished.contains(module);
		// if (!contains)
		// finished.add(module);
		return contains;
	}

	private void addModuleItem(ModuleItem element) {
		// TODO Auto-generated method stub
		if (element instanceof ClassStatement) {
			ClassStatement cl = (ClassStatement) element;
			addClass(cl);
		} else if (element instanceof ImportStatement) {
			ImportStatement imp = (ImportStatement) element;
			addImport(imp);
		}
		// if (elementDone(element)) {
		// try {
		// CtClass ctc = cp.makeClass(element.);
		// // ctc.setPackagename(pacakageName); // TODO
		// System.out.println(ctc.toString());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else {
		// synchronized (element.items) {
		// for (ModuleItem element : module.items)
		// addModuleItem(element);
		//
		// }
		//
		// }
	}

	private void addImport(ImportStatement imp) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	private void addClassItem(ClassItem element) {
		// TODO Auto-generated method stub
		// throw new NotImplementedException();
		if (elementDone(element)) {
			throw new NotImplementedException();
		} else {
			if (element instanceof FunctionDef) {
				FunctionDef fd = (FunctionDef) element;
				System.out.print("void " + fd.funName + "(){\n");
				element.visit(this);
				System.out.print("}\n\n");
			} else
				element.visit(this);
		}
	}

	public void addFunctionItem(FunctionItem element) {
		// TODO Auto-generated method stub
		if (element instanceof VariableSequence)
			for (VariableStatement ii : ((VariableSequence) element).items()) {
				System.out.print("int vv" + ii.name + ";");

			}
		else {
			if (elementDone(element)) {
				throw new NotImplementedException();
			} else {
				// element.visit(this);
			}
			System.out.print(element);

		}
	}

}
