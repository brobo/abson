package tech.magnitude.abson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import tech.magnitude.abson.elements.Abson32Integer;
import tech.magnitude.abson.elements.Abson64Integer;
import tech.magnitude.abson.elements.AbsonBoolean;
import tech.magnitude.abson.elements.AbsonFloatingPoint;
import tech.magnitude.abson.elements.AbsonNull;
import tech.magnitude.abson.elements.AbsonObject;

public class JsonUtil {
	
	private static final String FLOATING_REGEX =
			"^-?[0-9]+.[0-9]+([eE][+-]?[0-9]+)?$";
	
	private static final String INTEGER_REGEX =
			"^-?[0-9]+([eE][+-]?[0-9]+)?$";
	
	/**
	 * Attempts to deduce an Absonifyable object which does not have special syntax
	 * based on the provided token.
	 * @param token The token from which to deduce an Absonifyable object.
	 * @return The Absonifyable object which was deduced.
	 * @throws AbsonParseException Thrown if an object could not be deduced.
	 */
	public static Absonifyable assignToAbsonifyable(String token) throws AbsonParseException {
		if (token.equals("true") || token.equals("false")) {
			return AbsonBoolean.fromJson(token);
		}
		
		if (token.equals("null")) {
			return AbsonNull.fromJson(token);
		}
		
		if (token.matches(FLOATING_REGEX)) {
			return AbsonFloatingPoint.fromJson(token);
		}
		
		if (token.matches(INTEGER_REGEX)) {
			long temp = Long.parseLong(token);
			if (Integer.MIN_VALUE <= temp && temp <= Integer.MAX_VALUE) {
				return Abson32Integer.fromJson(token);
			} else {
				return Abson64Integer.fromJson(token);
			}
		}
		
		return new AbsonObject();
		
	}
	
	/**
	 * Repeats a specified String <code>amount</code> times.
	 * @param base The string to repeat.
	 * @param amount The amount of times the string should be repeated.
	 * @return A string which consists of the base string repeated <code>amount</code> times.
	 */
	public static String repeat(String base, int amount) {
		StringBuilder res = new StringBuilder();
		for(int x = 0; x < amount; x++)
			res.append(base);
		
		return res.toString();
	}
	
	/**
	 * Obtains the String representation of an Absonifyable object, according to its
	 * toJson(Writer, JsonPrintSettings) method and the provided settings.
	 * @param object The object to find the String representation fo.
	 * @param settings The settings to use when obtaining the JSON representation.
	 * @return The JSON representation of the passed object.
	 */
	public static String getString(Absonifyable object, JsonPrintSettings settings) {
		try {
			StringWriter writer = new StringWriter();
			object.toJson(writer, settings);
			
			return writer.toString();
		} catch(Exception ex) {
			return null; // TODO FIX THIS
		}
	}
	
	/**
	 * Writes the specified number of spaces to the specified writer.
	 * @param writer The writer to write to.
	 * @param spaces The amount of spaces to write.
	 * @throws IOException Thrown if an IO error occurs while writing to the specified writer.
	 */
	public static void indent(Writer writer, int spaces) throws IOException {
		for(int x = 0; x < spaces; x++)
			writer.write(" ");
	}
	
	/**
	 * A string containing all of the Base 64 characters, with their index being their value.
	 */
	public static String Base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	/**
	 * A reverse Base64 lookup which maps character to value. 
	 */
	public static byte[] Base64Lookup = new byte[123];
	
	/**
	 * The binary value 11111111, or 0xff.
	 */
	public static byte BYTE_MASK = (byte) -1;
	
	static {
		for(int x = 0; x < Base64Chars.length(); x++)
			Base64Lookup[Base64Chars.charAt(x)] = (byte) x;
	}
	
	public static char getBase64Char(int num, int lower) {
		return Base64Chars.charAt(((num >> lower) & 63)); 
	}
	
	public static void writeBase64(Writer writer, byte[] arr) throws IOException {
		for(int offset = 0; offset < arr.length; offset += 3) {
			int available = Math.min(arr.length, offset + 3) - offset;
			
			// Default case, available is 3:
			if(available == 3) {
				int temp = arr[offset] << 16 & 0xffffff | arr[offset + 1] << 8 & 0xffff | arr[offset + 2] & 0xff;
				writer.write(getBase64Char(temp, 18));
				writer.write(getBase64Char(temp, 12));
				writer.write(getBase64Char(temp, 6));
				writer.write(getBase64Char(temp, 0));
				
				System.out.printf("%24s%n", Integer.toBinaryString(temp));
			} else if(available == 2) {
				int temp = arr[offset] << 16 & 0xffffff | arr[offset + 1] << 8 & 0xffff;
				writer.write(getBase64Char(temp, 18));
				writer.write(getBase64Char(temp, 12));
				writer.write(getBase64Char(temp, 6));
				writer.write('=');
				
				System.out.printf("%24s%n", Integer.toBinaryString(temp));
			} else if(available == 1) { 
				int temp = arr[offset] << 16 & 0xffffff;
				writer.write(getBase64Char(temp, 18));
				writer.write(getBase64Char(temp, 12));
				writer.write('=');
				writer.write('=');
				
				System.out.printf("%24s%n", Integer.toBinaryString(temp));
			}
		}
	}
	
	public static byte[] getBase64Bytes(char[] chars) throws IOException {
		int res = 0;
		
		int read = 0;
		for(read = 0; read < chars.length; read++) {
			if(chars[read] == '=') break;
			res = (res | Base64Lookup[chars[read]]) << 6;
		}
		res >>>= 6;
		
		System.out.printf("%24s%n", Integer.toBinaryString(res));
		
		switch(read) {
		case 2:
			return new byte[] { (byte) (res >>> 4 & BYTE_MASK) };
		case 3:
			return new byte[] { (byte) (res >>> 10 & BYTE_MASK), (byte) (res >>> 2 & BYTE_MASK) };
		case 4:
			return new byte[] { (byte) (res >>> 16 & BYTE_MASK), (byte) (res >>> 8 & BYTE_MASK), (byte) (res & BYTE_MASK) };
		}
		
		return null; // I have no idea why this would ever happen.
	}
}
