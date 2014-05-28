package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.OutputStream;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;

public class AbsonString implements Absonifyable {
	
	String string;
	
	private static final String[] escapable = {"\"", "\\\\", "/", "\b", "\f", "\n", "\r", "\t"};
	private static final String[] escaped = {"\\\"", "\\\\", "\\/", "\\b", "\\f", "\\n", "\\r", "\\t"};
	
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
	public String toJson() {
		String res = string;
		for (int i=0; i<escapable.length; i++) {
			res = res.replaceAll(escapable[i], escaped[i]);
		}
		return "\"" + res + "\"";
	}
	
	public String getValue() {
		return string;
	}
	
}
