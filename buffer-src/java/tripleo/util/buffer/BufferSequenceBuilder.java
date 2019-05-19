/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.util.buffer;

import java.util.HashMap;
import java.util.Map;

import tripleo.elijah.comp.GenBuffer;
import tripleo.elijah.gen.nodes.ArgumentNode;

/**
 * @author Tripleo(ecl)
 *
 */
public class BufferSequenceBuilder {
	
	private String _built;
	private Map<String, Buffer> parts;
	private Map<Integer, String> part_names;

	public BufferSequenceBuilder(int i) {
		// TODO Auto-generated constructor stub
		parts = new HashMap<String, Buffer>(i);
		part_names = new HashMap<Integer, String>(i);
	}

	public BufferSequenceBuilder(int i, Iterable<ArgumentNode> argumentsIterator, Transform transform1,
			XX comma, GenBuffer gbn) {
		// TODO Auto-generated constructor stub
		parts = new HashMap<String, Buffer>(i);
		part_names = new HashMap<Integer, String>(i);
		//
		Bufbldr bb=new Bufbldr(gbn);
		for (ArgumentNode an : argumentsIterator) {
			transform1.transform(an, bb);
			bb.append(comma.getText());
		}
		_built = bb.getText();
	}

	public BufferSequenceBuilder named(String string) {
		// TODO Auto-generated method stub
		part_names.put(part_names.size(), string);
		return this;
	}

	public BufferSequenceBuilder semieol() {
		String key = "klkkl";
		// TODO Auto-generated method stub
		parts.put(key , new DefaultBuffer(";\n"));
		part_names.put(part_names.size(), key);
		return this;
	}

	public void set(String part_name, String setTo) {
		// TODO Auto-generated method stub
		parts.put(part_name, new DefaultBuffer(setTo));
	}

	public void set(String part_name, String setTo, char sep) {
		// TODO Auto-generated method stub
		parts.put(part_name, new DefaultBuffer(setTo+sep));		
	}

	public String build() {
		// TODO Auto-generated method stub
		if (_built != null)
			return _built;
		//
		StringBuffer sb = new StringBuffer();
		for (Buffer element : parts.values()) {
			sb.append(element.getText());
		}
		return sb.toString();
	}

	public void set(String part_name, String setTo, XX sep) {
		// TODO fix septoString
		parts.put(part_name, new DefaultBuffer(setTo+sep.toString()));
	}

	public void set(String part_name, Buffer sb2) {
		// TODO Auto-generated method stub
		parts.put(part_name, sb2);
	}

}
