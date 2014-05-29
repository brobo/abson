package tech.magnitude.abson;

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
	
	public static Absonifyable assignToAbsonifyable(String token) {
		
		if (token.startsWith("\"") && token.endsWith("\"")) {
			return AbsonString.fromJson(token);
		}
		
		if (token.startsWith("[")) {
			return AbsonArray.fromJson(token);
		}
		
		if (token.startsWith("{")) {
			return AbsonObject.fromJson(token);
		}
		
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
}
