package tripleo.util.buffer;

import static org.junit.Assert.*;

import org.junit.Test;

import tripleo.util.buffer.EnclosedBuffer;

public class BufferSequenceBuilderTest {

	@Test
	public void testBuild() {
		fail("Not yet implemented");
	}

	@Test
	public void shouldConcatSimpleStringsCorrectly2() {
		BufferSequenceBuilder bsb = new BufferSequenceBuilder(4).
				named("type").named("name").named("value").semieol();
		bsb.set("type", "int", XX.SPACE);
		bsb.set("name", "i");
		bsb.set("value", "= 3;");
		BufferSequence bsq = bsb.build();
		String s = bsq.getText();
		assertEquals("int i = 3;\n", s);
	}

	@Test
	public void shouldConcatSimpleStringsCorrectly() {
		EnclosedBuffer sb2 = new EnclosedBuffer("(", XX.RPAREN);
		sb2.setPayload("!");
		String s = sb2.getText();
		assertEquals("(!)", s);
	}

}
