package tripleo.elijah.lang;

/**
 * Created 8/16/20 2:16 AM
 */
public interface TypeName {
	public enum Type {
		NORMAL, FUNCTION
	}

	void type(TypeModifiers typeModifiers);
	Type kindOfType();
}
