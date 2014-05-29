package tech.magnitude.abson.elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigInteger;

import tech.magnitude.abson.Absonifyable;
import tech.magnitude.abson.BsonUtil;
import tech.magnitude.abson.JsonPrintSettings;

public class Abson64Integer implements Absonifyable, AbsonNumber<Long> {
	
	protected long value;

	public Abson64Integer(long value) {
		this.value = value;
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
	
	public Long getValue() {
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

	@Override
	public int getIntValue() {
		return getValue().intValue();
	}

	@Override
	public long getLongValue() {
		return getValue();
	}

	@Override
	public BigInteger getBigIntegerValue() {
		return BigInteger.valueOf(getLongValue());
	}
	
	@Override
	public double getDoubleValue() {
		return getValue();
	}
}
