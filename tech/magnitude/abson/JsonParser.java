package tech.magnitude.abson;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import tech.magnitude.abson.elements.AbsonArray;
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
	
	public JsonParser(Reader read) {
		reader = read;
	}
	
	public JsonParser(String string) {
		reader = new StringReader(string);
	}
	
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
				if(ch != ',')
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
	
	public Absonifyable readAbsonifyableValue() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		// This is an interesting function; we check the first character to determine what our course of action will be.
		char nextChar = readChar();
		markChar();
		
		switch(nextChar) {
		case '{': // Abson Object
			return readObject();
		case '[': // Abson array
			return readArray();
		case '"': // Abson string
			return new AbsonString(readStringLiteral());
		default: // Some other constant thing.
			return JsonUtil.assignToAbsonifyable(readToDelimiter().trim());
		}
	}
	
	public String readToDelimiter() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		StringBuffer res = new StringBuffer();
		
		char curr = 0x00;
		while((curr = readChar()) != AbsonConstants.ENTRY_SEPERATOR && curr != AbsonConstants.CLOSING_BRACKET)
			res.append(curr);
		
		if(curr == AbsonConstants.CLOSING_BRACKET)
			markChar();
		
		return res.toString();
	}
	
	public String readStringLiteral() throws IOException, AbsonParseException {
		eliminateWhitespace();
		
		if(readChar() != AbsonConstants.STRING_DELIMITER) {
			markChar();
			throw new AbsonParseException("The next character in the reader did not imply a string literal.");
		}
		
		StringBuilder buffer = new StringBuilder();
		char curr = 0x00, last = 0x00;
		while((curr = readChar()) != AbsonConstants.STRING_DELIMITER && last != '\\') {
			buffer.append(curr);
			last = curr;
		}
		
		return buffer.toString();
	}
	
	public void eliminateWhitespace() throws IOException {
		while(Character.isWhitespace(readChar()));
		
		markChar();
	}
	
	public char readChar() throws IOException {
		if(charMarked) {
			charMarked = false;
			return backupChar;
		}
		
		int charInt = reader.read();
		if(charInt == -1) throw new EOFException("The end of the file was reached.");
		return backupChar = (char) charInt;
	}
	
	public void markChar() {
		charMarked = true;
	}
}
