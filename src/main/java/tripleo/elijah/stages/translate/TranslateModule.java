package tripleo.elijah.stages.translate;

import tripleo.elijah.lang.ModuleItem;
import tripleo.elijah.lang.OS_Module;
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
				System.out.println("8000 "+item);
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
