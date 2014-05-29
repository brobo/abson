package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
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
	
	public String toString() {
		return toJson();
	}

	@Override
	public void toBson(OutputStream stream) throws IOException {
		return;
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

}
