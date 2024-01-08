package tripleo.elijah.nextgen.outputtree;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.nextgen.outputstatement.EG_SequenceStatement;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class _EOT_OutputTree__Utils {

	static @NotNull List<EG_Statement> _extractStatementSequenceFromAllOutputFiles(final @NotNull Collection<tripleo.elijah.nextgen.outputtree.EOT_OutputFile> tt) {
		List<EG_Statement> list2 = new ArrayList<>();
		for (tripleo.elijah.nextgen.outputtree.EOT_OutputFile of1 : tt) {
			list2.addAll(_extractStatementSequenceFromOutputFile(of1));
		}
		return list2;
	}

	private static @NotNull List<EG_Statement> _extractStatementSequenceFromOutputFile(final @NotNull EOT_OutputFile of1) {
		List<EG_Statement> llll = new ArrayList<EG_Statement>();

		final EG_Statement sequence = of1.getStatementSequence();

		if (sequence instanceof EG_SequenceStatement) {
			EG_SequenceStatement seqst = (EG_SequenceStatement) sequence;
			llll.addAll(seqst._list());
		} else {
			llll.add(sequence);
		}

		return llll;
	}
}
