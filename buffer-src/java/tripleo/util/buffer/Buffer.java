package tripleo.util.buffer;

public interface Buffer {

	void append(String string);

	void append_s(String string);

	void append_cb(String string);

	void decr_i();
	
	void incr_i();
	
	void append_nl_i(String string);

	void append_nl(String string);

	void append_ln(String string);

	String getText();

}