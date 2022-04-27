package tripleo.elijah.comp.queries;

import java.io.InputStream;

public class QuerySourceFileToModuleParams {
	public QuerySourceFileToModuleParams(final InputStream aInputStream, final String aSourceFilename, final boolean aDo_out) {
		inputStream    = aInputStream;
		sourceFilename = aSourceFilename;
		do_out         = aDo_out;
	}

	public final InputStream inputStream;
	public final String  sourceFilename;
	public final boolean do_out;
}
