package tech.magnitude.abson;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import tech.magnitude.abson.elements.Abson32Integer;
import tech.magnitude.abson.elements.Abson64Integer;
import tech.magnitude.abson.elements.AbsonBoolean;
import tech.magnitude.abson.elements.AbsonFloatingPoint;
import tech.magnitude.abson.elements.AbsonNull;

public class JsonUtil {
	
	private static final String FLOATING_REGEX =
			"^-?[0-9]+.[0-9]+([eE][+-]?[0-9]+)?$";
	
	private static final String INTEGER_REGEX =
			"^-?[0-9]+([eE][+-]?[0-9]+)?$";
	
	private static final char[] ESCAPED = { '\\', '"' };
	private static final HashSet<Character> FAST_ESCAPED;
	
	static {
		FAST_ESCAPED = new HashSet<>();
		for(char c : ESCAPED)
			FAST_ESCAPED.add(c);
	}
	
	/**
	 * Returns whether or not the character should be escaped by a backslash if it is to be contained in a string literal.
	 * @param c The character to check.
	 * @return Whether or not the character should be escaped if it is in a string literal.
	 */
	public static boolean shouldEscape(char c) {
		return FAST_ESCAPED.contains(c);
	}
	
	/**
	 * Attempts to deduce an Absonifyable object which does not have special syntax
	 * based on the provided token.
	 * @param token The token from which to deduce an Absonifyable object.
	 * @return The Absonifyable object which was deduced.
	 * @throws AbsonParseException Thrown if an object could not be deduced.
	 */
	public static AbsonValue assignToAbsonifyable(String token) throws AbsonParseException {
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
		throw new AbsonParseException("The token '" + token + "' could not be parsed.");
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
	public static String getString(AbsonValue object, JsonPrintSettings settings) {
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
	
	// NUMERICAL CASTING (COOL STUFF)
	private static interface Caster {
		public Object cast(Number number);
	}
	
	private static HashMap<Class<?>, Caster> numericalCasts;
	
	static {
		numericalCasts = new HashMap<>();
		numericalCasts.put(Integer.class, new Caster() {

			@Override
			public Object cast(Number number) {
				return number.intValue();
			}
			
		});
		
		numericalCasts.put(Long.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number.longValue();
			}
		});
		
		numericalCasts.put(Short.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number.shortValue();
			}
		});
		
		numericalCasts.put(Byte.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number.byteValue();
			}
		});
		
		numericalCasts.put(BigInteger.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return BigInteger.valueOf(number.longValue());
			}
		});
		
		numericalCasts.put(Float.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number.floatValue();
			}
		});
		
		numericalCasts.put(Double.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number.doubleValue();
			}
		});
		
		numericalCasts.put(Number.class, new Caster() {
			@Override
			public Object cast(Number number) {
				return number;
			}
		});
	}
	
	public static <T> T castNumber(Number number, Class<T> target) {
		if(number.getClass().equals(target)) return target.cast(number);
		return target.cast(numericalCasts.get(target).cast(number));
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
			} else if(available == 2) {
				int temp = arr[offset] << 16 & 0xffffff | arr[offset + 1] << 8 & 0xffff;
				writer.write(getBase64Char(temp, 18));
				writer.write(getBase64Char(temp, 12));
				writer.write(getBase64Char(temp, 6));
				writer.write('=');
			} else if(available == 1) { 
				int temp = arr[offset] << 16 & 0xffffff;
				writer.write(getBase64Char(temp, 18));
				writer.write(getBase64Char(temp, 12));
				writer.write('=');
				writer.write('=');
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
