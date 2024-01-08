package tripleo.elijah.comp;

import java.io.*;
import java.nio.file.*;

public class Q {
	public static Path makePath(final String aRequestedFilename) {
		return FileSystems.getDefault().getPath(aRequestedFilename);
	}

	public static Path makePath(final String aRequestedFileName, final String aCName, final String aDate) {
		final File f = new File(new File(aRequestedFileName, aCName), aDate);
		return f.toPath();
	}

	public static Path makePath(final String aRequestedFileName, final String aKey) {
		final File f = new File(aRequestedFileName, aKey);
		return f.toPath();
	}
}
