package tripleo.elijah.nextgen.inputtree;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.lang.*;

public class EIT_ModuleInput implements EIT_Input {
	private final Compilation c;
	private final OS_Module   module;

	@Contract(pure = true)
	public EIT_ModuleInput(final OS_Module aModule, final Compilation aC) {
		module = aModule;
		c      = aC;
	}

//	public @NotNull SM_Module computeSourceModel() {
//		final SM_Module sm = new SM_Module_(this);
//		return sm;
//	}

	@Override
	public @NotNull EIT_InputType getType() {
		return EIT_InputType.ELIJAH_SOURCE;
	}

	public OS_Module module() {
		return this.module;
	}
}
