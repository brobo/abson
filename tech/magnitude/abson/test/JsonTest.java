package tech.magnitude.abson.test;

import java.io.IOException;
import java.io.OutputStreamWriter;

import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class JsonTest {
	public static void main(String[] args) throws IOException {
		AbsonObject obj = new AbsonObject();
		obj.put("name", "Bobby Tables");
		obj.put("age", 24);
		obj.put("occupation", "Database Engineer");
		obj.put("favorite_decimal", 13.4);
		
		AbsonArray arr = new AbsonArray();
		arr.add(34);
		arr.add(35);
		arr.add("why is a string here? Nobody knows.");
		
		obj.put("favorite_numbers", arr);
		
		AbsonObject rec =  new AbsonObject();
		rec.put("father", "Samuel Tables");
		rec.put("mother", "Amanda Tables");
		obj.put("family", rec);
		
		JsonPrintSettings newPretty = new JsonPrintSettings().setMultiline(true).setWhitespace(true).setRoundFractions(true).setRoundAmount(3);
		
		OutputStreamWriter out = new OutputStreamWriter(System.out);
		obj.toJson(out, newPretty);
		
		out.flush();
	}
}
