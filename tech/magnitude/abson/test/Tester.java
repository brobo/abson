package tech.magnitude.abson.test;

import java.io.InputStreamReader;

import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonParser;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.elements.AbsonObject;

public class Tester {
	public static void it(AbsonObject target) throws Exception {
		String json = target.toJson(JsonPrintSettings.PRETTY_DEFAULT);
		System.out.println("JSON:\n" + json);
		byte[] bson = target.toBson();
		System.out.println("BSON:\n" + BsonUtil.byteString(bson));
		String json_from_json = AbsonObject.fromJson(json).toJson();
		System.out.println("From json:\n" + json_from_json);
		String json_from_bson = AbsonObject.fromBson(bson).toJson();
		System.out.println("From bson:\n" + json_from_bson);
	}
	
	public static void main(String[] args) throws Exception {
		InputStreamReader input = new InputStreamReader(System.in);
		while(true) {
			JsonParser parser = new JsonParser(input);
			
			AbsonObject obj = parser.readObject();
			it(obj);
		}
	}
}
