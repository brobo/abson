package tech.magnitude.abson;

/**
 * Defines a set of constants used by Abson.
 * @author blacksmithgu
 *
 */
public class AbsonConstants {
	/**
	 * Denotes the start of a JSON Object.
	 */
	public static final char OPENING_BRACKET = '{';
	
	/**
	 * Denotes the end of a JSON Object.
	 */
	public static final char CLOSING_BRACKET = '}';
	
	/**
	 * Denotes the start of a JSON array.
	 */
	public static final char OPENING_ARRAY = '[';
	
	/**
	 * Denotes the end of a JSON array.
	 */
	public static final char CLOSING_ARRAY = ']';
	
	/**
	 * Denotes the start and end of a string literal.
	 */
	public static final char STRING_DELIMITER = '"';
	
	/**
	 * Denotes the character used to seperate entries in a JSON object.
	 */
	public static final char ENTRY_SEPERATOR = ',';
	
	/**
	 * Denotes the character used to seperate keys and values in a JSON object.
	 */
	public static final char KEY_VALUE_SEPERATOR = ':';
	
	/**
	 * Denotes the character used to escape special characters in JSON literals.
	 */
	public static final char ESCAPE_CHARACTER = '\\';
	
	/**
	 * Denotes the character used to denote the start of a binary literal in JSON.
	 */
	public static final char BINARY_OPENER = '#';
}
