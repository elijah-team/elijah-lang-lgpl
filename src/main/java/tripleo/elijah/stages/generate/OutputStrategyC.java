/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.generate;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.lang.NamespaceTypes;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.GeneratedNamespace;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResult;

import java.io.File;

/**
 * Created 1/13/21 5:54 AM
 */
public class OutputStrategyC {
	private final OutputStrategy outputStrategy;

	public OutputStrategyC(OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public String nameForNamespace(GeneratedNamespace generatedNamespace, GenerateResult.TY aTy) {
		if (generatedNamespace.module().isPrelude()) {
			// We are dealing with the Prelude
			StringBuilder sb = new StringBuilder();
			sb.append("/Prelude/");
			sb.append("Prelude");
			switch (aTy) {
				case IMPL:
					sb.append(".c");
					break;
				case PRIVATE_HEADER:
					sb.append("_Priv.h");
				case HEADER:
					sb.append(".h");
					break;
			}
			return sb.toString();
		}
		String filename;
		if (generatedNamespace.getNamespaceStatement().getKind() == NamespaceTypes.MODULE) {
			final String moduleFileName = generatedNamespace.module().getFileName();
			File moduleFile = new File(moduleFileName);
			filename = moduleFile.getName();
			filename = strip_elijah_extension(filename);
		} else
			filename = generatedNamespace.getName();
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		final LibraryStatementPart lsp = generatedNamespace.module().getLsp();
		if (lsp == null)
			sb.append("___________________");
		else
			sb.append(lsp.getInstructions().getName());
		sb.append("/");
		OS_Package pkg = generatedNamespace.getNamespaceStatement().getPackageName();
		if (pkg != OS_Package.default_package) {
			sb.append(pkg.getName());
			sb.append("/");
		}
		sb.append(filename);
		switch (aTy) {
			case IMPL:
				sb.append(".c");
				break;
			case PRIVATE_HEADER:
				sb.append("_Priv.h");
			case HEADER:
				sb.append(".h");
				break;
		}
		return sb.toString();
	}

	String strip_elijah_extension(String aFilename) {
		if (aFilename.endsWith(".elijah")) {
			aFilename = aFilename.substring(0, aFilename.length()-7);
		} else if (aFilename.endsWith(".elijjah")) {
			aFilename = aFilename.substring(0, aFilename.length()-8);
		}
		return aFilename;
	}

	public String nameForFunction(GeneratedFunction generatedFunction) {
		GeneratedNode c = generatedFunction.getGenClass();
		if (c instanceof GeneratedClass)
			return nameForClass((GeneratedClass) c, GenerateResult.TY.IMPL);
		else if (c instanceof GeneratedNamespace)
			return nameForNamespace((GeneratedNamespace) c, GenerateResult.TY.IMPL);
		return null;
	}

	public String nameForClass(GeneratedClass generatedClass, GenerateResult.TY aTy) {
		if (generatedClass.module().isPrelude()) {
			// We are dealing with the Prelude
			StringBuilder sb = new StringBuilder();
			sb.append("/Prelude/");
			sb.append("Prelude");
			switch (aTy) {
				case IMPL:
					sb.append(".c");
					break;
				case PRIVATE_HEADER:
					sb.append("_Priv.h");
				case HEADER:
					sb.append(".h");
					break;
			}
			return sb.toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		final LibraryStatementPart lsp = generatedClass.module().getLsp();
		if (lsp == null)
			sb.append("______________");
		else
//			sb.append(generatedClass.module.lsp.getName());
			sb.append(lsp.getInstructions().getName());
		sb.append("/");
		OS_Package pkg = generatedClass.getKlass().getPackageName();
		if (pkg != OS_Package.default_package) {
			sb.append(pkg.getName());
			sb.append("/");
		}
		switch (outputStrategy.per()) {
			case PER_CLASS:
				sb.append(generatedClass.getName());
				break;
			case PER_MODULE:
				{
//					mod = generatedClass.getKlass().getContext().module();
					OS_Module mod = generatedClass.module();
					File f = new File(mod.getFileName());
					String ff = f.getName();
					int y=2;
					ff = strip_elijah_extension(ff);
					sb.append(ff);
//					sb.append('/');
				}
				break;
			case PER_PACKAGE:
				{
					final OS_Package pkg2 = generatedClass.getKlass().getPackageName();
					String pkgName;
					if (pkg2 != OS_Package.default_package) {
						pkgName = "$default_package";
					} else
						pkgName = pkg2.getName();
					sb.append(pkgName);
//					sb.append('/');
				}
				break;
			case PER_PROGRAM:
				{
					CompilerInstructions xx = lsp.getInstructions();
					String n = xx.getName();
					sb.append(n);
//					sb.append('/');
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + outputStrategy.per());
		}
		switch (aTy) {
			case IMPL:
				sb.append(".c");
				break;
			case PRIVATE_HEADER:
				sb.append("_Priv.h");
			case HEADER:
				sb.append(".h");
				break;
		}
		return sb.toString();
	}

}

//
//
//
