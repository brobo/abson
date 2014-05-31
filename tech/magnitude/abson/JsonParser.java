package tech.magnitude.abson;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonBinary;
import tech.magnitude.abson.elements.AbsonObject;
import tech.magnitude.abson.elements.AbsonString;

/**
 * Implements a Json Parser which parses standardized JSON.
 * @author blacksmithgu
 */
public class JsonParser {
	private Reader reader;
	private char backupChar;
	private boolean charMarked = false;
	
	/**
	 * Constructs a JsonParser which will read from a specified reader.
	 * @param read The reader to read from.
	 */
	public JsonParser(Reader read) {
		reader = read;
	}
	
	/**
	 * Constructs a JsonParser which will read from a the specified string.
	 * @param string The string to read from.
	 */
	public JsonParser(String string) {
		reader = new StringReader(string);
	}
	
	/**
	 * Reads an AbsonObject from the provided reader or string.
	 * @return The AbsonObject which was read.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 * @throws AbsonParseException Thrown if the JSON is incorrectly formatted.
	 */
	public AbsonObject readObject() throws IOException, AbsonParseException {
		// First, find the opening bracket.
		eliminateWhitespace();
		if(readChar() != AbsonConstants.OPENING_BRACKET) throw new AbsonParseException("The next object is not an AbsonObject.");
		
		AbsonObject res = new AbsonObject();
		
		try {
			eliminateWhitespace();
			
			char ch = 0;
			while((ch = readChar()) != AbsonConstants.CLOSING_BRACKET) {
				if(ch != ',')
					markChar();
				
				eliminateWhitespace();
				
				String key = readStringLiteral();
				
				char spacer = readChar();
				if(spacer != AbsonConstants.KEY_VALUE_SEPERATOR)
					throw new AbsonParseException("The key was not followed by the key value seperator.");
				
				eliminateWhitespace();
				
				Absonifyable value = readAbsonifyableValue();
				res.put(key, value);
				
				eliminateWhitespace();
			}
		} catch(EOFException ex) {
			throw new AbsonParseException("The JSON input was malformed as the end of the reader was reached.");
		} catch(IOException ex) {
			throw ex;
		}
		
		return res;
	}
	
	/**
	 * Reads an AbsonArray from the provided reader or string.
	 * @return The AbsonArray which was read.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 * @throws AbsonParseException Thrown if the JSON is incorrectly formatted.
	 */
	public AbsonArray readArray() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		if(readChar() != AbsonConstants.OPENING_ARRAY) {
			markChar();
			throw new AbsonParseException("The next JSON object is not an array.");
		}
		
		AbsonArray res = new AbsonArray();
		
		try {
			eliminateWhitespace();
			
			char ch = 0x00;
			while((ch = readChar()) != AbsonConstants.CLOSING_ARRAY) {
				if(ch != AbsonConstants.ENTRY_SEPERATOR)
					markChar();
				
				eliminateWhitespace();
				
				Absonifyable object = readAbsonifyableValue();
				res.add(object);
				
				eliminateWhitespace();
			}
			
		} catch(EOFException ex) {
			throw new AbsonParseException("The JSON input was malformed as the end of the reader was reached.");
		} catch(IOException ex) {
			throw ex;
		}
		
