package tripleo.elijah.comp.nextgen;

import tripleo.elijah.comp.nextgen.i.*;
import tripleo.elijah.nextgen.outputstatement.*;

import java.nio.file.*;

/**
 * See {@link tripleo.elijah.comp.nextgen.i.CompOutput#writeToPath(CE_Path, EG_Statement)}
 */
// TODO 09/04 Duplication madness
public interface ER_Node {
	Path getPath();

	EG_Statement getStatement();
}
