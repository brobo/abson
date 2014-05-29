package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class AbsonNull implements Absonifyable {

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		writer.write(toJson(settings));
	}
	
	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return "null";
	}
	
	@Override
	public String toString() {
		return toJson();
	}

	@Override
	public void toBson(OutputStream stream) throws IOException {
		return;
	}
	
	public byte[] toBson() {
		try {
			return BsonUtil.getArray(this);
		} catch(Exception ex) {
			return null; // Shouldn't happen.
		}
	}

	@Override
	public byte getBsonPrefix() {
		return 0x0a;
	}
	
	public static AbsonNull fromBson(InputStream stream) throws IOException {
		return new AbsonNull();
	}
	
	public static AbsonNull fromJson(String json) {
		return new AbsonNull();
	}
	
	public Object getValue() {
		return null;
	}

}
