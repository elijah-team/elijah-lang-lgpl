package tripleo.elijah.gen.java;

import java.util.*;

import javassist.*;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

public class JavaCodeGen implements ICodeGen {

	final ClassPool cp = new ClassPool();

	private List<OS_Element> finished = new ArrayList<OS_Element>();;

	public void addClass(ClassStatement klass) {
//		String pn = ((OS_Module)klass.parent).packageName();
//		if (pn != null)
//			System.out.print("package " + pn + ";");
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
				System.out.print("void " + fd.funName + "(){\n");  // TODO: _returnType and mFal
				fd.visit(this);
				System.out.print("\n}\n\n");
			} else if (element instanceof ClassStatement) {
				((ClassStatement) element).visitGen(this);
			}
		}
	}

	public void addFunctionItem(FunctionItem element) {
		// TODO Auto-generated method stub
		if (element instanceof VariableSequence)
			for (VariableStatement ii : ((VariableSequence) element).items()) {
				// TODO Will eventually have to move this
				String theType;
				if (ii.typeName().isNull()) {
//					theType = "int"; // Z0*
					theType = ii.initialValueType();
				} else{
					theType = ii.typeName().getName();
				}
				System.out.println(String.format("%s vv%s;", theType, ii.name));

			}
		else if (element instanceof ProcedureCallExpression) {
			ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("%s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			Loop loop = (Loop)element;
			if (loop.getType() == Loop.FROM_TO_TYPE) {
				String varname="vt"+loop.getIterName();
				System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
						varname, ((OS_Integer)loop.getFromPart()).getValue(),
						varname, ((OS_Integer)loop.getToPart()).getValue(),  varname));
				for (StatementItem item : loop.getItems()) {
					System.out.println("\t"+item);
				}
				System.out.println("}");
			} else throw new NotImplementedException();
		} else {
			if (elementDone(element)) {
				throw new NotImplementedException();
			} else {
				// element.visit(this);
			}
			System.out.println(element);

		}
	}

}