		return res;
	}
	
	/**
	 * Reads a binary literal from the provided reader or string.
	 * @return The binary literal that was read.
	 * @throws IOException Thrown if an IO exception occurs on the provided reader.
	 * @throws AbsonParseException Thrown if the following object is not a binary literal or is improperly formatted.
	 */
	public AbsonBinary readBinary() throws IOException, AbsonParseException {
		eliminateWhitespace();
		if(readChar() != AbsonConstants.BINARY_OPENER) {
			markChar();
			throw new AbsonParseException("The next JSON object is not a binary literal.");
		}
		
		if(readChar() != AbsonConstants.STRING_DELIMITER) {
			markChar();
			throw new AbsonParseException("The next JSON object does not suggest a binary literal.");
		}
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			eliminateWhitespace();
			
			char test = 0x00;
			while((test = readChar()) != AbsonConstants.STRING_DELIMITER) {
				markChar();
				buffer.write(JsonUtil.getBase64Bytes(readBinaryPart(4)));
			}
			
		} catch(EOFException ex) {
			throw new AbsonParseException("The JSON import was malformed as the end of the reader was reached.");
		} catch(IOException ex) {
			throw ex;
		}
		
		return new AbsonBinary(buffer.toByteArray());
	}
	
	/**
	 * Reads a generic JSON value from the provided reader or string.
	 * @return The generic JSON value which was obtained - will be one of the abson.elements.* objects.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 * @throws AbsonParseException Thrown if an Absonifyable object could not be deduced from the input.
	 */
	public Absonifyable readAbsonifyableValue() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		// This is an interesting function; we check the first character to determine what our course of action will be.
		char nextChar = readChar();
		markChar();
		
		switch(nextChar) {
		case AbsonConstants.OPENING_BRACKET: // Abson Object
			return readObject();
		case AbsonConstants.OPENING_ARRAY: // Abson array
			return readArray();
		case AbsonConstants.STRING_DELIMITER: // Abson string
			return new AbsonString(readStringLiteral());
		case AbsonConstants.BINARY_OPENER: // Abson Binary data (converted to base 64 and so on).
			return readBinary();
		default: // Some other constant thing.
			return JsonUtil.assignToAbsonifyable(readToDelimiter().trim());
		}
	}
	
	/**
	 * Reads until a delimiter (one of ", }, or }) is reached, and returns what was read.
	 * @return A String representing what was read.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 * @throws AbsonParseException Thrown if an Absonifyable object could not be deduced from the input.
	 */
	public String readToDelimiter() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		StringBuffer res = new StringBuffer();
		
		char curr = 0x00;
		while((curr = readChar()) != AbsonConstants.ENTRY_SEPERATOR && curr != AbsonConstants.CLOSING_BRACKET && curr != AbsonConstants.CLOSING_ARRAY)
			res.append(curr);
		
		if(curr == AbsonConstants.CLOSING_BRACKET || curr == AbsonConstants.CLOSING_ARRAY)
			markChar();

		return res.toString();
	}
	
	/**
	 * Reads a string literal from the provided reader or string.
	 * @return The string literal that was read.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 * @throws AbsonParseException Thrown if the next JSON object was not a string literal or was improperly formatted.
	 */
	public String readStringLiteral() throws IOException, AbsonParseException {
		if(readChar() != AbsonConstants.STRING_DELIMITER) {
			markChar();
			throw new AbsonParseException("The next character in the reader did not imply a string literal.");
		}
		
		StringBuilder buffer = new StringBuilder();
		char curr = 0x00, last = 0x00;
		while((curr = readChar()) != AbsonConstants.STRING_DELIMITER && last != AbsonConstants.ESCAPE_CHARACTER) {
			if(last == AbsonConstants.ESCAPE_CHARACTER && curr == AbsonConstants.STRING_DELIMITER)
				buffer.deleteCharAt(buffer.length() - 1);
			
			buffer.append(curr);
			last = curr;
		}
		
		return buffer.toString();
	}
	
	/**
	 * Reads a portion of a Base64 binary literal to be converted back to binary.
	 * @param chars The amount of characters to read; usually 3 (as Base64 is being used).
	 * @return The characters which were read.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 */
	public char[] readBinaryPart(int chars) throws IOException {
		StringBuilder buffer = new StringBuilder();
		char curr = 0x00;
		while(buffer.length() < chars && (curr = readChar()) != AbsonConstants.STRING_DELIMITER) {
			buffer.append(curr);
		}
		
		return buffer.toString().toCharArray();
	}
	
	/**
	 * Eliminates whitespace from the reader, such that the head of the stream is now the
	 * first non-whitespace character.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 */
	public void eliminateWhitespace() throws IOException {
		while(Character.isWhitespace(readChar()));
		
		markChar();
	}
	
	/**
	 * Reads a character from the provided reader; if a character has been marked,
	 * this function will return that character rather than actually read from the
	 * stream.
	 * @return The read character.
	 * @throws IOException Thrown if an IO error occurs on the provided reader.
	 */
	public char readChar() throws IOException {
		if(charMarked) {
			charMarked = false;
			return backupChar;
		}
		
		int charInt = reader.read();
		if(charInt == -1) throw new EOFException("The end of the file was reached.");
		return backupChar = (char) charInt;
	}
	
	/**
	 * Marks the last read character, such that the next invocation of readChar() will
	 * return the last read character instead of the next character in the Reader.
	 */
	public void markChar() {
		charMarked = true;
	}
	
	/**
	 * Obtains whether or not a character has been marked for re-reading.
	 * @return Whether or not a character has been marked for re-reading.
	 */
	public boolean isCharMarked() {
		return charMarked;
	}
	
	/**
	 * Returns the marked character as by markChar(), or 0x00 if no character has been marked.
	 * @return The marked character as by markChar().
	 */
	public char getMarkedChar() {
		return backupChar;
	}
}
