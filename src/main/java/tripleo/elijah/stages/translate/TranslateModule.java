package tripleo.elijah.stages.translate;

import tripleo.elijah.lang.*;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created 9/3/20 12:38 PM
 */
public class TranslateModule {
	private final OS_Module module;

	public TranslateModule(OS_Module module_) {
		module = module_;
	}

	public void translate() {
		TabbedOutputStream w = null;
		try {
			w = getFile();

			for (ModuleItem item : module.getItems()) {
				try {
					if (item instanceof ClassStatement) {
						w.put_string_ln("class C"+((ClassStatement) item).name()+ "{");
						w.incr_tabs();
						put_class_statement((ClassStatement) item, w);
						w.dec_tabs();
						w.put_string_ln("}");
					} else if (item instanceof NamespaceStatement) {
						w.put_string_ln("class NS_"+((NamespaceStatement) item).name()+ "{");
						w.incr_tabs();
						put_namespace_statement((NamespaceStatement) item, w);
						w.dec_tabs();
						w.put_string_ln("}");
					} else
						System.out.println("8000 "+item);
				} catch (IOException e) {
					module.parent.eee.exception(e);
				}
			}
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					module.parent.eee.exception(e);
				}
			}
		}
	}

	private void put_class_statement(ClassStatement classStatement, TabbedOutputStream w) throws IOException {
		for (ClassItem item : classStatement.getItems()) {
			if (item instanceof FunctionDef) {
				w.put_string("public void "+((FunctionDef) item).name()+"(");
				put_formal_arg_list(((FunctionDef) item).fal(), w);
				w.put_string_ln(") {");
				put_function_def((FunctionDef)item, w);
				w.put_string_ln("}");
			} else
				System.out.println("8001 "+item);
		}
	}

	private void put_formal_arg_list(FormalArgList fal, TabbedOutputStream w) throws IOException {
		for (FormalArgListItem fali : fal.falis) {
			w.put_string(fali.typeName().toString());
			w.put_string(" ");
			w.put_string(fali.name());
			w.put_string(",");
		}
	}

	private void put_function_def(FunctionDef functionDef, TabbedOutputStream w) {
		for (FunctionItem item : functionDef.getItems()) {
			System.out.println("8003 "+item);
		}
	}

	private void put_namespace_statement(NamespaceStatement namespaceStatement, TabbedOutputStream w) {
		for (ClassItem item : namespaceStatement.getItems()) {
			System.out.println("8002 "+item);
		}
	}

	private TabbedOutputStream getFile() {
		final String fn = module.getFileName();
		final String fn2 = fn.substring(0, fn.lastIndexOf('.'));
		final String fn3 = fn2 + ".java";
		final Path p = Path.of("output", fn3);
		p.getParent().toFile().mkdirs();
		System.err.println("PATH "+p.toString());
		TabbedOutputStream w = null;
		try {
			w = new TabbedOutputStream(Files.newOutputStream(p));
			return w;
		} catch (IOException e) {
			module.parent.eee.exception(e);
		}
		return null;
	}
}
