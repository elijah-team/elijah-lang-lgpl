/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
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
	private String filename;

	public IndexingStatement indexingStatement() {
		if (_idx == null)
			_idx = new IndexingStatement(null); // TODO ride this till the wheels fall off

		return _idx;
	}

	public void add(final GenerateStatement generateStatement) {
		gens.add(generateStatement);
	}

	public void add(final LibraryStatementPart libraryStatementPart) {
		lsps.add(libraryStatementPart);
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}
}

//
//
//
