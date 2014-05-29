package tech.magnitude.abson.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class Test {
	public static void main(String[] args) throws Exception {
		if (true) {
			AbsonObject foo = new AbsonObject();
			foo.put("name", "Colby Brown");
			foo.put("age", 17);
			AbsonArray books = new AbsonArray();
			books.add("1984");
			books.add("A Clockwork Orange");
			books.add("Moby Dick");
			foo.put("books", books);
			foo.put("isAwesome", true);
			foo.put("nothing");
			System.out.println(foo.toJson());
			System.out.println(AbsonObject.fromJson(foo.toJson()).toJson());
			System.out.println(foo.equals(AbsonObject.fromJson(foo.toJson())));
			System.out.println(foo.equals(AbsonObject.fromBson(foo.toBson())));
			String old = foo.toJson();
			foo.put("SomethingNew", "NEW!");
			System.out.println(foo.equals(AbsonObject.fromJson(old)));
		}
		
		if (false) {
			AbsonObject hello = new AbsonObject();
			hello.put("hello", "world");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			hello.toBson(stream);
			System.out.println(hello.toJson());
			System.out.println(BsonUtil.byteString(stream.toByteArray()));
			ByteArrayInputStream flip = new ByteArrayInputStream(stream.toByteArray());
			AbsonObject goodbye = AbsonObject.fromBson(flip);
			System.out.println(goodbye.toJson());
		}
		
		if (false) {
			AbsonObject bson = new AbsonObject();
			AbsonArray awesome = new AbsonArray();
			awesome.add("awesome");
			awesome.add(5.05);
			awesome.add(1986);
			bson.put("BSON", awesome);
			System.out.println(bson.toJson());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bson.toBson(stream);
			System.out.println(BsonUtil.byteString(stream));
			ByteArrayInputStream flip = new ByteArrayInputStream(stream.toByteArray());
			AbsonObject goodbye = AbsonObject.fromBson(flip);
			System.out.println(goodbye.toJson());
		}
	}
}
