package tripleo.elijah.comp.queries;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import tripleo.elijah.Out;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.Operation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.query.QueryDatabase;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;

import java.io.InputStream;

public class QuerySourceFileToModule {
	private final QuerySourceFileToModuleParams params;
	private       Compilation                   compilation;

	public QuerySourceFileToModule(final QuerySourceFileToModuleParams aParams, final Compilation aCompilation) {
		params      = aParams;
		compilation = aCompilation;
	}

	public OS_Module load(final QueryDatabase qb) {
		throw new NotImplementedException();
	}

	public Operation<OS_Module> calculate() {
		final String f = params.sourceFilename;
		final InputStream s = params.inputStream;
		final boolean do_out = params.do_out;

		final ElijjahLexer lexer = new ElijjahLexer(s);
		lexer.setFilename(f);
		final ElijjahParser parser = new ElijjahParser(lexer);
		parser.out = new Out(f, compilation, do_out);
		parser.setFilename(f);
		try {
			parser.program();
		} catch (RecognitionException aE) {
			return Operation.failure(aE);
		} catch (TokenStreamException aE) {
			return Operation.failure(aE);
		}
		final OS_Module module = parser.out.module();
		parser.out = null;
		return Operation.success(module);
	}


}
