package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class AbsonBoolean implements Absonifyable {

	protected boolean value;
	
	public AbsonBoolean(boolean val) {
		this.value = val;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryBoolean(value));
	}

	@Override
	public byte getBsonPrefix() {
		return 0x08;
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(toJson(settings));
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return value ? "true" : "false";
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	public boolean getValue() {
		return value;
	}
	
	public static AbsonBoolean fromJson(String json) {
		return new AbsonBoolean(json.equals("true"));
	}
	
	public static AbsonBoolean fromBson(InputStream stream) throws IOException {
		return new AbsonBoolean(stream.read() != 0x00);
	}

}
