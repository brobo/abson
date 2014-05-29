package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class Abson32Integer implements Absonifyable {

	int value;
	
	public Abson32Integer(int value) {
		this.value = value;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt32(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x10;
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(toJson(settings));
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return Integer.toString(value);
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	public int getValue() {
		return value;
	}
	
	public static Abson32Integer fromJson(String json) {
		return new Abson32Integer(Integer.parseInt(json));
	}
	
	public static Abson32Integer fromBson(InputStream stream) throws IOException {
		byte[] bytes = new byte[4];
		stream.read(bytes);
		return new Abson32Integer(BsonUtil.fromBinaryInt32(bytes));
	}
}