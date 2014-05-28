package tech.magnitude.abson.test;

import java.io.ByteArrayOutputStream;

import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.elements.AbsonArray;
import tech.magnitude.abson.elements.AbsonObject;

public class Test {
	public static void main(String[] args) throws Exception {
		AbsonObject foo = new AbsonObject();
		foo.put("name", "Colby Brown");
		foo.put("age", 17);
		AbsonArray books = new AbsonArray();
		books.add("1984");
		books.add("A Clockwork Orange");
		books.add("Moby Dick");
		foo.put("books", books);
		System.out.println(foo.toJson());
		
		AbsonObject hello = new AbsonObject();
		hello.put("hello", "world");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		hello.toBson(stream);
		System.out.println(hello.toJson());
		System.out.println(BsonUtil.byteString(stream));
		
		AbsonObject bson = new AbsonObject();
		AbsonArray awesome = new AbsonArray();
		awesome.add("awesome");
		awesome.add(5.05);
		awesome.add(1986);
		bson.put("BSON", awesome);
		System.out.println(bson.toJson());
		stream = new ByteArrayOutputStream();
		bson.toBson(stream);
		//The following test still fails.
		System.out.println(BsonUtil.byteString(stream));
	}
}
