/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_c;

import tripleo.elijah.stages.gen_generic.DependencyRef;
import tripleo.elijah.stages.gen_generic.IOutputFile;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 9/13/21 10:50 PM
 */
public class OutputFileC implements IOutputFile {
	private final String output;
	private List<DependencyRef> dependencies = new ArrayList<>();
	private List<Buffer> buffers = new ArrayList<>(); // LinkedList??

	public OutputFileC(String aOutput) {
		output = aOutput;
	}

	@Override
	public void putDependencies(List<DependencyRef> aDependencies) {
		dependencies.addAll(aDependencies);
	}

	@Override
	public void putBuffer(Buffer aBuffer) {
		buffers.add(aBuffer);
	}

	@Override
	public String getOutput() {
		StringBuilder sb = new StringBuilder();
		for (DependencyRef dependencyRaw : dependencies) {
			CDependencyRef dependency = (CDependencyRef) dependencyRaw;
			String headerFile = dependency.getHeaderFile();
			String output = String.format("#include \"%s\"\n", headerFile.substring(1));
			sb.append(output);
		}
		for (Buffer buffer : buffers) {
			sb.append(buffer.getText());
		}
		return sb.toString();
	}
}

//
//
//
