/**
 * 
 */
package tripleo.util.buffer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author olu
 *
 */
public class BufferSequenceBuilder {

	private Map<String, Buffer> parts;
	private Map<Integer, String> part_names;

	public BufferSequenceBuilder(int i) {
		// TODO Auto-generated constructor stub
		parts = new HashMap<String, Buffer>(i);
		part_names = new HashMap<Integer, String>(i);
	}

	public BufferSequenceBuilder named(String string) {
		// TODO Auto-generated method stub
		part_names.put(part_names.size(), string);
		return this;
	}

	public BufferSequenceBuilder semieol() {
		// TODO Auto-generated method stub
		parts.put(key, ";\n");
		part_names.put(part_names.size(), "");
		return this;
	}

	public void set(String part_name, String setTo) {
		// TODO Auto-generated method stub
		parts.put(part_name, new StringBuffer(setTo));
	}

	public void set(String part_name, String setTo, char sep) {
		// TODO Auto-generated method stub
		parts.put(part_name, new StringBuffer(setTo+sep));
		
	}

	public String build() {
		// TODO Auto-generated method stub
		return null;
	}

}
