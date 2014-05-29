package tech.magnitude.abson;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import tech.magnitude.abson.elements.Abson32Integer;
import tech.magnitude.abson.elements.Abson64Integer;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonBoolean;
import tech.magnitude.abson.elements.AbsonFloatingPoint;
import tech.magnitude.abson.elements.AbsonNull;
import tech.magnitude.abson.elements.AbsonObject;
import tech.magnitude.abson.elements.AbsonString;

public class JsonUtil {
	
	private static final String FLOATING_REGEX =
			"^-?[0-9]+.[0-9]+([eE][+-]?[0-9]+)?$";
	private static final String INTEGER_REGEX =
			"^-?[0-9]+([eE][+-]?[0-9]+)?$";
	
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
	
	public static String repeat(String base, int amount) {
		StringBuilder res = new StringBuilder();
		for(int x = 0; x < amount; x++)
			res.append(base);
		
		return res.toString();
	}
	
	public static String getString(Absonifyable object, JsonPrintSettings settings) {
		
		try {
			StringWriter writer = new StringWriter();
			object.toJson(writer, settings);
			
			return writer.toString();
		} catch(Exception ex) {
			return null; // TODO FIX THIS
		}
	}
	
	public static void indent(Writer writer, int spaces) throws IOException {
		for(int x = 0; x < spaces; x++)
			writer.write(" ");
	}
}
