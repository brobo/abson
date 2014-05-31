package tech.magnitude.abson;

/**
 * Represents an error caused by invalid JSON or BSON formatting.
 * @author blacksmithgu
 *
 */
public class AbsonParseException extends Exception {
	private static final long serialVersionUID = -3656028812472768657L;

	/**
	 * Creates an AbsonParseException with the specified message.
	 * @param message The message explaining the cause of this exception.
	 */
	public AbsonParseException(String message) {
		super(message);
	}
}
