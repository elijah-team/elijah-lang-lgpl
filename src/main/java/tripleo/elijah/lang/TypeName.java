package tripleo.elijah.lang;

/**
 * Created 8/16/20 2:16 AM
 */
public interface TypeName {
	default boolean isNull() {return false;}

	void setContext(Context context);

	Context getContext();

	public enum Type {
		NORMAL, GENERIC, TYPE_OF, FUNCTION
	}

	void type(TypeModifiers typeModifiers);
	Type kindOfType();
}
