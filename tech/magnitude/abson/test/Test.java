package tech.magnitude.abson.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class Test {
	public static void main(String[] args) throws Exception {
		if (false) {
			AbsonObject foo = new AbsonObject();
			foo.put("name", "Colby Brown");
			foo.put("age", 17);
			AbsonArray books = new AbsonArray();
			books.add("1984");
			books.add("A Clockwork Orange");
			books.add("Moby Dick");
			foo.put("books", books);
			//foo.put("isAwesome", true);
			System.out.println(foo.toJson());
			System.out.println(AbsonObject.fromJson(foo.toJson()).toJson());
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
		
		if (true) {
			System.out.println(TestUser.COLBY.decompose().toJson());
			System.out.println(TestUser.compose(TestUser.TRES.decompose()).decompose().toJson());
		}
	}
}
