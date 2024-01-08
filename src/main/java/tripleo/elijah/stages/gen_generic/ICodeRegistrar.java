package tripleo.elijah.stages.gen_generic;

import tripleo.elijah.stages.gen_fn.*;

/**
 * Move this to Mir or Lir layer ASAP
 * <p>
 * Created 11/28/21 4:45 PM
 */
public interface ICodeRegistrar {
	void registerClass(EvaClass aClass);

	void registerClass1(EvaClass aClass);

	void registerFunction(BaseEvaFunction aFunction);

	void registerFunction1(BaseEvaFunction aFunction);

	void registerNamespace(EvaNamespace aNamespace);
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab:
//
