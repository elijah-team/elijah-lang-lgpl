package antlr;

import java.io.IOException;


/** Parser-specific grammar subclass */
class TreeWalkerGrammar extends Grammar {
	// true for transform mode
	protected boolean transform = false;


	TreeWalkerGrammar(String className_, Tool tool_, String superClass) {
		super(className_, tool_, superClass);
	}
	/** Top-level call to generate the code for this grammar */
	public void generate() throws IOException {
		generator.gen(this);
	}
	// Get name of class from which generated parser/lexer inherits
	protected String getSuperClass() { return "TreeParser"; }
	/**Process command line arguments.
	 * -trace			have all rules call traceIn/traceOut
	 * -traceParser		have parser rules call traceIn/traceOut
	 * -debug			generate debugging output for parser debugger
	 */
	public void processArguments(String[] args) {
		for (int i=0; i<args.length; i++) {
			if ( args[i].equals("-trace") ) {
				traceRules = true;
				Tool.setArgOK(i);
			}
			else if ( args[i].equals("-traceTreeParser") ) {
				traceRules = true;
				Tool.setArgOK(i);
			}
//			else if ( args[i].equals("-debug") ) {
//				debuggingOutput = true;
//				superClass = "parseview.DebuggingTreeWalker";
//				Tool.setArgOK(i);
//			}
		}
	}
	/** Set tree parser options */
	public boolean setOption(String key, Token value) {
		if (key.equals("buildAST")) {
			if (value.getText().equals("true")) {
				buildAST = true;
			} else if (value.getText().equals("false")) {
				buildAST = false;
			} else {
				tool.error("buildAST option must be true or false", getFilename(), value.getLine());
			}
			return true;
		}
		if (key.equals("ASTLabelType")) {
			super.setOption(key, value);
			return true;
		}	
		if (super.setOption(key, value)) {
			return true;
		}
		tool.error("Invalid option: " + key, getFilename(), value.getLine());
		return false;
	}
}
