package tech.magnitude.abson.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.TreeMap;

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
		obj.put("obj", (String) null);
		obj.put("binaryStuff", new byte[] { 13, 4, -6, -128, -1, 14, 17, 37, 19, 26, -4, 13, -1, -1, -1, -2, -3 });
		obj.put("lol", "\"what is this\\\"-");
		
		AbsonArray arr = new AbsonArray();
		arr.add(34L);
		arr.add(35L);
		arr.add(34.0);
		arr.add(13.0f);
		arr.add(19038102938129L);
		
		AbsonArray nullable = null;
		
		obj.put("favorite_numbers", arr);
		obj.put("hated_numbers", nullable);
		
		AbsonObject rec =  new AbsonObject();
		rec.put("father", "Samuel Tables");
		rec.put("mother", "Amanda Tables");
		rec.put("sibling1", "Samuel Tables, The Second");
		rec.put("sibling2", "Johann Tables");
		obj.put("family", rec);
		
		JsonPrintSettings newPretty = new JsonPrintSettings().setMultiline(true).setWhitespace(false).setRoundDecimals(true).setRoundAmount(3);
		
		Writer out = new StringWriter();
		obj.toJson(out, newPretty);
		
		out.flush();
		
		String res = out.toString();
		System.out.println(res);
		
		JsonParser parser = new JsonParser(new StringReader(res));
		
		System.out.println("Parser created.");
		
		AbsonObject newObj = parser.readObject();
		System.out.println("Parsed successfully.");
		System.out.println(newObj.getObject("family"));
		System.out.println(newObj.getArray("favorite_numbers"));
		System.out.println(newObj.getArray("hated_numbers"));
		System.out.println(Arrays.toString(newObj.getBinary("binaryStuff")));
		
		TreeMap<String, String> family = new TreeMap<>();
		newObj.getObject("family").fillMap(family, String.class);
		
		System.out.println("As map: " + family);
		System.out.println("As list: " + newObj.getArray("favorite_numbers").asList(Long.class));
	}
}
