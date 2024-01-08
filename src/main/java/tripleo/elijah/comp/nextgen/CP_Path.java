package tripleo.elijah.comp.nextgen;

import org.jdeferred2.*;

import java.io.*;
import java.nio.file.*;

public interface CP_Path {
	CP_SubFile subFile(String aFile);

	CP_Path child(String aPath0);

	Path getPath();

	Promise<Path, Void, Void> getPathPromise();

	File toFile();

	File getRootFile();

	CP_Path getParent();

	String getName();

	_CP_RootPath getRootPath();
}
