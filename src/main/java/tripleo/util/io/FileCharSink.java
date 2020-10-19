/**
 * 
 */
package tripleo.util.io;

import java.io.FileOutputStream;
import java.io.IOException;
/**
 * @author SBUSER
 *
 * Created 	Dec 9, 2019 at 3:23:57 PM
 */
public class FileCharSink implements CharSink {

	/* (non-Javadoc)
	 * @see tripleo.util.io.CharSink#accept(char)
	 */
	@Override
	public void accept(final char char1) {
		try {
			fos.write(char1);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see tripleo.util.io.CharSink#accept(java.lang.String)
	 */
	@Override
	public void accept(final String string1) {
		try {
			fos.write(string1.getBytes());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	FileOutputStream fos;

	/**
	 * @param fos
	 */
	public FileCharSink(final FileOutputStream fos) {
		super();
		this.fos = fos;
	}

}
