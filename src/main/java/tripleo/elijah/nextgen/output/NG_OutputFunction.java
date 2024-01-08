package tripleo.elijah.nextgen.output;

import org.jetbrains.annotations.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.elijah.nextgen.outputtree.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.gen_generic.*;
import tripleo.elijah.stages.generate.*;

import java.util.*;

public class NG_OutputFunction implements NG_OutputItem {
	@Override
	public @NotNull List<NG_OutputStatement> getOutputs() {
		return null;
	}

	@Override
	public EOT_FileNameProvider outName(final OutputStrategyC aOutputStrategyC, final GenerateResult.TY ty) {
		return null;
	}
//	private List<C2C_Result> collect;
//	//private GenerateFiles    generateFiles;
//	private IPP_Function     ppf;
//
//	@Override
//	public @NotNull List<NG_OutputStatement> getOutputs() {
//		final List<NG_OutputStatement> r = new ArrayList<>();
//
//		if (collect != null) {
//			for (C2C_Result c2c : collect) {
//				final EG_Statement x = c2c.getStatement();
//				final GenerateResult.TY y = c2c.ty();
//
//				r.add(new NG_OutputFunctionStatement(c2c));
//			}
//		}
//
//		return r;
//	}
//
//	@Override
//	public EOT_FileNameProvider outName(final @NotNull OutputStrategyC aOutputStrategyC, final GenerateResult.@NotNull TY ty) {
//		if (getGf() instanceof EvaFunction)
//			return aOutputStrategyC.nameForFunction1((EvaFunction) getGf(), ty);
//		else
//			return aOutputStrategyC.nameForConstructor1((EvaConstructor) getGf(), ty);
//	}
//
//	public BaseEvaFunction getGf() {
//		return ((PP_Function) ppf).getCarrier();
//	}
//
//	public void setFunction(final IPP_Function aGf, final GenerateFiles ignoredAGenerateFiles, final List<C2C_Result> aCollect) {
//		ppf           = aGf;
//		//generateFiles = aGenerateFiles;
//		collect       = aCollect;
//	}
}
