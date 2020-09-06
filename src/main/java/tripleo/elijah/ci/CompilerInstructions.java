package tripleo.elijah.ci;

import tripleo.elijah.lang.IndexingStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/6/20 11:20 AM
 */
public class CompilerInstructions {
	private IndexingStatement _idx;
	private List<GenerateStatement> gens = new ArrayList<GenerateStatement>();
	public List<LibraryStatementPart> lsps = new ArrayList<LibraryStatementPart>();

	public IndexingStatement indexingStatement() {
		if (_idx == null)
			_idx = new IndexingStatement(null); // TODO ride this till the wheels fall off

		return _idx;
	}

	public void add(GenerateStatement generateStatement) {
		gens.add(generateStatement);
	}

	public void add(LibraryStatementPart libraryStatementPart) {
		lsps.add(libraryStatementPart);
	}
}

//
//
//
