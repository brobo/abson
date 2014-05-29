package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class Abson64Integer implements Absonifyable {
	long value;

	public Abson64Integer(long value) {
		this.value = value;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt64(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x12;
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(toJson(settings));
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return Long.toString(value);
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	public long getValue() {
		return value;
	}
	
	public static Abson64Integer fromJson(String json) {
		return new Abson64Integer(Long.parseLong(json));
	}
	
	public static Abson64Integer fromBson(InputStream stream) throws IOException {
		byte[] bytes = new byte[8];
		stream.read(bytes);
		return new Abson64Integer(BsonUtil.fromBinaryInt64(bytes));
	}
}
