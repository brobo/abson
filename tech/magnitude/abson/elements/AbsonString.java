package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;
import tech.magnitude.abson.PrintUtil;

public class AbsonString implements Absonifyable {
	
	protected String string;
	
	public static final String[] escapable = {"\"", "\\\\", "/", "\b", "\f", "\n", "\r", "\t"};
	public static final String[] escaped = {"\\\"", "\\\\", "\\/", "\\b", "\\f", "\\n", "\\r", "\\t"};
	
	public AbsonString(String value) {
		this.string = value;
	}
	
	@Override
	public byte getBsonPrefix() {
		return 0x02;
	}

	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryString(string));
	}

	@Override
	public void toJson(Writer writer, JsonPrintSettings settings) throws IOException {
		String res = string;
		for (int i=0; i<escapable.length; i++) {
			res = res.replaceAll(escapable[i], escaped[i]);
		}
		
		writer.write("\"");
		writer.write(res);
		writer.write("\"");
	}
	
	public String getValue() {
		return string;
	}
	
	public static AbsonString fromJson(String json) {
		String res = json;
		for (int i=0; i<escapable.length; i++) {
			res = res.replaceAll("\\" + escapable[i], escapable[i]);
		}
		return new AbsonString(res);
	}
	
	public static AbsonString fromBson(InputStream stream) throws IOException {
		byte[] lengthArr = new byte[4];
		stream.read(lengthArr);
		int length = BsonUtil.fromBinaryInt32(lengthArr);
		
		StringBuilder builder = new StringBuilder(length);
		for (int i=1; i<length; i++) { //Ignore the 0x00
			builder.append((char) stream.read());
		}
		
		stream.read();
		return new AbsonString(builder.toString());
	}

	public String toJson() {
		return toJson(JsonPrintSettings.DEFAULT);
	}
	
	public String toJson(JsonPrintSettings settings) {
		return PrintUtil.getString(this, settings);
	}
	
	@Override
	public String toString() {
		return toJson();
	}
}
