package tech.magnitude.abson.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class JsonTest {
	public static void main(String[] args) throws Exception {
		AbsonObject obj = new AbsonObject();
		obj.put("name", "Bobby Tables");
		obj.put("age", 24);
		obj.put("occupation", "Database Engineer");
		obj.put("favorite_decimal", 13.4);
		
		AbsonArray arr = new AbsonArray();
		arr.add(34);
		arr.add(35);
		arr.add("why is a string here? Nobody knows.");
		arr.add(13.4);
		arr.add("nope");
		arr.add("what is going on here?");
		
		obj.put("favorite_numbers", arr);
		
		AbsonObject rec =  new AbsonObject();
		rec.put("father", "Samuel Tables");
		rec.put("mother", "Amanda Tables");
		obj.put("family", rec);
		
		JsonPrintSettings newPretty = new JsonPrintSettings().setMultiline(true).setWhitespace(true).setRoundFractions(true).setRoundAmount(3);
		
		Writer out = new StringWriter();
		obj.toJson(out, newPretty);
		
		out.flush();
		
		String res = out.toString();
		
		JsonParser parser = new JsonParser(new StringReader(res));
		System.out.println(parser.readObject().toJson(JsonPrintSettings.PRETTY_DEFAULT));
	}
}
