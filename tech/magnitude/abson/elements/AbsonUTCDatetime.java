package tech.magnitude.abson.elements;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;

import tech.magnitude.abson.AbsonValue;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class AbsonUTCDatetime implements AbsonValue {
	
	protected long value;
	protected Date realValue;
	
	public AbsonUTCDatetime(long value) {
		this.value = value;
		realValue = new Date(value);
	}
	
	public AbsonUTCDatetime(Date date) {
		value = date.getTime();
		realValue = date;
	}
	
	@Override
	public void toBson(OutputStream stream) throws IOException {
		stream.write(BsonUtil.toBinaryInt64(value));
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
		return 0x09;
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
	
	
	public Date getValue() {
		return realValue;
	}
	
	public static AbsonUTCDatetime fromJson(String json) {
		return new AbsonUTCDatetime(Long.parseLong(json));
	}
	
	public static AbsonUTCDatetime fromBson(InputStream stream) throws IOException {
		byte[] bytes = new byte[8];
		stream.read(bytes);
		return new AbsonUTCDatetime(BsonUtil.fromBinaryInt64(bytes));
	}

	@Override
	public int hashCode() {
		return realValue.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbsonUTCDatetime) {
			return realValue.equals(((AbsonUTCDatetime)obj).realValue);
		}
		return false;
	}
	
	
}
