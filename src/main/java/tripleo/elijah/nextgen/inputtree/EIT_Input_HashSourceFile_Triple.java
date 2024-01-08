package tripleo.elijah.nextgen.inputtree;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.util.*;

import java.io.*;
import java.util.*;

import static tripleo.elijah.util.Mode.*;

public final class EIT_Input_HashSourceFile_Triple implements EIT_Input {
	private final String hash;
	private final EIT_SourceOrigin source;
	private final String filename;

	public EIT_Input_HashSourceFile_Triple(String hash,
	                                       EIT_SourceOrigin source,
	                                       String filename) {
		this.hash = hash;
		this.source = source;
		this.filename = filename;
	}

	public static @NotNull EIT_Input_HashSourceFile_Triple decode(final @NotNull String fn) {
		// move to Builder...Operation...
		// also CP_Filename hashPromise products
		final @NotNull Operation<String> op2 = Helpers.getHashForFilename2(fn);

		if (op2.mode() == SUCCESS) {
			final String hh = op2.success();
			assert hh != null;

			EIT_SourceOrigin x;

			// TODO EG_Statement here

			if (fn.equals("lib_elijjah/lib-c/Prelude.elijjah")) {
				x = EIT_SourceOrigin.PREL;
			} else if (fn.startsWith("lib_elijjah/")) {
				x = EIT_SourceOrigin.LIB;
			} else if (fn.startsWith("test/")) {
				x = EIT_SourceOrigin.SRC;
			} else {
				throw new IllegalStateException("Error"); // Operation??
			}

			//noinspection UnnecessaryLocalVariable
			final EIT_Input_HashSourceFile_Triple yy2 = new EIT_Input_HashSourceFile_Triple(hh, x, fn);
			return yy2;
		}
		throw new IllegalStateException("hash failure"); // Operation??
	}

	@Override
	public @NotNull EIT_InputType getType() {
		// builder?? memtc st pat
		if (filename.endsWith(".elijah")) {
			return EIT_InputType.ELIJAH_SOURCE;
		}
		if (filename.endsWith(".ez")) {
			return EIT_InputType.EZ_FILE;
		}
		throw new IllegalStateException("Unexpected value " + filename);
	}

	public String hash() {
		return hash;
	}

	public EIT_SourceOrigin source() {
		return source;
	}

	public String filename() {
		return filename;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		final EIT_Input_HashSourceFile_Triple that = (EIT_Input_HashSourceFile_Triple) obj;
		return Objects.equals(this.hash, that.hash) &&
				Objects.equals(this.source, that.source) &&
				Objects.equals(this.filename, that.filename);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hash, source, filename);
	}

	@Override
	public String toString() {
		return "EIT_Input_HashSourceFile_Triple[" +
				"hash=" + hash + ", " +
				"source=" + source + ", " +
				"filename=" + filename + ']';
	}

}
