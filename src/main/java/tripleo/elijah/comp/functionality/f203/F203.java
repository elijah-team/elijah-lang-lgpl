package tripleo.elijah.comp.functionality.f203;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.nextgen.*;

import java.io.*;
import java.time.*;

public class F203 {
	public final @NotNull ChooseDirectoryNameBehavior cdn;
	private final         ErrSink                     errSink;
	final                 LocalDateTime               localDateTime = LocalDateTime.now();

	@Contract(pure = true)
	public F203(final ErrSink aErrSink, final Compilation c) {
		errSink = aErrSink;

		//cdn = new<c.cfg._ChooseDirectoryNameBehavior>();

//		cdn = new ChooseCompilationNameBehavior(c);
//		cdn = new ChooseHashDirectoryNameBehavior(c, localDateTime);
		cdn = new ChooseHashDirectoryNameBehaviorPaths(c, localDateTime);
	}

	public File chooseDirectory() {
		final File file = cdn.chooseDirectory();

		if (cdn instanceof ChooseHashDirectoryNameBehaviorPaths) {
			final @NotNull ChooseHashDirectoryNameBehaviorPaths paths = (ChooseHashDirectoryNameBehaviorPaths) cdn;

			final CP_Path p = paths.getPath();

			p.getPathPromise().then(pp -> {
				final File file1 = pp.toFile();
				System.err.println("mkdirs 71b " + file1);
				file1.mkdirs();
			});

			return p.toFile(); // FIXME file1;
		} else {
			System.err.println("mkdirs 71 " + file);
			file.mkdirs();
			final String fn1 = new File(file, "inputs.txt").toString();
			return file;
		}
	}
}
