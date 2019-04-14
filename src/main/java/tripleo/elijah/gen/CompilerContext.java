package tripleo.elijah.gen;

import tripleo.elijah.util.NotImplementedException;

public class CompilerContext {

	private String _module;

	public CompilerContext(String aModule) {
		_module=aModule;
	}
	public String module() {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
		return _module;
	}

}
