/**
 * 
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.lang.ClassItem;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.ImportStatement;
import tripleo.elijah.lang.LookupResult;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.Loop;
import tripleo.elijah.lang.ModuleItem;
import tripleo.elijah.lang.NamespaceModify;
import tripleo.elijah.lang.NumericExpression;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.ProcedureCallExpression;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.lang.StatementItem;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 5:39:30 AM
 */
public class DeduceTypes {

	private static int _classCode = 101;
	private static int _functionCode = 1001;

	private OS_Module module;

	public DeduceTypes(OS_Module module) {
//		NotImplementedException.raise();
		this.module = module;
	}

	public void addClass(ClassStatement klass, OS_Module parent) {
//		System.out.print("class " + klass.clsName + "{\n");
		klass._a.setCode(nextClassCode());
		parent.getContext().add(klass, klass.getName());
		{
			for (ClassItem element : klass.items())
				addClassItem(element, klass);
		}
//		System.out.print("}\n");
	}
	
	private void addClassItem(ClassItem element, ClassStatement parent) {
		{
			if (element instanceof FunctionDef) {
				FunctionDef fd = (FunctionDef) element;
//				System.out.print("void " + fd.funName + "(){\n");  // TODO: _returnType and mFal
				fd._a.setCode(nextFunctionCode());
				parent._a.getContext().add(fd, fd.funName);
				{
					for (FunctionItem fi : fd.items())
						addFunctionItem(fi, fd);

				}
//				fd.visit(this);
//				System.out.print("\n}\n\n");
			} else if (element instanceof ClassStatement) {
//				((ClassStatement) element).visitGen(this);
			}
		}
	}
	
	public void addFunctionItem(FunctionItem element, FunctionDef parent) {
		// TODO Auto-generated method stub
		if (element instanceof VariableSequence) {
//			fd._a.setCode(nextFunctionCode());
//			parent._a.getContext().add(element, null);
			for (VariableStatement ii : ((VariableSequence) element).items()) {
				OS_Type dtype = null;
				if (ii.typeName().isNull()) {
					if (ii.initialValue() != null) {
						IExpression iv = ii.initialValue();
						if (iv instanceof NumericExpression) {
							dtype = new OS_Type(BuiltInTypes.SystemInteger);
						} else if (iv instanceof IdentExpression) {
							LookupResultList lrl = parent.getContext().lookup(((IdentExpression) iv).getText());
							for (LookupResult n: lrl.results()) {
								System.out.println("99"+n);
							}
						}
					}
				}
				parent._a.getContext().add(ii, ii.getName(), dtype);
//				String theType;
//				if (ii.typeName().isNull()) {
////					theType = "int"; // Z0*
//					theType = ii.initialValueType();
//				} else{
//					theType = ii.typeName().getName();
//				}
				System.out.println(String.format("%s;", ii.getName()));

			}
		}
		else if (element instanceof ProcedureCallExpression) {
			ProcedureCallExpression pce = (ProcedureCallExpression) element;
			System.out.println(String.format("%s(%s);", pce./*target*/getLeft(), pce.exprList()));
		} else if (element instanceof Loop) {
			Loop loop = (Loop)element;
			if (loop.getType() == Loop.FROM_TO_TYPE) {
				String varname="vt"+loop.getIterName();
				final NumericExpression fromPart = (NumericExpression)loop.getFromPart();
				if (loop.getToPart() instanceof NumericExpression) {
					final NumericExpression toPart = (NumericExpression)loop.getToPart();
				
					System.out.println(String.format("{for (int %s=%d;%s<=%d;%s++){\n\t",
							varname, fromPart.getValue(),
							varname, toPart.getValue(),  varname));
				} else if (loop.getToPart() instanceof IdentExpression) {
					final IdentExpression toPart = (IdentExpression)loop.getToPart();
					
					System.out.println(String.format("{for (int %s=%d;%s<=%s;%s++){\n\t",
							varname, fromPart.getValue(),
							varname, "vv"+toPart.getText(),  varname));
					
				}
				for (StatementItem item : loop.getItems()) {
					System.out.println("\t"+item);
				}
				System.out.println("}");
			} else throw new NotImplementedException();
		} else {
				// element.visit(this);
			System.out.println(element);

		}
	}
	
	private void addImport(ImportStatement imp) {
//		throw new NotImplementedException();
		if (imp.getRoot() == null) {
			for (Qualident q : imp.parts()) {
				module.modify_namespace(q, NamespaceModify.IMPORT);
			}
		}
//		module.
	}

	private void addModuleItem(ModuleItem element) {
		// TODO Auto-generated method stub
		if (element instanceof ClassStatement) {
			ClassStatement cl = (ClassStatement) element;
			addClass(cl, module);
		} else if (element instanceof ImportStatement) {
			ImportStatement imp = (ImportStatement) element;
			addImport(imp);
		}
	}
	public void deduce() {
		System.out.println("-------------------------------------------");
		for (ModuleItem element : module.items) {
			addModuleItem(element);
		}
	}

	private int nextClassCode() {
		return ++_classCode;
	}
	
	private int nextFunctionCode() {
		return ++_functionCode;
	}
}
