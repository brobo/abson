package tech.magnitude.abson;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class PrintUtil {
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
